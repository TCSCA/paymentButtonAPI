package api.preRegistro.to;

public class TypeCommerceTo {

    private Long idTypeCommerce;

    private String typeCommerce;

    private Boolean status;

    public TypeCommerceTo() {
    }

    public TypeCommerceTo(Long idTypeCommerce, String typeCommerce, Boolean status) {
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
