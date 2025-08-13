package api.apiB2p.entity;

import com.google.gson.annotations.Expose;
import jakarta.persistence.*;
import lombok.Data;


@Data
@Entity
@Table(name = "t_municipio")
public class MunicipalityEntity {

    @Id
    @Column(name = "id_municipio")
    @Expose
    private Long idMunicipality;

    @Column(name = "municipio")
    private String municipalityName;

    @ManyToOne()
    @JoinColumn(name = "id_estado")
    private StateEntity stateEntity;

    @Column(name = "status")
    private Boolean status;
}
