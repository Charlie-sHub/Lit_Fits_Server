package litfitsserver.ejbs;

import java.io.File;
import java.io.IOException;
import java.util.List;
import javax.ejb.Local;
import litfitsserver.entities.Garment;
import litfitsserver.exceptions.CreateException;
import litfitsserver.exceptions.DeleteException;
import litfitsserver.exceptions.ReadException;
import litfitsserver.exceptions.UpdateException;

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
    int countGarments() throws ReadException;

    /**
     * Inserts a Garment into the database
     *
     * @param garment
     */
    void createGarment(Garment garment) throws CreateException;

    /**
     * Edits a Garment
     *
     * @param garment
     */
    void editGarment(Garment garment) throws UpdateException;

    /**
     * Gets all the garments
     *
     * @return
     */
    List<Garment> findAllGarments() throws ReadException;

    /**
     * Gets a garment by its id
     *
     * @param id
     * @return Garment
     */
    Garment findGarment(Long id) throws ReadException;

    /**
     * Deletes a garment
     *
     * @param garment
     */
    void removeGarment(Garment garment) throws ReadException, DeleteException;

    /**
     * Gets the garments of a Company
     *
     * @param nif
     * @return List
     */
    public List<Garment> findGarmentsByCompany(String nif) throws ReadException;

    /**
     * Gets the garments of which a promotion has been requested
     *
     * @param requested
     * @return List
     */
    public List<Garment> findGarmentsByRequest(Boolean requested) throws ReadException;

    /**
     * Gets a garment by its barcode
     *
     * @param barcode
     * @return Garment
     */
    public Garment findGarmentByBarcode(String barcode) throws ReadException;

    /**
     * Gets the garments currently being promoted
     *
     * @param promoted
     * @return List
     */
    public List<Garment> findGarmentsPromoted(Boolean promoted) throws ReadException;

    /**
     * Gets the picture of the garment
     *
     * @param id
     * @return byte[]
     */
    public File getImage(Long id) throws IOException, ReadException;
}
