package api.internalrepository.to;

public class RegisterCommerceTo {

    private Long idPreRegistro;

    private String commerceName;

    private String commerceDocument;

    private String commerceEmail;

    private String phoneNumberCommerce;

    private String contactPerson;

    private String contactPersonEmail;

    private Long idState;

    private String address;

    private Long statusPreRegister;

    private String contactPersonDocument;

    private Long idPlan;

    private Long idTypeCommerce;

    public RegisterCommerceTo() {
    }

    public RegisterCommerceTo(Long idPreRegistro, String commerceName, String commerceDocument, String commerceEmail, String phoneNumberCommerce, String contactPerson, String contactPersonEmail, Long idState, String address, Long statusPreRegister, String contactPersonDocument, Long idPlan, Long idTypeCommerce) {
        this.idPreRegistro = idPreRegistro;
        this.commerceName = commerceName;
        this.commerceDocument = commerceDocument;
        this.commerceEmail = commerceEmail;
        this.phoneNumberCommerce = phoneNumberCommerce;
        this.contactPerson = contactPerson;
        this.contactPersonEmail = contactPersonEmail;
        this.idState = idState;
        this.address = address;
        this.statusPreRegister = statusPreRegister;
        this.contactPersonDocument = contactPersonDocument;
        this.idPlan = idPlan;
        this.idTypeCommerce = idTypeCommerce;
    }

    public Long getIdPreRegistro() {
        return idPreRegistro;
    }

    public void setIdPreRegistro(Long idPreRegistro) {
        this.idPreRegistro = idPreRegistro;
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

    public String getCommerceEmail() {
        return commerceEmail;
    }

    public void setCommerceEmail(String commerceEmail) {
        this.commerceEmail = commerceEmail;
    }

    public String getPhoneNumberCommerce() {
        return phoneNumberCommerce;
    }

    public void setPhoneNumberCommerce(String phoneNumberCommerce) {
        this.phoneNumberCommerce = phoneNumberCommerce;
    }

    public String getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    public String getContactPersonEmail() {
        return contactPersonEmail;
    }

    public void setContactPersonEmail(String contactPersonEmail) {
        this.contactPersonEmail = contactPersonEmail;
    }

    public Long getidState() {
        return idState;
    }

    public void setidState(Long idState) {
        this.idState = idState;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Long getStatusPreRegister() {
        return statusPreRegister;
    }

    public void setStatusPreRegister(Long statusPreRegister) {
        this.statusPreRegister = statusPreRegister;
    }

    public String getContactPersonDocument() {
        return contactPersonDocument;
    }

    public void setContactPersonDocument(String contactPersonDocument) {
        this.contactPersonDocument = contactPersonDocument;
    }

    public Long getIdPlan() {
        return idPlan;
    }

    public void setIdPlan(Long idPlan) {
        this.idPlan = idPlan;
    }

    public Long getIdTypeCommerce() {
        return idTypeCommerce;
    }

    public void setIdTypeCommerce(Long idTypeCommerce) {
        this.idTypeCommerce = idTypeCommerce;
    }
}
