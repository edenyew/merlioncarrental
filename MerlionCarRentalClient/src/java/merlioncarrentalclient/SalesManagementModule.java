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
import ejb.session.stateless.TransitDriverDispatchRecordSessionBeanRemote;
import entity.CarEntity;
import entity.Category;
import entity.EmployeeEntity;
import entity.Model;
import entity.OutletEntity;
import entity.RentalRate;
import entity.TransitDriverDispatchRecord;
import exception.CarNotFoundException;
import exception.DeleteModelException;
import exception.EmployeeNotFoundException;
import exception.InvalidAccessRightException;
import exception.ModelNotFoundException;
import exception.OutletNotFoundException;
import exception.RentalRateNotFoundException;
import exception.TransitRecordNotFoundException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import util.enumeration.AccessRightEnum;
import util.enumeration.CarStatusEnum;

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
    
    private EmployeeEntity currentEmployeeEntity;
    
    
    public SalesManagementModule() {
    }
    
    // add constructor with parameters
    
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
                    catch(CarNotFoundException ex)
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
                    catch(ParseException | CarNotFoundException | RentalRateNotFoundException ex)
                    {
                        System.out.println("An error has occurred: " + ex.getMessage() + "\n");
                    }
                }
                else if(response == 5)
                {
//                    deleteRentalRate();
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
    
    private void doCreateRentalRate() throws CarNotFoundException
    {
        Scanner scanner = new Scanner(System.in);
        RentalRate newRentalRate = new RentalRate();
        Integer categoryResponse = 1;
        CarEntity car = new CarEntity();
        
        System.out.println("*** Merlion Car Rental System :: Sales Management :: Create New Rental Rate ***\n");
        System.out.print("Enter Name> ");
        newRentalRate.setName(scanner.nextLine().trim());
        System.out.print("Enter Rate Per Day> ");
        newRentalRate.setRatePerDay(scanner.nextBigDecimal());
        System.out.print("Enter Validity Period> ");
        newRentalRate.setValidityPeriod(scanner.nextLine().trim());
        
        System.out.print("Enter In Use? Y/N> ");
        if (scanner.nextLine().trim().compareToIgnoreCase("Y") == 0)
        {
            newRentalRate.setInUse(Boolean.TRUE);
        }
        else
        {
            newRentalRate.setInUse(Boolean.FALSE);
        }
        
        System.out.print("Enter Disable? Y/N> ");
        if (scanner.nextLine().trim().compareToIgnoreCase("Y") == 0)
        {
            newRentalRate.setDisabled(Boolean.TRUE);
        }
        else
        {
            newRentalRate.setDisabled(Boolean.FALSE);
        }
        
        System.out.print("Enter Category> \n");
        List<Category> category = categorySessionBeanRemote.retrieveAllCategory();
        
        for (Category chosenCategory : category)
        {
            System.out.println(categoryResponse + ": " + chosenCategory.getName());
            categoryResponse++;
        }
        categoryResponse = scanner.nextInt();
        newRentalRate.setCategory(category.get(categoryResponse - 1));
        
        System.out.print("Enter Car Plate Number To Add Car> ");
        
        car = carEntitySessionBeanRemote.retrieveCarByPlateNumber(scanner.nextLine().trim()); 
        newRentalRate.setCar(car);
        
        rentalRateSessionBeanRemote.createRentalRate(newRentalRate); 
    }
    
    private void viewAllRentalRates() throws RentalRateNotFoundException
    {
        System.out.println("*** View All Car Records Rental Rate: ***\n");
        List<RentalRate> rentalRates = rentalRateSessionBeanRemote.retrieveAllRentalRate();
        
        for(RentalRate rentalRate : rentalRates)
        {
            rentalRateSessionBeanRemote.viewRentalRateDetails(rentalRate);
            System.out.println("");
        }
        
    }
    
    
    private void viewRentalRateDetails() throws CarNotFoundException, RentalRateNotFoundException
    {
        Scanner scanner = new Scanner(System.in);
        CarEntity car = new CarEntity();
        
        System.out.println("*** Merlion Car Rental System :: Sales Management :: View Details of Car Rental Rate ***\n");
        System.out.print("Enter Car Plate Number To View Rental Rate Details> ");
        
        car = carEntitySessionBeanRemote.retrieveCarByPlateNumber(scanner.nextLine().trim()); 
        rentalRateSessionBeanRemote.viewRentalRateDetails(car.getRentalRate());
    }
    
    public void updateRentalRate() throws ParseException, CarNotFoundException, RentalRateNotFoundException
    {
        Scanner scanner = new Scanner(System.in);
        
        RentalRate rentalRateToUpdate = new RentalRate();
        String startDate = "";
        String endDate = "";
        Long response;
        
        System.out.println("*** Merlion Car Rental System :: Sales Management :: Update Rental Rate ***\n");
        
        viewRentalRateDetails();
        
        System.out.print("Enter Name To Update> ");
        
        rentalRateToUpdate.setName(scanner.nextLine().trim());
        
        System.out.print("Enter Car Category To Update> ");
        List<Category> allCategories = categorySessionBeanRemote.retrieveAllCategory();
        for (Category category : allCategories)
        {
            System.out.println(category.getCategoryId() + ": " + category.getName());
        }
        response = scanner.nextLong();
        rentalRateToUpdate.setCategory(categorySessionBeanRemote.retrieveCategoyrById(response));
        
        System.out.print("Enter StartDate (DD/MM/YYYY) To Update> ");
        startDate = scanner.nextLine().trim();
        Date startingDate = new SimpleDateFormat("dd/MM/yyyy").parse(startDate);
        
//        rentalRateToUpdate.setStartDate(startingDate);
        
        System.out.print("Enter EndDate (DD/MM/YYYY) To Update> ");
        endDate = scanner.nextLine().trim();
        Date endingDate = new SimpleDateFormat("dd/MM/yyyy").parse(endDate);
        
        
//        rentalRateToUpdate.setEndDate(endingDate);

        rentalRateSessionBeanRemote.updateRentalRate(rentalRateToUpdate);
        
    }
    
/*
    public void deleteRentalRate() throws CarNotFoundException, RentalRateNotFoundException 
    {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("*** Merlion Car Rental System :: Sales Management :: Delete Rental Rate ***\n");
    }
*/
    
    
    
    
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
            System.out.println("13: Back\n");
            response = 0;
            
            while (response < 1 || response > 13)
            {
                 System.out.print("> ");

                response = scanner.nextInt();

                if (response == 1)
                {
                    createNewModel();
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
                    catch (ModelNotFoundException ex)
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
                    catch (OutletNotFoundException | RentalRateNotFoundException | ModelNotFoundException ex)
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
                    catch(ModelNotFoundException | CarNotFoundException ex)
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
                    catch(CarNotFoundException ex)
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
                    catch (OutletNotFoundException | ParseException ex)
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
                    catch (EmployeeNotFoundException | TransitRecordNotFoundException ex)
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
                    catch(CarNotFoundException | EmployeeNotFoundException | TransitRecordNotFoundException ex)
                    {
                        System.out.println("An error has occurred: " + ex.getMessage() + "\n");
                    }
                    
                }
                else if (response == 13)
                {
                    break;
                }
                else
                {
                    System.out.println("Invalid option, please try again!\n");      
                }
            }
            if (response == 13)
            {
                break;
            }
        }
    }
    
    private void createNewModel()
    {
        Scanner scanner = new Scanner(System.in);
        Model newModel = new Model();
        System.out.println("*** Merlion Car Rental System :: Operations Management :: Create New Model ***\n");
        System.out.print("Enter Model Name> ");
        newModel.setModelName(scanner.nextLine().trim());
        System.out.print("Enter Make Name> ");
        newModel.setMakeName(scanner.nextLine().trim());
        System.out.print("Enter In Use? Y/N> ");
        if (scanner.nextLine().trim().compareToIgnoreCase("Y") == 0)
        {
            newModel.setInUse(Boolean.TRUE);
        }
        else
        {
            newModel.setInUse(Boolean.FALSE);
        }
        
        System.out.print("Enter Disable? Y/N> ");
        if (scanner.nextLine().trim().compareToIgnoreCase("Y") == 0)
        {
            newModel.setDisabled(Boolean.TRUE);
        }
        else
        {
            newModel.setDisabled(Boolean.FALSE);
        }
        List<Category> allCategory = categorySessionBeanRemote.retrieveAllCategory();
        Integer categoryChoice = 1;
        for (Category category : allCategory)
        {
            System.out.println(categoryChoice + ": " + category.getName() + ", " + category.getCategoryId());
            categoryChoice++;
        }
        categoryChoice = scanner.nextInt();
        newModel.setCars(new ArrayList<>());
        
        modelSessionBeanRemote.createNewModel(newModel, new Long(categoryChoice));
    }
    
    private void viewAllModels()
    {
        System.out.println("*** Merlion Car Rental System :: Operations Management :: View All Model ***\n");
        List<Model> allModels = modelSessionBeanRemote.retrieveAllModels();
        for (Model model : allModels)
        {
            System.out.println(model.getMakeName()+ ", " + model.getModelName()+ ", " + model.getCategory().getName());
        }
    }
    
    private void updateModel() throws ModelNotFoundException
    {
        Scanner scanner = new Scanner(System.in);
        Model modelToUpdate = new Model();
        Long response;
        System.out.println("*** Merlion Car Rental System :: Operations Management :: Update Model ***\n");
        
        System.out.print("Enter Make> ");
        modelToUpdate.setMakeName(scanner.nextLine().trim());
        
        System.out.print("Enter Model> ");
        modelToUpdate.setModelName(scanner.nextLine().trim());
        
        System.out.print("Enter Category> \n");
        List<Category> allCategories = categorySessionBeanRemote.retrieveAllCategory();
        for (Category category : allCategories)
        {
            System.out.println(category.getCategoryId() + ": " + category.getName());
        }
        response = scanner.nextLong();
        modelToUpdate.setCategory(categorySessionBeanRemote.retrieveCategoyrById(response));
        modelSessionBeanRemote.updateModel(modelToUpdate);
        
        System.out.println("Model successfully updated!");
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
    
    
    private void createNewCar() throws ModelNotFoundException, RentalRateNotFoundException, OutletNotFoundException
    {
        Scanner scanner = new Scanner(System.in);
        CarEntity newCar = new CarEntity();
        System.out.println("*** Merlion Car Rental System :: Operations Management :: Create New Car ***\n");
        
        System.out.print("Enter Car Plate Number> ");
        newCar.setCarPlateNumber(scanner.nextLine().trim());
        
        System.out.print("Enter Colour> ");
        newCar.setColour(scanner.nextLine().trim());
        
        System.out.print("Enter Location> ");
        newCar.setLocation(scanner.nextLine().trim());
        
        System.out.print("Enter Disable? Y/N> ");
        if (scanner.nextLine().trim().compareToIgnoreCase("Y") == 0)
        {
            newCar.setDisabled(Boolean.TRUE);
        }
        else
        {
            newCar.setDisabled(Boolean.FALSE);
        }
        
        newCar.setCurrentStatus(CarStatusEnum.NOT_IN_USE);
        
        List<Model> allModels = modelSessionBeanRemote.retrieveAllModels();
        
        for (Model model : allModels) 
        {
            System.out.println(model.getModelId() + ": " + model.getMakeName() + ", " + model.getModelName());
        }
        
        Long modelId = scanner.nextLong();
        
        List<Category> allCategory = categorySessionBeanRemote.retrieveAllCategory();
        
        for (Category category : allCategory) 
        {
            System.out.println(category.getCategoryId()+ ": " + category.getName());
        }
        
        Long categoryId = scanner.nextLong();
        
        List<RentalRate> allRentalRate = rentalRateSessionBeanRemote.retrieveAllRentalRate();
        
        for (RentalRate rentalRate : allRentalRate) 
        {
            System.out.println(rentalRate.getId()+ ": " + rentalRate.getName() + ", " + rentalRate.getRatePerDay());
        }
        
        Long rentalRateId = scanner.nextLong();
        
        
        List<OutletEntity> allOutlets = outletEntitySessionBeanRemote.retrieveAllOutlets();
        
        for (OutletEntity outlet : allOutlets) 
        {
            System.out.println(outlet.getOutletId()+ ": " + outlet.getAddress());
        }
        
        Long outletId = scanner.nextLong();
        
        carEntitySessionBeanRemote.createNewCar(newCar, modelId, outletId, rentalRateId);
        
    }
    
    
    private void viewAllCars() throws CarNotFoundException
    {
        System.out.println("*** Merlion Car Rental System :: Operations Management :: View All Car Records ***\n");
        List<CarEntity> allCars =  carEntitySessionBeanRemote.retrieveAllCars();
        for (CarEntity car : allCars)
        {
            carEntitySessionBeanRemote.viewCarDetails(car);
        }
    }
    
    private void viewCarDetails() throws CarNotFoundException
    {
        Scanner scanner = new Scanner(System.in);
        CarEntity car = new CarEntity();
        System.out.println("*** Merlion Car Rental System :: Operations Management :: View Car Details ***\n");
        
        
        System.out.print("Enter Car Plate Number To View Car Details> ");
        car = carEntitySessionBeanRemote.retrieveCarByPlateNumber(scanner.nextLine().trim()); 
        
        carEntitySessionBeanRemote.viewCarDetails(car);
        
    }
    
    private void updateCar() throws ModelNotFoundException, CarNotFoundException
    {
        Scanner scanner = new Scanner(System.in);
        Long response;
        CarEntity carToUpdate = new CarEntity();
        System.out.println("*** Merlion Car Rental System :: Operations Management :: Update Car ***\n");
        
        viewCarDetails();
        
        System.out.print("Enter Plate Number To Update> ");
        carToUpdate.setCarPlateNumber(scanner.nextLine().trim());
        
        System.out.print("Select Make and Model To Update> ");
        List<Model> allModels = modelSessionBeanRemote.retrieveAllModels();
        for (Model model : allModels)
        {
            System.out.println(model.getModelId()+ ": " + model.getMakeName() + ", " + model.getModelName());
        }
        response = scanner.nextLong();
        Model chosenModel = modelSessionBeanRemote.retrieveModelById(response);
        carToUpdate.setModel(chosenModel);
        carEntitySessionBeanRemote.updateCarEntity(carToUpdate);
        
        System.out.println("Car successfully updated!");
    }
    
    
    private void deleteCar() throws CarNotFoundException
    {
        Scanner scanner = new Scanner(System.in);
        CarEntity carToDelete = new CarEntity();
        System.out.println("*** Merlion Car Rental System :: Operations Management :: Delete Car ***\n");
        
        viewCarDetails();
        
        System.out.print("Enter Plate Number To Delete> ");
        carToDelete = carEntitySessionBeanRemote.retrieveCarByPlateNumber(scanner.nextLine().trim());
        
        carEntitySessionBeanRemote.deleteCarEntity(carToDelete);
        
        System.out.println("Car successfully deleted!");
        
    }
    
    
    private void viewTransitDriverDispatchRecordsForCurrentDayReservations() throws OutletNotFoundException, ParseException
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
        outletId = scanner.nextLong();
   
        System.out.print("Enter today's date (DD/MM/YYYY)> ");
        todayDateString = scanner.nextLine().trim();
        Date currDate = new SimpleDateFormat("dd/MM/yyyy").parse(todayDateString);
        
        List<TransitDriverDispatchRecord> transitDriverDispatchRecords = transitDriverDispatchRecordSessionBeanRemote.viewCurrentDayTransitRecord(currDate, outletId);
        for (TransitDriverDispatchRecord transitDriverRecord : transitDriverDispatchRecords)
        {
//            transitDriverDispatchRecordSessionBeanRemote.viewRecordDetails(transitDriverRecord);
//            add viewRecordDetailsMethod in SessionBean
        }
    }
    
    
    private void assignTransitDriver() throws EmployeeNotFoundException, TransitRecordNotFoundException
    {
        Scanner scanner = new Scanner(System.in);

        String employeeUsername = "";
        Long response;
        
        System.out.println("*** Merlion Car Rental System :: Operations Management :: Assign Transit Driver ***\n");
        System.out.print("Enter Employee Username> ");
        employeeUsername = scanner.nextLine().trim();
        EmployeeEntity employee = employeeEntitySessionBeanRemote.retrieveEmployeeByUsername(employeeUsername);
        
        List<TransitDriverDispatchRecord> allTransitDriverRecords = transitDriverDispatchRecordSessionBeanRemote.retrieveAllTransitDriverRecord();
        
        System.out.print("Select Car To Assign Transit Driver To> ");
        for (TransitDriverDispatchRecord transitDriverRecord : allTransitDriverRecords)
        {
            System.out.println(transitDriverRecord.getTransitDriverDispatchId() + ": " + transitDriverRecord.getCar().getCarPlateNumber());
        }
        
        response = scanner.nextLong();
        
        TransitDriverDispatchRecord transitRecordToAssign = transitDriverDispatchRecordSessionBeanRemote.retrieveTransitRecordById(response);
        
        transitDriverDispatchRecordSessionBeanRemote.assignTransitDriver(transitRecordToAssign, employee.getId());
        
    }
    
    
    private  void updateTransitAsCompleted() throws CarNotFoundException, EmployeeNotFoundException, TransitRecordNotFoundException
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
            transitDriverDispatchRecordSessionBeanRemote.updateTransitAsComplete(transitDriverRecord);
        }
        else
        {
            throw new TransitRecordNotFoundException ("Transit Record Not Found!");
        }
    }
    
}
