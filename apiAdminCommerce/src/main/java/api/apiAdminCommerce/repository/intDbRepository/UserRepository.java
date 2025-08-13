package api.apiAdminCommerce.repository.intDbRepository;

import api.apiAdminCommerce.entity.CommerceEntity;
import api.apiAdminCommerce.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    UserEntity findByUserName(String username);

    @Transactional(transactionManager = "primaryTransactionManager")
    UserEntity findByIdUser(Long idUser);

//    @Query(value = "select user from UserEntity user inner join TokenEntity token on " +
//            "user.idUser = token.userEntity.idUser " +
//            "where token.token = :token and token.activo = true")
//    UserEntity getUserEntityByTokenAndStatusTokenTrue(@Param("token") String token);

    @Query("SELECT user FROM UserEntity user INNER JOIN TokenEntity token ON user.idUser = token.userEntity.idUser " +
            "WHERE token.token = :token AND token.active = true")
    UserEntity getUserByToken(@Param("token") String token);

   /* @Query(value = "")
    UserEntity getUserByProfile(@Param("idProfile") Long idProfile);*/

    @Query("select commerce from CommerceEntity as commerce " +
            "where commerce.commerceDocument = :rif")
    CommerceEntity findByCommerceDocument(@Param("rif") String rif);
}