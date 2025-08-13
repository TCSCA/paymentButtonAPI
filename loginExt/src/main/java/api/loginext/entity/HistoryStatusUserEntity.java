package api.loginext.entity;

import jakarta.persistence.*;

import java.time.OffsetDateTime;

@Entity
@Table(name = "t_historia_estado_usuario")
public class HistoryStatusUserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "t_historia_estado_usuario_seq")
    @SequenceGenerator(name = "t_historia_estado_usuario_seq", sequenceName = "t_historia_estado_usuario_seq", allocationSize = 1)
    @Column(name = "id_historia_estado_usuario")
    private Long idHistoryStatusUser;

    @ManyToOne()
    @JoinColumn(name = "id_usuario")
    private UserEntity userEntity;

    @ManyToOne()
    @JoinColumn(name = "id_estado_usuario")
    private StatusUserEntity statusUserEntity;

    @Column(name = "motivo_estado")
    private String reasonStatus;

    @Column(name = "fecha_registro")
    private OffsetDateTime registerDate;

    public HistoryStatusUserEntity() {
    }

    public Long getIdHistoryStatusUser() {
        return idHistoryStatusUser;
    }

    public void setIdHistoryStatusUser(Long idHistoryStatusUser) {
        this.idHistoryStatusUser = idHistoryStatusUser;
    }

    public UserEntity getUserEntity() {
        return userEntity;
    }

    public void setUserEntity(UserEntity userEntity) {
        this.userEntity = userEntity;
    }

    public StatusUserEntity getStatusUserEntity() {
        return statusUserEntity;
    }

    public void setStatusUserEntity(StatusUserEntity statusUserEntity) {
        this.statusUserEntity = statusUserEntity;
    }

    public String getReasonStatus() {
        return reasonStatus;
    }

    public void setReasonStatus(String reasonStatus) {
        this.reasonStatus = reasonStatus;
    }

    public OffsetDateTime getRegisterDate() {
        return registerDate;
    }

    public void setRegisterDate(OffsetDateTime registerDate) {
        this.registerDate = registerDate;
    }
}
