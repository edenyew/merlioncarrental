/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Customer;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
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
    
    
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    
    public Long createCustomer(Customer customer)
    {
        em.persist(customer);
        em.flush();
        
        return customer.getId();
    }
    
    public Customer retrieveCustomerWithPassportNumber(String passportNum)
    {
        Query query = em.createQuery("Select c From Customer c Where c.passportNumber = :passportNumber");
        query.setParameter("passportNumber", passportNum);
        
        return (Customer) query.getSingleResult(); // add exception
    }
    
    public void updateCustomer(Customer customer)
    {
        em.merge(customer);
    }
    
}
