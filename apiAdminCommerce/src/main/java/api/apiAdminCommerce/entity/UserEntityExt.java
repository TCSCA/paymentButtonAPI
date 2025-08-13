package api.apiAdminCommerce.entity;

import com.google.gson.annotations.Expose;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "t_usuario")
public class UserEntityExt {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "t_usuario_ext_seq")
    @SequenceGenerator(name = "t_usuario_ext_seq", sequenceName = "t_usuario_ext_seq", allocationSize = 1)
    @Column(name = "id_usuario")
    @Expose
    private Long idUser;

    @Column(name = "username")
    private String userName;

    @Column(name = "primer_ingreso")
    private Boolean firstLogin;

    @ManyToOne()
    @Expose
    @JoinColumn(name = "id_estado_usuario")
    private StatusUserEntity statusUserEntity;

    @ManyToOne()
    @Expose
    @JoinColumn(name = "id_perfil")
    private ProfileEntity profileEntity;

    @Transient
    private String token;

    @Column(name = "clave_temporal")
    private String temporalPassword;

    public UserEntityExt() {
    }

    public UserEntityExt(Long idUser) {
        this.idUser = idUser;
    }

    public UserEntityExt(Long idUser, String userName, Boolean firstLogin, StatusUserEntity statusUserEntity, ProfileEntity profileEntity, String token, String temporalPassword) {
        this.idUser = idUser;
        this.userName = userName;
        this.firstLogin = firstLogin;
        this.statusUserEntity = statusUserEntity;
        this.profileEntity = profileEntity;
        this.token = token;
        this.temporalPassword = temporalPassword;
    }

    public Long getIdUser() {
        return idUser;
    }

    public void setIdUser(Long idUser) {
        this.idUser = idUser;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Boolean getFirstLogin() {
        return firstLogin;
    }

    public void setFirstLogin(Boolean firstLogin) {
        this.firstLogin = firstLogin;
    }

    public StatusUserEntity getStatusUserEntity() {
        return statusUserEntity;
    }

    public void setStatusUserEntity(StatusUserEntity statusUserEntity) {
        this.statusUserEntity = statusUserEntity;
    }

    public ProfileEntity getProfileEntity() {
        return profileEntity;
    }

    public void setProfileEntity(ProfileEntity profileEntity) {
        this.profileEntity = profileEntity;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getTemporalPassword() {
        return temporalPassword;
    }

    public void setTemporalPassword(String temporalPassword) {
        this.temporalPassword = temporalPassword;
    }
}
