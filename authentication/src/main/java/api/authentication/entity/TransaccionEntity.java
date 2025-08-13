package api.authentication.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "t_transaccion")
public class TransaccionEntity {

    @Id
    @Column(name = "id_transaccion")
    private Long idTransaccion;

    @Column(name = "transaccion")
    private String transaccion;

    @ManyToOne
    @JoinColumn(name = "id_seccion")
    private SeccionEntity seccionEntity;

    public Long getIdTransaccion() {
        return idTransaccion;
    }

    public void setIdTransaccion(Long idTransaccion) {
        this.idTransaccion = idTransaccion;
    }

    public String getTransaccion() {
        return transaccion;
    }

    public void setTransaccion(String transaccion) {
        this.transaccion = transaccion;
    }

    public SeccionEntity getSeccionEntity() {
        return seccionEntity;
    }

    public void setSeccionEntity(SeccionEntity seccionEntity) {
        this.seccionEntity = seccionEntity;
    }
}

