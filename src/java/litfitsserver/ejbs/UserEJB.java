/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package litfitsserver.ejbs;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import litfitsserver.entities.Color;
import litfitsserver.entities.Garment;
import litfitsserver.entities.Material;
import litfitsserver.entities.User;
import litfitsserver.exceptions.CreateException;
import litfitsserver.exceptions.DeleteException;
import litfitsserver.exceptions.ReadException;
import litfitsserver.exceptions.UpdateException;

/**
 * The EJB for the User on the app
 * @author Asier Vila Dominguez
 */
@Stateless
public class UserEJB implements LocalUserEJB{

    @PersistenceContext(unitName = "Test_Server2PU")
    private EntityManager em;
    
    @Override
    public void createUser(User user) throws CreateException {
        em.persist(user);
    }

    @Override
    public void editUser(User user) throws UpdateException {
        em.merge(user);
    }

    @Override
    public void removeUser(User user) throws ReadException, DeleteException {
        em.remove(em.merge(user));
    }

    @Override
    public User findUser(String username) throws ReadException {
        return em.find(User.class, username);
    }

    @Override
    public List<User> findAllUsers() throws ReadException {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(User.class));
        return (List<User>) em.createQuery(cq).getResultList();
    }

    @Override
    public int countUsers() throws ReadException {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        Root<User> rt = cq.from(User.class);
        cq.select(em.getCriteriaBuilder().count(rt));
        Query q = em.createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }

    @Override
    public List<Garment> getUserGarments(String username) throws ReadException {
        return (List<Garment>) em.createNamedQuery("getUserGarments").setParameter("username", username).getResultList();
    }

    @Override
    public List<Color> getUserLikedColors(String username) throws ReadException {
        return (List<Color>) em.createNamedQuery("getUserLikedColors").setParameter("username", username).getResultList();
    }

    @Override
    public List<Material> getUserLikedMaterials(String username) throws ReadException {
        return (List<Material>) em.createNamedQuery("getUserLikedMaterials").setParameter("username", username).getResultList();
    }
}
