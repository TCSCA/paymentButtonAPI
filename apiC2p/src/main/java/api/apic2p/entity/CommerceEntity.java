package api.apic2p.entity;

import com.google.gson.annotations.Expose;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.OffsetDateTime;

@Entity
@Table(name = "t_comercio")
public class CommerceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "t_comercio_seq")
    @SequenceGenerator(name = "t_comercio_seq", sequenceName = "t_comercio_seq", allocationSize = 1)
    @Column(name = "id_comercio")
    @Expose
    private Long idCommerce;

    @Column(name = "razon_social")
    private String commerceName;

    @Column(name = "rif")
    private String commerceDocument;

    @Column(name = "url_rif")
    private String commerceDocumentUrl;

    @Column(name = "correo")
    private String commerceEmail;

    @Column(name = "num_telefono")
    private String phoneNumberCommerce;

    @Column(name = "persona_contacto")
    private String contactPerson;

    @Column(name = "correo_persona_contacto")
    private String contactPersonEmail;

    @ManyToOne()
    @Expose
    @JoinColumn(name = "id_estado_registro")
    private StatusCommerceEntity statusCommerce;

    @NotNull
    @ManyToOne()
    @JoinColumn(name = "id_direccion")
    private DirectionEntity directionEntity;

    @Column(name = "fecha_registro")
    private OffsetDateTime registerDate;

    @Column(name = "fecha_actualizacion")
    private OffsetDateTime updateDate;

    @Column(name = "url_documento")
    private String documentUrl;

    public CommerceEntity() {
    }

    public Long getIdCommerce() {
        return idCommerce;
    }

    public void setIdCommerce(Long idCommerce) {
        this.idCommerce = idCommerce;
    }

    public String getCommerceName() {
        return commerceName;
    }

    public void setCommerceName(String commerceName) {
        this.commerceName = commerceName;
    }

    public String getCommerceDocument() {
        return commerceDocument;
    }

    public void setCommerceDocument(String commerceDocument) {
        this.commerceDocument = commerceDocument;
    }

    public String getCommerceDocumentUrl() {
        return commerceDocumentUrl;
    }

    public void setCommerceDocumentUrl(String commerceDocumentUrl) {
        this.commerceDocumentUrl = commerceDocumentUrl;
    }

    public String getCommerceEmail() {
        return commerceEmail;
    }

    public void setCommerceEmail(String commerceEmail) {
        this.commerceEmail = commerceEmail;
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

    public StatusCommerceEntity getStatusCommerce() {
        return statusCommerce;
    }

    public void setStatusCommerce(StatusCommerceEntity statusCommerce) {
        this.statusCommerce = statusCommerce;
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

    public String getDocumentUrl() {
        return documentUrl;
    }

    public void setDocumentUrl(String documentUrl) {
        this.documentUrl = documentUrl;
    }
}
