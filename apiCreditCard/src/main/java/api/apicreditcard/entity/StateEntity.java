package api.apicreditcard.entity;

import com.google.gson.annotations.Expose;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "t_estado")
public class StateEntity {

    @Id
    @Column(name = "id_estado")
    @Expose
    private Long idState;

    @Column(name = "estado")
    private String stateName;

    @ManyToOne()
    @JoinColumn(name = "id_pais")
    private CountryEntity countryEntity;

    @Column(name = "status")
    private Boolean status;
}
