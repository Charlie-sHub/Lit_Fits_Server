package litfitsserver.ejbs;

import java.util.List;
import javax.ejb.Local;
import litfitsserver.entities.Material;

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
    int countMaterials();

    /**
     * Inserts a new material in the database
     *
     * @param material
     */
    void createMaterial(Material material);

    /**
     * Edits a Material
     *
     * @param material
     */
    void editMaterial(Material material);

    /**
     * Gets all the materials
     *
     * @return List
     */
    List<Material> findAllMaterials();

    /**
     * Gets a Material by its name
     *
     * @param name
     * @return Material
     */
    Material findMaterial(String name);

    /**
     * Deletes a material
     *
     * @param material
     */
    void removeMaterial(Material material);
}
