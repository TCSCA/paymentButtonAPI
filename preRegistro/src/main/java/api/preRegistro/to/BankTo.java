package api.preRegistro.to;

public class BankTo {

    private Long idBank;

    private String bank;

    private String bankCode;

    private Boolean status;

    public BankTo() {
    }

    public BankTo(Long idBank, String bank, String bankCode, Boolean status) {
        this.idBank = idBank;
        this.bank = bank;
        this.bankCode = bankCode;
        this.status = status;
    }

    public Long getIdBank() {
        return idBank;
    }

    public void setIdBank(Long idBank) {
        this.idBank = idBank;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }
}
