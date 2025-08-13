package api.internalrepository.repository;

import api.internalrepository.entity.AdministrativeUserEntity;
import api.internalrepository.to.AdministrativesUserTo;
import api.internalrepository.to.SupportContactTo;
import api.internalrepository.to.UsersByCommerceTo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AdministrativeUserRepository extends JpaRepository<AdministrativeUserEntity, Long> {

    @Query(value = "SELECT DISTINCT new api.internalrepository.to.AdministrativesUserTo(" +
            "user.idUser, admin.name, admin.phoneNumber, admin.email, " +
            "admin.document, user.userName, user.profileEntity, " +
            "user.statusUserEntity, admin.registerDate,  " +
            "history.reasonStatus,'Intelipay') " +
            "FROM AdministrativeUserEntity AS admin " +
            "INNER JOIN UserEntity AS user ON user.idUser = admin.userEntity.idUser " +
            "INNER JOIN HistoryStatusUserEntity AS history ON history.userEntity.idUser = user.idUser " +
            "AND history.idHistoryStatusUser = ( " +
            "    SELECT MAX(h.idHistoryStatusUser) " +
            "    FROM HistoryStatusUserEntity h " +
            "    WHERE h.userEntity.idUser = user.idUser " +
            ") " +
            "ORDER BY admin.registerDate DESC")
    List<AdministrativesUserTo> findAllAdministrativeUsers();

    @Query(value = "SELECT DISTINCT new api.internalrepository.to.AdministrativesUserTo(" +
            "user.idUser, admin.name, admin.phoneNumber, admin.email, " +
            "admin.document, user.userName, user.profileEntity, " +
            "user.statusUserEntity, admin.registerDate, " +
            "history.reasonStatus,'Intelipay') " +
            "FROM AdministrativeUserEntity AS admin " +
            "INNER JOIN UserEntity AS user ON user.idUser = admin.userEntity.idUser " +
            "INNER JOIN HistoryStatusUserEntity AS history ON history.userEntity.idUser = user.idUser " +
            "AND history.idHistoryStatusUser IN ( " +
            "    SELECT MAX(h.idHistoryStatusUser) " +
            "    FROM HistoryStatusUserEntity h " +
            "    WHERE h.userEntity.idUser = user.idUser " +
            ") " +
            "WHERE cast(admin.registerDate as DATE) BETWEEN :startDate AND :endDate " +
            "ORDER BY admin.registerDate DESC")
    Page<AdministrativesUserTo> findRegisterDateByAdmin(
            @Param("startDate") final LocalDate startDate,
            @Param("endDate") final LocalDate endDate,
            Pageable pageable);

    @Query(value = "SELECT DISTINCT new api.internalrepository.to.AdministrativesUserTo(" +
            "user.idUser, admin.name, admin.phoneNumber, admin.email, " +
            "admin.document, user.userName, user.profileEntity, " +
            "user.statusUserEntity, admin.registerDate, " +
            "history.reasonStatus,'Intelipay') " +
            "FROM AdministrativeUserEntity AS admin " +
            "INNER JOIN UserEntity AS user ON user.idUser = admin.userEntity.idUser " +
            "INNER JOIN HistoryStatusUserEntity AS history ON history.userEntity.idUser = user.idUser " +
            "AND history.idHistoryStatusUser IN ( " +
            "    SELECT MAX(h.idHistoryStatusUser) " +
            "    FROM HistoryStatusUserEntity h " +
            "    WHERE h.userEntity.idUser = user.idUser " +
            ") " +
            "WHERE LOWER(admin.name) LIKE LOWER(CONCAT('%', :filterField, '%')) " +
            "ORDER BY admin.registerDate DESC")
    Page<AdministrativesUserTo> findFullNameByAdmin(@Param("filterField") final String filterField, Pageable pageable);

    @Query(value = "SELECT DISTINCT new api.internalrepository.to.AdministrativesUserTo(" +
            "user.idUser, admin.name, admin.phoneNumber, admin.email, " +
            "admin.document, user.userName, user.profileEntity, " +
            "user.statusUserEntity, admin.registerDate, " +
            "history.reasonStatus,'Intelipay') " +
            "FROM AdministrativeUserEntity AS admin " +
            "INNER JOIN UserEntity AS user ON user.idUser = admin.userEntity.idUser " +
            "INNER JOIN HistoryStatusUserEntity AS history ON history.userEntity.idUser = user.idUser " +
            "AND history.idHistoryStatusUser IN ( " +
            "    SELECT MAX(h.idHistoryStatusUser) " +
            "    FROM HistoryStatusUserEntity h " +
            "    WHERE h.userEntity.idUser = user.idUser " +
            ") " +
            "WHERE LOWER(admin.document) LIKE LOWER(CONCAT('%', :filterField, '%')) " +
            "ORDER BY admin.registerDate DESC")
    Page<AdministrativesUserTo> findIdentificationDocumentByAdmin(@Param("filterField") final String filterField, Pageable pageable);

    @Query(value = "SELECT DISTINCT new api.internalrepository.to.AdministrativesUserTo(" +
            "user.idUser, admin.name, admin.phoneNumber, admin.email, " +
            "admin.document, user.userName, user.profileEntity, " +
            "user.statusUserEntity, admin.registerDate, " +
            "history.reasonStatus,'Intelipay') " +
            "FROM AdministrativeUserEntity AS admin " +
            "INNER JOIN UserEntity AS user ON user.idUser = admin.userEntity.idUser " +
            "INNER JOIN HistoryStatusUserEntity AS history ON history.userEntity.idUser = user.idUser " +
            "AND history.idHistoryStatusUser IN ( " +
            "    SELECT MAX(h.idHistoryStatusUser) " +
            "    FROM HistoryStatusUserEntity h " +
            "    WHERE h.userEntity.idUser = user.idUser " +
            ") " +
            "WHERE LOWER(user.userName) LIKE LOWER(CONCAT('%', :filterField, '%')) " +
            "ORDER BY admin.registerDate DESC")
    Page<AdministrativesUserTo> findUsernameByAdmin(@Param("filterField") final String filterField, Pageable pageable);

    @Query(value = "SELECT DISTINCT new api.internalrepository.to.AdministrativesUserTo(" +
            "user.idUser, admin.name, admin.phoneNumber, admin.email, " +
            "admin.document, user.userName, user.profileEntity, " +
            "user.statusUserEntity, admin.registerDate, " +
            "history.reasonStatus,'Intelipay') " +
            "FROM AdministrativeUserEntity AS admin " +
            "INNER JOIN UserEntity AS user ON user.idUser = admin.userEntity.idUser " +
            "INNER JOIN HistoryStatusUserEntity AS history ON history.userEntity.idUser = user.idUser " +
            "AND history.idHistoryStatusUser IN ( " +
            "    SELECT MAX(h.idHistoryStatusUser) " +
            "    FROM HistoryStatusUserEntity h " +
            "    WHERE h.userEntity.idUser = user.idUser " +
            ") " +
            "WHERE LOWER(user.profileEntity.profileDescription) LIKE LOWER(CONCAT('%', :filterField, '%')) " +
            "ORDER BY admin.registerDate DESC")
    Page<AdministrativesUserTo> findProfileByAdmin(@Param("filterField") final String filterField, Pageable pageable);

    @Query(value = "SELECT DISTINCT new api.internalrepository.to.AdministrativesUserTo(" +
            "user.idUser, admin.name, admin.phoneNumber, admin.email, " +
            "admin.document, user.userName, user.profileEntity, " +
            "user.statusUserEntity, admin.registerDate, " +
            "history.reasonStatus,'Intelipay') " +
            "FROM AdministrativeUserEntity AS admin " +
            "INNER JOIN UserEntity AS user ON user.idUser = admin.userEntity.idUser " +
            "INNER JOIN HistoryStatusUserEntity AS history ON history.userEntity.idUser = user.idUser " +
            "AND history.idHistoryStatusUser IN ( " +
            "    SELECT MAX(h.idHistoryStatusUser) " +
            "    FROM HistoryStatusUserEntity h " +
            "    WHERE h.userEntity.idUser = user.idUser " +
            ") " +
            "WHERE user.profileEntity.idProfile IN (2,3,4) " +
            "ORDER BY admin.registerDate DESC")
    Page<AdministrativesUserTo> findCommerceNameByAdmin(@Param("filterField") final String filterField, Pageable pageable);

    @Query(value = "SELECT DISTINCT new api.internalrepository.to.AdministrativesUserTo(" +
            "user.idUser, admin.name, admin.phoneNumber, admin.email, " +
            "admin.document, user.userName, user.profileEntity, " +
            "user.statusUserEntity, admin.registerDate, " +
            "history.reasonStatus,'Intelipay') " +
            "FROM AdministrativeUserEntity AS admin " +
            "INNER JOIN UserEntity AS user ON user.idUser = admin.userEntity.idUser " +
            "INNER JOIN HistoryStatusUserEntity AS history ON history.userEntity.idUser = user.idUser " +
            "AND history.idHistoryStatusUser IN ( " +
            "    SELECT MAX(h.idHistoryStatusUser) " +
            "    FROM HistoryStatusUserEntity h " +
            "    WHERE h.userEntity.idUser = user.idUser " +
            ") " +
            "WHERE LOWER(user.statusUserEntity.statusUserDescription) LIKE LOWER(CONCAT('%', :filterField, '%')) " +
            "ORDER BY admin.registerDate DESC")
    Page<AdministrativesUserTo> findStatusUserByAdmin(@Param("filterField") final String filterField, Pageable pageable);


    AdministrativeUserEntity findByUserEntity_IdUser(final Long idUser);

}
