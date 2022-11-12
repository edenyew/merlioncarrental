/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CarEntity;
import entity.CreditCard;
import entity.Customer;
import entity.Model;
import entity.OutletEntity;
import entity.PartnerEntity;
import entity.RentalRate;
import entity.Reservation;
import exception.CarNotFoundException;
import exception.CustomerNotFoundException;
import exception.ModelNotFoundException;
import exception.OutletNotFoundException;
import exception.RentalRateNotFoundException;
import exception.ReservationNotFoundException;
import exception.TransitRecordNotFoundException;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author jonta
 */
@Stateless
public class ReservationSessionBean implements ReservationSessionBeanRemote, ReservationSessionBeanLocal {

    @EJB
    private ModelSessionBeanLocal modelSessionBean;

    @EJB
    private RentalRateSessionBeanLocal rentalRateSessionBean;

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
    public Long creatNewReservation(Reservation reservation, Long modelId, Long returnOutletId, Long pickUpOutletId, Long creditCardId, Long customerId, List<RentalRate> finalRentalRatesApplied) throws ModelNotFoundException, OutletNotFoundException, RentalRateNotFoundException, CustomerNotFoundException 
    {
        Model model = modelSessionBean.retrieveModelById(modelId);
        OutletEntity returnOutlet = outletSessionBeanLocal.retrieveOutletById(returnOutletId);
        OutletEntity pickUpOutlet = outletSessionBeanLocal.retrieveOutletById(pickUpOutletId);
        CreditCard creditCard = creditCardSessionBeanLocal.retrieveCreditCardById(creditCardId);
        Customer customer = customerSessionBeanLocal.retrieveCustomerById(customerId);
        
        em.persist(reservation);
        
        reservation.setModel(model);
        reservation.setReturnOutlet(returnOutlet);
         reservation.setPickUpOutlet(pickUpOutlet);
        //model.getReservations().add(reservation);
        returnOutlet.getReservations().add(reservation);
        reservation.setCreditCard(creditCard);
        creditCard.setReservation(reservation);
        reservation.setCustomer(customer);
        customer.getReservations().add(reservation);
        for (RentalRate rentalRate : finalRentalRatesApplied) {   
            RentalRate updateInUse = rentalRateSessionBean.retrieveRentalRateByRentalRateId(rentalRate.getId());
            updateInUse.setInUse(true);
        }
        
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
            throw new ReservationNotFoundException("Reservation Not Found!");
        }
    }
    
    @Override
    public List<Reservation> retrieveReservationsOnPickUpDate(Date date) throws ReservationNotFoundException{
        Query query = em.createQuery("SELECT r FROM Reservation r WHERE r.pickUpDate = :date");
        query.setParameter("date", date);
        if (!query.getResultList().isEmpty()){
               return query.getResultList();
        } else {
            throw new ReservationNotFoundException("Reservations Not Found!");
        }
    }
    
    @Override
    public List<Reservation> retrieveReservationsByCustomer(Long customerId) throws ReservationNotFoundException, CustomerNotFoundException{
        Customer customer = customerSessionBeanLocal.retrieveCustomerById(customerId);
        Query query = em.createQuery("SELECT r FROM Reservation r WHERE r.customer = :customer");
        query.setParameter("customer", customer);
        if (!query.getResultList().isEmpty()){
               return query.getResultList();
        } else {
            throw new ReservationNotFoundException("You have no reservations!");
        }
    }
    
    @Override
    public double cancelReservation(Reservation reservation, Date currDate) throws ReservationNotFoundException {
        Reservation reservationToCancel = retrieveReservationById(reservation.getId());
        if (reservationToCancel.getCar() != null){
            reservationToCancel.getCar().getReservations().remove(reservationToCancel);
        }
        reservationToCancel.getReturnOutlet().getReservations().remove(reservationToCancel);
        reservationToCancel.getPickUpOutlet().getReservations().remove(reservationToCancel);
        reservationToCancel.getCustomer().getReservations().remove(reservationToCancel);
        //reservationToCancel.getCreditCard().setReservation(null);
        
        
        em.remove(reservationToCancel);  
        
        long diffInMillies = Math.abs(reservationToCancel.getPickUpDate().getTime()  - currDate.getTime());
        long diffInDays = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
        double penalty =0;
        if (diffInDays < 3) {
            penalty = 0.7;
        } else if (diffInDays < 7) {
            penalty = 0.5;
        } else if (diffInDays < 14) {
            penalty = 0.3;
        } else {
            penalty =0;
        }
        
        return penalty * reservationToCancel.getTotalCost();
    }

    
}
