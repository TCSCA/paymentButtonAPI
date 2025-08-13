package api.authentication.repository;

import api.authentication.entity.AdministrativeUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdministrativeUserRepository extends JpaRepository<AdministrativeUserEntity, Long> {

    AdministrativeUserEntity findByUserEntity_IdUserAndActiveTrue(Long idUser);
}
