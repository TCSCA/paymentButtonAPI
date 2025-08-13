package api.apiAdminCommerce.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "t_plan")
public class PlanEntity {

    @Id
    @Column(name = "id_plan")
    private Long idPlan;

    @Column(name = "nombre_plan")
    private String planName;

    @Column(name = "status")
    private Boolean status;

    @ManyToOne
    @JoinColumn(name = "id_tipo_comercio")
    private TypeCommerceEntity typeCommerceEntity;

    public PlanEntity() {
    }

    public PlanEntity(Long idPlan) {
        this.idPlan = idPlan;
    }

    public PlanEntity(Long idPlan, String planName, Boolean status, TypeCommerceEntity typeCommerceEntity) {
        this.idPlan = idPlan;
        this.planName = planName;
        this.status = status;
        this.typeCommerceEntity = typeCommerceEntity;
    }

    public Long getIdPlan() {
        return idPlan;
    }

    public void setIdPlan(Long idPlan) {
        this.idPlan = idPlan;
    }

    public String getPlanName() {
        return planName;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
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
