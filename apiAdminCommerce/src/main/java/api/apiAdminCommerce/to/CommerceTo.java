package api.apiAdminCommerce.to;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.OffsetDateTime;
import java.util.LinkedHashMap;

public class CommerceTo {

    private Long idCommerce;

    private String commerceDocument;

    private String commerceName;

    private String stateName;

    private String municipalityName;

    private String cityDescription;

    private String contactPerson;

    private OffsetDateTime registerDate;

    private OffsetDateTime desvinculacionDate;

    private String statusDescription;

    private String commerceEmail;

    private String phoneNumberCommerce;

    private String typeCommerce;

    private Long idTypeCommerce;

    private String planName;

    public CommerceTo(Long idCommerce, String commerceDocument, String commerceName, String stateName, String municipalityName, String cityDescription, String contactPerson, OffsetDateTime registerDate, String statusDescription, String phoneNumberCommerce, String typeCommerce, String planName) {
        this.idCommerce = idCommerce;
        this.commerceDocument = commerceDocument;
        this.commerceName = commerceName;
        this.stateName = stateName;
        this.municipalityName = municipalityName;
        this.cityDescription = cityDescription;
        this.contactPerson = contactPerson;
        this.registerDate = registerDate;
        this.statusDescription = statusDescription;
        this.phoneNumberCommerce = phoneNumberCommerce;
        this.typeCommerce = typeCommerce;
        this.planName = planName;
    }

    public CommerceTo(Long idCommerce, String commerceDocument, String commerceEmail) {
        this.idCommerce = idCommerce;
        this.commerceDocument = commerceDocument;
        this.commerceEmail = commerceEmail;
    }

    public CommerceTo(ObjectMapper objectMapper, LinkedHashMap<String, Object> response){

        this.idCommerce = objectMapper.convertValue(response.get("idCommerce"), Long.class);
        this.commerceDocument = objectMapper.convertValue(response.get("commerceDocument"), String.class);
        this.commerceEmail = objectMapper.convertValue(response.get("commerceEmail"), String.class);
        this.phoneNumberCommerce = objectMapper.convertValue(response.get("phoneNumberCommerce"), String.class);
        this.idTypeCommerce =  objectMapper.convertValue(response.get("idTypeCommerce"), Long.class);

    }

    public Long getIdCommerce() {
        return idCommerce;
    }

    public void setIdCommerce(Long idCommerce) {
        this.idCommerce = idCommerce;
    }

    public String getCommerceDocument() {
        return commerceDocument;
    }

    public void setCommerceDocument(String commerceDocument) {
        this.commerceDocument = commerceDocument;
    }

    public String getCommerceName() {
        return commerceName;
    }

    public void setCommerceName(String commerceName) {
        this.commerceName = commerceName;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public String getMunicipalityName() {
        return municipalityName;
    }

    public void setMunicipalityName(String municipalityName) {
        this.municipalityName = municipalityName;
    }

    public String getCityDescription() {
        return cityDescription;
    }

    public void setCityDescription(String cityDescription) {
        this.cityDescription = cityDescription;
    }

    public String getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    public OffsetDateTime getRegisterDate() {
        return registerDate;
    }

    public void setRegisterDate(OffsetDateTime registerDate) {
        this.registerDate = registerDate;
    }

    public OffsetDateTime getDesvinculacionDate() {
        return desvinculacionDate;
    }

    public void setDesvinculacionDate(OffsetDateTime desvinculacionDate) {
        this.desvinculacionDate = desvinculacionDate;
    }

    public String getStatusDescription() {
        return statusDescription;
    }

    public void setStatusDescription(String statusDescription) {
        this.statusDescription = statusDescription;
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

    public String getTypeCommerce() {
        return typeCommerce;
    }

    public void setTypeCommerce(String typeCommerce) {
        this.typeCommerce = typeCommerce;
    }

    public String getPlanName() {
        return planName;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }

    public Long getIdTypeCommerce() {
        return idTypeCommerce;
    }

    public void setIdTypeCommerce(Long idTypeCommerce) {
        this.idTypeCommerce = idTypeCommerce;
    }
}