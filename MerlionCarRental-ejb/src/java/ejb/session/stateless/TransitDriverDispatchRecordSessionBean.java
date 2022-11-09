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
import exception.TransitRecordNotFoundException;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.enumeration.TransitStatusEnum;

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
    @EJB
    private EmployeeEntitySessionBeanLocal employeeEntitySessionBean;
    
    
    @Override
    public Long createNewTransitRecord(TransitDriverDispatchRecord transitRecord, Long employeeId, Long pickupOutletId, Long returnOutletId, Long carId) throws OutletNotFoundException,CarNotFoundException  
    {
       
       EmployeeEntity employee = em.find(EmployeeEntity.class, employeeId);
       OutletEntity pickupOutlet = outletSessionBeanLocal.retrieveOutletById(pickupOutletId);
       OutletEntity returnOutlet = outletSessionBeanLocal.retrieveOutletById(returnOutletId);
       CarEntity car = carEntitySessionBeanLocal.retrieveCarById(carId);
       
       em.persist(transitRecord);
      
       returnOutlet.getTransitDriverDispatchRecords().add(transitRecord);
       car.getTransitDriverDispatchRecords().add(transitRecord);
       transitRecord.setTransitDriver(employee);
       transitRecord.setPickUpOutlet(pickupOutlet);
       transitRecord.setReturnOutlet(returnOutlet);
       transitRecord.setCar(car);
       
       em.flush();
       return transitRecord.getTransitDriverDispatchId();
       
   }
    @Override
    public TransitDriverDispatchRecord retrieveTransitRecordById(Long transitRecordId) throws TransitRecordNotFoundException
    {
        TransitDriverDispatchRecord record = em.find(TransitDriverDispatchRecord.class, transitRecordId);
        if (record != null){
            return record;
        } else {
            throw new TransitRecordNotFoundException();
        }
    }
    
    @Override
    public List<TransitDriverDispatchRecord> retrieveAllTransitDriverRecord()
    {
        Query query = em.createQuery("Select tdr From TransitDriverDispatchRecord tdr");
        return query.getResultList();
    }
    
    @Override
    public List<TransitDriverDispatchRecord> viewCurrentDayTransitRecord(Date currentDate, Long currentOutletId) throws OutletNotFoundException{
        
         OutletEntity currentOutlet = outletSessionBeanLocal.retrieveOutletById(currentOutletId);
         if (currentOutlet == null){
             throw new OutletNotFoundException();
         }
         
        Query query = em.createQuery("SELECT t FROM TransitDriverDispatchRecord t WHERE t.dateOfTransit = :currentDate AND t.returnOutlet = :outlet");
        query.setParameter("currentDate", currentDate).setParameter("outlet", currentOutlet);
        return query.getResultList();
    }
    
    @Override
    public void updateTransitAsComplete(TransitDriverDispatchRecord transitRecord) throws TransitRecordNotFoundException{
        
        TransitDriverDispatchRecord transitRecordToUpdate = retrieveTransitRecordById(transitRecord.getTransitDriverDispatchId());
        if (transitRecordToUpdate != null) {
            transitRecordToUpdate.setTransitStatus(TransitStatusEnum.COMPLETED);
        } else {
            throw new TransitRecordNotFoundException();
        }
        
    }
    
    public void viewRecordDetails(TransitDriverDispatchRecord transitDriverRecord) throws TransitRecordNotFoundException
    {
        TransitDriverDispatchRecord transitDriverRecordToView = retrieveTransitRecordById(transitDriverRecord.getTransitDriverDispatchId());
        
        if (transitDriverRecordToView != null)
        {
            System.out.println("Car Plate Number, Date Of Transit, Pickup Outlet, Return Outlet");
            System.out.println(transitDriverRecord.getCar().getCarPlateNumber() + ", " + transitDriverRecord.getDateOfTransit() + ", " + transitDriverRecord.getPickUpOutlet().getAddress() + ", " + transitDriverRecord.getReturnOutlet().getAddress());
        }
        else
        {
            throw new TransitRecordNotFoundException("Transit Driver Dispatch Record does not exist!");
        }
    }
    
    @Override
    public TransitDriverDispatchRecord retrieveTransitRecordByEmployee(EmployeeEntity employee)
    {
        Query query = em.createQuery("Select t From TransitDriverDispatchRecord r Where r.transitDriver := driver");
        query.setParameter("driver", employee);
        
        return (TransitDriverDispatchRecord) query.getSingleResult();
    }
    
//    @Override
//    public List<CarEntity> retrieveCurrentDayRecords(OutletEntity returnOutlet, Date currentDay) {
//       
//       Query query;
//       query = em.createQuery("SELECT r FROM TransitDriverDispatchRecord r WHERE returnOutlet =  ");
//       
//       return query.getResultList();
//   }
    
    @Override
    public void assignTransitDriver(TransitDriverDispatchRecord transitRecord, Long employeeId)
    {
        EmployeeEntity employee = employeeEntitySessionBean.retrieveEmployeeById(employeeId);
        transitRecord.setTransitDriver(employee);
        employee.setTransitDriverDistpachRecord(transitRecord);
        
        updateTransitDriverRecord(transitRecord);
        employeeEntitySessionBean.updateEmployee(employee);
    }
    
    @Override
    public void updateTransitDriverRecord(TransitDriverDispatchRecord transitRecord)
    {
        em.merge(transitRecord);
    }
}
