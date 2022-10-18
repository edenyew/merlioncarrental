/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CarEntity;
import entity.Category;
import entity.Model;
import java.util.List;
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
    
    @Override
    public Long createNewCar(CarEntity car, Long modelId){
       
       Model model = em.find(Model.class, modelId);
       Category category = model.getCategory();
       
       em.persist(car);
       
       model.getCars().add(car);
       car.setModel(model);
       car.setCategory(category);
       
       em.flush();
       return car.getCarId();
       
   }
    
    @Override
    public List<CarEntity> retrieveAllCars() {
       
       Query query = em.createQuery("SELECT c FROM CarEntity c");
       
       return query.getResultList();
   }
    
    @Override
     public CarEntity retrieveCarByPlateNumber(String carPlateNumber) {
         Query query = em.createQuery("SELECT c FROM CarEntity c WHERE c.carPlateNumber = :inCarPlateNumber");
        query.setParameter("inCarPlateNumber", carPlateNumber);
        
        return (CarEntity)query.getSingleResult();
     }
        
    @Override
    public void updateCarEntity(CarEntity carEntity)
    {
        em.merge(carEntity);
    }

    
    
}
