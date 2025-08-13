package api.externalrepository.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "t_recaudo")
public class RequirementEntity {

    @Id
    @Column(name = "id_recaudo")
    private Long idRequirement;

    @Column(name = "recaudo")
    private String recaudo;

    @Column(name = "requerido")
    private Boolean required;

    @Column(name = "status")
    private Boolean status;

    @ManyToOne
    @JoinColumn(name = "id_tipo_comercio")
    private TypeCommerceEntity typeCommerceEntity;

    public RequirementEntity() {
    }

    public RequirementEntity(Long idRequirement) {
        this.idRequirement = idRequirement;
    }

    public RequirementEntity(Long idRequirement, String recaudo, Boolean required, Boolean status, TypeCommerceEntity typeCommerceEntity) {
        this.idRequirement = idRequirement;
        this.recaudo = recaudo;
        this.required = required;
        this.status = status;
        this.typeCommerceEntity = typeCommerceEntity;
    }

    public Long getIdRequirement() {
        return idRequirement;
    }

    public void setIdRequirement(Long idRequirement) {
        this.idRequirement = idRequirement;
    }

    public String getRecaudo() {
        return recaudo;
    }

    public void setRecaudo(String recaudo) {
        this.recaudo = recaudo;
    }

    public Boolean getRequired() {
        return required;
    }

    public void setRequired(Boolean required) {
        this.required = required;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public TypeCommerceEntity getTypeCommerceEntity() {
        return typeCommerceEntity;
    }

    public void setTypeCommerceEntity(TypeCommerceEntity typeCommerceEntity) {
        this.typeCommerceEntity = typeCommerceEntity;
    }
}
