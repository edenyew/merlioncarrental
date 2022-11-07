/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CarEntity;
import entity.OutletEntity;
import entity.RentalRate;
import entity.Reservation;
import exception.CarNotFoundException;
import exception.OutletNotFoundException;
import exception.RentalRateNotFoundException;
import exception.ReservationNotFoundException;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author jonta
 */
@Stateless
public class ReservationSessionBean implements ReservationSessionBeanRemote, ReservationSessionBeanLocal {

    @PersistenceContext(unitName = "MerlionCarRental-ejbPU")
    private EntityManager em;
    @EJB
    private OutletEntitySessionBeanLocal outletSessionBeanLocal;
    @EJB
    private CarEntitySessionBeanLocal carSessionBeanLocal;

    @Override
    public Long creatNewReservation(Reservation reservation, Long carId, Long returnOutletId, Long pickUpOutletId) throws CarNotFoundException, OutletNotFoundException, RentalRateNotFoundException 
    {
        CarEntity car = carSessionBeanLocal.retrieveCarById(carId);
        OutletEntity returnOutlet = outletSessionBeanLocal.retrieveOutletById(returnOutletId);
        OutletEntity pickUpOutlet = outletSessionBeanLocal.retrieveOutletById(pickUpOutletId);
        
        em.persist(reservation);
        
        reservation.setCar(car);
        reservation.setReturnOutlet(returnOutlet);
         reservation.setPickUpOutlet(pickUpOutlet);
        car.getReservations().add(reservation);
        returnOutlet.getReservations().add(reservation);
        
        em.flush();
        
        return reservation.getId();
    }
    
    @Override
    public Reservation retrieveReservationById(Long reservationId) throws ReservationNotFoundException
    {
        Reservation reservation = em.find(Reservation.class, reservationId);
        if (reservation != null){
            return reservation;
        } else {
            throw new ReservationNotFoundException();
        }
    }

    
}
