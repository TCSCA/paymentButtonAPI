package api.apiB2p.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Out {
    private long codigoError;
    private String descripcionError;
    private long secuencial;

    @JsonProperty("codigoError")
    public long getCodigoError() { return codigoError; }
    @JsonProperty("codigoError")
    public void setCodigoError(long value) { this.codigoError = value; }

    @JsonProperty("descripcionError")
    public String getDescripcionError() { return descripcionError; }
    @JsonProperty("descripcionError")
    public void setDescripcionError(String value) { this.descripcionError = value; }

    @JsonProperty("secuencial")
    public long getSecuencial() { return secuencial; }
    @JsonProperty("secuencial")
    public void setSecuencial(long value) { this.secuencial = value; }
}
