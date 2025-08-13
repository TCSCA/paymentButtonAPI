package api.internalrepository.to;

import lombok.Data;
import lombok.NonNull;

public class PreRegisterRequestTo {

    private Long idPreregister;

    private Long statusPreregister;

    private Long idUser;

    private Long idEconomicActivity;

    private String rejectMotive;

    public PreRegisterRequestTo() {
    }

    public Long getIdPreregister() {
        return idPreregister;
    }

    public void setIdPreregister(Long idPreregister) {
        this.idPreregister = idPreregister;
    }

    public Long getStatusPreregister() {
        return statusPreregister;
    }

    public void setStatusPreregister(Long statusPreregister) {
        this.statusPreregister = statusPreregister;
    }

    public Long getIdUser() {
        return idUser;
    }

    public void setIdUser(Long idUser) {
        this.idUser = idUser;
    }

    public Long getIdEconomicActivity() {
        return idEconomicActivity;
    }

    public void setIdEconomicActivity(Long idEconomicActivity) {
        this.idEconomicActivity = idEconomicActivity;
    }

    public String getRejectMotive() {
        return rejectMotive;
    }

    public void setRejectMotive(String rejectMotive) {
        this.rejectMotive = rejectMotive;
    }
}
