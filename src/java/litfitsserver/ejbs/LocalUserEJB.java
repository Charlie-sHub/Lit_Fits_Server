package litfitsserver.ejbs;

import java.util.List;
import javax.ejb.Local;
import litfitsserver.entities.Color;
import litfitsserver.entities.Garment;
import litfitsserver.entities.Material;
import litfitsserver.entities.User;

/**
 * Interface that contains all the method for User's EJB.
 * @author Asier Vila Dominguez
 */
@Local
interface LocalUserEJB {
    
    /**
     * Creates a new user.
     * @param user The user that will be created.
     */
    public void createUser (User user);
    
    /**
     * Modifies the data of the received user.
     * @param user The user that will be modified.
     */
    public void editUser (User user);
    
    /**
     * Deletes a user from the database.
     * @param user The user that will be deleted.
     */
    public void removeUser(User user);
    
    /**
     * Gets the data of the user with the received username.
     * @param username The username that will be used to filter.
     * @return The user data for that username.
     */
    public User findUser(String username);
    
    /**
     * Gets the data of all the users in the database.
     * @return A list with all the existing users.
     */
    public List<User> findAllUsers();
    
    /**
     * Gets the amount of users saved on the database.
     * @return The number of users on the database.
     */
    public int countUsers();
    
    /**
     * Gets all the garments that the user saved on his "closet".
     * @param username The username that will be used to filter.
     * @return A list with all the garments that the user has.
     */
    public List<Garment> getUserGarments (String username);
    
    /**
     * Gets all the colors that the user likes.
     * @param username The username that will be used to filter.
     * @return A list with all the colors that the user likes.
     */
    public List<Color> getUserLikedColors (String username);
    
    /**
     * Gets all the materials that the user likes.
     * @param username The username that will be used to filter.
     * @return A list with all the materials that the user likes.
     */
    public List<Material> getUserLikedMaterials (String username);
}
