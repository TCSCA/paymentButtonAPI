package api.loginext.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "t_pais")
public class CountryEntity {

    @Id
    @Column(name = "id_pais")
    private Long idCountry;

    @Column(name = "pais")
    private String countryName;

    @Column(name = "status")
    private Boolean status;
}
