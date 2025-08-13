package api.apiAdminCommerce.repository.intDbRepository;

import api.apiAdminCommerce.entity.BankTransactionEntity;
import api.apiAdminCommerce.to.BankTransactionListTo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BankTransactionRepository extends JpaRepository<BankTransactionEntity, Long> {

    @Query(value = "select distinct new api.apiAdminCommerce.to.BankTransactionListTo(bankTransactionEntity, license) " +
            "from BankTransactionEntity as bankTransactionEntity " +
            "inner join CommerceEntity as com on com.idCommerce = bankTransactionEntity.commerceEntity.idCommerce " +
            "inner join LicenseEntity as license on com.idCommerce = license.commerceEntity.idCommerce " +
            "inner join PlanEntity as plan on plan.idPlan = license.planEntity.idPlan " +
            "inner join ClientEntity as client on com.idCommerce = client.commerceEntity.idCommerce " +
            "inner join TypeCommerceEntity as type on type.idTypeCommerce = com.typeCommerceEntity.idTypeCommerce " +
            "where cast( bankTransactionEntity.registerDate as DATE) BETWEEN (:startDate) AND (:endDate) " +
            "order by bankTransactionEntity.registerDate DESC ")
    Page<BankTransactionListTo> getBankTransactionsByDateIntervalPaginated
            (Pageable pageable, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query(value = "select distinct new api.apiAdminCommerce.to.BankTransactionListTo(bankTransactionEntity, license) " +
            "from BankTransactionEntity as bankTransactionEntity " +
            "inner join CommerceEntity as com on com.idCommerce = bankTransactionEntity.commerceEntity.idCommerce " +
            "inner join LicenseEntity as license on com.idCommerce = license.commerceEntity.idCommerce " +
            "inner join PlanEntity as plan on plan.idPlan = license.planEntity.idPlan " +
            "inner join ClientEntity as client on com.idCommerce = client.commerceEntity.idCommerce " +
            "inner join TypeCommerceEntity as type on type.idTypeCommerce = com.typeCommerceEntity.idTypeCommerce " +
            "order by bankTransactionEntity.registerDate DESC ")
    Page<BankTransactionListTo> getBankTransactionsByDateIntervalPaginatedLast
            (Pageable pageable);

    @Query(value = "select new api.apiAdminCommerce.to.BankTransactionListTo(bankTransactionEntity, license) " +
            "from BankTransactionEntity as bankTransactionEntity " +
            "inner join CommerceEntity as com on com.idCommerce = bankTransactionEntity.commerceEntity.idCommerce " +
            "inner join LicenseEntity as license on com.idCommerce = license.commerceEntity.idCommerce " +
            "inner join PlanEntity as plan on plan.idPlan = license.planEntity.idPlan " +
            "inner join ClientEntity as client on com.idCommerce = client.commerceEntity.idCommerce " +
            "inner join TypeCommerceEntity as type on type.idTypeCommerce = com.typeCommerceEntity.idTypeCommerce " +
            "where bankTransactionEntity.paymentMethodEntity.idPaymentMethod = 8L " +
            "ORDER BY CASE WHEN bankTransactionEntity.transactionStatusEntity.idTransactionStatus = 4L THEN 1L ELSE 2L END," +
            " bankTransactionEntity.registerDate DESC ")
    Page<BankTransactionListTo> getBankTransactionsByDateIntervalPaginatedAndManualPaymentLast
            (Pageable pageable);

    @Query(value = "select new api.apiAdminCommerce.to.BankTransactionListTo(bankTransactionEntity, license) " +
            "from BankTransactionEntity as bankTransactionEntity " +
            "inner join CommerceEntity as com on com.idCommerce = bankTransactionEntity.commerceEntity.idCommerce " +
            "inner join LicenseEntity as license on com.idCommerce = license.commerceEntity.idCommerce " +
            "inner join PlanEntity as plan on plan.idPlan = license.planEntity.idPlan " +
            "inner join ClientEntity as client on com.idCommerce = client.commerceEntity.idCommerce " +
            "inner join TypeCommerceEntity as type on type.idTypeCommerce = com.typeCommerceEntity.idTypeCommerce " +
            "where bankTransactionEntity.paymentMethodEntity.idPaymentMethod = 8L " +
            "AND bankTransactionEntity.commerceEntity.idCommerce = :idCommerce " +
            "ORDER BY CASE WHEN bankTransactionEntity.transactionStatusEntity.idTransactionStatus = 4L THEN 1L ELSE 2L END," +
            " bankTransactionEntity.registerDate DESC ")
    Page<BankTransactionListTo> getBankTransactionsByDateIntervalPaginatedAndManualPaymentByCommerceLast
            (Pageable pageable, @Param("idCommerce") Long idCommerce);

    @Query(value = "select distinct new api.apiAdminCommerce.to.BankTransactionListTo(bankTransactionEntity, license) " +
            "from BankTransactionEntity as bankTransactionEntity " +
            "inner join CommerceEntity as com on com.idCommerce = bankTransactionEntity.commerceEntity.idCommerce " +
            "inner join LicenseEntity as license on com.idCommerce = license.commerceEntity.idCommerce " +
            "inner join PlanEntity as plan on plan.idPlan = license.planEntity.idPlan " +
            "inner join ClientEntity as client on com.idCommerce = client.commerceEntity.idCommerce " +
            "inner join TypeCommerceEntity as type on type.idTypeCommerce = com.typeCommerceEntity.idTypeCommerce " +
            "where bankTransactionEntity.commerceEntity.idCommerce = :idCommerce " +
            "order by bankTransactionEntity.registerDate DESC ")
    Page<BankTransactionListTo> getBankTransactionsByDateIntervalPaginatedLastByCommerce
            (Pageable pageable, @Param("idCommerce") Long idCommerce);

    @Query(value = "select distinct new api.apiAdminCommerce.to.BankTransactionListTo(bankTransactionEntity, license) " +
            "from BankTransactionEntity as bankTransactionEntity " +
            "inner join CommerceEntity as com on com.idCommerce = bankTransactionEntity.commerceEntity.idCommerce " +
            "inner join LicenseEntity as license on com.idCommerce = license.commerceEntity.idCommerce " +
            "inner join PlanEntity as plan on plan.idPlan = license.planEntity.idPlan " +
            "inner join ClientEntity as client on com.idCommerce = client.commerceEntity.idCommerce " +
            "inner join TypeCommerceEntity as type on type.idTypeCommerce = com.typeCommerceEntity.idTypeCommerce " +
            "where cast( bankTransactionEntity.registerDate as DATE) BETWEEN (:startDate) AND (:endDate) " +
            "order by bankTransactionEntity.registerDate DESC ")
    List<BankTransactionListTo> getBankTransactionsExport(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query(value = "select distinct new api.apiAdminCommerce.to.BankTransactionListTo(bankTransactionEntity, license) " +
            "from BankTransactionEntity as bankTransactionEntity " +
            "inner join CommerceEntity as com on com.idCommerce = bankTransactionEntity.commerceEntity.idCommerce " +
            "inner join LicenseEntity as license on com.idCommerce = license.commerceEntity.idCommerce " +
            "inner join PlanEntity as plan on plan.idPlan = license.planEntity.idPlan " +
            "inner join ClientEntity as client on com.idCommerce = client.commerceEntity.idCommerce " +
            "inner join TypeCommerceEntity as type on type.idTypeCommerce = com.typeCommerceEntity.idTypeCommerce " +
            "where cast( bankTransactionEntity.registerDate as DATE) BETWEEN (:startDate) AND (:endDate) " +
            "AND bankTransactionEntity.commerceEntity.idCommerce = :idCommerce " +
            "order by bankTransactionEntity.registerDate DESC")
    Page<BankTransactionEntity> getBankTransactionsByDateIntervalPaginatedAndByIdCommerce
            (Pageable pageable, @Param("startDate") LocalDate startDate,
             @Param("endDate") LocalDate endDate, @Param("idCommerce") Long idCommerce);

    @Query(value = "select distinct new api.apiAdminCommerce.to.BankTransactionListTo(bankTransactionEntity, license) " +
            "from BankTransactionEntity as bankTransactionEntity " +
            "inner join CommerceEntity as com on com.idCommerce = bankTransactionEntity.commerceEntity.idCommerce " +
            "inner join LicenseEntity as license on com.idCommerce = license.commerceEntity.idCommerce " +
            "inner join PlanEntity as plan on plan.idPlan = license.planEntity.idPlan " +
            "inner join ClientEntity as client on com.idCommerce = client.commerceEntity.idCommerce " +
            "inner join TypeCommerceEntity as type on type.idTypeCommerce = com.typeCommerceEntity.idTypeCommerce " +
            "where cast( bankTransactionEntity.registerDate as DATE) BETWEEN (:startDate) AND (:endDate) " +
            "AND bankTransactionEntity.commerceEntity.idCommerce = :idCommerce " +
            "order by bankTransactionEntity.registerDate DESC")
    List<BankTransactionListTo> getBankTransactionsByIdCommerceExport
            (@Param("startDate") LocalDate startDate,
             @Param("endDate") LocalDate endDate,
             @Param("idCommerce") Long idCommerce);

    @Query(value = "select distinct new api.apiAdminCommerce.to.BankTransactionListTo(bankTransactionEntity, license) " +
            "from BankTransactionEntity as bankTransactionEntity " +
            "inner join CommerceEntity as com on com.idCommerce = bankTransactionEntity.commerceEntity.idCommerce " +
            "inner join LicenseEntity as license on com.idCommerce = license.commerceEntity.idCommerce " +
            "inner join PlanEntity as plan on plan.idPlan = license.planEntity.idPlan " +
            "inner join ClientEntity as client on com.idCommerce = client.commerceEntity.idCommerce " +
            "inner join TypeCommerceEntity as type on type.idTypeCommerce = com.typeCommerceEntity.idTypeCommerce " +
            "where cast( bankTransactionEntity.registerDate as DATE) BETWEEN (:startDate) AND (:endDate) " +
            "AND bankTransactionEntity.commerceEntity.idCommerce = :idCommerce " +
            "AND bankTransactionEntity.paymentMethodEntity.idPaymentMethod = :idPaymentMethod " +
            "order by bankTransactionEntity.registerDate DESC")
    Page<BankTransactionEntity> getBankTransactionsByDateIntervalPaginatedAndByIdCommerceAndPaymentMethod
            (Pageable pageable, @Param("startDate") LocalDate startDate,
             @Param("endDate") LocalDate endDate, @Param("idCommerce") Long idCommerce, @Param("idPaymentMethod") Long idPaymentMethod);

    @Query(value = "select distinct new api.apiAdminCommerce.to.BankTransactionListTo(bankTransactionEntity, license) " +
            "from BankTransactionEntity as bankTransactionEntity " +
            "inner join CommerceEntity as com on com.idCommerce = bankTransactionEntity.commerceEntity.idCommerce " +
            "inner join LicenseEntity as license on com.idCommerce = license.commerceEntity.idCommerce " +
            "inner join PlanEntity as plan on plan.idPlan = license.planEntity.idPlan " +
            "inner join ClientEntity as client on com.idCommerce = client.commerceEntity.idCommerce " +
            "inner join TypeCommerceEntity as type on type.idTypeCommerce = com.typeCommerceEntity.idTypeCommerce " +
            "where cast( bankTransactionEntity.registerDate as DATE) BETWEEN (:startDate) AND (:endDate) " +
            "AND bankTransactionEntity.commerceEntity.idCommerce = :idCommerce " +
            "AND bankTransactionEntity.paymentMethodEntity.idPaymentMethod = :idPaymentMethod " +
            "order by bankTransactionEntity.registerDate DESC")
    List<BankTransactionListTo> getBankTransactionsByIdCommerceAndPaymentMethodExport
            (@Param("startDate") LocalDate startDate,
             @Param("endDate") LocalDate endDate,
             @Param("idCommerce") Long idCommerce,
             @Param("idPaymentMethod") Long idPaymentMethod);

    @Query(value = "select distinct new api.apiAdminCommerce.to.BankTransactionListTo(bankTransactionEntity, license) " +
            "from BankTransactionEntity as bankTransactionEntity " +
            "inner join CommerceEntity as com on com.idCommerce = bankTransactionEntity.commerceEntity.idCommerce " +
            "inner join LicenseEntity as license on com.idCommerce = license.commerceEntity.idCommerce " +
            "inner join PlanEntity as plan on plan.idPlan = license.planEntity.idPlan " +
            "inner join ClientEntity as client on com.idCommerce = client.commerceEntity.idCommerce " +
            "inner join TypeCommerceEntity as type on type.idTypeCommerce = com.typeCommerceEntity.idTypeCommerce " +
            "where cast( bankTransactionEntity.registerDate as DATE) BETWEEN (:startDate) AND (:endDate) " +
            "AND bankTransactionEntity.paymentMethodEntity.idPaymentMethod = :idPaymentMethod " +
            "order by bankTransactionEntity.registerDate DESC")
    Page<BankTransactionEntity> getBankTransactionsByDateIntervalPaginatedAndByPaymentMethod
            (Pageable pageable, @Param("startDate") LocalDate startDate,
             @Param("endDate") LocalDate endDate, @Param("idPaymentMethod") Long idPaymentMethod);

    @Query(value = "select distinct new api.apiAdminCommerce.to.BankTransactionListTo(bankTransactionEntity, license) " +
            "from BankTransactionEntity as bankTransactionEntity " +
            "inner join CommerceEntity as com on com.idCommerce = bankTransactionEntity.commerceEntity.idCommerce " +
            "inner join LicenseEntity as license on com.idCommerce = license.commerceEntity.idCommerce " +
            "inner join PlanEntity as plan on plan.idPlan = license.planEntity.idPlan " +
            "inner join ClientEntity as client on com.idCommerce = client.commerceEntity.idCommerce " +
            "inner join TypeCommerceEntity as type on type.idTypeCommerce = com.typeCommerceEntity.idTypeCommerce " +
            "where cast( bankTransactionEntity.registerDate as DATE) BETWEEN (:startDate) AND (:endDate) " +
            "AND bankTransactionEntity.paymentMethodEntity.idPaymentMethod = :idPaymentMethod " +
            "order by bankTransactionEntity.registerDate DESC")
    List<BankTransactionListTo> getBankTransactionsByPaymentMethodExport
            ( @Param("startDate") LocalDate startDate,
              @Param("endDate") LocalDate endDate,
              @Param("idPaymentMethod") Long idPaymentMethod);

    @Query(value = "select distinct new api.apiAdminCommerce.to.BankTransactionListTo(bankTransactionEntity, license) " +
            "from BankTransactionEntity as bankTransactionEntity " +
            "inner join CommerceEntity as com on com.idCommerce = bankTransactionEntity.commerceEntity.idCommerce " +
            "inner join LicenseEntity as license on com.idCommerce = license.commerceEntity.idCommerce " +
            "inner join PlanEntity as plan on plan.idPlan = license.planEntity.idPlan " +
            "inner join ClientEntity as client on com.idCommerce = client.commerceEntity.idCommerce " +
            "inner join TypeCommerceEntity as type on type.idTypeCommerce = com.typeCommerceEntity.idTypeCommerce " +
            "where cast( bankTransactionEntity.registerDate as DATE) BETWEEN (:startDate) AND (:endDate) " +
            "AND bankTransactionEntity.paymentChannel.idPaymentChannel = :idPaymentChannel " +
            "order by bankTransactionEntity.registerDate DESC")
    Page<BankTransactionEntity> getBankTransactionsByDateIntervalPaginatedAndByProductType
            (Pageable pageable, @Param("startDate") LocalDate startDate,
             @Param("endDate") LocalDate endDate, @Param("idPaymentChannel") Long idPaymentChannel);

    @Query(value = "select distinct new api.apiAdminCommerce.to.BankTransactionListTo(bankTransactionEntity, license) " +
            "from BankTransactionEntity as bankTransactionEntity " +
            "inner join CommerceEntity as com on com.idCommerce = bankTransactionEntity.commerceEntity.idCommerce " +
            "inner join LicenseEntity as license on com.idCommerce = license.commerceEntity.idCommerce " +
            "inner join PlanEntity as plan on plan.idPlan = license.planEntity.idPlan " +
            "inner join ClientEntity as client on com.idCommerce = client.commerceEntity.idCommerce " +
            "inner join TypeCommerceEntity as type on type.idTypeCommerce = com.typeCommerceEntity.idTypeCommerce " +
            "where cast( bankTransactionEntity.registerDate as DATE) BETWEEN (:startDate) AND (:endDate) " +
            "AND bankTransactionEntity.paymentChannel.idPaymentChannel = :idPaymentChannel " +
            "order by bankTransactionEntity.registerDate DESC")
    List<BankTransactionListTo> getBankTransactionsByProductTypeExport
            (@Param("startDate") LocalDate startDate,
             @Param("endDate") LocalDate endDate,
             @Param("idPaymentChannel") Long idPaymentChannel);

    @Query(value = "select distinct new api.apiAdminCommerce.to.BankTransactionListTo(bankTransactionEntity, license) " +
            "from BankTransactionEntity as bankTransactionEntity " +
            "inner join CommerceEntity as com on com.idCommerce = bankTransactionEntity.commerceEntity.idCommerce " +
            "inner join LicenseEntity as license on com.idCommerce = license.commerceEntity.idCommerce " +
            "inner join PlanEntity as plan on plan.idPlan = license.planEntity.idPlan " +
            "inner join ClientEntity as client on com.idCommerce = client.commerceEntity.idCommerce " +
            "inner join TypeCommerceEntity as type on type.idTypeCommerce = com.typeCommerceEntity.idTypeCommerce " +
            "where cast( bankTransactionEntity.registerDate as DATE) BETWEEN (:startDate) AND (:endDate) " +
            "AND bankTransactionEntity.commerceEntity.idCommerce = :idCommerce " +
            "AND bankTransactionEntity.paymentChannel.idPaymentChannel = :idPaymentChannel " +
            "order by bankTransactionEntity.registerDate DESC")
    Page<BankTransactionEntity> getBankTransactionsByDateIntervalPaginatedAndByIdCommerceAndProductType
            (Pageable pageable, @Param("startDate") LocalDate startDate,
             @Param("endDate") LocalDate endDate, @Param("idCommerce") Long idCommerce, @Param("idPaymentChannel") Long idPaymentChannel);

    @Query(value = "select distinct new api.apiAdminCommerce.to.BankTransactionListTo(bankTransactionEntity, license) " +
            "from BankTransactionEntity as bankTransactionEntity " +
            "inner join CommerceEntity as com on com.idCommerce = bankTransactionEntity.commerceEntity.idCommerce " +
            "inner join LicenseEntity as license on com.idCommerce = license.commerceEntity.idCommerce " +
            "inner join PlanEntity as plan on plan.idPlan = license.planEntity.idPlan " +
            "inner join ClientEntity as client on com.idCommerce = client.commerceEntity.idCommerce " +
            "inner join TypeCommerceEntity as type on type.idTypeCommerce = com.typeCommerceEntity.idTypeCommerce " +
            "where cast( bankTransactionEntity.registerDate as DATE) BETWEEN (:startDate) AND (:endDate) " +
            "AND bankTransactionEntity.commerceEntity.idCommerce = :idCommerce " +
            "AND bankTransactionEntity.paymentChannel.idPaymentChannel = :idPaymentChannel " +
            "order by bankTransactionEntity.registerDate DESC")
    List<BankTransactionListTo> getBankTransactionsByIdCommerceAndProductTypeExport
            (@Param("startDate") LocalDate startDate,
             @Param("endDate") LocalDate endDate,
             @Param("idCommerce") Long idCommerce,
             @Param("idPaymentChannel") Long idPaymentChannel);

    @Query(value = "select distinct new api.apiAdminCommerce.to.BankTransactionListTo(bankTransactionEntity, license) " +
            "from BankTransactionEntity as bankTransactionEntity " +
            "inner join CommerceEntity as com on com.idCommerce = bankTransactionEntity.commerceEntity.idCommerce " +
            "inner join LicenseEntity as license on com.idCommerce = license.commerceEntity.idCommerce " +
            "inner join PlanEntity as plan on plan.idPlan = license.planEntity.idPlan " +
            "inner join ClientEntity as client on com.idCommerce = client.commerceEntity.idCommerce " +
            "inner join TypeCommerceEntity as type on type.idTypeCommerce = com.typeCommerceEntity.idTypeCommerce " +
            "where cast( bankTransactionEntity.registerDate as DATE) BETWEEN (:startDate) AND (:endDate) " +
            "AND bankTransactionEntity.commerceEntity.idCommerce = :idCommerce " +
            "AND bankTransactionEntity.transactionStatusEntity.idTransactionStatus = :idStatusTransaction " +
            "order by bankTransactionEntity.registerDate DESC")
    Page<BankTransactionEntity> getBankTransactionsByDateIntervalPaginatedAndByIdCommerceAndStatusTransaction
            (Pageable pageable, @Param("startDate") LocalDate startDate,
             @Param("endDate") LocalDate endDate, @Param("idCommerce") Long idCommerce, @Param("idStatusTransaction") Long idStatusTransaction);

    @Query(value = "select distinct new api.apiAdminCommerce.to.BankTransactionListTo(bankTransactionEntity, license) " +
            "from BankTransactionEntity as bankTransactionEntity " +
            "inner join CommerceEntity as com on com.idCommerce = bankTransactionEntity.commerceEntity.idCommerce " +
            "inner join LicenseEntity as license on com.idCommerce = license.commerceEntity.idCommerce " +
            "inner join PlanEntity as plan on plan.idPlan = license.planEntity.idPlan " +
            "inner join ClientEntity as client on com.idCommerce = client.commerceEntity.idCommerce " +
            "inner join TypeCommerceEntity as type on type.idTypeCommerce = com.typeCommerceEntity.idTypeCommerce " +
            "where cast( bankTransactionEntity.registerDate as DATE) BETWEEN (:startDate) AND (:endDate) " +
            "AND bankTransactionEntity.commerceEntity.idCommerce = :idCommerce " +
            "AND bankTransactionEntity.transactionStatusEntity.idTransactionStatus = :idStatusTransaction " +
            "order by bankTransactionEntity.registerDate DESC")
    List<BankTransactionListTo> getBankTransactionsByIdCommerceAndStatusTransactionExport
            (@Param("startDate") LocalDate startDate,
             @Param("endDate") LocalDate endDate,
             @Param("idCommerce") Long idCommerce,
             @Param("idStatusTransaction") Long idStatusTransaction);

    @Query(value = "select distinct new api.apiAdminCommerce.to.BankTransactionListTo(bankTransactionEntity, license) " +
            "from BankTransactionEntity as bankTransactionEntity " +
            "inner join CommerceEntity as com on com.idCommerce = bankTransactionEntity.commerceEntity.idCommerce " +
            "inner join LicenseEntity as license on com.idCommerce = license.commerceEntity.idCommerce " +
            "inner join PlanEntity as plan on plan.idPlan = license.planEntity.idPlan " +
            "inner join ClientEntity as client on com.idCommerce = client.commerceEntity.idCommerce " +
            "inner join TypeCommerceEntity as type on type.idTypeCommerce = com.typeCommerceEntity.idTypeCommerce " +
            "where bankTransactionEntity.commerceEntity.idCommerce = :idCommerce " +
            "AND bankTransactionEntity.transactionStatusEntity.idTransactionStatus = :idStatusTransaction " +
            "order by bankTransactionEntity.registerDate DESC")
    Page<BankTransactionListTo> getBankTransactionsByIdCommerceAndStatusTransactionExportLast
            (Pageable pageable,
             @Param("idStatusTransaction") Long idStatusTransaction, @Param("idCommerce") Long idCommerce);

    @Query(value = "select distinct new api.apiAdminCommerce.to.BankTransactionListTo(bankTransactionEntity, license) " +
            "from BankTransactionEntity as bankTransactionEntity " +
            "inner join CommerceEntity as com on com.idCommerce = bankTransactionEntity.commerceEntity.idCommerce " +
            "inner join LicenseEntity as license on com.idCommerce = license.commerceEntity.idCommerce " +
            "inner join PlanEntity as plan on plan.idPlan = license.planEntity.idPlan " +
            "inner join ClientEntity as client on com.idCommerce = client.commerceEntity.idCommerce " +
            "inner join TypeCommerceEntity as type on type.idTypeCommerce = com.typeCommerceEntity.idTypeCommerce " +
            "where cast( bankTransactionEntity.registerDate as DATE) BETWEEN (:startDate) AND (:endDate) " +
            "AND bankTransactionEntity.transactionStatusEntity.idTransactionStatus = :idStatusTransaction " +
            "order by bankTransactionEntity.registerDate DESC")
    Page<BankTransactionEntity> getBankTransactionsByDateIntervalPaginatedByStatusTransaction
            (Pageable pageable, @Param("startDate") LocalDate startDate,
             @Param("endDate") LocalDate endDate, @Param("idStatusTransaction") Long idStatusTransaction);

    @Query(value = "select distinct new api.apiAdminCommerce.to.BankTransactionListTo(bankTransactionEntity, license) " +
            "from BankTransactionEntity as bankTransactionEntity " +
            "inner join CommerceEntity as com on com.idCommerce = bankTransactionEntity.commerceEntity.idCommerce " +
            "inner join LicenseEntity as license on com.idCommerce = license.commerceEntity.idCommerce " +
            "inner join PlanEntity as plan on plan.idPlan = license.planEntity.idPlan " +
            "inner join ClientEntity as client on com.idCommerce = client.commerceEntity.idCommerce " +
            "inner join TypeCommerceEntity as type on type.idTypeCommerce = com.typeCommerceEntity.idTypeCommerce " +
            "where bankTransactionEntity.transactionStatusEntity.idTransactionStatus = :idStatusTransaction " +
            "order by bankTransactionEntity.registerDate DESC")
    Page<BankTransactionListTo> getBankTransactionsByDateIntervalPaginatedByStatusTransactionLast
            (Pageable pageable, @Param("idStatusTransaction") Long idStatusTransaction);


    @Query(value = "select distinct new api.apiAdminCommerce.to.BankTransactionListTo(bankTransactionEntity, license) " +
            "from BankTransactionEntity as bankTransactionEntity " +
            "inner join CommerceEntity as com on com.idCommerce = bankTransactionEntity.commerceEntity.idCommerce " +
            "inner join LicenseEntity as license on com.idCommerce = license.commerceEntity.idCommerce " +
            "inner join PlanEntity as plan on plan.idPlan = license.planEntity.idPlan " +
            "inner join ClientEntity as client on com.idCommerce = client.commerceEntity.idCommerce " +
            "inner join TypeCommerceEntity as type on type.idTypeCommerce = com.typeCommerceEntity.idTypeCommerce " +
            "where cast( bankTransactionEntity.registerDate as DATE) BETWEEN (:startDate) AND (:endDate) " +
            "AND bankTransactionEntity.transactionStatusEntity.idTransactionStatus = :idStatusTransaction " +
            "order by bankTransactionEntity.registerDate DESC")
    List<BankTransactionListTo> getBankTransactionsByStatusTransactionExport
            (@Param("startDate") LocalDate startDate,
             @Param("endDate") LocalDate endDate,
             @Param("idStatusTransaction") Long idStatusTransaction);

}
