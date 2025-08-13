package api.apiAdminCommerce.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommerceConfigRequest {

    @NotNull
    private Long idCommerce;

    @NotNull
    private Long idBank;

    @NotNull
    private String consumerKey;

    @NotNull
    private String consumerSecret;

    @NotNull
    private String rif;

    @NotNull
    private String commercePhone;

    private String token;
}

