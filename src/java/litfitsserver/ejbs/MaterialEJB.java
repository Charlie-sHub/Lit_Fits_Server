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
    private EntityManager entityManager;

    @Override
    public void createMaterial(Material material) throws CreateException {
        entityManager.persist(material);
    }

    @Override
    public void editMaterial(Material material) throws UpdateException {
        entityManager.merge(material);
        entityManager.flush();
    }

    @Override
    public void removeMaterial(Material material) throws ReadException, DeleteException {
        entityManager.remove(entityManager.merge(material));
    }

    @Override
    public Material findMaterial(String name) throws ReadException {
        return entityManager.find(Material.class, name);
    }

    @Override
    public List<Material> findAllMaterials() throws ReadException {
        CriteriaQuery cq = entityManager.getCriteriaBuilder().createQuery();
        cq.select(cq.from(Material.class));
        return entityManager.createQuery(cq).getResultList();
    }

    @Override
    public int countMaterials() throws ReadException {
        CriteriaQuery cq = entityManager.getCriteriaBuilder().createQuery();
        Root<Material> rt = cq.from(Material.class);
        cq.select(entityManager.getCriteriaBuilder().count(rt));
        Query q = entityManager.createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }
}
