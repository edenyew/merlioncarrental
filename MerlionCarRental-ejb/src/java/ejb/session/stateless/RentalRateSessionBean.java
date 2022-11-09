/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Category;
import entity.RentalRate;
import exception.DeleteRentalRateException;
import exception.RentalRateNotFoundException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.enumeration.RentalRateTypeEnum;

/**
 *
 * @author edenyew
 */
@Stateless
public class RentalRateSessionBean implements RentalRateSessionBeanRemote, RentalRateSessionBeanLocal {

    @PersistenceContext(unitName = "MerlionCarRental-ejbPU")
    private EntityManager em;
    
    @EJB
    CategorySessionBeanLocal categorySessionBeanLocal;
    
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    
    @Override
    public Long createRentalRate(RentalRate rentalRate, Long categoryId)
    {
        Category category = categorySessionBeanLocal.retrieveCategoryById(categoryId);
        em.persist(rentalRate);
        
        rentalRate.setCategory(category);
        category.getRentalRates().add(rentalRate);
        
        em.flush();
        
        return rentalRate.getId();
    }
    
    @Override
    public List<RentalRate> retrieveAllRentalRate()
    {
        Query query = em.createQuery("Select r FROM RentalRate r ORDER BY r.category ASC, r.startDate ASC");
        
        return query.getResultList();
    }
    
    
    @Override
    public List<RentalRate> retrieveRentalRatesOfCarCategory(Long categoryId)
    {
        Category category = categorySessionBeanLocal.retrieveCategoryById(categoryId);
        
        return category.getRentalRates();
    }

    
    @Override
    public RentalRate retrieveRentalRateByRentalRateId(Long rentalRateId) throws RentalRateNotFoundException
    {
        RentalRate rentalRate = em.find(RentalRate.class, rentalRateId);
        
        if (rentalRate != null)
        {
            return rentalRate;
        }
        else 
        {
            throw new RentalRateNotFoundException("Rental Rate ID " + rentalRateId + " does not exist!");
        }
    }
    
    @Override
    public void updateRentalRate(RentalRate rentalRate) throws RentalRateNotFoundException
    {
        if (rentalRate != null)
        {            
            
            RentalRate rentalRateToUpdate = retrieveRentalRateByRentalRateId(rentalRate.getId());
            viewRentalRateDetails(rentalRateToUpdate);
            
            rentalRateToUpdate.setName(rentalRate.getName());
            rentalRateToUpdate.setRatePerDay(rentalRate.getRatePerDay());

            rentalRateToUpdate.setInUse(rentalRate.getInUse());
            rentalRateToUpdate.setDisabled(rentalRate.getDisabled());
            rentalRateToUpdate.setCategory(rentalRate.getCategory());
            
            em.merge(rentalRate);
        }
        else 
        {
            throw new RentalRateNotFoundException("Rental Rate Record does not exist!");
        }
    }
    
    @Override
    public void deleteRentalRate(RentalRate rentalRate) throws RentalRateNotFoundException, DeleteRentalRateException
    {
        RentalRate rentalRateToDelete = retrieveRentalRateByRentalRateId(rentalRate.getId());
        
        if (rentalRateToDelete != null)
        {
            viewRentalRateDetails(rentalRateToDelete);
            
            if (rentalRateToDelete.getInUse() == true)
            {
                rentalRateToDelete.setDisabled(true);
                throw new DeleteRentalRateException("Rental Rate is in use and cannot be deleted!");
            }
            else if (rentalRateToDelete.getInUse() == false)
            {
                em.remove(rentalRateToDelete);
            }
        }
        else
        {
            throw new RentalRateNotFoundException("Rental Rate Record does not exist!");
        }   
    }
    
    @Override
    public void viewRentalRateDetails(RentalRate rentalRate) throws RentalRateNotFoundException
    {
        RentalRate rentalRateToView = retrieveRentalRateByRentalRateId(rentalRate.getId());
        
        if (rentalRateToView != null)
        {
            System.out.println("Name, CarCategory, RatePerDay, StartDateTime, EndDateTime");
            System.out.println(rentalRateToView.getName() + ", " + rentalRateToView.getCategory().getName() + ", " + rentalRateToView.getRatePerDay());
        }
        else 
        {
            throw new RentalRateNotFoundException("Rental Rate Record does not exist!");
        }
        
    } 
    
    @Override
    public Long calculateTotalCost(Date pickUpDate, Date returnDate, List<RentalRate> rentalRates) {
       Date currentDate = pickUpDate;
       List<RentalRate> listOfFinalRatesEachDay = new ArrayList<>();
       
        while(currentDate.before(returnDate)){
            HashMap<RentalRateTypeEnum, RentalRate> mapOfEligibleRates = new HashMap<>();
            for(RentalRate rentalRate : rentalRates){
                if (rentalRate.getStartDate() == null ||rentalRate.getEndDate() == null ){
                mapOfEligibleRates.put(rentalRate.getRentalRateType(), rentalRate);
                }
                else if (currentDate.compareTo(rentalRate.getStartDate())<0 && currentDate.compareTo(rentalRate.getEndDate())>0){
                    if(mapOfEligibleRates.containsKey(rentalRate.getRentalRateType()) && mapOfEligibleRates.get(rentalRate.getRentalRateType()).getRatePerDay() - (rentalRate.getRatePerDay()) >0)
                   mapOfEligibleRates.replace(rentalRate.getRentalRateType(), rentalRate);
                
                    else {
                    mapOfEligibleRates.put(rentalRate.getRentalRateType(), rentalRate);
                    }
                }
            }
            RentalRateTypeEnum rentalRateTypeToChoose = RentalRateTypeEnum.DEFAULT;
            if (mapOfEligibleRates.containsKey(RentalRateTypeEnum.PEAK) && mapOfEligibleRates.containsKey(RentalRateTypeEnum.PROMOTION)){
                rentalRateTypeToChoose = RentalRateTypeEnum.PROMOTION;
            } else if (mapOfEligibleRates.containsKey(RentalRateTypeEnum.PEAK) ){
                rentalRateTypeToChoose = RentalRateTypeEnum.PEAK;
            }else if (mapOfEligibleRates.containsKey(RentalRateTypeEnum.PROMOTION) ){
                rentalRateTypeToChoose = RentalRateTypeEnum.PROMOTION;
            } 
            
           listOfFinalRatesEachDay.add(mapOfEligibleRates.get(rentalRateTypeToChoose));
            
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(currentDate);
            calendar.add(Calendar.DATE, 1);
            currentDate = calendar.getTime();
        }
        
        long totalSum =0;
        for(RentalRate rentalRate : listOfFinalRatesEachDay){
            totalSum += rentalRate.getRatePerDay();
        }
        return totalSum;
    }
    
}

