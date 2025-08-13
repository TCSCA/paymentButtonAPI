package api.apiAdminCommerce.request;

public class PreRegisterRequest {

    private Long idPreregister;

    private Long statusPreregister;

    private Long idUser;

    private Long idEconomicActivity;

    private String rejectMotive;

    private Long idCity;

    private String activationDate;

    public PreRegisterRequest() {
    }

    public PreRegisterRequest(Long idPreregister, Long statusPreregister, Long idUser, Long idEconomicActivity, String rejectMotive, Long idCity,String activationDate) {
        this.idPreregister = idPreregister;
        this.statusPreregister = statusPreregister;
        this.idUser = idUser;
        this.idEconomicActivity = idEconomicActivity;
        this.rejectMotive = rejectMotive;
        this.idCity = idCity;
        this.activationDate = activationDate;
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

    public Long getIdCity() {
        return idCity;
    }

    public void setIdCity(Long idCity) {
        this.idCity = idCity;
    }

    public String getActivationDate() {
        return activationDate;
    }

    public void setActivationDate(String activationDate) {
        this.activationDate = activationDate;
    }

}
