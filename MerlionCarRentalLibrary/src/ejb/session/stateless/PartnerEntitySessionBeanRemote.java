/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.PartnerEntity;
import exception.InvalidLoginCredentialException;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author edenyew
 */
@Remote
public interface PartnerEntitySessionBeanRemote {
    
    public Long createNewPartnerRecord(PartnerEntity partner);
    
    public List<PartnerEntity> retrieveAllPartnerRecord();
    
    public PartnerEntity retrievePartnerById(Long id);

    public PartnerEntity retrievePartnerByEmail(String email);

    public PartnerEntity partnerLogin(String email, String password) throws InvalidLoginCredentialException;

    public PartnerEntity partnerLogout(PartnerEntity partner) throws InvalidLoginCredentialException;
}
