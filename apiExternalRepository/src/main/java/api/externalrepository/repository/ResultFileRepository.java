package api.externalrepository.repository;

import api.externalrepository.entity.ResultFileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResultFileRepository extends JpaRepository<ResultFileEntity, Long> {

    @Query(value = "select file from ResultFileEntity as file " +
            "inner join RequirementEntity as requirement on requirement.idRequirement = file.requirementEntity.idRequirement " +
            "inner join PreRegisterEntity as pre on pre.idPreRegistro = file.preRegisterEntity.idPreRegistro " +
            "where pre.idPreRegistro = :idPreRegister and requirement.idRequirement = :idRequirement")
    ResultFileEntity getResultFileByIdPreRegisterAndIdRequirement(@Param("idPreRegister") final Long idPreRegister,
                                                                  @Param("idRequirement") final Long idRequirement);

    @Query(value = "select file from ResultFileEntity as file " +
            "inner join RequirementEntity as requirement on requirement.idRequirement = file.requirementEntity.idRequirement " +
            "inner join PreRegisterEntity as pre on pre.idPreRegistro = file.preRegisterEntity.idPreRegistro " +
            "where pre.idPreRegistro = :idPreRegister ")
    List<ResultFileEntity> getAllResultFileByIdPreRegister(@Param("idPreRegister") final Long idPreRegister);

}
