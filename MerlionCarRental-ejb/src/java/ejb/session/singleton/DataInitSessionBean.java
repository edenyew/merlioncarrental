/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.singleton;

import ejb.session.stateless.CarEntitySessionBeanLocal;
import ejb.session.stateless.CategorySessionBeanLocal;
import ejb.session.stateless.EmployeeEntitySessionBeanLocal;
import ejb.session.stateless.ModelSessionBeanLocal;
import ejb.session.stateless.OutletEntitySessionBeanLocal;
import ejb.session.stateless.PartnerEntitySessionBeanLocal;
import ejb.session.stateless.RentalRateSessionBeanLocal;
import entity.CarEntity;
import entity.Category;
import entity.EmployeeEntity;
import entity.Model;
import entity.OutletEntity;
import entity.PartnerEntity;
import entity.RentalRate;
import entity.Reservation;
import entity.TransitDriverDispatchRecord;
import exception.ModelNotFoundException;
import exception.OutletNotFoundException;
import exception.RentalRateNotFoundException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import util.enumeration.AccessRightEnum;
import util.enumeration.CarStatusEnum;
import util.enumeration.RentalRateTypeEnum;

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
    @EJB
    private CarEntitySessionBeanLocal carEntitySessionBeanLocal;
    @EJB
    private EmployeeEntitySessionBeanLocal employeeEntitySessionBeanLocal;

    @PersistenceContext(unitName = "MerlionCarRental-ejbPU")
    private EntityManager em;

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    
    @PostConstruct
    public void postConstruct() 
    {
        if (em.find(PartnerEntity.class, 1l) == null)
        {
            partnerEntitySessionBeanLocal.createNewPartnerRecord(new PartnerEntity("Holiday.com", "partner1@gmail.com", "password"));
        }
        
        if (em.find(OutletEntity.class, 1l) == null && em.find(Category.class, 1l) == null && em.find(Model.class, 1l) == null && em.find(CarEntity.class, 1l) ==null && em.find(RentalRate.class, 1l) ==null)
        {
            try {
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
                
                Long carOneId = carEntitySessionBeanLocal.createNewCar(new CarEntity("SS00A1TC", "Red", false, CarStatusEnum.NOT_IN_USE), corollaId, outletAId);
                Long carTwoId = carEntitySessionBeanLocal.createNewCar(new CarEntity("SS00A2TC", "Red", false, CarStatusEnum.NOT_IN_USE), corollaId, outletAId);
                Long carThreeId = carEntitySessionBeanLocal.createNewCar(new CarEntity("SS00A3TC", "Red", false, CarStatusEnum.NOT_IN_USE), corollaId, outletAId);
                Long carFourId = carEntitySessionBeanLocal.createNewCar(new CarEntity("SS00B1HC", "Red", false, CarStatusEnum.NOT_IN_USE), civicId, outletBId);
                Long carFiveId = carEntitySessionBeanLocal.createNewCar(new CarEntity("SS00B2HC", "Red", false, CarStatusEnum.NOT_IN_USE), civicId, outletBId);
                Long carSixId = carEntitySessionBeanLocal.createNewCar(new CarEntity("SS00B3HC", "Red", false, CarStatusEnum.NOT_IN_USE), civicId, outletBId);
                Long carSevenId = carEntitySessionBeanLocal.createNewCar(new CarEntity("SS00C1NS", "Red", false, CarStatusEnum.NOT_IN_USE), sunnyId, outletCId);
                Long carEightId = carEntitySessionBeanLocal.createNewCar(new CarEntity("SS00C2NS", "Red", false, CarStatusEnum.NOT_IN_USE), sunnyId, outletCId);
                Long carNineId = carEntitySessionBeanLocal.createNewCar(new CarEntity("SS00C3NS", "Red", false, CarStatusEnum.NOT_IN_USE), sunnyId, outletCId);
                Long carTenId = carEntitySessionBeanLocal.createNewCar(new CarEntity("LS00A4ME", "Red", false, CarStatusEnum.NOT_IN_USE), eClassId, outletAId);
                Long carElevenId = carEntitySessionBeanLocal.createNewCar(new CarEntity("LS00B4B5", "Red", false, CarStatusEnum.NOT_IN_USE), fiveSeriesId, outletBId);
                Long carTwelveId = carEntitySessionBeanLocal.createNewCar(new CarEntity("LS00C4A6", "Red", false, CarStatusEnum.NOT_IN_USE), aSixId, outletCId);
                
                Long ssDefaultId = rentalRateSessionBeanLocal.createRentalRate(new RentalRate("Standard Sedan - Default", new Long(100), null, null, null ,null, false, false, RentalRateTypeEnum.DEFAULT), categorySSid);
                Long ssPromoId = rentalRateSessionBeanLocal.createRentalRate(new RentalRate("Standard Sedan - Weekend Promo", new Long(80), new SimpleDateFormat("dd/MM/yyyy").parse("09/12/2022"), new SimpleDateFormat("dd/MM/yyyy hh:mm").parse("09/12/2022 12:00"), new SimpleDateFormat("dd/MM/yyyy").parse("11/12/2022"), new SimpleDateFormat("dd/MM/yyyy hh:mm").parse("09/12/2022 00:00"), false, false, RentalRateTypeEnum.PROMOTION), categorySSid);
                Long fsDefaultId = rentalRateSessionBeanLocal.createRentalRate(new RentalRate("Family Sedan - Default", new Long(200), null, null,  null ,null, false, false, RentalRateTypeEnum.DEFAULT), categoryFSid);
                Long lsDefaultId = rentalRateSessionBeanLocal.createRentalRate(new RentalRate("Luxury Sedan - Default", new Long(300), null, null, null ,null, false, false, RentalRateTypeEnum.DEFAULT), categoryLSid);
                Long lsMonPeakId = rentalRateSessionBeanLocal.createRentalRate(new RentalRate("Luxury Sedan - Monday", new Long(310), new SimpleDateFormat("dd/MM/yyyy").parse("05/12/2022"), new SimpleDateFormat("dd/MM/yyyy hh:mm").parse("05/12/2022 00:00"), new SimpleDateFormat("dd/MM/yyyy").parse("05/12/2022"), new SimpleDateFormat("dd/MM/yyyy hh:mm").parse("05/12/2022 23:59"), false, false, RentalRateTypeEnum.PEAK), categoryLSid);
                Long lsTuesPeakId = rentalRateSessionBeanLocal.createRentalRate(new RentalRate("Luxury Sedan - Tuesday", new Long(320), new SimpleDateFormat("dd/MM/yyyy").parse("06/12/2022"),new SimpleDateFormat("dd/MM/yyyy hh:mm").parse("06/12/2022 00:00"),  new SimpleDateFormat("dd/MM/yyyy").parse("06/12/2022"), new SimpleDateFormat("dd/MM/yyyy hh:mm").parse("06/12/2022 23:59"), false, false, RentalRateTypeEnum.PEAK), categoryLSid);
                Long lsWedPeakId = rentalRateSessionBeanLocal.createRentalRate(new RentalRate("Luxury Sedan - Wednesday", new Long(330), new SimpleDateFormat("dd/MM/yyyy").parse("07/12/2022"), new SimpleDateFormat("dd/MM/yyyy hh:mm").parse("07/12/2022 00:00"), new SimpleDateFormat("dd/MM/yyyy").parse("07/12/2022"), new SimpleDateFormat("dd/MM/yyyy hh:mm").parse("07/12/2022 23:59"), false, false, RentalRateTypeEnum.PEAK), categoryLSid);
                Long lsPromoId = rentalRateSessionBeanLocal.createRentalRate(new RentalRate("Luxury Sedan - Weekday Promo", new Long(250), new SimpleDateFormat("dd/MM/yyyy").parse("07/12/2022"), new SimpleDateFormat("dd/MM/yyyy hh:mm").parse("07/12/2022 12:00"), new SimpleDateFormat("dd/MM/yyyy").parse("08/12/2022"), new SimpleDateFormat("dd/MM/yyyy hh:mm").parse("08/12/2022 12:00"), false, false, RentalRateTypeEnum.PROMOTION), categoryLSid);
                Long suvDefaultId = rentalRateSessionBeanLocal.createRentalRate(new RentalRate("SUV and Minivan - Default", new Long(400), null, null, null ,null, false, false, RentalRateTypeEnum.DEFAULT),categorySUVid );
                
                Long EmployeeA1Id = employeeEntitySessionBeanLocal.createNewEmployee(new EmployeeEntity("Employee A1", "91234567", AccessRightEnum.SALES_MANAGER, "EmployeeA1", "password", false), outletAId);
                Long EmployeeA2Id = employeeEntitySessionBeanLocal.createNewEmployee(new EmployeeEntity("Employee A2", "91234567", AccessRightEnum.OPERATIONS_MANAGER, "EmployeeA2", "password", false), outletAId);
                Long EmployeeA3Id = employeeEntitySessionBeanLocal.createNewEmployee(new EmployeeEntity("Employee A3", "91234567", AccessRightEnum.CUSTOMER_SERVICE_EXECUTIVE, "EmployeeA3", "password", false), outletAId);
                Long EmployeeA4Id = employeeEntitySessionBeanLocal.createNewEmployee(new EmployeeEntity("Employee A4", "91234567", AccessRightEnum.EMPLOYEE, "EmployeeA4", "password", false), outletAId);
                Long EmployeeA5Id = employeeEntitySessionBeanLocal.createNewEmployee(new EmployeeEntity("Employee A5", "91234567", AccessRightEnum.EMPLOYEE, "EmployeeA5", "password", false), outletAId);
                Long EmployeeB1Id = employeeEntitySessionBeanLocal.createNewEmployee(new EmployeeEntity("Employee B1", "91234567", AccessRightEnum.SALES_MANAGER, "EmployeeB1", "password", false), outletBId);
                Long EmployeeB2Id = employeeEntitySessionBeanLocal.createNewEmployee(new EmployeeEntity("Employee B2", "91234567", AccessRightEnum.OPERATIONS_MANAGER, "EmployeeB2", "password", false), outletBId);
                Long EmployeeB3Id = employeeEntitySessionBeanLocal.createNewEmployee(new EmployeeEntity("Employee B3", "91234567", AccessRightEnum.CUSTOMER_SERVICE_EXECUTIVE, "EmployeeB3", "password", false), outletBId);
                Long EmployeeC1Id = employeeEntitySessionBeanLocal.createNewEmployee(new EmployeeEntity("Employee C1", "91234567", AccessRightEnum.SALES_MANAGER, "EmployeeC1", "password", false), outletCId);
                Long EmployeeC2Id = employeeEntitySessionBeanLocal.createNewEmployee(new EmployeeEntity("Employee C2", "91234567", AccessRightEnum.OPERATIONS_MANAGER, "EmployeeC2", "password", false), outletCId);
                Long EmployeeC3Id = employeeEntitySessionBeanLocal.createNewEmployee(new EmployeeEntity("Employee C3", "91234567", AccessRightEnum.CUSTOMER_SERVICE_EXECUTIVE, "EmployeeC3", "password", false), outletCId);
                
            } catch (ModelNotFoundException | OutletNotFoundException  | RentalRateNotFoundException | ParseException ex) {
                Logger.getLogger(DataInitSessionBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        
    }
    //| RentalRateNotFoundException | ParseException
}
