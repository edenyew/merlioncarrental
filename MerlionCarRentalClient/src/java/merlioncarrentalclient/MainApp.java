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
import ejb.session.stateless.RentalRateSessionBeanRemote;
import ejb.session.stateless.TransitDriverDispatchRecordSessionBeanRemote;
import exception.InvalidLoginCredentialException;
import java.util.Scanner;
import entity.EmployeeEntity;
import exception.CarNotFoundException;
import exception.InvalidAccessRightException;

/**
 *
 * @author edenyew
 */
public class MainApp {

    
    private EmployeeEntitySessionBeanRemote employeeEntitySessionBeanRemote;
    private CarEntitySessionBeanRemote carEntitySessionBeanRemote;
    private CustomerSessionBeanRemote customerSessionBeanRemote;
    private CategorySessionBeanRemote categorySessionBeanRemote;
    private RentalRateSessionBeanRemote rentalRateSessionBeanRemote;
    private ModelSessionBeanRemote modelSessionBeanRemote;
    private OutletEntitySessionBeanRemote outletEntitySessionBeanRemote;
    private TransitDriverDispatchRecordSessionBeanRemote transitDriverDispatchRecordSessionBeanRemote;
    
    
    private SalesManagementModule salesManagementModule;
    private CustomerServiceModule customerServiceModule;
    
    private EmployeeEntity currentEmployeeEntity;
    
    
    public MainApp() {
    }

    public MainApp(EmployeeEntitySessionBeanRemote employeeEntitySessionBeanRemote, CarEntitySessionBeanRemote carEntitySessionBeanRemote, CustomerSessionBeanRemote customerSessionBeanRemote, CategorySessionBeanRemote categorySessionBeanRemote, RentalRateSessionBeanRemote rentalRateSessionBeanRemote, ModelSessionBeanRemote modelSessionBeanRemote, OutletEntitySessionBeanRemote outletEntitySessionBeanRemote, TransitDriverDispatchRecordSessionBeanRemote transitDriverDispatchRecordSessionBeanRemote) {
        this.employeeEntitySessionBeanRemote = employeeEntitySessionBeanRemote;
        this.carEntitySessionBeanRemote = carEntitySessionBeanRemote;
        this.customerSessionBeanRemote = customerSessionBeanRemote;
        this.categorySessionBeanRemote = categorySessionBeanRemote;
        this.rentalRateSessionBeanRemote = rentalRateSessionBeanRemote;
        this.modelSessionBeanRemote = modelSessionBeanRemote;
        this.outletEntitySessionBeanRemote = outletEntitySessionBeanRemote;
        this.transitDriverDispatchRecordSessionBeanRemote = transitDriverDispatchRecordSessionBeanRemote;
    }
    
    
    public void runApp() 
    {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        
        while(true)
        {
            System.out.println("*** Welcome to Merlion Car Rental System ***\n");
            System.out.println("1: Login");
            System.out.println("2: Exit\n");
            response = 0;
            
            while(response < 1 || response > 2)
            {
                System.out.print("> ");

                response = scanner.nextInt();

                if(response == 1)
                {
                    try
                    {
                        doLogin();
                        System.out.println("Login successful!\n");
                        
                        salesManagementModule = new SalesManagementModule(categorySessionBeanRemote, carEntitySessionBeanRemote, rentalRateSessionBeanRemote, modelSessionBeanRemote, outletEntitySessionBeanRemote, employeeEntitySessionBeanRemote, transitDriverDispatchRecordSessionBeanRemote);
                        customerServiceModule = new CustomerServiceModule(carEntitySessionBeanRemote, customerSessionBeanRemote, currentEmployeeEntity);
                    
                        menuMain();
                    }
                    catch(InvalidLoginCredentialException ex)
                    {
                        System.out.println("Invalid login credential: " + ex.getMessage() + "\n");
                    }
                }
                else if (response == 2)
                {
                    break;
                }
                else
                {
                    System.out.println("Invalid option, please try again!\n"); 
                }            
            }
            if (response == 2)
            {
                break;
            }
        }
    }
    
    
    private void doLogin() throws InvalidLoginCredentialException
    {
        Scanner scanner = new Scanner(System.in);
        String username = "";
        String password = "";
        
        System.out.println("*** Merlion Car Rental System :: Login ***\n");
        System.out.print("Enter username> ");
        username = scanner.nextLine().trim();
        System.out.print("Enter password> ");
        password = scanner.nextLine().trim();
        
        if(username.length() > 0 && password.length() > 0)
        {
            currentEmployeeEntity = employeeEntitySessionBeanRemote.employeeLogin(username, password);      
        }
        else
        {
            throw new InvalidLoginCredentialException("Missing login credential!");
        }
    }
    
    private void menuMain()
    {
         Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        
        while(true)
        {
            System.out.println("*** Point-of-Sale (POS) System (v3.1) ***\n");
            System.out.println("You are login as " + currentEmployeeEntity.getName()+ " " + " with " + currentEmployeeEntity.getAccessRightEnum().toString() + " rights\n");
            System.out.println("1: Sales Management");
            System.out.println("2: Customer Service");
            System.out.println("3: Logout\n");
            response = 0;
            
            while(response < 1 || response > 3)
            {
                System.out.print("> ");

                response = scanner.nextInt();

                if(response == 1)
                {
                    try
                    {
                        salesManagementModule.menuSalesManagement();
                    }
                    catch (InvalidAccessRightException | CarNotFoundException ex)
                    {
                        System.out.println("Invalid option, please try again!: " + ex.getMessage() + "\n");
                    }
                }
                else if (response == 2)
                {
                    try
                    {
                        customerServiceModule.menuCustomerService();
                    }
                    catch (InvalidAccessRightException ex)
                    {
                        System.out.println("Invalid option, please try again!: " + ex.getMessage() + "\n");
                    }
                 }
                else if (response == 3)
                {
                    break;
                }
                else
                {
                    System.out.println("Invalid option, please try again!\n");                
                }
            }
            if(response == 3)
            {
                break;
            }
        }
    }    
}
