package api.apiAdminCommerce.to;

import api.apiAdminCommerce.entity.DirectionEntity;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.LinkedHashMap;

public class CommerceDetailTo {

    private Long idCommerce;

    private String commerceName;

    private String commerceEmail;

    private String commerceDocument;

    private String contactPerson;

    private String phoneNumberCommerce;

    private String statusDescription;

    private String planName;

    private String address;

    private String typeCommerce;

    private String cityDescription;

    private Long idMunicipality;

    private Long idState;

    private String countryName;

    private OffsetDateTime registerDate;

    private String associatedBank;

    private String accountNumber;

    private Long idStatusCommerce;

    private Long idTypeCommerce;

    private Long idPlan;

    private String unlinkReason;

    private OffsetDateTime unlinkDate;

    private Long idCity;

    private Long idCommerceActivity;

    private OffsetDateTime activationDate;

    public CommerceDetailTo() {
    }

    public CommerceDetailTo(ObjectMapper objectMapper, LinkedHashMap<String, Object> responseMap) {

        LinkedHashMap<String, Object> commerceMap =
                (LinkedHashMap<String, Object>) responseMap.get("commerceEntity");

        LinkedHashMap<String, Object> bankComerceInformation =
                (LinkedHashMap<String, Object>) responseMap.get("bankCommerceEntity");

        LinkedHashMap<String, Object> economicActivity =
                (LinkedHashMap<String, Object>) commerceMap.get("economicActivityEntity");

        if(bankComerceInformation != null) {

            this.accountNumber = bankComerceInformation.get("bankAccount")!= null ? bankComerceInformation.get("bankAccount").toString() : null;
            this.associatedBank = objectMapper.convertValue(((LinkedHashMap<String, Object>) bankComerceInformation.get("bankEntity")).
                    get("bankName"), String.class);
        }

        this.idCommerce = Long.parseLong(commerceMap.get("idCommerce").toString());
        this.commerceName = commerceMap.get("commerceName").toString();
        this.commerceEmail = commerceMap.get("contactPersonEmail").toString() != null ? commerceMap.get("contactPersonEmail").toString() : null;
        this.commerceDocument = commerceMap.get("commerceDocument").toString();
        this.contactPerson = commerceMap.get("contactPerson").toString();
        this.phoneNumberCommerce = commerceMap.get("phoneNumberCommerce").toString();
        this.idCommerceActivity = Long.parseLong(economicActivity.get("idEconomicActivity").toString());

        String registerDateString = (String) commerceMap.get("registerDate");
        this.registerDate = registerDateString != null ? OffsetDateTime.parse(registerDateString) : null;
        String activationDateString = (String) responseMap.get("activationDate");
        this.activationDate = activationDateString != null ?
                OffsetDateTime.parse(activationDateString + "T00:00:00Z") : null;

        this.statusDescription = objectMapper.
                convertValue(((LinkedHashMap<String, Object>) commerceMap.get("statusCommerce")).
                        get("statusDescription"), String.class);

        this.idStatusCommerce = objectMapper.
                convertValue(((LinkedHashMap<String, Object>) commerceMap.get("statusCommerce")).
                        get("idStatusCommerce"), Long.class);

        if (this.idStatusCommerce == 6L){
            if(responseMap.get("unlinkReason") != null) {
                this.unlinkReason = responseMap.get("unlinkReason").toString() != null ? responseMap.
                        get("unlinkReason").toString() : null;

                String unlinkDate = (String) responseMap.get("unlinkDate");
                this.unlinkDate = unlinkDate != null ? OffsetDateTime.parse(unlinkDate) : null;
            }
        }

        this.idPlan = responseMap.get("idPlan") != null ?
                Long.parseLong(responseMap.get("idPlan").toString()) : null;

        this.planName = responseMap.get("planName").toString() != null ? responseMap.
                get("planName").toString() : null;

        LinkedHashMap<String, Object> directionEntityMap =
                (LinkedHashMap<String, Object>) commerceMap.get("directionEntity");

        this.address = directionEntityMap.get("address").toString();

        this.idTypeCommerce = responseMap.get("idTypeCommerce") != null ?
                Long.parseLong(responseMap.get("idTypeCommerce").toString()) : null;

        this.typeCommerce = responseMap.get("typeCommerce") != null ? responseMap.
                get("typeCommerce").toString() : null;

        LinkedHashMap<String, Object> cityEntityMap =
                (LinkedHashMap<String, Object>) directionEntityMap.get("cityEntity");


        if(cityEntityMap != null) {
//            this.cityDescription = cityEntityMap.get("cityDescription").toString();

            this.idCity = cityEntityMap.get("idCity") != null ?
                    Long.parseLong(cityEntityMap.get("idCity").toString()) : null;;

            LinkedHashMap<String, Object> municipalityEntityMap =
                    (LinkedHashMap<String, Object>) cityEntityMap.get("municipalityEntity");

            this.idMunicipality =municipalityEntityMap.get("idMunicipality") != null ?
                    Long.parseLong(municipalityEntityMap.get("idMunicipality").toString()) : null;;

            LinkedHashMap<String, Object> stateEntityMap =
                    (LinkedHashMap<String, Object>) municipalityEntityMap.get("stateEntity");

            this.idState = stateEntityMap.get("idState") != null ?
                    Long.parseLong(stateEntityMap.get("idState").toString()) : null;;

            LinkedHashMap<String, Object> countryEntityMap =
                    (LinkedHashMap<String, Object>) stateEntityMap.get("countryEntity");

            this.countryName = countryEntityMap.get("countryName").toString();
        } else {
            this.cityDescription = "";
            this.idMunicipality = Long.valueOf("");
            this.idState = Long.valueOf("");
            this.countryName = "";
        }
    }

    public Long getIdCommerce() {
        return idCommerce;
    }

    public void setIdCommerce(Long idCommerce) {
        this.idCommerce = idCommerce;
    }

    public String getCommerceName() {
        return commerceName;
    }

    public void setCommerceName(String commerceName) {
        this.commerceName = commerceName;
    }

    public String getCommerceEmail() {
        return commerceEmail;
    }

    public void setCommerceEmail(String commerceEmail) {
        this.commerceEmail = commerceEmail;
    }

    public String getCommerceDocument() {
        return commerceDocument;
    }

    public void setCommerceDocument(String commerceDocument) {
        this.commerceDocument = commerceDocument;
    }

    public String getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    public String getPhoneNumberCommerce() {
        return phoneNumberCommerce;
    }

    public void setPhoneNumberCommerce(String phoneNumberCommerce) {
        this.phoneNumberCommerce = phoneNumberCommerce;
    }

    public String getStatusDescription() {
        return statusDescription;
    }

    public void setStatusDescription(String statusDescription) {
        this.statusDescription = statusDescription;
    }

    public String getPlanName() {
        return planName;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTypeCommerce() {
        return typeCommerce;
    }

    public void setTypeCommerce(String typeCommerce) {
        this.typeCommerce = typeCommerce;
    }

    public String getCityDescription() {
        return cityDescription;
    }

    public void setCityDescription(String cityDescription) {
        this.cityDescription = cityDescription;
    }

    public Long getIdMunicipality() {
        return idMunicipality;
    }

    public void setIdMunicipality(Long idMunicipality) {
        this.idMunicipality = idMunicipality;
    }

    public Long getIdState() {
        return idState;
    }

    public void setIdState(Long idState) {
        this.idState = idState;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public OffsetDateTime getRegisterDate() {
        return registerDate;
    }

    public void setRegisterDate(OffsetDateTime registerDate) {
        this.registerDate = registerDate;
    }

    public String getAssociatedBank() {
        return associatedBank;
    }
    public void setAssociatedBank(String associatedBank) {
        this.associatedBank = associatedBank;
    }
    public String getAccountNumber() {
        return accountNumber;
    }
    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public Long getIdStatusCommerce() {
        return idStatusCommerce;
    }

    public void setIdStatusCommerce(Long idStatusCommerce) {
        this.idStatusCommerce = idStatusCommerce;
    }

    public Long getIdTypeCommerce() {
        return idTypeCommerce;
    }

    public void setIdTypeCommerce(Long idTypeCommerce) {
        this.idTypeCommerce = idTypeCommerce;
    }

    public Long getIdPlan() {
        return idPlan;
    }

    public void setIdPlan(Long idPlan) {
        this.idPlan = idPlan;
    }

    public String getUnlinkReason() {
        return unlinkReason;
    }

    public void setUnlinkReason(String unlinkReason) {
        this.unlinkReason = unlinkReason;
    }

    public OffsetDateTime getUnlinkDate() {
        return unlinkDate;
    }

    public void setUnlinkDate(OffsetDateTime unlinkDate) {
        this.unlinkDate = unlinkDate;
    }

    public Long getIdCity() {
        return idCity;
    }

    public void setIdCity(Long idCity) {
        this.idCity = idCity;
    }

    public Long getIdCommerceActivity() {
        return idCommerceActivity;
    }

    public void setIdCommerceActivity(Long idCommerceActivity) {
        this.idCommerceActivity = idCommerceActivity;
    }


}
