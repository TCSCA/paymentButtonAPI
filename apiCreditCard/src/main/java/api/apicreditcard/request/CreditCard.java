package api.apicreditcard.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CreditCard {
    private String cardNumber;
    private long expirationMonth;
    private long expirationYear;
    private String holderName;
    private String holderIdDoc;
    private String holderId;
    private String cardType;
    private String cvc;


    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public long getExpirationMonth() {
        return expirationMonth;
    }

    public void setExpirationMonth(long expirationMonth) {
        this.expirationMonth = expirationMonth;
    }

    public long getExpirationYear() {
        return expirationYear;
    }

    public void setExpirationYear(long expirationYear) {
        this.expirationYear = expirationYear;
    }

    public String getHolderName() {
        return holderName;
    }

    public void setHolderName(String holderName) {
        this.holderName = holderName;
    }

    public String getHolderIdDoc() {
        return holderIdDoc;
    }

    public void setHolderIdDoc(String holderIdDoc) {
        this.holderIdDoc = holderIdDoc;
    }

    public String getHolderId() {
        return holderId;
    }

    public void setHolderId(String holderId) {
        this.holderId = holderId;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public String getCvc() {
        return cvc;
    }

    public void setCvc(String cvc) {
        this.cvc = cvc;
    }

}
