package api.authentication.to;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.LinkedHashMap;

public class TermsAndConditionsTo {

    private String urlFile;

    private Boolean status;

    private String type;

    public TermsAndConditionsTo() {
    }

    public TermsAndConditionsTo(String urlFile, Boolean status, String type) {
        this.urlFile = urlFile;
        this.status = status;
        this.type = type;
    }

    public String getUrlFile() {
        return urlFile;
    }

    public void setUrlFile(String urlFile) {
        this.urlFile = urlFile;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
