package api.preRegistro.to;

public class PlanTo {

    private Long idPlan;

    private String planName;

    private Boolean status;

    private Long idTypeCommerce;

    public PlanTo() {
    }

    public PlanTo(Long idPlan, String namePlan, Boolean status, Long idTypeCommerce) {
        this.idPlan = idPlan;
        this.planName = namePlan;
        this.status = status;
        this.idTypeCommerce = idTypeCommerce;
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

    public Long getIdTypeCommerce() {
        return idTypeCommerce;
    }

    public void setIdTypeCommerce(Long idTypeCommerce) {
        this.idTypeCommerce = idTypeCommerce;
    }
}
