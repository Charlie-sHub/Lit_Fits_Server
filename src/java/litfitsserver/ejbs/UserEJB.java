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

/**
 * The EJB for the User on the app
 * 
 * @author Asier
 */
@Stateless
public class UserEJB implements LocalUserEJB{

    @PersistenceContext(unitName = "Lit_Fits_ServerPU")
    private EntityManager em;
    
    /**
     * Creates a new user.
     * 
     * @param user
     * @throws CreateException 
     */
    @Override
    public void createUser(User user) throws CreateException {
        em.persist(user);
    }

    /**
     * Edits the data for the received user in the database.
     * 
     * @param user The user with the new data.
     * @throws UpdateException 
     */
    @Override
    public void editUser(User user) throws UpdateException {
        em.merge(user);
        em.flush();
    }

    /**
     * Removes a user from the database.
     * 
     * @param user The user that will be removed.
     * @throws ReadException
     * @throws DeleteException 
     */
    @Override
    public void removeUser(User user) throws ReadException, DeleteException {
        em.remove(em.merge(user));
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
        return em.find(User.class, username);
    }

    /**
     * Returns all the users in the database.
     * 
     * @return
     * @throws ReadException 
     */
    @Override
    public List<User> findAllUsers() throws ReadException {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(User.class));
        return (List<User>) em.createQuery(cq).getResultList();
    }

    /**
     * Returns the number of users in the database.
     * 
     * @return The number of users in the database.
     * @throws ReadException 
     */
    @Override
    public int countUsers() throws ReadException {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        Root<User> rt = cq.from(User.class);
        cq.select(em.getCriteriaBuilder().count(rt));
        Query q = em.createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }
    
    /**
     * Returns a User with all the data by filtering with the received email.
     * 
     * @param email The email that will be used to filter.
     * @return
     * @throws ReadException 
     */
    @Override
    public User findUserByEmail(String email) throws ReadException {
        return (User) em.createNamedQuery("findUserByEmail").setParameter("email", email).getResultList();
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
        
        User receivedUser = user;
        User userInDB = this.findUser(user.getUsername());
        String auxPassword = user.getPassword();
        
        try {
            Decryptor decryptor = new Decryptor();
            receivedUser.setPassword(decryptor.decypherRSA(receivedUser.getPassword()));
            boolean rightPassword = userInDB.getPassword().equals(toHash(receivedUser.getPassword()));
            
            if (!rightPassword) {
                throw new NotAuthorizedException("Wrong password");
            }
            userInDB.setLastAccess(new Date());
            em.merge(userInDB);
            //userInDB.setGarments(garmentEJB.findGarmentsByUser(userInDB.getUsername()));
            
        } catch (InvalidKeySpecException | NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException ex) {
            throw new Exception(ex.getMessage());
        }
        
        // To keep the same "password" that was sent by the client, meaning the encrypted one
        userInDB.setPassword(auxPassword);
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
        
        MessageDigest messageDigest = MessageDigest.getInstance("SHA");
        
        byte dataBytes[] = password.getBytes();
        
        messageDigest.update(dataBytes);
        
        byte hash[] = messageDigest.digest();
        
        String passwordHash = new String(hash);
        
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
        user.setPassword(generatedString);
        Decryptor decryptor = new Decryptor();
        EmailService emailService = this.newEmailService(decryptor);
        emailService.sendUserPasswordReestablishmentEmail(user);
        em.merge(user);
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
}
