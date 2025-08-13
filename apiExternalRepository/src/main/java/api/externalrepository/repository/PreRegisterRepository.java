package api.externalrepository.repository;


import api.externalrepository.entity.PreRegisterEntity;
import jakarta.transaction.Transactional;
import org.springframework.cglib.core.Local;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;

@Repository
public interface PreRegisterRepository extends JpaRepository<PreRegisterEntity, Long> {

    @Modifying
    @Query(value = "update PreRegisterEntity preRegisterEntity set " +
            "preRegisterEntity.statusPreRegisterEntity.idStatusPreRegister = :idStatusPreRegister " +
            "where preRegisterEntity.idPreRegistro = :idPreRegister")
    void updateStatusPreRegisterExt(@Param("idStatusPreRegister") Long idStatusPreRegister,
                                    @Param("idPreRegister") Long idPreRegister);

    PreRegisterEntity findByIdPreRegistro(final Long idPreRegistro);

    PreRegisterEntity findByCommerceDocument(final String rif);

    @Modifying
    @Query(value = "update PreRegisterEntity preRegisterEntity set " +
            "preRegisterEntity.statusPreRegisterEntity.idStatusPreRegister = :idStatusPreRegister, " +
            "preRegisterEntity.rejectMotive = :rejectMotive, " +
            "preRegisterEntity.updateDate =:updateDate " +
            "where preRegisterEntity.idPreRegistro = :idPreRegister")
    void updateStatusPreRegisterExtRejected(@Param("idStatusPreRegister") Long idStatusPreRegister,
                                            @Param("idPreRegister") Long idPreRegister,
                                            @Param("rejectMotive") String rejectMotive, @Param("updateDate")OffsetDateTime updateDate);

    Page<PreRegisterEntity> findByStatusPreRegisterEntity_IdStatusPreRegisterOrderByUpdateDateDesc(Pageable pageable,
                                                                                                     final Long idStatusPreRegister);

    @Query("select preRegister from PreRegisterEntity preRegister " +
            "where preRegister.statusPreRegisterEntity.idStatusPreRegister = :idStatusPreRegister " +
            "order by preRegister.registerDate DESC ")
    List<PreRegisterEntity> getPreregisterByStatusExport(@Param("idStatusPreRegister") Long idStatusPreRegister);

    @Query(value = "select preRegister from PreRegisterEntity preRegister " +
            "where cast( preRegister.registerDate as DATE) BETWEEN (:startDate) AND (:endDate) " +
            "and preRegister.statusPreRegisterEntity.idStatusPreRegister = 1 " +
            "order by preRegister.registerDate DESC")
    Page<PreRegisterEntity> getPreRegisterInformationByDate
            (Pageable pageable, @Param("startDate") Date startDate, @Param("endDate") Date endDate);

    @Query(value = "select preRegister from PreRegisterEntity preRegister " +
            "where cast( preRegister.registerDate as DATE) BETWEEN (:startDate) AND (:endDate) " +
            "and preRegister.statusPreRegisterEntity.idStatusPreRegister = 1 " +
            "order by preRegister.registerDate DESC")
    List<PreRegisterEntity> getPreRegisterInformationByDateExport
            (@Param("startDate") Date startDate, @Param("endDate") Date endDate);

    @Query(value = "select commerce from PreRegisterEntity commerce " +
            "where cast( commerce.registerDate as DATE) BETWEEN (:startDate) AND (:endDate) " +
            "and commerce.statusPreRegisterEntity.idStatusPreRegister = 3 order by commerce.registerDate DESC")
    Page<PreRegisterEntity> getCommerceRejectedInformationByDate
            (Pageable pageable, @Param("startDate") Date startDate, @Param("endDate") Date endDate);

    @Query(value = "select commerce from PreRegisterEntity commerce " +
            "where cast( commerce.registerDate as DATE) BETWEEN (:startDate) AND (:endDate) " +
            "and commerce.statusPreRegisterEntity.idStatusPreRegister = 3 order by commerce.registerDate DESC")
    List<PreRegisterEntity> getCommerceRejectedInformationByDateExport
            (@Param("startDate") Date startDate, @Param("endDate") Date endDate);

    @Query(value = "select preRegister from PreRegisterEntity as preRegister " +
            "inner join StatusPreRegisterEntity as status on status.idStatusPreRegister = preRegister.statusPreRegisterEntity.idStatusPreRegister " +
            "where preRegister.idPreRegistro <> :idPreRegister " +
            "and preRegister.commerceDocument = :commerceDocument " +
            "and status.idStatusPreRegister = :idStatus")
    PreRegisterEntity findByCommerceDocumentAndStatus(@Param("idPreRegister") final Long idPreRegister,
                                                      @Param("commerceDocument") final String commerceDocument,
                                                      @Param("idStatus") final Long idStatus);

    @Query("select preRegister from PreRegisterEntity preRegister " +
            "where cast( preRegister.registerDate as DATE) BETWEEN (:startDate) AND (:endDate) " +
            "and preRegister.statusPreRegisterEntity.idStatusPreRegister = :idStatusPreRegister " +
            "order by preRegister.registerDate DESC ")
    List<PreRegisterEntity> getPreregisterByStatusFilter(@Param("idStatusPreRegister") Long idStatusPreRegister,
                                                         @Param("startDate") LocalDate startDate,
                                                         @Param("endDate") LocalDate endDate);

    @Query("select preRegister from PreRegisterEntity preRegister " +
            "where cast( preRegister.registerDate as DATE) BETWEEN (:startDate) AND (:endDate) " +
            "and preRegister.statusPreRegisterEntity.idStatusPreRegister = :idStatusPreRegister " +
            "and preRegister.commerceDocument like %:filter% " +
            "order by preRegister.registerDate DESC ")
    List<PreRegisterEntity> getPreregisterByRifFilter(@Param("idStatusPreRegister") Long idStatusPreRegister,
                                                      @Param("filter") String filter,
                                                      @Param("startDate") LocalDate startDate,
                                                      @Param("endDate") LocalDate endDate);

    @Query("select preRegister from PreRegisterEntity preRegister " +
            "where cast( preRegister.registerDate as DATE) BETWEEN (:startDate) AND (:endDate) " +
            "and preRegister.statusPreRegisterEntity.idStatusPreRegister = :idStatusPreRegister " +
            "and lower(preRegister.commerceName) like %:filter% " +
            "order by preRegister.registerDate DESC ")
    List<PreRegisterEntity> getPreregisterByCommerceNameFilter(@Param("idStatusPreRegister") Long idStatusPreRegister,
                                                               @Param("filter") String filter,
                                                               @Param("startDate") LocalDate startDate,
                                                               @Param("endDate") LocalDate endDate);

    @Query("select preRegister from PreRegisterEntity preRegister " +
            "where cast( preRegister.registerDate as DATE) BETWEEN (:startDate) AND (:endDate) " +
            "and preRegister.statusPreRegisterEntity.idStatusPreRegister = :idStatusPreRegister " +
            "and lower(preRegister.contactPerson) like %:filter% " +
            "order by preRegister.registerDate DESC ")
    List<PreRegisterEntity> getPreregisterByContactPersonFilter(@Param("idStatusPreRegister") Long idStatusPreRegister,
                                                                @Param("filter") String filter,
                                                                @Param("startDate") LocalDate startDate,
                                                                @Param("endDate") LocalDate endDate);

    @Query("select preRegister from PreRegisterEntity preRegister " +
            "inner join PlanEntity as plan on plan.idPlan = preRegister.planEntity.idPlan " +
            "inner join TypeCommerceEntity as typeCommerce on plan.typeCommerceEntity.idTypeCommerce = typeCommerce.idTypeCommerce " +
            "where cast(preRegister.registerDate as DATE) BETWEEN (:startDate) AND (:endDate) " +
            "and preRegister.statusPreRegisterEntity.idStatusPreRegister = :idStatusPreRegister " +
            "and preRegister.planEntity.typeCommerceEntity.idTypeCommerce = :filter " +
            "order by preRegister.registerDate DESC ")
    List<PreRegisterEntity> getPreregisterByTypeCommerceFilter(@Param("idStatusPreRegister") Long idStatusPreRegister,
                                                               @Param("filter") Long filter,
                                                               @Param("startDate") LocalDate startDate,
                                                               @Param("endDate") LocalDate endDate);

    @Query("select preRegister from PreRegisterEntity preRegister " +
            "inner join PlanEntity as plan on plan.idPlan = preRegister.planEntity.idPlan " +
            "where cast(preRegister.registerDate as DATE) BETWEEN (:startDate) AND (:endDate) " +
            "and preRegister.statusPreRegisterEntity.idStatusPreRegister = :idStatusPreRegister " +
            "and preRegister.planEntity.idPlan = :filter " +
            "order by preRegister.registerDate DESC ")
    List<PreRegisterEntity> getPreregisterByPlanFilter(@Param("idStatusPreRegister") Long idStatusPreRegister,
                                                       @Param("filter") Long filter,
                                                       @Param("startDate") LocalDate startDate,
                                                       @Param("endDate") LocalDate endDate);

}
