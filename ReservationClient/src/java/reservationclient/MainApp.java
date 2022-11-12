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
import javax.persistence.NoResultException;
import ejb.session.stateless.ModelSessionBeanRemote;
import entity.Model;
import exception.ModelNotFoundException;
import exception.ReservationNotFoundException;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.HashSet;
import ejb.session.stateless.CustomerEntitySessionBeanRemote;
import exception.CreditCardNotFoundException;
import exception.InputDataValidationException;
import exception.UnknownPersistenceException;

/**
 *
 * @author jonta
 */
public class MainApp {

    @EJB
    private CustomerSessionBeanRemote customerSessionBeanRemote;
    @EJB
     private CustomerEntitySessionBeanRemote customerSessionBeanRemote;
    @EJB 
    private ReservationSessionBeanRemote reservationSessionBeanRemote;
    @EJB
    private CarEntitySessionBeanRemote carSessionBeanRemote;
    @EJB
    private OutletEntitySessionBeanRemote outletSessionBeanRemote;
    @EJB
    private RentalRateSessionBeanRemote rentalRateSessionBeanRemote;
    @EJB
    private CreditCardSessionBeanRemote creditCardSessionBeanRemote;
    @EJB
    private ModelSessionBeanRemote modelSessionBeanRemote;
    private Customer currentCustomer;

    public MainApp() {
    }

    public MainApp(CustomerEntitySessionBeanRemote customerSessionBeanRemote, ReservationSessionBeanRemote reservationSessionBeanRemote, CarEntitySessionBeanRemote carSessionBeanRemote, OutletEntitySessionBeanRemote outletSessionBeanRemote, RentalRateSessionBeanRemote rentalRateSessionBeanRemote, CreditCardSessionBeanRemote creditCardSessionBeanRemote, ModelSessionBeanRemote modelSessionBeanRemote) {
        this();
        this.customerSessionBeanRemote = customerSessionBeanRemote;
        this.reservationSessionBeanRemote = reservationSessionBeanRemote;
        this.carSessionBeanRemote = carSessionBeanRemote;
        this.outletSessionBeanRemote = outletSessionBeanRemote;
        this.rentalRateSessionBeanRemote = rentalRateSessionBeanRemote;
        this.creditCardSessionBeanRemote = creditCardSessionBeanRemote;
        this.modelSessionBeanRemote = modelSessionBeanRemote;
    }

    public void runApp() {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;

        while (true) {
            System.out.println("*** Welcome to Customer Resrvation Client ***\n");
            System.out.println("1: Register as new Customer");
            System.out.println("2: Login");
            System.out.println("3: Search Car");
            System.out.println("4: Exit \n");

            response = 0;

            while (response < 1 || response > 4) {
                System.out.print("> ");

                response = scanner.nextInt();

                if(response == 1)
                {
                    try
                    {
                        registerAsCustomer();
                    }
                    catch (CustomerNotFoundException | UnknownPersistenceException | InputDataValidationException ex)
                    {
                        System.out.println("Invalid login credential: " + ex.getMessage() + "\n");
                    }
                }
                else if(response == 2)
                {
                    try {                     
                        customerLogin();    
                    } catch (InvalidLoginCredentialException ex) {

                        System.out.println("Invalid login credential: " + ex.getMessage() + "\n");
                    }
                } else if (response == 3) {
                    searchCar();

                } else if (response == 4) {
                    break;

                } else {
                    System.out.println("Invalid option, please try again!\n");
                }

            }
            if (response == 4) {
                break;
            }

        }
    }

    private void customerLogin() throws InvalidLoginCredentialException {
        Scanner scanner = new Scanner(System.in);
        String email = "";
        String password = "";

        System.out.println("*** Reservation Client :: Login ***\n");
        System.out.print("Enter email> ");
        email = scanner.nextLine().trim();
        System.out.print("Enter password> ");
        password = scanner.nextLine().trim();

        if (email.length() > 0 && password.length() > 0) {
            try {

                currentCustomer = customerSessionBeanRemote.customerLogin(email, password);
                System.out.println("Login successful!\n");
                menuMain();
            } catch (CustomerNotFoundException | AlreadyLoggedInException | NoResultException ex) {

                System.out.println("Error: " + ex.getMessage() + "\n");
            }
        } else {
            throw new InvalidLoginCredentialException("Missing login credential!");
        }
    }

    private void searchCar() {
        try {
            Scanner scanner = new Scanner(System.in);
            String pickUpDateString = "";
            String returnDateString = "";
            int pickUpTime = 0;
            int returnTime = 0;

            Calendar cal = Calendar.getInstance(); // creates calendar
            // returns new date object plus one hour
            System.out.println("*** Reservation Client :: Search Car ***\n");
            System.out.print("Enter pick up date (DD/MM/YYYY)> ");
            pickUpDateString = scanner.nextLine().trim();
            Date pickUpDate = new SimpleDateFormat("dd/MM/yyyy").parse(pickUpDateString);

            System.out.print("Enter pick up time (in Hours ie 0 - 24)> ");
            pickUpTime = Integer.parseInt(scanner.nextLine().trim());
            cal.setTime(pickUpDate);
            cal.add(Calendar.HOUR_OF_DAY, pickUpTime);
            Date pickUpTimeDate = cal.getTime();

            System.out.print("Enter return Date (DD/MM/YYYY)> ");
            returnDateString = scanner.nextLine().trim();
            Date returnDate = new SimpleDateFormat("dd/MM/yyyy").parse(returnDateString);

            System.out.print("Enter pick up time (in Hours ie 0 - 24)> ");
            returnTime = Integer.parseInt(scanner.nextLine().trim());
            cal.setTime(returnDate);
            cal.add(Calendar.HOUR_OF_DAY, returnTime);
            Date returnTimeDate = cal.getTime();

//            DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");  
//                String strDate = dateFormat.format(pickUpDate);  
//                System.out.println("Converted String: " + strDate);
            System.out.print("Choose pickup outlet by typing in its outlet id: \n");
            List<OutletEntity> listOfOutlets = outletSessionBeanRemote.retrieveAllOutlets();
            for (OutletEntity outlet : listOfOutlets) {
                System.out.println("OutletId: " + outlet.getOutletId() + ", Outlet Name: " + outlet.getAddress());
            }
            Long pickUpOutletId = scanner.nextLong();

            System.out.print("Choose return outlet by typing in its outlet id: \n");
            for (OutletEntity outlet : listOfOutlets) {
                System.out.println("OutletId: " + outlet.getOutletId() + ", Outlet Address: " + outlet.getAddress());
            }
            Long returnOutletId = scanner.nextLong();

            List<CarEntity> listOfSearchedCars = carSessionBeanRemote.findListOfCars(pickUpOutletId, returnOutletId, pickUpDate, returnDate);
            HashSet<Model> setOfSearchedModels = new HashSet<>();
            for (CarEntity car : listOfSearchedCars) {
                if (!setOfSearchedModels.contains(car.getModel())) {
                    setOfSearchedModels.add(car.getModel());
                }
            }
            System.out.print("Choose car by typing in its model id: \n");
            for (Model model : setOfSearchedModels) {
                System.out.println("Model ID " + model.getModelId() + ": " + "Car Category, Make and Model: " + model.getCategory().getName() + "," + model.getMakeName() + ", " + model.getModelName());
            }
            Long response = scanner.nextLong();
            scanner.nextLine();

            try {
                //CarEntity carChosen = carSessionBeanRemote.retrieveCarById(response);
                Model modelChosen = modelSessionBeanRemote.retrieveModelById(response);
                //List<RentalRate> listOfRentalRates = rentalRateSessionBeanRemote.retrieveRentalRatesOfCarCategory(modelChosen.getCategory().getCategoryId());
                List<RentalRate> finalRentalRatesApplied = rentalRateSessionBeanRemote.getEligibleRentalRatesForReservation(pickUpDate, pickUpTimeDate, returnDate, returnTimeDate, modelChosen.getCategory().getCategoryId());
                Long totalAmountPayable = rentalRateSessionBeanRemote.calculateTotalCost(finalRentalRatesApplied);
                System.out.println("Total amount payable: " + totalAmountPayable);
                System.out.println("*** Reservation Client :: Reserve Car? Y/N ***\n");

                String res = "";

                res = scanner.nextLine();
                if (!"N".equals(res) || !"Y".equals(res)) {
<<<<<<< HEAD
              
                     if (currentCustomer != null && currentCustomer.isLoggedIn()){
                        if ("Y".equals(res)) 
                        {
                            try
                            {
                                reserveCar(modelChosen, pickUpOutletId, returnOutletId, pickUpDate, pickUpTimeDate, returnDate, returnTimeDate, finalRentalRatesApplied,totalAmountPayable);
                            }
                            catch (ReservationNotFoundException | CreditCardNotFoundException | UnknownPersistenceException | InputDataValidationException ex)
                            {
                                System.out.println("Error: " + ex.getMessage() + "\n");
                            }
=======

                    if (currentCustomer != null && currentCustomer.isLoggedIn()) {
                        if ("Y".equals(res)) {
                            reserveCar(modelChosen, pickUpOutletId, returnOutletId, pickUpDate, pickUpTimeDate, returnDate, returnTimeDate, finalRentalRatesApplied, totalAmountPayable);
>>>>>>> 84e262e81fd2b7e98d0eafe77b0d1dd8ec784341
                        }

                    } else {
                        if ("Y".equals(res)) {

                            System.out.println("You are not logged in!");
                        }

                    }
                }
            } catch (ModelNotFoundException ex) {
                System.out.println("Error: " + ex.getMessage() + "\n");
            }

        } catch (ParseException | OutletNotFoundException ex) {
            System.out.println("Error: " + ex.getMessage() + "\n");
        }
    }
<<<<<<< HEAD
    
    public void reserveCar(Model modelChosen, Long pickUpOutletId, Long returnOutletId, Date pickUpDate, Date pickUpTime, Date returnDate, Date returnTime, List<RentalRate> finalRentalRatesApplied, Long totalAmountPayable) throws CreditCardNotFoundException, UnknownPersistenceException, InputDataValidationException, ReservationNotFoundException{
=======

    public void reserveCar(Model modelChosen, Long pickUpOutletId, Long returnOutletId, Date pickUpDate, Date pickUpTime, Date returnDate, Date returnTime, List<RentalRate> finalRentalRatesApplied, Long totalAmountPayable) {
>>>>>>> 84e262e81fd2b7e98d0eafe77b0d1dd8ec784341
//       CarEntity carToReserve = carSessionBeanRemote.retrieveCarById(carChosen.getCarId());
//       OutletEntity pickUpOutlet = outletSessionBeanRemote.retrieveOutletById(pickUpOutletId);
//       OutletEntity returnOutlet = outletSessionBeanRemote.retrieveOutletById(returnOutletId);

        Scanner scanner = new Scanner(System.in);
        String cardNumber = "";
        String cvv = "";
        String expDate = "";
        String cardName = "";

        CreditCard creditCard = new CreditCard();
        System.out.println("Please enter credit card details before proceeding");
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
        Long creditCardId = creditCardSessionBeanRemote.createNewCreditCard(creditCard);

        int res = 0;
        System.out.println("Would you like to pay now using this credit card(1) or pay in store(2)? :");

        while (res < 1 || res > 2) {
            res = scanner.nextInt();
            System.out.println(res);
        }
        scanner.nextLine();
        try {

            Reservation reservation = new Reservation();

            reservation.setCustomer(currentCustomer);
            reservation.setPickUpDate(pickUpDate);
            reservation.setPickUpTime(pickUpTime);
            reservation.setReturnDate(returnDate);
            reservation.setReturnTime(returnTime);
            reservation.setTotalCost(totalAmountPayable);

            if (res == 1) {
                reservation.setPaid(true);
            } else {
                reservation.setPaid(false);
            }

            Long reservationId = reservationSessionBeanRemote.creatNewReservation(reservation, modelChosen.getModelId(), returnOutletId, pickUpOutletId, creditCardId, currentCustomer.getCustomerId(), finalRentalRatesApplied);
            System.out.println("Reservation successful, reservation Id:" + reservationId);

        } catch (ModelNotFoundException | OutletNotFoundException | RentalRateNotFoundException | CustomerNotFoundException ex) {
            System.out.println("Error: " + ex.getMessage() + "\n");
        }
    }

    private void menuMain() {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;

        while (true) {
            System.out.println("*** Main Menu :: Reservation Client ***\n");
            System.out.println("1: Search Car");
            System.out.println("2: Reserve Car");
            System.out.println("3: View All My Reservations");
            System.out.println("4: View Reservation Details");
            System.out.println("5: Cancel A Reservation");
            System.out.println("6: Logout\n");
            response = 0;

            while (currentCustomer.isLoggedIn() && (response < 1 || response > 6)) {
                System.out.print("> ");

                response = scanner.nextInt();

                if (response == 1) {
                    searchCar();
                } else if (response == 2) {
                    searchCar();
                } else if (response == 3) {
                    viewAllReservations();
                } else if (response == 4) {
                    viewReservationDetails();
                } else if (response == 5) {
                    cancelReservation();
                } else if (response == 6) {
                    customerLogout(currentCustomer);
                } else {
                    System.out.println("Invalid option, please try again!\n");
                }
            }

            if (response == 6) {
                break;
            }
        }

    }

    private void viewAllReservations() {
        try {
            List<Reservation> listOfReservations = reservationSessionBeanRemote.retrieveReservationsByCustomer(currentCustomer.getCustomerId());
            for (Reservation reservation : listOfReservations) {
                System.out.println("Reservation Id " + reservation.getId() + ": Model " + reservation.getModel().getModelName() + " " + reservation.getModel().getMakeName());
            }

        } catch (ReservationNotFoundException | CustomerNotFoundException ex) {
            System.out.println("Error: " + ex.getMessage() + "\n");
        }
    }

    private void viewReservationDetails() {
        try {
            Scanner scanner = new Scanner(System.in);
            viewAllReservations();
            System.out.println("Key in the Reservation ID to see the reservation details");
            long reservationId = 0;

            System.out.println();
            System.out.print("Enter Reservation ID> ");
            reservationId = scanner.nextLong();

            Reservation reservation = reservationSessionBeanRemote.retrieveReservationById(reservationId);
            System.out.println("Reservation Id " + reservation.getId() + ": Model " + reservation.getModel().getModelName() + " " + reservation.getModel().getMakeName() + ", Pick up Date on " + reservation.getPickUpDate() + "Pick up Time at:  " + reservation.getPickUpTime() + "Pick up outlet at " + reservation.getPickUpOutlet().getAddress());

        } catch (ReservationNotFoundException ex) {
            System.out.println("Error: " + ex.getMessage() + "\n");
        }
    }

    private void cancelReservation() {
        try {
            Scanner scanner = new Scanner(System.in);
            viewAllReservations();
            System.out.println("Key in the Reservation ID to cancel the reservation: ");
            long reservationId = 0;

            System.out.println();
            System.out.print("Enter Reservation ID> ");
            reservationId = scanner.nextLong();
            Reservation reservation = reservationSessionBeanRemote.retrieveReservationById(reservationId);

            Date date = new Date();
            double penaltyPayable = reservationSessionBeanRemote.cancelReservation(reservation, date);
            System.out.println("penalty charged to creditcard: $" + String.format("%.2f", penaltyPayable));
        } catch (ReservationNotFoundException ex) {
            Logger.getLogger(MainApp.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
<<<<<<< HEAD
    
    private void registerAsCustomer() throws CustomerNotFoundException, UnknownPersistenceException, InputDataValidationException{
=======

    private void registerAsCustomer() {
>>>>>>> 84e262e81fd2b7e98d0eafe77b0d1dd8ec784341
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
            currentCustomer = customerSessionBeanRemote.customerLogout(customer);
        } catch (CustomerNotFoundException | InvalidLoginCredentialException ex) {
            System.out.println("Error: " + ex.getMessage() + "\n");
        }
    }
}
