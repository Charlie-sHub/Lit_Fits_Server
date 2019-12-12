package litfitsserver.ejbs;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import litfitsserver.entities.Color;

/**
 * EJB for Colors
 *
 * @author Charlie
 */
@Stateless
public class ColorEJB implements LocalColorEJB {
    @PersistenceContext(unitName = "Test_Server2PU")
    private EntityManager em;

    @Override
    public void createColor(Color color) {
        em.persist(color);
    }

    @Override
    public void editColor(Color color) {
        em.merge(color);
        em.flush();
    }

    @Override
    public void removeColor(Color color) {
        em.remove(em.merge(color));
    }

    @Override
    public Color findColor(String name) {
        return em.find(Color.class, name);
    }

    @Override
    public List<Color> findAllColors() {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(Color.class));
        return em.createQuery(cq).getResultList();
    }

    @Override
    public int countColors() {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        Root<Color> rt = cq.from(Color.class);
        cq.select(em.getCriteriaBuilder().count(rt));
        Query q = em.createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }
}
