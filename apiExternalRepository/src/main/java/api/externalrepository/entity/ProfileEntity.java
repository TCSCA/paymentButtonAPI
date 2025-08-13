package api.externalrepository.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "t_perfil")
public class ProfileEntity {

    @Id
    @Column(name = "id_perfil")
    private Long idProfile;

    @Column(name = "perfil")
    private String profileDescription;

    @Column(name = "status")
    private Boolean status;

    public ProfileEntity() {
    }

    public ProfileEntity(Long idProfile) {
        this.idProfile = idProfile;
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
