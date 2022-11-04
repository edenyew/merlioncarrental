/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.TransitDriverDispatchRecord;
import exception.CarNotFoundException;
import exception.OutletNotFoundException;
import exception.TransitRecordNotFoundException;
import java.util.Date;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author jonta
 */
@Local
public interface TransitDriverDispatchRecordSessionBeanLocal {

    public Long createNewTransitRecord(TransitDriverDispatchRecord transitRecord, Long employeeId, Long pickupOutletId, Long returnOutletId, Long carId) throws OutletNotFoundException, CarNotFoundException;

    public TransitDriverDispatchRecord retrieveTransitRecordById(Long transitRecordId) throws TransitRecordNotFoundException;

    public List<TransitDriverDispatchRecord> viewCurrentDayTransitRecord(Date currentDate, Long currentOutletId) throws OutletNotFoundException;

    public void updateTransitAsComplete(TransitDriverDispatchRecord transitRecord) throws TransitRecordNotFoundException;
    
}
