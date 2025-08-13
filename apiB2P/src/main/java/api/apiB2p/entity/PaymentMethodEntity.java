package api.apiB2p.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "t_metodo_pago")
@NoArgsConstructor
public class PaymentMethodEntity {

    @Id
    @Column(name = "id_metodo_pago")
    private Long idPaymentMethod;

    @Column(name = "metodo_pago")
    private String paymentMethod;

    @Column(name = "status")
    private String status;

    public PaymentMethodEntity(Long idPaymentMethod) {
        this.idPaymentMethod = idPaymentMethod;
    }
}
