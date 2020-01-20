package litfitsserver.ejbs;

import java.util.List;
import java.util.Set;
import javax.ejb.Local;
import javax.mail.MessagingException;
import javax.ws.rs.NotAuthorizedException;
import litfitsserver.entities.Company;
import litfitsserver.entities.User;
import litfitsserver.exceptions.CreateException;
import litfitsserver.exceptions.DeleteException;
import litfitsserver.exceptions.ReadException;
import litfitsserver.exceptions.UpdateException;

/**
 * Interface that contains all the method for User's EJB.
 * 
 * @author Asier Vila Dominguez
 */
@Local
public interface LocalUserEJB {
    
    /**
     * Creates a new user.
     * @param user The user that will be created.
     * @throws CreateException
     */
    public void createUser (User user) throws CreateException;
    
    /**
     * Modifies the data of the received user.
     * @param user The user that will be modified.
     * @throws UpdateException
     */
    public void editUser (User user) throws UpdateException;
    
    /**
     * Deletes a user from the database.
     * @param user The user that will be deleted.
     * @throws ReadException
     * @throws DeleteException
     */
    public void removeUser(User user) throws ReadException, DeleteException;
    
    /**
     * Gets the data of the user with the received username.
     * @param username The username that will be used to filter.
     * @return The user data for that username.
     * @throws ReadException
     */
    public User findUser(String username) throws ReadException;
    
    /**
     * Gets the data of all the users in the database.
     * @return A list with all the existing users.
     * @throws ReadException
     */
    public List<User> findAllUsers() throws ReadException;
    
    /**
     * Gets the amount of users saved on the database.
     * @return The number of users on the database.
     * @throws ReadException
     */
    public int countUsers() throws ReadException;
    
    /**
     * Gets the user that has the received email linked to its account.
     * @param email The email that will be used to filter.
     * @return A User with all the data.
     * @throws ReadException
     */
    public User findUserByEmail (String email) throws ReadException;
    
    /**
     * Takes the data from the sent User and returns it.
     *
     * @param user
     * @return Company
     * @throws NoSuchAlgorithmException
     * @throws ReadException
     * @throws NotAuthorizedException
     */
    public User login(User user) throws ReadException, NotAuthorizedException, Exception;

    /**
     * Reestablish the password for the User with the received username.
     *
     * @param username
     * @throws ReadException
     * @throws javax.mail.MessagingException
     */
    public void reestablishPassword(String username) throws ReadException, MessagingException, Exception;
}
