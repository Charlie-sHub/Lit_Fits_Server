package litfitsserver.ejbs;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import litfitsserver.entities.Material;

/**
 * EJB for Materials
 *
 * @author Charlie
 */
@Stateless
public class MaterialEJB implements LocalMaterialEJB {
    @PersistenceContext(unitName = "Test_Server2PU")
    private EntityManager em;

    @Override
    public void createMaterial(Material material) {
        em.persist(material);
    }

    @Override
    public void editMaterial(Material material) {
        em.merge(material);
        em.flush();
    }

    @Override
    public void removeMaterial(Material material) {
        em.remove(em.merge(material));
    }

    @Override
    public Material findMaterial(String name) {
        return em.find(Material.class, name);
    }

    @Override
    public List<Material> findAllMaterials() {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(Material.class));
        return em.createQuery(cq).getResultList();
    }

    @Override
    public int countMaterials() {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        Root<Material> rt = cq.from(Material.class);
        cq.select(em.getCriteriaBuilder().count(rt));
        Query q = em.createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }
}
