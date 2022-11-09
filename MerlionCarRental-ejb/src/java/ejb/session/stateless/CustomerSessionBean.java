/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Customer;
import entity.Reservation;
import exception.AlreadyLoggedInException;
import exception.CustomerNotFoundException;
import exception.InvalidLoginCredentialException;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author edenyew
 */
@Stateless
public class CustomerSessionBean implements CustomerSessionBeanRemote, CustomerSessionBeanLocal {

    @PersistenceContext(unitName = "MerlionCarRental-ejbPU")
    private EntityManager em;
    
    @EJB
    ReservationSessionBeanLocal reservationSessionBeanLocal;
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    
    @Override
    public Long createCustomer(Customer customer) 
    {  
        //Customer customerWithSamePassport = retrieveCustomerWithPassportNumber(customer.getPassportNumber());
        
        em.persist(customer);
        em.flush();
        
        return customer.getCustomerId();
    }
    
    @Override
    public Customer retrieveCustomerWithPassportNumber(String passportNum) throws CustomerNotFoundException
    {
        Query query = em.createQuery("Select c From Customer c Where c.passportNumber = :passportNumber");
        query.setParameter("passportNumber", passportNum);
        Customer customer = (Customer) query.getSingleResult();
        
        if (customer != null){
            return customer;
        } else {
            throw new CustomerNotFoundException("Customer Not Found!");
        }
        
    }
   @Override 
    public Customer retrieveCustomerById(Long customerId) throws CustomerNotFoundException
    {
        Customer customer = em.find(Customer.class, customerId);
        if (customer != null){
            return customer;
        } else {
            throw new CustomerNotFoundException("Customer Not Found!");
        }
    }
    
     @Override
    public Customer retrieveCustomerByEmail(String email) throws CustomerNotFoundException
    {
        Query query = em.createQuery("Select c From Customer c Where c.email = :email");
        query.setParameter("email", email);
        try {
        Customer customer = (Customer) query.getSingleResult();
        if (customer != null){
            return customer;
        } else {
            throw new CustomerNotFoundException("Customer Not Found!");
        }
        } catch(NoResultException ex){
            throw new CustomerNotFoundException("Customer Not Found!");
        }
        
        
    }
    
    @Override
    public void updateCustomer(Customer customer)
    {
        em.merge(customer);
    }
    
    @Override
    public Customer customerLogin(String email, String password)throws CustomerNotFoundException, InvalidLoginCredentialException, AlreadyLoggedInException
    {
        Customer customer = retrieveCustomerByEmail(email);
        if (customer.getPassword().equals(password)){
           
                customer.setLoggedIn(true);
                return customer;
            
            
        } else {
            throw new InvalidLoginCredentialException("Email or Password is wrong");
        }
    }
    
    @Override
    public Customer customerLogout(Customer customer) throws CustomerNotFoundException,InvalidLoginCredentialException
    {
        Customer customerToLogout = retrieveCustomerById(customer.getCustomerId());
        
        if (customerToLogout.isLoggedIn()){
            customerToLogout.setLoggedIn(false);
            return customerToLogout;
        } else {
            throw new InvalidLoginCredentialException("Customer is already logged out");
        }
        
    }
    
    @Override
    public void viewAllReservations(Customer customer) throws CustomerNotFoundException
    {
        Customer customerToView= retrieveCustomerById(customer.getCustomerId());
        List<Reservation> reservations = customerToView.getReservations();
        //Reservation reservation = customerToView.getReservation();
        
        //System.out.println(reservation.getId());
    }
    
//    public void reserveCar(Reservation reservation, Customer customer, CarEntity car){
//        
//    }
    
}
