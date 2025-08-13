package api.apiB2p.repository;

import api.apiB2p.entity.TokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenRepository extends JpaRepository<TokenEntity, Long> {

    TokenEntity findByUserEntity_IdUserAndTokenAndActiveTrue(Long idUser, String token);
}
