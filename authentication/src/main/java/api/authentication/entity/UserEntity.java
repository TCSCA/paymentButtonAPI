package api.authentication.entity;

import com.google.gson.annotations.Expose;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@Table(name = "t_usuario")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "t_usuario_seq")
    @SequenceGenerator(name = "t_usuario_seq", sequenceName = "t_usuario_seq", allocationSize = 1)
    @Column(name = "id_usuario")
    @Expose
    private Long idUser;

    @NotNull
    @Column(name = "username")
    private String userName;

    @NotNull
    @Column(name = "password")
    private String password;

    @ManyToOne()
    @Expose
    @JoinColumn(name = "id_estado_usuario")
    private StatusUserEntity statusUserEntity;

    @ManyToOne()
    @Expose
    @JoinColumn(name = "id_perfil")
    private ProfileEntity profileEntity;

    @Transient
    private String token;

    public UserEntity(Long idUser) {
        this.idUser = idUser;
    }
}
