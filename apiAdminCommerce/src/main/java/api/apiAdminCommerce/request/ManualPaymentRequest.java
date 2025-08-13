package api.apiAdminCommerce.request;

public class ManualPaymentRequest {

    private Long idBankTransaction;

    private Long statusBankTransaction;

    private String username;

    public Long getIdBankTransaction() {
        return idBankTransaction;
    }

    public void setIdBankTransaction(Long idBankTransaction) {
        this.idBankTransaction = idBankTransaction;
    }

    public Long getStatusBankTransaction() {
        return statusBankTransaction;
    }

    public void setStatusBankTransaction(Long statusBankTransaction) {
        this.statusBankTransaction = statusBankTransaction;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
