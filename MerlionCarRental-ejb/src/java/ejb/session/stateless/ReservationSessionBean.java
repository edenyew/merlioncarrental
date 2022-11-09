/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CarEntity;
import entity.CreditCard;
import entity.Customer;
import entity.OutletEntity;
import entity.PartnerEntity;
import entity.RentalRate;
import entity.Reservation;
import exception.CarNotFoundException;
import exception.CustomerNotFoundException;
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
    @EJB
    private CreditCardSessionBeanLocal creditCardSessionBeanLocal;
    @EJB 
    private CustomerSessionBeanLocal customerSessionBeanLocal;
    @EJB
    private PartnerEntitySessionBeanLocal partnerSessionBeanLocal;

    @Override
    public Long creatNewReservation(Reservation reservation, Long carId, Long returnOutletId, Long pickUpOutletId, Long creditCardId, Long customerId) throws CarNotFoundException, OutletNotFoundException, RentalRateNotFoundException, CustomerNotFoundException 
    {
        CarEntity car = carSessionBeanLocal.retrieveCarById(carId);
        OutletEntity returnOutlet = outletSessionBeanLocal.retrieveOutletById(returnOutletId);
        OutletEntity pickUpOutlet = outletSessionBeanLocal.retrieveOutletById(pickUpOutletId);
        CreditCard creditCard = creditCardSessionBeanLocal.retrieveCreditCardById(creditCardId);
        Customer customer = customerSessionBeanLocal.retrieveCustomerById(customerId);
        
        em.persist(reservation);
        
        reservation.setCar(car);
        reservation.setReturnOutlet(returnOutlet);
         reservation.setPickUpOutlet(pickUpOutlet);
        car.getReservations().add(reservation);
        returnOutlet.getReservations().add(reservation);
        reservation.setCreditCard(creditCard);
        creditCard.setReservation(reservation);
        reservation.setCustomer(customer);
        customer.getReservations().add(reservation);
        
        em.flush();
        
        return reservation.getId();
    }
    
    @Override
    public Long creatNewReservationPartner(Reservation reservation, Long carId, Long returnOutletId, Long pickUpOutletId, Long creditCardId, Long partnerId) throws CarNotFoundException, OutletNotFoundException, RentalRateNotFoundException, CustomerNotFoundException 
    {
        CarEntity car = carSessionBeanLocal.retrieveCarById(carId);
        OutletEntity returnOutlet = outletSessionBeanLocal.retrieveOutletById(returnOutletId);
        OutletEntity pickUpOutlet = outletSessionBeanLocal.retrieveOutletById(pickUpOutletId);
        CreditCard creditCard = creditCardSessionBeanLocal.retrieveCreditCardById(creditCardId);
        PartnerEntity partner = partnerSessionBeanLocal.retrievePartnerById(partnerId);
        
        em.persist(reservation);
        
        reservation.setCar(car);
        reservation.setReturnOutlet(returnOutlet);
         reservation.setPickUpOutlet(pickUpOutlet);
        car.getReservations().add(reservation);
        returnOutlet.getReservations().add(reservation);
        reservation.setCreditCard(creditCard);
        creditCard.setReservation(reservation);
        reservation.setPartner(partner);
        partner.getReservations().add(reservation);
        
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
    
    public void cancelReservation(Reservation reservation) throws ReservationNotFoundException {
        Reservation reservationToCancel = retrieveReservationById(reservation.getId());
        
        reservationToCancel.getCar().getReservations().remove(reservationToCancel);
        reservationToCancel.getReturnOutlet().getReservations().remove(reservationToCancel);
        reservationToCancel.getPickUpOutlet().getReservations().remove(reservationToCancel);
        reservationToCancel.getCustomer().getReservations().remove(reservationToCancel);
        
        em.remove(reservationToCancel);
        
    }

    
}
