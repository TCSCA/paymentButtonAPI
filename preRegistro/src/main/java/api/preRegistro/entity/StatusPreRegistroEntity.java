package api.preRegistro.entity;

import com.google.gson.annotations.Expose;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@Table(name = "t_estado_registro")
public class StatusPreRegistroEntity {

    @Id
    @Column(name = "id_estado_registro")
    private Long idStatusPreRegistro;

    @Column(name = "estado_registro")
    private String statusDescription;

    @Column(name = "status")
    private Boolean status;

    public StatusPreRegistroEntity(Long idStatusPreRegistro) {
        this.idStatusPreRegistro = idStatusPreRegistro;
    }

}
