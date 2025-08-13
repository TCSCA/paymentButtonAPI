package api.externalrepository.to;


import api.externalrepository.entity.ProfileEntity;
import api.externalrepository.entity.StatusUserEntity;

public class UsersBlockedTo {

    private Long idUser;

    private String username;

    private ProfileEntity profile;

    private StatusUserEntity statusUserEntity;

    public UsersBlockedTo() {
    }

    public UsersBlockedTo(Long idUser, String username,
                          ProfileEntity profile, StatusUserEntity statusUserEntity) {
        this.idUser = idUser;
        this.username = username;
        this.profile = profile;
        this.statusUserEntity = statusUserEntity;
    }

    public Long getIdUser() {
        return idUser;
    }

    public void setIdUser(Long idUser) {
        this.idUser = idUser;
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
}
