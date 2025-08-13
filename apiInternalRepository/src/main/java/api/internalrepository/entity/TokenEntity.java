package api.internalrepository.entity;

import jakarta.persistence.*;

import java.time.OffsetDateTime;

@Entity
@Table(name = "t_token")
public class TokenEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "t_token_seq")
    @SequenceGenerator(name = "t_token_seq", sequenceName = "t_token_seq", allocationSize = 1)
    @Column(name = "id_token")
    private Long idToken;

    @Column(name = "token")
    private String token;

    @Column(name = "fecha_registro")
    private OffsetDateTime registerDate;

    @Column(name = "fecha_expiracion")
    private OffsetDateTime expirationDate;

    @ManyToOne()
    @JoinColumn(name = "id_usuario")
    private UserEntity userEntity;

    @Column(name = "activo")
    private Boolean active;

    public TokenEntity() {
    }

    public TokenEntity(Long idToken, String token, OffsetDateTime registerDate, OffsetDateTime expirationDate, UserEntity userEntity, Boolean active) {
        this.idToken = idToken;
        this.token = token;
        this.registerDate = registerDate;
        this.expirationDate = expirationDate;
        this.userEntity = userEntity;
        this.active = active;
    }

    public Long getIdToken() {
        return idToken;
    }

    public void setIdToken(Long idToken) {
        this.idToken = idToken;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public OffsetDateTime getRegisterDate() {
        return registerDate;
    }

    public void setRegisterDate(OffsetDateTime registerDate) {
        this.registerDate = registerDate;
    }

    public OffsetDateTime getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(OffsetDateTime expirationDate) {
        this.expirationDate = expirationDate;
    }

    public UserEntity getUserEntity() {
        return userEntity;
    }

    public void setUserEntity(UserEntity userEntity) {
        this.userEntity = userEntity;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}
