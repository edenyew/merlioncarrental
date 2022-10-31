/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.RentalRate;
import exception.DeleteRentalRateException;
import exception.RentalRateNotFoundException;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author edenyew
 */
@Remote
public interface RentalRateSessionBeanRemote {
    
    public RentalRate retrieveRentalRateByRentalRateId(Long rentalRateId) throws RentalRateNotFoundException;

    public List<RentalRate> retrieveAllRentalRate();

    public List<RentalRate> viewAllRentalRates();

    public Long createRentalRate(RentalRate rentalRate);

    public void updateRentalRate(RentalRate rentalRate) throws RentalRateNotFoundException;

    public void deleteRentalRate(RentalRate rentalRate) throws RentalRateNotFoundException, DeleteRentalRateException;
    
    
}
