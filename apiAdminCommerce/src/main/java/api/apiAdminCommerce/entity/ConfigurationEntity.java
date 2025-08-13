package api.apiAdminCommerce.entity;

import com.google.gson.annotations.Expose;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Entity
@Table(name = "t_configuracion")
public class ConfigurationEntity {

    @Id
    @Column(name = "id_configuracion")
    @Expose
    private Long idConfiguration;

    @NotNull
    @Column(name = "clave")
    private String password;

    @NotNull
    @Column(name = "valor")
    private String value;

    @Column(name = "status")
    private Boolean status;


}
