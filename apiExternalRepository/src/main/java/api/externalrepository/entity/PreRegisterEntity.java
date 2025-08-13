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
import jakarta.validation.constraints.NotNull;

import java.time.OffsetDateTime;

@Entity
@Table(name = "t_comercio")
public class PreRegisterEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "t_pre_registro_comercio_seq")
    @SequenceGenerator(name = "t_pre_registro_comercio_seq", sequenceName = "t_pre_registro_comercio_seq", allocationSize = 1)
    @Column(name = "id_comercio")
    private Long idPreRegistro;


    @Column(name = "razon_social")
    private String commerceName;

    @Column(name = "correo")
    private String commerceEmail;

    @NotNull
    @Column(name = "rif")
    private String commerceDocument;

    @NotNull
    @Column(name = "num_telefono")
    private String phoneNumberCommerce;

    @NotNull
    @Column(name = "persona_contacto")
    private String contactPerson;

    @NotNull
    @Column(name = "correo_persona_contacto")
    private String contactPersonEmail;

    @Column(name = "documento_persona_contacto")
    private String contactPersonDocument;

    @ManyToOne()
    @JoinColumn(name = "id_estado_registro")
    private StatusPreRegisterEntity statusPreRegisterEntity;

    @NotNull
    @ManyToOne()
    @JoinColumn(name = "id_direccion")
    private DirectionEntity directionEntity;

    @Column(name = "fecha_registro")
    private OffsetDateTime registerDate;

    @Column(name = "fecha_actualizacion")
    private OffsetDateTime updateDate;

    @Column(name = "motivo_rechazo")
    private String rejectMotive;

    @ManyToOne
    @JoinColumn(name = "id_plan")
    private PlanEntity planEntity;

    public PreRegisterEntity() {
    }

    public PreRegisterEntity(Long idPreRegistro, String commerceName, String commerceEmail, String commerceDocument, String phoneNumberCommerce, String contactPerson, String contactPersonEmail, String contactPersonDocument, StatusPreRegisterEntity statusPreRegisterEntity, DirectionEntity directionEntity, OffsetDateTime registerDate, OffsetDateTime updateDate, String rejectMotive, PlanEntity planEntity) {
        this.idPreRegistro = idPreRegistro;
        this.commerceName = commerceName;
        this.commerceEmail = commerceEmail;
        this.commerceDocument = commerceDocument;
        this.phoneNumberCommerce = phoneNumberCommerce;
        this.contactPerson = contactPerson;
        this.contactPersonEmail = contactPersonEmail;
        this.contactPersonDocument = contactPersonDocument;
        this.statusPreRegisterEntity = statusPreRegisterEntity;
        this.directionEntity = directionEntity;
        this.registerDate = registerDate;
        this.updateDate = updateDate;
        this.rejectMotive = rejectMotive;
        this.planEntity = planEntity;
    }

    public Long getIdPreRegistro() {
        return idPreRegistro;
    }

    public void setIdPreRegistro(Long idPreRegistro) {
        this.idPreRegistro = idPreRegistro;
    }

    public String getCommerceName() {
        return commerceName;
    }

    public void setCommerceName(String commerceName) {
        this.commerceName = commerceName;
    }

    public String getCommerceEmail() {
        return commerceEmail;
    }

    public void setCommerceEmail(String commerceEmail) {
        this.commerceEmail = commerceEmail;
    }

    public String getCommerceDocument() {
        return commerceDocument;
    }

    public void setCommerceDocument(String commerceDocument) {
        this.commerceDocument = commerceDocument;
    }

    public String getPhoneNumberCommerce() {
        return phoneNumberCommerce;
    }

    public void setPhoneNumberCommerce(String phoneNumberCommerce) {
        this.phoneNumberCommerce = phoneNumberCommerce;
    }

    public String getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    public String getContactPersonEmail() {
        return contactPersonEmail;
    }

    public void setContactPersonEmail(String contactPersonEmail) {
        this.contactPersonEmail = contactPersonEmail;
    }

    public StatusPreRegisterEntity getStatusPreRegisterEntity() {
        return statusPreRegisterEntity;
    }

    public void setStatusPreRegisterEntity(StatusPreRegisterEntity statusPreRegisterEntity) {
        this.statusPreRegisterEntity = statusPreRegisterEntity;
    }

    public DirectionEntity getDirectionEntity() {
        return directionEntity;
    }

    public void setDirectionEntity(DirectionEntity directionEntity) {
        this.directionEntity = directionEntity;
    }

    public OffsetDateTime getRegisterDate() {
        return registerDate;
    }

    public void setRegisterDate(OffsetDateTime registerDate) {
        this.registerDate = registerDate;
    }

    public OffsetDateTime getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(OffsetDateTime updateDate) {
        this.updateDate = updateDate;
    }

    public String getRejectMotive() {
        return rejectMotive;
    }

    public void setRejectMotive(String rejectMotive) {
        this.rejectMotive = rejectMotive;
    }

    public String getContactPersonDocument() {
        return contactPersonDocument;
    }

    public void setContactPersonDocument(String contactPersonDocument) {
        this.contactPersonDocument = contactPersonDocument;
    }

    public PlanEntity getPlanEntity() {
        return planEntity;
    }

    public void setPlanEntity(PlanEntity planEntity) {
        this.planEntity = planEntity;
    }
}

