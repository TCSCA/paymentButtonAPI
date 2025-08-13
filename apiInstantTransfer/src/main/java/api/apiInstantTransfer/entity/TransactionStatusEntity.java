package api.apiInstantTransfer.entity;

import com.google.gson.annotations.Expose;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "t_estado_transaccion")
@NoArgsConstructor
public class TransactionStatusEntity {

    @Id
    @Column(name = "id_estado_transaccion")
    @Expose
    private Long idTransactionStatus;

    @Column(name = "estado_transaccion")
    private String transactionStatus;

    @Column(name = "status")
    private String status;

    public TransactionStatusEntity(Long idTransactionStatus) {
        this.idTransactionStatus = idTransactionStatus;
    }
}
