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
 * @author Carlos Mendez
 */
@Local
public interface LocalColorEJB {
    /**
     * Gets the amount of colors
     *
     * @return int
     * @throws litfitsserver.exceptions.ReadException
     */
    int countColors() throws ReadException;

    /**
     * Inserts a new color in the database
     *
     * @param color
     * @throws litfitsserver.exceptions.CreateException
     */
    void createColor(Color color) throws CreateException;

    /**
     * Edits a Color
     *
     * @param color
     * @throws litfitsserver.exceptions.UpdateException
     */
    void editColor(Color color) throws UpdateException;

    /**
     * Gets all the colors
     *
     * @return List
     * @throws litfitsserver.exceptions.ReadException
     */
    List<Color> findAllColors() throws ReadException;

    /**
     * Gets a Color by its name
     *
     * @param name
     * @return Color
     * @throws litfitsserver.exceptions.ReadException
     */
    Color findColor(String name) throws ReadException;

    /**
     * Deletes a color
     *
     * @param color
     * @throws litfitsserver.exceptions.ReadException
     * @throws litfitsserver.exceptions.DeleteException
     */
    void removeColor(Color color) throws ReadException, DeleteException;
}
