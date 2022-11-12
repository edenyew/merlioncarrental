/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CarEntity;
import entity.Model;
import entity.OutletEntity;
import exception.CarNotFoundException;
import exception.CarNotInOutletException;
import exception.InputDataValidationException;
import exception.OutletNotFoundException;
import exception.UnknownPersistenceException;
import java.util.List;
import java.util.Objects;
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

/**
 *
 * @author edenyew
 */
@Stateless
public class OutletEntitySessionBean implements OutletEntitySessionBeanRemote, OutletEntitySessionBeanLocal {

    @PersistenceContext(unitName = "MerlionCarRental-ejbPU")
    private EntityManager em;
    
    @EJB
    private CarEntitySessionBeanLocal carSessionBeanLocal;
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")

    private final ValidatorFactory validatorFactory;
    private final Validator validator;
    
    
    public OutletEntitySessionBean() 
    {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }
         
     
    @Override
    public Long createNewOutlet(OutletEntity outletEntity) throws OutletNotFoundException, UnknownPersistenceException, InputDataValidationException
    {
        Set<ConstraintViolation<OutletEntity>>constraintViolations = validator.validate(outletEntity);
        
        if (constraintViolations.isEmpty())
        {
            try
            {
                em.persist(outletEntity);
                em.flush();

                return outletEntity.getOutletId();
            }
            catch(PersistenceException ex)
            {
                if(ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException"))
                {
                    if(ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException"))
                    {
                        throw new OutletNotFoundException();
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
    public List<OutletEntity> retrieveAllOutlets() {
         Query query;
        query = em.createQuery("SELECT o FROM OutletEntity o");
       
       return query.getResultList();
    }
    
    @Override
    public OutletEntity retrieveOutletById(Long outletId) throws OutletNotFoundException
    {
        OutletEntity outlet = em.find(OutletEntity.class, outletId);
        if (outlet != null ){
            return outlet;
        } else {
            throw new OutletNotFoundException("Outlet does not exist!");
        }
    }
    
    @Override
    public boolean findCarInOutlet(Long outletId, Long carId) throws CarNotFoundException, OutletNotFoundException{
        CarEntity carToFind = carSessionBeanLocal.retrieveCarById(carId);
        OutletEntity outlet = retrieveOutletById(outletId);
        if (carToFind == null ){
            throw new CarNotFoundException();
        } 
        if (outlet == null){
            throw new OutletNotFoundException();
        }
        
        boolean carIsInOutlet = false;
        
        for(CarEntity car : outlet.getCars()){
            if (Objects.equals(carToFind.getCarId(), car.getCarId())){
                carIsInOutlet = true;
                break;
            } 
        }
        
        return carIsInOutlet;
        
    }
    public void updateOutletEntity(OutletEntity outlet)
    {
        em.merge(outlet);
    }
    
    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<OutletEntity>>constraintViolations)
    {
        String msg = "Input data validation error!:";
            
        for(ConstraintViolation constraintViolation:constraintViolations)
        {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }
        
        return msg;
    }
    
    
}
