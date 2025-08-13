package api.apiB2p.entity;

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

@Entity
@Table(name = "t_cliente")
public class ClientEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "t_cliente_seq")
    @SequenceGenerator(name = "t_cliente_seq", sequenceName = "t_cliente_seq", allocationSize = 1)
    @Column(name = "id_cliente")
    private Long idClient;

    @Column(name = "nombre_cliente")
    private String clientName;

    @Column(name = "documento_identidad")
    private String identificationDocument;

    @Column(name = "telefono")
    private String phoneNumber;

    @Column(name = "correo")
    private String email;

    @Column(name = "registrado_por")
    private Long registerBy;

    @OneToOne
    @JoinColumn(name = "id_usuario")
    private UserEntity userEntity;

    @ManyToOne
    @JoinColumn(name = "id_comercio")
    private CommerceEntity commerceEntity;

    public ClientEntity() {
    }

    public ClientEntity(Long idClient, String clientName, String identificationDocument, String phoneNumber, String email, Long registerBy, UserEntity userEntity, CommerceEntity commerceEntity) {
        this.idClient = idClient;
        this.clientName = clientName;
        this.identificationDocument = identificationDocument;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.registerBy = registerBy;
        this.userEntity = userEntity;
        this.commerceEntity = commerceEntity;
    }

    public Long getIdClient() {
        return idClient;
    }

    public void setIdClient(Long idClient) {
        this.idClient = idClient;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getIdentificationDocument() {
        return identificationDocument;
    }

    public void setIdentificationDocument(String identificationDocument) {
        this.identificationDocument = identificationDocument;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getRegisterBy() {
        return registerBy;
    }

    public void setRegisterBy(Long registerBy) {
        this.registerBy = registerBy;
    }

    public UserEntity getUserEntity() {
        return userEntity;
    }

    public void setUserEntity(UserEntity userEntity) {
        this.userEntity = userEntity;
    }

    public CommerceEntity getCommerceEntity() {
        return commerceEntity;
    }

    public void setCommerceEntity(CommerceEntity commerceEntity) {
        this.commerceEntity = commerceEntity;
    }
}
