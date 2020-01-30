package litfitsserver.ejbs;

import java.security.InvalidKeyException;
import litfitsserver.miscellaneous.EmailService;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.List;
import java.util.Date;
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
import org.apache.commons.lang3.RandomStringUtils;

/**
 * The EJB for the User.
 *
 * @author Asier Vila Dominguez
 */
@Stateless
public class UserEJB implements LocalUserEJB {
    @PersistenceContext(unitName = "Lit_Fits_ServerPU")
    private EntityManager entityManager;
    @EJB
    LocalUserEJB userEJB;

    @Override
    public void createUser(User user) throws CreateException {

        try {
            user.setPassword(Decryptor.decypherRSA(user.getPassword()));
            user.setPassword(toHash(user.getPassword()));
            user.setLastAccess(new Date());
            user.setLastPasswordChange(new Date());
            entityManager.persist(user);

        } catch (BadPaddingException badPaddingException) {
            badPaddingException.printStackTrace();
            throw new CreateException(badPaddingException.getMessage());

        } catch (Exception exception) {
            exception.printStackTrace();
            throw new CreateException(exception.getMessage());
        }
    }

    @Override
    public User login(User user) throws ReadException, NotAuthorizedException, Exception {

        User userInDB;

        try {
            userInDB = findUser(user.getUsername());
            user.setPassword(Decryptor.decypherRSA(user.getPassword()));
            boolean rightPassword = userInDB.getPassword().equals(toHash(user.getPassword()));

            if (!rightPassword) {
                throw new NotAuthorizedException("The password does not match with the one into the database.");
            }

            userInDB.setLastAccess(new Date());
            entityManager.merge(userInDB);
            entityManager.flush();

        } catch (InvalidKeySpecException | NoSuchAlgorithmException | NoSuchPaddingException 
                | InvalidKeyException | IllegalBlockSizeException | BadPaddingException exception) {
            exception.printStackTrace();
            throw new Exception(exception.getMessage());
        }

        return userInDB;
    }

    @Override
    public void editUser(User user) throws UpdateException, NoSuchAlgorithmException, ReadException, MessagingException, Exception {
        
        Decryptor decryptor = new Decryptor();
        user.setPassword(Decryptor.decypherRSA(user.getPassword()));
        User userInDB = findUser(user.getUsername());
        boolean rightPassword = userInDB.getPassword().equals(toHash(user.getPassword()));
        
        if (!rightPassword) {
            EmailService emailService = newEmailService(decryptor);
            emailService.sendUserPasswordReestablishmentEmail(user);
            user.setLastPasswordChange(new Date());
        }

        user.setPassword(toHash(user.getPassword()));
        entityManager.merge(user);
        entityManager.flush();
    }

    @Override
    public void removeUser(User user) throws ReadException, DeleteException {
        entityManager.remove(entityManager.merge(user));
    }

    @Override
    public User findUser(String username) throws ReadException {
        return entityManager.find(User.class, username);
    }

    @Override
    public List<User> findAllUsers() throws ReadException {

        CriteriaQuery criteriaQuery = entityManager.getCriteriaBuilder().createQuery();
        criteriaQuery.select(criteriaQuery.from(User.class));

        return entityManager.createQuery(criteriaQuery).getResultList();
    }

    @Override
    public int countUsers() throws ReadException {

        CriteriaQuery criteriaQuery = entityManager.getCriteriaBuilder().createQuery();
        Root<User> rootUser = criteriaQuery.from(User.class);
        criteriaQuery.select(entityManager.getCriteriaBuilder().count(rootUser));
        Query query = entityManager.createQuery(criteriaQuery);

        return ((Long) query.getSingleResult()).intValue();
    }

    @Override
    public void reestablishPassword(String username) throws ReadException, MessagingException, Exception {
        
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
     * @return EmailService
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
     * Creates and returns the hash value of the given password
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
}