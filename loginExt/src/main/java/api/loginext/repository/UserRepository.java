package api.loginext.repository;

import api.loginext.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByUserName(String username);

    @Modifying
    @Query(value = "update UserEntity user set " +
            "user.statusUserEntity.idStatusUser = :idStatusUSer " +
            "where user.idUser = :idUser")
    void updateStatusUser(@Param("idStatusUSer") Long idStatusUSer, @Param("idUser") Long idUser);

    @Query(value = "select  u from UserEntity as u " +
            "where u.userName like :username ")
    UserEntity getByUsername(@Param("username") final String username);

    UserEntity findByIdUser(Long idUser);

   /* @Query(value = "")
    UserEntity getUserByProfile(@Param("idProfile") Long idProfile);*/
}
