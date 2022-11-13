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
import exception.CarNotFoundException;
import exception.InputDataValidationException;
import exception.OutletNotFoundException;
import exception.ReservationNotFoundException;
import exception.TransitRecordNotFoundException;
import exception.UnknownPersistenceException;
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
    
    @Schedule(hour = "2", minute = "0", second = "0", info = "generateTransitDriverDispatchRecordCheckTimer")    
    @Override
    public void generateTransitRecordsTimer()  
   
    {
        try {
            Date currDate = new Date();
            List<OutletEntity> allOutlets = outletEntitySessionBeanLocal.retrieveAllOutlets();//            
            for (OutletEntity outlet : allOutlets)
            {
                 List<TransitDriverDispatchRecord>  transitDriverDispatchRecords = transitDriverDispatchRecordSessionBeanLocal. viewCurrentDayTransitRecord(currDate, outlet.getOutletId());
                 for (TransitDriverDispatchRecord transitDriverDispatchRecord: transitDriverDispatchRecords){
                    System.out.println(transitDriverDispatchRecord.getCar().getCarPlateNumber());
                    System.out.println(transitDriverDispatchRecord.getPickUpOutlet().getAddress());
                    System.out.println(transitDriverDispatchRecord.getReturnOutlet().getAddress());
                 }
            }
            
        }   catch (OutletNotFoundException | TransitRecordNotFoundException ex) {
            Logger.getLogger(EjbTimerSessionBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Schedule(hour = "0", minute = "0", second = "0", info = "allocateCarsToCurrentDayReservations")   
    @Override
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
                         TransitDriverDispatchRecord record = new TransitDriverDispatchRecord();
                        Long pickUpOutletId = car.getOutletEntity().getOutletId();
                        Long returnOutletId = reservation.getPickUpOutlet().getOutletId();
                             
                             try {
                                 transitDriverDispatchRecordSessionBeanLocal.createNewTransitRecord(record, pickUpOutletId, returnOutletId, car.getCarId());
                             } catch (OutletNotFoundException | CarNotFoundException | TransitRecordNotFoundException | InputDataValidationException | UnknownPersistenceException ex) {
                                 Logger.getLogger(EjbTimerSessionBean.class.getName()).log(Level.SEVERE, null, ex);
                             }
         
                    }                                      
                }
            }
            
        } catch (ReservationNotFoundException ex) {
            System.out.println("Error: " + ex.getMessage() + "\n");
        }
    }
    
}
