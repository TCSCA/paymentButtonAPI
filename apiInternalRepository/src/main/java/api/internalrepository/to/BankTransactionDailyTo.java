package api.internalrepository.to;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class BankTransactionDailyTo {

    private Long idPaymentChannel;

    private String paymentChannelDescription;

    private Long commerceId;

    private String commerceDocument;

    private String commerceName;

    private Long idPaymentMethod;

    private String paymentMethodName;

    private Long idBankTransactionStatus;

    private Long idBankTransaction;

    private Long reference;

    private BigDecimal amount;

    private OffsetDateTime transactionDate;

    private Long idPlan;

    private String planName;

    private OffsetDateTime fileDate;

    private LocalDate activationDate;

    public BankTransactionDailyTo(Long idPaymentChannel, String paymentChannelDescription, Long commerceId,
                                  String commerceDocument, String commerceName, Long idPaymentMethod,
                                  String paymentMethodName, Long idBankTransactionStatus, Long idBankTransaction,
                                  Long reference, BigDecimal amount, OffsetDateTime transactionDate, Long idPlan,
                                  String planName, OffsetDateTime fileDate, LocalDate activationDate) {
        this.idPaymentChannel = idPaymentChannel;
        this.paymentChannelDescription = paymentChannelDescription;
        this.commerceId = commerceId;
        this.commerceDocument = commerceDocument;
        this.commerceName = commerceName;
        this.idPaymentMethod = idPaymentMethod;
        this.paymentMethodName = paymentMethodName;
        this.idBankTransactionStatus = idBankTransactionStatus;
        this.idBankTransaction = idBankTransaction;
        this.reference = reference;
        this.amount = amount;
        this.transactionDate = transactionDate.minusHours(4L);
        this.idPlan = idPlan;
        this.planName = planName;
        this.fileDate = fileDate;
        this.activationDate = activationDate;
    }

    public Long getIdPaymentChannel() {
        return idPaymentChannel;
    }

    public void setIdPaymentChannel(Long idPaymentChannel) {
        this.idPaymentChannel = idPaymentChannel;
    }

    public String getPaymentChannelDescription() {
        return paymentChannelDescription;
    }

    public void setPaymentChannelDescription(String paymentChannelDescription) {
        this.paymentChannelDescription = paymentChannelDescription;
    }

    public Long getCommerceId() {
        return commerceId;
    }

    public void setCommerceId(Long commerceId) {
        this.commerceId = commerceId;
    }

    public String getCommerceDocument() {
        return commerceDocument;
    }

    public void setCommerceDocument(String commerceDocument) {
        this.commerceDocument = commerceDocument;
    }

    public String getCommerceName() {
        return commerceName;
    }

    public void setCommerceName(String commerceName) {
        this.commerceName = commerceName;
    }

    public Long getIdPaymentMethod() {
        return idPaymentMethod;
    }

    public void setIdPaymentMethod(Long idPaymentMethod) {
        this.idPaymentMethod = idPaymentMethod;
    }

    public String getPaymentMethodName() {
        return paymentMethodName;
    }

    public void setPaymentMethodName(String paymentMethodName) {
        this.paymentMethodName = paymentMethodName;
    }

    public Long getIdBankTransactionStatus() {
        return idBankTransactionStatus;
    }

    public void setIdBankTransactionStatus(Long idBankTransactionStatus) {
        this.idBankTransactionStatus = idBankTransactionStatus;
    }

    public Long getIdBankTransaction() {
        return idBankTransaction;
    }

    public void setIdBankTransaction(Long idBankTransaction) {
        this.idBankTransaction = idBankTransaction;
    }

    public Long getReference() {
        return reference;
    }

    public void setReference(Long reference) {
        this.reference = reference;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public OffsetDateTime getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(OffsetDateTime transactionDate) {
        this.transactionDate = transactionDate;
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

    public OffsetDateTime getFileDate() {
        return fileDate;
    }

    public void setFileDate(OffsetDateTime fileDate) {
        this.fileDate = fileDate;
    }

    public LocalDate getActivationDate() {
        return activationDate;
    }

    public void setActivationDate(LocalDate activationDate) {
        this.activationDate = activationDate;
    }
}
