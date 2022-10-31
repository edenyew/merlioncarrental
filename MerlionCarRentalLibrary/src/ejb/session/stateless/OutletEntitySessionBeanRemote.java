/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.OutletEntity;
import javax.ejb.Remote;

/**
 *
 * @author edenyew
 */
@Remote
public interface OutletEntitySessionBeanRemote {
    
    public OutletEntity retrieveOutletById(Long outletId);

    public void updateOutletEntity(OutletEntity outlet);

    public Long createNewOutlet(OutletEntity outletEntity);
    
}
