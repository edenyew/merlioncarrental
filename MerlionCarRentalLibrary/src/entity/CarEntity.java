/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import util.enumeration.CarStatusEnum;

/**
 *
 * @author jonta
 */
@Entity
public class CarEntity implements Serializable {


    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long carId;
    @Column(length = 8, nullable = false)
    private String carPlateNumber;
    @Column(nullable = false)
    private String colour;
    private String location;
    @Column(nullable = false)
    private Boolean disabled;
    private CarStatusEnum currentStatus;
    
    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private Model model;
    
    @ManyToOne (optional = false)
    private Category category;
    
    @OneToMany(mappedBy="car")
    private List<RentalRate> rentalRates;
    
    @ManyToOne 
    private OutletEntity outletEntity;
    
    @OneToMany(mappedBy = "car")
    private List<TransitDriverDispatchRecord> transitDriverDispatchRecords;
    
    @OneToMany(mappedBy = "car")
    private List<Reservation> reservations;
    
    
    public CarEntity() {
    }

    public CarEntity(String carPlateNumber, String colour, String location, Boolean disabled, CarStatusEnum currentStatus, Model model, Category category, List<RentalRate> rentalRate, OutletEntity outletEntity, List<TransitDriverDispatchRecord> transitDriverDispatchRecords, List<Reservation> reservations) {
        this.carPlateNumber = carPlateNumber;
        this.colour = colour;
        this.location = location;
        this.disabled = disabled;
        this.currentStatus = currentStatus;
        this.model = model;
        this.category = category;
        this.rentalRates = rentalRate;
        this.outletEntity = outletEntity;
        this.transitDriverDispatchRecords = transitDriverDispatchRecords;
        this.reservations = reservations;
    }

   

    
      public List<Reservation> getReservations() {
        return reservations;
    }

    public void setReservations(List<Reservation> reservations) {
        this.reservations = reservations;
    }
    
    
    public Long getCarId() {
        return carId;
    }

    public void setCarId(Long carId) {
        this.carId = carId;
    }
    

     public CarStatusEnum getCurrentStatus() {
        return currentStatus;
    }

    public void setCurrentStatus(CarStatusEnum currentStatus) {
        this.currentStatus = currentStatus;
    }
    
     public List<TransitDriverDispatchRecord> getTransitDriverDispatchRecords() {
        return transitDriverDispatchRecords;
    }

    public void setTransitDriverDispatchRecords(List<TransitDriverDispatchRecord> transitDriverDispatchRecords) {
        this.transitDriverDispatchRecords = transitDriverDispatchRecords;
    }
    
    

    public OutletEntity getOutletEntity() {
        return outletEntity;
    }

    public void setOutletEntity(OutletEntity outletEntity) {
        this.outletEntity = outletEntity;
    }
    
      public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getColour() {
        return colour;
    }

    public void setColour(String colour) {
        this.colour = colour;
    }

    public List<RentalRate> getRentalRates() {
        return rentalRates;
    }

    public void setRentalRates(List<RentalRate> rentalRates) {
        this.rentalRates = rentalRates;
    }

   
    public String getCarPlateNumber() {
        return carPlateNumber;
    }
    
    public void setCarPlateNumber(String carPlateNumber) {
        this.carPlateNumber = carPlateNumber;
    }

      public Model getModel() {
        return model;
    }

    public void setModel(Model model) {
        this.model = model;
    }
    
      public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
    
    public Boolean getDisabled() {
        return disabled;
    }

    public void setDisabled(Boolean disabled) {
        this.disabled = disabled;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (carId != null ? carId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CarEntity)) {
            return false;
        }
        CarEntity other = (CarEntity) object;
        if ((this.carId == null && other.carId != null) || (this.carId != null && !this.carId.equals(other.carId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.CarEntity[ id=" + carId + " ]";
    }
    
}
