package litfitsserver.ejbs;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import litfitsserver.entities.Company;
import litfitsserver.exceptions.CreateException;
import litfitsserver.exceptions.DeleteException;
import litfitsserver.exceptions.ReadException;
import litfitsserver.exceptions.UpdateException;

/**
 * EJB for Companies
 *
 * @author Carlos
 */
@Stateless
public class CompanyEJB implements LocalCompanyEJB {
    @PersistenceContext(unitName = "Test_Server2PU")
    private EntityManager em;

    @Override
    public void createCompany(Company company) throws CreateException {
        em.persist(company);
    }

    @Override
    public void editCompany(Company company) throws UpdateException {
        em.merge(company);
        em.flush();
    }

    @Override
    public void removeCompany(Company company) throws ReadException, DeleteException {
        em.remove(em.merge(company));
    }

    @Override
    public Company findCompany(Long id) throws ReadException {
        return em.find(Company.class, id);
    }

    @Override
    public List<Company> findAllCompanies() throws ReadException {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(Company.class));
        return em.createQuery(cq).getResultList();
    }

    @Override
    public int countCompanies() throws ReadException {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        Root<Company> rt = cq.from(Company.class);
        cq.select(em.getCriteriaBuilder().count(rt));
        Query q = em.createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }

    @Override
    public Company findCompanyByNif(String nif) throws ReadException {
        return (Company) em.createNamedQuery("findCompanyByNif").setParameter("nif", nif).getSingleResult();
    }
    //add garment
}
