/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.EmployeeEntity;
import entity.OutletEntity;
import exception.EmployeeNotFoundException;
import exception.InputDataValidationException;
import exception.InvalidLoginCredentialException;
import exception.OutletNotFoundException;
import exception.UnknownPersistenceException;
import java.util.List;
import java.util.Set;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

/**
 *
 * @author edenyew
 */
@Stateless
public class EmployeeEntitySessionBean implements EmployeeEntitySessionBeanRemote, EmployeeEntitySessionBeanLocal {

    @PersistenceContext(unitName = "MerlionCarRental-ejbPU")
    private EntityManager em;
    
    @EJB
    private OutletEntitySessionBeanLocal outletSessionBeanLocal;
    
    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    
    public EmployeeEntitySessionBean() 
    {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }
    
    
    @Override
    public Long createNewEmployee(EmployeeEntity employeeEntity, Long outletId) throws OutletNotFoundException, EmployeeNotFoundException, UnknownPersistenceException, InputDataValidationException
    {
        Set<ConstraintViolation<EmployeeEntity>>constraintViolations = validator.validate(employeeEntity);
        
        if (constraintViolations.isEmpty())
        {
            try
            {
        
                OutletEntity outlet = outletSessionBeanLocal.retrieveOutletById(outletId);
                em.persist(employeeEntity);

                employeeEntity.setOutlet(outlet);
                outlet.getEmployees().add(employeeEntity);

                em.flush();

                return employeeEntity.getId();
            }
            catch(PersistenceException ex)
            {
                if(ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException"))
                {
                    if(ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException"))
                    {
                        throw new EmployeeNotFoundException();
                    }
                    else
                    {
                        throw new UnknownPersistenceException(ex.getMessage());
                    }
                }
                else
                {
                    throw new UnknownPersistenceException(ex.getMessage());
                }
            }
        }
        else
        {
            throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
        }
    }
    
    @Override
    public List<EmployeeEntity> retrieveAllEmployee() 
    {
        Query query = em.createQuery("Select em FROM EmployeeEntity em");
        
        return query.getResultList();
    }
    
    @Override
    public void updateEmployee(EmployeeEntity employee)
    {
        em.merge(employee);
    }
    
    @Override
    public EmployeeEntity retrieveEmployeeById(Long employeeId)
    {
        EmployeeEntity employee = em.find(EmployeeEntity.class, employeeId);
        return employee;
    }
    
    @Override
    public EmployeeEntity retrieveEmployeeByUsername(String username) throws EmployeeNotFoundException
    {
        Query query = em.createQuery("SELECT em FROM EmployeeEntity em WHERE em.username = :inUsername");
        query.setParameter("inUsername", username);
        
        try
        {
            return (EmployeeEntity)query.getSingleResult();
        }
        catch(NoResultException | NonUniqueResultException ex)
        {
            throw new EmployeeNotFoundException("Staff Username " + username + " does not exist!");
        }
    }
      
    
    @Override
    public EmployeeEntity employeeLogin(String username, String password) throws InvalidLoginCredentialException
    {
        try 
        {
            EmployeeEntity employeeEntity = retrieveEmployeeByUsername(username);
            
            if (employeeEntity.getPassword().equals(password))
            {
                return employeeEntity.login();
            }
            else 
            {
                throw new InvalidLoginCredentialException("Username does not exist or invalid password!");
            }
        }
        catch(EmployeeNotFoundException ex) 
        {
            throw new InvalidLoginCredentialException("Username does not exist or invalid password!");
        }
    }
    
    @Override
    public EmployeeEntity employeeLogout(EmployeeEntity employee) throws InvalidLoginCredentialException 
    {
        if (employee.isLogged_in() == true) 
        {
            return employee.logout();
        }
        
        throw new InvalidLoginCredentialException("User is not logged in!");
    }

    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<EmployeeEntity>>constraintViolations)
    {
        String msg = "Input data validation error!:";
            
        for(ConstraintViolation constraintViolation:constraintViolations)
        {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }
        
        return msg;
    }

   // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
}
