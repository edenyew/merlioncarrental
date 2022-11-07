/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Reservation;
import exception.CarNotFoundException;
import exception.OutletNotFoundException;
import exception.RentalRateNotFoundException;
import exception.ReservationNotFoundException;
import javax.ejb.Remote;

/**
 *
 * @author jonta
 */
@Remote
public interface ReservationSessionBeanRemote {
    
     

    public Reservation retrieveReservationById(Long reservationId) throws ReservationNotFoundException;

     public Long creatNewReservation(Reservation reservation, Long carId, Long returnOutletId, Long pickUpOutletId) throws CarNotFoundException, OutletNotFoundException, RentalRateNotFoundException;
    
    
}
