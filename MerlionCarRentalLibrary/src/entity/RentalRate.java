/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import util.enumeration.RentalRateTypeEnum;

/**
 *
 * @author edenyew
 */
@Entity
public class RentalRate implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, length = 32)
    private String name;
       
    @Column(precision = 11, scale = 2)
    private Long ratePerDay;
    
    @Column(nullable = false, length = 32)
    private String validityPeriod;
    
    @Column(nullable = true)
    private Date startDate;
    
    @Column(nullable = true)
    private Date endDate;
    
    @Column(nullable = false)
    private Boolean inUse;
    
    @Column(nullable = false)
    private Boolean disabled;
    
    @OneToOne (optional = false)
    @JoinColumn(nullable = false, name = "CategoryId")
    private Category category;
    
    @Column(nullable=false)
    private RentalRateTypeEnum rentalRateType = RentalRateTypeEnum.DEFAULT;

    public RentalRate() {
    }

    public RentalRate(String name, Long ratePerDay, String validityPeriod, Date startDate, Date endDate, Boolean inUse, Boolean disabled, Category category) {
        this.name = name;
        this.ratePerDay = ratePerDay;
        this.validityPeriod = validityPeriod;
        this.startDate = startDate;
        this.endDate = endDate;
        this.inUse = inUse;
        this.disabled = disabled;
        this.category = category;
    }
       

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof RentalRate)) {
            return false;
        }
        RentalRate other = (RentalRate) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.RentalRate[ id=" + id + " ]";
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the category
     */
    public Category getCategory() {
        return category;
    }

    /**
     * @param category the category to set
     */
    public void setCategory(Category category) {
        this.category = category;
    }

    /**
     * @return the ratePerDay
     */
    public Long getRatePerDay() {
        return ratePerDay;
    }

    /**
     * @param ratePerDay the ratePerDay to set
     */
    public void setRatePerDay(Long ratePerDay) {
        this.ratePerDay = ratePerDay;
    }

    public RentalRateTypeEnum getRentalRateType() {
        return rentalRateType;
    }

    public void setRentalRateType(RentalRateTypeEnum rentalRateType) {
        this.rentalRateType = rentalRateType;
    }

    /**
     * @return the validityPeriod
     */
    public String getValidityPeriod() {
        return validityPeriod;
    }

    /**
     * @param validityPeriod the validityPeriod to set
     */
    public void setValidityPeriod(String validityPeriod) {
        this.validityPeriod = validityPeriod;
    }

    /**
     * @return the disabled
     */
    public Boolean getDisabled() {
        return disabled;
    }

    /**
     * @param disabled the disabled to set
     */
    public void setDisabled(Boolean disabled) {
        this.disabled = disabled;
    }

    /**
     * @return the inUse
     */
    public Boolean getInUse() {
        return inUse;
    }

    /**
     * @param inUse the inUse to set
     */
    public void setInUse(Boolean inUse) {
        this.inUse = inUse;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
  
    
}
