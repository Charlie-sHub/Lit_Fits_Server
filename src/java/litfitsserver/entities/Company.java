package litfitsserver.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * Company class, has all the data required for the Use Cases of company type actors
 *
 * @author Charlie
 */
@NamedQueries({
    @NamedQuery(
            name = "findCompanyByNif",
            query = "SELECT com FROM Company com WHERE com.nif=:nif"
    )
    ,
        @NamedQuery(
            name = "companyExists",
            query = "SELECT count(*) FROM Company com WHERE com.nif=:nif"
    )
})
@Entity
@Table(name = "company", schema = "litfitsdb")
@XmlRootElement
public class Company implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    /**
     * Unique nif identifier of the garment
     */
    @NotNull
    private String nif;
    /**
     * Password required for the company to log in
     */
    @NotNull
    private String password;
    /**
     * Name of the company
     */
    @NotNull
    private String fullName;
    /**
     * Phone number to contact the company
     */
    private String phoneNumber;
    /**
     * Email to contact the company
     */
    private String email;
    /**
     * When the company last accessed the app
     */
    @Temporal(TemporalType.DATE)
    private Date lastAccess;
    /**
     * When the company last changed its password
     */
    @Temporal(TemporalType.DATE)
    private Date lastPasswordChange;
    /**
     * List of garments "owned" by the company.
     */
    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL)
    private List<Garment> garments;

    /**
     * Empty constructor
     */
    public Company() {
    }

    /**
     * Full Constructor
     *
     * @param id
     * @param nif
     * @param password
     * @param fullName
     * @param phoneNumber
     * @param email
     * @param lastAccess
     * @param lastPasswordChange
     * @param garments
     */
    public Company(long id, String nif, String password, String fullName, String phoneNumber, String email, Date lastAccess, Date lastPasswordChange, List<Garment> garments) {
        this.id = id;
        this.nif = nif;
        this.password = password;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.lastAccess = lastAccess;
        this.lastPasswordChange = lastPasswordChange;
        this.garments = garments;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public Date getLastAccess() {
        return lastAccess;
    }

    public void setLastAccess(Date lastAccess) {
        this.lastAccess = lastAccess;
    }

    public Date getLastPasswordChange() {
        return lastPasswordChange;
    }

    public void setLastPasswordChange(Date lastPasswordChange) {
        this.lastPasswordChange = lastPasswordChange;
    }

    @XmlTransient
    public List<Garment> getGarments() {
        return garments;
    }

    public void setGarments(List<Garment> garments) {
        this.garments = garments;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 71 * hash + (int) (this.id ^ (this.id >>> 32));
        hash = 71 * hash + Objects.hashCode(this.nif);
        hash = 71 * hash + Objects.hashCode(this.password);
        hash = 71 * hash + Objects.hashCode(this.fullName);
        hash = 71 * hash + Objects.hashCode(this.phoneNumber);
        hash = 71 * hash + Objects.hashCode(this.email);
        hash = 71 * hash + Objects.hashCode(this.lastAccess);
        hash = 71 * hash + Objects.hashCode(this.lastPasswordChange);
        hash = 71 * hash + Objects.hashCode(this.garments);
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
        if (this.id != other.id) {
            return false;
        }
        if (!Objects.equals(this.nif, other.nif)) {
            return false;
        }
        return Objects.equals(this.garments, other.garments);
    }

    @Override
    public String toString() {
        return "Company{" + "id=" + id + ", nif=" + nif + ", password=" + password + ", fullName=" + fullName + ", phoneNumber=" + phoneNumber + ", email=" + email + ", lastAccess=" + lastAccess + ", lastPasswordChange=" + lastPasswordChange + ", garments=" + garments + '}';
    }
}
