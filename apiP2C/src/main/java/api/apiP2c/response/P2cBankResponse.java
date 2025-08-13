package api.apiP2c.response;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class P2cBankResponse {

    private Integer codigoConfirmacion;

    private Integer codigoError;

    private String descripcionError;

    private Integer secuencial;
}
