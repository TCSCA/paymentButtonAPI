package api.authentication.to;

public class TransaccionTo {

    private Long idTransaccion;

    private String transaccion;

    public TransaccionTo(Long idTransaccion, String transaccion) {
        this.idTransaccion = idTransaccion;
        this.transaccion = transaccion;
    }

    public Long getIdTransaccion() {
        return idTransaccion;
    }

    public void setIdTransaccion(Long idTransaccion) {
        this.idTransaccion = idTransaccion;
    }

    public String getTransaccion() {
        return transaccion;
    }

    public void setTransaccion(String transaccion) {
        this.transaccion = transaccion;
    }
}
