package api.internalrepository.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "t_actividad_comercial")
public class EconomicActivityEntity {
    @Id
    @Column(name = "id_actividad_comercial")
    private Long idEconomicActivity;

    @Column(name = "actividad_comercial")
    private String economicActivityDescription;

    @Column(name = "status")
    private Boolean status;

    public EconomicActivityEntity() {
    }

    public EconomicActivityEntity(Long idEconomicActivity) {
        this.idEconomicActivity = idEconomicActivity;
    }

    public Long getIdEconomicActivity() {
        return idEconomicActivity;
    }

    public void setIdEconomicActivity(Long idEconomicActivity) {
        this.idEconomicActivity = idEconomicActivity;
    }

    public String getEconomicActivityDescription() {
        return economicActivityDescription;
    }

    public void setEconomicActivityDescription(String economicActivityDescription) {
        this.economicActivityDescription = economicActivityDescription;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }
}
