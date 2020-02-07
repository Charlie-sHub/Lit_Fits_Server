/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package litfitsserver.entities;

import java.io.Serializable;
import java.util.List;
import static javax.persistence.CascadeType.ALL;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Ander Rodriguez
 */
@NamedQueries({
    @NamedQuery(
            name = "findByUsername",
            query = "SELECT par FROM Particular par WHERE par.username=:username"
    ),
    @NamedQuery(
            name = "findByName",
            query = "SELECT par FROM Particular par WHERE par.fullName=:fullName"
    ),
    @NamedQuery(
            name = "findByFreelance",
            query = "SELECT par FROM Particular par WHERE par.freelance=:freelance"
    ),
})
@Entity
@Table(name="particular", schema = "litfitsdb")
@XmlRootElement
public class Particular implements Serializable {
    private static long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @NotNull
    private String username;
    @NotNull
    private String password;
    @NotNull
    private String fullName;
    private String phoneNumber;
    private String email;
    private boolean freelance;
    
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "company", cascade = ALL)
    private List<Garment> garments;
    
    public Particular(){
    }

    public Particular(long id, String username, String password, String fullName, String phoneNumber, String email, boolean freelance, List<Garment> garments) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.freelance = freelance;
        this.garments = garments;
    }
    
    


    /**
     * @return the id
     */
    public long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(long id) {
        this.id = id;
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
     * @return the garments
     */
    public List<Garment> getGarments() {
        return garments;
    }

    /**
     * @param garments the garments to set
     */
    public void setGarments(List<Garment> garments) {
        this.garments = garments;
    }

    /**
     * @return the freelance
     */
    public boolean isFreelance() {
        return freelance;
    }

    /**
     * @param freelance the freelance to set
     */
    public void setFreelance(boolean freelance) {
        this.freelance = freelance;
    }
    
    
}
