package api.internalrepository.repository;

import api.internalrepository.entity.UserEntity;
import api.internalrepository.to.UsersByCommerceTo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    UserEntity findByIdUser(final Long idUser);

    UserEntity findByUserName(final String userName);

    @Query(value = "SELECT DISTINCT new api.internalrepository.to.UsersByCommerceTo(" +
            "user.idUser, client.clientName, client.phoneNumber, client.email, " +
            "client.identificationDocument, user.userName, user.profileEntity, " +
            "user.statusUserEntity, client.registerDate, commerce.commerceName, " +
            "commerce.commerceDocument, history.reasonStatus) " +
            "FROM ClientEntity AS client " +
            "INNER JOIN CommerceEntity AS commerce ON commerce.idCommerce = client.commerceEntity.idCommerce " +
            "INNER JOIN UserEntity AS user ON user.idUser = client.userEntity.idUser " +
            "INNER JOIN HistoryStatusUserEntity AS history ON history.userEntity.idUser = user.idUser " +
            "AND history.idHistoryStatusUser = ( " +
            "    SELECT MAX(h.idHistoryStatusUser) " +
            "    FROM HistoryStatusUserEntity h " +
            "    WHERE h.userEntity.idUser = user.idUser " +
            ") " +
            "WHERE client.clientName IS NOT NULL " +
            "AND client.registerDate IS NOT NULL " +
            "AND client.clientName <> 'null' " +
            "ORDER BY client.registerDate DESC")
    List<UsersByCommerceTo> findUsersByAdmin();

    @Modifying
    @Query(value = "INSERT INTO int_inteligensa.t_historia_estado_usuario (id_historia_estado_usuario, id_usuario, " +
            "id_estado_usuario, motivo_estado, fecha_registro) " +
            "SELECT nextval('int_inteligensa.t_historia_estado_usuario_seq'), u.id_usuario, :idStatus, :reasonStatus, :registerDate " +
            "FROM int_inteligensa.t_usuario u " +
            "JOIN int_inteligensa.t_estado_usuario status ON status.id_estado_usuario = u.id_estado_usuario " +
            "JOIN int_inteligensa.t_cliente c ON u.id_usuario = c.id_usuario " +
            "JOIN int_inteligensa.t_comercio co ON c.id_comercio = co.id_comercio " +
            "WHERE co.rif = :commerceDocument ", nativeQuery = true)
    void updateStatusByCommerceRif(@Param("commerceDocument") final String commerceDocument,
                                   @Param("idStatus") final Long idStatus,
                                   @Param("reasonStatus") final String reasonStatus,
                                   @Param("registerDate") final OffsetDateTime registerDate);

    @Query(value = "select user.userName from UserEntity as user " +
            "inner join ClientEntity as client on user.idUser = client.userEntity.idUser " +
            "inner join CommerceEntity as commerce on commerce.idCommerce = client.commerceEntity.idCommerce " +
            "where commerce.idCommerce = :idCommerce order by user.idUser asc ")
    List<String> getALlUsernamesByIdCommerce(@Param("idCommerce") final Long idCommerce);

    @Query(value = "select user.idUser from UserEntity as user " +
            "inner join ClientEntity as client on user.idUser = client.userEntity.idUser " +
            "inner join CommerceEntity as commerce on commerce.idCommerce = client.commerceEntity.idCommerce " +
            "where commerce.idCommerce = :idCommerce order by user.idUser asc ")
    List<Long> getAllIdUserByIdCommerce(@Param("idCommerce") final Long idCommerce);

    @Modifying
    @Query("update UserEntity as user set user.statusUserEntity.idStatusUser = :idStatus " +
            "where user.userName in (:usernameList) and user.statusUserEntity.idStatusUser = :currentStatus ")
    void updateAllUserStatusByUsername(@Param("usernameList") final List<String> usernameList,
                                       @Param("idStatus") final Long idStatus,
                                       @Param("currentStatus") final Long currentStatus);

}
