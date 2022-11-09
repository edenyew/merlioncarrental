/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.PartnerEntity;
import exception.InvalidLoginCredentialException;
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
    
    @Override
    public PartnerEntity retrievePartnerById(Long id) 
    {
        
        return em.find(PartnerEntity.class, id);
    }
    
    @Override
    public PartnerEntity retrievePartnerByEmail(String email){
        Query query = em.createQuery("Select p From PartnerEntity p Where p.email = :email");
        query.setParameter("email", email);
        
        PartnerEntity partner = (PartnerEntity) query.getSingleResult();

            return partner;
    }
    
    @Override
    public PartnerEntity partnerLogin(String email, String password) throws InvalidLoginCredentialException{
        PartnerEntity partner = retrievePartnerByEmail(email);
        if (partner.getPassword().equals(password)){
           
                partner.setLoggedIn(true);
                return partner;
            
            
        } else {
            throw new InvalidLoginCredentialException("Email or Password is wrong");
        }
    }

    

    @Override
    public PartnerEntity partnerLogout(PartnerEntity partner) throws InvalidLoginCredentialException
    {
        PartnerEntity parnterToLogout = retrievePartnerById(partner.getPartnerId());
        
        if (parnterToLogout.isLoggedIn()){
            parnterToLogout.setLoggedIn(false);
            return parnterToLogout;
        } else {
            throw new InvalidLoginCredentialException("Customer is already logged out");
        }
        
    }

}
