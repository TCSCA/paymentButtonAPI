package api.internalrepository.repository;

import api.internalrepository.entity.PlanEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlanRepository extends JpaRepository<PlanEntity, Long> {

    PlanEntity findByIdPlan(final Long idPlan);

}
