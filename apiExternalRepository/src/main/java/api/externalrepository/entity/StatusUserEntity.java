package api.externalrepository.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "t_estado_usuario")
public class StatusUserEntity {

    @Id
    @Column(name = "id_estado_usuario")
    private Long idStatusUser;

    @Column(name = "estado_usuario")
    private String statusUserDescription;

    @Column(name = "status")
    private Boolean status;

    public StatusUserEntity() {
    }

    public StatusUserEntity(Long idStatusUser) {
        this.idStatusUser = idStatusUser;
    }

    public Long getIdStatusUser() {
        return idStatusUser;
    }

    public void setIdStatusUser(Long idStatusUser) {
        this.idStatusUser = idStatusUser;
    }

    public String getStatusUserDescription() {
        return statusUserDescription;
    }

    public void setStatusUserDescription(String statusUserDescription) {
        this.statusUserDescription = statusUserDescription;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }
}
