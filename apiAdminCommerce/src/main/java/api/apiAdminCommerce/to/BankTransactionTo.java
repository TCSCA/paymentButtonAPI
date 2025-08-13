package api.apiAdminCommerce.to;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.util.LinkedHashMap;

public class BankTransactionTo {

    private String rif;

    private String phoneNumberCommerce;

    private String commerceName;

    private String senderIdentificationDocument;

    private String senderPhoneNumber;

    private BigDecimal amount;

    private Long paymentMethod;

    public BankTransactionTo() {
    }

    public BankTransactionTo(String rif, String phoneNumberCommerce, String commerceName, String senderIdentificationDocument, String senderPhoneNumber, BigDecimal amount) {
        this.rif = rif;
        this.phoneNumberCommerce = phoneNumberCommerce;
        this.commerceName = commerceName;
        this.senderIdentificationDocument = senderIdentificationDocument;
        this.senderPhoneNumber = senderPhoneNumber;
        this.amount = amount;
    }

    public BankTransactionTo(ObjectMapper objectMapper, LinkedHashMap<String, Object> response) {

        LinkedHashMap<String, Object> commerceEntityMap = (LinkedHashMap<String, Object>) ((LinkedHashMap<String, Object>) response.get("bankTransactionEntity")).get("commerceEntity");

        this.rif = objectMapper.convertValue(commerceEntityMap.get("commerceDocument"), String.class);

        this.phoneNumberCommerce = objectMapper.
                convertValue(commerceEntityMap.get("phoneNumberCommerce"), String.class);

        this.commerceName = objectMapper.convertValue(commerceEntityMap.get("commerceName"), String.class);

        this.amount = objectMapper.
                convertValue(((LinkedHashMap<String, Object>) response.get("bankTransactionEntity")).
                        get("amount"), BigDecimal.class);

        this.senderIdentificationDocument = objectMapper.
                convertValue(((LinkedHashMap<String, Object>) response.get("bankTransactionEntity")).
                        get("senderIdentificationDocument"), String.class);

        this.senderPhoneNumber = objectMapper.
                convertValue(((LinkedHashMap<String, Object>) response.get("bankTransactionEntity")).
                        get("senderPhoneNumber"), String.class);

        LinkedHashMap<String, Object> paymentMethodEntityMap = (LinkedHashMap<String, Object>) ((LinkedHashMap<String, Object>) response.
                get("bankTransactionEntity")).get("paymentMethodEntity");

        this.paymentMethod = objectMapper.convertValue(paymentMethodEntityMap.get("idPaymentMethod"), Long.class);
    }

    public String getRif() {
        return rif;
    }

    public void setRif(String rif) {
        this.rif = rif;
    }

    public String getPhoneNumberCommerce() {
        return phoneNumberCommerce;
    }

    public void setPhoneNumberCommerce(String phoneNumberCommerce) {
        this.phoneNumberCommerce = phoneNumberCommerce;
    }

    public String getCommerceName() {
        return commerceName;
    }

    public void setCommerceName(String commerceName) {
        this.commerceName = commerceName;
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
}
