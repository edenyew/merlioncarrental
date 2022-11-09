/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.ws;

import ejb.session.stateless.CarEntitySessionBeanLocal;
import entity.CarEntity;
import exception.CarNotFoundException;
import exception.OutletNotFoundException;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.ejb.Stateless;

/**
 *
 * @author jonta
 */
@WebService(serviceName = "CarWebService")
@Stateless()
public class CarWebService {

    @EJB
    private CarEntitySessionBeanLocal carEntitySessionBean;
    
    @WebMethod(operationName = "retrieveCarById")
    public CarEntity retrieveCarById(Long carId) throws CarNotFoundException
    {
        return carEntitySessionBean.retrieveCarById(carId);
    }
    
    @WebMethod(operationName = "findListOfCars")
    public List<CarEntity> findListOfCars(Long pickUpOutletId, Long returnOutletId, Date pickUpDate, Date returnDate) throws OutletNotFoundException{
        
        return carEntitySessionBean.findListOfCars(pickUpOutletId, returnOutletId, pickUpDate, returnDate);
    }
}
