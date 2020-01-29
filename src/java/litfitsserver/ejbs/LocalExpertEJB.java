package litfitsserver.ejbs;

import java.security.NoSuchAlgorithmException;
import java.util.List;
import javax.ejb.CreateException;
import javax.ejb.Local;
import javax.mail.MessagingException;
import javax.ws.rs.NotAuthorizedException;
import litfitsserver.entities.Color;
import litfitsserver.entities.FashionExpert;
import litfitsserver.entities.Material;
import litfitsserver.exceptions.DeleteException;
import litfitsserver.exceptions.ReadException;
import litfitsserver.exceptions.UpdateException;

/**
 *
 * @author Ander
 */
@Local
public interface LocalExpertEJB {

    /**
     * Method to create an exert
     * @param expert
     * @throws CreateException 
     */
    void createExpert (FashionExpert expert) throws CreateException;
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
    void modifyExpert(FashionExpert expert) throws UpdateException, NoSuchAlgorithmException, ReadException, MessagingException, Exception;
    /**
     * Method to delete the expert
     * @param expert
     * @throws ReadException
     * @throws DeleteException 
     */
    void deleteExpert(FashionExpert expert) throws ReadException, DeleteException;
    /**
     * Method to find the expert by primary key
     * @param id
     * @return
     * @throws ReadException 
     */
    FashionExpert findExpert(Long id) throws ReadException;
    /**
     * Method to find expert by username
     * @param username
     * @return
     * @throws ReadException 
     */
    FashionExpert findExpertByUsername(String username) throws ReadException;
    /**
     * Method to list all the experts
     * @return list of experts
     * @throws litfitsserver.exceptions.ReadException
     */
    List<FashionExpert> findAllExperts() throws ReadException;
    /**
     * Method to list all the colors that are recommended
     * @return list of colors
     * @throws litfitsserver.exceptions.ReadException
     */
    List<Color> getRecommendedColors() throws ReadException;
    /**
     * Method to list all the materials that are recommended
     * @return list of materials
     * @throws litfitsserver.exceptions.ReadException
     */
    List<Material> getRecommendedMaterials() throws ReadException;
    /**
     * Method to Login in the apllication
     * @param expert
     * @return
     * @throws ReadException
     * @throws NotAuthorizedException
     * @throws Exception 
     */
    public FashionExpert login(FashionExpert expert) throws ReadException, NotAuthorizedException, Exception;;
    /**
     * Method to reestablish the password of a given expert username
     * @param username
     * @throws ReadException
     * @throws Exception 
     */
    public void reestabilishPassword(String username) throws ReadException, MessagingException, Exception;

}
