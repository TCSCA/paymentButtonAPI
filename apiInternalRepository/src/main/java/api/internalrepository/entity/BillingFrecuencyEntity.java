package api.internalrepository.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "t_periodo_facturacion")
public class BillingFrecuencyEntity {

    @Id
    @Column(name = "id_periodo_facturacion")
    private Long idBillingFrecuency;

    @Column(name = "descripcion")
    private String description;

    @Column(name = "status")
    private Boolean status;

    public Long getIdBillingFrecuency() {
        return idBillingFrecuency;
    }

    public void setIdBillingFrecuency(Long idBillingFrecuency) {
        this.idBillingFrecuency = idBillingFrecuency;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }
}
