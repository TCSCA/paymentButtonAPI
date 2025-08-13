package api.internalrepository.entity;

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
@Table(name = "t_comercio_desvinculado")
public class UnlinkCommerceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "t_comercio_desvinculado_seq")
    @SequenceGenerator(name = "t_comercio_desvinculado_seq", sequenceName = "t_comercio_desvinculado_seq", allocationSize = 1)
    @Column(name = "id_comercio_desvinculado")
    private Long idUnlinkCommerce;

    @ManyToOne()
    @JoinColumn(name = "id_comercio")
    private CommerceEntity commerceEntity;

    @Column(name = "motivo_desvinculacion")
    private String unlinkReason;

    @Column(name = "fecha_registro")
    private OffsetDateTime registerDate;

    @Column(name = "fecha_actualizacion")
    private OffsetDateTime updateDate;

    @Column(name = "status")
    private Boolean status;

    public UnlinkCommerceEntity() {
    }

    public UnlinkCommerceEntity(Long idUnlinkCommerce, CommerceEntity commerceEntity, String unlinkReason, OffsetDateTime registerDate, OffsetDateTime updateDate, Boolean status) {
        this.idUnlinkCommerce = idUnlinkCommerce;
        this.commerceEntity = commerceEntity;
        this.unlinkReason = unlinkReason;
        this.registerDate = registerDate;
        this.updateDate = updateDate;
        this.status = status;
    }

    public Long getIdUnlinkCommerce() {
        return idUnlinkCommerce;
    }

    public void setIdUnlinkCommerce(Long idUnlinkCommerce) {
        this.idUnlinkCommerce = idUnlinkCommerce;
    }

    public CommerceEntity getCommerceEntity() {
        return commerceEntity;
    }

    public void setCommerceEntity(CommerceEntity commerceEntity) {
        this.commerceEntity = commerceEntity;
    }

    public String getUnlinkReason() {
        return unlinkReason;
    }

    public void setUnlinkReason(String unlinkReason) {
        this.unlinkReason = unlinkReason;
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

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }
}
