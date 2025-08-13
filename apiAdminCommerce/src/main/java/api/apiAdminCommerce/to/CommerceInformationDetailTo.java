package api.apiAdminCommerce.to;

import api.apiAdminCommerce.entity.CityEntity;
import api.apiAdminCommerce.entity.CountryEntity;
import api.apiAdminCommerce.entity.MunicipalityEntity;
import api.apiAdminCommerce.entity.StateEntity;

import java.time.OffsetDateTime;

public class CommerceInformationDetailTo {

    private Long idCommerce;

    private OffsetDateTime registerDate;

    private OffsetDateTime desvinculacionDate;

    private String statusDescription;

    private String commerceDocument;

    private String commerceName;

    private String contactPerson;

    private String phoneNumberCommerce;

    private String commerceEmail;

    private CountryEntity countryEntity;

    private StateEntity stateEntity;

    private MunicipalityEntity municipalityEntity;

    private CityEntity cityEntity;

    private String address;


    private String bankAccount;


    private String associatedBank;


    private String typeCommerce;


    private String planName;

    public CommerceInformationDetailTo(Long idCommerce, OffsetDateTime registerDate,
                                       OffsetDateTime desvinculacionDate, String statusDescription,
                                       String commerceDocument, String commerceName,
                                       String contactPerson, String phoneNumberCommerce,
                                       String commerceEmail, CountryEntity countryEntity,
                                       StateEntity stateEntity, MunicipalityEntity municipalityEntity,
                                       CityEntity cityEntity, String address) {
        this.idCommerce = idCommerce;
        this.registerDate = registerDate;
        this.desvinculacionDate = desvinculacionDate;
        this.statusDescription = statusDescription;
        this.commerceDocument = commerceDocument;
        this.commerceName = commerceName;
        this.contactPerson = contactPerson;
        this.phoneNumberCommerce = phoneNumberCommerce;
        this.commerceEmail = commerceEmail;
        this.countryEntity = countryEntity;
        this.stateEntity = stateEntity;
        this.municipalityEntity = municipalityEntity;
        this.cityEntity = cityEntity;
        this.address = address;
    }

    public CommerceInformationDetailTo(Long idCommerce, OffsetDateTime registerDate,
                                       String statusDescription, String commerceDocument,
                                       String commerceName, String contactPerson,
                                       String phoneNumberCommerce, String commerceEmail,
                                       CityEntity cityEntity, String address,
                                       String typeCommerce, String planName) {
        this.idCommerce = idCommerce;
        this.registerDate = registerDate;
        this.statusDescription = statusDescription;
        this.commerceDocument = commerceDocument;
        this.commerceName = commerceName;
        this.contactPerson = contactPerson;
        this.phoneNumberCommerce = phoneNumberCommerce;
        this.commerceEmail = commerceEmail;
        this.cityEntity = cityEntity;
        this.address = address;
        this.typeCommerce = typeCommerce;
        this.planName = planName;
    }

    public CommerceInformationDetailTo(Long idCommerce, OffsetDateTime registerDate,
                                       String statusDescription, String commerceDocument,
                                       String commerceName, String contactPerson,
                                       String phoneNumberCommerce, String commerceEmail,
                                       CityEntity cityEntity, String address,
                                       String bankAccount, String associatedBank,
                                       String typeCommerce, String planName) {
        this.idCommerce = idCommerce;
        this.registerDate = registerDate;
        this.statusDescription = statusDescription;
        this.commerceDocument = commerceDocument;
        this.commerceName = commerceName;
        this.contactPerson = contactPerson;
        this.phoneNumberCommerce = phoneNumberCommerce;
        this.commerceEmail = commerceEmail;
        this.cityEntity = cityEntity;
        this.address = address;
        this.bankAccount = bankAccount;
        this.associatedBank = associatedBank;
        this.typeCommerce = typeCommerce;
        this.planName = planName;
    }

    public CommerceInformationDetailTo(Long idCommerce, OffsetDateTime registerDate, String statusDescription, String commerceDocument, String commerceName, String contactPerson, String phoneNumberCommerce, String commerceEmail, CountryEntity countryEntity, StateEntity stateEntity, MunicipalityEntity municipalityEntity, CityEntity cityEntity, String address) {
        this.idCommerce = idCommerce;
        this.registerDate = registerDate;
        this.statusDescription = statusDescription;
        this.commerceDocument = commerceDocument;
        this.commerceName = commerceName;
        this.contactPerson = contactPerson;
        this.phoneNumberCommerce = phoneNumberCommerce;
        this.commerceEmail = commerceEmail;
        this.countryEntity = countryEntity;
        this.stateEntity = stateEntity;
        this.municipalityEntity = municipalityEntity;
        this.cityEntity = cityEntity;
        this.address = address;
    }

    public Long getIdCommerce() {
        return idCommerce;
    }

    public void setIdCommerce(Long idCommerce) {
        this.idCommerce = idCommerce;
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

    public CountryEntity getCountryEntity() {
        return countryEntity;
    }

    public void setCountryEntity(CountryEntity countryEntity) {
        this.countryEntity = countryEntity;
    }

    public StateEntity getStateEntity() {
        return stateEntity;
    }

    public void setStateEntity(StateEntity stateEntity) {
        this.stateEntity = stateEntity;
    }

    public MunicipalityEntity getMunicipalityEntity() {
        return municipalityEntity;
    }

    public void setMunicipalityEntity(MunicipalityEntity municipalityEntity) {
        this.municipalityEntity = municipalityEntity;
    }

    public CityEntity getCityEntity() {
        return cityEntity;
    }

    public void setCityEntity(CityEntity cityEntity) {
        this.cityEntity = cityEntity;
    }

    public String getStatusDescription() {
        return statusDescription;
    }

    public void setStatusDescription(String statusDescription) {
        this.statusDescription = statusDescription;
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

    public String getCommerceEmail() {
        return commerceEmail;
    }

    public void setCommerceEmail(String commerceEmail) {
        this.commerceEmail = commerceEmail;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount;
    }

    public String getAssociatedBank() {
        return associatedBank;
    }

    public void setAssociatedBank(String associatedBank) {
        this.associatedBank = associatedBank;
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
}
