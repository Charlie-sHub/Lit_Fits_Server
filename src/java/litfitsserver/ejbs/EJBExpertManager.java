/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package litfitsserver.ejbs;

import java.util.List;
import javax.ejb.CreateException;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import litfitsserver.entities.Color;
import litfitsserver.entities.FashionExpert;
import litfitsserver.entities.Material;

/**
 *
 * @author Ander
 */

@Stateless
public class EJBExpertManager implements LocalExpertEJB {

    @PersistenceContext(unitName = "Lit_Fits_ServerPU")
    private EntityManager em;
    
    @Override
    public void createExpert (FashionExpert expert) throws CreateException{
        Decryptor decryptor = new Decryptor();
        try{
            em.persist(expert);
        }catch(Exception e){
            throw new CreateException(e.getMessage());
        }
    }

    @Override
    public void modifyExpert(FashionExpert expert) {
        em.merge(expert);
        em.flush();
    }

    @Override
    public void deleteExpert(FashionExpert expert) {
        em.remove(em.merge(em.find(FashionExpert.class, expert)));
    }
    
    @Override
    public FashionExpert findExpertById(String id){
        return em.find(FashionExpert.class, id);
    }
    
    @Override
    public List<FashionExpert> findAllExperts() {
        return em.createNamedQuery("findAllExperts").getResultList();
    }

    @Override
    public List<Color> getRecommendedColors(String username) {
        return (List<Color>) em.createNamedQuery("getExpertRecommendedColors").setParameter("username", username).getResultList();
    }

    @Override
    public List<Material> getRecommendedMaterials(String username) {
        return (List<Material>) em.createNamedQuery("getExpertRecommendedMaterials").setParameter("username", username).getResultList();
    }

    @Override
    public FashionExpert login(FashionExpert expert) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void reestabilishPassword(String nif) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
