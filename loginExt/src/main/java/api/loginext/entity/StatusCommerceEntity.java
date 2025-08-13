package api.loginext.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@Table(name = "t_estado_comercio")
public class StatusCommerceEntity {

    @Id
    @Column(name = "id_estado_comercio")
    private Long idStatusPreRegister;

    @Column(name = "estado_comercio")
    private String statusDescription;

    @Column(name = "status")
    private Boolean status;

    public StatusCommerceEntity(Long idStatusPreRegister) {
        this.idStatusPreRegister = idStatusPreRegister;
    }

}
