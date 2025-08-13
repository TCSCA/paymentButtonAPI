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
@NoArgsConstructor
@Table(name = "t_canal_pago")
public class PaymentChannel {

    @Id
    @Column(name = "id_canal_pago")
    @Expose
    private Long idPaymentChannel;

    @Column(name = "canal_pago")
    private String paymentChannel;

    @Column(name = "status")
    private Boolean status;

    public PaymentChannel(Long idPaymentChannel) {
        this.idPaymentChannel = idPaymentChannel;
    }
}
