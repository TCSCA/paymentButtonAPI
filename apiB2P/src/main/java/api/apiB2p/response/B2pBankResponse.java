package api.apiB2p.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class B2pBankResponse {

    private Boolean success;

    private Message message;

    private Long codigoConfirmacion;

    private Long codigoError;

    private String descripcionError;

    private Long secuencial;

    @JsonProperty("message")
    public Message getMessage() { return message; }
    @JsonProperty("message")
    public void setMessage(Message value) { this.message = value; }
}
