package api.apicreditcard.entity;

import com.google.gson.annotations.Expose;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@Entity
@Table(name = "t_token")
@NoArgsConstructor
public class TokenEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "t_usuario_seq")
    @SequenceGenerator(name = "t_usuario_seq", sequenceName = "t_usuario_seq", allocationSize = 1)
    @Column(name = "id_token")
    @Expose
    private Long idToken;

    @Column(name = "token")
    private String token;

    @Column(name = "fecha_registro")
    private OffsetDateTime registerDate;

    @Column(name = "fecha_expiracion")
    private OffsetDateTime expirationDate;

    @ManyToOne()
    @Expose
    @JoinColumn(name = "id_usuario")
    private UserEntity userEntity;

    @Column(name = "activo")
    private Boolean active;
}
