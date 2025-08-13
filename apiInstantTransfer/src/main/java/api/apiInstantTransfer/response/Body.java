package api.apiInstantTransfer.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Body {
    private IniciarCreditoInmediatoAPIResponse iniciarCreditoInmediatoAPIResponse;

    @JsonProperty("iniciarCreditoInmediatoAPIResponse")
    public IniciarCreditoInmediatoAPIResponse getRegistrarPagoC2PAPIResponse() { return iniciarCreditoInmediatoAPIResponse; }
    @JsonProperty("iniciarCreditoInmediatoAPIResponse")
    public void setRegistrarPagoC2PAPIResponse(IniciarCreditoInmediatoAPIResponse value) { this.iniciarCreditoInmediatoAPIResponse = value; }
}
