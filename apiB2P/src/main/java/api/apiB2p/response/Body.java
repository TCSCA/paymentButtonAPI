package api.apiB2p.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Body {
    private SendPaymentB2PApiResponse sendPaymentB2PApiResponse;

    @JsonProperty("registrarPagoC2pApiResponse")
    public SendPaymentB2PApiResponse getRegistrarPagoC2PAPIResponse() { return sendPaymentB2PApiResponse; }
    @JsonProperty("registrarPagoC2pApiResponse")
    public void setRegistrarPagoC2PAPIResponse(SendPaymentB2PApiResponse value) { this.sendPaymentB2PApiResponse = value; }
}
