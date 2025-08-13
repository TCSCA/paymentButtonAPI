package api.authentication.entity;

import com.google.gson.annotations.Expose;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@Entity
@NoArgsConstructor
@Table(name = "t_token")
public class TokenEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "t_token_seq")
    @SequenceGenerator(name = "t_token_seq", sequenceName = "t_token_seq", allocationSize = 1)
    @Column(name = "id_token")
    @Expose
    private Long idUser;

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
    private Boolean status;
}
