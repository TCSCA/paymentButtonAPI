package api.internalrepository.repository;

import api.internalrepository.entity.ResultFileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ResultFileRepository extends JpaRepository<ResultFileEntity, Long> {

    @Query(value = "select file from ResultFileEntity as file " +
            "inner join RequirementEntity as requirement on requirement.idRequirement = file.requirementEntity.idRequirement " +
            "inner join CommerceEntity as commerce on commerce.idCommerce = file.commerceEntity.idCommerce " +
            "where commerce.idCommerce = :idCommerce and requirement.idRequirement = :idRequirement")
    ResultFileEntity getResultFileByIdCommerceAndIdRequirement(@Param("idCommerce") final Long idCommerce,
                                                               @Param("idRequirement") final Long idRequirement);

}
