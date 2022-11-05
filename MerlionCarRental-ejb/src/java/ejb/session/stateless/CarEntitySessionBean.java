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
import exception.ModelNotFoundException;
import exception.OutletNotFoundException;
import exception.RentalRateNotFoundException;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
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
    
    @Override
    public Long createNewCar(CarEntity car, Long modelId, Long outletId, Long rentalRateId) throws ModelNotFoundException, OutletNotFoundException, RentalRateNotFoundException
    {
       
       Model model = modelSessionBeanLocal.retrieveModelById(modelId);
       Category category = model.getCategory();
       OutletEntity outlet = outletSessionBeanLocal.retrieveOutletById(outletId);
       RentalRate rentalRate = rentalRateSessionBeanLocal.retrieveRentalRateByRentalRateId(rentalRateId);
       
       em.persist(car);
       
       model.getCars().add(car);
       car.setModel(model);
       car.setCategory(category);
       car.setOutletEntity(outlet);
       car.setRentalRate(rentalRate);
       
       em.flush();
       return car.getCarId();
       
   }
    
    @Override
    public List<CarEntity> retrieveAllCars() {
       
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
    public void updateCarEntity(CarEntity carEntity) throws CarNotFoundException
    {
            CarEntity carToUpdate = retrieveCarById(carEntity.getCarId());
            
            if (carToUpdate != null) {
            carToUpdate.setCategory(carEntity.getCategory());
                carToUpdate.setDisabled(carEntity.getDisabled());
                carToUpdate.setCurrentStatus(carEntity.getCurrentStatus());
                carToUpdate.setCarPlateNumber(carEntity.getCarPlateNumber()); 
                carToUpdate.setColour(carEntity.getColour());
                carToUpdate.setLocation(carEntity.getLocation());
                carToUpdate.setOutletEntity(carEntity.getOutletEntity());
                carToUpdate.setRentalRate(carEntity.getRentalRate());
                carToUpdate.setTransitDriverDispatchRecords(carEntity.getTransitDriverDispatchRecords());
            } else {
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
                em.remove(carToDelete);
            }
    }
    
    @Override
    public void viewCarDetails(CarEntity car) throws CarNotFoundException{
        CarEntity carToView = retrieveCarById(car.getCarId());
        if (carToView != null)
        {
            System.out.println("*** View Car Details: ***\n");
            System.out.println("Car plate Number: " + carToView.getCarPlateNumber());
            System.out.println("Car Color: " + carToView.getColour());
            System.out.println("Car Model: " + carToView.getModel());      
            System.out.println("Car Category " + carToView.getCategory());
            System.out.println("Car Rental Rate: " + carToView.getRentalRate());
            System.out.println("Car Current Outlet " + carToView.getOutletEntity());
                 
        }
        else 
        {
            throw new CarNotFoundException("Car does not exist!");
        }
    }
    
   
    @Override
    public List<CarEntity> findListOfCars(Long pickUpOutletId, Long returnOutletId, Date pickUpDate, Date returnDate) throws OutletNotFoundException{
        
        OutletEntity returnOutlet = outletSessionBeanLocal.retrieveOutletById(returnOutletId);
        OutletEntity pickUpOutlet = outletSessionBeanLocal.retrieveOutletById(pickUpOutletId);
        Query query;
        query = em.createQuery("SELECT c FROM CarEntity c WHERE c.outletEntity = :pickUpOutlet AND c.currentStatus = :notInUse");
        query.setParameter("pickUpOutlet", pickUpOutlet).setParameter("notInUse", CarStatusEnum.NOT_IN_USE);
        
        List<CarEntity> listOfCarsAtPickUpOutlet = query.getResultList();
        for (CarEntity car : listOfCarsAtPickUpOutlet){
            List<Reservation> reservations = car.getReservations();
            for (Reservation reservation: reservations){
                if (reservation.getPickUpDate().compareTo(pickUpDate) < 0 || reservation.getReturnDate().compareTo(returnDate) > 0){
                    listOfCarsAtPickUpOutlet.remove(car);
            }
            }
        }
        
        return listOfCarsAtPickUpOutlet;
    }
    
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
    
      public void returnCar(Long outletId, CarEntity carEntity) throws OutletNotFoundException, CarNotFoundException, CarAlreadyInOutletException {
        OutletEntity outlet = outletSessionBeanLocal.retrieveOutletById(outletId);
        CarEntity car = retrieveCarById(carEntity.getCarId());
        if (outletSessionBeanLocal.findCarInOutlet(outlet.getOutletId(), car.getCarId())) {
            car.setCurrentStatus(CarStatusEnum.NOT_IN_USE);
            car.setLocation(outlet.getAddress());
            outlet.getCars().add(car);
        } else {
            throw new CarAlreadyInOutletException("Car Is Already At That Outlet!");
        }
    }
    
}
