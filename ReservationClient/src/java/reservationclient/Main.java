/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package reservationclient;

import ejb.session.stateless.CarEntitySessionBeanRemote;
import ejb.session.stateless.CreditCardSessionBeanRemote;
import ejb.session.stateless.OutletEntitySessionBeanRemote;
import ejb.session.stateless.RentalRateSessionBeanRemote;
import ejb.session.stateless.ReservationSessionBeanRemote;
import javax.ejb.EJB;
import ejb.session.stateless.ModelSessionBeanRemote;
import ejb.session.stateless.CustomerEntitySessionBeanRemote;

/**
 *
 * @author jonta
 */
public class Main {

    @EJB
    private static CustomerEntitySessionBeanRemote customerSessionBeanRemote;
    
    @EJB 
    private static ReservationSessionBeanRemote reservationSessionBeanRemote;
    
    @EJB 
    private static CarEntitySessionBeanRemote carSessionBeanRemote;
    @EJB 
    private static OutletEntitySessionBeanRemote outletSessionBeanRemote;
    @EJB
    private static RentalRateSessionBeanRemote rentalRateSessionBeanRemote;
    @EJB
    private static CreditCardSessionBeanRemote creditCardSessionBeanRemote;
    @EJB
    private static ModelSessionBeanRemote modelSessionBeanRemote;
     
   
    public static void main(String[] args) {
       MainApp mainApp = new MainApp(customerSessionBeanRemote, reservationSessionBeanRemote, carSessionBeanRemote, outletSessionBeanRemote, rentalRateSessionBeanRemote, creditCardSessionBeanRemote, modelSessionBeanRemote);
        mainApp.runApp();
    }
    
}
