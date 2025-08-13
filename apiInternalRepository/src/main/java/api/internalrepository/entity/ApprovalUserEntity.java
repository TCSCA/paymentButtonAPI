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
@Table(name = "t_aprobacion_usuario")
public class ApprovalUserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "t_aprobacion_usuario_seq")
    @SequenceGenerator(name = "t_aprobacion_usuario_seq", sequenceName = "t_aprobacion_usuario_seq", allocationSize = 1)
    @Column(name = "id_aprobacion_usuario")
    private Long idApprovalUser;

    @ManyToOne
    @JoinColumn(name = "id_terminos_condiciones")
    private TermsAndConditionsEntity termsAndConditionsEntity;

    @ManyToOne
    @JoinColumn(name = "id_usuario")
    private UserEntity userEntity;

    @Column(name = "fecha_aprobacion")
    private OffsetDateTime approvalDate;

    @Column(name = "fecha_registro")
    private OffsetDateTime registerDate;

    @Column(name = "estado_aprobacion")
    private Boolean approvalStatus;

    public ApprovalUserEntity() {
    }

    public ApprovalUserEntity(Long idApprovalUser, TermsAndConditionsEntity termsAndConditionsEntity, UserEntity userEntity, OffsetDateTime approvalDate, OffsetDateTime registerDate, Boolean approvalStatus) {
        this.idApprovalUser = idApprovalUser;
        this.termsAndConditionsEntity = termsAndConditionsEntity;
        this.userEntity = userEntity;
        this.approvalDate = approvalDate;
        this.registerDate = registerDate;
        this.approvalStatus = approvalStatus;
    }

    public Long getIdApprovalUser() {
        return idApprovalUser;
    }

    public void setIdApprovalUser(Long idApprovalUser) {
        this.idApprovalUser = idApprovalUser;
    }

    public TermsAndConditionsEntity getTermsAndConditionsEntity() {
        return termsAndConditionsEntity;
    }

    public void setTermsAndConditionsEntity(TermsAndConditionsEntity termsAndConditionsEntity) {
        this.termsAndConditionsEntity = termsAndConditionsEntity;
    }

    public UserEntity getUserEntity() {
        return userEntity;
    }

    public void setUserEntity(UserEntity userEntity) {
        this.userEntity = userEntity;
    }

    public OffsetDateTime getApprovalDate() {
        return approvalDate;
    }

    public void setApprovalDate(OffsetDateTime approvalDate) {
        this.approvalDate = approvalDate;
    }

    public OffsetDateTime getRegisterDate() {
        return registerDate;
    }

    public void setRegisterDate(OffsetDateTime registerDate) {
        this.registerDate = registerDate;
    }

    public Boolean getApprovalStatus() {
        return approvalStatus;
    }

    public void setApprovalStatus(Boolean approvalStatus) {
        this.approvalStatus = approvalStatus;
    }

}
