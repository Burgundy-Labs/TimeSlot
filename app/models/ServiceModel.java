package models;

public class ServiceModel {
    private String serviceId;
    private String service;

    public ServiceModel() {

    }

    public ServiceModel(String serviceId, String service) {
        this.serviceId = serviceId;
        this.service = service;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }
}
