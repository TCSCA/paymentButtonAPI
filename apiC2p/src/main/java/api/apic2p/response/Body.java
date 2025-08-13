package api.apic2p.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Body {
    private RegistrarPagoC2PAPIResponse registrarPagoC2PAPIResponse;

    @JsonProperty("registrarPagoC2pApiResponse")
    public RegistrarPagoC2PAPIResponse getRegistrarPagoC2PAPIResponse() { return registrarPagoC2PAPIResponse; }
    @JsonProperty("registrarPagoC2pApiResponse")
    public void setRegistrarPagoC2PAPIResponse(RegistrarPagoC2PAPIResponse value) { this.registrarPagoC2PAPIResponse = value; }
}
