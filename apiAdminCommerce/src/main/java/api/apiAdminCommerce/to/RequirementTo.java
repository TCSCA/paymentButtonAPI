package api.apiAdminCommerce.to;

public class RequirementTo {

    private Long idRequirement;

    private String recaudo;

    private Boolean required;

    private Boolean status;

    private Long idTypeCommerce;

    public RequirementTo() {
    }

    public RequirementTo(Long idRequirement, String recaudo, Boolean required, Boolean status, Long idTypeCommerce) {
        this.idRequirement = idRequirement;
        this.recaudo = recaudo;
        this.required = required;
        this.status = status;
        this.idTypeCommerce = idTypeCommerce;
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

    public Long getIdTypeCommerce() {
        return idTypeCommerce;
    }

    public void setIdTypeCommerce(Long idTypeCommerce) {
        this.idTypeCommerce = idTypeCommerce;
    }
}
