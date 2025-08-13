package api.apicreditcard.response;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class CreditCardBankResponse {

    private Long code;

    private String message;

    private String state;

    private List<String> cause;

}
