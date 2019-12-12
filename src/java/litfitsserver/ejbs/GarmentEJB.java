package litfitsserver.ejbs;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import litfitsserver.entities.Garment;

/**
 * EJB for Garments
 *
 * @author Charlie
 */
@Stateless
public class GarmentEJB implements LocalGarmentEJB {
    @PersistenceContext(unitName = "Test_Server2PU")
    private EntityManager em;

    @Override
    public void createGarment(Garment garment) {
        em.persist(garment);
    }

    @Override
    public void editGarment(Garment garment) {
        em.merge(garment);
        em.flush();
    }

    @Override
    public void removeGarment(Garment garment) {
        em.remove(em.merge(garment));
    }

    @Override
    public Garment findGarment(Long id) {
        return em.find(Garment.class, id);
    }

    @Override
    public List<Garment> findAllGarments() {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(Garment.class));
        return em.createQuery(cq).getResultList();
    }

    @Override
    public int countGarments() {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        Root<Garment> rt = cq.from(Garment.class);
        cq.select(em.getCriteriaBuilder().count(rt));
        Query q = em.createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }

    @Override
    public List<Garment> findGarmentsByCompany(String nif) {
        return (List<Garment>) em.createNamedQuery("findGarmentsByCompany").setParameter("nif", nif).getResultList();
    }

    @Override
    public List<Garment> findGarmentsByRequest(Boolean requested) {
        return (List<Garment>) em.createNamedQuery("findGarmentsByRequest").setParameter("requested", requested).getResultList();
    }

    @Override
    public Garment findGarmentByBarcode(String barcode) {
        return (Garment) em.createNamedQuery("findGarmentByBarcode").setParameter("barcode", barcode).getSingleResult();
    }

    @Override
    public List<Garment> findGarmentsPromoted(Boolean promoted) {
        return (List<Garment>) em.createNamedQuery("findGarmentsPromoted").setParameter("promoted", promoted).getResultList();
    }
    // add color
    // add material
    // remove color
    // remove material
}
