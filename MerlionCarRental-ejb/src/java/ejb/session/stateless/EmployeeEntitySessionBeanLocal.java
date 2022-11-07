/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.EmployeeEntity;
import exception.EmployeeNotFoundException;
import exception.InvalidLoginCredentialException;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author edenyew
 */
@Local
public interface EmployeeEntitySessionBeanLocal {

    public List<EmployeeEntity> retrieveAllEmployee();

    public Long createNewEmployee(EmployeeEntity employeeEntity);

    public EmployeeEntity employeeLogin(String username, String password) throws InvalidLoginCredentialException;

    public EmployeeEntity employeeLogout(EmployeeEntity employee) throws InvalidLoginCredentialException;

    public EmployeeEntity retrieveEmployeeByUsername(String username) throws EmployeeNotFoundException;

    public EmployeeEntity retrieveEmployeeById(Long employeeId);

    public void updateEmployee(EmployeeEntity employee);
    
}
