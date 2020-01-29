package litfitsserver.ejbs;

import java.util.List;
import javax.ejb.Local;
import litfitsserver.entities.Material;
import litfitsserver.exceptions.CreateException;
import litfitsserver.exceptions.DeleteException;
import litfitsserver.exceptions.ReadException;
import litfitsserver.exceptions.UpdateException;

/**
 * Interface for the MaterialEJB
 *
 * @author Carlos Mendez
 */
@Local
public interface LocalMaterialEJB {
    /**
     * Gets the amount of materials
     *
     * @return int
     * @throws litfitsserver.exceptions.ReadException
     */
    int countMaterials() throws ReadException;

    /**
     * Inserts a new material in the database
     *
     * @param material
     * @throws litfitsserver.exceptions.CreateException
     */
    void createMaterial(Material material) throws CreateException;

    /**
     * Edits a Material
     *
     * @param material
     * @throws litfitsserver.exceptions.UpdateException
     */
    void editMaterial(Material material) throws UpdateException;

    /**
     * Gets all the materials
     *
     * @return List
     * @throws litfitsserver.exceptions.ReadException
     */
    List<Material> findAllMaterials() throws ReadException;

    /**
     * Gets a Material by its name
     *
     * @param name
     * @return Material
     * @throws litfitsserver.exceptions.ReadException
     */
    Material findMaterial(String name) throws ReadException;

    /**
     * Deletes a material
     *
     * @param material
     * @throws litfitsserver.exceptions.ReadException
     * @throws litfitsserver.exceptions.DeleteException
     */
    void removeMaterial(Material material) throws ReadException, DeleteException;
}
