/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Customer;
import exception.AlreadyLoggedInException;
import exception.CustomerNotFoundException;
import exception.InvalidLoginCredentialException;
import javax.ejb.Remote;

/**
 *
 * @author edenyew
 */
@Remote
public interface CustomerSessionBeanRemote {
    
    public void updateCustomer(Customer customer);

    public Customer retrieveCustomerWithPassportNumber(String passportNum) throws CustomerNotFoundException;

    public Long createCustomer(Customer customer);
    
    public Customer retrieveCustomerByEmail(String email) throws CustomerNotFoundException;
    
     public Customer customerLogin(String email, String password) throws CustomerNotFoundException, InvalidLoginCredentialException, AlreadyLoggedInException;

    public Customer customerLogout(Customer customer) throws CustomerNotFoundException, InvalidLoginCredentialException;

    public Customer retrieveCustomerById(Long customerId) throws CustomerNotFoundException;
    
}
