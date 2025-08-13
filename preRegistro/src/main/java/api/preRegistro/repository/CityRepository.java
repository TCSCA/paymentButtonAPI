package api.preRegistro.repository;

import api.preRegistro.entity.CityEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CityRepository extends JpaRepository<CityEntity, Long> {

    CityEntity findByIdCity(Long idCity);

    @Query("select cityEntity from CityEntity cityEntity " +
            "where cityEntity.municipalityEntity.idMunicipality = :idMunicipality " +
            "and cityEntity.status = true order by cityEntity.cityDescription asc")
    List<CityEntity> getAllCitiesByMunicipality(@Param("idMunicipality") Long idMunicipality);
}
