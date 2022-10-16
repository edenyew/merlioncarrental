/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.singleton;

import ejb.session.stateless.PartnerEntitySessionBeanLocal;
import entity.PartnerEntity;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author edenyew
 */
@Singleton
@LocalBean
@Startup

public class DataInitSessionBean {

    @EJB
    private PartnerEntitySessionBeanLocal partnerEntitySessionBeanLocal;

    @PersistenceContext(unitName = "MerlionCarRental-ejbPU")
    private EntityManager em;

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    
    @PostConstruct
    public void postConstruct() 
    {
        if (em.find(PartnerEntity.class, 1l) == null)
        {
            partnerEntitySessionBeanLocal.createNewPartnerRecord(new PartnerEntity("partner1", "91234567", "partner1@gmail.com" ,"A123456"));
        }
    }
    
}
