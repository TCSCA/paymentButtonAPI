package api.loginext.entity;

import com.google.gson.annotations.Expose;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
@Entity
@Table(name = "t_historia_upass")
public class HistoryPasswordEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "t_historia_upass_seq")
    @SequenceGenerator(name = "t_historia_upass_seq", sequenceName = "t_historia_upass_seq", allocationSize = 1)
    @Column(name = "id_historia_upass")
    @Expose
    private Long idHistoryPassword;

    @NotNull
    @Column(name = "password")
    private String password;

    @Column(name = "fecha_registro")
    private OffsetDateTime registerDate;

    @Column(name = "fecha_expiracion")
    private OffsetDateTime exipartionDate;

    @ManyToOne()
    @Expose
    @JoinColumn(name = "id_usuario")
    private UserEntity userEntity;
}
