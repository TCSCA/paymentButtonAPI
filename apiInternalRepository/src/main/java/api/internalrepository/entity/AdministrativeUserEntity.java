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

import java.time.LocalDate;
import java.time.OffsetDateTime;

@Entity
@Table(name = "t_administrativo")
public class AdministrativeUserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "t_administrativo_seq")
    @SequenceGenerator(name = "t_administrativo_seq", sequenceName = "t_administrativo_seq", allocationSize = 1)
    @Column(name = "id_administrativo")
    private Long idAdministrativeUser;

    @Column(name = "nombre")
    private String name;

    @Column(name = "cedula")
    private String document;

    @Column(name = "numero_telefono")
    private String phoneNumber;

    @Column(name = "correo")
    private String email;

    @ManyToOne
    @JoinColumn(name = "id_usuario")
    private UserEntity userEntity;

    @Column(name = "fecha_registro")
    private OffsetDateTime registerDate;

    @Column(name = "activo")
    private Boolean active;

    public AdministrativeUserEntity(Long idAdministrativeUser, String name, String document, String phoneNumber, String email, UserEntity userEntity, OffsetDateTime registerDate, Boolean active) {
        this.idAdministrativeUser = idAdministrativeUser;
        this.name = name;
        this.document = document;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.userEntity = userEntity;
        this.registerDate = registerDate;
        this.active = active;
    }

    public AdministrativeUserEntity() {

    }

    public Long getIdAdministrativeUser() {
        return idAdministrativeUser;
    }

    public void setIdAdministrativeUser(Long idAdministrativeUser) {
        this.idAdministrativeUser = idAdministrativeUser;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDocument() {
        return document;
    }

    public void setDocument(String document) {
        this.document = document;
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

    public UserEntity getUserEntity() {
        return userEntity;
    }

    public void setUserEntity(UserEntity userEntity) {
        this.userEntity = userEntity;
    }

    public OffsetDateTime getRegisterDate() {
        return registerDate;
    }

    public void setRegisterDate(OffsetDateTime registerDate) {
        this.registerDate = registerDate;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}
