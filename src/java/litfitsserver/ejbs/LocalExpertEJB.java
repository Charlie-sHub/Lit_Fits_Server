/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package litfitsserver.ejbs;

import java.security.NoSuchAlgorithmException;
import java.util.List;
import javax.ejb.CreateException;
import javax.ejb.Local;
import javax.mail.MessagingException;
import javax.ws.rs.NotAuthorizedException;
import litfitsserver.entities.FashionExpert;
import litfitsserver.exceptions.DeleteException;
import litfitsserver.exceptions.ReadException;
import litfitsserver.exceptions.UpdateException;

/**
 *
 * @author Ander
 */
@Local
public interface LocalExpertEJB {

    
    void createExpert (FashionExpert expert) throws CreateException;
    void modifyExpert(FashionExpert expert) throws UpdateException, NoSuchAlgorithmException, ReadException, MessagingException, Exception;
    void deleteExpert(FashionExpert expert) throws ReadException, DeleteException;
    FashionExpert findExpertById(String id) throws ReadException;
    List<FashionExpert> findAllExperts() throws ReadException;
    // List<Color> getRecommendedColors(String username);
    // List<Material> getRecommendedMaterials(String username);
    public FashionExpert login(FashionExpert expert) throws ReadException, NotAuthorizedException, Exception;;
    public void reestabilishPassword(String username) throws ReadException, MessagingException, Exception;

}
