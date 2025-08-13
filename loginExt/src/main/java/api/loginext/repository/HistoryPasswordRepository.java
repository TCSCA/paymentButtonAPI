package api.loginext.repository;

import api.loginext.entity.HistoryPasswordEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.LinkedHashMap;
import java.util.List;

@Repository
public interface HistoryPasswordRepository extends JpaRepository<HistoryPasswordEntity, Long> {

    @Query(value = "select historyPassword from HistoryPasswordEntity historyPassword " +
                   "where historyPassword.userEntity.idUser= :idUser " +
                   "order by historyPassword.registerDate DESC")
    List<HistoryPasswordEntity> getLastPasswordFromIdUser(Pageable pageable, @Param("idUser") Long idUser);
    @Query(value = "select history.password from HistoryPasswordEntity as history " +
            "inner join UserEntity as user on user.idUser = history.userEntity.idUser " +
            "where user.idUser = :idUser order by history.registerDate desc")
    List<String> getHistoryRepositoryByIdUser(@Param("idUser") final Long idUser);

}
