package api.apiAdminCommerce.to;


public class ProfileTo {

    private Long idProfile;

    private String profileDescription;

    private Boolean status;


    public ProfileTo() {
    }

    public ProfileTo(Long idProfile, String profileDescription, Boolean status) {
        this.idProfile = idProfile;
        this.profileDescription = profileDescription;
        this.status = status;
    }

    public Long getIdProfile() {
        return idProfile;
    }

    public void setIdProfile(Long idProfile) {
        this.idProfile = idProfile;
    }

    public String getProfileDescription() {
        return profileDescription;
    }

    public void setProfileDescription(String profileDescription) {
        this.profileDescription = profileDescription;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }
}
