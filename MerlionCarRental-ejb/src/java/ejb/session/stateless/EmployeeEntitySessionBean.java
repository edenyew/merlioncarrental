/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.EmployeeEntity;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
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
    public EmployeeEntity employeeLogin(String username, String password) 
    {
        List<EmployeeEntity> employeeEntities = retrieveAllEmployee();
        
        for (EmployeeEntity employeeEntity:employeeEntities) 
        {
            if (employeeEntity.getUsername().equals(username) && employeeEntity.getPassword().equals(password)) 
            {
                return employeeEntity.login();
            }
        }
        
        throw new InvalidLoginCredentialException("Username does not exist or invalid password!");
        
    }
    
    @Override
    public EmployeeEntity employeeLogout(EmployeeEntity employee) {
        if (employee.isLogged_in() == true) 
        {
            return employee.logout();
        }
        
        throw new InvalidLoginCredentialException("User is not logged in!");
    }

   // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    
    
}
