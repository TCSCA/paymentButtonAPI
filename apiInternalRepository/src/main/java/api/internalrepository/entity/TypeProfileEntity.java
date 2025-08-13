package api.internalrepository.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
@Entity
@Table(name = "t_tipo_perfil")
public class TypeProfileEntity {

    @Id
    @Column(name = "id_tipo_perfil")
    private Long idTypeProfile;

    @Column(name = "tipo_perfil")
    private String typeProfileDescription;

    public TypeProfileEntity() {
    }

    public TypeProfileEntity(Long idTypeProfile, String typeProfileDescription) {
        this.idTypeProfile = idTypeProfile;
        this.typeProfileDescription = typeProfileDescription;
    }

    public Long getIdTypeProfile() {
        return idTypeProfile;
    }

    public void setIdTypeProfile(Long idTypeProfile) {
        this.idTypeProfile = idTypeProfile;
    }

    public String getTypeProfileDescription() {
        return typeProfileDescription;
    }

    public void setTypeProfileDescription(String typeProfileDescription) {
        this.typeProfileDescription = typeProfileDescription;
    }

    @Override
    public String toString() {
        return "TypeProfileEntity{" +
                "idTypeProfile=" + idTypeProfile +
                ", typeProfileDescription='" + typeProfileDescription + '\'' +
                '}';
    }
}
