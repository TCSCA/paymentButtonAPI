package api.apic2p.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RegistrarPagoC2PAPIResponse {
    private Out out;

    @JsonProperty("out")
    public Out getOut() { return out; }
    @JsonProperty("out")
    public void setOut(Out value) { this.out = value; }
}
