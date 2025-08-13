package api.apic2p.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class C2pRequest {

    @Size(max = 32)
    private String externalIdentifier;


    @Size(max = 20)
    private String customerIpAddress;

    @NotNull
    @Size(max = 20)
    private String rif;

    @NotNull
    @Size (max = 11)
    private String phoneNumberCommerce;

    @NotNull
    @Size (max = 25)
    private String nameCommerce;

    @Size (max = 4)
    private String bankCredit;

    @Size (max = 1)
    private String virtualChannel;

    @NotNull
    @Size (max = 13)
    private String identificationDocument;

    @NotNull
    @Size (max = 11)
    private String phoneNumber;

    @NotNull
    @Size (max = 8)
    private String otp;

    @NotNull
    @Size (max = 4)
    private String bankPayment;

    private Integer currencyCode;

    private Long paymentChannel;

    @NotNull
    @Size (max = 12)
    private String transactionAmount;

    private Integer branch;

    private Long terminalBox;

    @Size (max = 4)
    private String terminalType;

    @NotNull
    @Size (max = 40)
    private String concept;

    private Integer seller;

    @Size (max = 1)
    private String cancelTransaction;

    private Long referenceNumber;

    private Long idBankTransaction;

    private String email;
}
