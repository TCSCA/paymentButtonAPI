package api.apiAdminCommerce.to;

public class ResultFileTo {

    private Long idResultFile;

    private String fileName;

    private String rif;

    public ResultFileTo() {
    }

    public ResultFileTo(String fileName, String rif) {
        this.fileName = fileName;
        this.rif = rif;
    }

    public ResultFileTo(String fileName) {
        this.fileName = fileName;
    }

    public Long getIdResultFile() {
        return idResultFile;
    }

    public void setIdResultFile(Long idResultFile) {
        this.idResultFile = idResultFile;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getRif() {
        return rif;
    }

    public void setRif(String rif) {
        this.rif = rif;
    }
}
