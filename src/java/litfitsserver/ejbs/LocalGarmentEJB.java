package litfitsserver.ejbs;

import java.util.List;
import javax.ejb.Local;
import litfitsserver.entities.Garment;

/**
 * Interface for the GarmentEJB
 *
 * @author Carlos
 */
@Local
public interface LocalGarmentEJB {
    /**
     * Counts the amount of garments
     *
     * @return int
     */
    int countGarments();

    /**
     * Inserts a Garment into the database
     *
     * @param garment
     */
    void createGarment(Garment garment);

    /**
     * Edits a Garment
     *
     * @param garment
     */
    void editGarment(Garment garment);

    /**
     * Gets all the garments
     *
     * @return
     */
    List<Garment> findAllGarments();

    /**
     * Gets a garment by its id
     *
     * @param id
     * @return Garment
     */
    Garment findGarment(Long id);

    /**
     * Deletes a garment
     *
     * @param garment
     */
    void removeGarment(Garment garment);

    /**
     * Gets the garments of a Company
     *
     * @param nif
     * @return List
     */
    public List<Garment> findGarmentsByCompany(String nif);

    /**
     * Gets the garments of which a promotion has been requested
     *
     * @param requested
     * @return List
     */
    public List<Garment> findGarmentsByRequest(Boolean requested);

    /**
     * Gets a garment by its barcode
     *
     * @param barcode
     * @return Garment
     */
    public Garment findGarmentByBarcode(String barcode);

    /**
     * Gets the garments currently being promoted
     *
     * @param promoted
     * @return List
     */
    public List<Garment> findGarmentsPromoted(Boolean promoted);
}
