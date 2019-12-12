package litfitsserver.ejbs;

import java.util.List;
import javax.ejb.Local;
import litfitsserver.entities.Color;
import litfitsserver.exceptions.CreateException;
import litfitsserver.exceptions.DeleteException;
import litfitsserver.exceptions.ReadException;
import litfitsserver.exceptions.UpdateException;

/**
 * Interface for the ColorEJB
 *
 * @author Carlos
 */
@Local
public interface LocalColorEJB {
    /**
     * Gets the amount of colors
     *
     * @return int
     */
    int countColors() throws ReadException;

    /**
     * Inserts a new color in the database
     *
     * @param color
     */
    void createColor(Color color) throws CreateException;

    /**
     * Edits a Color
     *
     * @param color
     */
    void editColor(Color color) throws UpdateException;

    /**
     * Gets all the colors
     *
     * @return List
     */
    List<Color> findAllColors() throws ReadException;

    /**
     * Gets a Color by its name
     *
     * @param name
     * @return Color
     */
    Color findColor(String name) throws ReadException;

    /**
     * Deletes a color
     *
     * @param color
     */
    void removeColor(Color color) throws ReadException, DeleteException;
}
