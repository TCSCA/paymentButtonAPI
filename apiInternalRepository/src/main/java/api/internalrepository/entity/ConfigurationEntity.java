package api.internalrepository.entity;

import com.google.gson.annotations.Expose;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

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

    public ConfigurationEntity() {
    }

    public Long getIdConfiguration() {
        return idConfiguration;
    }

    public void setIdConfiguration(Long idConfiguration) {
        this.idConfiguration = idConfiguration;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }
}
