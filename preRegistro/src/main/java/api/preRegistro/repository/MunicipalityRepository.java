package api.preRegistro.repository;

import api.preRegistro.entity.MunicipalityEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MunicipalityRepository extends JpaRepository<MunicipalityEntity, Long> {

    @Query("select municipalityEntity from MunicipalityEntity municipalityEntity " +
            "where municipalityEntity.stateEntity.idState = :idState " +
            "and municipalityEntity.status = true order by municipalityEntity.municipalityName asc")
    List<MunicipalityEntity> getAllCitiesByMunicipality(@Param("idState") Long idState);
}
