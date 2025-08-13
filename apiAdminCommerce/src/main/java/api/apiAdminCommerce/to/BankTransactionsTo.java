package api.apiAdminCommerce.to;

import api.apiAdminCommerce.entity.*;
import com.google.gson.annotations.Expose;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public class BankTransactionsTo {


    private Long idBankTransaction;


    private String transactionCode;


    private Long referenceNumber;


    private BigDecimal amount;


    private BankEntity bankEntity;


    private CommerceEntity commerceEntity;


    private CurrencyEntity currencyEntity;


    private TransactionTypeEntity transactionTypeEntity;


    private PaymentMethodEntity paymentMethodEntity;


    private TransactionStatusEntity transactionStatusEntity;


    private OffsetDateTime registerDate;


    private OffsetDateTime updateDate;


    private Long transactionsAmount;

    public BankTransactionsTo(Long idBankTransaction, String transactionCode,
                              Long referenceNumber, BigDecimal amount,
                              BankEntity bankEntity, CommerceEntity commerceEntity,
                              CurrencyEntity currencyEntity,
                              TransactionTypeEntity transactionTypeEntity,
                              PaymentMethodEntity paymentMethodEntity,
                              TransactionStatusEntity transactionStatusEntity, OffsetDateTime registerDate,
                              OffsetDateTime updateDate, Long transactionsAmount) {
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
        this.transactionsAmount = transactionsAmount;
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

    public Long getTransactionsAmount() {
        return transactionsAmount;
    }

    public void setTransactionsAmount(Long transactionsAmount) {
        this.transactionsAmount = transactionsAmount;
    }
}
