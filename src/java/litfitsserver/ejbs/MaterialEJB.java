package litfitsserver.ejbs;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import litfitsserver.entities.Material;
import litfitsserver.exceptions.CreateException;
import litfitsserver.exceptions.DeleteException;
import litfitsserver.exceptions.ReadException;
import litfitsserver.exceptions.UpdateException;

/**
 * EJB for Materials
 *
 * @author Carlos
 */
@Stateless
public class MaterialEJB implements LocalMaterialEJB {
    @PersistenceContext(unitName = "Lit_Fits_ServerPU")
    private EntityManager em;

    @Override
    public void createMaterial(Material material) throws CreateException {
        em.persist(material);
    }

    @Override
    public void editMaterial(Material material) throws UpdateException {
        em.merge(material);
        em.flush();
    }

    @Override
    public void removeMaterial(Material material) throws ReadException, DeleteException {
        em.remove(em.merge(material));
    }

    @Override
    public Material findMaterial(String name) throws ReadException {
        return em.find(Material.class, name);
    }

    @Override
    public List<Material> findAllMaterials() throws ReadException {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(Material.class));
        return em.createQuery(cq).getResultList();
    }

    @Override
    public int countMaterials() throws ReadException {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        Root<Material> rt = cq.from(Material.class);
        cq.select(em.getCriteriaBuilder().count(rt));
        Query q = em.createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }
}
