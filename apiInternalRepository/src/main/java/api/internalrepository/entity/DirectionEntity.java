package api.internalrepository.entity;

import jakarta.persistence.*;

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
    @JoinColumn(name = "id_ciudad")
    private CityEntity cityEntity;

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

    public CityEntity getCityEntity() {
        return cityEntity;
    }

    public void setCityEntity(CityEntity cityEntity) {
        this.cityEntity = cityEntity;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }
}
