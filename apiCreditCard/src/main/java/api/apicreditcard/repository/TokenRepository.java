package api.apicreditcard.repository;

import api.apicreditcard.entity.TokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenRepository extends JpaRepository<TokenEntity, Long> {

    TokenEntity findByUserEntity_IdUserAndTokenAndActiveTrue(Long idUser, String token);
}
