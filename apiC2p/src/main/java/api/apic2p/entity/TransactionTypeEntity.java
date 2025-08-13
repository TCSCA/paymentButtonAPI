package api.apic2p.entity;

import com.google.gson.annotations.Expose;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "t_tipo_transaccion")
@NoArgsConstructor
public class TransactionTypeEntity {

    @Id
    @Column(name = "id_tipo_transaccion")
    @Expose
    private Long idTransactionType;

    @Column(name = "tipo_transaccion")
    private String transactionType;

    @Column(name = "status")
    private Boolean status;

}
