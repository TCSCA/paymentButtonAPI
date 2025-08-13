package api.externalrepository.repository;

import api.externalrepository.entity.UserEntityExt;
import api.externalrepository.to.UsersBlockedTo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserExtRepository extends JpaRepository<UserEntityExt, Long> {


    @Query("SELECT distinct new api.externalrepository.to.UsersBlockedTo(user.idUser,user.userName,user.profileEntity,user.statusUserEntity) FROM UserEntityExt user WHERE user.statusUserEntity.idStatusUser IN (3)")
    Page<UsersBlockedTo> findUsersBlocked(Pageable pageable);

    UserEntityExt findByIdUser(long idUser);

    UserEntityExt findByUserName(final String username);

    @Modifying
    @Query("update UserEntityExt as user set user.statusUserEntity.idStatusUser = :idStatus " +
            "where user.userName in (:usernameList) and user.statusUserEntity.idStatusUser = :currentStatus ")
    void updateAllUserStatusByUsername(@Param("usernameList") final List<String> usernameList,
                                       @Param("idStatus") final Long idStatus,
                                       @Param("currentStatus") final Long currentStatus);

}
