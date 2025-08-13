package api.apicommerce.entity;


import lombok.Data;

@Data
public class PaymentLinkEntity {

    private Long idPaymentLink;


    private String paymentLinkCode;


    private String token;

}
