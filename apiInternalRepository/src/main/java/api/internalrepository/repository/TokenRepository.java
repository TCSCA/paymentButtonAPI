package api.internalrepository.repository;

import api.internalrepository.entity.TokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TokenRepository extends JpaRepository<TokenEntity, Long> {

    @Modifying
    @Query(value = "update TokenEntity as token set token.active = false " +
            "where token.userEntity.idUser in (:idUserList)")
    void updateAllTokensByIdUser(@Param("idUserList") final List<Long> idUserList);

}
