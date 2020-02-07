package litfitsserver.ejbs;

import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.mail.MessagingException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.ws.rs.NotAuthorizedException;
import litfitsserver.entities.User;
import litfitsserver.exceptions.CreateException;
import litfitsserver.exceptions.DeleteException;
import litfitsserver.exceptions.ReadException;
import litfitsserver.exceptions.UpdateException;
import litfitsserver.miscellaneous.Decryptor;
import litfitsserver.miscellaneous.EmailService;
import org.apache.commons.lang3.RandomStringUtils;

/**
 * The EJB for the User on the app.
 * 
 * @author Asier
 */
@Stateless
public class UserEJB implements LocalUserEJB{
    /**
     * Injects the EJB of the Garment
     */
    @EJB
    private LocalCompanyEJB garmentEJB;
    @PersistenceContext(unitName = "Lit_Fits_ServerPU")
    private EntityManager entityManager;
    
    /**
     * Creates a new user.
     * 
     * @param user
     * @throws CreateException 
     */
    @Override
    public void createUser(User user) throws CreateException {
        try {
            user.setPassword(Decryptor.decypherRSA(user.getPassword()));
            if (userExists(user.getUsername())) {
                throw new Exception("Username already exists in the database");
            } else {
                user.setPassword(toHash(user.getPassword()));
                user.setLastAccess(new Date());
                user.setLastPasswordChange(new Date());
                entityManager.persist(user);
            }
        } catch (BadPaddingException ex) {
            throw new CreateException(ex.getMessage());
        } catch (Exception ex) {
            throw new CreateException(ex.getMessage());
        }
    }

    /**
     * Edits the data for the received user in the database.
     * 
     * @param user The user with the new data.
     * @throws UpdateException 
     */
    @Override
    public void editUser(User user) throws UpdateException {
        entityManager.merge(user);
        entityManager.flush();
    }

    /**
     * Removes a user from the database.
     * 
     * @param username The user that will be removed.
     * @throws ReadException
     * @throws DeleteException 
     */
    @Override
    public void removeUser(String username) throws ReadException, DeleteException {
        User removeUser = this.findUser(username);
        
        entityManager.remove(removeUser);
    }

    /**
     * Finds a user and returns it by filtering with the received username.
     * 
     * @param username The username that will be used to filter.
     * @return
     * @throws ReadException 
     */
    @Override
    public User findUser(String username) throws ReadException {
        return entityManager.find(User.class, username);
    }

    /**
     * Returns all the users in the database.
     * 
     * @return
     * @throws ReadException 
     */
    @Override
    public List<User> findAllUsers() throws ReadException {
        CriteriaQuery cq = entityManager.getCriteriaBuilder().createQuery();
        cq.select(cq.from(User.class));
        return (List<User>) entityManager.createQuery(cq).getResultList();
    }

    /**
     * Returns the number of users in the database.
     * 
     * @return The number of users in the database.
     * @throws ReadException 
     */
    @Override
    public int countUsers() throws ReadException {
        CriteriaQuery cq = entityManager.getCriteriaBuilder().createQuery();
        Root<User> rt = cq.from(User.class);
        cq.select(entityManager.getCriteriaBuilder().count(rt));
        Query q = entityManager.createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }
    
    /**
     * Returns a User with all the data by filtering with the received email.
     * 
     * @param email The email that will be used to filter.
     * @return The user that contains that email.
     * @throws ReadException 
     */
    @Override
    public User findUserByEmail(String email) throws ReadException {
        return (User) entityManager.createNamedQuery("findUserByEmail").setParameter("email", email).getResultList().get(0);
    }

    /**
     * Gets all the data of the received user if the username and password are corrects.
     * 
     * @param user The user that is trying to login.
     * @return
     * @throws ReadException
     * @throws NotAuthorizedException
     * @throws Exception 
     */
    @Override
    public User login (User user) throws ReadException, NotAuthorizedException, Exception {
        
        User userInDB;
        try {
            userInDB = findUser(user.getUsername());
            user.setPassword(Decryptor.decypherRSA(user.getPassword()));
            boolean rightPassword = userInDB.getPassword().equals(toHash(user.getPassword()));
            if (!rightPassword) {
                throw new NotAuthorizedException("Wrong password");
            }
            userInDB.setLastAccess(new Date());
            entityManager.merge(userInDB);
            entityManager.flush();
            
        } catch (InvalidKeySpecException | NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException ex) {
            throw new Exception(ex.getMessage());
        }
        return userInDB;
    }
    
    /**
     * Creates and returns the hash value of the received password.
     *
     * @param password
     * @return String hash of the password
     * @throws NoSuchAlgorithmException
     */
    private String toHash(String password) throws NoSuchAlgorithmException {
        
        String passwordHash;
        MessageDigest messageDigest;
        messageDigest = MessageDigest.getInstance("SHA");
        byte dataBytes[] = password.getBytes();
        messageDigest.update(dataBytes);
        byte hash[] = messageDigest.digest();
        passwordHash = new String(hash);
        
        return passwordHash;
    }

    /**
     * Reestablishes the password for the User with the received username.
     * 
     * @param username The username that will be used to filter.
     * @throws ReadException
     * @throws MessagingException
     * @throws Exception 
     */
    @Override
    public void reestablishPassword (String username) throws ReadException, MessagingException, Exception {
        User user = findUser(username);
        String generatedString = RandomStringUtils.randomAlphabetic(10);
        user.setPassword(toHash(generatedString));
        Decryptor decryptor = new Decryptor();
        EmailService emailService = newEmailService(decryptor);
        emailService.sendUserPasswordReestablishmentEmail(user);
        entityManager.merge(user);
        entityManager.flush();
    }
    
    /**
     * Creates a new email service object with the address and password from their respective files.
     *
     * @param decryptor
     * @return
     * @throws Exception
     */
    private EmailService newEmailService(Decryptor decryptor) throws Exception {
        String encodedPasswordPath = ResourceBundle.getBundle("litfitsserver.miscellaneous.paths").getString("serverLocalSystemAddress") + "/miscellaneous/EncodedPassword.dat";
        String encodedAddressPath = ResourceBundle.getBundle("litfitsserver.miscellaneous.paths").getString("serverLocalSystemAddress") + "/miscellaneous/EncodedAddress.dat";
        String emailAddress = decryptor.decypherAES("Nothin personnel kid", encodedAddressPath);
        String emailAddressPassword = decryptor.decypherAES("Nothin personnel kid", encodedPasswordPath);
        EmailService emailService = new EmailService(emailAddress, emailAddressPassword, null, null);
        return emailService;
    }
    
    /**
     * Checks if the username already exists into the database.
     * 
     * @param username The username that will be checked.
     * @return True if it exists, false if not.
     */
    private boolean userExists(String username){
        
        boolean exists = false;
        
        try {
            if (this.findUser(username).getUsername().equals(username)) {
                exists = true;
            }
        }catch (Exception ex) {
            exists = false;
        }
        
        return exists;   
    }
}
