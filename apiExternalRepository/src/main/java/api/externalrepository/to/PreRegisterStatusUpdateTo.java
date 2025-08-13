package api.externalrepository.to;

public class PreRegisterStatusUpdateTo {

    private Long idStatus;

    private Long idPreRegister;

    public PreRegisterStatusUpdateTo(Long idStatus, Long idPreRegister) {
        this.idStatus = idStatus;
        this.idPreRegister = idPreRegister;
    }

    public Long getIdStatus() {
        return idStatus;
    }

    public void setIdStatus(Long idStatus) {
        this.idStatus = idStatus;
    }

    public Long getIdPreRegister() {
        return idPreRegister;
    }

    public void setIdPreRegister(Long idPreRegister) {
        this.idPreRegister = idPreRegister;
    }
}
