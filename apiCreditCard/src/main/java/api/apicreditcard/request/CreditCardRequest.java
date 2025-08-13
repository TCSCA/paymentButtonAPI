package api.apicreditcard.request;


import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;



public class CreditCardRequest {

    private String rif;

    private String country;

    private String reason;

    private BigDecimal amount;

    private Long paymentChannel;

    private String bankPayment;

    private String payerName;

    private String email;

    private String currency;
    private CreditCard creditCard;
    @JsonProperty("creditCard")
    public CreditCard getCreditCard() { return creditCard; }
    @JsonProperty("creditCard")
    public void getCreditCard(CreditCard value) { this.creditCard = value; }

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

    public String getPayerName() {
        return payerName;
    }

    public void setPayerName(String payerName) {
        this.payerName = payerName;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public void setCreditCard(CreditCard creditCard) {
        this.creditCard = creditCard;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
