package api.apiInstantTransfer.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InstantTransferRequest {

    @NotNull
    @Size(max = 4)
    private String bankPayment;

    @NotNull
    @Size(max = 20)
    private String rif;

    @NotNull
    @Size(max = 13)
    private String paymentDocument;

    @NotNull
    @Size(max = 50)
    private String concept;

    @NotNull
    @Size(max = 20)
    private String accountPayment;

    private Integer currencyPayment;

    private String hash;

    private String externalIdentifier;

    private Long paymentChannel;

    @Size(max = 20)
    private String customerIpAddress;

    @NotNull
    private BigDecimal amount;

    @Size(max = 40)
    private String originName;

    @NotNull
    @Size(max = 40)
    private String paymentName;

    @Size(max = 55)
    private String terminal;

    @Size(max = 55)
    private String typeCtaCele;

}
