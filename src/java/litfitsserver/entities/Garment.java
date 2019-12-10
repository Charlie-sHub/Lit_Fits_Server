package litfitsserver.entities;

import java.io.Serializable;
import java.util.Objects;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * Garment entity
 *
 * @author Charlie
 */
@Entity
@Table(name = "garment", schema = "Lit_Fits_DB")
@XmlRootElement
public class Garment implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * Unique barcode identifier of the garment
     */
    @Id
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
    private GarmentType garmenteType;
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
    @JoinColumn(name = "nif")
    private Company company;
    /**
     * What colors are in the garment
     */
    @NotNull
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "garment_colors", schema = "Lit_Fits_DB")
    private Set<Color> colors;
    /**
     * What materials is the garment made out of
     */
    @NotNull
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "garment_materials", schema = "Lit_Fits_DB")
    private Set<Material> materials;

    public Garment() {
    }

    public Garment(String barcode, String designer, Double price, Mood mood, BodyPart bodyPart, GarmentType garmenteType, boolean available, boolean promotionRequest, boolean promoted, String imagePath, Company company, Set<Color> colors, Set<Material> materials) {
        this.barcode = barcode;
        this.designer = designer;
        this.price = price;
        this.mood = mood;
        this.bodyPart = bodyPart;
        this.garmenteType = garmenteType;
        this.available = available;
        this.promotionRequest = promotionRequest;
        this.promoted = promoted;
        this.imagePath = imagePath;
        this.company = company;
        this.colors = colors;
        this.materials = materials;
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

    public GarmentType getGarmenteType() {
        return garmenteType;
    }

    public void setGarmenteType(GarmentType garmenteType) {
        this.garmenteType = garmenteType;
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

    @XmlTransient
    public Set<Color> getColors() {
        return colors;
    }

    public void setColors(Set<Color> colors) {
        this.colors = colors;
    }

    @XmlTransient
    public Set<Material> getMaterials() {
        return materials;
    }

    public void setMaterials(Set<Material> materials) {
        this.materials = materials;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 41 * hash + Objects.hashCode(this.barcode);
        hash = 41 * hash + Objects.hashCode(this.designer);
        hash = 41 * hash + Objects.hashCode(this.price);
        hash = 41 * hash + Objects.hashCode(this.mood);
        hash = 41 * hash + Objects.hashCode(this.bodyPart);
        hash = 41 * hash + Objects.hashCode(this.garmenteType);
        hash = 41 * hash + (this.available ? 1 : 0);
        hash = 41 * hash + (this.promotionRequest ? 1 : 0);
        hash = 41 * hash + (this.promoted ? 1 : 0);
        hash = 41 * hash + Objects.hashCode(this.imagePath);
        hash = 41 * hash + Objects.hashCode(this.company);
        hash = 41 * hash + Objects.hashCode(this.colors);
        hash = 41 * hash + Objects.hashCode(this.materials);
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
        if (this.garmenteType != other.garmenteType) {
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
        return "Garment{" + "barcode=" + barcode + ", designer=" + designer + ", price=" + price + ", mood=" + mood + ", bodyPart=" + bodyPart + ", garmenteType=" + garmenteType + ", available=" + available + ", promotionRequest=" + promotionRequest + ", promoted=" + promoted + ", imagePath=" + imagePath + ", company=" + company + ", colors=" + colors + ", materials=" + materials + '}';
    }
}
