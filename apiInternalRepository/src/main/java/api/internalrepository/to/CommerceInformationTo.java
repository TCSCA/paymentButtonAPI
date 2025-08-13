package api.internalrepository.to;

public class CommerceInformationTo {

    private Long idCommerce;

    private String commerceName;

    private String commerceDocument;

    public CommerceInformationTo() {
    }

    public CommerceInformationTo(Long idCommerce, String commerceName, String commerceDocument) {
        this.idCommerce = idCommerce;
        this.commerceName = commerceName;
        this.commerceDocument = commerceDocument;
    }

    public Long getIdCommerce() {
        return idCommerce;
    }

    public void setIdCommerce(Long idCommerce) {
        this.idCommerce = idCommerce;
    }

    public String getCommerceName() {
        return commerceName;
    }

    public void setCommerceName(String commerceName) {
        this.commerceName = commerceName;
    }

    public String getCommerceDocument() {
        return commerceDocument;
    }

    public void setCommerceDocument(String commerceDocument) {
        this.commerceDocument = commerceDocument;
    }
}
