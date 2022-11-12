/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author edenyew
 */
@Entity
public class CreditCard implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, length = 32)
    @NotNull
    @Size(min = 8, max = 32)
    private String creditCardNum;
    
    @Column(nullable = false, length = 3)
    @NotNull
    @Size(min = 3, max = 10)
    private String CVV;
    
    @Column(nullable = false, length = 32)
    @NotNull
    @Size(min = 1, max = 64)
    private String cardName;
    
    @Column(nullable = false, length = 32)
    @NotNull
    @Size(min = 1, max = 64)
    private String expiryDate;

    @OneToOne (optional = true)
    @JoinColumn(nullable = true, name ="CustomerId")
    private Customer customer;
    
    @OneToOne
    @JoinColumn(name ="ReservationId")
    private Reservation reservation;
   

    public CreditCard() {
    }

    public CreditCard(String creditCardNum, String CVV, String cardName, String expiryDate) {
        this.creditCardNum = creditCardNum;
        this.CVV = CVV;
        this.cardName = cardName;
        this.expiryDate = expiryDate;
    }
    
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCreditCardNum() {
        return creditCardNum;
    }

    public void setCreditCardNum(String creditCardNum) {
        this.creditCardNum = creditCardNum;
    }

    public String getCVV() {
        return CVV;
    }

    public void setCVV(String CVV) {
        this.CVV = CVV;
    }

    public String getCardName() {
        return cardName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public Reservation getReservation() {
        return reservation;
    }

    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
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
        if (!(object instanceof CreditCard)) {
            return false;
        }
        CreditCard other = (CreditCard) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.CreditCard[ id=" + id + " ]";
    }
    
}
