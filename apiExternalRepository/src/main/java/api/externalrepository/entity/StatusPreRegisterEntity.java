package api.externalrepository.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "t_estado_registro")
public class StatusPreRegisterEntity {

    @Id
    @Column(name = "id_estado_registro")
    private Long idStatusPreRegister;

    @Column(name = "estado_registro")
    private String statusDescription;

    @Column(name = "status")
    private Boolean status;

    public StatusPreRegisterEntity() {
    }

    public StatusPreRegisterEntity(Long idStatusPreRegister) {
        this.idStatusPreRegister = idStatusPreRegister;
    }

    public Long getIdStatusPreRegister() {
        return idStatusPreRegister;
    }

    public void setIdStatusPreRegister(Long idStatusPreRegister) {
        this.idStatusPreRegister = idStatusPreRegister;
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
