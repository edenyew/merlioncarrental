/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import util.enumeration.TransitStatusEnum;

/**
 *
 * @author jonta
 */
@Entity
public class TransitDriverDispatchRecord implements Serializable {


    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long transitDriverDispatchId;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date dateOfTransit;
    private TransitStatusEnum transitStatus;
    @OneToOne
    private EmployeeEntity transitDriver;
    @ManyToOne
    private CarEntity car;
    @OneToOne
    private OutletEntity pickUpOutlet;
    @OneToOne
    private OutletEntity returnOutlet;

    public TransitDriverDispatchRecord() {
    }

    public TransitDriverDispatchRecord(Date dateOfTransit, TransitStatusEnum transitStatus, EmployeeEntity transitDriver, CarEntity car, OutletEntity pickUpOutlet, OutletEntity returnOutlet) {
        this.dateOfTransit = dateOfTransit;
        this.transitStatus = transitStatus;
        this.transitDriver = transitDriver;
        this.car = car;
        this.pickUpOutlet = pickUpOutlet;
        this.returnOutlet = returnOutlet;
    }
    
    public Long getTransitDriverDispatchId() {
        return transitDriverDispatchId;
    }

    public void setTransitDriverDispatchId(Long transitDriverDispatchId) {
        this.transitDriverDispatchId = transitDriverDispatchId;
    }
    
     public OutletEntity getPickUpOutlet() {
        return pickUpOutlet;
    }

    public void setPickUpOutlet(OutletEntity pickUpOutlet) {
        this.pickUpOutlet = pickUpOutlet;
    }

    public OutletEntity getReturnOutlet() {
        return returnOutlet;
    }

    public void setReturnOutlet(OutletEntity returnOutlet) {
        this.returnOutlet = returnOutlet;
    }

    public CarEntity getCar() {
        return car;
    }

    public void setCar(CarEntity car) {
        this.car = car;
    }

    public EmployeeEntity getTransitDriver() {
        return transitDriver;
    }

    public void setTransitDriver(EmployeeEntity transitDriver) {
        this.transitDriver = transitDriver;
    }

    public Date getDateOfTransit() {
        return dateOfTransit;
    }

    public void setDateOfTransit(Date dateOfTransit) {
        this.dateOfTransit = dateOfTransit;
    }
    
     public TransitStatusEnum getTransitStatus() {
        return transitStatus;
    }

    public void setTransitStatus(TransitStatusEnum transitStatus) {
        this.transitStatus = transitStatus;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (transitDriverDispatchId != null ? transitDriverDispatchId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TransitDriverDispatchRecord)) {
            return false;
        }
        TransitDriverDispatchRecord other = (TransitDriverDispatchRecord) object;
        if ((this.transitDriverDispatchId == null && other.transitDriverDispatchId != null) || (this.transitDriverDispatchId != null && !this.transitDriverDispatchId.equals(other.transitDriverDispatchId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.TransitDriverDispatchRecord[ id=" + transitDriverDispatchId + " ]";
    }
    
}
