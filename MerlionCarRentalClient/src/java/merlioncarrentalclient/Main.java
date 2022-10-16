/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package merlioncarrentalclient;

import ejb.session.stateless.PartnerEntitySessionBeanRemote;
import entity.PartnerEntity;
import java.util.List;
import javax.ejb.EJB;

/**
 *
 * @author edenyew
 */
public class Main {

    @EJB
    private static PartnerEntitySessionBeanRemote partnerEntitySessionBeanRemote;

    
           
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        
      List<PartnerEntity> partnerRecords = partnerEntitySessionBeanRemote.retrieveAllPartnerRecord();
      
      for (PartnerEntity partnerEntity: partnerRecords)
      {
          System.out.println("Partner Entity:" + partnerEntity.getPartnerId() + partnerEntity.getContactNumber() + partnerEntity.getName() + partnerEntity.getEmail() + partnerEntity.getPassportNumber());
               
      }

    }
    
}
