/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.RentalRate;
import exception.DeleteRentalRateException;
import exception.RentalRateNotFoundException;
import java.math.BigDecimal;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author edenyew
 */
@Stateless
public class RentalRateSessionBean implements RentalRateSessionBeanRemote, RentalRateSessionBeanLocal {

    @PersistenceContext(unitName = "MerlionCarRental-ejbPU")
    private EntityManager em;
    
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    
    @Override
    public Long createRentalRate(RentalRate rentalRate)
    {
        em.persist(rentalRate);
        em.flush();
        
        return rentalRate.getId();
        
    }
    
    @Override
    public List<RentalRate> retrieveAllRentalRate()
    {
        Query query = em.createQuery("Select r FROM RentalRate r ORDER BY r.category ASC, r.validityPeriod ASC");
        
        return query.getResultList();
    }
    
    
/*    @Override
    public List<RentalRate> viewAllRentalRates()
    {
        List<RentalRate> allRentalRates =  retrieveAllRentalRate();
        
        return allRentalRates;
    }
*/
    
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
            rentalRateToUpdate.setValidityPeriod(rentalRate.getValidityPeriod());
            rentalRateToUpdate.setInUse(rentalRate.getInUse());
            rentalRateToUpdate.setDisabled(rentalRate.getDisabled());
            rentalRateToUpdate.setCategory(rentalRate.getCategory());
            rentalRateToUpdate.setCar(rentalRate.getCar());
            
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
            System.out.println(rentalRateToView.getName() + ", " + rentalRateToView.getCategory().getName() + ", " + rentalRateToView.getRatePerDay() + ", " + rentalRateToView.getValidityPeriod());
        }
        else 
        {
            throw new RentalRateNotFoundException("Rental Rate Record does not exist!");
        }
        
    } 
}

