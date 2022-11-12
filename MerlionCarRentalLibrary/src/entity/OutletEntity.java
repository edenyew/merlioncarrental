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
import javax.persistence.OneToMany;
import java.util.List;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


/**
 *
 * @author edenyew
 */
@Entity
public class OutletEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long outletId;
    
    @Column(nullable = false, length = 64, unique = true)
    @NotNull
    @Size(min = 1, max = 64)
    private String address;
       
    @Column(nullable = true, length = 64)
    @Size(min = 1, max = 64)
    private String openingTime;
    
    @Column(nullable = true, length = 64)
    @Size(min = 1, max = 64)
    private String closingTime;
    
    @OneToMany(mappedBy = "returnOutlet")
    private List<Reservation> reservations;
    
    @OneToMany(mappedBy = "outletEntity")
    private List<CarEntity> cars;
    
    @OneToMany(mappedBy = "returnOutlet")
    private List<TransitDriverDispatchRecord> transitDriverDispatchRecords;
    
    @OneToMany(mappedBy = "outlet")
    private List<EmployeeEntity> employees;

    
    public OutletEntity() {
    }

    public OutletEntity(String address, String openingTime, String closingTime, List<Reservation> reservations, List<CarEntity> cars, List<TransitDriverDispatchRecord> transitDriverDispatchRecords) {
        this.address = address;
        this.openingTime = openingTime;
        this.closingTime = closingTime;
        this.reservations = reservations;
        this.cars = cars;
        this.transitDriverDispatchRecords = transitDriverDispatchRecords;
    }
   

    public Long getOutletId() {
        return outletId;
    }

    public void setOutletId(Long outletId) {
        this.outletId = outletId;
    }

    public List<TransitDriverDispatchRecord> getTransitDriverDispatchRecords() {
        return transitDriverDispatchRecords;
    }

    public void setTransitDriverDispatchRecords(List<TransitDriverDispatchRecord> transitDriverDispatchRecords) {
        this.transitDriverDispatchRecords = transitDriverDispatchRecords;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (outletId != null ? outletId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof OutletEntity)) {
            return false;
        }
        OutletEntity other = (OutletEntity) object;
        if ((this.outletId == null && other.outletId != null) || (this.outletId != null && !this.outletId.equals(other.outletId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.OutletEntity[ id=" + outletId + " ]";
    }

    /**
     * @return the address
     */
    public String getAddress() {
        return address;
    }

    /**
     * @param address the address to set
     */
    public void setAddress(String address) {
        this.address = address;
    
    }

    public String getOpeningTime() {
        return openingTime;
    }

    public void setOpeningTime(String openingTime) {
        this.openingTime = openingTime;
    }

    public String getClosingTime() {
        return closingTime;
    }

    public void setClosingTime(String closingTime) {
        this.closingTime = closingTime;
    }

    /**
     * @return the cars
     */
    public List<CarEntity> getCars() {
        return cars;
    }

    /**
     * @param cars the cars to set
     */
    public void setCars(List<CarEntity> cars) {
        this.cars = cars;
    }

    public List<EmployeeEntity> getEmployees() {
        return employees;
    }

    public void setEmployees(List<EmployeeEntity> employees) {
        this.employees = employees;
    }

    /**
     * @return the reservations
     */
    public List<Reservation> getReservations() {
        return reservations;
    }

    /**
     * @param reservations the reservations to set
     */
    public void setReservations(List<Reservation> reservations) {
        this.reservations = reservations;
    } 
    
   
    

    
}
