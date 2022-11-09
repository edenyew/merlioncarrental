/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CreditCard;
import entity.Customer;
import exception.CustomerNotFoundException;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author jonta
 */
@Stateless
public class CreditCardSessionBean implements CreditCardSessionBeanRemote, CreditCardSessionBeanLocal {

    @PersistenceContext(unitName = "MerlionCarRental-ejbPU")
    private EntityManager em;


    @Override
    public Long createNewCreditCard(CreditCard creditCard) {

        em.persist(creditCard);
            
        em.flush();
        return creditCard.getId();
    }
    
    @Override
    public CreditCard retrieveCreditCardById(Long creditCardId) {
        CreditCard creditCard = em.find(CreditCard.class,creditCardId);
        return creditCard;
    }

}
