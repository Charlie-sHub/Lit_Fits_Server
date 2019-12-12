/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package litfitsserver.ejbs;

import java.util.List;
import javax.ejb.EJB;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import litfitsserver.entities.Color;
import litfitsserver.entities.Garment;
import litfitsserver.entities.Material;
import litfitsserver.entities.User;

/**
 * The EJB for the User on the app
 * @author Asier Vila Dominguez
 */
@EJB
public abstract class UserEJB implements LocalUserEJB{

    @PersistenceContext(unitName = "Lit_Fits_ServerPU")
    private EntityManager em;
    
    @Override
    public void createUser (User user) {
        em.persist(user);
    }

    @Override
    public void editUser (User user) {
        em.merge(user);
    }

    @Override
    public void removeUser (User user) {
        em.remove(em.merge(user));
    }

    @Override
    public User findUser (String username) {
        return em.find(User.class, username);
    }

    @Override
    public List<User> findAllUsers () {
        javax.persistence.criteria.CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(User.class));
        return (List<User>) em.createQuery(cq).getResultList();
    }

    @Override
    public int countUsers () {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        Root<User> rt = cq.from(User.class);
        cq.select(em.getCriteriaBuilder().count(rt));
        javax.persistence.Query q = em.createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }

    @Override
    public List<Garment> getUserGarments (String username) {
        return (List<Garment>) em.createNamedQuery("getUserGarments").setParameter("username", username).getResultList();
    }

    @Override
    public List<Color> getUserLikedColors (String username) {
        return (List<Color>) em.createNamedQuery("getUserLikedColors").setParameter("username", username).getResultList();
    }

    @Override
    public List<Material> getUserLikedMaterials (String username) {
        return (List<Material>) em.createNamedQuery("getUserLikedColors").setParameter("username", username).getResultList();
    }
}
