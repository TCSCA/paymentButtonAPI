package api.internalrepository.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ManualPaymentRequest {

    private String rif;

    private String payerDocument;

    private String debitPhone;

    private String transactionAmount;

    private String typeTransaction;

    private String bankCode;

    private String referenceNumber;

    private String paymentReference;

    private String dateTransaction;

    private Long paymentChannel;

    private String email;


}
