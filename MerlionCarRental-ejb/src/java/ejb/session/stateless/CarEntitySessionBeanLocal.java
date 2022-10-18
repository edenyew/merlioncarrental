/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CarEntity;
import entity.Model;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author jonta
 */
@Local
public interface CarEntitySessionBeanLocal {

    public Long createNewCar(CarEntity car, Long modelId);

    public List<CarEntity> retrieveAllCars();

    public CarEntity retrieveCarByPlateNumber(String carPlateNumber);

    public void updateCarEntity(CarEntity carEntity);
    
}
