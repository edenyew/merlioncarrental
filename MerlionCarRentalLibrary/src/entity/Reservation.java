/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class Reservation implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    @NotNull
    @Temporal(TemporalType.DATE)
    private Date pickUpDate;
    
    @Column(nullable = false)
    @NotNull
    @Temporal(TemporalType.TIME)
    private Date pickUpTime;
    
    @Column(nullable = false)
    @NotNull
    @Temporal(TemporalType.DATE)
    private Date returnDate;
    
    @Column(nullable = false)
    @NotNull
    @Temporal(TemporalType.TIME)
    private Date returnTime;
    
    @Column(precision = 11, scale = 2)
    @NotNull
    private double totalCost;
    
    @OneToOne(orphanRemoval = true, optional = true)
    @JoinColumn(name="CreditCardId", nullable = true)
    private CreditCard creditCard;
    
    private boolean paid;
    
    @ManyToOne (optional = true)
    @JoinColumn(name = "CarId", nullable = true)
    private CarEntity car;
    
    @OneToOne (optional = false)
    @JoinColumn(name = "pickUpOutletId", nullable = false)
    private OutletEntity pickUpOutlet;
    
    @OneToOne (optional = false)
    @JoinColumn(name = "returnOutletId", nullable = false)
    private OutletEntity returnOutlet;
    @OneToMany 
    private List<RentalRate> rentalRates;
    @ManyToOne
    private Model model;
    
    @ManyToOne 
//    //@JoinColumn(name = "customerId")
     private Customer customer;
    
    @ManyToOne
    private PartnerEntity partner;

    
    public Reservation() {
    }

//    public Reservation(Date pickUpDate, Date returnDate, double totalCost, CreditCard creditCard, CarEntity car, OutletEntity pickUpOutlet, OutletEntity returnOutlet, List<RentalRate> rentalRates, Customer customer, boolean paid) {
//        this.pickUpDate = pickUpDate;
//        this.returnDate = returnDate;
//        this.totalCost = totalCost;
//        this.creditCard = creditCard;
//        this.car = car;
//        this.pickUpOutlet = pickUpOutlet;
//        this.returnOutlet = returnOutlet;
//        this.rentalRates = rentalRates;
//        this.customer = customer;
//        this.paid = paid;
//    }

    public Reservation(Date pickUpDate, Date pickUpTime, Date returnDate, Date returnTime, double totalCost, CreditCard creditCard, boolean paid, CarEntity car, OutletEntity pickUpOutlet, OutletEntity returnOutlet, Customer customer) {
        this.pickUpDate = pickUpDate;
        this.pickUpTime = pickUpTime;
        this.returnDate = returnDate;
        this.returnTime = returnTime;
        this.totalCost = totalCost;
        this.creditCard = creditCard;
        this.paid = paid;
        this.car = car;
        this.pickUpOutlet = pickUpOutlet;
        this.returnOutlet = returnOutlet;
        this.customer = customer;
    }
    
    public Reservation(Date pickUpDate, Date returnDate, double totalCost, CreditCard creditCard, boolean paid, CarEntity car, OutletEntity pickUpOutlet, OutletEntity returnOutlet, List<RentalRate> rentalRates, PartnerEntity partner) {
        this.pickUpDate = pickUpDate;
        this.returnDate = returnDate;
        this.totalCost = totalCost;
        this.creditCard = creditCard;
        this.paid = paid;
        this.car = car;
        this.pickUpOutlet = pickUpOutlet;
        this.returnOutlet = returnOutlet;
        this.rentalRates = rentalRates;
        this.partner = partner;
    }

   

    public Date getPickUpDate() {
        return pickUpDate;
    }

    public void setPickUpDate(Date pickUpDate) {
        this.pickUpDate = pickUpDate;
    }

    public Date getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(Date returnDate) {
        this.returnDate = returnDate;
    }

    public List<RentalRate> getRentalRates() {
        return rentalRates;
    }

    public void setRentalRates(List<RentalRate> rentalRates) {
        this.rentalRates = rentalRates;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }

    public CreditCard getCreditCard() {
        return creditCard;
    }

    public void setCreditCard(CreditCard creditCard) {
        this.creditCard = creditCard;
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
        if (!(object instanceof Reservation)) {
            return false;
        }
        Reservation other = (Reservation) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Reservation[ id=" + id + " ]";
    }

    /**
     * @return the car
     */
    public CarEntity getCar() {
        return car;
    }

    /**
     * @param car the car to set
     */
    public void setCar(CarEntity car) {
        this.car = car;
    }

    public Date getPickUpTime() {
        return pickUpTime;
    }

    public void setPickUpTime(Date pickUpTime) {
        this.pickUpTime = pickUpTime;
    }

    public Date getReturnTime() {
        return returnTime;
    }

    public void setReturnTime(Date returnTime) {
        this.returnTime = returnTime;
    }

    public boolean isPaid() {
        return paid;
    }

    public void setPaid(boolean paid) {
        this.paid = paid;
    }

    /**
     * @return the outlet
     */
    public OutletEntity getPickUpOutlet() {
        return pickUpOutlet;
    }

    public PartnerEntity getPartner() {
        return partner;
    }

    public void setPartner(PartnerEntity partner) {
        this.partner = partner;
    }

    /**
     * @param outlet the outlet to set
     */
    public void setPickUpOutlet(OutletEntity outlet) {
        this.pickUpOutlet = outlet;
    }

    public Model getModel() {
        return model;
    }

    public void setModel(Model model) {
        this.model = model;
    }

    public OutletEntity getReturnOutlet() {
        return returnOutlet;
    }

    public void setReturnOutlet(OutletEntity returnOutlet) {
        this.returnOutlet = returnOutlet;
    }

    /**
     * @return the customer
     */
    public Customer getCustomer() {
        return customer;
    }

    /**
     * @param customer the customer to set
     */
    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
    
}
