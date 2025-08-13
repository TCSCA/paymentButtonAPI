package api.internalrepository.repository;

import api.internalrepository.entity.RequirementEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RequirementRepository extends JpaRepository<RequirementEntity, Long> {
}
