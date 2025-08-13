package api.preRegistro.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.time.OffsetDateTime;

@Entity
@Table(name = "t_comercio")
public class PreRegistroEntity {

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
    private StatusPreRegistroEntity statusPreRegistroEntity;

    @NotNull
    @ManyToOne()
    @JoinColumn(name = "id_direccion")
    private DirectionEntity directionEntity;

    @Column(name = "fecha_registro")
    private OffsetDateTime registerDate;

    @Column(name = "fecha_actualizacion")
    private OffsetDateTime updateDate;

    @ManyToOne
    @JoinColumn(name = "id_plan")
    private PlanEntity planEntity;

    public PreRegistroEntity() {
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

    public String getContactPersonDocument() {
        return contactPersonDocument;
    }

    public void setContactPersonDocument(String contactPersonDocument) {
        this.contactPersonDocument = contactPersonDocument;
    }

    public StatusPreRegistroEntity getStatusPreRegistroEntity() {
        return statusPreRegistroEntity;
    }

    public void setStatusPreRegistroEntity(StatusPreRegistroEntity statusPreRegistroEntity) {
        this.statusPreRegistroEntity = statusPreRegistroEntity;
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

    public PlanEntity getPlanEntity() {
        return planEntity;
    }

    public void setPlanEntity(PlanEntity planEntity) {
        this.planEntity = planEntity;
    }
}

