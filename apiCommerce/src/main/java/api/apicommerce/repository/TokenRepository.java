package api.apicommerce.repository;

import api.apicommerce.entity.TokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenRepository extends JpaRepository<TokenEntity, Long> {

    TokenEntity findByUserEntity_IdUserAndTokenAndActiveTrue(Long idUser, String token);
}
