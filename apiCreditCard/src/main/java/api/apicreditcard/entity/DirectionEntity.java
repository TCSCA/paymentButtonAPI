package api.apicreditcard.entity;

import com.google.gson.annotations.Expose;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Entity
@Table(name = "t_direccion")
public class DirectionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "t_direccion_seq")
    @SequenceGenerator(name = "t_direccion_seq", sequenceName = "t_direccion_seq", allocationSize = 1)
    @Column(name = "id_direccion")
    @Expose
    private Long idDirection;

    @NotNull
    @Column(name = "direccion_fiscal")
    private String address;

    @ManyToOne()
    @Expose
    @JoinColumn(name = "id_ciudad")
    private CityEntity cityEntity;

    @Column(name = "status")
    private Boolean status;
}
