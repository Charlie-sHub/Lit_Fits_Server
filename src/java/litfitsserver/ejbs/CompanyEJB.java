package litfitsserver.ejbs;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import litfitsserver.entities.Company;

/**
 * EJB for Companies
 *
 * @author Charlie
 */
@Stateless
public class CompanyEJB implements LocalCompanyEJB {
    @PersistenceContext(unitName = "Test_Server2PU")
    private EntityManager em;

    @Override
    public void createCompany(Company company) {
        em.persist(company);
    }

    @Override
    public void editCompany(Company company) {
        em.merge(company);
        em.flush();
    }

    @Override
    public void removeCompany(Company company) {
        em.remove(em.merge(company));
    }

    @Override
    public Company findCompany(Long id) {
        return em.find(Company.class, id);
    }

    @Override
    public List<Company> findAllCompanies() {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(Company.class));
        return em.createQuery(cq).getResultList();
    }

    @Override
    public int countCompanies() {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        Root<Company> rt = cq.from(Company.class);
        cq.select(em.getCriteriaBuilder().count(rt));
        Query q = em.createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }
    
    @Override
    public Company findCompanyByNif(String nif) {
        return (Company) em.createNamedQuery("findCompanyByNif").setParameter("nif", nif).getSingleResult();
    }
    //add garment
}
