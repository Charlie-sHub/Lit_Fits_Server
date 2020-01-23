/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package litfitsserver.ejbs;

import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import javax.crypto.NoSuchPaddingException;
import javax.ejb.CreateException;
import javax.ejb.Stateless;
import javax.mail.MessagingException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaQuery;
import javax.ws.rs.NotAuthorizedException;
import litfitsserver.entities.Color;
import litfitsserver.entities.FashionExpert;
import litfitsserver.entities.Material;
import litfitsserver.exceptions.DeleteException;
import litfitsserver.exceptions.ReadException;
import litfitsserver.exceptions.UpdateException;
import litfitsserver.miscellaneous.Decryptor;
import litfitsserver.miscellaneous.EmailService;
import org.apache.commons.lang3.RandomStringUtils;

/**
 *  EJB for Experts
 * @author Ander
 */

@Stateless
public class EJBExpertManager implements LocalExpertEJB {

    @PersistenceContext(unitName = "Lit_Fits_ServerPU")
    private EntityManager em;
    
    /**
     * Method to create an exert
     * @param expert
     * @throws CreateException 
     */
    @Override
    public void createExpert (FashionExpert expert) throws CreateException{
        Decryptor decryptor = new Decryptor();
        try{
            expert.setPassword(decryptor.decypherRSA(expert.getPassword()));
            if(expertExists(expert.getUsername())){
                throw new Exception("Username already exists in the database");
            }else{
                expert.setPassword(toHash(expert.getPassword()));
                expert.setLastAccess(new Date());
                expert.setLastPasswordChange(new Date());
                em.persist(expert);
            }
        }catch(Exception e){
            throw new CreateException(e.getMessage());
        }
    }
    /**
     * Method to modify the expert data
     * 
     * @param expert
     * @throws UpdateException
     * @throws NoSuchAlgorithmException
     * @throws ReadException
     * @throws MessagingException
     * @throws Exception 
     */
    @Override
    public void modifyExpert(FashionExpert expert) throws UpdateException, NoSuchAlgorithmException, ReadException, MessagingException, Exception{
        Decryptor decryptor = new Decryptor();
        expert.setPassword(decryptor.decypherRSA(expert.getPassword()));
        FashionExpert expertInDB = findExpertByUsername(expert.getUsername());
        boolean correctPassword = expertInDB.getPassword().equals(toHash(expert.getPassword()));
        
        if(!correctPassword){
            expert.setLastPasswordChange(new Date());
            String password = expert.getPassword();
            expert.setPassword(toHash(password));
        }
        
        em.merge(expert);
        em.flush();
    }
    
    /**
     * Method to delete the expert
     * @param expert
     * @throws ReadException
     * @throws DeleteException 
     */
    @Override
    public void deleteExpert(FashionExpert expert) throws ReadException, DeleteException {
        em.remove(em.merge(expert));
    }
    
    /**
     * Method to find the expert by primary key
     * @param id
     * @return
     * @throws ReadException 
     */
    @Override
    public FashionExpert findExpert(Long id) throws ReadException {
        return em.find(FashionExpert.class, id);
    }
    
    /**
     * Method to find expert by username
     * @param username
     * @return
     * @throws ReadException 
     */
    @Override
    public FashionExpert findExpertByUsername(String username) throws ReadException{
        return (FashionExpert) em.createNamedQuery("findExpertByUsername").setParameter("username", username).getSingleResult();
    }
    
    /**
     * Method to list all the experts
     * @return list of experts
     */
    @Override
    public List<FashionExpert> findAllExperts() {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(FashionExpert.class));
        return em.createQuery(cq).getResultList();
    }
    
    /**
     * Method to list all the colors that are recommended
     * @return list of colors
     */
    @Override
    public List<Color> getRecommendedColors() throws ReadException {
        List<FashionExpert> experts;
        List<Color> colors = null;
        experts = findAllExperts();
        experts.forEach((expert) -> {
            List<Color> color = expert.getRecommendedColors();
            color.forEach((c) -> {
                colors.add(c);
            });
        });
        return colors;
    }
    
    /**
     * Method to list all the materials that are recommended
     * @return list of materials
     */
    @Override
    public List<Material> getRecommendedMaterials() throws ReadException {
        List<FashionExpert> experts;
        List<Material> materials = null;
        experts = findAllExperts();
        experts.forEach((expert) -> {
            List<Material> material = expert.getRecommendedMaterials();
            material.forEach((m) -> {
                materials.add(m);
            });
        });
        return materials;
    }
    
    /**
     * Method to Login in the apllication
     * @param expert
     * @return
     * @throws ReadException
     * @throws NotAuthorizedException
     * @throws Exception 
     */
    @Override
    public FashionExpert login(FashionExpert expert) throws ReadException, NotAuthorizedException, Exception {
        FashionExpert expertInDB = findExpertByUsername(expert.getUsername());
        try {
            Decryptor decryptor = new Decryptor();
            expert.setPassword(decryptor.decypherRSA(expert.getPassword()));
            boolean correctPassword = expertInDB.getPassword().equals(toHash(expert.getPassword()));
            if(!correctPassword){
                throw new NotAuthorizedException("Wrong Password");
            }
            expertInDB.setLastAccess(new Date());
            em.merge(expertInDB);
                        
        } catch (InvalidKeySpecException | NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException  ex) {
            throw new Exception(ex.getMessage());
        }
        
        return expertInDB;
    }

    /**
     * Method to reestablish the password of a given expert username
     * @param username
     * @throws ReadException
     * @throws Exception 
     */
    @Override
    public void reestabilishPassword(String username) throws ReadException, Exception {
        FashionExpert expert = findExpertByUsername(username);
        String generatedString = RandomStringUtils.randomAlphabetic(10);
        expert.setPassword(generatedString);
        Decryptor decryptor = new Decryptor();
        EmailService emailService = newEmailService(decryptor);
        emailService.sendExpertPasswordReestablishmentEmail(expert);
        em.merge(expert);
        
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
    
    /**
     * Checks if a company with a given nif exists
     *
     * @param nif
     * @return boolean exists
     */
    private boolean expertExists(String username) throws ReadException {
        return (long) em.createNamedQuery("expertExists").setParameter("username", username).getSingleResult() == 1;
    }
    
    /**
     * Creates a new email service object with the address and password from their respective files
     *
     * @param decryptor
     * @return
     * @throws Exception
     */
    private EmailService newEmailService(Decryptor decryptor) throws Exception {
        String encodedPasswordPath = ResourceBundle.getBundle("litfitsserver.miscellaneous.paths").getString("serverLocalSystemAdress") + "/miscellaneous/EncodedPassword.dat";
        String encodedAddressPath = ResourceBundle.getBundle("litfitsserver.miscellaneous.paths").getString("serverLocalSystemAddress") + "/miscellaneous/EncodedAddress.dat";
        String emailAddress = decryptor.decypherAES("Nothin personnel kid", encodedAddressPath);
        String emailAddressPassword = decryptor.decypherAES("Nothin personnel kid", encodedPasswordPath);
        EmailService emailService = new EmailService(emailAddress, emailAddressPassword, null, null);
        return emailService;
    
    }

   
    
}
