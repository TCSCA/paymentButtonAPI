package api.internalrepository.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@Table(name = "t_perfil")
public class ProfileEntity {

    @Id
    @Column(name = "id_perfil")
    private Long idProfile;

    @Column(name = "perfil")
    private String profileDescription;

    @Column(name = "status")
    private Boolean status;

    @ManyToOne()
    @JoinColumn(name = "id_tipo_perfil")
    private TypeProfileEntity typeProfileEntity;


    public ProfileEntity(Long idProfile) {
        this.idProfile = idProfile;
    }
}
