package litfitsserver.ejbs;

import java.util.List;
import javax.ejb.Local;
import litfitsserver.entities.Color;
import litfitsserver.exceptions.CreateException;

/**
 * Interface for the ColorEJB
 * @author Carlos
 */
@Local
public interface LocalColorEJB {
    /**
     * Gets the amount of colors
     *
     * @return int
     */
    int countColors() throws Exception;

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
    void editColor(Color color);

    /**
     * Gets all the colors
     *
     * @return List
     */
    List<Color> findAllColors();

    /**
     * Gets a Color by its name
     *
     * @param name
     * @return Color
     */
    Color findColor(String name);

    /**
     * Deletes a color
     *
     * @param color
     */
    void removeColor(Color color);
}
