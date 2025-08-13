package api.apicommerce.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "t_tipo_transaccion")
public class TransactionTypeEntity {

    @Id
    @Column(name = "id_tipo_transaccion")
    private Long idTransactionType;

    @Column(name = "tipo_transaccion")
    private String transactionType;

    @Column(name = "status")
    private Boolean status;

    public TransactionTypeEntity() {
    }

    public TransactionTypeEntity(Long idTransactionType, String transactionType, Boolean status) {
        this.idTransactionType = idTransactionType;
        this.transactionType = transactionType;
        this.status = status;
    }

    public Long getIdTransactionType() {
        return idTransactionType;
    }

    public void setIdTransactionType(Long idTransactionType) {
        this.idTransactionType = idTransactionType;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }
}
