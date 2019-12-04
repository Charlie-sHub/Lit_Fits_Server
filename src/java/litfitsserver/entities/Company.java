package litfitsserver.entities;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Objects;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

/**
 * Company class, has all the data required for the Use Cases of company type
 * actors
 *
 * @author Charlie
 */
@Entity
@Table(name = "company", schema = "Lit_Fits_DB")
public class Company implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * Unique identifier for the company it's NIF
     */
    @Id
    protected String nif;
    /**
     * Passqord required for the company to log in
     */
    @NotNull
    protected String password;
    /**
     * Name of the company
     */
    @NotNull
    protected String fullName;
    /**
     * Phone number to contact the company
     */
    //Add pattern
    protected String phoneNumber;
    /**
     * Email to contact the company
     */
    //Add pattern
    protected String email;
    /**
     * When the company last accessed the app
     */
    @Temporal(TemporalType.TIMESTAMP)
    protected Timestamp lastAccess;
    /**
     * When the company last changed its password
     */
    @Temporal(TemporalType.TIMESTAMP)
    protected Timestamp lastPasswordChange;
    /**
     * List of garments "owned" by the company.
     */
    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL)
    protected Set<Garment> garments;

    public Company() {
    }

    public Company(String nif, String password, String fullName, String phoneNumber, String email, Timestamp lastAccess, Timestamp lastPasswordChange, Set<Garment> garments) {
        this.nif = nif;
        this.password = password;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.lastAccess = lastAccess;
        this.lastPasswordChange = lastPasswordChange;
        this.garments = garments;
    }

    public String getNif() {
        return nif;
    }

    public void setNif(String nif) {
        this.nif = nif;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Timestamp getLastAccess() {
        return lastAccess;
    }

    public void setLastAccess(Timestamp lastAccess) {
        this.lastAccess = lastAccess;
    }

    public Timestamp getLastPasswordChange() {
        return lastPasswordChange;
    }

    public void setLastPasswordChange(Timestamp lastPasswordChange) {
        this.lastPasswordChange = lastPasswordChange;
    }

    public Set<Garment> getGarments() {
        return garments;
    }

    public void setGarments(Set<Garment> garments) {
        this.garments = garments;
    }

    @Override
    public String toString() {
        return "Company{" + "nif=" + nif + ", password=" + password + ", fullName=" + fullName + ", phoneNumber=" + phoneNumber + ", email=" + email + ", lastAccess=" + lastAccess + ", lastPasswordChange=" + lastPasswordChange + ", garments=" + garments + '}';
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 97 * hash + Objects.hashCode(this.nif);
        hash = 97 * hash + Objects.hashCode(this.password);
        hash = 97 * hash + Objects.hashCode(this.fullName);
        hash = 97 * hash + Objects.hashCode(this.phoneNumber);
        hash = 97 * hash + Objects.hashCode(this.email);
        hash = 97 * hash + Objects.hashCode(this.lastAccess);
        hash = 97 * hash + Objects.hashCode(this.lastPasswordChange);
        hash = 97 * hash + Objects.hashCode(this.garments);
        return hash;
    }

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
        final Company other = (Company) obj;
        if (!Objects.equals(this.nif, other.nif)) {
            return false;
        }
        if (!Objects.equals(this.password, other.password)) {
            return false;
        }
        if (!Objects.equals(this.fullName, other.fullName)) {
            return false;
        }
        if (!Objects.equals(this.phoneNumber, other.phoneNumber)) {
            return false;
        }
        if (!Objects.equals(this.email, other.email)) {
            return false;
        }
        if (!Objects.equals(this.lastAccess, other.lastAccess)) {
            return false;
        }
        if (!Objects.equals(this.lastPasswordChange, other.lastPasswordChange)) {
            return false;
        }
        if (!Objects.equals(this.garments, other.garments)) {
            return false;
        }
        return true;
    }
}
