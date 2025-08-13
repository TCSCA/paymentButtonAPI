package api.loginext.entity;

import com.google.gson.annotations.Expose;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
@Entity
@Table(name = "t_comercio")
public class PreRegistroEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "t_pre_registro_comercio_seq")
    @SequenceGenerator(name = "t_pre_registro_comercio_seq", sequenceName = "t_pre_registro_comercio_seq", allocationSize = 1)
    @Column(name = "id_comercio")
    @Expose
    private Long idPreRegistro;

    @NotNull
    /*@Size(min = 18, max = 65)*/
    @Column(name = "razon_social")
    private String commerceName;

    @NotNull
    @Column(name = "correo")
    private String commerceEmail;

    @NotNull
    @Column(name = "rif")
    private String commerceDocument;

    @NotNull
    @Column(name = "num_telefono")
    private String phoneNumberCommerce;

    @NotNull
    @Column(name = "persona_contacto")
    private String contactPerson;

    @NotNull
    @Column(name = "correo_persona_contacto")
    private String contactPersonEmail;

    @ManyToOne()
    @Expose
    @JoinColumn(name = "id_estado_registro")
    private StatusPreRegistroEntity statusPreRegistroEntity;

    @NotNull
    @ManyToOne()
    @JoinColumn(name = "id_direccion")
    private DirectionEntity directionEntity;

    @Column(name = "fecha_registro")
    private OffsetDateTime registerDate;

    @Column(name = "fecha_actualizacion")
    private OffsetDateTime updateDate;

}

