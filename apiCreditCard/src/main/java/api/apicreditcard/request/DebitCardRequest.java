package api.apicreditcard.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;


public class DebitCardRequest {

    private String rif;

    private String country;

    private String reason;

    private BigDecimal amount;

    private String payerName;
    private Long paymentChannel;

    private String bankPayment;

    private String currency;

    private DebitCard debitCard;

    private String email;

    @JsonProperty("debitCard")
    public DebitCard getDebitCard() { return debitCard; }
    @JsonProperty("debitCard")
    public void getDebitCard(DebitCard value) { this.debitCard = value; }


    public DebitCardRequest() {
    }

    public DebitCardRequest(String rif, String country, String reason, BigDecimal amount, Long paymentChannel, String bankPayment, String currency, DebitCard debitCard) {
        this.rif = rif;
        this.country = country;
        this.reason = reason;
        this.amount = amount;
        this.paymentChannel = paymentChannel;
        this.bankPayment = bankPayment;
        this.currency = currency;
        this.debitCard = debitCard;
    }

    public String getRif() {
        return rif;
    }

    public void setRif(String rif) {
        this.rif = rif;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Long getPaymentChannel() {
        return paymentChannel;
    }

    public void setPaymentChannel(Long paymentChannel) {
        this.paymentChannel = paymentChannel;
    }

    public String getBankPayment() {
        return bankPayment;
    }

    public void setBankPayment(String bankPayment) {
        this.bankPayment = bankPayment;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public void setDebitCard(DebitCard debitCard) {
        this.debitCard = debitCard;
    }

    public String getPayerName() {
        return payerName;
    }

    public void setPayerName(String payerName) {
        this.payerName = payerName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}