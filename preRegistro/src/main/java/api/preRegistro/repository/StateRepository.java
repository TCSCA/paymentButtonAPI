package api.preRegistro.repository;

import api.preRegistro.entity.StateEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StateRepository extends JpaRepository<StateEntity, Long> {

    @Query("select stateEntity from StateEntity stateEntity " +
            "where stateEntity.countryEntity.idCountry = :idCountry " +
            "and stateEntity.status = true order by stateEntity.stateName asc")
    List<StateEntity> getAllStateByCountry(@Param("idCountry") Long idCountry);
}
