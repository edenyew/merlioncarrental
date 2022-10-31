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
import exception.CarNotFoundException;
import exception.ModelNotFoundException;
import exception.OutletNotFoundException;
import exception.RentalRateNotFoundException;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

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
        if (carEntity != null && carEntity.getCarId() != null)
        {
            CarEntity carToUpdate = retrieveCarById(carEntity.getCarId());
            
            carToUpdate.setCategory(carEntity.getCategory());
            carToUpdate.setDisabled(carEntity.getDisabled());
            carToUpdate.setInUse(carEntity.getInUse());
            carToUpdate.setCarPlateNumber(carEntity.getCarPlateNumber());
            carToUpdate.setColour(carEntity.getColour());
            carToUpdate.setLocation(carEntity.getLocation());
            carToUpdate.setOutletEntity(carEntity.getOutletEntity());
            carToUpdate.setRentalRate(carEntity.getRentalRate());
            

        }
        else 
        {
            throw new CarNotFoundException("Model does not exist!");
        }
    }
    
    @Override
    public void deleteCarEntity(CarEntity car)throws CarNotFoundException{
        
        CarEntity carToDelete = retrieveCarById(car.getCarId());
        if (carToDelete.getInUse() == true)
            {
                carToDelete.setDisabled(true);
            }
            else 
            {
                em.remove(carToDelete);
            }
    }
    
    
}
