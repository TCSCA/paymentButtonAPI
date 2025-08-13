package api.authentication.entity;

import com.google.gson.annotations.Expose;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Entity
@NoArgsConstructor
@Table(name = "t_administrativo")
public class AdministrativeUserEntity {

    @Id
    @Column(name = "id_administrativo")
    @Expose
    private Long idAdministrativeUser;

    @Column(name = "nombre")
    private String name;

    @Column(name = "cedula")
    private String document;

    @Column(name = "numero_telefono")
    private String phoneNumber;

    @Column(name = "correo")
    private String email;

    @ManyToOne()
    @Expose
    @JoinColumn(name = "id_usuario")
    private UserEntity userEntity;

    @Column(name = "fecha_registro")
    private LocalDate registerDate;

    @Column(name = "activo")
    private Boolean active;
}
