package api.internalrepository.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Entity
@Table(name = "t_transaccion_bancaria")
public class BankTransactionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "t_transaccion_bancaria_seq")
    @SequenceGenerator(name = "t_transaccion_bancaria_seq", sequenceName = "t_transaccion_bancaria_seq", allocationSize = 1)
    @Column(name = "id_transaccion_bancaria")
    private Long idBankTransaction;

    @Column(name = "codigo_transaccion")
    private String transactionCode;

    @Column(name = "numero_referencia")
    private Long referenceNumber;

    @Column(name = "monto")
    private BigDecimal amount;

    @ManyToOne()
    @JoinColumn(name = "id_banco")
    private BankEntity bankEntity;

    @ManyToOne()
    @JoinColumn(name = "id_comercio")
    private CommerceEntity commerceEntity;

    @ManyToOne()
    @JoinColumn(name = "id_moneda")
    private CurrencyEntity currencyEntity;

    @ManyToOne()
    @JoinColumn(name = "id_tipo_transaccion")
    private TransactionTypeEntity transactionTypeEntity;

    @ManyToOne()
    @JoinColumn(name = "id_metodo_pago")
    private PaymentMethodEntity paymentMethodEntity;

    @ManyToOne()
    @JoinColumn(name = "id_estado_transaccion")
    private TransactionStatusEntity transactionStatusEntity;

    @Column(name = "fecha_registro")
    private OffsetDateTime registerDate;

    @Column(name = "fecha_actualizacion")
    private OffsetDateTime updateDate;

    @ManyToOne()
    @JoinColumn(name = "id_canal_pago")
    private PaymentChannel paymentChannel;

    @Column(name = "cedula_remitente")
    private String senderIdentificationDocument;

    @Column(name = "telefono_remitente")
    private String senderPhoneNumber;

    @Column(name = "registrado_por")
    private String registerBy;

    @Column(name = "actualizado_por")
    private String updateBy;

    @Column(name = "motivo_cobro")
    private String reasonOfCollection;

    public BankTransactionEntity() {
    }

    public BankTransactionEntity(Long idBankTransaction, String transactionCode,
                                 Long referenceNumber, BigDecimal amount, BankEntity bankEntity,
                                 CommerceEntity commerceEntity, CurrencyEntity currencyEntity,
                                 TransactionTypeEntity transactionTypeEntity,
                                 PaymentMethodEntity paymentMethodEntity,
                                 TransactionStatusEntity transactionStatusEntity,
                                 OffsetDateTime registerDate, OffsetDateTime updateDate,
                                 PaymentChannel paymentChannel, String senderIdentificationDocument,
                                 String senderPhoneNumber, String registerBy, String updateBy,
                                 String reasonOfCollection) {
        this.idBankTransaction = idBankTransaction;
        this.transactionCode = transactionCode;
        this.referenceNumber = referenceNumber;
        this.amount = amount;
        this.bankEntity = bankEntity;
        this.commerceEntity = commerceEntity;
        this.currencyEntity = currencyEntity;
        this.transactionTypeEntity = transactionTypeEntity;
        this.paymentMethodEntity = paymentMethodEntity;
        this.transactionStatusEntity = transactionStatusEntity;
        this.registerDate = registerDate;
        this.updateDate = updateDate;
        this.paymentChannel = paymentChannel;
        this.senderIdentificationDocument = senderIdentificationDocument;
        this.senderPhoneNumber = senderPhoneNumber;
        this.registerBy = registerBy;
        this.updateBy = updateBy;
        this.reasonOfCollection = reasonOfCollection;
    }

    public BankTransactionEntity(BankTransactionEntity bankTransactionEntity) {
        this.idBankTransaction = bankTransactionEntity.getIdBankTransaction();
        this.transactionCode = bankTransactionEntity.getTransactionCode();
        this.referenceNumber = bankTransactionEntity.getReferenceNumber();
        this.amount = bankTransactionEntity.getAmount();
        this.bankEntity = bankTransactionEntity.getBankEntity();
        this.commerceEntity = bankTransactionEntity.getCommerceEntity();
        this.currencyEntity = bankTransactionEntity.getCurrencyEntity();
        this.transactionTypeEntity = bankTransactionEntity.getTransactionTypeEntity();
        this.paymentMethodEntity = bankTransactionEntity.getPaymentMethodEntity();
        this.transactionStatusEntity = bankTransactionEntity.getTransactionStatusEntity();
        this.registerDate = bankTransactionEntity.getRegisterDate();
        this.updateDate = bankTransactionEntity.getUpdateDate();
        this.paymentChannel = bankTransactionEntity.getPaymentChannel();
        if(bankTransactionEntity.getSenderIdentificationDocument() != null){
            this.senderIdentificationDocument = bankTransactionEntity.getSenderIdentificationDocument();
        }else{
            this.senderIdentificationDocument = "N/A";
        }
        if(bankTransactionEntity.getSenderPhoneNumber() != null){
            this.senderPhoneNumber = bankTransactionEntity.getSenderPhoneNumber();
        }else{
            this.senderPhoneNumber = "N/A";
        }
        if(bankTransactionEntity.getRegisterBy() != null){
            this.registerBy = bankTransactionEntity.getRegisterBy();
        }else{
            this.registerBy = "N/A";
        }
        this.updateBy = bankTransactionEntity.getUpdateBy();
        this.reasonOfCollection = bankTransactionEntity.getReasonOfCollection();
    }

    public Long getIdBankTransaction() {
        return idBankTransaction;
    }

    public void setIdBankTransaction(Long idBankTransaction) {
        this.idBankTransaction = idBankTransaction;
    }

    public String getTransactionCode() {
        return transactionCode;
    }

    public void setTransactionCode(String transactionCode) {
        this.transactionCode = transactionCode;
    }

    public Long getReferenceNumber() {
        return referenceNumber;
    }

    public void setReferenceNumber(Long referenceNumber) {
        this.referenceNumber = referenceNumber;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BankEntity getBankEntity() {
        return bankEntity;
    }

    public void setBankEntity(BankEntity bankEntity) {
        this.bankEntity = bankEntity;
    }

    public CommerceEntity getCommerceEntity() {
        return commerceEntity;
    }

    public void setCommerceEntity(CommerceEntity commerceEntity) {
        this.commerceEntity = commerceEntity;
    }

    public CurrencyEntity getCurrencyEntity() {
        return currencyEntity;
    }

    public void setCurrencyEntity(CurrencyEntity currencyEntity) {
        this.currencyEntity = currencyEntity;
    }

    public TransactionTypeEntity getTransactionTypeEntity() {
        return transactionTypeEntity;
    }

    public void setTransactionTypeEntity(TransactionTypeEntity transactionTypeEntity) {
        this.transactionTypeEntity = transactionTypeEntity;
    }

    public PaymentMethodEntity getPaymentMethodEntity() {
        return paymentMethodEntity;
    }

    public void setPaymentMethodEntity(PaymentMethodEntity paymentMethodEntity) {
        this.paymentMethodEntity = paymentMethodEntity;
    }

    public TransactionStatusEntity getTransactionStatusEntity() {
        return transactionStatusEntity;
    }

    public void setTransactionStatusEntity(TransactionStatusEntity transactionStatusEntity) {
        this.transactionStatusEntity = transactionStatusEntity;
    }

    public OffsetDateTime getRegisterDate() {
        return registerDate;
    }

    public void setRegisterDate(OffsetDateTime registerDate) {
        this.registerDate = registerDate;
    }

    public OffsetDateTime getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(OffsetDateTime updateDate) {
        this.updateDate = updateDate;
    }

    public PaymentChannel getPaymentChannel() {
        return paymentChannel;
    }

    public void setPaymentChannel(PaymentChannel paymentChannel) {
        this.paymentChannel = paymentChannel;
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

    public String getRegisterBy() {
        return registerBy;
    }

    public void setRegisterBy(String registerBy) {
        this.registerBy = registerBy;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public String getReasonOfCollection() {
        return reasonOfCollection;
    }

    public void setReasonOfCollection(String reasonOfCollection) {
        this.reasonOfCollection = reasonOfCollection;
    }
}
