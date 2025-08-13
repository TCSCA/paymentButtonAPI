package api.internalrepository.repository;

import api.internalrepository.entity.ProfileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProfileRepository extends JpaRepository<ProfileEntity, Long> {

    @Query("SELECT pro FROM ProfileEntity pro WHERE pro.typeProfileEntity.idTypeProfile IN (2) and pro.status =true")
    List<ProfileEntity> findProfilesForCommerce();

    @Query("SELECT pro FROM ProfileEntity pro WHERE pro.typeProfileEntity.idTypeProfile IN (1) and pro.status =true")
    List<ProfileEntity> findProfilesForIntelipay();

    @Query(value = "SELECT pro FROM ProfileEntity AS pro " +
            "inner join UserEntity as user on pro.idProfile = user.profileEntity.idProfile " +
            "where user.idUser = :idUser ")
    ProfileEntity getProfileEntityByIdUser(@Param("idUser") final Long idUser);
}
