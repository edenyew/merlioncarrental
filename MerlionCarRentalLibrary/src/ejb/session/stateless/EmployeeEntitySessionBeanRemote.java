/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.EmployeeEntity;
import exception.EmployeeNotFoundException;
import exception.InputDataValidationException;
import exception.InvalidLoginCredentialException;
import exception.OutletNotFoundException;
import exception.UnknownPersistenceException;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author edenyew
 */
@Remote
public interface EmployeeEntitySessionBeanRemote {
    
    public List<EmployeeEntity> retrieveAllEmployee();

    public Long createNewEmployee(EmployeeEntity employeeEntity, Long outletId) throws OutletNotFoundException, EmployeeNotFoundException, UnknownPersistenceException, InputDataValidationException;

    public EmployeeEntity employeeLogin(String username, String password) throws InvalidLoginCredentialException;

    public EmployeeEntity employeeLogout(EmployeeEntity employee) throws InvalidLoginCredentialException;

    public EmployeeEntity retrieveEmployeeByUsername(String username) throws EmployeeNotFoundException;

    public EmployeeEntity retrieveEmployeeById(Long employeeId);

    public void updateEmployee(EmployeeEntity employee);
    
}
