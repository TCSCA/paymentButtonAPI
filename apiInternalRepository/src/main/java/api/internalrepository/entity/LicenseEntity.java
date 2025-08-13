package api.internalrepository.entity;

import com.google.gson.annotations.Expose;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;

@Entity
@Table(name = "t_licencia")
public class LicenseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "t_licencia_seq")
    @SequenceGenerator(name = "t_licencia_seq", sequenceName = "t_licencia_seq", allocationSize = 1)
    @Column(name = "id_licencia")
    @Expose
    private Long idLicense;

    @Column(name = "codigo_licencia")
    private String licenseCode;

    @Column(name = "fecha_emision")
    private LocalDate emisionDate;

    @Column(name = "fecha_vencimiento")
    private LocalDate expireDate;

    @Column(name = "otorgada_por")
    private String giveBy;

    @ManyToOne
    @JoinColumn(name = "id_comercio")
    private CommerceEntity commerceEntity;

    @ManyToOne
    @JoinColumn(name = "id_estado_licencia")
    private StatusLicenseEntity statusLicenseEntity;

    @ManyToOne
    @JoinColumn(name = "id_plan")
    private PlanEntity planEntity;

    @Column(name = "fecha_activacion")
    private LocalDate activationDate;

    public LicenseEntity() {
    }

    public LicenseEntity(Long idLicense, String licenseCode, LocalDate emisionDate, LocalDate expireDate, String giveBy, CommerceEntity commerceEntity, StatusLicenseEntity statusLicenseEntity, PlanEntity planEntity,LocalDate activationDate) {
        this.idLicense = idLicense;
        this.licenseCode = licenseCode;
        this.emisionDate = emisionDate;
        this.expireDate = expireDate;
        this.giveBy = giveBy;
        this.commerceEntity = commerceEntity;
        this.statusLicenseEntity = statusLicenseEntity;
        this.planEntity = planEntity;
        this.activationDate = activationDate;
    }

    public Long getIdLicense() {
        return idLicense;
    }

    public void setIdLicense(Long idLicense) {
        this.idLicense = idLicense;
    }

    public String getLicenseCode() {
        return licenseCode;
    }

    public void setLicenseCode(String licenseCode) {
        this.licenseCode = licenseCode;
    }

    public LocalDate getEmisionDate() {
        return emisionDate;
    }

    public void setEmisionDate(LocalDate emisionDate) {
        this.emisionDate = emisionDate;
    }

    public LocalDate getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(LocalDate expireDate) {
        this.expireDate = expireDate;
    }

    public String getGiveBy() {
        return giveBy;
    }

    public void setGiveBy(String giveBy) {
        this.giveBy = giveBy;
    }

    public CommerceEntity getCommerceEntity() {
        return commerceEntity;
    }

    public void setCommerceEntity(CommerceEntity commerceEntity) {
        this.commerceEntity = commerceEntity;
    }

    public StatusLicenseEntity getStatusLicenseEntity() {
        return statusLicenseEntity;
    }

    public void setStatusLicenseEntity(StatusLicenseEntity statusLicenseEntity) {
        this.statusLicenseEntity = statusLicenseEntity;
    }

    public PlanEntity getPlanEntity() {
        return planEntity;
    }

    public void setPlanEntity(PlanEntity planEntity) {
        this.planEntity = planEntity;
    }

    public LocalDate getActivationDate() {
        return activationDate;
    }

    public void setActivationDate(LocalDate activationDate) {
        this.activationDate = activationDate;
    }
}
