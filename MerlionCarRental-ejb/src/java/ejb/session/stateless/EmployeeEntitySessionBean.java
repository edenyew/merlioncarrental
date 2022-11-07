/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.EmployeeEntity;
import exception.EmployeeNotFoundException;
import exception.InvalidLoginCredentialException;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author edenyew
 */
@Stateless
public class EmployeeEntitySessionBean implements EmployeeEntitySessionBeanRemote, EmployeeEntitySessionBeanLocal {

    @PersistenceContext(unitName = "MerlionCarRental-ejbPU")
    private EntityManager em;
    
    
    @Override
    public Long createNewEmployee(EmployeeEntity employeeEntity)
    {
        em.persist(employeeEntity);
        em.flush();
        
        return employeeEntity.getId();
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

   // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
}
