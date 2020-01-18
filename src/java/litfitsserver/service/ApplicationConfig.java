package litfitsserver.service;

import java.util.Set;
import javax.ws.rs.core.Application;

/**
 *
 * @author Carlos
 */
@javax.ws.rs.ApplicationPath("webresources")
public class ApplicationConfig extends Application {
    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new java.util.HashSet<>();
        addRestResourceClasses(resources);
        return resources;
    }

    /**
     * Do not modify addRestResourceClasses() method. It is automatically
     * populated with all resources defined in the project. If required, comment
     * out calling this method in getClasses().
     */
    private void addRestResourceClasses(Set<Class<?>> resources) {
        resources.add(litfitsserver.service.ColorFacadeREST.class);
        resources.add(litfitsserver.service.CompanyFacadeREST.class);
        resources.add(litfitsserver.service.FashionExpertFacadeREST.class);
        resources.add(litfitsserver.service.GarmentFacadeREST.class);
        resources.add(litfitsserver.service.MaterialFacadeREST.class);
        resources.add(litfitsserver.service.PublicKeyFacadeREST.class);
        resources.add(litfitsserver.service.UserFacadeREST.class);
    }
}
