package litfitsserver.ejbs;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import litfitsserver.entities.Color;
import litfitsserver.entities.Garment;
import litfitsserver.entities.Material;
import litfitsserver.exceptions.CreateException;
import litfitsserver.exceptions.DeleteException;
import litfitsserver.exceptions.ReadException;
import litfitsserver.exceptions.UpdateException;

/**
 * EJB for Garments
 *
 * @author Carlos Mendez
 */
@Stateless
public class GarmentEJB implements LocalGarmentEJB {
    /**
     * Injects the EJB of the Company
     */
    @EJB
    private LocalCompanyEJB companyEJB;
    /**
     * Injects the EJB of the Color
     */
    @EJB
    private LocalColorEJB colorEJB;
    /**
     * Injects the EJB of the Material
     */
    @EJB
    private LocalMaterialEJB materialEJB;
    @PersistenceContext(unitName = "Lit_Fits_ServerPU")
    private EntityManager entityManager;

    @Override
    public void createGarment(Garment garment) throws CreateException, ReadException {
        Long companyID = companyEJB.findCompanyByNif(garment.getCompany().getNif()).getId();
        garment.getCompany().setId(companyID);
        Set<Color> colors = new HashSet<>();
        garment.getColors().stream().forEach(color -> {
            try {
                colors.add(colorEJB.findColor(color.getName()));
            } catch (ReadException ex) {
                Logger.getLogger(GarmentEJB.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        garment.setColors(colors);
        Set<Material> materials = new HashSet<>();
        garment.getMaterials().stream().forEach(material -> {
            try {
                materials.add(materialEJB.findMaterial(material.getName()));
            } catch (ReadException ex) {
                Logger.getLogger(GarmentEJB.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        garment.setMaterials(materials);
        entityManager.persist(garment);
    }

    @Override
    public void editGarment(Garment garment) throws UpdateException, ReadException {
        Long companyID = companyEJB.findCompanyByNif(garment.getCompany().getNif()).getId();
        garment.getCompany().setId(companyID);
        Garment garmentInDb = findGarmentByBarcode(garment.getBarcode());
        garment.setId(garmentInDb.getId());
        entityManager.merge(garment);
        entityManager.flush();
    }

    @Override
    public void removeGarment(Garment garment) throws ReadException, DeleteException {
        garment = findGarmentByBarcode(garment.getBarcode());
        entityManager.remove(garment);
    }

    @Override
    public Garment findGarment(Long id) throws ReadException {
        return entityManager.find(Garment.class, id);
    }

    @Override
    public List<Garment> findAllGarments() throws ReadException {
        CriteriaQuery cq = entityManager.getCriteriaBuilder().createQuery();
        cq.select(cq.from(Garment.class));
        return entityManager.createQuery(cq).getResultList();
    }

    @Override
    public int countGarments() throws ReadException {
        CriteriaQuery cq = entityManager.getCriteriaBuilder().createQuery();
        Root<Garment> rt = cq.from(Garment.class);
        cq.select(entityManager.getCriteriaBuilder().count(rt));
        Query q = entityManager.createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }

    @Override
    public List<Garment> findGarmentsByCompany(String nif) throws ReadException {
        return (List<Garment>) entityManager.createNamedQuery("findGarmentsByCompany").setParameter("nif", nif).getResultList();
    }

    @Override
    public List<Garment> findGarmentsByRequest(Boolean requested) throws ReadException {
        return (List<Garment>) entityManager.createNamedQuery("findGarmentsByRequest").setParameter("requested", requested).getResultList();
    }

    @Override
    public Garment findGarmentByBarcode(String barcode) throws ReadException {
        return (Garment) entityManager.createNamedQuery("findGarmentByBarcode").setParameter("barcode", barcode).getSingleResult();
    }

    @Override
    public List<Garment> findGarmentsPromoted(Boolean promoted) throws ReadException {
        return (List<Garment>) entityManager.createNamedQuery("findGarmentsPromoted").setParameter("promoted", promoted).getResultList();
    }

    @Override
    public byte[] getImage(Long id) throws IOException, ReadException {
        byte[] imageBytes = findGarment(id).getPicture();
        return imageBytes;
    }
}
