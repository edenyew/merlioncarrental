/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.ws;

import ejb.session.stateless.CarEntitySessionBeanLocal;
import ejb.session.stateless.OutletEntitySessionBeanLocal;
import ejb.session.stateless.PartnerEntitySessionBeanLocal;
import ejb.session.stateless.RentalRateSessionBeanLocal;
import ejb.session.stateless.ReservationSessionBeanLocal;
import entity.CarEntity;
import entity.OutletEntity;
import entity.PartnerEntity;
import entity.Reservation;
import exception.CarNotFoundException;
import exception.CustomerNotFoundException;
import exception.InvalidLoginCredentialException;
import exception.OutletNotFoundException;
import exception.RentalRateNotFoundException;
import exception.ReservationNotFoundException;
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
   
  

    
    @WebMethod(operationName = "createNewReservation")
    public Long createNewReservation(Reservation reservation, Long carId, Long returnOutletId, Long pickUpOutletId, Long creditCardId, Long customerId) throws CarNotFoundException, OutletNotFoundException, RentalRateNotFoundException, CustomerNotFoundException{
        Long reservationId = reservationSessionBean.creatNewReservationPartner(reservation, carId, returnOutletId, pickUpOutletId, creditCardId, customerId);
        return reservationId;
    }
    
    @WebMethod(operationName = "retrieveReservationById")
    public Reservation retrieveReservationById(Long reservationId) throws ReservationNotFoundException
    {
      return reservationSessionBean.retrieveReservationById(reservationId);
    }
    
    @WebMethod(operationName = "retrieveAllOutlets")
    public List<OutletEntity> retrieveAllOutlets() {
        return outletEntitySessionBean.retrieveAllOutlets();
    }
    
    @WebMethod(operationName = "cancelReservation")
    public double cancelReservation(Reservation reservation, Date currDate) throws ReservationNotFoundException
    {
        return reservationSessionBean.cancelReservation(reservation, currDate);
    }
    
    

}
