package api.apiInstantTransfer.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class IniciarCreditoInmediatoAPIResponse {
    private Out out;

    @JsonProperty("out")
    public Out getOut() { return out; }
    @JsonProperty("out")
    public void setOut(Out value) { this.out = value; }
}
