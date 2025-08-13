package api.apicommerce.request;

import java.math.BigDecimal;

public class PaymentLinkRequest {

    private String senderIdentificationDocument;

    private String senderPhoneNumber;

    private BigDecimal amount;

    private String rif;

    private Long idCurrency;

    private Long idPaymentChannel;

    private Long idPaymentMethod;

    private String reasonOfCollection;

    public PaymentLinkRequest() {
    }

    public PaymentLinkRequest(String senderIdentificationDocument, String senderPhoneNumber,
                              BigDecimal amount, String rif, Long idCurrency, Long idPaymentChannel,
                              Long idPaymentMethod, String reasonOfCollection) {
        this.senderIdentificationDocument = senderIdentificationDocument;
        this.senderPhoneNumber = senderPhoneNumber;
        this.amount = amount;
        this.rif = rif;
        this.idCurrency = idCurrency;
        this.idPaymentChannel = idPaymentChannel;
        this.idPaymentMethod = idPaymentMethod;
        this.reasonOfCollection = reasonOfCollection;
    }

    public String getSenderIdentificationDocument() {
        return senderIdentificationDocument;
    }

    public void setSenderIdentificationDocument(String senderIdentificationDocument) {
        this.senderIdentificationDocument = senderIdentificationDocument;
    }

    public String getSenderPhoneNumber() {
        return senderPhoneNumber;
    }

    public void setSenderPhoneNumber(String senderPhoneNumber) {
        this.senderPhoneNumber = senderPhoneNumber;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getRif() {
        return rif;
    }

    public void setRif(String rif) {
        this.rif = rif;
    }

    public Long getIdCurrency() {
        return idCurrency;
    }

    public void setIdCurrency(Long idCurrency) {
        this.idCurrency = idCurrency;
    }

    public Long getIdPaymentChannel() {
        return idPaymentChannel;
    }

    public void setIdPaymentChannel(Long idPaymentChannel) {
        this.idPaymentChannel = idPaymentChannel;
    }

    public Long getIdPaymentMethod() {
        return idPaymentMethod;
    }

    public void setIdPaymentMethod(Long idPaymentMethod) {
        this.idPaymentMethod = idPaymentMethod;
    }

    public String getReasonOfCollection() {
        return reasonOfCollection;
    }

    public void setReasonOfCollection(String reasonOfCollection) {
        this.reasonOfCollection = reasonOfCollection;
    }
}
