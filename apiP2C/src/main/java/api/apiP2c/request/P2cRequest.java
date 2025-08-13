package api.apiP2c.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class P2cRequest {

    private String rif;

    private String payerDocument;

    private String debitPhone;

    private String transactionAmount;

    private String typeTransaction;

    private String referenceNumber;

    private String paymentReference;

    private String dateTransaction;

    private Long paymentChannel;

    private String bankPayment;

    private String email;

    private Long idBankTransaction;
}
