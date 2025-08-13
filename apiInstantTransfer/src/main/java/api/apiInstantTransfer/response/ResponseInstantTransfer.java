package api.apiInstantTransfer.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ResponseInstantTransfer {
    private Envelope envelope;

    @JsonProperty("Envelope")
    public Envelope getEnvelope() { return envelope; }
    @JsonProperty("Envelope")
    public void setEnvelope(Envelope value) { this.envelope = value; }
}
