package api.apiAdminCommerce.entity;

import com.google.gson.annotations.Expose;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.OffsetDateTime;

@Entity
@Table(name = "t_comercio")
public class PreRegisterEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "t_pre_registro_comercio_seq")
    @SequenceGenerator(name = "t_pre_registro_comercio_seq", sequenceName = "t_pre_registro_comercio_seq", allocationSize = 1)
    @Column(name = "id_comercio")
    @Expose
    private Long idPreRegistro;

    /*@Size(min = 18, max = 65)*/
    @Column(name = "razon_social")
    private String commerceName;

    @Column(name = "correo")
    private String commerceEmail;

    @Column(name = "rif")
    private String commerceDocument;

    @Column(name = "num_telefono")
    private String phoneNumberCommerce;

    @Column(name = "persona_contacto")
    private String contactPerson;

    @Column(name = "correo_persona_contacto")
    private String contactPersonEmail;

    @ManyToOne()
    @Expose
    @JoinColumn(name = "id_estado_registro")
    private StatusPreRegisterEntity statusPreRegisterEntity;

    @ManyToOne()
    @JoinColumn(name = "id_direccion")
    private DirectionEntity directionEntity;

    @Column(name = "fecha_registro")
    private OffsetDateTime registerDate;

    @Column(name = "fecha_actualizacion")
    private OffsetDateTime updateDate;

    @Column(name = "motivo_rechazo")
    private String rejectMotive;

    public PreRegisterEntity() {
    }

    public PreRegisterEntity(Long idPreRegistro, String commerceName, String commerceEmail, String commerceDocument, String phoneNumberCommerce, String contactPerson, String contactPersonEmail, StatusPreRegisterEntity statusPreRegisterEntity, DirectionEntity directionEntity, OffsetDateTime registerDate, OffsetDateTime updateDate, String rejectMotive) {
        this.idPreRegistro = idPreRegistro;
        this.commerceName = commerceName;
        this.commerceEmail = commerceEmail;
        this.commerceDocument = commerceDocument;
        this.phoneNumberCommerce = phoneNumberCommerce;
        this.contactPerson = contactPerson;
        this.contactPersonEmail = contactPersonEmail;
        this.statusPreRegisterEntity = statusPreRegisterEntity;
        this.directionEntity = directionEntity;
        this.registerDate = registerDate;
        this.updateDate = updateDate;
        this.rejectMotive = rejectMotive;
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
}

