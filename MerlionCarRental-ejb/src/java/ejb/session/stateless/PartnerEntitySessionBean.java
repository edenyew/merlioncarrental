/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.PartnerEntity;
import exception.InputDataValidationException;
import exception.InvalidLoginCredentialException;
import exception.PartnerEntityNotFoundException;
import exception.UnknownPersistenceException;
import java.util.List;
import java.util.Set;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
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
public class PartnerEntitySessionBean implements PartnerEntitySessionBeanRemote, PartnerEntitySessionBeanLocal {

    @PersistenceContext(unitName = "MerlionCarRental-ejbPU")
    private EntityManager em;
    

    private final ValidatorFactory validatorFactory;
    private final Validator validator;
    
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")

    
    
    public PartnerEntitySessionBean() 
    {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    
    @Override
    public Long createNewPartnerRecord(PartnerEntity partner) throws PartnerEntityNotFoundException, UnknownPersistenceException, InputDataValidationException
    {
        Set<ConstraintViolation<PartnerEntity>>constraintViolations = validator.validate(partner);
        
        if (constraintViolations.isEmpty())
        {
            try
            {
        
                em.persist(partner);
                em.flush();

                return partner.getPartnerId();
            }
            catch(PersistenceException ex)
            {
                if(ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException"))
                {
                    if(ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException"))
                    {
                        throw new PartnerEntityNotFoundException();
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
    public List<PartnerEntity> retrieveAllPartnerRecord() 
    {
        Query query = em.createQuery("SELECT p FROM PartnerEntity p");
        
        return query.getResultList();
    }
    
    @Override
    public PartnerEntity retrievePartnerById(Long id) 
    {
        
        return em.find(PartnerEntity.class, id);
    }
    
    @Override
    public PartnerEntity retrievePartnerByEmail(String email){
        Query query = em.createQuery("Select p From PartnerEntity p Where p.email = :email");
        query.setParameter("email", email);
        
        PartnerEntity partner = (PartnerEntity) query.getSingleResult();

            return partner;
    }
    
    @Override
    public PartnerEntity partnerLogin(String email, String password) throws InvalidLoginCredentialException{
        PartnerEntity partner = retrievePartnerByEmail(email);
        if (partner.getPassword().equals(password)){
           
                partner.setLoggedIn(true);
                return partner;
            
            
        } else {
            throw new InvalidLoginCredentialException("Email or Password is wrong");
        }
    }

    

    @Override
    public PartnerEntity partnerLogout(PartnerEntity partner) throws InvalidLoginCredentialException
    {
        PartnerEntity parnterToLogout = retrievePartnerById(partner.getPartnerId());
        
        if (parnterToLogout.isLoggedIn()){
            parnterToLogout.setLoggedIn(false);
            return parnterToLogout;
        } else {
            throw new InvalidLoginCredentialException("Customer is already logged out");
        }
        
    }
    
    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<PartnerEntity>>constraintViolations)
    {
        String msg = "Input data validation error!:";
            
        for(ConstraintViolation constraintViolation:constraintViolations)
        {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }
        
        return msg;
    }

}
