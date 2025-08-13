package api.apiAdminCommerce.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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

    public ProfileEntity(Long idProfile) {
        this.idProfile = idProfile;
    }
}
