/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.ws;

import ejb.session.stateless.PartnerEntitySessionBeanLocal;
import entity.PartnerEntity;
import exception.InvalidLoginCredentialException;
import javax.ejb.EJB;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.ejb.Stateless;

/**
 *
 * @author jonta
 */
@WebService(serviceName = "PartnerWebService")
@Stateless()
public class PartnerWebService {

    @EJB
    private PartnerEntitySessionBeanLocal partnerEntitySessionBean;
        
    @WebMethod(operationName = "retrievePartnerByEmail")
    public PartnerEntity retrievePartnerByEmail(@WebParam(name = "email")String email){
        return partnerEntitySessionBean.retrievePartnerByEmail(email);
    }
    @WebMethod(operationName = "partnerLogin")
    public PartnerEntity partnerLogin(@WebParam(name = "email")String email, @WebParam(name = "password")String password) throws InvalidLoginCredentialException{
        return partnerEntitySessionBean.partnerLogin(email, password);
    }
    @WebMethod(operationName = "partnerLogout")
    public PartnerEntity partnerLogout(@WebParam(name = "partner")PartnerEntity partner) throws InvalidLoginCredentialException {
        return partnerEntitySessionBean.partnerLogout(partner);
    }
}
