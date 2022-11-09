/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package merlioncarrentalclient;

import ejb.session.stateless.CarEntitySessionBeanRemote;
import ejb.session.stateless.CategorySessionBeanRemote;
import ejb.session.stateless.CustomerSessionBeanRemote;
import ejb.session.stateless.EmployeeEntitySessionBeanRemote;
import ejb.session.stateless.ModelSessionBeanRemote;
import ejb.session.stateless.OutletEntitySessionBeanRemote;
import ejb.session.stateless.PartnerEntitySessionBeanRemote;
import ejb.session.stateless.RentalRateSessionBeanRemote;
import ejb.session.stateless.ReservationSessionBeanRemote;
import ejb.session.stateless.TransitDriverDispatchRecordSessionBeanRemote;
import entity.PartnerEntity;
import java.util.List;
import javax.ejb.EJB;

/**
 *
 * @author edenyew
 */
public class Main {

    @EJB
    private static TransitDriverDispatchRecordSessionBeanRemote transitDriverDispatchRecordSessionBean;
    @EJB
    private static ModelSessionBeanRemote modelSessionBean;
    @EJB
    private static CategorySessionBeanRemote categorySessionBean;
    @EJB
    private static EmployeeEntitySessionBeanRemote employeeEntitySessionBean;
    @EJB
    private static RentalRateSessionBeanRemote rentalRateSessionBean;
    @EJB
    private static CustomerSessionBeanRemote customerSessionBean;
    @EJB
    private static OutletEntitySessionBeanRemote outletEntitySessionBean;
    @EJB
    private static CarEntitySessionBeanRemote carEntitySessionBean;
    @EJB
    private static ReservationSessionBeanRemote reservationSessionBeanRemote;
           
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        
        MainApp mainApp = new MainApp(employeeEntitySessionBean, carEntitySessionBean, customerSessionBean, categorySessionBean, rentalRateSessionBean, modelSessionBean, outletEntitySessionBean, transitDriverDispatchRecordSessionBean, reservationSessionBeanRemote);
        mainApp.runApp();
        
    }
    
}
