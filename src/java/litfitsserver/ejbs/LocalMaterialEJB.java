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
 * @author Carlos
 */
@Local
public interface LocalMaterialEJB {
    /**
     * Gets the amount of materials
     *
     * @return int
     */
    int countMaterials() throws ReadException;

    /**
     * Inserts a new material in the database
     *
     * @param material
     */
    void createMaterial(Material material) throws CreateException;

    /**
     * Edits a Material
     *
     * @param material
     */
    void editMaterial(Material material) throws UpdateException;

    /**
     * Gets all the materials
     *
     * @return List
     */
    List<Material> findAllMaterials() throws ReadException;

    /**
     * Gets a Material by its name
     *
     * @param name
     * @return Material
     */
    Material findMaterial(String name) throws ReadException;

    /**
     * Deletes a material
     *
     * @param material
     */
    void removeMaterial(Material material) throws ReadException, DeleteException;
}
