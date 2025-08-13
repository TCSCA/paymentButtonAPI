package api.apiAdminCommerce.request;

public class FileUploadRequest {

    private String file;
    private Long idPreRegister;
    private Long idRequirement;
    private String fileName;

    private String commerceDocument;

    public FileUploadRequest() {
    }

    public FileUploadRequest(String file, Long idPreRegister, Long idRequirement, String fileName, String commerceDocument) {
        this.file = file;
        this.idPreRegister = idPreRegister;
        this.idRequirement = idRequirement;
        this.fileName = fileName;
        this.commerceDocument = commerceDocument;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public Long getIdPreRegister() {
        return idPreRegister;
    }

    public void setIdPreRegister(Long idPreRegister) {
        this.idPreRegister = idPreRegister;
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

    public String getCommerceDocument() {
        return commerceDocument;
    }

    public void setCommerceDocument(String commerceDocument) {
        this.commerceDocument = commerceDocument;
    }
}
