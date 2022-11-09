/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.OutletEntity;
import entity.TransitDriverDispatchRecord;
import exception.OutletNotFoundException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author jonta
 */
@Stateless
public class EjbTimerSessionBean implements EjbTimerSessionBeanRemote, EjbTimerSessionBeanLocal {

    @EJB 
    TransitDriverDispatchRecordSessionBeanLocal transitDriverDispatchRecordSessionBeanLocal;
    @EJB
    ReservationSessionBeanLocal reservationSessionBeanLocal;
    @EJB
    OutletEntitySessionBeanLocal outletEntitySessionBeanLocal;
    
    public void timer(Long outletId) {
      
        
    }
    
}
