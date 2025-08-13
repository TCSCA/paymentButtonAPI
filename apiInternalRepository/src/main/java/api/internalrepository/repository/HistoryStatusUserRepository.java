package api.internalrepository.repository;


import api.internalrepository.entity.HistoryStatusUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HistoryStatusUserRepository extends JpaRepository<HistoryStatusUserEntity, Long> {
}
