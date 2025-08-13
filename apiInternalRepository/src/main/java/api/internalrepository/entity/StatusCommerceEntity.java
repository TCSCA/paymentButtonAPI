package api.internalrepository.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "t_estado_comercio")
public class StatusCommerceEntity {

    @Id
    @Column(name = "id_estado_comercio")
    private Long idStatusCommerce;

    @Column(name = "estado_comercio")
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
