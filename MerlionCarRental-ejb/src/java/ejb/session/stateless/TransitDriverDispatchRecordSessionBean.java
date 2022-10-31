/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CarEntity;
import entity.EmployeeEntity;
import entity.OutletEntity;
import entity.TransitDriverDispatchRecord;
import exception.CarNotFoundException;
import exception.OutletNotFoundException;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author jonta
 */
@Stateless
public class TransitDriverDispatchRecordSessionBean implements TransitDriverDispatchRecordSessionBeanRemote, TransitDriverDispatchRecordSessionBeanLocal {

    @PersistenceContext(unitName = "MerlionCarRental-ejbPU")
    private EntityManager em;
    @EJB
    OutletEntitySessionBeanLocal outletSessionBeanLocal;
    @EJB
    CarEntitySessionBeanLocal carEntitySessionBeanLocal;
    
    public Long createNewTransitRecord(TransitDriverDispatchRecord transitRecord, Long employeeId, Long pickupOutletId, Long returnOutletId, Long carId) throws OutletNotFoundException,CarNotFoundException  
    {
       
       EmployeeEntity employee = em.find(EmployeeEntity.class, employeeId);
       OutletEntity pickupOutlet = outletSessionBeanLocal.retrieveOutletById(pickupOutletId);
       OutletEntity returnOutlet = outletSessionBeanLocal.retrieveOutletById(returnOutletId);
       CarEntity car = carEntitySessionBeanLocal.retrieveCarById(carId);
       
       em.persist(transitRecord);
      
       returnOutlet.setTransitDriverDispatchRecord(transitRecord);
       car.getTransitDriverDispatchRecords().add(transitRecord);
       transitRecord.setTransitDriver(employee);
       transitRecord.setPickUpOutlet(pickupOutlet);
       transitRecord.setReturnOutlet(returnOutlet);
       transitRecord.setCar(car);
       
       em.flush();
       return transitRecord.getTransitDriverDispatchId();
       
   }
}
