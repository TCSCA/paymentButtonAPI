package api.externalrepository.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

import java.time.OffsetDateTime;

@Entity
@Table(name = "t_usuario")
public class UserEntityExt {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "t_usuario_ext_seq")
    @SequenceGenerator(name = "t_usuario_ext_seq", sequenceName = "t_usuario_ext_seq", allocationSize = 1)
    @Column(name = "id_usuario")
    private Long idUser;

    @Column(name = "username")
    private String userName;

    @Column(name = "primer_ingreso")
    private Boolean firstLogin;

    @ManyToOne()
    @JoinColumn(name = "id_estado_usuario")
    private StatusUserEntity statusUserEntity;

    @ManyToOne()
    @JoinColumn(name = "id_perfil")
    private ProfileEntity profileEntity;

    @Column(name = "intentos_fallidos")
    private Integer failedAttempts;

    @Column(name = "clave_temporal")
    private String temporalPassword;

    @Column(name = "correo")
    private String email;

    @Column(name = "update_date")
    private OffsetDateTime updateDate;

    @Transient
    private String token;

    public UserEntityExt() {
    }

    public UserEntityExt(Long idUser) {
        this.idUser = idUser;
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

    public Integer getFailedAttempts() {
        return failedAttempts;
    }

    public void setFailedAttempts(Integer failedAttempts) {
        this.failedAttempts = failedAttempts;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTemporalPassword() {
        return temporalPassword;
    }

    public void setTemporalPassword(String temporalPassword) {
        this.temporalPassword = temporalPassword;
    }

    public OffsetDateTime getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(OffsetDateTime updateDate) {
        this.updateDate = updateDate;
    }
}
