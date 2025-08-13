package api.apiAdminCommerce.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

import java.time.OffsetDateTime;

@Entity
@Table(name = "t_historia_upass")
public class HistoryPasswordEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "t_historia_upass_seq")
    @SequenceGenerator(name = "t_historia_upass_seq", sequenceName = "t_historia_upass_seq", allocationSize = 1)
    @Column(name = "id_historia_upass")
    private Long idHistoryPassword;

    @Column(name = "password")
    private String password;

    @Column(name = "fecha_registro")
    private OffsetDateTime registerDate;

    @Column(name = "fecha_expiracion")
    private OffsetDateTime expirationDate;

    @ManyToOne
    @JoinColumn(name = "id_usuario")
    private UserEntityExt userEntity;

    public HistoryPasswordEntity() {
    }

    public HistoryPasswordEntity(Long idHistoryPassword, String password, OffsetDateTime registerDate, OffsetDateTime expirationDate, UserEntityExt userEntity) {
        this.idHistoryPassword = idHistoryPassword;
        this.password = password;
        this.registerDate = registerDate;
        this.expirationDate = expirationDate;
        this.userEntity = userEntity;
    }

    public Long getIdHistoryPassword() {
        return idHistoryPassword;
    }

    public void setIdHistoryPassword(Long idHistoryPassword) {
        this.idHistoryPassword = idHistoryPassword;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public UserEntityExt getUserEntity() {
        return userEntity;
    }

    public void setUserEntity(UserEntityExt userEntity) {
        this.userEntity = userEntity;
    }
}
