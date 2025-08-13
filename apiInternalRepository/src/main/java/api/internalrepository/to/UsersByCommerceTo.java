package api.internalrepository.to;

import api.internalrepository.entity.ProfileEntity;
import api.internalrepository.entity.StatusUserEntity;

import java.time.OffsetDateTime;

public class UsersByCommerceTo {

    private Long idUser;

    private String fullName;

    private String phoneNumberClient;

    private String contactPersonEmail;

    private String identificationDocument;

    private String username;

    private ProfileEntity profile;

    private StatusUserEntity statusUserEntity;


    private OffsetDateTime registerDate;


    private String companyName;


    private String rif;


    private String reasonStatus;


    public UsersByCommerceTo() {
    }

    public UsersByCommerceTo(Long idUser, String fullName, String phoneNumberClient, String contactPersonEmail,
                             String identificationDocument, String username, ProfileEntity profile,
                             StatusUserEntity statusUserEntity, OffsetDateTime registerDate, String companyName,
                             String rif, String reasonStatus) {
        this.idUser = idUser;
        this.fullName = fullName;
        this.phoneNumberClient = phoneNumberClient;
        this.contactPersonEmail = contactPersonEmail;
        this.identificationDocument = identificationDocument;
        this.username = username;
        this.profile = profile;
        this.statusUserEntity = statusUserEntity;
        this.registerDate = registerDate;
        this.companyName = companyName;
        this.rif = rif;
        this.reasonStatus = reasonStatus;
    }

    public Long getIdUser() {
        return idUser;
    }

    public void setIdUser(Long idUser) {
        this.idUser = idUser;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhoneNumberClient() {
        return phoneNumberClient;
    }

    public void setPhoneNumberClient(String phoneNumberClient) {
        this.phoneNumberClient = phoneNumberClient;
    }

    public String getContactPersonEmail() {
        return contactPersonEmail;
    }

    public void setContactPersonEmail(String contactPersonEmail) {
        this.contactPersonEmail = contactPersonEmail;
    }

    public String getIdentificationDocument() {
        return identificationDocument;
    }

    public void setIdentificationDocument(String identificationDocument) {
        this.identificationDocument = identificationDocument;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public ProfileEntity getProfile() {
        return profile;
    }

    public void setProfile(ProfileEntity profile) {
        this.profile = profile;
    }

    public StatusUserEntity getStatusUserEntity() {
        return statusUserEntity;
    }

    public void setStatusUserEntity(StatusUserEntity statusUserEntity) {
        this.statusUserEntity = statusUserEntity;
    }

    public OffsetDateTime getRegisterDate() {
        return registerDate;
    }

    public void setRegisterDate(OffsetDateTime registerDate) {
        this.registerDate = registerDate;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getRif() {
        return rif;
    }

    public void setRif(String rif) {
        this.rif = rif;
    }

    public String getReasonStatus() {
        return reasonStatus;
    }

    public void setReasonStatus(String reasonStatus) {
        this.reasonStatus = reasonStatus;
    }
}
