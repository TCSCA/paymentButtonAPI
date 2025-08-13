package api.internalrepository.repository;

import api.internalrepository.entity.ClientEntity;
import api.internalrepository.entity.UserEntity;
import api.internalrepository.to.SupportContactTo;
import api.internalrepository.to.UsersByCommerceTo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;

@Repository
public interface ClientRepository extends JpaRepository<ClientEntity, Long> {

    ClientEntity findByIdClient(final Long idClient);

    @Query(value = "select distinct new api.internalrepository.to.SupportContactTo(client,commerce,sup) from ClientEntity as client " +
            "inner join CommerceEntity as commerce on commerce.idCommerce = client.commerceEntity.idCommerce " +
            "inner join UserEntity as user on user.idUser = client.userEntity.idUser " +
            "inner join SupportEntity  as sup on sup.clientEntity.idClient = client.idClient " +
            "WHERE user.idUser = :idUser ")
    List<SupportContactTo> findByIdUser(@Param("idUser")final Long idUser);

    ClientEntity findByUserEntity_IdUser(long idUser);

    @Query(value = "SELECT distinct new api.internalrepository.to.UsersByCommerceTo(user.idUser, client.clientName, client.phoneNumber, " +
            "client.email, client.identificationDocument, user.userName, user.profileEntity, user.statusUserEntity, " +
            "client.registerDate, commerce.commerceName, commerce.commerceDocument, history.reasonStatus) " +
            "FROM ClientEntity AS client " +
            "INNER JOIN CommerceEntity AS commerce ON commerce.idCommerce = client.commerceEntity.idCommerce " +
            "INNER JOIN UserEntity AS user ON user.idUser = client.userEntity.idUser " +
            "INNER JOIN HistoryStatusUserEntity AS history ON history.userEntity.idUser = user.idUser " +
            "AND history.idHistoryStatusUser IN ( " +
            "    SELECT MAX(h.idHistoryStatusUser) " +
            "    FROM HistoryStatusUserEntity h " +
            "    WHERE h.userEntity.idUser = user.idUser " +
            ") " +
            "WHERE commerce.idCommerce = :idCommerce " +
            "and client.clientName IS NOT NULL and client.registerDate IS NOT NULL " +
            "AND client.clientName <> 'null' " +
            "ORDER BY client.registerDate DESC")
    Page<UsersByCommerceTo> findUsersByCommerce(@Param("idCommerce") final Long idCommerce, Pageable pageable);


    @Query(value = "SELECT DISTINCT new api.internalrepository.to.UsersByCommerceTo(" +
            "user.idUser, client.clientName, client.phoneNumber, client.email, " +
            "client.identificationDocument, user.userName, user.profileEntity, " +
            "user.statusUserEntity, client.registerDate, commerce.commerceName, " +
            "commerce.commerceDocument, history.reasonStatus) " +
            "FROM ClientEntity AS client " +
            "INNER JOIN CommerceEntity AS commerce ON commerce.idCommerce = client.commerceEntity.idCommerce " +
            "INNER JOIN UserEntity AS user ON user.idUser = client.userEntity.idUser " +
            "INNER JOIN HistoryStatusUserEntity AS history ON history.userEntity.idUser = user.idUser " +
            "AND history.idHistoryStatusUser IN ( " +
            "    SELECT MAX(h.idHistoryStatusUser) " +
            "    FROM HistoryStatusUserEntity h " +
            "    WHERE h.userEntity.idUser = user.idUser " +
            ") " +
            "WHERE client.identificationDocument like %:filterField% " +
            "and client.clientName IS NOT NULL and client.registerDate IS NOT NULL " +
            "AND client.clientName <> 'null' " +
            "ORDER BY client.registerDate DESC")
    Page<UsersByCommerceTo> findByIdentificationDocument(@Param("filterField") final String filterField, Pageable pageable);

    @Query(value = "SELECT DISTINCT new api.internalrepository.to.UsersByCommerceTo(" +
            "user.idUser, client.clientName, client.phoneNumber, client.email, " +
            "client.identificationDocument, user.userName, user.profileEntity, " +
            "user.statusUserEntity, client.registerDate, commerce.commerceName, " +
            "commerce.commerceDocument, history.reasonStatus) " +
            "FROM ClientEntity AS client " +
            "INNER JOIN CommerceEntity AS commerce ON commerce.idCommerce = client.commerceEntity.idCommerce " +
            "INNER JOIN UserEntity AS user ON user.idUser = client.userEntity.idUser " +
            "INNER JOIN HistoryStatusUserEntity AS history ON history.userEntity.idUser = user.idUser " +
            "AND history.idHistoryStatusUser IN ( " +
            "    SELECT MAX(h.idHistoryStatusUser) " +
            "    FROM HistoryStatusUserEntity h " +
            "    WHERE h.userEntity.idUser = user.idUser " +
            ") " +
            "WHERE cast(client.registerDate as DATE) BETWEEN :startDate AND :endDate " +
            "AND client.clientName <> 'null' " +
            "AND client.clientName IS NOT NULL AND client.registerDate IS NOT NULL " +
            "ORDER BY client.registerDate DESC")
    Page<UsersByCommerceTo> findByRegisterDate(@Param("startDate") final LocalDate startDate,
                                                      @Param("endDate") final LocalDate endDate,
                                                      Pageable pageable);


    @Query(value = "SELECT DISTINCT new api.internalrepository.to.UsersByCommerceTo(" +
            "user.idUser, client.clientName, client.phoneNumber, client.email, " +
            "client.identificationDocument, user.userName, user.profileEntity, " +
            "user.statusUserEntity, client.registerDate, commerce.commerceName, " +
            "commerce.commerceDocument, history.reasonStatus) " +
            "FROM ClientEntity AS client " +
            "INNER JOIN CommerceEntity AS commerce ON commerce.idCommerce = client.commerceEntity.idCommerce " +
            "INNER JOIN UserEntity AS user ON user.idUser = client.userEntity.idUser " +
            "INNER JOIN HistoryStatusUserEntity AS history ON history.userEntity.idUser = user.idUser " +
            "AND history.idHistoryStatusUser IN ( " +
            "    SELECT MAX(h.idHistoryStatusUser) " +
            "    FROM HistoryStatusUserEntity h " +
            "    WHERE h.userEntity.idUser = user.idUser " +
            ") " +
            "WHERE LOWER(user.userName) LIKE LOWER(CONCAT('%', :filterField, '%')) " +
            "and client.clientName IS NOT NULL and client.registerDate IS NOT NULL " +
            "AND client.clientName <> 'null' " +
            "ORDER BY client.registerDate DESC")
    Page<UsersByCommerceTo> findByUsername(@Param("filterField") final String filterField, Pageable pageable);

    @Query(value = "SELECT DISTINCT new api.internalrepository.to.UsersByCommerceTo(" +
            "user.idUser, client.clientName, client.phoneNumber, client.email, " +
            "client.identificationDocument, user.userName, user.profileEntity, " +
            "user.statusUserEntity, client.registerDate, commerce.commerceName, " +
            "commerce.commerceDocument, history.reasonStatus) " +
            "FROM ClientEntity AS client " +
            "INNER JOIN CommerceEntity AS commerce ON commerce.idCommerce = client.commerceEntity.idCommerce " +
            "INNER JOIN UserEntity AS user ON user.idUser = client.userEntity.idUser " +
            "INNER JOIN HistoryStatusUserEntity AS history ON history.userEntity.idUser = user.idUser " +
            "AND history.idHistoryStatusUser IN ( " +
            "    SELECT MAX(h.idHistoryStatusUser) " +
            "    FROM HistoryStatusUserEntity h " +
            "    WHERE h.userEntity.idUser = user.idUser " +
            ") " +
            "WHERE LOWER(client.clientName) LIKE LOWER(CONCAT('%', :filterField, '%')) " +
            "AND client.clientName IS NOT NULL AND client.registerDate IS NOT NULL " +
            "AND client.clientName <> 'null' " +
            "ORDER BY client.registerDate DESC")
    Page<UsersByCommerceTo> findByFullName(@Param("filterField") final String filterField, Pageable pageable);

    @Query(value = "SELECT DISTINCT new api.internalrepository.to.UsersByCommerceTo(" +
            "user.idUser, client.clientName, client.phoneNumber, client.email, " +
            "client.identificationDocument, user.userName, user.profileEntity, " +
            "user.statusUserEntity, client.registerDate, commerce.commerceName, " +
            "commerce.commerceDocument, history.reasonStatus) " +
            "FROM ClientEntity AS client " +
            "INNER JOIN CommerceEntity AS commerce ON commerce.idCommerce = client.commerceEntity.idCommerce " +
            "INNER JOIN UserEntity AS user ON user.idUser = client.userEntity.idUser " +
            "INNER JOIN HistoryStatusUserEntity AS history ON history.userEntity.idUser = user.idUser " +
            "AND history.idHistoryStatusUser IN ( " +
            "    SELECT MAX(h.idHistoryStatusUser) " +
            "    FROM HistoryStatusUserEntity h " +
            "    WHERE h.userEntity.idUser = user.idUser " +
            ") " +
            "WHERE LOWER(user.profileEntity.profileDescription) LIKE LOWER(CONCAT('%', :filterField, '%')) " +
            "and client.clientName IS NOT NULL and client.registerDate IS NOT NULL " +
            "and client.clientName <> 'null'" +
            "ORDER BY client.registerDate DESC")
    Page<UsersByCommerceTo> findByProfile(@Param("filterField") final String filterField, Pageable pageable);

    @Query(value = "SELECT DISTINCT new api.internalrepository.to.UsersByCommerceTo(" +
            "user.idUser, client.clientName, client.phoneNumber, client.email, " +
            "client.identificationDocument, user.userName, user.profileEntity, " +
            "user.statusUserEntity, client.registerDate, commerce.commerceName, " +
            "commerce.commerceDocument, history.reasonStatus) " +
            "FROM ClientEntity AS client " +
            "INNER JOIN CommerceEntity AS commerce ON commerce.idCommerce = client.commerceEntity.idCommerce " +
            "INNER JOIN UserEntity AS user ON user.idUser = client.userEntity.idUser " +
            "INNER JOIN HistoryStatusUserEntity AS history ON history.userEntity.idUser = user.idUser " +
            "AND history.idHistoryStatusUser IN ( " +
            "    SELECT MAX(h.idHistoryStatusUser) " +
            "    FROM HistoryStatusUserEntity h " +
            "    WHERE h.userEntity.idUser = user.idUser " +
            ") " +
            "WHERE LOWER(commerce.commerceDocument) LIKE LOWER(CONCAT('%', :filterField, '%')) " +
            "and client.clientName IS NOT NULL and client.registerDate IS NOT NULL " +
            "and client.clientName <> 'null' " +
            "ORDER BY client.registerDate DESC")
    Page<UsersByCommerceTo> findByRif(@Param("filterField") final String filterField, Pageable pageable);

    @Query(value = "SELECT DISTINCT new api.internalrepository.to.UsersByCommerceTo(" +
            "user.idUser, client.clientName, client.phoneNumber, client.email, " +
            "client.identificationDocument, user.userName, user.profileEntity, " +
            "user.statusUserEntity, client.registerDate, commerce.commerceName, " +
            "commerce.commerceDocument, history.reasonStatus) " +
            "FROM ClientEntity AS client " +
            "INNER JOIN CommerceEntity AS commerce ON commerce.idCommerce = client.commerceEntity.idCommerce " +
            "INNER JOIN UserEntity AS user ON user.idUser = client.userEntity.idUser " +
            "INNER JOIN HistoryStatusUserEntity AS history ON history.userEntity.idUser = user.idUser " +
            "AND history.idHistoryStatusUser IN ( " +
            "    SELECT MAX(h.idHistoryStatusUser) " +
            "    FROM HistoryStatusUserEntity h " +
            "    WHERE h.userEntity.idUser = user.idUser " +
            ") " +
            "WHERE LOWER(commerce.commerceName) LIKE LOWER(CONCAT('%', :filterField, '%')) " +
            "and client.clientName IS NOT NULL and client.registerDate IS NOT NULL " +
            "and client.clientName <> 'null' " +
            "ORDER BY client.registerDate DESC")
    Page<UsersByCommerceTo> findByCommerceName(@Param("filterField") final String filterField, Pageable pageable);


    @Query(value = "SELECT DISTINCT new api.internalrepository.to.UsersByCommerceTo(" +
            "user.idUser, client.clientName, client.phoneNumber, client.email, " +
            "client.identificationDocument, user.userName, user.profileEntity, " +
            "user.statusUserEntity, client.registerDate, commerce.commerceName, " +
            "commerce.commerceDocument, history.reasonStatus) " +
            "FROM ClientEntity AS client " +
            "INNER JOIN CommerceEntity AS commerce ON commerce.idCommerce = client.commerceEntity.idCommerce " +
            "INNER JOIN UserEntity AS user ON user.idUser = client.userEntity.idUser " +
            "INNER JOIN HistoryStatusUserEntity AS history ON history.userEntity.idUser = user.idUser " +
            "AND history.idHistoryStatusUser IN ( " +
            "    SELECT MAX(h.idHistoryStatusUser) " +
            "    FROM HistoryStatusUserEntity h " +
            "    WHERE h.userEntity.idUser = user.idUser " +
            ") " +
            "WHERE LOWER(user.statusUserEntity.statusUserDescription)  LIKE LOWER(CONCAT('%', :filterField, '%')) " +
            "and client.clientName IS NOT NULL and client.registerDate IS NOT NULL " +
            "and client.clientName <> 'null' " +
            "ORDER BY client.registerDate DESC")
    Page<UsersByCommerceTo> findByStatusUser(@Param("filterField") final String filterField, Pageable pageable);


    @Query(value = "SELECT DISTINCT new api.internalrepository.to.UsersByCommerceTo(" +
            "user.idUser, client.clientName, client.phoneNumber, client.email, " +
            "client.identificationDocument, user.userName, user.profileEntity, " +
            "user.statusUserEntity, client.registerDate, commerce.commerceName, " +
            "commerce.commerceDocument, history.reasonStatus) " +
            "FROM ClientEntity AS client " +
            "INNER JOIN CommerceEntity AS commerce ON commerce.idCommerce = client.commerceEntity.idCommerce " +
            "INNER JOIN UserEntity AS user ON user.idUser = client.userEntity.idUser " +
            "INNER JOIN HistoryStatusUserEntity AS history ON history.userEntity.idUser = user.idUser " +
            "AND history.idHistoryStatusUser IN ( " +
            "    SELECT MAX(h.idHistoryStatusUser) " +
            "    FROM HistoryStatusUserEntity h " +
            "    WHERE h.userEntity.idUser = user.idUser " +
            ") " +
            "WHERE client.identificationDocument like %:filterField% " +
            "AND commerce.idCommerce =:idCommerce " +
            "AND client.clientName IS NOT NULL and client.registerDate IS NOT NULL " +
            "ORDER BY client.registerDate DESC")
    Page<UsersByCommerceTo> findByIdentificationDocumentAndCommerce(
            @Param("filterField") final String filterField,@Param("idCommerce") final Long idCommerce, Pageable pageable);

    @Query(value = "SELECT DISTINCT new api.internalrepository.to.UsersByCommerceTo(" +
            "user.idUser, client.clientName, client.phoneNumber, client.email, " +
            "client.identificationDocument, user.userName, user.profileEntity, " +
            "user.statusUserEntity, client.registerDate, commerce.commerceName, " +
            "commerce.commerceDocument, history.reasonStatus) " +
            "FROM ClientEntity AS client " +
            "INNER JOIN CommerceEntity AS commerce ON commerce.idCommerce = client.commerceEntity.idCommerce " +
            "INNER JOIN UserEntity AS user ON user.idUser = client.userEntity.idUser " +
            "INNER JOIN HistoryStatusUserEntity AS history ON history.userEntity.idUser = user.idUser " +
            "AND history.idHistoryStatusUser IN ( " +
            "    SELECT MAX(h.idHistoryStatusUser) " +
            "    FROM HistoryStatusUserEntity h " +
            "    WHERE h.userEntity.idUser = user.idUser " +
            ") " +
            "WHERE cast(client.registerDate as DATE) BETWEEN :startDate AND :endDate " +
            "AND client.clientName IS NOT NULL AND client.registerDate IS NOT NULL " +
            "AND commerce.idCommerce =:idCommerce " +
            "ORDER BY client.registerDate DESC")
    Page<UsersByCommerceTo> findByRegisterDateAndCommerce(@Param("startDate") final LocalDate startDate,
                                               @Param("endDate") final LocalDate endDate,@Param("idCommerce")  final Long idCommerce,
                                               Pageable pageable);


    @Query(value = "SELECT DISTINCT new api.internalrepository.to.UsersByCommerceTo(" +
            "user.idUser, client.clientName, client.phoneNumber, client.email, " +
            "client.identificationDocument, user.userName, user.profileEntity, " +
            "user.statusUserEntity, client.registerDate, commerce.commerceName, " +
            "commerce.commerceDocument, history.reasonStatus) " +
            "FROM ClientEntity AS client " +
            "INNER JOIN CommerceEntity AS commerce ON commerce.idCommerce = client.commerceEntity.idCommerce " +
            "INNER JOIN UserEntity AS user ON user.idUser = client.userEntity.idUser " +
            "INNER JOIN HistoryStatusUserEntity AS history ON history.userEntity.idUser = user.idUser " +
            "AND history.idHistoryStatusUser IN ( " +
            "    SELECT MAX(h.idHistoryStatusUser) " +
            "    FROM HistoryStatusUserEntity h " +
            "    WHERE h.userEntity.idUser = user.idUser " +
            ") " +
            "WHERE LOWER(user.userName) LIKE LOWER(CONCAT('%', :filterField, '%')) " +
            "AND commerce.idCommerce =:idCommerce " +
            "and client.clientName IS NOT NULL and client.registerDate IS NOT NULL " +
            "AND client.clientName <> 'null' " +
            "ORDER BY client.registerDate DESC")
    Page<UsersByCommerceTo> findByUsernameAndCommerce(@Param("filterField") final String filterField, Pageable pageable,
                                                      @Param("idCommerce") final Long idCommerce);

    @Query(value = "SELECT DISTINCT new api.internalrepository.to.UsersByCommerceTo(" +
            "user.idUser, client.clientName, client.phoneNumber, client.email, " +
            "client.identificationDocument, user.userName, user.profileEntity, " +
            "user.statusUserEntity, client.registerDate, commerce.commerceName, " +
            "commerce.commerceDocument, history.reasonStatus) " +
            "FROM ClientEntity AS client " +
            "INNER JOIN CommerceEntity AS commerce ON commerce.idCommerce = client.commerceEntity.idCommerce " +
            "INNER JOIN UserEntity AS user ON user.idUser = client.userEntity.idUser " +
            "INNER JOIN HistoryStatusUserEntity AS history ON history.userEntity.idUser = user.idUser " +
            "AND history.idHistoryStatusUser IN ( " +
            "    SELECT MAX(h.idHistoryStatusUser) " +
            "    FROM HistoryStatusUserEntity h " +
            "    WHERE h.userEntity.idUser = user.idUser " +
            ") " +
            "WHERE LOWER(client.clientName) LIKE LOWER(CONCAT('%', :filterField, '%')) " +
            "AND commerce.idCommerce =:idCommerce " +
            "AND client.clientName IS NOT NULL AND client.registerDate IS NOT NULL " +
            "AND client.clientName <> 'null' " +
            "ORDER BY client.registerDate DESC")
    Page<UsersByCommerceTo> findByFullNameAndCommerce(@Param("filterField") final String filterField, Pageable pageable,
                                                      @Param("idCommerce")  final Long idCommerce);

    @Query(value = "SELECT DISTINCT new api.internalrepository.to.UsersByCommerceTo(" +
            "user.idUser, client.clientName, client.phoneNumber, client.email, " +
            "client.identificationDocument, user.userName, user.profileEntity, " +
            "user.statusUserEntity, client.registerDate, commerce.commerceName, " +
            "commerce.commerceDocument, history.reasonStatus) " +
            "FROM ClientEntity AS client " +
            "INNER JOIN CommerceEntity AS commerce ON commerce.idCommerce = client.commerceEntity.idCommerce " +
            "INNER JOIN UserEntity AS user ON user.idUser = client.userEntity.idUser " +
            "INNER JOIN HistoryStatusUserEntity AS history ON history.userEntity.idUser = user.idUser " +
            "AND history.idHistoryStatusUser IN ( " +
            "    SELECT MAX(h.idHistoryStatusUser) " +
            "    FROM HistoryStatusUserEntity h " +
            "    WHERE h.userEntity.idUser = user.idUser " +
            ") " +
            "WHERE LOWER(user.profileEntity.profileDescription) LIKE LOWER(CONCAT('%', :filterField, '%')) " +
            "AND commerce.idCommerce =:idCommerce " +
            "and client.clientName IS NOT NULL and client.registerDate IS NOT NULL " +
            "and client.clientName <> 'null'" +
            "ORDER BY client.registerDate DESC")
    Page<UsersByCommerceTo> findByProfileAndCommerce(@Param("filterField") final String filterField, Pageable pageable,
                                                     @Param("idCommerce") final Long idCommerce);

    @Query(value = "SELECT DISTINCT new api.internalrepository.to.UsersByCommerceTo(" +
            "user.idUser, client.clientName, client.phoneNumber, client.email, " +
            "client.identificationDocument, user.userName, user.profileEntity, " +
            "user.statusUserEntity, client.registerDate, commerce.commerceName, " +
            "commerce.commerceDocument, history.reasonStatus) " +
            "FROM ClientEntity AS client " +
            "INNER JOIN CommerceEntity AS commerce ON commerce.idCommerce = client.commerceEntity.idCommerce " +
            "INNER JOIN UserEntity AS user ON user.idUser = client.userEntity.idUser " +
            "INNER JOIN HistoryStatusUserEntity AS history ON history.userEntity.idUser = user.idUser " +
            "AND history.idHistoryStatusUser IN ( " +
            "    SELECT MAX(h.idHistoryStatusUser) " +
            "    FROM HistoryStatusUserEntity h " +
            "    WHERE h.userEntity.idUser = user.idUser " +
            ") " +
            "WHERE LOWER(commerce.commerceDocument) LIKE LOWER(CONCAT('%', :filterField, '%')) " +
            "AND commerce.idCommerce =:idCommerce " +
            "and client.clientName IS NOT NULL and client.registerDate IS NOT NULL " +
            "and client.clientName <> 'null' " +
            "ORDER BY client.registerDate DESC")
    Page<UsersByCommerceTo> findByRifAndCommerce(@Param("filterField") final String filterField,
                                                 @Param("idCommerce")  final Long idCommerce, Pageable pageable);

    @Query(value = "SELECT DISTINCT new api.internalrepository.to.UsersByCommerceTo(" +
            "user.idUser, client.clientName, client.phoneNumber, client.email, " +
            "client.identificationDocument, user.userName, user.profileEntity, " +
            "user.statusUserEntity, client.registerDate, commerce.commerceName, " +
            "commerce.commerceDocument, history.reasonStatus) " +
            "FROM ClientEntity AS client " +
            "INNER JOIN CommerceEntity AS commerce ON commerce.idCommerce = client.commerceEntity.idCommerce " +
            "INNER JOIN UserEntity AS user ON user.idUser = client.userEntity.idUser " +
            "INNER JOIN HistoryStatusUserEntity AS history ON history.userEntity.idUser = user.idUser " +
            "AND history.idHistoryStatusUser IN ( " +
            "    SELECT MAX(h.idHistoryStatusUser) " +
            "    FROM HistoryStatusUserEntity h " +
            "    WHERE h.userEntity.idUser = user.idUser " +
            ") " +
            "WHERE LOWER(commerce.commerceName) LIKE LOWER(CONCAT('%', :filterField, '%')) " +
            "AND commerce.idCommerce =:idCommerce " +
            "and client.clientName IS NOT NULL and client.registerDate IS NOT NULL " +
            "and client.clientName <> 'null' " +
            "ORDER BY client.registerDate DESC")
    Page<UsersByCommerceTo> findByCommerceNameAndCommerce(@Param("filterField") final String filterField,
                                                          @Param("idCommerce")  final Long idCommerce, Pageable pageable);


    @Query(value = "SELECT DISTINCT new api.internalrepository.to.UsersByCommerceTo(" +
            "user.idUser, client.clientName, client.phoneNumber, client.email, " +
            "client.identificationDocument, user.userName, user.profileEntity, " +
            "user.statusUserEntity, client.registerDate, commerce.commerceName, " +
            "commerce.commerceDocument, history.reasonStatus) " +
            "FROM ClientEntity AS client " +
            "INNER JOIN CommerceEntity AS commerce ON commerce.idCommerce = client.commerceEntity.idCommerce " +
            "INNER JOIN UserEntity AS user ON user.idUser = client.userEntity.idUser " +
            "INNER JOIN HistoryStatusUserEntity AS history ON history.userEntity.idUser = user.idUser " +
            "AND history.idHistoryStatusUser IN ( " +
            "    SELECT MAX(h.idHistoryStatusUser) " +
            "    FROM HistoryStatusUserEntity h " +
            "    WHERE h.userEntity.idUser = user.idUser " +
            ") " +
            "WHERE LOWER(user.statusUserEntity.statusUserDescription)  LIKE LOWER(CONCAT('%', :filterField, '%')) " +
            "AND commerce.idCommerce =:idCommerce " +
            "and client.clientName IS NOT NULL and client.registerDate IS NOT NULL " +
            "and client.clientName <> 'null' " +
            "ORDER BY client.registerDate DESC")
    Page<UsersByCommerceTo> findByStatusUserAndCommerce(@Param("filterField") final String filterField,
                                                        @Param("idCommerce") final Long idCommerce, Pageable pageable);
}
