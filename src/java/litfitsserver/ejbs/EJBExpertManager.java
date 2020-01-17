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

    @Override
    public void modifyExpert(FashionExpert expert) throws UpdateException, NoSuchAlgorithmException, ReadException, MessagingException, Exception{
        Decryptor decryptor = new Decryptor();
        expert.setPassword(decryptor.decypherRSA(expert.getPassword()));
        FashionExpert expertInDB = findExpertByUsername(expert.getUsername());
        boolean correctPassword = expertInDB.getPassword().equals(toHash(expert.getPassword()));
        
        if(!correctPassword){
            //Change the passwword by email
        }
        
        em.merge(expert);
        em.flush();
    }

    @Override
    public void deleteExpert(FashionExpert expert) throws ReadException, DeleteException {
        em.remove(em.merge(expert));
    }
    
    @Override
    public FashionExpert findExpert(Long id) throws ReadException {
        return em.find(FashionExpert.class, id);
    }
    
    @Override
    public FashionExpert findExpertByUsername(String username) throws ReadException{
        return (FashionExpert) em.createNamedQuery("findExpertByUsername").setParameter("username", username).getSingleResult();
    }
    
    @Override
    public List<FashionExpert> findAllExperts() {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(FashionExpert.class));
        return em.createQuery(cq).getResultList();
    }
    
    @Override
    public List<Color> getRecommendedColors() {
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

    @Override
    public List<Material> getRecommendedMaterials() {
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

    @Override
    public void reestabilishPassword(String username) {
    }

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
    
    private boolean expertExists(String username) throws ReadException {
        return (long) em.createNamedQuery("expertExists").setParameter("username", username).getSingleResult() == 1;
    }

   
    
}
