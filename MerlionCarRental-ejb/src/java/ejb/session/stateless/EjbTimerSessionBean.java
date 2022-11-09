/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.OutletEntity;
import entity.TransitDriverDispatchRecord;
import exception.OutletNotFoundException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import javax.ejb.EJB;
import javax.ejb.Schedule;
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
    
    
    @Schedule(hour = "*", minute = "*", second = "*/5", info = "generateTransitDriverDispatchRecordCheckTimer")    
    // For testing purpose, we are allowing the timer to trigger every 5 seconds instead of every 5 minutes
    // To trigger the timer once every 5 minutes instead, use the following the @Schedule annotation
    // @Schedule(hour = "*", minute = "*/5", info = "productEntityReorderQuantityCheckTimer")
    @Override
    public void timer(Long outletId) throws ParseException, OutletNotFoundException 
    {
        Scanner scanner = new Scanner(System.in);
        List<OutletEntity> allOutlets = outletEntitySessionBeanLocal.retrieveAllOutlets();
        Long response;
        String todayDateString = "";
        
        System.out.println("Choose OutletID> ");
        
        for (OutletEntity outlet : allOutlets)
        {
            System.out.println(outlet.getOutletId() + ", " + outlet.getAddress());
        }
        
        response = scanner.nextLong();
        
        System.out.print("Enter today's date (DD/MM/YYYY)> ");
        todayDateString = scanner.nextLine().trim();
        Date currDate = new SimpleDateFormat("dd/MM/yyyy").parse(todayDateString);
        
        String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
        System.out.println("********** EjbTimerSessionBean.generateTransitDriverDispatchRecordCheckTimer(): Timeout at " + timeStamp);
        
        List<TransitDriverDispatchRecord> transitDriverDispatchRecords = transitDriverDispatchRecordSessionBeanLocal.viewCurrentDayTransitRecord(currDate, response);
        
        System.out.println("Car Plate Number, Date Of Transit, Pickup Outlet, Return Outlet");
        
        for(TransitDriverDispatchRecord transitDriverRecord : transitDriverDispatchRecords)
        {
            System.out.println(transitDriverRecord.getCar().getCarPlateNumber() + ", " + transitDriverRecord.getDateOfTransit() + ", " + transitDriverRecord.getPickUpOutlet().getAddress() + ", " + transitDriverRecord.getReturnOutlet().getAddress());
        }
    }
    
}
