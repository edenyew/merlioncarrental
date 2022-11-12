/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Customer;
import exception.AlreadyLoggedInException;
import exception.CustomerNotFoundException;
import exception.InputDataValidationException;
import exception.InvalidLoginCredentialException;
import exception.UnknownPersistenceException;
import javax.ejb.Local;

/**
 *
 * @author edenyew
 */
@Local
public interface CustomerEntitySessionBeanLocal {

    public void updateCustomer(Customer customer);

    public Customer retrieveCustomerWithPassportNumber(String passportNum) throws CustomerNotFoundException;

    public Long createCustomer(Customer customer) throws CustomerNotFoundException, UnknownPersistenceException, InputDataValidationException;

    public Customer retrieveCustomerByEmail(String email) throws CustomerNotFoundException;
    
    public Customer retrieveCustomerById(Long customerId) throws CustomerNotFoundException;

    public Customer customerLogin(String email, String password) throws CustomerNotFoundException, InvalidLoginCredentialException, AlreadyLoggedInException;
    
    public void viewAllReservations(Customer customer) throws CustomerNotFoundException;
    
}
