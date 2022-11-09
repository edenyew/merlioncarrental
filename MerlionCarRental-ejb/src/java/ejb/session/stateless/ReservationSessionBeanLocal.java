/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Reservation;
import exception.CarNotFoundException;
import exception.CustomerNotFoundException;
import exception.OutletNotFoundException;
import exception.RentalRateNotFoundException;
import exception.ReservationNotFoundException;
import javax.ejb.Local;

/**
 *
 * @author jonta
 */
@Local
public interface ReservationSessionBeanLocal {


    public Reservation retrieveReservationById(Long reservationId) throws ReservationNotFoundException;

    public Long creatNewReservation(Reservation reservation, Long carId, Long returnOutletId, Long pickUpOutletId, Long creditCardId, Long customerId) throws CarNotFoundException, OutletNotFoundException, RentalRateNotFoundException, CustomerNotFoundException;

    public Long creatNewReservationPartner(Reservation reservation, Long carId, Long returnOutletId, Long pickUpOutletId, Long creditCardId, Long partnerId) throws CarNotFoundException, OutletNotFoundException, RentalRateNotFoundException, CustomerNotFoundException;

    
}
