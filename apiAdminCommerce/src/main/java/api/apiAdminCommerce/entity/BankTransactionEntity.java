package api.apiAdminCommerce.entity;

import com.google.gson.annotations.Expose;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;

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

    @ManyToOne()
    @JoinColumn(name = "id_canal_pago")
    private PaymentChannel paymentChannel;

    @Column(name = "fecha_registro")
    private OffsetDateTime registerDate;

    @Column(name = "fecha_actualizacion")
    private OffsetDateTime updateDate;

    public BankTransactionEntity() {
    }

    public BankTransactionEntity(Long idBankTransaction, String transactionCode, Long referenceNumber, BigDecimal amount, BankEntity bankEntity, CommerceEntity commerceEntity, CurrencyEntity currencyEntity, TransactionTypeEntity transactionTypeEntity, PaymentMethodEntity paymentMethodEntity, TransactionStatusEntity transactionStatusEntity, OffsetDateTime registerDate, OffsetDateTime updateDate) {
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
}
