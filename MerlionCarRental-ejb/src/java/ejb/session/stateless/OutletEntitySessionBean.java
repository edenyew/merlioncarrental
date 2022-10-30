/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.OutletEntity;
import exception.OutletNotFoundException;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author edenyew
 */
@Stateless
public class OutletEntitySessionBean implements OutletEntitySessionBeanRemote, OutletEntitySessionBeanLocal {

    @PersistenceContext(unitName = "MerlionCarRental-ejbPU")
    private EntityManager em;
    
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    
    @Override
    public Long createNewOutlet(OutletEntity outletEntity)
    {
        em.persist(outletEntity);
        em.flush();
        
        return outletEntity.getId();
        
    }
    
    @Override
    public OutletEntity retrieveOutletById(Long outletId) throws OutletNotFoundException
    {
        OutletEntity outlet = em.find(OutletEntity.class, outletId);
        if (outlet != null ){
            return outlet;
        } else {
            throw new OutletNotFoundException("Outlet does not exist!");
        }
    }
    
    
}
