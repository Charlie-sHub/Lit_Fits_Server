package litfitsserver.entities;

import java.io.File;
import java.io.Serializable;
import java.util.Objects;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import static javax.persistence.FetchType.EAGER;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Garment entity
 *
 * @author Charlie
 */
@NamedQueries({
    @NamedQuery(
            name = "findGarmentsByCompany",
            query = "SELECT gar FROM Garment gar WHERE gar.company.nif=:nif"
    )
    ,
    @NamedQuery(
            name = "findGarmentsByRequest",
            query = "SELECT gar FROM Garment gar WHERE gar.promotionRequest=:requested"
    )
    ,
    @NamedQuery(
            name = "findGarmentByBarcode",
            query = "SELECT gar FROM Garment gar WHERE gar.barcode=:barcode"
    )
    ,
    @NamedQuery(
            name = "findGarmentsPromoted",
            query = "SELECT gar FROM Garment gar WHERE gar.promoted=:promoted"
    )
})
@Entity
@Table(name = "garment", schema = "TestLitFitsDB")
@XmlRootElement
public class Garment implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    /**
     * Unique barcode identifier of the garment
     */
    @NotNull
    @Column(unique = true)
    private String barcode;
    /**
     * The person that designed the garment
     */
    @NotNull
    private String designer;
    /**
     * How much is worth
     */
    @NotNull
    private Double price;
    /**
     * The kind of situation is suited for
     */
    @NotNull
    private Mood mood;
    /**
     * Where it is worn
     */
    @NotNull
    private BodyPart bodyPart;
    /**
     * What kind of garment it is
     */
    @NotNull
    private GarmentType garmentType;
    /**
     * Indicates if it can be bought
     */
    @NotNull
    private boolean available;
    /**
     * Indicates if the company requested a promotion for this garment
     */
    @NotNull
    private boolean promotionRequest;
    /**
     * Indicates if the promotion request is accepted
     */
    @NotNull
    private boolean promoted;
    /**
     * Path in the database to the picture of the garment
     */
    @NotNull
    private String imagePath;
    /**
     * Company that sells the garment
     */
    @NotNull
    @ManyToOne
    @JoinColumn(name = "company")
    private Company company;
    /**
     * What colors are in the garment
     */
    @ManyToMany(fetch = EAGER)
    @JoinTable(name = "garment_colors", schema = "TestLitFitsDB")
    private Set<Color> colors;
    /**
     * What materials is the garment made out of
     */
    @ManyToMany(fetch = EAGER)
    @JoinTable(name = "garment_materials", schema = "TestLitFitsDB")
    private Set<Material> materials;
    /**
     * The picture of the garment
     */
    @Transient
    private File picture;

    /**
     * Empty constructor
     */
    public Garment() {
    }

    /**
     * Full constructor
     *
     * @param id
     * @param barcode
     * @param designer
     * @param price
     * @param mood
     * @param bodyPart
     * @param garmentType
     * @param available
     * @param promotionRequest
     * @param promoted
     * @param imagePath
     * @param company
     * @param colors
     * @param materials
     */
    public Garment(long id, String barcode, String designer, Double price, Mood mood, BodyPart bodyPart, GarmentType garmentType, boolean available, boolean promotionRequest, boolean promoted, String imagePath, Company company, Set<Color> colors, Set<Material> materials) {
        this.id = id;
        this.barcode = barcode;
        this.designer = designer;
        this.price = price;
        this.mood = mood;
        this.bodyPart = bodyPart;
        this.garmentType = garmentType;
        this.available = available;
        this.promotionRequest = promotionRequest;
        this.promoted = promoted;
        this.imagePath = imagePath;
        this.company = company;
        this.colors = colors;
        this.materials = materials;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getDesigner() {
        return designer;
    }

    public void setDesigner(String designer) {
        this.designer = designer;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Mood getMood() {
        return mood;
    }

    public void setMood(Mood mood) {
        this.mood = mood;
    }

    public BodyPart getBodyPart() {
        return bodyPart;
    }

    public void setBodyPart(BodyPart bodyPart) {
        this.bodyPart = bodyPart;
    }

    public GarmentType getGarmentType() {
        return garmentType;
    }

    public void setGarmentType(GarmentType garmentType) {
        this.garmentType = garmentType;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public boolean isPromotionRequest() {
        return promotionRequest;
    }

    public void setPromotionRequest(boolean promotionRequest) {
        this.promotionRequest = promotionRequest;
    }

    public boolean isPromoted() {
        return promoted;
    }

    public void setPromoted(boolean promoted) {
        this.promoted = promoted;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public Set<Color> getColors() {
        return colors;
    }

    public void setColors(Set<Color> colors) {
        this.colors = colors;
    }

    public Set<Material> getMaterials() {
        return materials;
    }

    public void setMaterials(Set<Material> materials) {
        this.materials = materials;
    }

    public File getPicture() {
        return picture;
    }

    public void setPicture(File picture) {
        this.picture = picture;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 29 * hash + (int) (this.id ^ (this.id >>> 32));
        hash = 29 * hash + Objects.hashCode(this.barcode);
        hash = 29 * hash + Objects.hashCode(this.designer);
        hash = 29 * hash + Objects.hashCode(this.price);
        hash = 29 * hash + Objects.hashCode(this.mood);
        hash = 29 * hash + Objects.hashCode(this.bodyPart);
        hash = 29 * hash + Objects.hashCode(this.garmentType);
        hash = 29 * hash + (this.available ? 1 : 0);
        hash = 29 * hash + (this.promotionRequest ? 1 : 0);
        hash = 29 * hash + (this.promoted ? 1 : 0);
        hash = 29 * hash + Objects.hashCode(this.imagePath);
        hash = 29 * hash + Objects.hashCode(this.company);
        hash = 29 * hash + Objects.hashCode(this.colors);
        hash = 29 * hash + Objects.hashCode(this.materials);
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
        final Garment other = (Garment) obj;
        if (this.id != other.id) {
            return false;
        }
        if (this.available != other.available) {
            return false;
        }
        if (this.promotionRequest != other.promotionRequest) {
            return false;
        }
        if (this.promoted != other.promoted) {
            return false;
        }
        if (!Objects.equals(this.barcode, other.barcode)) {
            return false;
        }
        if (!Objects.equals(this.designer, other.designer)) {
            return false;
        }
        if (!Objects.equals(this.imagePath, other.imagePath)) {
            return false;
        }
        if (!Objects.equals(this.price, other.price)) {
            return false;
        }
        if (this.mood != other.mood) {
            return false;
        }
        if (this.bodyPart != other.bodyPart) {
            return false;
        }
        if (this.garmentType != other.garmentType) {
            return false;
        }
        if (!Objects.equals(this.company, other.company)) {
            return false;
        }
        if (!Objects.equals(this.colors, other.colors)) {
            return false;
        }
        if (!Objects.equals(this.materials, other.materials)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Garment{" + "id=" + id + ", barcode=" + barcode + ", designer=" + designer + ", price=" + price + ", mood=" + mood + ", bodyPart=" + bodyPart + ", garmentType=" + garmentType + ", available=" + available + ", promotionRequest=" + promotionRequest + ", promoted=" + promoted + ", imagePath=" + imagePath + ", company=" + company + ", colors=" + colors + ", materials=" + materials + '}';
    }
}
