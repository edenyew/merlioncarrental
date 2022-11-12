/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CarEntity;
import entity.Model;
import entity.OutletEntity;
import entity.Reservation;
import entity.TransitDriverDispatchRecord;
import exception.OutletNotFoundException;
import exception.ReservationNotFoundException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Schedule;
import javax.ejb.Stateless;
import util.enumeration.CarStatusEnum;

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
    
    
//@Schedule(hour = "*", minute = "*", second = "*/5", info = "generateTransitDriverDispatchRecordCheckTimer")    
    // For testing purpose, we are allowing the timer to trigger every 5 seconds instead of every 5 minutes
    // To trigger the timer once every 5 minutes instead, use the following the @Schedule annotation
    // @Schedule(hour = "*", minute = "*/5", info = "productEntityReorderQuantityCheckTimer")
    @Override
    public void timer()  
    {
//        try {
//            Scanner scanner = new Scanner(System.in);
//            List<OutletEntity> allOutlets = outletEntitySessionBeanLocal.retrieveAllOutlets();
//            Long response;
//            String todayDateString = "";
//            
//            System.out.println("Choose OutletID> ");
//            
//            for (OutletEntity outlet : allOutlets)
//            {
//                System.out.println(outlet.getOutletId() + ", " + outlet.getAddress());
//            }
//            
//            response = scanner.nextLong();
            
//            System.out.print("Enter today's date (DD/MM/YYYY)> ");
//            todayDateString = scanner.nextLine().trim();
//            Date currDate = new SimpleDateFormat("dd/MM/yyyy").parse(todayDateString);
//            
//            String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
//            System.out.println("********** EjbTimerSessionBean.generateTransitDriverDispatchRecordCheckTimer(): Timeout at " + timeStamp);
            
//            List<TransitDriverDispatchRecord> transitDriverDispatchRecords = transitDriverDispatchRecordSessionBeanLocal.viewCurrentDayTransitRecord(currDate, response);
//            
//            System.out.println("Car Plate Number, Date Of Transit, Pickup Outlet, Return Outlet");
            
//            for(TransitDriverDispatchRecord transitDriverRecord : transitDriverDispatchRecords)
//            {
//                System.out.println(transitDriverRecord.getCar().getCarPlateNumber() + ", " + transitDriverRecord.getDateOfTransit() + ", " + transitDriverRecord.getPickUpOutlet().getAddress() + ", " + transitDriverRecord.getReturnOutlet().getAddress());
//            }
//        } catch (ParseException | OutletNotFoundException ex) {
//            Logger.getLogger(EjbTimerSessionBean.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }
    
   // @Schedule(hour = "*", minute = "*", second = "*/5", info = "allocateCarsToCurrentDayReservations")   
    public void allocateCarsToCurrentDayReservationsTimer() {
        try {
            Date currDate = new Date();
            System.out.println(currDate);
            List<Reservation> reservations = reservationSessionBeanLocal.retrieveReservationsOnPickUpDate(currDate);
            for(Reservation reservation :reservations){
                Model model = reservation.getModel();
                for (CarEntity car : model.getCars()) {
                    if (car.getOutletEntity().equals(reservation.getPickUpOutlet()) && car.getCurrentStatus().equals(CarStatusEnum.NOT_IN_USE)){
                        for (Reservation existingReservation : car.getReservations()) {
                            if (existingReservation.getPickUpDate().compareTo(currDate) < 0 && existingReservation.getReturnDate().compareTo(currDate) > 0) {
                               reservation.setCar(car);
                               car.getReservations().add(reservation);
                            }
                        }
                    } else {
                        
                    }                                      
                }
            }
            
        } catch (ReservationNotFoundException ex) {
            System.out.println("Error: " + ex.getMessage() + "\n");
        }
        
    }
    
}
