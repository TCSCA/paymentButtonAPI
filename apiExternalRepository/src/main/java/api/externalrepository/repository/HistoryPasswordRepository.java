package api.externalrepository.repository;

import api.externalrepository.entity.HistoryPasswordEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HistoryPasswordRepository extends JpaRepository<HistoryPasswordEntity, Long> {
}
