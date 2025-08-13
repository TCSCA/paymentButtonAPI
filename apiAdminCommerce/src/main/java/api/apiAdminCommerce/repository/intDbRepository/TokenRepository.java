package api.apiAdminCommerce.repository.intDbRepository;

import api.apiAdminCommerce.entity.TokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TokenRepository extends JpaRepository<TokenEntity, Long> {

    TokenEntity findByUserEntity_IdUserAndTokenAndActiveTrue(Long idUser, String token);

    @Query(value = "select tokenEntity from TokenEntity tokenEntity " +
            "inner join UserEntity userEntity on userEntity.idUser = tokenEntity.userEntity.idUser " +
            "inner join ProfileEntity profileEntity on profileEntity.idProfile = userEntity.profileEntity.idProfile " +
            "where tokenEntity.token = :token and userEntity.idUser = :idUser and profileEntity.idProfile = 2 ")
    TokenEntity getTokenByIdUserAndProfileAdmin(@Param("idUser") Long idUser, @Param("token")
                                                String token);

    TokenEntity findByTokenAndActiveTrue(String token);
}
