package litfitsserver.ejbs;

import java.security.NoSuchAlgorithmException;
import java.util.List;
import javax.ejb.Local;
import javax.mail.MessagingException;
import javax.ws.rs.NotAuthorizedException;
import litfitsserver.entities.User;
import litfitsserver.exceptions.CreateException;
import litfitsserver.exceptions.DeleteException;
import litfitsserver.exceptions.ReadException;
import litfitsserver.exceptions.UpdateException;

/**
 * Interface for the UserEJB
 *
 * @author Asier Vila Dominguez
 */
@Local
public interface LocalUserEJB {
    /**
     * The number of users that are stored into the database.
     *
     * @return int
     * @throws litfitsserver.exceptions.ReadException
     */
    int countUsers() throws ReadException;

    /**
     * Creates a new user in the database.
     *
     * @param user
     * @throws litfitsserver.exceptions.CreateException
     */
    void createUser(User user) throws CreateException;

    /**
     * Edit the data of the received user.
     *
     * @param user
     * @throws litfitsserver.exceptions.UpdateException
     * @throws java.security.NoSuchAlgorithmException
     * @throws litfitsserver.exceptions.ReadException
     * @throws javax.mail.MessagingException
     */
    void editUser(User user) throws UpdateException, NoSuchAlgorithmException, ReadException, MessagingException, Exception;

    /**
     * Get the data of all the users stored into the database.
     *
     * @return List
     * @throws litfitsserver.exceptions.ReadException
     */
    List<User> findAllUsers() throws ReadException;

    /**
     * Get the data of a user using the username.
     *
     * @param username
     * @return User
     * @throws litfitsserver.exceptions.ReadException
     */
    User findUser(String username) throws ReadException;

    /**
     * Deletes a user from the database
     *
     * @param user
     * @throws litfitsserver.exceptions.ReadException
     * @throws litfitsserver.exceptions.DeleteException
     */
    void removeUser(User user) throws ReadException, DeleteException;

    /**
     * Makes a login. Gets all the data of the user if the password and username match the database record.
     *
     * @param user
     * @return User
     * @throws NoSuchAlgorithmException
     * @throws ReadException
     * @throws NotAuthorizedException
     */
    User login(User user) throws ReadException, NotAuthorizedException, Exception;

    /**
     * Reestablishes the password for the received user. It creates a random new one.
     *
     * @param username
     * @throws ReadException
     * @throws javax.mail.MessagingException
     */
    void reestablishPassword(String username) throws ReadException, MessagingException, Exception;
}