package api.internalrepository.to;

public class ApprovalUserTo {

    private Boolean approvalStatus;

    private String urlFile;

    private String fileName;

    public ApprovalUserTo() {
    }

    public ApprovalUserTo(Boolean approvalStatus, String urlFile, String fileName) {
        this.approvalStatus = approvalStatus;
        this.urlFile = urlFile;
        this.fileName = fileName;
    }

    public Boolean getApprovalStatus() {
        return approvalStatus;
    }

    public void setApprovalStatus(Boolean approvalStatus) {
        this.approvalStatus = approvalStatus;
    }

    public String getUrlFile() {
        return urlFile;
    }

    public void setUrlFile(String urlFile) {
        this.urlFile = urlFile;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
