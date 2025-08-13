package api.apiP2c.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Message {
    private String code;
    private String message;

    @JsonProperty("code")
    public String getCode() { return code; }
    @JsonProperty("code")
    public void setCode(String value) { this.code = value; }

    @JsonProperty("message")
    public String getMessage() { return message; }
    @JsonProperty("message")
    public void setMessage(String value) { this.message = value; }
}
