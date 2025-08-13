package api.apiP2c.to;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.LinkedHashMap;

public class CommerceBankInformationTo {

    private String commerceName;

    private String rif;

    private String commercePhone;

    private String bankAccount;

    private String bankName;

    public CommerceBankInformationTo(String commerceName, String rif, String commercePhone, String bankAccount, String bankName) {
        this.commerceName = commerceName;
        this.rif = rif;
        this.commercePhone = commercePhone;
        this.bankAccount = bankAccount;
        this.bankName = bankName;
    }

    public CommerceBankInformationTo() {
    }

    public CommerceBankInformationTo(ObjectMapper objectMapper, LinkedHashMap<String, Object> response){

        this.commerceName = objectMapper.convertValue(((LinkedHashMap<String, Object>) response.get("commerceBankInformationTo")).get("commerceName"), String.class);
        this.rif = objectMapper.convertValue(((LinkedHashMap<String, Object>) response.get("commerceBankInformationTo")).get("rif"), String.class);
        this.commercePhone = objectMapper.convertValue(((LinkedHashMap<String, Object>) response.get("commerceBankInformationTo")).get("commercePhone"), String.class);
        this.bankAccount = objectMapper.convertValue(((LinkedHashMap<String, Object>) response.get("commerceBankInformationTo")).get("bankAccount"), String.class);
        this.bankName = objectMapper.convertValue(((LinkedHashMap<String, Object>) response.get("commerceBankInformationTo")).get("bankName"), String.class);

    }

    public String getCommerceName() {
        return commerceName;
    }

    public void setCommerceName(String commerceName) {
        this.commerceName = commerceName;
    }

    public String getRif() {
        return rif;
    }

    public void setRif(String rif) {
        this.rif = rif;
    }

    public String getCommercePhone() {
        return commercePhone;
    }

    public void setCommercePhone(String commercePhone) {
        this.commercePhone = commercePhone;
    }

    public String getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }
}
