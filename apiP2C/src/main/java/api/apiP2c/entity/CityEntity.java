package api.apiP2c.entity;

import com.google.gson.annotations.Expose;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@Table(name = "t_ciudad")
public class CityEntity {

    @Id
    @Column(name = "id_ciudad")
    @Expose
    private Long idCity;

    @Column(name = "ciudad")
    private String cityDescription;

    @ManyToOne()
    @JoinColumn(name = "id_municipio")
    private MunicipalityEntity municipalityEntity;

    @Column(name = "status")
    private Boolean status;

    public CityEntity(Long idCity) {
        this.idCity = idCity;
    }

}
