package api.apiP2c.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.OffsetDateTime;

public class ResponseP2C {
    private boolean success;
    private Message message;
    private String cedulaCliente;
    private String fechaTransaccion;
    private String horaTransaccion;
    private String monto;
    private String tlfCliente;
    private long estadoEjecucion;
    private String descripcion;
    private String signo;
    private String codigoConfirmacion;
    private String estadoTransaccion;
    private String concepto;

    @JsonProperty("success")
    public boolean getSuccess() { return success; }
    @JsonProperty("success")
    public void setSuccess(boolean value) { this.success = value; }

    @JsonProperty("message")
    public Message getMessage() { return message; }
    @JsonProperty("message")
    public void setMessage(Message value) { this.message = value; }

    @JsonProperty("cedulaCliente")
    public String getCedulaCliente() { return cedulaCliente; }
    @JsonProperty("cedulaCliente")
    public void setCedulaCliente(String value) { this.cedulaCliente = value; }

    @JsonProperty("fechaTransaccion")
    public String getFechaTransaccion() { return fechaTransaccion; }
    @JsonProperty("fechaTransaccion")
    public void setFechaTransaccion(String value) { this.fechaTransaccion = value; }

    @JsonProperty("horaTransaccion")
    public String getHoraTransaccion() { return horaTransaccion; }
    @JsonProperty("horaTransaccion")
    public void setHoraTransaccion(String value) { this.horaTransaccion = value; }

    @JsonProperty("monto")
    public String getMonto() { return monto; }
    @JsonProperty("monto")
    public void setMonto(String value) { this.monto = value; }

    @JsonProperty("tlfCliente")
    public String getTlfCliente() { return tlfCliente; }
    @JsonProperty("tlfCliente")
    public void setTlfCliente(String value) { this.tlfCliente = value; }

    @JsonProperty("estadoEjecucion")
    public long getEstadoEjecucion() { return estadoEjecucion; }
    @JsonProperty("estadoEjecucion")
    public void setEstadoEjecucion(long value) { this.estadoEjecucion = value; }

    @JsonProperty("descripcion")
    public String getDescripcion() { return descripcion; }
    @JsonProperty("descripcion")
    public void setDescripcion(String value) { this.descripcion = value; }

    @JsonProperty("signo")
    public String getSigno() { return signo; }
    @JsonProperty("signo")
    public void setSigno(String value) { this.signo = value; }

    @JsonProperty("codigoConfirmacion")
    public String getCodigoConfirmacion() { return codigoConfirmacion; }
    @JsonProperty("codigoConfirmacion")
    public void setCodigoConfirmacion(String value) { this.codigoConfirmacion = value; }

    @JsonProperty("estadoTransaccion")
    public String getEstadoTransaccion() { return estadoTransaccion; }
    @JsonProperty("estadoTransaccion")
    public void setEstadoTransaccion(String value) { this.estadoTransaccion = value; }

    @JsonProperty("concepto")
    public String getConcepto() { return concepto; }
    @JsonProperty("concepto")
    public void setConcepto(String value) { this.concepto = value; }
}
