/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.OutletEntity;
import exception.CarNotFoundException;
import exception.InputDataValidationException;
import exception.OutletNotFoundException;
import exception.UnknownPersistenceException;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author edenyew
 */
@Remote
public interface OutletEntitySessionBeanRemote {
    
    public OutletEntity retrieveOutletById(Long outletId) throws OutletNotFoundException;
    
    public void updateOutletEntity(OutletEntity outlet);

    public Long createNewOutlet(OutletEntity outletEntity) throws OutletNotFoundException, UnknownPersistenceException, InputDataValidationException;
    
    public boolean findCarInOutlet(Long outletId, Long carId) throws CarNotFoundException, OutletNotFoundException;

    public List<OutletEntity> retrieveAllOutlets();
    
    
}
