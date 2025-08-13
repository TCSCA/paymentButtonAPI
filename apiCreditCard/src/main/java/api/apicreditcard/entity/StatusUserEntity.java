package api.apicreditcard.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
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
}
