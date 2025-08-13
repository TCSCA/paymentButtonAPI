package api.externalrepository.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "t_estado_registro")
public class StatusCommerceEntity {

    @Id
    @Column(name = "id_estado_registro")
    private Long idStatusCommerce;

    @Column(name = "estado_registro")
    private String statusDescription;

    @Column(name = "status")
    private Boolean status;

    public StatusCommerceEntity() {
    }

    public StatusCommerceEntity(Long idStatusPreRegister) {
        this.idStatusCommerce = idStatusPreRegister;
    }

    public Long getIdStatusCommerce() {
        return idStatusCommerce;
    }

    public void setIdStatusCommerce(Long idStatusPreRegister) {
        this.idStatusCommerce = idStatusPreRegister;
    }

    public String getStatusDescription() {
        return statusDescription;
    }

    public void setStatusDescription(String statusDescription) {
        this.statusDescription = statusDescription;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }
}
