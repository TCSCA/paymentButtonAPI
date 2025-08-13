package api.authentication.to;

public class ApprovalUserTo {

    private Boolean status;

    private String url;

    private String name;

    private String file;

    public ApprovalUserTo() {
    }

    public ApprovalUserTo(Boolean status, String url, String name) {
        this.status = status;
        this.url = url;
        this.name = name;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }
}
