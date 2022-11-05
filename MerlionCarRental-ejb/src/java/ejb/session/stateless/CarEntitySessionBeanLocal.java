/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CarEntity;
import entity.Model;
import exception.CarNotFoundException;
import exception.ModelNotFoundException;
import exception.OutletNotFoundException;
import exception.RentalRateNotFoundException;
import java.util.Date;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author jonta
 */
@Local
public interface CarEntitySessionBeanLocal {

    public Long createNewCar(CarEntity car, Long modelId, Long outletId, Long rentalRateId) throws ModelNotFoundException, OutletNotFoundException, RentalRateNotFoundException;

    public List<CarEntity> retrieveAllCars();

    public CarEntity retrieveCarByPlateNumber(String carPlateNumber) throws CarNotFoundException;

    public void updateCarEntity(CarEntity carEntity) throws CarNotFoundException;

    public void deleteCarEntity(CarEntity car) throws CarNotFoundException;

    public CarEntity retrieveCarById(Long carId) throws CarNotFoundException;

    public void viewCarDetails(CarEntity car) throws CarNotFoundException;
    
    public List<CarEntity> findListOfCars(Long pickUpOutletId, Long returnOutletId, Date pickUpDate, Date returnDate) throws OutletNotFoundException;
    
}
