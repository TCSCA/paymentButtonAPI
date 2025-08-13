package api.apic2p.response;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class C2pBankResponse {

    private Integer codigoConfirmacion;

    private Integer codigoError;

    private String descripcionError;

    private Integer secuencial;

}
