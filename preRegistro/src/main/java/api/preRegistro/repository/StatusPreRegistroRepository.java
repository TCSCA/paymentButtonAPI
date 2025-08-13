package api.preRegistro.repository;

import api.preRegistro.entity.StatusPreRegistroEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StatusPreRegistroRepository extends JpaRepository<StatusPreRegistroEntity, Long> {

    StatusPreRegistroEntity findByIdStatusPreRegistro(Long idPreRegistro);
}
