/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package litfitsserver.ejbs;

import java.util.List;
import javax.ejb.CreateException;
import javax.ejb.Local;
import litfitsserver.entities.Color;
import litfitsserver.entities.FashionExpert;
import litfitsserver.entities.Material;

/**
 *
 * @author 2dam
 */
@Local
public interface LocalExpertEJB {

    
    void createExpert (FashionExpert expert) throws CreateException;
    void modifyExpert(FashionExpert expert);
    void deleteExpert(FashionExpert expert);
    FashionExpert findExpertById(String id);
    List<FashionExpert> findAllExperts();
    List<Color> getRecommendedColors(String username);
    List<Material> getRecommendedMaterials(String username);
    public FashionExpert login(FashionExpert expert);
    public void reestabilishPassword(String nif);

}
