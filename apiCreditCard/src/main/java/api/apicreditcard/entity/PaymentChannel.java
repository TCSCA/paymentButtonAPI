package api.apicreditcard.entity;

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
    private Long idBank;

    @Column(name = "canal_pago")
    private String bankCode;

    @Column(name = "status")
    private Boolean status;

    public PaymentChannel(Long idBank) {
        this.idBank = idBank;
    }
}
