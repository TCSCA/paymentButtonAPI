package api.apiP2c.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RequestQueryPaymentB2P {
    private String rif;
    private String referencia;
    private String montoTransaccion;
    private String identificadorPersona;
    private String telefonoDebito;
    private String factura;
    private String tipoTrx;

    @JsonProperty("rif")
    public String getRif() { return rif; }
    @JsonProperty("rif")
    public void setRif(String value) { this.rif = value; }

    @JsonProperty("referencia")
    public String getReferencia() { return referencia; }
    @JsonProperty("referencia")
    public void setReferencia(String value) { this.referencia = value; }

    @JsonProperty("montoTransaccion")
    public String getMontoTransaccion() { return montoTransaccion; }
    @JsonProperty("montoTransaccion")
    public void setMontoTransaccion(String value) { this.montoTransaccion = value; }

    @JsonProperty("identificadorPersona")
    public String getIdentificadorPersona() { return identificadorPersona; }
    @JsonProperty("identificadorPersona")
    public void setIdentificadorPersona(String value) { this.identificadorPersona = value; }

    @JsonProperty("telefonoDebito")
    public String getTelefonoDebito() { return telefonoDebito; }
    @JsonProperty("telefonoDebito")
    public void setTelefonoDebito(String value) { this.telefonoDebito = value; }

    @JsonProperty("factura")
    public String getFactura() { return factura; }
    @JsonProperty("factura")
    public void setFactura(String value) { this.factura = value; }

    @JsonProperty("tipoTrx")
    public String getTipoTrx() { return tipoTrx; }
    @JsonProperty("tipoTrx")
    public void setTipoTrx(String value) { this.tipoTrx = value; }
}
