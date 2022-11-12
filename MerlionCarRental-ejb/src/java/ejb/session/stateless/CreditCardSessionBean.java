/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import exception.CreditCardNotFoundException;
import entity.CreditCard;
import entity.Customer;
import exception.CustomerNotFoundException;
import exception.InputDataValidationException;
import exception.UnknownPersistenceException;
import java.util.Set;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

/**
 *
 * @author jonta
 */
@Stateless
public class CreditCardSessionBean implements CreditCardSessionBeanRemote, CreditCardSessionBeanLocal {

    @PersistenceContext(unitName = "MerlionCarRental-ejbPU")
    private EntityManager em;

    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    public CreditCardSessionBean() 
    {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }


    @Override
    public Long createNewCreditCard(CreditCard creditCard) throws CreditCardNotFoundException, UnknownPersistenceException, InputDataValidationException {
        
        Set<ConstraintViolation<CreditCard>>constraintViolations = validator.validate(creditCard);
        
        if (constraintViolations.isEmpty())
        {
            try
            {

                em.persist(creditCard);

                em.flush();
                return creditCard.getId();
            }
            catch(PersistenceException ex)
            {
                if(ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException"))
                {
                    if(ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException"))
                    {
                        throw new CreditCardNotFoundException();
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
    public CreditCard retrieveCreditCardById(Long creditCardId) {
        CreditCard creditCard = em.find(CreditCard.class,creditCardId);
        return creditCard;
    }
    
    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<CreditCard>>constraintViolations)
    {
        String msg = "Input data validation error!:";
            
        for(ConstraintViolation constraintViolation:constraintViolations)
        {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }
        
        return msg;
    }

}
