package api.apicommerce.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "t_metodo_pago")
public class PaymentMethodEntity {

    @Id
    @Column(name = "id_metodo_pago")
    private Long idPaymentMethod;

    @Column(name = "metodo_pago")
    private String paymentMethod;

    @Column(name = "status")
    private String status;

    public PaymentMethodEntity() {
    }

    public PaymentMethodEntity(Long idPaymentMethod) {
        this.idPaymentMethod = idPaymentMethod;
    }

    public PaymentMethodEntity(Long idPaymentMethod, String paymentMethod, String status) {
        this.idPaymentMethod = idPaymentMethod;
        this.paymentMethod = paymentMethod;
        this.status = status;
    }

    public Long getIdPaymentMethod() {
        return idPaymentMethod;
    }

    public void setIdPaymentMethod(Long idPaymentMethod) {
        this.idPaymentMethod = idPaymentMethod;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
