package api.apiAdminCommerce.entity;

import com.google.gson.annotations.Expose;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "t_licencia")
public class LicenseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "t_licencia_seq")
    @SequenceGenerator(name = "t_licencia_seq", sequenceName = "t_licencia_seq", allocationSize = 1)
    @Column(name = "id_licencia")
    @Expose
    private Long idLicencia;

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

    @ManyToOne()
    @JoinColumn(name = "id_estado_licencia")
    private StatusLicenseEntity statusLicenseEntity;

    @ManyToOne
    @JoinColumn(name = "id_plan")
    private PlanEntity planEntity;

    public LicenseEntity() {
    }

    public Long getIdLicencia() {
        return idLicencia;
    }

    public void setIdLicencia(Long idLicencia) {
        this.idLicencia = idLicencia;
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
}
