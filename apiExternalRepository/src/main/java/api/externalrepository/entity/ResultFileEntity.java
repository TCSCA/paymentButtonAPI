package api.externalrepository.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

import java.time.OffsetDateTime;

@Entity
@Table(name = "t_archivo_recaudos")
public class ResultFileEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "t_archivo_recaudos_seq")
    @SequenceGenerator(name = "t_archivo_recaudos_seq", sequenceName = "t_archivo_recaudos_seq", allocationSize = 1)
    @Column(name = "id_archivo_recaudo")
    private Long idResultFile;

    @Column(name = "size_file")
    private Long size;

    @Column(name = "cargado_por")
    private Long chargedBy;

    @ManyToOne
    @JoinColumn(name = "id_comercio")
    private PreRegisterEntity preRegisterEntity;

    @Column(name = "fecha_carga")
    private OffsetDateTime chargedDate;

    @ManyToOne
    @JoinColumn(name = "id_recaudo")
    private RequirementEntity requirementEntity;

    @Column(name = "archivo")
    private String fileName;

    public ResultFileEntity() {
    }

    public ResultFileEntity(Long idResultFile, Long size, Long chargedBy, PreRegisterEntity preRegisterEntity, OffsetDateTime chargedDate, RequirementEntity requirementEntity, String fileName) {
        this.idResultFile = idResultFile;
        this.size = size;
        this.chargedBy = chargedBy;
        this.preRegisterEntity = preRegisterEntity;
        this.chargedDate = chargedDate;
        this.requirementEntity = requirementEntity;
        this.fileName = fileName;
    }

    public Long getIdResultFile() {
        return idResultFile;
    }

    public void setIdResultFile(Long idResultFile) {
        this.idResultFile = idResultFile;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public Long getChargedBy() {
        return chargedBy;
    }

    public void setChargedBy(Long chargedBy) {
        this.chargedBy = chargedBy;
    }

    public PreRegisterEntity getPreRegisterEntity() {
        return preRegisterEntity;
    }

    public void setPreRegisterEntity(PreRegisterEntity preRegisterEntity) {
        this.preRegisterEntity = preRegisterEntity;
    }

    public OffsetDateTime getChargedDate() {
        return chargedDate;
    }

    public void setChargedDate(OffsetDateTime chargedDate) {
        this.chargedDate = chargedDate;
    }

    public RequirementEntity getRequirementEntity() {
        return requirementEntity;
    }

    public void setRequirementEntity(RequirementEntity requirementEntity) {
        this.requirementEntity = requirementEntity;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
