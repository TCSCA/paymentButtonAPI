package api.apiB2p.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class B2pRequest {

    @Size(max = 20)
    private String ipDirection;

    @NotNull
    @Size(max = 4)
    private String creditBank;

    @Size(max = 4)
    private String payingBank;

    private Integer terminalBox;

    @Size (max = 1)
    private String virtualChannel;

    private Integer currencyCode;

    private Long paymentChannel;

    @NotNull
    @Size (max = 50)
    private String concept;

    @NotNull
    @Size (max = 13)
    private String identificationDocument;

    @NotNull
    @Size (max = 12)
    private String transactionAmount;


    @Size (max = 25)
    private String nameCommerce;

    private Integer Office;

    @NotNull
    @Size (max = 20)
    private String rif;

    private Integer branch;


    @Size (max = 11)
    private String debitPhone;

    @NotNull
    @Size (max = 11)
    private String creditPhone;

    @Size (max = 4)
    private String terminalType;

    private Integer seller;

    @Size (max = 10)
    private String bill;
}
