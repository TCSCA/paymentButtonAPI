package api.apicommerce.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "t_estado_transaccion")
public class TransactionStatusEntity {

    @Id
    @Column(name = "id_estado_transaccion")
    private Long idTransactionStatus;

    @Column(name = "estado_transaccion")
    private String transactionStatus;

    @Column(name = "status")
    private String status;

    public TransactionStatusEntity() {
    }

    public TransactionStatusEntity(Long idTransactionStatus, String transactionStatus, String status) {
        this.idTransactionStatus = idTransactionStatus;
        this.transactionStatus = transactionStatus;
        this.status = status;
    }

    public Long getIdTransactionStatus() {
        return idTransactionStatus;
    }

    public void setIdTransactionStatus(Long idTransactionStatus) {
        this.idTransactionStatus = idTransactionStatus;
    }

    public String getTransactionStatus() {
        return transactionStatus;
    }

    public void setTransactionStatus(String transactionStatus) {
        this.transactionStatus = transactionStatus;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
