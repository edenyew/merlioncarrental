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
import exception.InputDataValidationException;
import exception.ModelNotFoundException;
import exception.OutletNotFoundException;
import exception.RentalRateNotFoundException;
import exception.ReservationNotFoundException;
import exception.TransitRecordNotFoundException;
import exception.UnknownPersistenceException;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
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
    private CustomerEntitySessionBeanLocal customerSessionBeanLocal;
    @EJB
    private PartnerEntitySessionBeanLocal partnerSessionBeanLocal;
    
    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    public ReservationSessionBean() 
    {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }
    

    @Override
    public Long creatNewReservation(Reservation reservation, Long modelId, Long returnOutletId, Long pickUpOutletId, Long creditCardId, Long customerId, List<RentalRate> finalRentalRatesApplied) throws ModelNotFoundException, OutletNotFoundException, RentalRateNotFoundException, CustomerNotFoundException, ReservationNotFoundException, UnknownPersistenceException, InputDataValidationException 
    {
        Set<ConstraintViolation<Reservation>>constraintViolations = validator.validate(reservation);
        
        if (constraintViolations.isEmpty())
        {
            try
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
            catch(PersistenceException ex)
            {
                if(ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException"))
                {
                    if(ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException"))
                    {
                        throw new ReservationNotFoundException();
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
    
    
    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<Reservation>>constraintViolations)
    {
        String msg = "Input data validation error!:";
            
        for(ConstraintViolation constraintViolation:constraintViolations)
        {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }
        
        return msg;
    }

    
}
