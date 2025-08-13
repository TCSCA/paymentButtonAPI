package api.externalrepository.repository;

import api.externalrepository.entity.PlanEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlanRepository extends JpaRepository<PlanEntity, Long> {

    @Query(value = "select plan from PlanEntity as plan " +
            "inner join TypeCommerceEntity as type on type.idTypeCommerce = plan.typeCommerceEntity.idTypeCommerce " +
            "where type.idTypeCommerce = :idTypeCommerce ")
    List<PlanEntity> getAllPlanEntityByIdTypeCommerce(@Param("idTypeCommerce") final Long idTypeCommerce);

    PlanEntity findByIdPlan(final Long idPlan);

}
