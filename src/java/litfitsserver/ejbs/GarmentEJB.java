package litfitsserver.ejbs;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.ws.rs.core.MultivaluedMap;
import litfitsserver.entities.Garment;
import litfitsserver.exceptions.CreateException;
import litfitsserver.exceptions.DeleteException;
import litfitsserver.exceptions.ReadException;
import litfitsserver.exceptions.UpdateException;
import org.apache.commons.io.IOUtils;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

/**
 * EJB for Garments
 *
 * @author Carlos Mendez
 */
@Stateless
public class GarmentEJB implements LocalGarmentEJB {
    @PersistenceContext(unitName = "Lit_Fits_ServerPU")
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

    @Override
    public File getImage(Long id) throws IOException, ReadException {
        File image = new File(findGarment(id).getPictureName());
        return image;
    }

    @Override
    public boolean uploadPicture(MultipartFormDataInput input, Long id) throws IOException, ReadException {
        Boolean success = false;
        Map<String, List<InputPart>> uploadForm = input.getFormDataMap();
        // Get file data to save
        List<InputPart> inputParts = uploadForm.get("attachment");
        // change to inputParts.stream().forEach(inputPart -> {});
        for (InputPart inputPart : inputParts) {
            MultivaluedMap<String, String> header = inputPart.getHeaders();
            String fileName = getFileName(header);
            // convert the uploaded file to inputstream
            InputStream inputStream = inputPart.getBody(InputStream.class, null);
            byte[] bytes = IOUtils.toByteArray(inputStream);
            String path = System.getProperty("user.home") + File.separator + "Pictures";
            File customDir = new File(path);
            fileName = customDir.getCanonicalPath() + File.separator + fileName;
            writeFile(bytes, fileName);
            findGarment(id).setPictureName(path);
            success = true;
        }
        return success;
    }

    private String getFileName(MultivaluedMap<String, String> header) {
        String[] contentDisposition = header.getFirst("Content-Disposition").split(";");
        // stream() it
        for (String filename : contentDisposition) {
            if ((filename.trim().startsWith("filename"))) {
                String[] name = filename.split("=");
                String finalFileName = name[1].trim().replaceAll("\"", "");
                return finalFileName;
            }
        }
        return "unknown";
    }

    private void writeFile(byte[] content, String filename) throws IOException {
        File file = new File(filename);
        if (!file.exists()) {
            file.createNewFile();
        }
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        fileOutputStream.write(content);
        fileOutputStream.flush();
        fileOutputStream.close();
    }
}
