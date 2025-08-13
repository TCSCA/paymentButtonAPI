package api.authentication.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "t_transaccion_usuario")
public class TransaccionUsuarioEntity {

    @Id
    @Column(name = "id_transaccion_usuario")
    private Long idTransaccionUsuario;

    @ManyToOne
    @JoinColumn(name = "id_transaccion")
    private TransaccionEntity transaccionEntity;

    @ManyToOne
    @JoinColumn(name = "id_usuario")
    private UserEntity userEntity;

    @Column(name = "status")
    private Boolean status;

    public Long getIdTransaccionUsuario() {
        return idTransaccionUsuario;
    }

    public void setIdTransaccionUsuario(Long idTransaccionPerfil) {
        this.idTransaccionUsuario = idTransaccionPerfil;
    }

    public TransaccionEntity getTransaccionEntity() {
        return transaccionEntity;
    }

    public void setTransaccionEntity(TransaccionEntity transaccionEntity) {
        this.transaccionEntity = transaccionEntity;
    }

    public UserEntity getUserEntity() {
        return userEntity;
    }

    public void setUserEntity(UserEntity userEntity) {
        this.userEntity = userEntity;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }
}
