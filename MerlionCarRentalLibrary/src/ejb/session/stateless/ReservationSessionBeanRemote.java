/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.RentalRate;
import entity.Reservation;
import exception.CarNotFoundException;
import exception.CustomerNotFoundException;
import exception.ModelNotFoundException;
import exception.OutletNotFoundException;
import exception.RentalRateNotFoundException;
import exception.ReservationNotFoundException;
import java.util.Date;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author jonta
 */
@Remote
public interface ReservationSessionBeanRemote {
       
    public Reservation retrieveReservationById(Long reservationId) throws ReservationNotFoundException;

    //public Long creatNewReservation(Reservation reservation, Long carId, Long returnOutletId, Long pickUpOutletId, Long creditCardId, Long customerId) throws CarNotFoundException, OutletNotFoundException, RentalRateNotFoundException, CustomerNotFoundException;
    public Long creatNewReservation(Reservation reservation, Long carId, Long returnOutletId, Long pickUpOutletId, Long creditCardId, Long customerId, List<RentalRate> finalRentalRatesApplied) throws ModelNotFoundException, OutletNotFoundException, RentalRateNotFoundException, CustomerNotFoundException;

    public Long creatNewReservationPartner(Reservation reservation, Long carId, Long returnOutletId, Long pickUpOutletId, Long creditCardId, Long partnerId) throws CarNotFoundException, OutletNotFoundException, RentalRateNotFoundException, CustomerNotFoundException;
    
    public List<Reservation> retrieveReservationsOnPickUpDate(Date date) throws ReservationNotFoundException;
    
 

    public List<Reservation> retrieveReservationsByCustomer(Long customerId) throws ReservationNotFoundException, CustomerNotFoundException;

    public double cancelReservation(Reservation reservation, Date currDate) throws ReservationNotFoundException;

}
