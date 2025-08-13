package api.internalrepository.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "t_canal_pago")
public class PaymentChannel {

    @Id
    @Column(name = "id_canal_pago")
    private Long idPaymentChannel;

    @Column(name = "canal_pago")
    private String bankCode;

    @Column(name = "status")
    private Boolean status;

    public PaymentChannel() {
    }

    public PaymentChannel(Long idPaymentChannel) {
        this.idPaymentChannel = idPaymentChannel;
    }

    public PaymentChannel(Long idPaymentChannel, String bankCode, Boolean status) {
        this.idPaymentChannel = idPaymentChannel;
        this.bankCode = bankCode;
        this.status = status;
    }

    public Long getIdPaymentChannel() {
        return idPaymentChannel;
    }

    public void setIdPaymentChannel(Long idBank) {
        this.idPaymentChannel = idBank;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }
}
