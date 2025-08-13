package api.internalrepository.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "t_ciudad")
public class CityEntity {

    @Id
    @Column(name = "id_ciudad")
    private Long idCity;

    @Column(name = "ciudad")
    private String cityDescription;

    @ManyToOne()
    @JoinColumn(name = "id_municipio")
    private MunicipalityEntity municipalityEntity;

    @Column(name = "status")
    private Boolean status;

    public CityEntity() {
    }

    public CityEntity(Long idCity) {
        this.idCity = idCity;
    }

    public Long getIdCity() {
        return idCity;
    }

    public void setIdCity(Long idCity) {
        this.idCity = idCity;
    }

    public String getCityDescription() {
        return cityDescription;
    }

    public void setCityDescription(String cityDescription) {
        this.cityDescription = cityDescription;
    }

    public MunicipalityEntity getMunicipalityEntity() {
        return municipalityEntity;
    }

    public void setMunicipalityEntity(MunicipalityEntity municipalityEntity) {
        this.municipalityEntity = municipalityEntity;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }
}
