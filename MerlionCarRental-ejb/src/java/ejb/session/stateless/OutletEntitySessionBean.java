/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.OutletEntity;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

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
    
    public Long createNewOutlet(OutletEntity outletEntity)
    {
        em.persist(outletEntity);
        em.flush();
        
        return outletEntity.getId();
        
    }
    
    
    public OutletEntity retrieveOutletById(Long outletId)
    {
        Query query = em.createQuery("Select o From OutletEntity o Where o.id =: idNum");
        query.setParameter("idNum", outletId);
        
        return (OutletEntity) query.getSingleResult(); // add exception here
    }
    
    
    public void updateOutletEntity(OutletEntity outlet)
    {
        em.merge(outlet);
    }
    
    
}
