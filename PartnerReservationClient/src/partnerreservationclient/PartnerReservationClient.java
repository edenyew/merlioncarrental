/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package partnerreservationclient;

import static java.lang.System.gc;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import ws.client.car.CarEntity;
import ws.client.car.CarWebService;
import ws.client.car.CarWebService_Service;
import ws.client.car.OutletNotFoundException_Exception;
import ws.client.partner.InvalidLoginCredentialException_Exception;
import ws.client.partner.PartnerEntity;
import ws.client.partner.PartnerWebService;
import ws.client.partner.PartnerWebService_Service;
import ws.client.reservation.CreditCard;
import ws.client.reservation.CustomerNotFoundException;
import ws.client.reservation.OutletEntity;
import ws.client.reservation.Reservation;
import ws.client.reservation.ReservationNotFoundException_Exception;
import ws.client.reservation.ReservationWebService;
import ws.client.reservation.ReservationWebService_Service;

/**
 *
 * @author jonta
 */
public class PartnerReservationClient {
    
    
        
        
    public static void main(String[] args) throws DatatypeConfigurationException, OutletNotFoundException_Exception {
         
        ReservationWebService_Service reservationWebService_Service = new ReservationWebService_Service();
       ReservationWebService reservationWebServicePort = reservationWebService_Service.getReservationWebServicePort();
        PartnerWebService_Service partnerWebService_Service = new PartnerWebService_Service();
        PartnerWebService partnerWebServicePort = partnerWebService_Service.getPartnerWebServicePort();
        CarWebService_Service carWebService_Service = new CarWebService_Service();
        CarWebService carWebServicePort = carWebService_Service.getCarWebServicePort();
        
        PartnerEntity currentPartner;
        
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        
        while(true)
        {
            System.out.println("*** Welcome to Partner Resrvation Client ***\n");
            System.out.println("1: Login");
            System.out.println("2: Search Car");
             System.out.println("3: View Reservation Details");
              System.out.println("4: Reserve Car");
            System.out.println("5: Exit \n");
           
            response = 0;
            
            while(response < 1 || response > 3)
            {
                System.out.print("> ");

                response = scanner.nextInt();
                scanner.nextLine();
                if(response == 1)
                {
                    
                String email = "";
                String password = "";

                System.out.println("*** Partner Reservation Client :: Login ***\n");
                System.out.print("Enter email> ");
                email = scanner.nextLine().trim();
                System.out.print("Enter password> ");
                password = scanner.nextLine().trim();

                if(email.length() > 0 && password.length() > 0)
                {

                    try {
                         currentPartner = partnerWebServicePort.partnerLogin(email, password);
                        
                        System.out.println("Login successful!\n");
                    } catch (InvalidLoginCredentialException_Exception ex) {
                        System.out.println("An error has occurred: " + ex.getMessage() + "\n");
                }
            
        
                }
                else if(response == 2)
                {
                    
                    try {
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
                        
                        System.out.print("Enter pick up time in hours from 0-24 )> ");
                        pickUpTime = Integer.parseInt(scanner.nextLine().trim());
                        cal.setTime(pickUpDate);
                        cal.add(Calendar.HOUR_OF_DAY, pickUpTime);
                        Date pickUpTimeDate = cal.getTime();
                        
                        System.out.print("Enter return Date (DD/MM/YYYY)> ");
                        returnDateString = scanner.nextLine().trim();
                        Date returnDate = new SimpleDateFormat("dd/MM/yyyy").parse(returnDateString);

                        System.out.print("Enter pick up time in hours from 0-24> ");
                        returnTime = Integer.parseInt(scanner.nextLine().trim());
                        cal.setTime(returnDate);
                        cal.add(Calendar.HOUR_OF_DAY, returnTime);
                        Date returnTimeDate = cal.getTime();

                        System.out.print("Choose pickup outlet by typing in its outlet id: \n");
                        List<OutletEntity> listOfOutlets = reservationWebServicePort.retrieveAllOutlets();
                        for (OutletEntity outlet : listOfOutlets) {
                                System.out.println("OutletId: " + outlet.getOutletId() + ", Outlet Name: " + outlet.getAddress());
                            }
                            Long pickUpOutletId = scanner.nextLong();

                            System.out.print("Choose return outlet by typing in its outlet id: \n");
                            for (OutletEntity outlet : listOfOutlets) {
                                System.out.println("OutletId: " + outlet.getOutletId() + ", Outlet Address: " + outlet.getAddress());
                            }
                            Long returnOutletId = scanner.nextLong();
                            GregorianCalendar pickUp = new GregorianCalendar();
                            pickUp.setTime(pickUpDate);
                            XMLGregorianCalendar xmlDate = DatatypeFactory.newInstance().newXMLGregorianCalendar(pickUp);
                            GregorianCalendar returns = new GregorianCalendar();
                            returns.setTime(returnDate);
                            XMLGregorianCalendar xmlDate2 = DatatypeFactory.newInstance().newXMLGregorianCalendar(returns);
                            List<CarEntity> listOfSearchedCars = carWebServicePort.findListOfCars(pickUpOutletId, returnOutletId, xmlDate, xmlDate2);

                            System.out.print("List of available cars  at outlet chosen: \n");
                            for (CarEntity car : listOfSearchedCars) {
                                System.out.println("Car :" + car.getCategory().getName() + ", " + car.getModel().getModelName());
                            }
                            
//                             String cardNumber = "";
//                    String cvv = "";
//                    String expDate = "";
//                    String cardName = "";
//
//                    CreditCard creditCard = new CreditCard();
//                    System.out.println("Please enter credit card details before proceeding");
//                    System.out.println("Card Number:");
//                    cardNumber = scanner.nextLine().trim();
//                    creditCard.setCreditCardNum(cardNumber);
//
//                    System.out.println("Card Name:");
//                    cardName = scanner.nextLine().trim();
//                    creditCard.setCardName(cardName);
//
//                    System.out.println("CVV:");
//                    cvv = scanner.nextLine().trim();
//                    creditCard.setCVV(cvv);
//
//                    System.out.println("Expiry Date:");
//                    expDate = scanner.nextLine().trim();
//                    creditCard.setExpiryDate(expDate);
//                    Long creditCardId = reservationWebServicePort.createCreditCard(creditCard);
//
//                    int res = 0;
//                    System.out.println("Would you like to pay now using this credit card(1) or pay in store(2)? :");
//
//                    while (res < 1 || res > 2) {
//                        res = scanner.nextInt();
//                        System.out.println(res);
//                    }
//                    scanner.nextLine();
//                    Reservation reservation = new Reservation();
//                        
//                        //reservation.setPartner(currentPartner);
//                        reservation.setPickUpDate(xmlDate);
//                        //reservation.setPickUpTime(pickUpTime);
//                        reservation.setReturnDate(xmlDate2);
//                        //reservation.setReturnTime(returnTime);
//                        //reservation.setTotalCost(totalAmountPayable);
//
//                        if (res == 1) {
//                            reservation.setPaid(true);
//                        } else {
//                            reservation.setPaid(false);
//                        }
//
//                        Long reservationId = reservationWebServicePort.creatNewReservation(reservation, modelChosen.getModelId(), returnOutletId, pickUpOutletId, creditCardId, currentCustomer.getCustomerId(), finalRentalRatesApplied);
//                        System.out.println("Reservation successful, reservation Id:" + reservationId);
////                    
                        } catch (ParseException ex) {
                            System.out.println("An error has occurred: " + ex.getMessage() + "\n");
                        }
                    } else if (response == 3) {
                    
        
                    try {
                        System.out.println("Key in the Reservation ID to see the reservation details");
                        long reservationId = 0;
                        
                        System.out.println();
                        System.out.print("Enter Reservation ID> ");
                        reservationId = scanner.nextLong();
                        
                        Reservation reservation = reservationWebServicePort.retrieveReservationById(reservationId);
                        System.out.println("Reservation Id " + reservation.getId() + ": Model " + reservation.getModel().getModelName() + " " + reservation.getModel().getMakeName() + ", Pick up Date and Time: "  + reservation.getPickUpTime() + ", Pick up outlet at " + reservation.getPickUpOutlet().getAddress());
                    } catch (ReservationNotFoundException_Exception ex) {
                        System.out.println("An error has occurred: " + ex.getMessage() + "\n");
                       
                    }

       

                           } else if (response == 4) {
                                //partnerWebServicePort.partnerLogout(currentPartner);

                            } else if (response == 5) {
                                break;

                            } else {
                                    System.out.println("Invalid option, please try again!\n");
                                }
                            }

                    }
          if (response == 5) {
                    break;
                } 
         
        }
    } 
    
}
