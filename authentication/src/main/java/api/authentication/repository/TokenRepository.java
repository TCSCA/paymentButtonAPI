package api.authentication.repository;

import api.authentication.entity.TokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenRepository extends JpaRepository<TokenEntity, Long> {

    TokenEntity findByUserEntity_IdUserAndTokenAndStatusTrue(Long idUser, String token);

    TokenEntity findTopByUserEntity_IdUserAndStatusIsTrueOrderByExpirationDateDesc(Long idUser);

    TokenEntity findByUserEntity_IdUserAndTokenAndStatusTrueOrderByExpirationDateDesc(Long idUser, String token);

    @Modifying
    @Query("update TokenEntity t set t.status = false where t.idUser = :id")
    void deactivateTokenUser(@Param("id") Long id);

}
