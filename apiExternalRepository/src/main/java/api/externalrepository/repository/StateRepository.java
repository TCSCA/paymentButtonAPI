package api.externalrepository.repository;

import api.externalrepository.entity.StateEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StateRepository extends JpaRepository<StateEntity, Long> {

    StateEntity findByIdState(final Long idState);
}
