package api.apicommerce.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommerceConfigRequest {

    private Long idCommerce;

    @NotNull
    private String idBank;

    @NotNull
    private String consumerKey;

    @NotNull
    private String consumerSecret;

    @NotNull
    private String rif;

    @NotNull
    private String commercePhone;

    @NotNull
    private String hash;

    private String token;

    private Boolean status;

    private String consumerKeyCreditCard;

    private String consumerSecretCreditCard;

    private String bankAccount;
}

