/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CarEntity;
import entity.Category;
import entity.Model;
import entity.OutletEntity;
import entity.RentalRate;
import entity.Reservation;
import exception.CarAlreadyInOutletException;
import exception.CarNotFoundException;
import exception.CarNotInOutletException;
import exception.InputDataValidationException;
import exception.ModelNotFoundException;
import exception.OutletNotFoundException;
import exception.RentalRateNotFoundException;
import exception.UnknownPersistenceException;
import java.util.Date;
import java.util.List;
import java.util.Set;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.enumeration.CarStatusEnum;

/**
 *
 * @author jonta
 */
@Stateless
public class CarEntitySessionBean implements CarEntitySessionBeanRemote, CarEntitySessionBeanLocal {
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    @PersistenceContext(unitName = "MerlionCarRental-ejbPU")
    private EntityManager em;
    @EJB
    private ModelSessionBeanLocal modelSessionBeanLocal;
    @EJB
    private OutletEntitySessionBeanLocal outletSessionBeanLocal;
    @EJB 
    private RentalRateSessionBeanLocal rentalRateSessionBeanLocal;
    
    private final ValidatorFactory validatorFactory;
    private final Validator validator;
    
    
    public CarEntitySessionBean()
    {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }
    
    @Override
    public Long createNewCar(CarEntity car, Long modelId, Long outletId) throws ModelNotFoundException, OutletNotFoundException, RentalRateNotFoundException, InputDataValidationException, UnknownPersistenceException, CarNotFoundException
    {
        Set<ConstraintViolation<CarEntity>>constraintViolations = validator.validate(car);
        
        if (constraintViolations.isEmpty())
        {
            try
            {
                
             Model model = modelSessionBeanLocal.retrieveModelById(modelId);
             Category category = model.getCategory();
             OutletEntity outlet = outletSessionBeanLocal.retrieveOutletById(outletId);



              em.persist(car);

              model.getCars().add(car);
              model.setInUse(true);

              car.setModel(model);
              car.setCategory(category);
              car.setOutletEntity(outlet);

              em.flush();
              
              return car.getCarId();
            }
            catch(PersistenceException ex)
            {
                if(ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException"))
                {
                    if(ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException"))
                    {
                        throw new CarNotFoundException();
                    }
                    else
                    {
                        throw new UnknownPersistenceException(ex.getMessage());
                    }
                }
                else
                {
                    throw new UnknownPersistenceException(ex.getMessage());
                }
            }
        }
        else
        {
            throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
        }
    }
    
    @Override
    public List<CarEntity> retrieveAllCars() 
    {
        Query query;
        query = em.createQuery("SELECT c FROM CarEntity c ORDER BY c.category, c.model, c.model.makeName, c.carPlateNumber");
       
       return query.getResultList();
    }
    
    @Override
    public CarEntity retrieveCarByPlateNumber(String carPlateNumber) throws CarNotFoundException{
        Query query = em.createQuery("SELECT c FROM CarEntity c WHERE c.carPlateNumber = :inCarPlateNumber");
        query.setParameter("inCarPlateNumber", carPlateNumber);
        if ((CarEntity)query.getSingleResult() != null){
            return (CarEntity)query.getSingleResult();
        } else {
            throw new CarNotFoundException("Model does not exist!");
        }
        
     }
     
     @Override
   public CarEntity retrieveCarById(Long carId) throws CarNotFoundException
    {
        CarEntity car = em.find(CarEntity.class, carId);
        if (car != null ){
            return car;
        } else {
            throw new CarNotFoundException("Model does not exist!");
        }
    }
        
    @Override
    public void updateCarEntity(CarEntity carEntity) throws CarNotFoundException, InputDataValidationException
    {
            CarEntity carToUpdate = retrieveCarById(carEntity.getCarId());
            
            if (carToUpdate != null) 
            {
                Set<ConstraintViolation<CarEntity>>constraintViolations = validator.validate(carToUpdate);
                if (constraintViolations.isEmpty())
                {
                    carToUpdate.setCategory(carEntity.getCategory());
                    carToUpdate.setDisabled(carEntity.getDisabled());
                    carToUpdate.setCurrentStatus(carEntity.getCurrentStatus());
                    carToUpdate.setCarPlateNumber(carEntity.getCarPlateNumber()); 
                    carToUpdate.setColour(carEntity.getColour());
                    carToUpdate.setOutletEntity(carEntity.getOutletEntity());

                    carToUpdate.setTransitDriverDispatchRecords(carEntity.getTransitDriverDispatchRecords()); 
                }
                else
                {
                    throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
                }
            }
            else
            {
                    throw new CarNotFoundException();                
            }
    }
    
    @Override
    public void deleteCarEntity(CarEntity car)throws CarNotFoundException{
        
        CarEntity carToDelete = retrieveCarById(car.getCarId());
        if (carToDelete.getCurrentStatus().equals(CarStatusEnum.IN_USE))
            {
                carToDelete.setDisabled(true);
            }
            else 
            {
                carToDelete.getOutletEntity().getCars().remove(carToDelete);
                if(carToDelete.getModel().getCars().isEmpty()){
                    carToDelete.getModel().setInUse(false);
                }
                em.remove(carToDelete);
            }
    }
    
    @Override
    public void viewCarDetails(CarEntity car) throws CarNotFoundException{
        CarEntity carToView = retrieveCarById(car.getCarId());
        if (carToView != null)
        {
            System.out.println("*** View Car Details: ***\n");
            System.out.println("LicensePlateNumber, Make, Model, Status, Outlet");
            System.out.println(carToView.getCarPlateNumber() + ", " + carToView.getModel().getMakeName() + ", " + carToView.getModel().getModelName() + ", " + carToView.getCurrentStatus() + ", " + carToView.getOutletEntity().getAddress());
        }
        else 
        {
            throw new CarNotFoundException("Car does not exist!");
        }
    }
    
   
    @Override // this is for searchCar use case
    public List<CarEntity> findListOfCars(Long pickUpOutletId, Long returnOutletId, Date pickUpDate, Date returnDate) throws OutletNotFoundException{
        
        OutletEntity returnOutlet = outletSessionBeanLocal.retrieveOutletById(returnOutletId);
        OutletEntity pickUpOutlet = outletSessionBeanLocal.retrieveOutletById(pickUpOutletId);
        
        Query query = em.createQuery("SELECT c FROM CarEntity c WHERE c.outletEntity = :pickUpOutlet AND c.currentStatus = :notInUse");
        query.setParameter("pickUpOutlet", pickUpOutlet).setParameter("notInUse", CarStatusEnum.NOT_IN_USE);
        
        List<CarEntity> listOfCarsAtPickUpOutlet = query.getResultList();
        for (int i=0; i< listOfCarsAtPickUpOutlet.size();i++){
            List<Reservation> reservations = listOfCarsAtPickUpOutlet.get(i).getReservations();
            for (Reservation reservation: reservations){
                if (reservation.getPickUpDate().compareTo(pickUpDate) < 0 || reservation.getReturnDate().compareTo(returnDate) > 0){
                    listOfCarsAtPickUpOutlet.remove(listOfCarsAtPickUpOutlet.get(i));
                    break;
            }
            }
        }
        
        return listOfCarsAtPickUpOutlet;
    }
    
    
        
    
    // change state of car to in use, only if the car is in the outlet
    public void pickUpCar(Long outletId, CarEntity carEntity) throws OutletNotFoundException, CarNotFoundException, CarNotInOutletException {
        OutletEntity outlet = outletSessionBeanLocal.retrieveOutletById(outletId);
        CarEntity car = retrieveCarById(carEntity.getCarId());
        if (outletSessionBeanLocal.findCarInOutlet(outlet.getOutletId(), car.getCarId())) {
            car.setCurrentStatus(CarStatusEnum.IN_USE);
            outlet.getCars().remove(car);
        } else {
            throw new CarNotInOutletException("Car Not Found In Outlet!");
        }
    }
    
    // change state of car to not in use, only if the car is not already in the outlet
    @Override
      public void returnCar(Long outletId, CarEntity carEntity) throws OutletNotFoundException, CarNotFoundException, CarAlreadyInOutletException {
        OutletEntity outlet = outletSessionBeanLocal.retrieveOutletById(outletId);
        CarEntity car = retrieveCarById(carEntity.getCarId());
        if (outletSessionBeanLocal.findCarInOutlet(outlet.getOutletId(), car.getCarId())) {
            car.setCurrentStatus(CarStatusEnum.NOT_IN_USE);
            car.setOutletEntity(outlet); 
            outlet.getCars().add(car);
        } else {
            throw new CarAlreadyInOutletException("Car Is Already At That Outlet!");
        }
    }
      
    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<CarEntity>>constraintViolations)
    {
        String msg = "Input data validation error!:";
            
        for(ConstraintViolation constraintViolation:constraintViolations)
        {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }
        
        return msg;
    }
}
