/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Customer;
import javax.ejb.Remote;

/**
 *
 * @author edenyew
 */
@Remote
public interface CustomerSessionBeanRemote {
    
    public void updateCustomer(Customer customer);

    public Customer retrieveCustomerWithPassportNumber(String passportNum);

    public Long createCustomer(Customer customer);
    
}
