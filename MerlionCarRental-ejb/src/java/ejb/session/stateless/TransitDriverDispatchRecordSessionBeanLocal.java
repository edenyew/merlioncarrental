/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.EmployeeEntity;
import entity.TransitDriverDispatchRecord;
import exception.CarNotFoundException;
import exception.InputDataValidationException;
import exception.OutletNotFoundException;
import exception.TransitRecordNotFoundException;
import exception.UnknownPersistenceException;
import java.util.Date;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author jonta
 */
@Local
public interface TransitDriverDispatchRecordSessionBeanLocal {

    public Long createNewTransitRecord(TransitDriverDispatchRecord transitRecord, Long employeeId, Long pickupOutletId, Long returnOutletId, Long carId) throws OutletNotFoundException,CarNotFoundException, TransitRecordNotFoundException, UnknownPersistenceException, InputDataValidationException;  

    public TransitDriverDispatchRecord retrieveTransitRecordById(Long transitRecordId) throws TransitRecordNotFoundException;

    public List<TransitDriverDispatchRecord> viewCurrentDayTransitRecord(Date currentDate, Long currentOutletId) throws OutletNotFoundException, TransitRecordNotFoundException;

    public void updateTransitAsComplete(TransitDriverDispatchRecord transitRecord) throws TransitRecordNotFoundException, InputDataValidationException;

    public TransitDriverDispatchRecord retrieveTransitRecordByEmployee(EmployeeEntity employee);

    public void updateTransitDriverRecord(TransitDriverDispatchRecord transitRecord);

    public void assignTransitDriver(TransitDriverDispatchRecord transitRecord, Long employeeId);

    public List<TransitDriverDispatchRecord> retrieveAllTransitDriverRecord();

    public void viewRecordDetails(TransitDriverDispatchRecord transitDriverRecord) throws TransitRecordNotFoundException;
    
}
