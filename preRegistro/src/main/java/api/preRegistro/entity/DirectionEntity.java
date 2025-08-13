package api.preRegistro.entity;

import com.google.gson.annotations.Expose;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Entity
@Table(name = "t_direccion")
public class DirectionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "t_direccion_comercio_seq")
    @SequenceGenerator(name = "t_direccion_comercio_seq", sequenceName = "t_direccion_comercio_seq", allocationSize = 1)
    @Column(name = "id_direccion")
    @Expose
    private Long idDirection;

    @NotNull
    @Column(name = "direccion_fiscal")
    private String address;

    @ManyToOne()
    @JoinColumn(name = "id_estado")
    private StateEntity stateEntity;

    @Column(name = "status")
    private Boolean status;

}
