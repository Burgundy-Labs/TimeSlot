package models;

public class SubscriptionTypeModel {
    private String subscriptionTypeId;
    private String subscriptionType;
    private Boolean optin;
    private Boolean optout;

    public SubscriptionTypeModel() {

    }

    public SubscriptionTypeModel(String subscriptionTypeId, String subscriptionType) {
        this.subscriptionTypeId = subscriptionTypeId;
        this.subscriptionType = subscriptionType;
    }

    public SubscriptionTypeModel(String appointmentTypeId, String appointmentType, Boolean weekly, Boolean oneTime) {
        this.subscriptionTypeId = appointmentTypeId;
        this.subscriptionType = appointmentType;
        this.optin = optin;
        this.optout = optout;
    }

    public String getSubscriptionTypeId() {
        return subscriptionTypeId;
    }

    public void setSubscriptionTypeId(String subscriptionTypeId) {
        this.subscriptionTypeId = subscriptionTypeId;
    }

    public String getSubscriptionTypes() {
        return subscriptionType;
    }

    public void setSubscriptionType(String subscriptionType) {
        this.subscriptionType = subscriptionType;
    }

    public Boolean getOptin() {
        return optin;
    }

    public void setOptin(Boolean optin) {
        this.optin = optin;
    }

    public Boolean getOptout() {
        return optout;
    }

    public void setOptout(Boolean optout) {
        this.optout = optout;
    }
}

