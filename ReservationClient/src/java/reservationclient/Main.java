/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package reservationclient;

import ejb.session.stateless.CarEntitySessionBeanRemote;
import ejb.session.stateless.CustomerSessionBeanRemote;
import ejb.session.stateless.OutletEntitySessionBeanRemote;
import ejb.session.stateless.RentalRateSessionBeanRemote;
import ejb.session.stateless.ReservationSessionBeanRemote;
import javax.ejb.EJB;

/**
 *
 * @author jonta
 */
public class Main {

    @EJB
    private static CustomerSessionBeanRemote customerSessionBeanRemote;
    
    @EJB 
    private static ReservationSessionBeanRemote reservationSessionBeanRemote;
    
    @EJB 
    private static CarEntitySessionBeanRemote carSessionBeanRemote;
    @EJB 
    private static OutletEntitySessionBeanRemote outletSessionBeanRemote;
    @EJB
    private static RentalRateSessionBeanRemote rentalRateSessionBeanRemote;
     
   
    public static void main(String[] args) {
       MainApp mainApp = new MainApp(customerSessionBeanRemote, reservationSessionBeanRemote, carSessionBeanRemote, outletSessionBeanRemote, rentalRateSessionBeanRemote);
        mainApp.runApp();
    }
    
}
