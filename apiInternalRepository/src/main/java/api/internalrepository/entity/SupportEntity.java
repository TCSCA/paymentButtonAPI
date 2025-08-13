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
@Table(name = "t_soporte")
public class SupportEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "t_soporte_seq")
    @SequenceGenerator(name = "t_soporte_seq", sequenceName = "t_soporte_seq", allocationSize = 1)
    @Column(name = "id_soporte")
    private Long idSupport;

    @Column(name = "solicitud")
    private String request;

    @ManyToOne
    @JoinColumn(name = "id_cliente")
    private ClientEntity clientEntity;

    @ManyToOne
    @JoinColumn(name = "id_administrador")
    private AdministrativeUserEntity administrativeUserEntity;

    @Column(name = "fecha_registro")
    private OffsetDateTime registerDate;

    @Column(name = "fecha_actualizacion")
    private OffsetDateTime updateDate;

    @Column(name = "telefono_contacto")
    private String phoneContactPerson;

    @Column(name = "correo_contactor")
    private String contactEmail;


    public SupportEntity() {
    }

    public SupportEntity(Long idSupport, String request, ClientEntity clientEntity, AdministrativeUserEntity administrativeUserEntity, OffsetDateTime registerDate, OffsetDateTime updateDate, String phoneContactPerson, String contactEmail) {
        this.idSupport = idSupport;
        this.request = request;
        this.clientEntity = clientEntity;
        this.administrativeUserEntity = administrativeUserEntity;
        this.registerDate = registerDate;
        this.updateDate = updateDate;
        this.phoneContactPerson = phoneContactPerson;
        this.contactEmail = contactEmail;
    }

    public Long getIdSupport() {
        return idSupport;
    }

    public void setIdSupport(Long idSupport) {
        this.idSupport = idSupport;
    }

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    public ClientEntity getClientEntity() {
        return clientEntity;
    }

    public void setClientEntity(ClientEntity clientEntity) {
        this.clientEntity = clientEntity;
    }

    public AdministrativeUserEntity getAdministrativeUserEntity() {
        return administrativeUserEntity;
    }

    public void setAdministrativeUserEntity(AdministrativeUserEntity administratorEntity) {
        this.administrativeUserEntity = administratorEntity;
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

    public String getPhoneContactPerson() {
        return phoneContactPerson;
    }

    public void setPhoneContactPerson(String phoneContactPerson) {
        this.phoneContactPerson = phoneContactPerson;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }
}
