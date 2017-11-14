package controllers.ApplicationComponents;

import java.util.ArrayList;
import java.util.List;

public class Services {
    private static List<Services> services = new ArrayList<>();
    private String service;

    public Services(String service) {
        this.service = service;
        services.add(this);
    }

    public static List<Services> getServices() {
        return services;
    }

    public static Services getService(String service) {
        for(Services s : services) {
            if(s.service.equals(service)){
                return s;
            }
        }
        return null;
    }

    public String service() {
        return this.service;
    }

    public void removeService(Services service) {
        services.remove(service);
    }
}
