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
    
    public Long createNewCar(CarEntity car, Long modelId, Long outletId) throws ModelNotFoundException, OutletNotFoundException, RentalRateNotFoundException
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
            throw new CarNotFoundException("Car does not exist!");
        }
    }
        
    @Override
    public void updateCarEntity(CarEntity carEntity) throws CarNotFoundException
    {
            CarEntity carToUpdate = retrieveCarById(carEntity.getCarId());
            
            if (carToUpdate != null) {
//             carToUpdate.setCategory(carEntity.getCategory());
                carToUpdate.setCarPlateNumber(carEntity.getCarPlateNumber()); 
//                carToUpdate.setColour(carEntity.getColour());
//                carToUpdate.setOutletEntity(carEntity.getOutletEntity());
//                carToUpdate.setTransitDriverDispatchRecords(carEntity.getTransitDriverDispatchRecords());
                em.merge(carToUpdate);
            } else {
                throw new CarNotFoundException("Car Does not Exist");
            }
        
    
    }
    
    @Override
    public void deleteCarEntity(CarEntity car)throws CarNotFoundException, ModelNotFoundException{
        
        CarEntity carToDelete = retrieveCarById(car.getCarId());
        if (carToDelete.getCurrentStatus().equals(CarStatusEnum.IN_USE))
            {
                carToDelete.setDisabled(true);
            }
            else 
            {
                carToDelete.getOutletEntity().getCars().remove(carToDelete);
                long modelId = carToDelete.getModel().getModelId();
                Model modelToUpdate = modelSessionBeanLocal.retrieveModelById(modelId);
                if(modelToUpdate.getCars().isEmpty()){
                    modelToUpdate.setInUse(false);
                    modelSessionBeanLocal.updateModel(modelToUpdate);
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
    
}
