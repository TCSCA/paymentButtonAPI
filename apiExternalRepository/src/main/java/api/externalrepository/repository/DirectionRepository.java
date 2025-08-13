package api.externalrepository.repository;

import api.externalrepository.entity.DirectionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DirectionRepository extends JpaRepository<DirectionEntity, Long> {
}
