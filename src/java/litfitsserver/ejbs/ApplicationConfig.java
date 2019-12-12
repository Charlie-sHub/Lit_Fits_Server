/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package litfitsserver.ejbs;

import litfitsserver.entities.*;
import service.*;
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
        resources.add(service.ColorFacadeREST.class);
        resources.add(service.CompanyFacadeREST.class);
        resources.add(service.GarmentFacadeREST.class);
        resources.add(service.MaterialFacadeREST.class);
        resources.add(service.UserFacadeREST.class);
    }
}
