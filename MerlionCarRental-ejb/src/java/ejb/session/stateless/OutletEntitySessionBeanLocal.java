/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.OutletEntity;
import exception.CarNotFoundException;
import exception.CarNotInOutletException;
import exception.OutletNotFoundException;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author edenyew
 */
@Local
public interface OutletEntitySessionBeanLocal {

    public OutletEntity retrieveOutletById(Long outletId) throws OutletNotFoundException;
    
    public void updateOutletEntity(OutletEntity outlet);

    public Long createNewOutlet(OutletEntity outletEntity);   

    public List<OutletEntity> retrieveAllOutlets();
    
    public boolean findCarInOutlet(Long outletId, Long carId) throws CarNotFoundException, OutletNotFoundException;
    
}
