package api.internalrepository.entity;

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

@Entity
@Table(name = "t_usuario")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "t_usuario_seq")
    @SequenceGenerator(name = "t_usuario_seq", sequenceName = "t_usuario_seq", allocationSize = 1)
    @Column(name = "id_usuario")
    private Long idUser;

    @Column(name = "username")
    private String userName;

    @Column(name = "password")
    private String password;

    @ManyToOne
    @JoinColumn(name = "id_estado_usuario")
    private StatusUserEntity statusUserEntity;

    @ManyToOne
    @JoinColumn(name = "id_perfil")
    private ProfileEntity profileEntity;

    @Column(name = "primer_ingreso")
    private Boolean firstLogin;

    @Transient
    private String token;

    public UserEntity() {
    }

    public UserEntity(Long idUser) {
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public Boolean getFirstLogin() {
        return firstLogin;
    }

    public void setFirstLogin(Boolean firstLogin) {
        this.firstLogin = firstLogin;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
