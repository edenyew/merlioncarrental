/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package merlioncarrentalclient;

import ejb.session.stateless.CarEntitySessionBeanRemote;
import ejb.session.stateless.CategorySessionBeanRemote;
import ejb.session.stateless.EmployeeEntitySessionBeanRemote;
import ejb.session.stateless.ModelSessionBeanRemote;
import ejb.session.stateless.OutletEntitySessionBeanRemote;
import ejb.session.stateless.RentalRateSessionBeanRemote;
import ejb.session.stateless.ReservationSessionBeanRemote;
import ejb.session.stateless.TransitDriverDispatchRecordSessionBeanRemote;
import entity.CarEntity;
import entity.Category;
import entity.EmployeeEntity;
import entity.Model;
import entity.OutletEntity;
import entity.RentalRate;
import entity.Reservation;
import entity.TransitDriverDispatchRecord;
import exception.CarNotFoundException;
import exception.DeleteModelException;
import exception.DeleteRentalRateException;
import exception.EmployeeNotFoundException;
import exception.InputDataValidationException;
import exception.InvalidAccessRightException;
import exception.ModelNotFoundException;
import exception.OutletNotFoundException;
import exception.RentalRateNotFoundException;
import exception.ReservationNotFoundException;
import exception.TransitRecordNotFoundException;
import exception.UnknownPersistenceException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import javax.validation.Validation;
import util.enumeration.AccessRightEnum;
import util.enumeration.CarStatusEnum;
import util.enumeration.RentalRateTypeEnum;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

/**
 *
 * @author edenyew
 */
public class SalesManagementModule {

    private CategorySessionBeanRemote categorySessionBeanRemote;
    private CarEntitySessionBeanRemote carEntitySessionBeanRemote;
    private RentalRateSessionBeanRemote rentalRateSessionBeanRemote;
    private ModelSessionBeanRemote modelSessionBeanRemote;
    private OutletEntitySessionBeanRemote outletEntitySessionBeanRemote;
    private EmployeeEntitySessionBeanRemote employeeEntitySessionBeanRemote;
    private TransitDriverDispatchRecordSessionBeanRemote transitDriverDispatchRecordSessionBeanRemote;
    private ReservationSessionBeanRemote reservationSessionBeanRemote;
    
    private EmployeeEntity currentEmployeeEntity;
    
    private final ValidatorFactory validatorFactory;
    private final Validator validator;
    
    public SalesManagementModule() 
    {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }
    

    public SalesManagementModule(CategorySessionBeanRemote categorySessionBeanRemote, CarEntitySessionBeanRemote carEntitySessionBeanRemote, RentalRateSessionBeanRemote rentalRateSessionBeanRemote, ModelSessionBeanRemote modelSessionBeanRemote, OutletEntitySessionBeanRemote outletEntitySessionBeanRemote, EmployeeEntitySessionBeanRemote employeeEntitySessionBeanRemote, TransitDriverDispatchRecordSessionBeanRemote transitDriverDispatchRecordSessionBeanRemote, EmployeeEntity currentEmployeeEntity, ReservationSessionBeanRemote reservationSessionBeanRemote)
    {
        this();
        this.categorySessionBeanRemote = categorySessionBeanRemote;
        this.carEntitySessionBeanRemote = carEntitySessionBeanRemote;
        this.rentalRateSessionBeanRemote = rentalRateSessionBeanRemote;
        this.modelSessionBeanRemote = modelSessionBeanRemote;
        this.outletEntitySessionBeanRemote = outletEntitySessionBeanRemote;
        this.employeeEntitySessionBeanRemote = employeeEntitySessionBeanRemote;
        this.transitDriverDispatchRecordSessionBeanRemote = transitDriverDispatchRecordSessionBeanRemote;
        this.currentEmployeeEntity = currentEmployeeEntity;
        this.reservationSessionBeanRemote = reservationSessionBeanRemote;
    }
    
    public void menuSalesManagement() throws InvalidAccessRightException, CarNotFoundException
    {
        if (currentEmployeeEntity.getAccessRightEnum() == AccessRightEnum.SALES_MANAGER)
        {
            doSalesManagerMenuAdministration();
        }
        else if (currentEmployeeEntity.getAccessRightEnum() == AccessRightEnum.OPERATIONS_MANAGER)
        {
            doOperationsManagerMenuAdministration();
        }
        else
        {
            throw new InvalidAccessRightException("You don't have OPERTAIONS MANAGER or SALES MANAGER rights to access the system administration module.");
        }
    }
    
    
    // Sales Manager
    
    private void doSalesManagerMenuAdministration()
    {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        
        while(true)
        {
            System.out.println("*** Merlion Car Rental :: Sales Manager Menu Administration ***\n");
            System.out.println("1: Create Rental Rate");
            System.out.println("2: View All Rental Rates");
            System.out.println("3: View Rental Rate Details");
            System.out.println("4: Update Rental Rate");
            System.out.println("5: Delete Rental Rate");
            System.out.println("6: Back\n");
            response = 0;
            
             while(response < 1 || response > 7)
            {
                System.out.print("> ");

                response = scanner.nextInt();

                if(response == 1)
                {
                    try
                    {
                        doCreateRentalRate(); // check this part again, does car and category need to be mandatory? if they are how to enforce when creating rental rate
                    }
                    catch(RentalRateNotFoundException | UnknownPersistenceException | InputDataValidationException | CarNotFoundException | ParseException ex)
                    {
                        System.out.println("An error has occurred: " + ex.getMessage() + "\n");
                    }
                    
                }
                else if(response == 2)
                {
                    try
                    {
                        viewAllRentalRates();
                    }
                    catch(RentalRateNotFoundException ex)
                    {
                        System.out.println("An error has occurred: " + ex.getMessage() + "\n");
                    }
                }
                else if(response == 3)
                {
                    try
                    {
                        viewRentalRateDetails();
                    }
                    catch(CarNotFoundException | RentalRateNotFoundException ex)
                    {
                        System.out.println("An error has occurred: " + ex.getMessage() + "\n");
                    }
                    
                }
                else if(response == 4)
                {
                    try
                    {
                        updateRentalRate();
                    }
                    catch(ParseException | CarNotFoundException | RentalRateNotFoundException | InputDataValidationException ex)
                    {
                        System.out.println("An error has occurred: " + ex.getMessage() + "\n");
                    }
                }
                else if(response == 5)
                {
                    try
                    {
                        deleteRentalRate();
                    }
                    catch (DeleteRentalRateException | RentalRateNotFoundException ex)
                    {
                        System.out.println("An error has occurred: " + ex.getMessage() + "\n");
                    }
                }
                else if (response == 6)
                {
                    break;
                }
                else
                {
                    System.out.println("Invalid option, please try again!\n");      
                }
            }
            if (response == 6)
            {
                break;
            }
        }
    }
    
    private void doCreateRentalRate() throws CarNotFoundException, RentalRateNotFoundException, UnknownPersistenceException, InputDataValidationException, ParseException
    {
        Scanner scanner = new Scanner(System.in);
        RentalRate newRentalRate = new RentalRate();
        Integer categoryResponse = 1;
        CarEntity car = new CarEntity();
        
        System.out.println("*** Merlion Car Rental System :: Sales Management :: Create New Rental Rate ***\n");
        System.out.print("Enter Name> ");
        newRentalRate.setName(scanner.nextLine().trim());
        System.out.print("Enter Rate Per Day> ");
        newRentalRate.setRatePerDay(scanner.nextLong());
        
        
        List<RentalRateTypeEnum> listOfRentalRateTypes = Arrays.asList(RentalRateTypeEnum.class.getEnumConstants());
        for (int i=0; i <listOfRentalRateTypes.size();i++){
            System.out.println(i + ": " +listOfRentalRateTypes.get(i));
        }
        System.out.print("Enter a RentalRate Type> ");
         RentalRateTypeEnum rentalRateType = listOfRentalRateTypes.get(scanner.nextInt());
        newRentalRate.setRentalRateType(rentalRateType);
        
        System.out.print("Enter Category> \n");
        List<Category> category = categorySessionBeanRemote.retrieveAllCategory();
        
        for (Category chosenCategory : category)
        {
            System.out.println(categoryResponse + ": " + chosenCategory.getName());
            categoryResponse++;
        }
         categoryResponse = scanner.nextInt();
        scanner.nextLine();
            Calendar cal = Calendar.getInstance(); // creates calendar
           
           
            if (!rentalRateType.equals(RentalRateTypeEnum.DEFAULT)){
                System.out.print("Enter start date (DD/MM/YYYY)> ");
                String pickUpDateString = scanner.nextLine().trim();
                Date pickUpDate = new SimpleDateFormat("dd/MM/yyyy").parse(pickUpDateString);

                System.out.print("Enter start time in hours from 0-24 )> ");
                int  pickUpTime = Integer.parseInt(scanner.nextLine().trim());
                cal.setTime(pickUpDate);
                cal.add(Calendar.HOUR_OF_DAY, pickUpTime);
                Date pickUpTimeDate = cal.getTime();

                System.out.print("Enter end Date (DD/MM/YYYY)> ");
                String returnDateString = scanner.nextLine().trim();
                Date returnDate = new SimpleDateFormat("dd/MM/yyyy").parse(returnDateString);

                System.out.print("Enter end time in hours from 0-24> ");
                int returnTime = Integer.parseInt(scanner.nextLine().trim());
                cal.setTime(returnDate);
                cal.add(Calendar.HOUR_OF_DAY, returnTime);
                Date returnTimeDate = cal.getTime();

                newRentalRate.setEndDate(returnDate);
                newRentalRate.setEndTime(returnTimeDate);
                newRentalRate.setStartDate(pickUpDate);
                newRentalRate.setStartTime(pickUpTimeDate);
            }
            
       ;
       
        newRentalRate.setCategory(category.get(categoryResponse - 1));
        newRentalRate.setDisabled(false);
        newRentalRate.setInUse(false);
        //System.out.print("Enter Car Plate Number To Add Car> ");
        
        Set<ConstraintViolation<RentalRate>>constraintViolations = validator.validate(newRentalRate);
        
        if(constraintViolations.isEmpty())
        {
            try
            {
                rentalRateSessionBeanRemote.createRentalRate(newRentalRate, new Long(categoryResponse)); 
                System.out.print("RentalRate succesfully created!");
            }
            catch(UnknownPersistenceException ex)
            {
                System.out.println("An unknown error has occurred while creating the new customer!: " + ex.getMessage() + "\n");
            }
            catch(InputDataValidationException ex)
            {
                System.out.println(ex.getMessage() + "\n");
            }
        }
        else
        {
            showInputDataValidationErrorsForRentalRate(constraintViolations);
        }
    }
    
    private void viewAllRentalRates() throws RentalRateNotFoundException
    {
       
        System.out.println("*** Merlion Car Rental System :: Sales Management :: View Details of Car Rental Rate ***\n");
        for (RentalRate currentRates : rentalRateSessionBeanRemote.retrieveAllRentalRate()) {
            System.out.println("Rental Rate ID:> " + currentRates.getId());
            System.out.println("Rental Rate Name:> " + currentRates.getName());
            //System.out.println("Rental Rate Rate per day:> " + currentRates.getRatePerDay());
        }

    }
    
    
    private Long viewRentalRateDetails() throws CarNotFoundException, RentalRateNotFoundException
    {
        Scanner scanner = new Scanner(System.in);
        RentalRate rentalRate = new RentalRate();
        
        
        System.out.print("Enter Rental Rate ID:> ");
        long res = scanner.nextLong();
        rentalRate = rentalRateSessionBeanRemote.retrieveRentalRateByRentalRateId(res); 
        System.out.println("Name :" + rentalRate.getName());
        System.out.println("CarCategory :" + rentalRate.getCategory().getName());
        System.out.println("RatePerDay :" + rentalRate.getRatePerDay());
        System.out.println("Rental Rate Type :" + rentalRate.getRentalRateType());
        if (rentalRate.getStartDate() != null || rentalRate.getEndDate() != null ){
            System.out.println("StartDateTime :" + rentalRate.getStartDate());
            System.out.println("EndDateTime :" + rentalRate.getEndDate());
        } else {
            System.out.println("No Start or End Time, Available all the time!");
            
        }
        
        return rentalRate.getId();
    }
    
    public void updateRentalRate() throws ParseException, CarNotFoundException, RentalRateNotFoundException, InputDataValidationException
    {
        Scanner scanner = new Scanner(System.in);
        
        
        String startDate = "";
        String endDate = "";
        Long response;
        
        System.out.println("*** Merlion Car Rental System :: Sales Management :: Update Rental Rate ***\n");
        
        Long rentalRateId = viewRentalRateDetails();
        RentalRate rentalRateToUpdate = rentalRateSessionBeanRemote.retrieveRentalRateByRentalRateId(rentalRateId);
        System.out.print("Enter Name To Update> ");
        
        rentalRateToUpdate.setName(scanner.nextLine().trim());
       
        System.out.print("Enter Rate Per Day To Update> ");
        rentalRateToUpdate.setRatePerDay(scanner.nextLong());
        scanner.nextLine();
        
//        System.out.print("Enter Car Category To Update> \n");
//        List<Category> allCategories = categorySessionBeanRemote.retrieveAllCategory();
//        for (Category category : allCategories)
//        {
//            System.out.println(category.getCategoryId() + ": " + category.getName());
//        }
//        response = new Long(scanner.nextLine());
//        rentalRateToUpdate.setCategory(categorySessionBeanRemote.retrieveCategoryById(response));
        
//        if (rentalRateToUpdate.getStartDate() != null || rentalRateToUpdate.getStartDate() != null){
//            System.out.print("Enter StartDate (DD/MM/YYYY) To Update> ");
//            startDate = scanner.nextLine().trim();
//            Date startingDate = new SimpleDateFormat("dd/MM/yyyy").parse(startDate);
//
//            rentalRateToUpdate.setStartDate(startingDate);
//
//            System.out.print("Enter EndDate (DD/MM/YYYY) To Update> ");
//            endDate = scanner.nextLine().trim();
//            Date endingDate = new SimpleDateFormat("dd/MM/yyyy").parse(endDate);
//
//
//            rentalRateToUpdate.setEndDate(endingDate);
//        }
        
        Set<ConstraintViolation<RentalRate>>constraintViolations = validator.validate(rentalRateToUpdate);
        
        if(constraintViolations.isEmpty())
        {
            try
            {
                rentalRateSessionBeanRemote.updateRentalRate(rentalRateToUpdate);
            }
            catch(InputDataValidationException ex)
            {
                System.out.println(ex.getMessage() + "\n");
            }
        }
        else
        {
            showInputDataValidationErrorsForRentalRate(constraintViolations);
        }
    }
    
    public void deleteRentalRate() throws DeleteRentalRateException, RentalRateNotFoundException 
    {
        Scanner scanner = new Scanner(System.in);
        List<RentalRate> allRentalRate = rentalRateSessionBeanRemote.retrieveAllRentalRate();
        Long response;
        
        System.out.println("*** Merlion Car Rental System :: Sales Management :: Delete Rental Rate ***\n");
        System.out.println("Enter Rental Rate ID> ");
        for (RentalRate rentalRate : allRentalRate)
        {
            System.out.println(rentalRate.getId() + ": " + rentalRate.getName() + ", " + rentalRate.getCategory().getName());
        }
        
        response = scanner.nextLong();
        RentalRate rentalRateToDelete = rentalRateSessionBeanRemote.retrieveRentalRateByRentalRateId(response);
        rentalRateSessionBeanRemote.deleteRentalRate(rentalRateToDelete);
    }
    
    
    
    
    // Operations Manager
    
    private void doOperationsManagerMenuAdministration() throws CarNotFoundException
    {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        
        while(true)
        {
            System.out.println("*** Merlion Car Rental :: Operations Manager Menu Administration ***\n");
            System.out.println("1: Create New Model");
            System.out.println("2: View All Models");
            System.out.println("3: Update Model");
            System.out.println("4: Delete Model");
            System.out.println("-----------------------------");
            System.out.println("5: Create New Car");
            System.out.println("6: View All Cars");
            System.out.println("7: View Car Details");
            System.out.println("8: Update Car");
            System.out.println("9: Delete Car");
            System.out.println("-----------------------------");
            System.out.println("10: View Transit Driver Dispatch Records for Current Day Reservations");
            System.out.println("11: Assign Transit Driver");
            System.out.println("12: Update Transit As Completed");
            System.out.println("13: Allocate Cars To Current Day Reservation (Demo)");
            System.out.println("14: Back\n");
            response = 0;
            
            while (response < 1 || response > 14)
            {
                 System.out.print("> ");

                response = scanner.nextInt();

                if (response == 1)
                {
                    try
                    {
                        createNewModel();
                    }
                    catch (ModelNotFoundException | UnknownPersistenceException | InputDataValidationException ex)
                    {
                        System.out.println("An error has occurred: " + ex.getMessage() + "\n");
                    }
                }
                else if (response == 2)
                {
                    viewAllModels();
                }
                else if (response == 3)
                {
                    try
                    {
                        updateModel();
                    }
                    catch (ModelNotFoundException | InputDataValidationException ex)
                    {
                        System.out.println("An error has occurred: " + ex.getMessage() + "\n");
                    }
                    
                }
                else if (response == 4)
                {
                    try
                    {
                        deleteModel();
                    }
                    catch (DeleteModelException | ModelNotFoundException ex)
                    {
                        System.out.println("An error has occurred: " + ex.getMessage() + "\n");
                    }
                }
                else if (response == 5)
                {
                    try
                    {
                        createNewCar();
                    }
                    catch (CarNotFoundException | UnknownPersistenceException | InputDataValidationException | OutletNotFoundException | RentalRateNotFoundException | ModelNotFoundException ex)
                    {
                        System.out.println("An error has occurred: " + ex.getMessage() + "\n");
                    }
                }
                else if (response == 6)
                {
                    try
                    {
                        viewAllCars();
                    }
                    catch(CarNotFoundException ex)
                    {
                        System.out.println("An error has occurred: " + ex.getMessage() + "\n");
                    }
                }
                else if (response == 7)
                {
                    try
                    {
                        viewCarDetails();
                    }
                    catch(CarNotFoundException ex)
                    {
                        System.out.println("An error has occurred: " + ex.getMessage() + "\n");  
                    }
                }
                else if (response == 8)
                {
                    try
                    {
                        updateCar();
                    }
                    catch(ModelNotFoundException | CarNotFoundException | InputDataValidationException ex)
                    {
                        System.out.println("An error has occurred: " + ex.getMessage() + "\n"); 
                    }
                    
                }
                else if (response == 9)
                {
                    try
                    {
                        deleteCar();
                    }
                    catch(CarNotFoundException | ModelNotFoundException | InputDataValidationException ex)
                    {
                        System.out.println("An error has occurred: " + ex.getMessage() + "\n");
                    }
                    
                }
                else if (response == 10)
                {
                    try
                    {
                        viewTransitDriverDispatchRecordsForCurrentDayReservations();
                    }
                    catch (TransitRecordNotFoundException | OutletNotFoundException | ParseException ex)
                    {
                        System.out.println("An error has occurred: " + ex.getMessage() + "\n");
                    }
                }
                else if (response == 11)
                {
                    try
                    {
                        assignTransitDriver();
                    }
                    catch (UnknownPersistenceException | InputDataValidationException | EmployeeNotFoundException | TransitRecordNotFoundException | OutletNotFoundException | CarNotFoundException | ParseException ex)
                    {
                        System.out.println("An error has occurred: " + ex.getMessage() + "\n");
                    }
                }
                else if (response == 12)
                {
                    try
                    {
                        updateTransitAsCompleted();
                    }
                    catch(CarNotFoundException | EmployeeNotFoundException | TransitRecordNotFoundException | InputDataValidationException ex)
                    {
                        System.out.println("An error has occurred: " + ex.getMessage() + "\n");
                    }
                    
                }
                else if (response == 13)
                {
                     try {
                         allocateCarsToCurrentDayReservations();
                     } catch (ParseException | ReservationNotFoundException | OutletNotFoundException | TransitRecordNotFoundException | UnknownPersistenceException | InputDataValidationException ex) {
                          System.out.println("An error has occurred: " + ex.getMessage() + "\n");
                     }
                }
                else if (response == 14)
                {
                    break;
                }
                else
                {
                    System.out.println("Invalid option, please try again!\n");      
                }
            }
            if (response == 14)
            {
                break;
            }
        }
    }
    
    private void createNewModel() throws ModelNotFoundException, UnknownPersistenceException, InputDataValidationException
    {
        Scanner scanner = new Scanner(System.in);
        Model newModel = new Model();
        System.out.println("*** Merlion Car Rental System :: Operations Management :: Create New Model ***\n");
        System.out.print("Enter Model Name> ");
        newModel.setModelName(scanner.nextLine().trim());
        System.out.print("Enter Make Name> ");
        newModel.setMakeName(scanner.nextLine().trim());        
        
        List<Category> allCategory = categorySessionBeanRemote.retrieveAllCategory();
        Integer categoryChoice = 1;
        for (Category category : allCategory)
        {
            System.out.println(categoryChoice + ": " + category.getName() + ", " + category.getCategoryId());
            categoryChoice++;
        }
        categoryChoice = scanner.nextInt();
        newModel.setCars(new ArrayList<>());
        newModel.setInUse(false);
        newModel.setDisabled(false);
        
        Set<ConstraintViolation<Model>>constraintViolations = validator.validate(newModel);
        
        if(constraintViolations.isEmpty())
        {
            try
            {
                modelSessionBeanRemote.createNewModel(newModel, new Long(categoryChoice));
            }
            catch(UnknownPersistenceException ex)
            {
                System.out.println("An unknown error has occurred while creating the new customer!: " + ex.getMessage() + "\n");
            }
            catch(InputDataValidationException ex)
            {
                System.out.println(ex.getMessage() + "\n");
            }
        }
        else
        {
            showInputDataValidationErrorsForModel(constraintViolations);
        }
    }
    
    private void viewAllModels()
    {
        System.out.println("*** Merlion Car Rental System :: Operations Management :: View All Model ***\n");
        List<Model> allModels = modelSessionBeanRemote.retrieveAllModels();
        int i=1;
        for (Model model : allModels)
        {
            System.out.println( i + ": " + model.getCategory().getName()+ ", " + model.getMakeName()+ ", " + model.getModelName() + "-- Model ID of this car is " + model.getModelId());
            i++;
        }
    }
    
    private void updateModel() throws ModelNotFoundException, InputDataValidationException
    {
        Scanner scanner = new Scanner(System.in);      
       
        System.out.println("*** Merlion Car Rental System :: Operations Management :: Update Model ***\n");
        System.out.print("Enter Model Id > ");
        Model modelToUpdate = modelSessionBeanRemote.retrieveModelById(Long.parseLong(scanner.nextLine().trim()));
        
        Long response;
        System.out.print("Enter Make To Update> ");
        modelToUpdate.setMakeName(scanner.nextLine().trim());
        
        System.out.print("Enter Model To Update> ");
        modelToUpdate.setModelName(scanner.nextLine().trim());
        
        System.out.print("Enter Category> \n");
        List<Category> allCategories = categorySessionBeanRemote.retrieveAllCategory();
        for (Category category : allCategories)
        {
            System.out.println(category.getCategoryId() + ": " + category.getName());
        }
        response = scanner.nextLong();
        modelToUpdate.setCategory(categorySessionBeanRemote.retrieveCategoryById(response));
        
         Set<ConstraintViolation<Model>>constraintViolations = validator.validate(modelToUpdate);
        
        if(constraintViolations.isEmpty())
        {
            try
            {
                modelSessionBeanRemote.updateModel(modelToUpdate);
                System.out.println("Model successfully updated!");
            }
            catch(InputDataValidationException ex)
            {
                System.out.println(ex.getMessage() + "\n");
            }
        }
        else
        {
            showInputDataValidationErrorsForModel(constraintViolations);
        }
    }
    
    private void deleteModel() throws ModelNotFoundException, DeleteModelException
    {
        Scanner scanner = new Scanner(System.in);
        Model modelToDelete = new Model();
        Long response;
        System.out.println("*** Merlion Car Rental System :: Operations Management :: Delete Model ***\n");
        
        List<Model> allModels = modelSessionBeanRemote.retrieveAllModels();
        for(Model chosenModel : allModels)
        {
            System.out.println(chosenModel.getModelId() + ": " + chosenModel.getModelName() + ", " + chosenModel.getMakeName());
        }
        
        response = scanner.nextLong();
        modelToDelete = modelSessionBeanRemote.retrieveModelById(response);
        
        modelSessionBeanRemote.deleteModel(modelToDelete);
        
        System.out.println("Model successfully deleted!");
    }
    
    
    private void createNewCar() throws ModelNotFoundException, RentalRateNotFoundException, OutletNotFoundException, InputDataValidationException, UnknownPersistenceException, CarNotFoundException
    {
        Scanner scanner = new Scanner(System.in);
        CarEntity newCar = new CarEntity();
        System.out.println("*** Merlion Car Rental System :: Operations Management :: Create New Car ***\n");
        
        System.out.print("Enter Car Plate Number> ");
        newCar.setCarPlateNumber(scanner.nextLine().trim());
        
        System.out.print("Enter Colour> ");
        newCar.setColour(scanner.nextLine().trim());
        
//        System.out.print("Enter Location> ");
//        newCar.setLocation(scanner.nextLine().trim());
        
//        System.out.print("Enter Disable? Y/N> ");
//        if (scanner.nextLine().trim().compareToIgnoreCase("Y") == 0)
//        {
//            newCar.setDisabled(Boolean.TRUE);
//        }
//        else
//        {
//            newCar.setDisabled(Boolean.FALSE);
//        }
        
        newCar.setCurrentStatus(CarStatusEnum.NOT_IN_USE);
        newCar.setDisabled(false);
        List<Model> allModels = modelSessionBeanRemote.retrieveAllModels();
        
        for (Model model : allModels) 
        {
            System.out.println(model.getModelId() + ": " + model.getMakeName() + ", " + model.getModelName());
        }
        
        Long modelId = scanner.nextLong();
              
        
        List<OutletEntity> allOutlets = outletEntitySessionBeanRemote.retrieveAllOutlets();
        
        for (OutletEntity outlet : allOutlets) 
        {
            System.out.println(outlet.getOutletId()+ ": " + outlet.getAddress());
        }
        
        Long outletId = scanner.nextLong();
        
        Set<ConstraintViolation<CarEntity>>constraintViolations = validator.validate(newCar);
        
        if(constraintViolations.isEmpty())
        {
            try
            {
                carEntitySessionBeanRemote.createNewCar(newCar, modelId, outletId);
            }
            catch(UnknownPersistenceException ex)
            {
                System.out.println("An unknown error has occurred while creating the new customer!: " + ex.getMessage() + "\n");
            }
            catch(InputDataValidationException ex)
            {
                System.out.println(ex.getMessage() + "\n");
            }
        }
        else
        {
            showInputDataValidationErrorsForCar(constraintViolations);
        }
    }
    
    
    private void viewAllCars() throws CarNotFoundException
    {
        System.out.println("*** Merlion Car Rental System :: Operations Management :: View All Car Records ***\n");
        List<CarEntity> allCars =  carEntitySessionBeanRemote.retrieveAllCars();
        for (CarEntity car : allCars)
        {
             System.out.println("CarId: " + car.getCarId() + ", LicensePlateNumber: " + car.getCarPlateNumber() + ", Make: " + car.getModel().getMakeName() + ", Model: " + car.getModel().getModelName());
        }
    }
    
    private long viewCarDetails() throws CarNotFoundException
    {
        Scanner scanner = new Scanner(System.in);
        CarEntity car = new CarEntity();
        System.out.println("*** Merlion Car Rental System :: Operations Management :: View Car Details ***\n");
        
        
        System.out.print("Enter Car Plate Number To View Car Details> ");
        car = carEntitySessionBeanRemote.retrieveCarByPlateNumber(scanner.nextLine().trim()); 
        
        System.out.println("*** View Car Details: ***\n");
        System.out.println("CarId: " + car.getCarId()); 
        System.out.println("LicensePlateNumber: " + car.getCarPlateNumber()); 
        System.out.println("Make: " + car.getModel().getMakeName()); 
        System.out.println("Model: " + car.getModel().getModelName()); 
        System.out.println("Status: " + car.getCurrentStatus()); 
        System.out.println("Outlet: " + car.getOutletEntity().getAddress());

        return car.getCarId();
    }
    
    private void updateCar() throws ModelNotFoundException, CarNotFoundException, InputDataValidationException
    {
        Scanner scanner = new Scanner(System.in);
        Long response;      

        System.out.println("*** Merlion Car Rental System :: Operations Management :: Update Car ***\n");
//        System.out.print("Enter CarId Id > ");        
        long carId = viewCarDetails();
        CarEntity carToUpdate = carEntitySessionBeanRemote.retrieveCarById(carId);
        
        System.out.print("Enter Plate Number To Update> ");
        carToUpdate.setCarPlateNumber(scanner.nextLine().trim());
        
//        System.out.print("Select Make and Model To Update> ");
//        List<Model> allModels = modelSessionBeanRemote.retrieveAllModels();
//        for (Model model : allModels)
//        {
//            System.out.println(model.getModelId()+ ": " + model.getMakeName() + ", " + model.getModelName());
//        }
//        response = scanner.nextLong();
//        Model chosenModel = modelSessionBeanRemote.retrieveModelById(response);
//        carToUpdate.setModel(chosenModel);

        Set<ConstraintViolation<CarEntity>>constraintViolations = validator.validate(carToUpdate);
        
        if(constraintViolations.isEmpty())
        {
            try
            {
                carEntitySessionBeanRemote.updateCarEntity(carToUpdate);
                System.out.println("Car successfully updated!");
            }
            catch(InputDataValidationException ex)
            {
                System.out.println(ex.getMessage() + "\n");
            }
        }
        else
        {
            showInputDataValidationErrorsForCar(constraintViolations);
        }
    }
    
    
    private void deleteCar() throws CarNotFoundException, ModelNotFoundException, InputDataValidationException
    {
        Scanner scanner = new Scanner(System.in);
        CarEntity carToDelete = new CarEntity();
        System.out.println("*** Merlion Car Rental System :: Operations Management :: Delete Car ***\n");
        
        System.out.print("Enter Plate Number To Delete> ");
        carToDelete = carEntitySessionBeanRemote.retrieveCarByPlateNumber(scanner.nextLine().trim());
        
        carEntitySessionBeanRemote.deleteCarEntity(carToDelete);
        
        System.out.println("Car successfully deleted!");
        
    }
    
    
    private void viewTransitDriverDispatchRecordsForCurrentDayReservations() throws OutletNotFoundException, ParseException, TransitRecordNotFoundException
    {
        Scanner scanner = new Scanner(System.in);
        System.out.println("*** Merlion Car Rental System :: Operations Management :: View Transit Driver Dispatch Records For Today's Reservation ***\n");
        System.out.println("Select Outlet To View Records> ");
        Long outletId;
        String todayDateString = "";
        List<OutletEntity> allOutlets = outletEntitySessionBeanRemote.retrieveAllOutlets();
        
        for (OutletEntity outlet : allOutlets)
        {
            System.out.println(outlet.getOutletId() + ": " + outlet.getAddress());
        }
        outletId = Long.parseLong(scanner.nextLine().trim());
   
        System.out.print("Enter today's date (DD/MM/YYYY)> ");
        todayDateString = scanner.nextLine().trim();
        Date currDate = new SimpleDateFormat("dd/MM/yyyy").parse(todayDateString);
        
        List<TransitDriverDispatchRecord> transitDriverDispatchRecords = transitDriverDispatchRecordSessionBeanRemote.viewCurrentDayTransitRecord(currDate, outletId);
        for (TransitDriverDispatchRecord transitDriverRecord : transitDriverDispatchRecords)
        {
            transitDriverDispatchRecordSessionBeanRemote.viewRecordDetails(transitDriverRecord);
        }
    }
    
    
    private void assignTransitDriver() throws EmployeeNotFoundException, TransitRecordNotFoundException, OutletNotFoundException, CarNotFoundException, UnknownPersistenceException, InputDataValidationException, ParseException
    {
        Scanner scanner = new Scanner(System.in);

        String employeeUsername = "";
        
        
        System.out.println("*** Merlion Car Rental System :: Operations Management :: Assign Transit Driver ***\n");
        viewTransitDriverDispatchRecordsForCurrentDayReservations();
        System.out.print("Enter Employee Username> ");
        employeeUsername = scanner.nextLine().trim();
        EmployeeEntity employee = employeeEntitySessionBeanRemote.retrieveEmployeeByUsername(employeeUsername);
        
       List<TransitDriverDispatchRecord> allTransitDriverRecords = transitDriverDispatchRecordSessionBeanRemote.retrieveAllTransitDriverRecord();
        
        System.out.print("Select Dispatch Record To Assign Transit Driver To> ");
        for( TransitDriverDispatchRecord  transitDriverDispatchRecord : allTransitDriverRecords){
            System.out.println(transitDriverDispatchRecord.getTransitDriverDispatchId() + ", " + transitDriverDispatchRecord.getPickUpOutlet().getAddress() + ", " + transitDriverDispatchRecord.getReturnOutlet().getAddress());
        }
        Long response = scanner.nextLong();
        TransitDriverDispatchRecord record = transitDriverDispatchRecordSessionBeanRemote.retrieveTransitRecordById(response);

       transitDriverDispatchRecordSessionBeanRemote.assignTransitDriver(record, employee.getId());
        
        //transitDriverDispatchRecordSessionBeanRemote.assignTransitDriver(record, employee.getId());
        
    }
    
    
    private  void updateTransitAsCompleted() throws CarNotFoundException, EmployeeNotFoundException, TransitRecordNotFoundException, InputDataValidationException
    {
        Scanner scanner = new Scanner(System.in);

        String employeeUsername = "";
        
        String returnedCarPlateNumber = "";
        
        System.out.println("*** Merlion Car Rental System :: Operations Management :: Update Transit As Completed ***\n");
        
        System.out.print("Enter Employee Username> ");
        employeeUsername = scanner.nextLine().trim();
        EmployeeEntity employee = employeeEntitySessionBeanRemote.retrieveEmployeeByUsername(employeeUsername);
        
        System.out.print("Enter Returned Car Plate Number> ");
        returnedCarPlateNumber = scanner.nextLine().trim();
        CarEntity returnedCar = carEntitySessionBeanRemote.retrieveCarByPlateNumber(returnedCarPlateNumber);
        
        TransitDriverDispatchRecord transitDriverRecord = transitDriverDispatchRecordSessionBeanRemote.retrieveTransitRecordByEmployee(employee);
        
        if (transitDriverRecord.getCar().equals(returnedCar))
        {
            
            Set<ConstraintViolation<TransitDriverDispatchRecord>>constraintViolations = validator.validate(transitDriverRecord);
        
            if(constraintViolations.isEmpty())
            {
                try
                {
                    transitDriverDispatchRecordSessionBeanRemote.updateTransitAsComplete(transitDriverRecord);
                }
                catch(InputDataValidationException ex)
                {
                    System.out.println(ex.getMessage() + "\n");
                }
            }
            else
            {
                showInputDataValidationErrorsForTransitDriverDispatchRecord(constraintViolations);
            }
        }
        else
        {
            throw new TransitRecordNotFoundException ("Transit Record Not Found!");
        }
    }
    public void allocateCarsToCurrentDayReservations() throws ParseException, ReservationNotFoundException, OutletNotFoundException, CarNotFoundException, TransitRecordNotFoundException, UnknownPersistenceException, InputDataValidationException {
        
            Scanner scanner = new Scanner(System.in);
            System.out.println("Enter current date: ");
            String currentDateString = scanner.nextLine().trim();
            Date currDate = new SimpleDateFormat("dd/MM/yyyy").parse(currentDateString);
            
            List<Reservation> reservations = reservationSessionBeanRemote.retrieveReservationsOnPickUpDate(currDate);
            for(Reservation reservation :reservations){
                Model model = reservation.getModel();
                for (CarEntity car : model.getCars()) {
                    if (car.getOutletEntity().equals(reservation.getPickUpOutlet()) && car.getCurrentStatus().equals(CarStatusEnum.NOT_IN_USE)){
                        for (Reservation existingReservation : car.getReservations()) {
                            if (existingReservation.getPickUpDate().compareTo(currDate) < 0 && existingReservation.getReturnDate().compareTo(currDate) > 0) {
                               reservation.setCar(car);
                               car.getReservations().add(reservation);
                               System.out.println(car.getCarPlateNumber() + " allocated to " +  reservation.getId());
                            }
                        }
                    } else {
                         TransitDriverDispatchRecord record = new TransitDriverDispatchRecord();
                        Long pickUpOutletId = car.getOutletEntity().getOutletId();
                        Long returnOutletId = reservation.getPickUpOutlet().getOutletId();
                         transitDriverDispatchRecordSessionBeanRemote.createNewTransitRecord(record, pickUpOutletId, returnOutletId, car.getCarId());
                         System.out.println(reservation.getId() + " allocated to " +  car.getCarPlateNumber());
                        }
                        
                    }
            }       
      
    }
    private void showInputDataValidationErrorsForRentalRate(Set<ConstraintViolation<RentalRate>>constraintViolations)
    {
        System.out.println("\nInput data validation error!:");
            
        for(ConstraintViolation constraintViolation:constraintViolations)
        {
            System.out.println("\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage());
        }

        System.out.println("\nPlease try again......\n");
    }
    
    private void showInputDataValidationErrorsForModel(Set<ConstraintViolation<Model>>constraintViolations)
    {
        System.out.println("\nInput data validation error!:");
            
        for(ConstraintViolation constraintViolation:constraintViolations)
        {
            System.out.println("\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage());
        }

        System.out.println("\nPlease try again......\n");
    }
    
    private void showInputDataValidationErrorsForCar(Set<ConstraintViolation<CarEntity>>constraintViolations)
    {
        System.out.println("\nInput data validation error!:");
            
        for(ConstraintViolation constraintViolation:constraintViolations)
        {
            System.out.println("\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage());
        }

        System.out.println("\nPlease try again......\n");
    }
    
    private void showInputDataValidationErrorsForTransitDriverDispatchRecord(Set<ConstraintViolation<TransitDriverDispatchRecord>>constraintViolations)
    {
        System.out.println("\nInput data validation error!:");
            
        for(ConstraintViolation constraintViolation:constraintViolations)
        {
            System.out.println("\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage());
        }

        System.out.println("\nPlease try again......\n");
    }
}