package litfitsserver.ejbs;

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
 * @author Carlos Mendez
 */
@Local
public interface LocalGarmentEJB {
    /**
     * Counts the amount of garments
     *
     * @return int
     * @throws litfitsserver.exceptions.ReadException
     */
    int countGarments() throws ReadException;

    /**
     * Inserts a Garment into the database
     *
     * @param garment
     * @throws litfitsserver.exceptions.CreateException
     */
    void createGarment(Garment garment) throws CreateException;

    /**
     * Edits a Garment
     *
     * @param garment
     * @throws litfitsserver.exceptions.UpdateException
     */
    void editGarment(Garment garment) throws UpdateException, ReadException;

    /**
     * Gets all the garments
     *
     * @return
     * @throws litfitsserver.exceptions.ReadException
     */
    List<Garment> findAllGarments() throws ReadException;

    /**
     * Gets a garment by its id
     *
     * @param id
     * @return Garment
     * @throws litfitsserver.exceptions.ReadException
     */
    Garment findGarment(Long id) throws ReadException;

    /**
     * Deletes a garment
     *
     * @param garment
     * @throws litfitsserver.exceptions.ReadException
     * @throws litfitsserver.exceptions.DeleteException
     */
    void removeGarment(Garment garment) throws ReadException, DeleteException;

    /**
     * Gets the garments of a Company
     *
     * @param nif
     * @return List
     * @throws litfitsserver.exceptions.ReadException
     */
    List<Garment> findGarmentsByCompany(String nif) throws ReadException;

    /**
     * Gets the garments of which a promotion has been requested
     *
     * @param requested
     * @return List
     * @throws litfitsserver.exceptions.ReadException
     */
    List<Garment> findGarmentsByRequest(Boolean requested) throws ReadException;

    /**
     * Gets a garment by its barcode
     *
     * @param barcode
     * @return Garment
     * @throws litfitsserver.exceptions.ReadException
     */
    Garment findGarmentByBarcode(String barcode) throws ReadException;

    /**
     * Gets the garments currently being promoted
     *
     * @param promoted
     * @return List
     * @throws litfitsserver.exceptions.ReadException
     */
    List<Garment> findGarmentsPromoted(Boolean promoted) throws ReadException;

    /**
     * Gets the picture of the garment
     *
     * @param id
     * @return byte[]
     * @throws java.io.IOException
     * @throws litfitsserver.exceptions.ReadException
     */
    byte[] getImage(Long id) throws IOException, ReadException;
}
