package api.externalrepository.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "t_municipio")
public class MunicipalityEntity {

    @Id
    @Column(name = "id_municipio")
    private Long idMunicipality;

    @Column(name = "municipio")
    private String municipalityName;

    @ManyToOne()
    @JoinColumn(name = "id_estado")
    private StateEntity stateEntity;

    @Column(name = "status")
    private Boolean status;

    public MunicipalityEntity() {
    }

    public Long getIdMunicipality() {
        return idMunicipality;
    }

    public void setIdMunicipality(Long idMunicipality) {
        this.idMunicipality = idMunicipality;
    }

    public String getMunicipalityName() {
        return municipalityName;
    }

    public void setMunicipalityName(String municipalityName) {
        this.municipalityName = municipalityName;
    }

    public StateEntity getStateEntity() {
        return stateEntity;
    }

    public void setStateEntity(StateEntity stateEntity) {
        this.stateEntity = stateEntity;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }
}
