package api.apiB2p.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Envelope {
    private Body body;

    @JsonProperty("Body")
    public Body getBody() { return body; }
    @JsonProperty("Body")
    public void setBody(Body value) { this.body = value; }
}
