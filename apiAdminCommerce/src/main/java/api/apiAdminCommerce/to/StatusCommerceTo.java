package api.apiAdminCommerce.to;

public class StatusCommerceTo {

    private Long idStatusCommerce;

    private String statusDescription;

    public StatusCommerceTo() {
    }

    public StatusCommerceTo(Long idStatusCommerce, String statusDescription) {
        this.idStatusCommerce = idStatusCommerce;
        this.statusDescription = statusDescription;
    }

    public Long getIdStatusCommerce() {
        return idStatusCommerce;
    }

    public void setIdStatusCommerce(Long idStatusCommerce) {
        this.idStatusCommerce = idStatusCommerce;
    }

    public String getStatusDescription() {
        return statusDescription;
    }

    public void setStatusDescription(String statusDescription) {
        this.statusDescription = statusDescription;
    }
}
