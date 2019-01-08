package models;

public class ServiceModel {
    private String serviceId;
    private String service;
    private String prompt;

    public ServiceModel() {

    }

    public ServiceModel(String serviceId, String service, String prompt) {
        this.serviceId = serviceId;
        this.service = service;
        this.prompt = prompt;
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

    public String getPrompt() {
        return prompt;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }
}
