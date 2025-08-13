package api.apiB2p.entity;

import com.google.gson.annotations.Expose;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Data
@Entity
@Table(name = "t_transaccion_bancaria")
@NoArgsConstructor
public class BankTransactionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "t_transaccion_bancaria_seq")
    @SequenceGenerator(name = "t_transaccion_bancaria_seq", sequenceName = "t_transaccion_bancaria_seq", allocationSize = 1)
    @Column(name = "id_transaccion_bancaria")
    @Expose
    private Long idBankTransaction;

    @Column(name = "codigo_transaccion")
    private String transactionCode;

    @Column(name = "numero_referencia")
    private Long referenceNumber;

    @Column(name = "monto")
    private BigDecimal amount;

    @ManyToOne()
    @Expose
    @JoinColumn(name = "id_banco")
    private BankEntity bankEntity;

    @ManyToOne()
    @Expose
    @JoinColumn(name = "id_comercio")
    private CommerceEntity commerceEntity;

    @ManyToOne()
    @Expose
    @JoinColumn(name = "id_moneda")
    private CurrencyEntity currencyEntity;

    @ManyToOne()
    @Expose
    @JoinColumn(name = "id_tipo_transaccion")
    private TransactionTypeEntity transactionTypeEntity;

    @ManyToOne()
    @Expose
    @JoinColumn(name = "id_metodo_pago")
    private PaymentMethodEntity paymentMethodEntity;

    @ManyToOne()
    @Expose
    @JoinColumn(name = "id_estado_transaccion")
    private TransactionStatusEntity transactionStatusEntity;

    @ManyToOne()
    @Expose
    @JoinColumn(name = "id_canal_pago")
    private PaymentChannel paymentChannel;

    @Column(name = "fecha_registro")
    private OffsetDateTime registerDate;

    @Column(name = "fecha_actualizacion")
    private OffsetDateTime updateDate;

    @Column(name = "registrado_por")
    private String registerBy;

    @Column(name = "actualizado_por")
    private String updateBy;
}
