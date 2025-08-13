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

@Entity
@Table(name = "t_direccion")
public class DirectionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "t_direccion_seq")
    @SequenceGenerator(name = "t_direccion_seq", sequenceName = "t_direccion_seq", allocationSize = 1)
    @Column(name = "id_direccion")
    private Long idDirection;

    @Column(name = "direccion_fiscal")
    private String address;

    @ManyToOne()
    @JoinColumn(name = "id_estado")
    private StateEntity stateEntity;

    @Column(name = "status")
    private Boolean status;

    public DirectionEntity() {
    }

    public Long getIdDirection() {
        return idDirection;
    }

    public void setIdDirection(Long idDirection) {
        this.idDirection = idDirection;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public StateEntity getStateEntity() {
        return stateEntity;
    }

    public void setStateEntity(StateEntity stateEntity) {
        this.stateEntity = stateEntity;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }
}
