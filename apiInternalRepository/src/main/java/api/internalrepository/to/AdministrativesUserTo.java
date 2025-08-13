package api.internalrepository.to;

import api.internalrepository.entity.ProfileEntity;
import api.internalrepository.entity.StatusUserEntity;

import java.time.OffsetDateTime;

public class AdministrativesUserTo {

    private Long idUser;

    private String fullName;

    private String phoneNumber;

    private String email;

    private String identificationDocument;

    private String username;

    private ProfileEntity profile;

    private StatusUserEntity statusUserEntity;

    private OffsetDateTime registerDate;

    private String reasonStatus;

    private String companyName;

    public AdministrativesUserTo(Long idUser, String fullName, String phoneNumber, String email, String identificationDocument,
                                 String username, ProfileEntity profile, StatusUserEntity statusUserEntity,
                                 OffsetDateTime registerDate, String reasonStatus,String companyName) {
        this.idUser = idUser;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.identificationDocument = identificationDocument;
        this.username = username;
        this.profile = profile;
        this.statusUserEntity = statusUserEntity;
        this.registerDate = registerDate;
        this.reasonStatus = reasonStatus;
        this.companyName = companyName;
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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getReasonStatus() {
        return reasonStatus;
    }

    public void setReasonStatus(String reasonStatus) {
        this.reasonStatus = reasonStatus;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
}
