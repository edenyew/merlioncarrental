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
import entity.CarEntity;
import entity.CreditCard;
import entity.Customer;
import entity.OutletEntity;
import entity.RentalRate;
import entity.Reservation;
import exception.AlreadyLoggedInException;
import exception.CarNotFoundException;
import exception.CustomerNotFoundException;
import exception.InvalidLoginCredentialException;
import exception.OutletNotFoundException;
import exception.RentalRateNotFoundException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;

/**
 *
 * @author jonta
 */
public class MainApp {
    @EJB
     CustomerSessionBeanRemote customerSessionBeanRemote;
    @EJB 
    ReservationSessionBeanRemote reservationSessionBeanRemote;
    @EJB 
    CarEntitySessionBeanRemote carSessionBeanRemote;
    @EJB 
    OutletEntitySessionBeanRemote outletSessionBeanRemote;
    @EJB
    RentalRateSessionBeanRemote rentalRateSessionBeanRemote;
    private Customer currentCustomer;

    public MainApp() {
    }

    public MainApp(CustomerSessionBeanRemote customerSessionBeanRemote, ReservationSessionBeanRemote reservationSessionBeanRemote, CarEntitySessionBeanRemote carSessionBeanRemote, OutletEntitySessionBeanRemote outletSessionBeanRemote, RentalRateSessionBeanRemote rentalRateSessionBeanRemote) {
        this.customerSessionBeanRemote = customerSessionBeanRemote;
        this.reservationSessionBeanRemote = reservationSessionBeanRemote;
        this.carSessionBeanRemote = carSessionBeanRemote;
        this.outletSessionBeanRemote = outletSessionBeanRemote;
        this.rentalRateSessionBeanRemote = rentalRateSessionBeanRemote;
    }
    
    public void runApp() {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        
        while(true)
        {
            System.out.println("*** Welcome to Customer Resrvation Client ***\n");
            System.out.println("1: Register as new Customer");
            System.out.println("2: Login");
            System.out.println("3: Search Car");
            System.out.println("4: Exit \n");
           
            response = 0;
            
            while(response < 1 || response > 4)
            {
                System.out.print("> ");

                response = scanner.nextInt();

                if(response == 1)
                {
                    registerAsCustomer();
                }
                else if(response == 2)
                {
                    try {
                        
                        customerLogin();
                        System.out.println("Login successful!\n");
                        menuMain();
                        
                    } catch (InvalidLoginCredentialException ex) {
                        
                        System.out.println("Invalid login credential: " + ex.getMessage() + "\n");
                    }
                }
                else if(response == 3)
                {
                    searchCar();
                }
                else
                {
                    System.out.println("Invalid option, please try again!\n");                
                }
            }
            
            if(response == 4)
            {
                break;
            }
        }
    }
    
    
    private void customerLogin() throws InvalidLoginCredentialException
    {
        Scanner scanner = new Scanner(System.in);
        String email = "";
        String password = "";
        
        System.out.println("*** Reservation Client :: Login ***\n");
        System.out.print("Enter email> ");
        email = scanner.nextLine().trim();
        System.out.print("Enter password> ");
        password = scanner.nextLine().trim();
        
        if(email.length() > 0 && password.length() > 0)
        {
            try {      
                
                currentCustomer = customerSessionBeanRemote.customerLogin(email, password);
                
            } catch (CustomerNotFoundException | AlreadyLoggedInException ex) {
                
                System.out.println("Error: " + ex.getMessage() + "\n");
            }
        }
        else
        {
            throw new InvalidLoginCredentialException("Missing login credential!");
        }
    }
    
    private void searchCar() 
    {
        try {
            Scanner scanner = new Scanner(System.in);
            String pickUpDateString = "";
            String returnDateString = "";
            
            
            System.out.println("*** Reservation Client :: Search Car ***\n");
            System.out.print("Enter pick up date (DD/MM/YYYY)> ");
            pickUpDateString = scanner.nextLine().trim();
            Date pickUpDate = new SimpleDateFormat("dd/MM/yyyy").parse(pickUpDateString);
            
            System.out.print("Enter return Date (DD/MM/YYYY)> ");
            returnDateString = scanner.nextLine().trim();
            Date returnDate = new SimpleDateFormat("dd/MM/yyyy").parse(returnDateString);
            
            System.out.print("Choose pickup outlet by typing in its outlet id: \n");
            List<OutletEntity> listOfOutlets = outletSessionBeanRemote.retrieveAllOutlets();
            for (OutletEntity outlet : listOfOutlets){
                 System.out.println("OutletId:" + outlet.getOutletId() + "Outlet Address: " + outlet.getAddress());
            }
            Long pickUpOutletId = scanner.nextLong();
            
            System.out.print("Choose return outlet by typing in its outlet id: \n");
            for (OutletEntity outlet : listOfOutlets){
                 System.out.println("OutletId:" + outlet.getOutletId() + "Outlet Address: " + outlet.getAddress());
            }
            Long returnOutletId = scanner.nextLong();
           
            List<CarEntity> listOfSearchedCars = carSessionBeanRemote.findListOfCars(pickUpOutletId, returnOutletId, pickUpDate, returnDate);
            
            System.out.print("Choose car to by typing in its car id: \n");
            for (CarEntity car : listOfSearchedCars){
                 System.out.println("CarId:" + car.getCarId() + "Car Make and Model: " + car.getModel().getModelName() + ", " + car.getModel().getMakeName() );
            }
            Long response = scanner.nextLong();
            
       
        try {
            CarEntity carChosen = carSessionBeanRemote.retrieveCarById(response);
             carSessionBeanRemote.viewCarDetails(carChosen);
             Long totalAmountPayable = rentalRateSessionBeanRemote.calculateTotalCost(pickUpDate, returnDate, carChosen.getCategory().getRentalRates());
             System.out.println("Total amount payable: " + totalAmountPayable);
             System.out.println("*** Reservation Client :: Reserve Car? Y/N ***\n");
        
            String res = "";
           
                while (!"N".equals(res) || !"Y".equals(res)) {
                    System.out.print("> ");
                        
                    res = scanner.nextLine();
                     if (currentCustomer.isLoggedIn()){
                        if ("Y".equals(res)) {
                            reserveCar(carChosen, pickUpOutletId, returnOutletId, pickUpDate, returnDate,totalAmountPayable);
                        }
                        if ("N".equals(res)) {
                            break;
                        }
                     } else {
                          System.out.println("You are not logged in!");
                          break;
                }
            }
            } catch (CarNotFoundException ex) {
            Logger.getLogger(MainApp.class.getName()).log(Level.SEVERE, null, ex);
        }            
            
        } catch (ParseException | OutletNotFoundException ex) {
             System.out.println("Error: " + ex.getMessage() + "\n");
        }
    }
    
    public void reserveCar(CarEntity carChosen, Long pickUpOutletId, Long returnOutletId, Date pickUpDate, Date returnDate, Long totalAmountPayable){
//       CarEntity carToReserve = carSessionBeanRemote.retrieveCarById(carChosen.getCarId());
//       OutletEntity pickUpOutlet = outletSessionBeanRemote.retrieveOutletById(pickUpOutletId);
//       OutletEntity returnOutlet = outletSessionBeanRemote.retrieveOutletById(returnOutletId);
      
        Scanner scanner = new Scanner(System.in);
        String cardNumber = "";
        String cvv = "";
        String expDate ="";
        String cardName = "";
        
        CreditCard creditCard = new CreditCard();
        System.out.println("Please enter credit card details");
        System.out.println("Card Number:");
        cardNumber = scanner.nextLine().trim();
        creditCard.setCreditCardNum(cardNumber);
        
        System.out.println("Card Name:");
        cardName = scanner.nextLine().trim();
        creditCard.setCardName(cardName);
        
        System.out.println("CVV:");
        cvv = scanner.nextLine().trim();
        creditCard.setCVV(cvv);
        
        System.out.println("Expiry Date:");
        expDate = scanner.nextLine().trim();
        creditCard.setExpiryDate(expDate);
        try {
        
           Reservation reservation = new Reservation();
         
        reservation.setCustomer(currentCustomer);
        reservation.setPickUpDate(pickUpDate);
        reservation.setReturnDate(returnDate);
        reservation.setTotalCost(totalAmountPayable);
        
        
        Long reservationId = reservationSessionBeanRemote.creatNewReservation(reservation, carChosen.getCarId(), returnOutletId, pickUpOutletId);
            System.out.println("Reservation successful, reservation Id:" + reservationId); 
        
        
        } catch (CarNotFoundException | OutletNotFoundException | RentalRateNotFoundException ex) {
           System.out.println("Error: " + ex.getMessage() + "\n");
        }
    }
    
    private void menuMain() {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        
        while(true)
        {
            System.out.println("*** Main Menu :: Reservation Client ***\n");
            System.out.println("1: Search Car");
            System.out.println("2: Reserve Car");
            System.out.println("3: View All My Reservations");
            System.out.println("4: Logout\n");
            response = 0;
            
            while(response < 1 || response > 3)
            {
                System.out.print("> ");

                response = scanner.nextInt();

                if(response == 1)
                {
                    searchCar();
                }
                else if(response == 2)
                {
                    searchCar();
                }
                else if(response == 3)
                {
                   viewAllReservations();
                }
                else if(response == 4)
                {
                    customerLogout(currentCustomer);
                }
                else
                {
                    System.out.println("Invalid option, please try again!\n");                
                }
            }
            
            if(response == 4)
            {
                break;
            }
        }
    
        
    }
    
    private void viewAllReservations() {
        try {
            customerSessionBeanRemote.viewAllReservations(currentCustomer);
        } catch (CustomerNotFoundException ex) {
             System.out.println("Error: " + ex.getMessage() + "\n");
        }
    }
    
    private void registerAsCustomer(){
        Scanner scanner = new Scanner(System.in);
         String firstName = "";
        String lastName = "";
        String email = "";
        String contactNumber = "";
        String passportNumber = "";
        String password = "";
        Customer newCustomer = new Customer();
        
        System.out.println("*** Reservation Client :: Register ***\n");
        System.out.print("Enter first name> ");
        firstName = scanner.nextLine().trim();
        newCustomer.setFirstName(firstName);
        
        System.out.print("Enter last name> ");
        lastName = scanner.nextLine().trim();
        newCustomer.setLastName(lastName);
        
         System.out.print("Enter email> ");
        email = scanner.nextLine().trim();
        newCustomer.setEmail(email);
        
        System.out.print("Enter password> ");
        password = scanner.nextLine().trim();
        newCustomer.setPassword(password);
        
         System.out.print("Enter contactNumber> ");
        contactNumber = scanner.nextLine().trim();
        newCustomer.setContactNumber(contactNumber);
        
        System.out.print("Enter passportNumber> ");
        passportNumber = scanner.nextLine().trim();
        newCustomer.setPassportNumber(passportNumber);
        
        Long newCustomerId = customerSessionBeanRemote.createCustomer(newCustomer);
        System.out.println("New student created successfully!: " + newCustomerId + "\n");
    }
    
    private void customerLogout(Customer customer) {
        try {
            customerSessionBeanRemote.customerLogout(customer);
        } catch (CustomerNotFoundException | InvalidLoginCredentialException ex) {
            System.out.println("Error: " + ex.getMessage() + "\n");
        }
    }
}
