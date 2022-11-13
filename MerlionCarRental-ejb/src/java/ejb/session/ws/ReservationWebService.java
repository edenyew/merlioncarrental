/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.ws;

import ejb.session.stateless.CarEntitySessionBeanLocal;
import ejb.session.stateless.CreditCardSessionBeanLocal;
import ejb.session.stateless.OutletEntitySessionBeanLocal;
import ejb.session.stateless.PartnerEntitySessionBeanLocal;
import ejb.session.stateless.RentalRateSessionBeanLocal;
import ejb.session.stateless.ReservationSessionBeanLocal;
import entity.CarEntity;
import entity.CreditCard;
import entity.OutletEntity;
import entity.PartnerEntity;
import entity.Reservation;
import exception.CarNotFoundException;
import exception.CreditCardNotFoundException;
import exception.CustomerNotFoundException;
import exception.InputDataValidationException;
import exception.InvalidLoginCredentialException;
import exception.OutletNotFoundException;
import exception.RentalRateNotFoundException;
import exception.ReservationNotFoundException;
import exception.UnknownPersistenceException;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.enumeration.CarStatusEnum;

/**
 *
 * @author jonta
 */
@WebService(serviceName = "ReservationWebService")
@Stateless()
public class ReservationWebService {

    

    @EJB
    private RentalRateSessionBeanLocal rentalRateSessionBean;

    @EJB
    private OutletEntitySessionBeanLocal outletEntitySessionBean;

    @PersistenceContext(unitName = "MerlionCarRental-ejbPU")
    private EntityManager em;

    @EJB
    private ReservationSessionBeanLocal reservationSessionBean;
    @EJB 
    private CreditCardSessionBeanLocal creditCardSessionBeanLocal;
   
  

    
    @WebMethod(operationName = "createNewReservation")
    public Long createNewReservation(@WebParam(name = "reservation") Reservation reservation, 
                                        @WebParam(name = "carId") Long carId, 
                                            @WebParam(name = "returnOutletId") Long returnOutletId, 
                                                @WebParam(name = "pickUpOutletId") Long pickUpOutletId, 
                                                    @WebParam(name = "creditCardId") Long creditCardId,
                                                        @WebParam(name = "customerId") Long customerId) throws CarNotFoundException, OutletNotFoundException, RentalRateNotFoundException, CustomerNotFoundException{
        Long reservationId = reservationSessionBean.creatNewReservationPartner(reservation, carId, returnOutletId, pickUpOutletId, creditCardId, customerId);
        return reservationId;
    }
    
    @WebMethod(operationName = "retrieveReservationById")
    
    public Reservation retrieveReservationById(@WebParam(name = "reservationId") Long reservationId) throws ReservationNotFoundException
    {
      return reservationSessionBean.retrieveReservationById(reservationId);
    }
    
    @WebMethod(operationName = "retrieveAllOutlets")
    public List<OutletEntity> retrieveAllOutlets() {
        return outletEntitySessionBean.retrieveAllOutlets();
    }
    

    
    @WebMethod(operationName = "retrieveOutletById")
    public OutletEntity retrieveOutletById(@WebParam(name = "outletId") Long outletId) throws OutletNotFoundException
    {
        return outletEntitySessionBean.retrieveOutletById(outletId);
    }
    
    @WebMethod(operationName = "cancelReservation")
    public double cancelReservation(@WebParam(name = "reservation") Reservation reservation,@WebParam(name = "currentDate")  Date currDate) throws ReservationNotFoundException
    {
        return reservationSessionBean.cancelReservation(reservation, currDate);
    }
    
    @WebMethod(operationName = "createCreditCard")
    public double createCreditCard(@WebParam(name = "creditCard") CreditCard creditCard) throws CreditCardNotFoundException, UnknownPersistenceException, InputDataValidationException 
    {
        return creditCardSessionBeanLocal.createNewCreditCard(creditCard);
    }
    

}
