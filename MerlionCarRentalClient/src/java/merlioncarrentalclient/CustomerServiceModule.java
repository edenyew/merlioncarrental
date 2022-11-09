/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package merlioncarrentalclient;

import ejb.session.stateless.CarEntitySessionBeanRemote;
import ejb.session.stateless.CustomerSessionBeanRemote;
import ejb.session.stateless.ReservationSessionBeanRemote;
import entity.CarEntity;
import entity.Customer;
import entity.EmployeeEntity;
import entity.OutletEntity;
import entity.Reservation;
import exception.CarAlreadyInOutletException;
import exception.CarNotFoundException;
import exception.CarNotInOutletException;
import exception.CustomerNotFoundException;
import exception.InvalidAccessRightException;
import exception.OutletNotFoundException;
import exception.ReservationNotFoundException;
import java.util.List;
import java.util.Scanner;
import util.enumeration.AccessRightEnum;

/**
 *
 * @author edenyew
 */
public class CustomerServiceModule {

    private CarEntitySessionBeanRemote carEntitySessionBeanRemote;
    private CustomerSessionBeanRemote customerSessionBeanRemote;
    private ReservationSessionBeanRemote reservationSessionBeanRemote;
    
    private EmployeeEntity currentEmployeeEntity;
    private Customer currentCustomer;
    
    
    
    public CustomerServiceModule() {
    }
    
    public CustomerServiceModule(CarEntitySessionBeanRemote carEntitySessionBeanRemote, CustomerSessionBeanRemote customerSessionBeanRemote, EmployeeEntity currentEmployeeEntity, ReservationSessionBeanRemote reservationSessionBeanRemote) {
        this.carEntitySessionBeanRemote = carEntitySessionBeanRemote;
        this.customerSessionBeanRemote =  customerSessionBeanRemote;
        this.currentEmployeeEntity = currentEmployeeEntity;
        this.reservationSessionBeanRemote = reservationSessionBeanRemote;
    }
    
    
    public void menuCustomerService() throws InvalidAccessRightException
    {
        if (currentEmployeeEntity.getAccessRightEnum() != AccessRightEnum.CUSTOMER_SERVICE_EXECUTIVE)
        {
            throw new InvalidAccessRightException("You don't have CUSTOMER SERVICE EXECUTIVE rights to access the system administration module.");
        }
        
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        
        while(true)
        {
            System.out.println("*** Merlion Car Rental System :: Customer Service ***\n");
            System.out.println("1: Pickup Car");
            System.out.println("2: Return Car");
            System.out.println("3: Back\n");
            response = 0;
            
            while(response < 1 || response > 3)
            {
                System.out.print("> ");
                response = scanner.nextInt();

                if(response == 1)
                {
                    try
                    {
                        doPickUpCard();
                    }
                    catch(OutletNotFoundException | CarNotFoundException | CarNotInOutletException | CustomerNotFoundException |  ReservationNotFoundException ex)
                    {
                        System.out.println("An error has occurred: " + ex.getMessage() + "\n");
                    }
                }
                else if(response == 2)
                {
                    try
                    {
                        doReturnCar();
                    }
                    catch(CustomerNotFoundException | OutletNotFoundException | CarNotFoundException | CarAlreadyInOutletException |  ReservationNotFoundException ex)
                    {
                        System.out.println("An error has occurred: " + ex.getMessage() + "\n");
                    }
                }
                else if(response == 3)
                {
                    break;
                }
                else 
                {
                    System.out.println("Invalid option, please try again!\n");     
                }
            }
            if (response == 3)
            {
                break;
            }
        }
    }
    
    private void doPickUpCard() throws CustomerNotFoundException, OutletNotFoundException, CarNotFoundException, CarNotInOutletException, ReservationNotFoundException
    {   
        Scanner scanner = new Scanner(System.in);
        String passportNum = "";
        
        System.out.println("*** Merlion Car Rental System :: Customer Service :: Pickup Car ***\n");
        System.out.print("Enter Customer Passport Number> ");
        passportNum = scanner.nextLine().trim();
        
        currentCustomer = customerSessionBeanRemote.retrieveCustomerWithPassportNumber(passportNum);

        List<Reservation> customerReservation = currentCustomer.getReservations();
        System.out.print("Choose a reservation by ID> ");
        Long reservationId = scanner.nextLong();
        
        Reservation selectedReservation = reservationSessionBeanRemote.retrieveReservationById(reservationId);
        CarEntity carReserved = selectedReservation.getCar();
        OutletEntity outletForCar = selectedReservation.getPickUpOutlet();
        carEntitySessionBeanRemote.pickUpCar(outletForCar.getOutletId(), carReserved);
    }
    
    
    private void doReturnCar() throws CustomerNotFoundException, OutletNotFoundException, CarNotFoundException, CarAlreadyInOutletException, ReservationNotFoundException
    {
        Scanner scanner = new Scanner(System.in);
        String passportNum = "";
        
        System.out.println("*** Merlion Car Rental System :: Customer Service :: Return Car ***\n");
        System.out.print("Enter Customer Passport Number> ");
        passportNum = scanner.nextLine().trim();
        currentCustomer = customerSessionBeanRemote.retrieveCustomerWithPassportNumber(passportNum);
        List<Reservation> customerReservation = currentCustomer.getReservations();
         System.out.print("Choose a reservation by ID> ");
        Long reservationId = scanner.nextLong();
        
        Reservation selectedReservation = reservationSessionBeanRemote.retrieveReservationById(reservationId);

        CarEntity carReserved = selectedReservation.getCar();
        OutletEntity outletForCar = selectedReservation.getReturnOutlet();
        carEntitySessionBeanRemote.returnCar(outletForCar.getOutletId(), carReserved);
    }

}
