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

/**
 *
 * @author edenyew
 */
@Stateless
public class EmployeeEntitySessionBean implements EmployeeEntitySessionBeanRemote, EmployeeEntitySessionBeanLocal {

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    
    public EmployeeEntity employeeLogin(String username, String password) 
    {
        List<EmployeeEntity> employeeEntities = retrieveAllEmployee();
        
        for (EmployeeEntity employeeEntity:employeeEntities) 
        {
            if (employeeEntity.getUsername().equals(username) && employeeEntity.getPassword().equals(password)) 
            {
                return employeeEntity;
            }
        }
        
        throw new InvalidLoginCredentialException("Username does not exist or invalid password!");
        
    }
    
    
}
