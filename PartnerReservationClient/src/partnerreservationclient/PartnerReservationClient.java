/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package partnerreservationclient;

import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import ws.client.car.CarWebService;
import ws.client.car.CarWebService_Service;
import ws.client.partner.InvalidLoginCredentialException_Exception;
import ws.client.partner.PartnerEntity;
import ws.client.partner.PartnerWebService;
import ws.client.partner.PartnerWebService_Service;
import ws.client.reservation.CustomerNotFoundException;
import ws.client.reservation.ReservationWebService;
import ws.client.reservation.ReservationWebService_Service;

/**
 *
 * @author jonta
 */
public class PartnerReservationClient {

        
        
    public static void main(String[] args) {
         
        ReservationWebService_Service reservationWebService_Service = new ReservationWebService_Service();
       ReservationWebService reservationWebServicePort = reservationWebService_Service.getReservationWebServicePort();
        PartnerWebService_Service partnerWebService_Service = new PartnerWebService_Service();
        PartnerWebService partnerWebServicePort = partnerWebService_Service.getPartnerWebServicePort();
        CarWebService_Service carWebService_Service = new CarWebService_Service();
        CarWebService carWebServicePort = carWebService_Service.getCarWebServicePort();
        
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        
        while(true)
        {
            System.out.println("*** Welcome to Partner Resrvation Client ***\n");
            System.out.println("1: Login");
            System.out.println("2: Search Car");
            System.out.println("3: Exit \n");
           
            response = 0;
            
            while(response < 1 || response > 3)
            {
                System.out.print("> ");

                response = scanner.nextInt();

                if(response == 1)
                {
                    
                String email = "";
                String password = "";

                System.out.println("*** Partner Reservation Client :: Login ***\n");
                System.out.println("Enter email> ");
                email = scanner.nextLine().trim();
                System.out.print("Enter password> ");
                password = scanner.nextLine().trim();

                if(email.length() > 0 && password.length() > 0)
                {

                    try {
                        PartnerEntity partner = partnerWebServicePort.partnerLogin(email, password);
                        System.out.println("Login successful!\n");
                    } catch (InvalidLoginCredentialException_Exception ex) {
                        Logger.getLogger(PartnerReservationClient.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            
        
                }
                else if(response == 2)
                {
                    //searchCar();
//                    
                } else if (response == 3) {
                    break;
                    
                } else {
                    System.out.println("Invalid option, please try again!\n");
                }
                if (response == 3) {
                    break;
                }
            }
//            
//           
        }
    }
         private void partnerLogin(PartnerWebService port) 
    {
        
    
    }
    
    
//    
    
    

}
