/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.PartnerEntity;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author edenyew
 */
@Stateless
public class PartnerEntitySessionBean implements PartnerEntitySessionBeanRemote, PartnerEntitySessionBeanLocal {

    @PersistenceContext(unitName = "MerlionCarRental-ejbPU")
    private EntityManager em;
    

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    
    
    @Override
    public Long createNewPartnerRecord(PartnerEntity partner) 
    {
        em.persist(partner);
        em.flush();
        
        return partner.getPartnerId();
    }      
    
    
    @Override
    public List<PartnerEntity> retrieveAllPartnerRecord() 
    {
        Query query = em.createQuery("SELECT p FROM PartnerEntity p");
        
        return query.getResultList();
    }
    
    
}
