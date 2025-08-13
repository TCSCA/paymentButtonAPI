package api.internalrepository.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

import java.time.OffsetDateTime;

@Entity
@Table(name = "t_terminos_condiciones")
public class TermsAndConditionsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "t_terminos_condiciones_seq")
    @SequenceGenerator(name = "t_terminos_condiciones_seq", sequenceName = "t_terminos_condiciones_seq", allocationSize = 1)
    @Column(name = "id_terminos_condiciones")
    private Long idTermsAndConditions;

    @Column(name = "url_file")
    private String urlFile;

    @Column(name = "fecha_registro")
    private OffsetDateTime registerDate;

    @Column(name = "status")
    private Boolean status;

    @Column(name = "nombre_archivo")
    private String fileName;

    @Column(name = "registrado_por")
    private String registerBy;

    public TermsAndConditionsEntity() {
    }

    public TermsAndConditionsEntity(Long idTermsAndConditions, String urlFile, OffsetDateTime registerDate, Boolean status, String fileName, String registerBy) {
        this.idTermsAndConditions = idTermsAndConditions;
        this.urlFile = urlFile;
        this.registerDate = registerDate;
        this.status = status;
        this.fileName = fileName;
        this.registerBy = registerBy;
    }

    public Long getIdTermsAndConditions() {
        return idTermsAndConditions;
    }

    public void setIdTermsAndConditions(Long idTermsAndConditions) {
        this.idTermsAndConditions = idTermsAndConditions;
    }

    public String getUrlFile() {
        return urlFile;
    }

    public void setUrlFile(String urlFile) {
        this.urlFile = urlFile;
    }

    public OffsetDateTime getRegisterDate() {
        return registerDate;
    }

    public void setRegisterDate(OffsetDateTime registerDate) {
        this.registerDate = registerDate;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getRegisterBy() {
        return registerBy;
    }

    public void setRegisterBy(String registerBy) {
        this.registerBy = registerBy;
    }
}
