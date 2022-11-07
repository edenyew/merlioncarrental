/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import util.enumeration.AccessRightEnum;

/**
 *
 * @author edenyew
 */
@Entity
public class EmployeeEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 32)
    private String name;
    @Column(nullable = false, length = 32)
    private String contactNumber;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AccessRightEnum accessRightEnum;
    @Column(nullable = false, unique = true, length = 32)
    private String username;
    @Column(nullable = false, length = 32)
    private String password;
    private boolean logged_in;
    
    @OneToOne(optional = true)
    @JoinColumn(name = "transitDriverRecordId", nullable = true)
    private TransitDriverDispatchRecord  transitDriverDistpachRecord;
    

    public EmployeeEntity() {
    }

    public EmployeeEntity(String name, String contactNumber, AccessRightEnum accessRightEnum, String username, String password) {
        this.name = name;
        this.contactNumber = contactNumber;
        this.accessRightEnum = accessRightEnum;
        this.username = username;
        this.password = password;
    }

    public EmployeeEntity(Long id, String name, String contactNumber, AccessRightEnum accessRightEnum, String username, String password) {
        this.id = id;
        this.name = name;
        this.contactNumber = contactNumber;
        this.accessRightEnum = accessRightEnum;
        this.username = username;
        this.password = password;
    }

    public EmployeeEntity(Long id, String name, String contactNumber, AccessRightEnum accessRightEnum, String username, String password, boolean logged_in) {
        this.id = id;
        this.name = name;
        this.contactNumber = contactNumber;
        this.accessRightEnum = accessRightEnum;
        this.username = username;
        this.password = password;
        this.logged_in = logged_in;
    }
    
    public EmployeeEntity login() {
        this.setLogged_in(true);
        return this;
    }

    public EmployeeEntity logout() {
        this.setLogged_in(false);
        return this;
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
        if (!(object instanceof EmployeeEntity)) {
            return false;
        }
        EmployeeEntity other = (EmployeeEntity) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.EmployeeEntity[ id=" + id + " ]";
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
     * @return the contactNumber
     */
    public String getContactNumber() {
        return contactNumber;
    }

    /**
     * @param contactNumber the contactNumber to set
     */
    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    /**
     * @return the accessRightEnum
     */
    public AccessRightEnum getAccessRightEnum() {
        return accessRightEnum;
    }

    /**
     * @param accessRightEnum the accessRightEnum to set
     */
    public void setAccessRightEnum(AccessRightEnum accessRightEnum) {
        this.accessRightEnum = accessRightEnum;
    }

    /**
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return the logged_in
     */
    public boolean isLogged_in() {
        return logged_in;
    }

    /**
     * @param logged_in the logged_in to set
     */
    public void setLogged_in(boolean logged_in) {
        this.logged_in = logged_in;
    }

    /**
     * @return the transitDriverDistpachRecord
     */
    public TransitDriverDispatchRecord getTransitDriverDistpachRecord() {
        return transitDriverDistpachRecord;
    }

    /**
     * @param transitDriverDistpachRecord the transitDriverDistpachRecord to set
     */
    public void setTransitDriverDistpachRecord(TransitDriverDispatchRecord transitDriverDistpachRecord) {
        this.transitDriverDistpachRecord = transitDriverDistpachRecord;
    }
    
}
