/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CreditCard;
import javax.ejb.Local;

/**
 *
 * @author jonta
 */
@Local
public interface CreditCardSessionBeanLocal {
    
    public Long createNewCreditCard(CreditCard creditCard);

    public CreditCard retrieveCreditCardById(Long creditCardId);
    
}
