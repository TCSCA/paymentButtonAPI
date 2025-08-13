package api.apiAdminCommerce.to;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.LinkedHashMap;

public class PreRegisterTo {

    private Long idPreRegistro;

    private String commerceName;

    private String commerceDocument;

    private String commerceEmail;

    private String phoneNumberCommerce;

    private String contactPerson;

    private String contactPersonEmail;

    private Long idState;

    private String address;

    private Long statusPreRegister;

    private String contactPersonDocument;

    private Long idPlan;

    private Long idTypeCommerce;

    public PreRegisterTo() {
    }

    public PreRegisterTo(ObjectMapper objectMapper, LinkedHashMap<String, Object> response) {

        this.idPreRegistro = objectMapper.convertValue(((LinkedHashMap<String, Object>) response.get("preRegisterEntity")).get("idPreRegistro"), Long.class);
        this.commerceName = objectMapper.convertValue(((LinkedHashMap<String, Object>) response.get("preRegisterEntity")).get("commerceName"), String.class);
        this.commerceDocument = objectMapper.convertValue(((LinkedHashMap<String, Object>) response.get("preRegisterEntity")).get("commerceDocument"), String.class);
        this.commerceEmail = objectMapper.convertValue(((LinkedHashMap<String, Object>) response.get("preRegisterEntity")).get("commerceEmail"), String.class);
        this.phoneNumberCommerce = objectMapper.convertValue(((LinkedHashMap<String, Object>) response.get("preRegisterEntity")).get("phoneNumberCommerce"), String.class);
        this.contactPerson = objectMapper.convertValue(((LinkedHashMap<String, Object>) response.get("preRegisterEntity")).get("contactPerson"), String.class);
        this.contactPersonEmail = objectMapper.convertValue(((LinkedHashMap<String, Object>) response.get("preRegisterEntity")).get("contactPersonEmail"), String.class);

        LinkedHashMap<String, Object> directionEntityMap = (LinkedHashMap<String, Object>) ((LinkedHashMap<String, Object>) response.get("preRegisterEntity")).get("directionEntity");
        this.address = objectMapper.convertValue(directionEntityMap.get("address"), String.class);

        LinkedHashMap<String, Object> stateEntityMap = (LinkedHashMap<String, Object>) directionEntityMap.get("stateEntity");

        if(stateEntityMap == null) {
            this.idState = null;
        } else {
            this.idState = objectMapper.convertValue(stateEntityMap.get("idState"), Long.class);
        }

        LinkedHashMap<String, Object> statusPreRegisterEntityMap = (LinkedHashMap<String, Object>) ((LinkedHashMap<String, Object>) response.get("preRegisterEntity")).get("statusPreRegisterEntity");
        this.statusPreRegister = objectMapper.convertValue(statusPreRegisterEntityMap.get("idPreRegistro"), Long.class);

        this.contactPersonDocument = objectMapper.convertValue(((LinkedHashMap<String, Object>) response.get("preRegisterEntity")).get("contactPersonDocument"), String.class);

        LinkedHashMap<String, Object> planEntityMap = (LinkedHashMap<String, Object>) ((LinkedHashMap<String, Object>) response.get("preRegisterEntity")).get("planEntity");
        this.idPlan = objectMapper.convertValue(planEntityMap.get("idPlan"), Long.class);

        LinkedHashMap<String, Object> typeCommerceEntityMap = (LinkedHashMap<String, Object>) planEntityMap.get("typeCommerceEntity");
        this.idTypeCommerce = objectMapper.convertValue(typeCommerceEntityMap.get("idTypeCommerce"), Long.class);

    }

    public PreRegisterTo(Long idPreRegistro, String commerceName, String commerceDocument, String commerceEmail, String phoneNumberCommerce, String contactPerson, String contactPersonEmail, Object statusPreRegisterEntity, Object directionEntity) {
        this.idPreRegistro = idPreRegistro;
        this.commerceName = commerceName;
        this.commerceDocument = commerceDocument;
        this.commerceEmail = commerceEmail;
        this.phoneNumberCommerce = phoneNumberCommerce;
        this.contactPerson = contactPerson;
        this.contactPersonEmail = contactPersonEmail;
    }

    public Long getIdPreRegistro() {
        return idPreRegistro;
    }

    public void setIdPreRegistro(Long idPreRegistro) {
        this.idPreRegistro = idPreRegistro;
    }

    public String getCommerceName() {
        return commerceName;
    }

    public void setCommerceName(String commerceName) {
        this.commerceName = commerceName;
    }

    public String getCommerceDocument() {
        return commerceDocument;
    }

    public void setCommerceDocument(String commerceDocument) {
        this.commerceDocument = commerceDocument;
    }

    public String getCommerceEmail() {
        return commerceEmail;
    }

    public void setCommerceEmail(String commerceEmail) {
        this.commerceEmail = commerceEmail;
    }

    public String getPhoneNumberCommerce() {
        return phoneNumberCommerce;
    }

    public void setPhoneNumberCommerce(String phoneNumberCommerce) {
        this.phoneNumberCommerce = phoneNumberCommerce;
    }

    public String getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    public String getContactPersonEmail() {
        return contactPersonEmail;
    }

    public void setContactPersonEmail(String contactPersonEmail) {
        this.contactPersonEmail = contactPersonEmail;
    }

    public Long getidState() {
        return idState;
    }

    public void setidState(Long idState) {
        this.idState = idState;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Long getStatusPreRegister() {
        return statusPreRegister;
    }

    public void setStatusPreRegister(Long statusPreRegister) {
        this.statusPreRegister = statusPreRegister;
    }

    public String getContactPersonDocument() {
        return contactPersonDocument;
    }

    public void setContactPersonDocument(String contactPersonDocument) {
        this.contactPersonDocument = contactPersonDocument;
    }

    public Long getIdPlan() {
        return idPlan;
    }

    public void setIdPlan(Long idPlan) {
        this.idPlan = idPlan;
    }

    public Long getIdTypeCommerce() {
        return idTypeCommerce;
    }

    public void setIdTypeCommerce(Long idTypeCommerce) {
        this.idTypeCommerce = idTypeCommerce;
    }
}
