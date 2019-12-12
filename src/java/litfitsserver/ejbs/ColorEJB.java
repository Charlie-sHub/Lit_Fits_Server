package litfitsserver.ejbs;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import litfitsserver.entities.Color;
import litfitsserver.exceptions.CreateException;
import litfitsserver.exceptions.DeleteException;
import litfitsserver.exceptions.ReadException;
import litfitsserver.exceptions.UpdateException;

/**
 * EJB for Colors
 *
 * @author Carlos
 */
@Stateless
public class ColorEJB implements LocalColorEJB {
    @PersistenceContext(unitName = "Test_Server2PU")
    private EntityManager em;

    @Override
    public void createColor(Color color) throws CreateException {
        em.persist(color);
    }

    @Override
    public void editColor(Color color) throws UpdateException {
        em.merge(color);
        em.flush();
    }

    @Override
    public void removeColor(Color color) throws ReadException, DeleteException {
        em.remove(em.merge(color));
    }

    @Override
    public Color findColor(String name) throws ReadException {
        return em.find(Color.class, name);
    }

    @Override
    public List<Color> findAllColors() throws ReadException {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(Color.class));
        return em.createQuery(cq).getResultList();
    }

    @Override
    public int countColors() throws ReadException {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        Root<Color> rt = cq.from(Color.class);
        cq.select(em.getCriteriaBuilder().count(rt));
        Query q = em.createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }
}
