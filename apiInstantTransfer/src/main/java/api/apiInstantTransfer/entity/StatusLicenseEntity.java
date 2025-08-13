package api.apiInstantTransfer.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "t_estado_licencia")
@NoArgsConstructor
public class StatusLicenseEntity {

    @Id
    @Column(name = "id_estado_licencia")
    private Long idStatusLicense;

    @Column(name = "estado_licencia")
    private String statusLicenseDescription;

    @Column(name = "status")
    private Boolean status;
}
