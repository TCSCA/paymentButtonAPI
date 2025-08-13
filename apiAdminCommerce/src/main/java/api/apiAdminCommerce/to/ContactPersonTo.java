package api.apiAdminCommerce.to;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.LinkedHashMap;

public class ContactPersonTo {

    private Long idUser;

    private String commerceName;

    private String phoneNumberCommerce;

    private String contactPersonEmail;

    private String description;


    public ContactPersonTo() {
    }

    public ContactPersonTo(ObjectMapper objectMapper, LinkedHashMap<String, Object> response) {


        this.commerceName = objectMapper.convertValue(((LinkedHashMap<String, Object>) response.get("commerceEntity")).get("commerceName"), String.class);
        this.phoneNumberCommerce = objectMapper.convertValue(((LinkedHashMap<String, Object>) response.get("supportEntity")).get("phoneContactPerson"), String.class);
        this.contactPersonEmail = objectMapper.convertValue(((LinkedHashMap<String, Object>) response.get("supportEntity")).get("contactEmail"), String.class);
        this.description = objectMapper.convertValue(((LinkedHashMap<String, Object>) response.get("supportEntity")).get("request"), String.class);



    }

    public ContactPersonTo(Long idUser, String commerceName, String phoneNumberCommerce, String contactPersonEmail, String description) {
        this.idUser = idUser;
        this.commerceName = commerceName;
        this.phoneNumberCommerce = phoneNumberCommerce;
        this.contactPersonEmail = contactPersonEmail;
        this.description = description;
    }

    public Long getIdUser() {
        return idUser;
    }

    public void setIdUser(Long idUser) {
        this.idUser = idUser;
    }

    public String getCommerceName() {
        return commerceName;
    }

    public void setCommerceName(String commerceName) {
        this.commerceName = commerceName;
    }

    public String getPhoneNumberCommerce() {
        return phoneNumberCommerce;
    }

    public void setPhoneNumberCommerce(String phoneNumberCommerce) {
        this.phoneNumberCommerce = phoneNumberCommerce;
    }

    public String getContactPersonEmail() {
        return contactPersonEmail;
    }

    public void setContactPersonEmail(String contactPersonEmail) {
        this.contactPersonEmail = contactPersonEmail;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
