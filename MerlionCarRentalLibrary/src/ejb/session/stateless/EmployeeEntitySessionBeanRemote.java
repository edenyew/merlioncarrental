/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.EmployeeEntity;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author edenyew
 */
@Remote
public interface EmployeeEntitySessionBeanRemote {
    
    public List<EmployeeEntity> retrieveAllEmployee();
    
    public Long createNewEmployee(EmployeeEntity employeeEntity);
    
    public EmployeeEntity employeeLogin(String username, String password);

    public EmployeeEntity employeeLogout(EmployeeEntity employee);
    
}
