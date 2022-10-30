/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.TransitDriverDispatchRecord;
import exception.CarNotFoundException;
import exception.OutletNotFoundException;
import javax.ejb.Remote;

/**
 *
 * @author jonta
 */
@Remote
public interface TransitDriverDispatchRecordSessionBeanRemote {
    
    public Long createNewTransitRecord(TransitDriverDispatchRecord transitRecord, Long employeeId, Long pickupOutletId, Long returnOutletId, Long carId) throws OutletNotFoundException, CarNotFoundException;
    
}
