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
    @PersistenceContext(unitName = "Lit_Fits_ServerPU")
    private EntityManager entityManager;

    @Override
    public void createColor(Color color) throws CreateException {
        entityManager.persist(color);
    }

    @Override
    public void editColor(Color color) throws UpdateException {
        entityManager.merge(color);
        entityManager.flush();
    }

    @Override
    public void removeColor(Color color) throws ReadException, DeleteException {
        entityManager.remove(entityManager.merge(color));
    }

    @Override
    public Color findColor(String name) throws ReadException {
        return entityManager.find(Color.class, name);
    }

    @Override
    public List<Color> findAllColors() throws ReadException {
        CriteriaQuery cq = entityManager.getCriteriaBuilder().createQuery();
        cq.select(cq.from(Color.class));
        return entityManager.createQuery(cq).getResultList();
    }

    @Override
    public int countColors() throws ReadException {
        CriteriaQuery cq = entityManager.getCriteriaBuilder().createQuery();
        Root<Color> rt = cq.from(Color.class);
        cq.select(entityManager.getCriteriaBuilder().count(rt));
        Query q = entityManager.createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }
}
