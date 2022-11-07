/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.singleton;

import ejb.session.stateless.CategorySessionBeanLocal;
import ejb.session.stateless.ModelSessionBeanLocal;
import ejb.session.stateless.OutletEntitySessionBeanLocal;
import ejb.session.stateless.PartnerEntitySessionBeanLocal;
import ejb.session.stateless.RentalRateSessionBeanLocal;
import entity.CarEntity;
import entity.Category;
import entity.Model;
import entity.OutletEntity;
import entity.PartnerEntity;
import entity.RentalRate;
import entity.Reservation;
import entity.TransitDriverDispatchRecord;
import java.util.ArrayList;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author edenyew
 */
@Singleton
@LocalBean
@Startup

public class DataInitSessionBean {

    @EJB
    private PartnerEntitySessionBeanLocal partnerEntitySessionBeanLocal;
    @EJB
    private OutletEntitySessionBeanLocal outletEntitySessionBeanLocal;
    @EJB
    private CategorySessionBeanLocal categorySessionBeanLocal;
    @EJB 
    private ModelSessionBeanLocal modelSessionBeanLocal;
    @EJB 
    private RentalRateSessionBeanLocal rentalRateSessionBeanLocal;

    @PersistenceContext(unitName = "MerlionCarRental-ejbPU")
    private EntityManager em;

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    
    @PostConstruct
    public void postConstruct() 
    {
        if (em.find(PartnerEntity.class, 1l) == null)
        {
            partnerEntitySessionBeanLocal.createNewPartnerRecord(new PartnerEntity("partner1", "91234567", "partner1@gmail.com" ,"A123456"));
        }
        
        if (em.find(OutletEntity.class, 1l) == null && em.find(Category.class, 1l) == null && em.find(Model.class, 1l) == null)
        {
            Long outletAId = outletEntitySessionBeanLocal.createNewOutlet(new OutletEntity("Outlet A", null, null, new ArrayList<Reservation>(), new ArrayList<CarEntity>(), new ArrayList<TransitDriverDispatchRecord>()));
            Long outletBId = outletEntitySessionBeanLocal.createNewOutlet(new OutletEntity("Outlet B", null, null, new ArrayList<Reservation>(), new ArrayList<CarEntity>(), new ArrayList<TransitDriverDispatchRecord>()));
            Long outletCId = outletEntitySessionBeanLocal.createNewOutlet(new OutletEntity("Outlet C", "10:00", "22:00", new ArrayList<Reservation>(), new ArrayList<CarEntity>(), new ArrayList<TransitDriverDispatchRecord>()));    
      
            Long categorySSid = categorySessionBeanLocal.createNewCategory(new Category("Standard Sedan", new ArrayList<Model>(), new ArrayList<RentalRate>()));
            Long categoryFSid = categorySessionBeanLocal.createNewCategory(new Category("Family Sedan", new ArrayList<Model>(), new ArrayList<RentalRate>()));
            Long categoryLSid = categorySessionBeanLocal.createNewCategory(new Category("Luxury Sedan", new ArrayList<Model>(), new ArrayList<RentalRate>()));
            Long categorySUVid = categorySessionBeanLocal.createNewCategory(new Category("SUV and Minivan", new ArrayList<Model>(), new ArrayList<RentalRate>()));

            Long corollaId = modelSessionBeanLocal.createNewModel(new Model("Corolla", "Toyota",false,false), categorySSid);
            Long civicId = modelSessionBeanLocal.createNewModel(new Model("Civic", "Honda",false,false), categorySSid);
            Long sunnyId = modelSessionBeanLocal.createNewModel(new Model("Sunny", "Nissan",false,false), categorySSid);
            Long eClassId = modelSessionBeanLocal.createNewModel(new Model("E Class", "Mercedes",false,false), categoryLSid);
            Long fiveSeriesId = modelSessionBeanLocal.createNewModel(new Model("5 Series", "BMW",false,false), categoryLSid);
            Long aSixId = modelSessionBeanLocal.createNewModel(new Model("A6", "Audi",false,false), categoryLSid);
            
        }
        
        
    }
    
}
