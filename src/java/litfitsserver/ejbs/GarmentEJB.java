package litfitsserver.ejbs;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import litfitsserver.entities.Garment;
import litfitsserver.exceptions.CreateException;
import litfitsserver.exceptions.DeleteException;
import litfitsserver.exceptions.ReadException;
import litfitsserver.exceptions.UpdateException;

/**
 * EJB for Garments
 *
 * @author Carlos
 */
@Stateless
public class GarmentEJB implements LocalGarmentEJB {
    @PersistenceContext(unitName = "Test_Server2PU")
    private EntityManager em;

    @Override
    public void createGarment(Garment garment) throws CreateException {
        em.persist(garment);
    }

    @Override
    public void editGarment(Garment garment) throws UpdateException {
        em.merge(garment);
        em.flush();
    }

    @Override
    public void removeGarment(Garment garment) throws ReadException, DeleteException {
        em.remove(em.merge(garment));
    }

    @Override
    public Garment findGarment(Long id) throws ReadException {
        return em.find(Garment.class, id);
    }

    @Override
    public List<Garment> findAllGarments() throws ReadException {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(Garment.class));
        return em.createQuery(cq).getResultList();
    }

    @Override
    public int countGarments() throws ReadException {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        Root<Garment> rt = cq.from(Garment.class);
        cq.select(em.getCriteriaBuilder().count(rt));
        Query q = em.createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }

    @Override
    public List<Garment> findGarmentsByCompany(String nif) throws ReadException {
        return (List<Garment>) em.createNamedQuery("findGarmentsByCompany").setParameter("nif", nif).getResultList();
    }

    @Override
    public List<Garment> findGarmentsByRequest(Boolean requested) throws ReadException {
        return (List<Garment>) em.createNamedQuery("findGarmentsByRequest").setParameter("requested", requested).getResultList();
    }

    @Override
    public Garment findGarmentByBarcode(String barcode) throws ReadException {
        return (Garment) em.createNamedQuery("findGarmentByBarcode").setParameter("barcode", barcode).getSingleResult();
    }

    @Override
    public List<Garment> findGarmentsPromoted(Boolean promoted) throws ReadException {
        return (List<Garment>) em.createNamedQuery("findGarmentsPromoted").setParameter("promoted", promoted).getResultList();
    }
}
