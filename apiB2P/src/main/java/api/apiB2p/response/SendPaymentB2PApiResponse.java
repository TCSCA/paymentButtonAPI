package api.apiB2p.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SendPaymentB2PApiResponse {
    private Out out;

    @JsonProperty("out")
    public Out getOut() { return out; }
    @JsonProperty("out")
    public void setOut(Out value) { this.out = value; }
}
