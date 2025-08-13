package api.loginext.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@Table(name = "t_actividad_comercial")
public class EconomicActivityEntity {
    @Id
    @Column(name = "id_actividad_comercial")
    private Long idEconomicActivity;

    @Column(name = "actividad_comercial")
    private String economicActivityDescription;

    @Column(name = "status")
    private Boolean status;

    public EconomicActivityEntity(Long idEconomicActivity) {
        this.idEconomicActivity = idEconomicActivity;
    }

}
