package api.apiInstantTransfer.response;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class InstantTransferResponse {

    private Integer codigoError;

    private String descripcionError;

    private Integer secuencial;

    private String referencia;

}
