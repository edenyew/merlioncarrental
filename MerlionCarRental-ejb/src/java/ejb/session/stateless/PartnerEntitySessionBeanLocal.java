/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.PartnerEntity;
import exception.InputDataValidationException;
import exception.InvalidLoginCredentialException;
import exception.PartnerEntityNotFoundException;
import exception.UnknownPersistenceException;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author edenyew
 */
@Local
public interface PartnerEntitySessionBeanLocal {

    public Long createNewPartnerRecord(PartnerEntity partner) throws PartnerEntityNotFoundException, UnknownPersistenceException, InputDataValidationException;

    public List<PartnerEntity> retrieveAllPartnerRecord();

    public PartnerEntity retrievePartnerById(Long id);

    public PartnerEntity retrievePartnerByEmail(String email);

    public PartnerEntity partnerLogin(String email, String password) throws InvalidLoginCredentialException;

    public PartnerEntity partnerLogout(PartnerEntity partner) throws InvalidLoginCredentialException;
    
}
