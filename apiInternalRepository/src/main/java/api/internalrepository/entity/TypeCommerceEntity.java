package api.internalrepository.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "t_tipo_comercio")
public class TypeCommerceEntity {

    @Id
    @Column(name = "id_tipo_comercio")
    private Long idTypeCommerce;

    @Column(name = "tipo_comercio")
    private String typeCommerce;

    @Column(name = "status")
    private Boolean status;

    public TypeCommerceEntity() {
    }

    public TypeCommerceEntity(Long idTypeCommerce) {
        this.idTypeCommerce = idTypeCommerce;
    }

    public TypeCommerceEntity(Long idTypeCommerce, String typeCommerce, Boolean status) {
        this.idTypeCommerce = idTypeCommerce;
        this.typeCommerce = typeCommerce;
        this.status = status;
    }

    public Long getIdTypeCommerce() {
        return idTypeCommerce;
    }

    public void setIdTypeCommerce(Long idTypeCommerce) {
        this.idTypeCommerce = idTypeCommerce;
    }

    public String getTypeCommerce() {
        return typeCommerce;
    }

    public void setTypeCommerce(String typeCommerce) {
        this.typeCommerce = typeCommerce;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }
}
