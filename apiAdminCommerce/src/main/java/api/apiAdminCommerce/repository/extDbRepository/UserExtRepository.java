package api.apiAdminCommerce.repository.extDbRepository;

import api.apiAdminCommerce.entity.UserEntityExt;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface UserExtRepository extends JpaRepository<UserEntityExt, Long> {

    @Transactional(transactionManager = "secondTransactionManager")
    UserEntityExt findByUserName(String username);

    UserEntityExt findByIdUser(Long idUser);

//    @Query(value = "select user from UserEntity user inner join TokenEntity token on " +
//            "user.idUser = token.userEntity.idUser " +
//            "where token.token = :token and token.activo = true")
//    UserEntity getUserEntityByTokenAndStatusTokenTrue(@Param("token") String token);

    @Query("SELECT user FROM UserEntityExt user INNER JOIN TokenEntity token ON user.idUser = token.userEntity.idUser " +
            "WHERE token.token = :token AND token.active = true")
    UserEntityExt getUserByToken(@Param("token") String token);

   /* @Query(value = "")
    UserEntity getUserByProfile(@Param("idProfile") Long idProfile);*/

    @Query("SELECT user FROM UserEntityExt user WHERE user.statusUserEntity.idStatusUser IN (3)")
    Page<UserEntityExt> findUsersBlocked(Pageable pageable);
}