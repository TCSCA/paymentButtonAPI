package api.authentication.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "t_menu_profile")
public class MenuProfileEntity {

    @Id
    @Column(name = "id_menu_profile")
    private Long idMenuProfile;

    @ManyToOne
    @JoinColumn(name = "id_menu")
    private MenuEntity menuEntity;

    @ManyToOne
    @JoinColumn(name = "id_perfil")
    private ProfileEntity profileEntity;

    @Column(name = "status")
    private Boolean status;

}
