package api.apic2p.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ResponseC2p {
    private Envelope envelope;

    @JsonProperty("Envelope")
    public Envelope getEnvelope() { return envelope; }
    @JsonProperty("Envelope")
    public void setEnvelope(Envelope value) { this.envelope = value; }
}
