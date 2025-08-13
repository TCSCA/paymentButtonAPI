package api.apiAdminCommerce.to;

import java.time.OffsetDateTime;

public class ResultFileDetailTo {

    private Long idResultFile;

    private Long sizeFile;

    private Long chargedBy;

    private Long idCommerce;

    private OffsetDateTime chargedDate;

    private Long idRequirement;

    private String fileName;

    public ResultFileDetailTo() {
    }

    public Long getIdResultFile() {
        return idResultFile;
    }

    public void setIdResultFile(Long idResultFile) {
        this.idResultFile = idResultFile;
    }

    public Long getSizeFile() {
        return sizeFile;
    }

    public void setSizeFile(Long sizeFile) {
        this.sizeFile = sizeFile;
    }

    public Long getChargedBy() {
        return chargedBy;
    }

    public void setChargedBy(Long chargedBy) {
        this.chargedBy = chargedBy;
    }

    public Long getIdCommerce() {
        return idCommerce;
    }

    public void setIdCommerce(Long idCommerce) {
        this.idCommerce = idCommerce;
    }

    public OffsetDateTime getChargedDate() {
        return chargedDate;
    }

    public void setChargedDate(OffsetDateTime chargedDate) {
        this.chargedDate = chargedDate;
    }

    public Long getIdRequirement() {
        return idRequirement;
    }

    public void setIdRequirement(Long idRequirement) {
        this.idRequirement = idRequirement;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
