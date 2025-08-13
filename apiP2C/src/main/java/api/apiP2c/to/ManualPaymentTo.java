package api.apiP2c.to;

public class ManualPaymentTo {

    private String rif;

    private String payerDocument;

    private String debitPhone;

    private String transactionAmount;

    private String typeTransaction;

    private String referenceNumber;

    private String paymentReference;

    private String dateTransaction;

    private Long paymentChannel;

    public ManualPaymentTo(String rif, String payerDocument,
                           String debitPhone, String transactionAmount,
                           String typeTransaction, String referenceNumber,
                           String paymentReference, String dateTransaction,
                           Long paymentChannel) {
        this.rif = rif;
        this.payerDocument = payerDocument;
        this.debitPhone = debitPhone;
        this.transactionAmount = transactionAmount;
        this.typeTransaction = typeTransaction;
        this.referenceNumber = referenceNumber;
        this.paymentReference = paymentReference;
        this.dateTransaction = dateTransaction;
        this.paymentChannel = paymentChannel;
    }

    public String getRif() {
        return rif;
    }

    public void setRif(String rif) {
        this.rif = rif;
    }

    public String getPayerDocument() {
        return payerDocument;
    }

    public void setPayerDocument(String payerDocument) {
        this.payerDocument = payerDocument;
    }

    public String getDebitPhone() {
        return debitPhone;
    }

    public void setDebitPhone(String debitPhone) {
        this.debitPhone = debitPhone;
    }

    public String getTransactionAmount() {
        return transactionAmount;
    }

    public void setTransactionAmount(String transactionAmount) {
        this.transactionAmount = transactionAmount;
    }

    public String getTypeTransaction() {
        return typeTransaction;
    }

    public void setTypeTransaction(String typeTransaction) {
        this.typeTransaction = typeTransaction;
    }

    public String getReferenceNumber() {
        return referenceNumber;
    }

    public void setReferenceNumber(String referenceNumber) {
        this.referenceNumber = referenceNumber;
    }

    public String getPaymentReference() {
        return paymentReference;
    }

    public void setPaymentReference(String paymentReference) {
        this.paymentReference = paymentReference;
    }

    public String getDateTransaction() {
        return dateTransaction;
    }

    public void setDateTransaction(String dateTransaction) {
        this.dateTransaction = dateTransaction;
    }

    public Long getPaymentChannel() {
        return paymentChannel;
    }

    public void setPaymentChannel(Long paymentChannel) {
        this.paymentChannel = paymentChannel;
    }
}
