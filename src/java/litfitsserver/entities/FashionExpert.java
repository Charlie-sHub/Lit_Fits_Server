/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package litfitsserver.entities;

import java.io.Serializable;
import java.security.Timestamp;
import java.util.Date;
import java.util.Objects;
import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Ander Rodriguez
 */
@Entity
@Table(name="fashionExpert", schema="Lit_Fits_DB")
@XmlRootElement
public class FashionExpert implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @Id
    private String username;
    @NotNull
    private String password;
    @NotNull
    private String phoneNumber;
    @NotNull
    private String fullName;
    @NotNull
    private String email;
    private String publication;
    @Temporal(TemporalType.DATE)
    private Date lastPasswordChange;
    @Temporal(TemporalType.DATE)
    private Date lastAccess;
    @ManyToMany
    @JoinTable(name = "expert_materials", schema = "Lit_Fits_DB")
    private Set<Material> recommendedMaterials;
    @ManyToMany
    @JoinTable(name = "expert_colors", schema = "Lit_Fits_DB")
    private Set<Color> recommendedColors;

    public FashionExpert() {
    }

    public FashionExpert(String username, String password, String phoneNumber, String fullName, String email, String publication, Date lastPasswordChange, Date lastAccess, Set<Material> recommendedMaterials, Set<Color> recommendedColors) {
        this.username = username;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.fullName = fullName;
        this.email = email;
        this.publication = publication;
        this.lastPasswordChange = lastPasswordChange;
        this.lastAccess = lastAccess;
        this.recommendedMaterials = recommendedMaterials;
        this.recommendedColors = recommendedColors;
    }
    
    
    
    /**
     * Function toString returns all the fields of the expert
     * @return a String with all the fields
     */
    @Override
    public String toString() {
        return "Expert{ " + "Username: " + username + ", Password: " + password + ", Phone Number: " + phoneNumber + 
                ", Full Name: " + fullName + ", Email: " + email + ", Publication: " + publication + ", lastAccess=" + lastAccess + ", lastPasswordChange=" + lastPasswordChange
                + ", Recommended Materials: " + recommendedMaterials + "and Recommended Colors: " + recommendedColors + " }" ;
   }
    /**
     * Funvtion hash code will verify
     * @return an integer
     */
    @Override
    public int hashCode() {
        return 0;

    }
    /**
     * Functon equals receives an object and compare it
     * @param obj an object is receive for comparing
     * @return a boolean if it is equals true, if not false
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final FashionExpert other = (FashionExpert) obj;
        if(!Objects.equals(this.username, other.username))
            return false;
        
        if (!Objects.equals(this.password, other.password)) 
            return false;

        if (!Objects.equals(this.phoneNumber, other.phoneNumber)) 
            return false;
        
        if (!Objects.equals(this.lastAccess, other.lastAccess)) 
            return false;
        
        if (!Objects.equals(this.lastPasswordChange, other.lastPasswordChange))
            return false;
        return true;
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
     * @return the phoneNumber
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * @param phoneNumber the phoneNumber to set
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /**
     * @return the fullName
     */
    public String getFullName() {
        return fullName;
    }

    /**
     * @param fullName the fullName to set
     */
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return the publication
     */
    public String getPublication() {
        return publication;
    }

    /**
     * @param publication the publication to set
     */
    public void setPublication(String publication) {
        this.publication = publication;
    }

    /**
     * @return the lastPasswordChange
     */
    public Date getLastPasswordChange() {
        return lastPasswordChange;
    }

    /**
     * @param lastPasswordChange the lastPasswordChange to set
     */
    public void setLastPasswordChange(Date lastPasswordChange) {
        this.lastPasswordChange = lastPasswordChange;
    }

    /**
     * @return the lastAccess
     */
    public Date getLastAccess() {
        return lastAccess;
    }

    /**
     * @param lastAccess the lastAccess to set
     */
    public void setLastAccess(Date lastAccess) {
        this.lastAccess = lastAccess;
    }

    /**
     * @return the recommendedMaterials
     */
    @XmlTransient
    public Set<Material> getRecommendedMaterials() {
        return recommendedMaterials;
    }

    /**
     * @param recommendedMaterials the recommendedMaterials to set
     */
    public void setRecommendedMaterials(Set<Material> recommendedMaterials) {
        this.recommendedMaterials = recommendedMaterials;
    }

    /**
     * @return the recommendedColors
     */
    @XmlTransient
    public Set<Color> getRecommendedColors() {
        return recommendedColors;
    }

    /**
     * @param recommendedColors the recommendedColors to set
     */
    public void setRecommendedColors(Set<Color> recommendedColors) {
        this.recommendedColors = recommendedColors;
    }
    
}