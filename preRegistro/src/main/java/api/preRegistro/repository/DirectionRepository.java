package api.preRegistro.repository;

import api.preRegistro.entity.DirectionEntity;
import api.preRegistro.entity.PreRegistroEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DirectionRepository extends JpaRepository<DirectionEntity, Long> {

}
