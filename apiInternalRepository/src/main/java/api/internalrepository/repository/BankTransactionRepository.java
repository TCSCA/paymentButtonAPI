package api.internalrepository.repository;

import api.internalrepository.entity.BankTransactionEntity;
import api.internalrepository.to.BankTransactionDailyTo;
import api.internalrepository.to.BankTransactionListTo;
import org.springframework.data.domain.Limit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Repository
public interface BankTransactionRepository extends JpaRepository<BankTransactionEntity, Long> {

    BankTransactionEntity findByIdBankTransaction(final Long idBankTransaction);

    @Query(value = "select new api.internalrepository.to.BankTransactionListTo(bankTransactionEntity, license) " +
            "from BankTransactionEntity as bankTransactionEntity " +
            "inner join CommerceEntity as com on com.idCommerce = bankTransactionEntity.commerceEntity.idCommerce " +
            "inner join LicenseEntity as license on com.idCommerce = license.commerceEntity.idCommerce " +
            "inner join PlanEntity as plan on plan.idPlan = license.planEntity.idPlan " +
            "inner join ClientEntity as client on com.idCommerce = client.commerceEntity.idCommerce " +
            "inner join TypeCommerceEntity as type on type.idTypeCommerce = com.typeCommerceEntity.idTypeCommerce " +
            "where cast( bankTransactionEntity.registerDate as DATE) BETWEEN (:startDate) AND (:endDate) " +
            "AND bankTransactionEntity.paymentMethodEntity.idPaymentMethod = 8L " +
            "ORDER BY CASE WHEN bankTransactionEntity.transactionStatusEntity.idTransactionStatus = 4L THEN 1L ELSE 2L END, " +
            "bankTransactionEntity.registerDate DESC")
    Page<BankTransactionEntity> getBankTransactionsByDateIntervalManualPayment
            (Pageable pageable, @Param("startDate") LocalDate startDate,
             @Param("endDate") LocalDate endDate);

    @Query(value = "select new api.internalrepository.to.BankTransactionListTo(bankTransactionEntity, license) " +
            "from BankTransactionEntity as bankTransactionEntity " +
            "inner join CommerceEntity as com on com.idCommerce = bankTransactionEntity.commerceEntity.idCommerce " +
            "inner join LicenseEntity as license on com.idCommerce = license.commerceEntity.idCommerce " +
            "inner join PlanEntity as plan on plan.idPlan = license.planEntity.idPlan " +
            "inner join ClientEntity as client on com.idCommerce = client.commerceEntity.idCommerce " +
            "inner join TypeCommerceEntity as type on type.idTypeCommerce = com.typeCommerceEntity.idTypeCommerce " +
            "where cast( bankTransactionEntity.registerDate as DATE) BETWEEN (:startDate) AND (:endDate) " +
            "AND bankTransactionEntity.transactionStatusEntity.idTransactionStatus = 4L " +
            "AND bankTransactionEntity.paymentMethodEntity.idPaymentMethod = 8L")
    List<BankTransactionListTo> getBankTransactionsManualPaymentExport(@Param("startDate") LocalDate startDate,
                                                                       @Param("endDate") LocalDate endDate);

    @Query(value = "select new api.internalrepository.to.BankTransactionListTo(bankTransactionEntity, license) " +
            "from BankTransactionEntity as bankTransactionEntity " +
            "inner join TransactionTypeEntity as tt on tt.idTransactionType = bankTransactionEntity.transactionTypeEntity.idTransactionType " +
            "inner join CommerceEntity as com on com.idCommerce = bankTransactionEntity.commerceEntity.idCommerce " +
            "inner join LicenseEntity as license on com.idCommerce = license.commerceEntity.idCommerce " +
            "inner join PlanEntity as plan on plan.idPlan = license.planEntity.idPlan " +
            "inner join ClientEntity as client on com.idCommerce = client.commerceEntity.idCommerce " +
            "inner join TypeCommerceEntity as type on type.idTypeCommerce = com.typeCommerceEntity.idTypeCommerce " +
            "where cast( bankTransactionEntity.registerDate as DATE) BETWEEN (:startDate) AND (:endDate) " +
            "AND bankTransactionEntity.paymentMethodEntity.idPaymentMethod = 8L " +
            "AND bankTransactionEntity.commerceEntity.idCommerce = :idCommerce " +
            "ORDER BY CASE WHEN bankTransactionEntity.transactionStatusEntity.idTransactionStatus = 4L THEN 1L ELSE 2L END, " +
            "bankTransactionEntity.registerDate DESC")
    Page<BankTransactionEntity> getBankTransactionsByDateIntervalManualPaymentByCommerce
            (Pageable pageable, @Param("startDate") LocalDate startDate,
             @Param("endDate") LocalDate endDate,
             @Param("idCommerce") Long idCommerce);

    @Query(value = "select new api.internalrepository.to.BankTransactionListTo(bankTransactionEntity, license) " +
            "from BankTransactionEntity as bankTransactionEntity " +
            "inner join TransactionTypeEntity as tt on tt.idTransactionType = bankTransactionEntity.transactionTypeEntity.idTransactionType " +
            "inner join CommerceEntity as com on com.idCommerce = bankTransactionEntity.commerceEntity.idCommerce " +
            "inner join LicenseEntity as license on com.idCommerce = license.commerceEntity.idCommerce " +
            "inner join PlanEntity as plan on plan.idPlan = license.planEntity.idPlan " +
            "inner join ClientEntity as client on com.idCommerce = client.commerceEntity.idCommerce " +
            "inner join TypeCommerceEntity as type on type.idTypeCommerce = com.typeCommerceEntity.idTypeCommerce " +
            "where cast( bankTransactionEntity.registerDate as DATE) BETWEEN (:startDate) AND (:endDate) " +
            "AND bankTransactionEntity.transactionStatusEntity.idTransactionStatus = 4L " +
            "AND bankTransactionEntity.paymentMethodEntity.idPaymentMethod = 8L " +
            "AND bankTransactionEntity.commerceEntity.idCommerce = :idCommerce ")
    List<BankTransactionListTo> getBankTransactionsByManualPaymentByCommerceExport
             (@Param("startDate") LocalDate startDate,
              @Param("endDate") LocalDate endDate,
              @Param("idCommerce") Long idCommerce);

    @Query(value = "select bt from BankTransactionEntity as bt " +
            "inner join TransactionStatusEntity as status on status.idTransactionStatus = bt.transactionStatusEntity.idTransactionStatus " +
            "inner join PaymentChannel as pc on pc.idPaymentChannel = bt.paymentChannel.idPaymentChannel " +
            "where status.idTransactionStatus = :idStatus and pc.idPaymentChannel = :idPaymentChannel " +
            "order by bt.idBankTransaction asc")
    Set<BankTransactionEntity> getAllBankTransactionByStatus(@Param("idStatus") final Long idStatus,
                                             @Param("idPaymentChannel") final Long idPaymentChannel);

    @Query(value = "select distinct new api.internalrepository.to.BankTransactionListTo(bankTransactionEntity, license) " +
            "from BankTransactionEntity as bankTransactionEntity " +
            "inner join CommerceEntity as com on com.idCommerce = bankTransactionEntity.commerceEntity.idCommerce " +
            "inner join LicenseEntity as license on com.idCommerce = license.commerceEntity.idCommerce " +
            "inner join PlanEntity as plan on plan.idPlan = license.planEntity.idPlan " +
            "inner join ClientEntity as client on com.idCommerce = client.commerceEntity.idCommerce " +
            "inner join TypeCommerceEntity as type on type.idTypeCommerce = com.typeCommerceEntity.idTypeCommerce " +
            "where cast( bankTransactionEntity.registerDate as DATE) BETWEEN (:startDate) AND (:endDate) " +
            "and (com.commerceDocument like %:rif% OR :rif IS NULL) " +
            "order by bankTransactionEntity.registerDate DESC ")
    List<BankTransactionListTo> getAllBankTransactionsByFilter(@Param("startDate") LocalDate startDate,
                                                               @Param("endDate") LocalDate endDate,
                                                               @Param("rif") String rif, Limit limit);

    @Query(value = "select new api.internalrepository.to.BankTransactionListTo(bankTransactionEntity, license) " +
            "from BankTransactionEntity as bankTransactionEntity " +
            "inner join CommerceEntity as com on com.idCommerce = bankTransactionEntity.commerceEntity.idCommerce " +
            "inner join LicenseEntity as license on com.idCommerce = license.commerceEntity.idCommerce " +
            "inner join PlanEntity as plan on plan.idPlan = license.planEntity.idPlan " +
            "inner join ClientEntity as client on com.idCommerce = client.commerceEntity.idCommerce " +
            "inner join TypeCommerceEntity as type on type.idTypeCommerce = com.typeCommerceEntity.idTypeCommerce " +
            "where cast( bankTransactionEntity.registerDate as DATE) BETWEEN (:startDate) AND (:endDate) " +
            "and (com.commerceDocument like %:rif% OR :rif IS NULL) " +
            "and bankTransactionEntity.paymentMethodEntity.idPaymentMethod = 8L " +
            "ORDER BY CASE WHEN bankTransactionEntity.transactionStatusEntity.idTransactionStatus = 4L THEN 1L ELSE 2L END, " +
            "bankTransactionEntity.registerDate DESC")
    List<BankTransactionListTo> getAllBankTransactionsByFilterManualPayment(@Param("startDate") LocalDate startDate,
                                                               @Param("endDate") LocalDate endDate,
                                                               @Param("rif") String rif, Limit limit);

    @Query(value = "select distinct new api.internalrepository.to.BankTransactionListTo(bankTransactionEntity, license) " +
            "from BankTransactionEntity as bankTransactionEntity " +
            "inner join CommerceEntity as com on com.idCommerce = bankTransactionEntity.commerceEntity.idCommerce " +
            "inner join LicenseEntity as license on com.idCommerce = license.commerceEntity.idCommerce " +
            "inner join PlanEntity as plan on plan.idPlan = license.planEntity.idPlan " +
            "inner join ClientEntity as client on com.idCommerce = client.commerceEntity.idCommerce " +
            "inner join TypeCommerceEntity as type on type.idTypeCommerce = com.typeCommerceEntity.idTypeCommerce " +
            "where cast(bankTransactionEntity.registerDate as DATE) BETWEEN (:startDate) AND (:endDate) " +
            "and lower(bankTransactionEntity.commerceEntity.commerceDocument) like %:filter% " +
            "and (com.commerceDocument like %:rif% OR :rif IS NULL) " +
            "order by bankTransactionEntity.registerDate DESC ")
    List<BankTransactionListTo> getAllBankTransactionsByFilterRif(@Param("startDate") LocalDate startDate,
                                                                  @Param("endDate") LocalDate endDate,
                                                                  @Param("filter") String filter,
                                                                  @Param("rif") String rif);

    @Query(value = "select distinct new api.internalrepository.to.BankTransactionListTo(bankTransactionEntity, license) " +
            "from BankTransactionEntity as bankTransactionEntity " +
            "inner join CommerceEntity as com on com.idCommerce = bankTransactionEntity.commerceEntity.idCommerce " +
            "inner join LicenseEntity as license on com.idCommerce = license.commerceEntity.idCommerce " +
            "inner join PlanEntity as plan on plan.idPlan = license.planEntity.idPlan " +
            "inner join ClientEntity as client on com.idCommerce = client.commerceEntity.idCommerce " +
            "inner join TypeCommerceEntity as type on type.idTypeCommerce = com.typeCommerceEntity.idTypeCommerce " +
            "where cast(bankTransactionEntity.registerDate as DATE) BETWEEN (:startDate) AND (:endDate) " +
            "and lower(bankTransactionEntity.commerceEntity.commerceName) like %:filter% " +
            "and (com.commerceDocument like %:rif% OR :rif IS NULL) " +
            "order by bankTransactionEntity.registerDate DESC ")
    List<BankTransactionListTo> getAllBankTransactionsByFilterCommerceName(@Param("startDate") LocalDate startDate,
                                                                           @Param("endDate") LocalDate endDate,
                                                                           @Param("filter") String filter,
                                                                           @Param("rif") String rif);

    @Query(value = "select distinct new api.internalrepository.to.BankTransactionListTo(bankTransactionEntity, license) " +
            "from BankTransactionEntity as bankTransactionEntity " +
            "inner join CommerceEntity as com on com.idCommerce = bankTransactionEntity.commerceEntity.idCommerce " +
            "inner join LicenseEntity as license on com.idCommerce = license.commerceEntity.idCommerce " +
            "inner join PlanEntity as plan on plan.idPlan = license.planEntity.idPlan " +
            "inner join ClientEntity as client on com.idCommerce = client.commerceEntity.idCommerce " +
            "inner join TypeCommerceEntity as type on type.idTypeCommerce = com.typeCommerceEntity.idTypeCommerce " +
            "where cast(bankTransactionEntity.registerDate as DATE) BETWEEN (:startDate) AND (:endDate) " +
            "and bankTransactionEntity.bankEntity.idBank = :filter " +
            "and (com.commerceDocument like %:rif% OR :rif IS NULL) " +
            "order by bankTransactionEntity.registerDate DESC ")
    List<BankTransactionListTo> getAllBankTransactionsByFilterBankCommerce(@Param("startDate") LocalDate startDate,
                                                                           @Param("endDate") LocalDate endDate,
                                                                           @Param("filter") Long filter,
                                                                           @Param("rif") String rif);

    @Query(value = "select distinct new api.internalrepository.to.BankTransactionListTo(bankTransactionEntity, license) " +
            "from BankTransactionEntity as bankTransactionEntity " +
            "inner join CommerceEntity as com on com.idCommerce = bankTransactionEntity.commerceEntity.idCommerce " +
            "inner join LicenseEntity as license on com.idCommerce = license.commerceEntity.idCommerce " +
            "inner join PlanEntity as plan on plan.idPlan = license.planEntity.idPlan " +
            "inner join ClientEntity as client on com.idCommerce = client.commerceEntity.idCommerce " +
            "inner join TypeCommerceEntity as type on type.idTypeCommerce = com.typeCommerceEntity.idTypeCommerce " +
            "where cast(bankTransactionEntity.registerDate as DATE) BETWEEN (:startDate) AND (:endDate) " +
            "and bankTransactionEntity.paymentChannel.idPaymentChannel = :filter " +
            "and (com.commerceDocument like %:rif% OR :rif IS NULL) " +
            "order by bankTransactionEntity.registerDate DESC ")
    List<BankTransactionListTo> getAllBankTransactionsByFilterProductType(@Param("startDate") LocalDate startDate,
                                                                          @Param("endDate") LocalDate endDate,
                                                                          @Param("filter") Long filter,
                                                                          @Param("rif") String rif);

    @Query(value = "select distinct new api.internalrepository.to.BankTransactionListTo(bankTransactionEntity, license) " +
            "from BankTransactionEntity as bankTransactionEntity " +
            "inner join CommerceEntity as com on com.idCommerce = bankTransactionEntity.commerceEntity.idCommerce " +
            "inner join LicenseEntity as license on com.idCommerce = license.commerceEntity.idCommerce " +
            "inner join PlanEntity as plan on plan.idPlan = license.planEntity.idPlan " +
            "inner join ClientEntity as client on com.idCommerce = client.commerceEntity.idCommerce " +
            "inner join TypeCommerceEntity as type on type.idTypeCommerce = com.typeCommerceEntity.idTypeCommerce " +
            "where cast(bankTransactionEntity.registerDate as DATE) BETWEEN (:startDate) AND (:endDate) " +
            "and bankTransactionEntity.paymentMethodEntity.idPaymentMethod = :filter " +
            "and (com.commerceDocument like %:rif% OR :rif IS NULL) " +
            "order by bankTransactionEntity.registerDate DESC ")
    List<BankTransactionListTo> getAllBankTransactionsByFilterPaymentMethod(@Param("startDate") LocalDate startDate,
                                                                          @Param("endDate") LocalDate endDate,
                                                                          @Param("filter") Long filter,
                                                                          @Param("rif") String rif);

    @Query(value = "select distinct new api.internalrepository.to.BankTransactionListTo(bankTransactionEntity, license) " +
            "from BankTransactionEntity as bankTransactionEntity " +
            "inner join CommerceEntity as com on com.idCommerce = bankTransactionEntity.commerceEntity.idCommerce " +
            "inner join LicenseEntity as license on com.idCommerce = license.commerceEntity.idCommerce " +
            "inner join PlanEntity as plan on plan.idPlan = license.planEntity.idPlan " +
            "inner join ClientEntity as client on com.idCommerce = client.commerceEntity.idCommerce " +
            "inner join TypeCommerceEntity as type on type.idTypeCommerce = com.typeCommerceEntity.idTypeCommerce " +
            "where cast(bankTransactionEntity.registerDate as DATE) BETWEEN (:startDate) AND (:endDate) " +
            "and bankTransactionEntity.amount BETWEEN :filter - 0.0001 AND :filter + 0.0001 " +
            "and (com.commerceDocument like %:rif% OR :rif IS NULL) " +
            "order by bankTransactionEntity.registerDate DESC ")
    List<BankTransactionListTo> getAllBankTransactionsByFilterAmount(@Param("startDate") LocalDate startDate,
                                                                     @Param("endDate") LocalDate endDate,
                                                                     @Param("filter") Float filter,
                                                                     @Param("rif") String rif);

    @Query(value = "select distinct new api.internalrepository.to.BankTransactionListTo(bankTransactionEntity, license) " +
            "from BankTransactionEntity as bankTransactionEntity " +
            "inner join CommerceEntity as com on com.idCommerce = bankTransactionEntity.commerceEntity.idCommerce " +
            "inner join LicenseEntity as license on com.idCommerce = license.commerceEntity.idCommerce " +
            "inner join PlanEntity as plan on plan.idPlan = license.planEntity.idPlan " +
            "inner join ClientEntity as client on com.idCommerce = client.commerceEntity.idCommerce " +
            "inner join TransactionStatusEntity as status on status.idTransactionStatus = " +
            "bankTransactionEntity.transactionStatusEntity.idTransactionStatus " +
            "where cast(bankTransactionEntity.registerDate as DATE) BETWEEN (:startDate) AND (:endDate) " +
            "and bankTransactionEntity.transactionStatusEntity.idTransactionStatus = :filter " +
            "and (com.commerceDocument like %:rif% OR :rif IS NULL) " +
            "order by bankTransactionEntity.registerDate DESC ")
    List<BankTransactionListTo> getAllBankTransactionsByFilterStatusTransaction(@Param("startDate") LocalDate startDate,
                                                                                @Param("endDate") LocalDate endDate,
                                                                                @Param("filter") Long filter,
                                                                                @Param("rif") String rif);

    @Query(value = "select distinct new api.internalrepository.to.BankTransactionListTo(bankTransactionEntity, license) " +
            "from BankTransactionEntity as bankTransactionEntity " +
            "inner join CommerceEntity as com on com.idCommerce = bankTransactionEntity.commerceEntity.idCommerce " +
            "inner join LicenseEntity as license on com.idCommerce = license.commerceEntity.idCommerce " +
            "inner join PlanEntity as plan on plan.idPlan = license.planEntity.idPlan " +
            "inner join ClientEntity as client on com.idCommerce = client.commerceEntity.idCommerce " +
            "inner join TypeCommerceEntity as type on type.idTypeCommerce = com.typeCommerceEntity.idTypeCommerce " +
            "order by bankTransactionEntity.registerDate DESC ")
    Page<BankTransactionListTo> getBankTransactionsByDateIntervalPaginatedLast
            (Pageable pageable);

    @Query(value = "select new api.internalrepository.to.BankTransactionListTo(bankTransactionEntity, license) " +
            "from BankTransactionEntity as bankTransactionEntity " +
            "inner join CommerceEntity as com on com.idCommerce = bankTransactionEntity.commerceEntity.idCommerce " +
            "inner join LicenseEntity as license on com.idCommerce = license.commerceEntity.idCommerce " +
            "inner join PlanEntity as plan on plan.idPlan = license.planEntity.idPlan " +
            "inner join ClientEntity as client on com.idCommerce = client.commerceEntity.idCommerce " +
            "inner join TypeCommerceEntity as type on type.idTypeCommerce = com.typeCommerceEntity.idTypeCommerce " +
            "where cast(bankTransactionEntity.registerDate as DATE) BETWEEN (:startDate) AND (:endDate) " +
            "and lower(bankTransactionEntity.commerceEntity.commerceDocument) like %:filter% " +
            "and (com.commerceDocument like %:rif% OR :rif IS NULL) " +
            "and bankTransactionEntity.paymentMethodEntity.idPaymentMethod = 8L " +
            "order by bankTransactionEntity.registerDate DESC ")
    List<BankTransactionListTo> getAllBankTransactionsByFilterRifManualPayment(@Param("startDate") LocalDate startDate,
                                                                  @Param("endDate") LocalDate endDate,
                                                                  @Param("filter") String filter,
                                                                  @Param("rif") String rif);

    @Query(value = "select new api.internalrepository.to.BankTransactionListTo(bankTransactionEntity, license) " +
            "from BankTransactionEntity as bankTransactionEntity " +
            "inner join CommerceEntity as com on com.idCommerce = bankTransactionEntity.commerceEntity.idCommerce " +
            "inner join LicenseEntity as license on com.idCommerce = license.commerceEntity.idCommerce " +
            "inner join PlanEntity as plan on plan.idPlan = license.planEntity.idPlan " +
            "inner join ClientEntity as client on com.idCommerce = client.commerceEntity.idCommerce " +
            "inner join TypeCommerceEntity as type on type.idTypeCommerce = com.typeCommerceEntity.idTypeCommerce " +
            "where cast(bankTransactionEntity.registerDate as DATE) BETWEEN (:startDate) AND (:endDate) " +
            "and lower(bankTransactionEntity.commerceEntity.commerceName) like %:filter% " +
            "and (com.commerceDocument like %:rif% OR :rif IS NULL) " +
            "and bankTransactionEntity.paymentMethodEntity.idPaymentMethod = 8L " +
            "order by bankTransactionEntity.registerDate DESC ")
    List<BankTransactionListTo> getAllBankTransactionsByFilterCommerceNameManualPayment(@Param("startDate") LocalDate startDate,
                                                                           @Param("endDate") LocalDate endDate,
                                                                           @Param("filter") String filter,
                                                                           @Param("rif") String rif);

    @Query(value = "select new api.internalrepository.to.BankTransactionListTo(bankTransactionEntity, license) " +
            "from BankTransactionEntity as bankTransactionEntity " +
            "inner join CommerceEntity as com on com.idCommerce = bankTransactionEntity.commerceEntity.idCommerce " +
            "inner join LicenseEntity as license on com.idCommerce = license.commerceEntity.idCommerce " +
            "inner join PlanEntity as plan on plan.idPlan = license.planEntity.idPlan " +
            "inner join ClientEntity as client on com.idCommerce = client.commerceEntity.idCommerce " +
            "inner join TypeCommerceEntity as type on type.idTypeCommerce = com.typeCommerceEntity.idTypeCommerce " +
            "where cast(bankTransactionEntity.registerDate as DATE) BETWEEN (:startDate) AND (:endDate) " +
            "and bankTransactionEntity.amount BETWEEN :filter - 0.0001 AND :filter + 0.0001 " +
            "and (com.commerceDocument like %:rif% OR :rif IS NULL) " +
            "and bankTransactionEntity.paymentMethodEntity.idPaymentMethod = 8L " +
            "order by bankTransactionEntity.registerDate DESC ")
    List<BankTransactionListTo> getAllBankTransactionsByFilterAmountManualPayment(@Param("startDate") LocalDate startDate,
                                                                     @Param("endDate") LocalDate endDate,
                                                                     @Param("filter") Float filter,
                                                                     @Param("rif") String rif);

    @Query(value = "select new api.internalrepository.to.BankTransactionListTo(bankTransactionEntity, license) " +
            "from BankTransactionEntity as bankTransactionEntity " +
            "inner join CommerceEntity as com on com.idCommerce = bankTransactionEntity.commerceEntity.idCommerce " +
            "inner join LicenseEntity as license on com.idCommerce = license.commerceEntity.idCommerce " +
            "inner join PlanEntity as plan on plan.idPlan = license.planEntity.idPlan " +
            "inner join ClientEntity as client on com.idCommerce = client.commerceEntity.idCommerce " +
            "inner join TransactionStatusEntity as status on status.idTransactionStatus = " +
            "bankTransactionEntity.transactionStatusEntity.idTransactionStatus " +
            "where cast(bankTransactionEntity.registerDate as DATE) BETWEEN (:startDate) AND (:endDate) " +
            "and bankTransactionEntity.transactionStatusEntity.idTransactionStatus = :filter " +
            "and bankTransactionEntity.paymentMethodEntity.idPaymentMethod = 8L " +
            "and (com.commerceDocument like %:rif% OR :rif IS NULL) " +
            "order by bankTransactionEntity.registerDate DESC ")
    List<BankTransactionListTo> getAllBankTransactionsByFilterStatusTransactionManualPayment(@Param("startDate") LocalDate startDate,
                                                                                @Param("endDate") LocalDate endDate,
                                                                                @Param("filter") Long filter,
                                                                                @Param("rif") String rif);

    @Query(value = "select new api.internalrepository.to.BankTransactionListTo(bankTransactionEntity, license) " +
            "from BankTransactionEntity as bankTransactionEntity " +
            "inner join CommerceEntity as com on com.idCommerce = bankTransactionEntity.commerceEntity.idCommerce " +
            "inner join LicenseEntity as license on com.idCommerce = license.commerceEntity.idCommerce " +
            "inner join PlanEntity as plan on plan.idPlan = license.planEntity.idPlan " +
            "inner join ClientEntity as client on com.idCommerce = client.commerceEntity.idCommerce " +
            "inner join TransactionStatusEntity as status on status.idTransactionStatus = " +
            "bankTransactionEntity.transactionStatusEntity.idTransactionStatus " +
            "where cast(bankTransactionEntity.registerDate as DATE) BETWEEN (:startDate) AND (:endDate) " +
            "and bankTransactionEntity.bankEntity.idBank = :filter " +
            "and bankTransactionEntity.paymentMethodEntity.idPaymentMethod = 8L " +
            "and (com.commerceDocument like %:rif% OR :rif IS NULL) " +
            "order by bankTransactionEntity.registerDate DESC ")
    List<BankTransactionListTo> getAllBankTransactionsByFilterBankManualPayment(@Param("startDate") LocalDate startDate,
                                                                                             @Param("endDate") LocalDate endDate,
                                                                                             @Param("filter") Long filter,
                                                                                             @Param("rif") String rif);

    @Query(value = "select distinct new api.internalrepository.to.BankTransactionListTo(bankTransactionEntity, license) " +
            "from BankTransactionEntity as bankTransactionEntity " +
            "inner join CommerceEntity as com on com.idCommerce = bankTransactionEntity.commerceEntity.idCommerce " +
            "inner join LicenseEntity as license on com.idCommerce = license.commerceEntity.idCommerce " +
            "inner join PlanEntity as plan on plan.idPlan = license.planEntity.idPlan " +
            "inner join ClientEntity as client on com.idCommerce = client.commerceEntity.idCommerce " +
            "inner join TypeCommerceEntity as type on type.idTypeCommerce = com.typeCommerceEntity.idTypeCommerce " +
            "where bankTransactionEntity.transactionStatusEntity.idTransactionStatus = :idStatusTransaction " +
            "and cast( bankTransactionEntity.registerDate as DATE) BETWEEN (:startDate) AND (:endDate) " +
            "and (com.commerceDocument like %:rif% OR :rif IS NULL) " +
            "order by bankTransactionEntity.registerDate DESC")
    List<BankTransactionListTo> getBankTransactionsByDateIntervalPaginatedByStatusTransactionLast
            ( @Param("idStatusTransaction") Long idStatusTransaction,
             @Param("rif") String rif,@Param("startDate") LocalDate startDate,
             @Param("endDate") LocalDate endDate,Limit limit);


    @Query(value = "select distinct new api.internalrepository.to.BankTransactionListTo(bankTransactionEntity, license) " +
            "from BankTransactionEntity as bankTransactionEntity " +
            "inner join CommerceEntity as com on com.idCommerce = bankTransactionEntity.commerceEntity.idCommerce " +
            "inner join LicenseEntity as license on com.idCommerce = license.commerceEntity.idCommerce " +
            "inner join PlanEntity as plan on plan.idPlan = license.planEntity.idPlan " +
            "inner join ClientEntity as client on com.idCommerce = client.commerceEntity.idCommerce " +
            "inner join TypeCommerceEntity as type on type.idTypeCommerce = com.typeCommerceEntity.idTypeCommerce " +
            "where bankTransactionEntity.transactionStatusEntity.idTransactionStatus = :idStatusTransaction " +
            "and cast( bankTransactionEntity.registerDate as DATE) BETWEEN (:startDate) AND (:endDate) " +
            "and lower(bankTransactionEntity.commerceEntity.commerceDocument) like %:filter% " +
            "and (com.commerceDocument like %:rif% OR :rif IS NULL) " +
            "order by bankTransactionEntity.registerDate DESC")
    List<BankTransactionListTo> getBankTransactionsAndStatusByRif
            ( @Param("idStatusTransaction") Long idStatusTransaction,
              @Param("rif") String rif,@Param("startDate") LocalDate startDate,
              @Param("endDate") LocalDate endDate,@Param("filter") String filter);


    @Query(value = "select distinct new api.internalrepository.to.BankTransactionListTo(bankTransactionEntity, license) " +
            "from BankTransactionEntity as bankTransactionEntity " +
            "inner join CommerceEntity as com on com.idCommerce = bankTransactionEntity.commerceEntity.idCommerce " +
            "inner join LicenseEntity as license on com.idCommerce = license.commerceEntity.idCommerce " +
            "inner join PlanEntity as plan on plan.idPlan = license.planEntity.idPlan " +
            "inner join ClientEntity as client on com.idCommerce = client.commerceEntity.idCommerce " +
            "inner join TypeCommerceEntity as type on type.idTypeCommerce = com.typeCommerceEntity.idTypeCommerce " +
            "where bankTransactionEntity.transactionStatusEntity.idTransactionStatus = :idStatusTransaction " +
            "and cast( bankTransactionEntity.registerDate as DATE) BETWEEN (:startDate) AND (:endDate) " +
            "and lower(bankTransactionEntity.commerceEntity.commerceName) like %:filter% " +
            "and (com.commerceDocument like %:rif% OR :rif IS NULL) " +
            "order by bankTransactionEntity.registerDate DESC")
    List<BankTransactionListTo> getBankTransactionsAndStatusByFilterCommerceName
            ( @Param("idStatusTransaction") Long idStatusTransaction,
              @Param("rif") String rif,@Param("startDate") LocalDate startDate,
              @Param("endDate") LocalDate endDate,@Param("filter") String filter);

    @Query(value = "select distinct new api.internalrepository.to.BankTransactionListTo(bankTransactionEntity, license) " +
            "from BankTransactionEntity as bankTransactionEntity " +
            "inner join CommerceEntity as com on com.idCommerce = bankTransactionEntity.commerceEntity.idCommerce " +
            "inner join LicenseEntity as license on com.idCommerce = license.commerceEntity.idCommerce " +
            "inner join PlanEntity as plan on plan.idPlan = license.planEntity.idPlan " +
            "inner join ClientEntity as client on com.idCommerce = client.commerceEntity.idCommerce " +
            "inner join TypeCommerceEntity as type on type.idTypeCommerce = com.typeCommerceEntity.idTypeCommerce " +
            "where bankTransactionEntity.transactionStatusEntity.idTransactionStatus = :idStatusTransaction " +
            "and cast( bankTransactionEntity.registerDate as DATE) BETWEEN (:startDate) AND (:endDate) " +
            "and bankTransactionEntity.bankEntity.idBank = :filter " +
            "and (com.commerceDocument like %:rif% OR :rif IS NULL) " +
            "order by bankTransactionEntity.registerDate DESC")
    List<BankTransactionListTo> getBankTransactionsAndStatusByFilterBankCommerce
            ( @Param("idStatusTransaction") Long idStatusTransaction,
              @Param("rif") String rif,@Param("startDate") LocalDate startDate,
              @Param("endDate") LocalDate endDate,@Param("filter") Long filter);

    @Query(value = "select distinct new api.internalrepository.to.BankTransactionListTo(bankTransactionEntity, license) " +
            "from BankTransactionEntity as bankTransactionEntity " +
            "inner join CommerceEntity as com on com.idCommerce = bankTransactionEntity.commerceEntity.idCommerce " +
            "inner join LicenseEntity as license on com.idCommerce = license.commerceEntity.idCommerce " +
            "inner join PlanEntity as plan on plan.idPlan = license.planEntity.idPlan " +
            "inner join ClientEntity as client on com.idCommerce = client.commerceEntity.idCommerce " +
            "inner join TypeCommerceEntity as type on type.idTypeCommerce = com.typeCommerceEntity.idTypeCommerce " +
            "where bankTransactionEntity.transactionStatusEntity.idTransactionStatus = :idStatusTransaction " +
            "and cast( bankTransactionEntity.registerDate as DATE) BETWEEN (:startDate) AND (:endDate) " +
            "and bankTransactionEntity.amount BETWEEN :filter - 0.0001 AND :filter + 0.0001 " +
            "and (com.commerceDocument like %:rif% OR :rif IS NULL) " +
            "order by bankTransactionEntity.registerDate DESC")
    List<BankTransactionListTo> getBankAndTransactionsByFilterAmount
            ( @Param("idStatusTransaction") Long idStatusTransaction,
              @Param("rif") String rif,@Param("startDate") LocalDate startDate,
              @Param("endDate") LocalDate endDate,@Param("filter") Float filter);



    @Query(value = "select distinct new api.internalrepository.to.BankTransactionListTo(bankTransactionEntity, license) " +
            "from BankTransactionEntity as bankTransactionEntity " +
            "inner join CommerceEntity as com on com.idCommerce = bankTransactionEntity.commerceEntity.idCommerce " +
            "inner join LicenseEntity as license on com.idCommerce = license.commerceEntity.idCommerce " +
            "inner join PlanEntity as plan on plan.idPlan = license.planEntity.idPlan " +
            "inner join ClientEntity as client on com.idCommerce = client.commerceEntity.idCommerce " +
            "inner join TypeCommerceEntity as type on type.idTypeCommerce = com.typeCommerceEntity.idTypeCommerce " +
            "where (com.commerceDocument like %:rif% OR :rif IS NULL) " +
            "and cast( bankTransactionEntity.registerDate as DATE) BETWEEN (:startDate) AND (:endDate) " +
            "order by bankTransactionEntity.registerDate DESC ")
    List<BankTransactionListTo> getBankTransactionsIntervalPaginatedLast
            (@Param("rif") String rif,@Param("startDate") LocalDate startDate,@Param("endDate") LocalDate endDate,Limit limit);

    @Query(value = "select distinct new api.internalrepository.to.BankTransactionListTo(bankTransactionEntity, license) " +
            "from BankTransactionEntity as bankTransactionEntity " +
            "inner join CommerceEntity as com on com.idCommerce = bankTransactionEntity.commerceEntity.idCommerce " +
            "inner join LicenseEntity as license on com.idCommerce = license.commerceEntity.idCommerce " +
            "inner join PlanEntity as plan on plan.idPlan = license.planEntity.idPlan " +
            "inner join ClientEntity as client on com.idCommerce = client.commerceEntity.idCommerce " +
            "inner join TypeCommerceEntity as type on type.idTypeCommerce = com.typeCommerceEntity.idTypeCommerce " +
            "where (com.commerceDocument like %:rif% OR :rif IS NULL) " +
            "and lower(bankTransactionEntity.commerceEntity.commerceDocument) like %:filter% " +
            "and cast( bankTransactionEntity.registerDate as DATE) BETWEEN (:startDate) AND (:endDate) " +
            "order by bankTransactionEntity.registerDate DESC ")
    List<BankTransactionListTo> getBankTransactionsByDateAndRif
            (@Param("rif") String rif,@Param("startDate") LocalDate startDate,@Param("endDate") LocalDate endDate,@Param("filter") String filter);

    @Query(value = "select distinct new api.internalrepository.to.BankTransactionListTo(bankTransactionEntity, license) " +
            "from BankTransactionEntity as bankTransactionEntity " +
            "inner join CommerceEntity as com on com.idCommerce = bankTransactionEntity.commerceEntity.idCommerce " +
            "inner join LicenseEntity as license on com.idCommerce = license.commerceEntity.idCommerce " +
            "inner join PlanEntity as plan on plan.idPlan = license.planEntity.idPlan " +
            "inner join ClientEntity as client on com.idCommerce = client.commerceEntity.idCommerce " +
            "inner join TypeCommerceEntity as type on type.idTypeCommerce = com.typeCommerceEntity.idTypeCommerce " +
            "where (com.commerceDocument like %:rif% OR :rif IS NULL) " +
            "and lower(bankTransactionEntity.commerceEntity.commerceName) like %:filter% " +
            "and cast( bankTransactionEntity.registerDate as DATE) BETWEEN (:startDate) AND (:endDate) " +
            "order by bankTransactionEntity.registerDate DESC ")
    List<BankTransactionListTo> getBankTransactionsByDateAndCommerceName
            (@Param("rif") String rif,@Param("startDate") LocalDate startDate,@Param("endDate") LocalDate endDate,@Param("filter") String filter);

    @Query(value = "select distinct new api.internalrepository.to.BankTransactionListTo(bankTransactionEntity, license) " +
            "from BankTransactionEntity as bankTransactionEntity " +
            "inner join CommerceEntity as com on com.idCommerce = bankTransactionEntity.commerceEntity.idCommerce " +
            "inner join LicenseEntity as license on com.idCommerce = license.commerceEntity.idCommerce " +
            "inner join PlanEntity as plan on plan.idPlan = license.planEntity.idPlan " +
            "inner join ClientEntity as client on com.idCommerce = client.commerceEntity.idCommerce " +
            "inner join TypeCommerceEntity as type on type.idTypeCommerce = com.typeCommerceEntity.idTypeCommerce " +
            "where (com.commerceDocument like %:rif% OR :rif IS NULL) " +
            "and bankTransactionEntity.amount BETWEEN :filter - 0.0001 AND :filter + 0.0001 " +
            "and cast( bankTransactionEntity.registerDate as DATE) BETWEEN (:startDate) AND (:endDate) " +
            "order by bankTransactionEntity.registerDate DESC ")
    List<BankTransactionListTo> getBankTransactionsByDateAndAmount
            (@Param("rif") String rif,@Param("startDate") LocalDate startDate,@Param("endDate") LocalDate endDate,@Param("filter") Float filter);

    @Query(value = "select distinct new api.internalrepository.to.BankTransactionListTo(bankTransactionEntity, license) " +
            "from BankTransactionEntity as bankTransactionEntity " +
            "inner join CommerceEntity as com on com.idCommerce = bankTransactionEntity.commerceEntity.idCommerce " +
            "inner join LicenseEntity as license on com.idCommerce = license.commerceEntity.idCommerce " +
            "inner join PlanEntity as plan on plan.idPlan = license.planEntity.idPlan " +
            "inner join ClientEntity as client on com.idCommerce = client.commerceEntity.idCommerce " +
            "inner join TypeCommerceEntity as type on type.idTypeCommerce = com.typeCommerceEntity.idTypeCommerce " +
            "where (com.commerceDocument like %:rif% OR :rif IS NULL) " +
            "and bankTransactionEntity.paymentChannel.idPaymentChannel = :filter  " +
            "and cast( bankTransactionEntity.registerDate as DATE) BETWEEN (:startDate) AND (:endDate) " +
            "order by bankTransactionEntity.registerDate DESC ")
    List<BankTransactionListTo> getBankTransactionsByDateAndIdTypeProduct
            (@Param("rif") String rif,@Param("startDate") LocalDate startDate,@Param("endDate") LocalDate endDate,@Param("filter") Long filter);

    @Query(value = "select distinct new api.internalrepository.to.BankTransactionListTo(bankTransactionEntity, license) " +
            "from BankTransactionEntity as bankTransactionEntity " +
            "inner join CommerceEntity as com on com.idCommerce = bankTransactionEntity.commerceEntity.idCommerce " +
            "inner join LicenseEntity as license on com.idCommerce = license.commerceEntity.idCommerce " +
            "inner join PlanEntity as plan on plan.idPlan = license.planEntity.idPlan " +
            "inner join ClientEntity as client on com.idCommerce = client.commerceEntity.idCommerce " +
            "inner join TypeCommerceEntity as type on type.idTypeCommerce = com.typeCommerceEntity.idTypeCommerce " +
            "where (com.commerceDocument like %:rif% OR :rif IS NULL) " +
            "and bankTransactionEntity.paymentChannel.idPaymentChannel = :filter  " +
            "and cast( bankTransactionEntity.updateDate as DATE) BETWEEN (:startDate) AND (:endDate) " +
            "order by bankTransactionEntity.updateDate DESC ")
    List<BankTransactionListTo> getBankTransactionsByDateAndIdTypeProductTypeValidatePayment
            (@Param("rif") String rif,@Param("startDate") LocalDate startDate,@Param("endDate") LocalDate endDate,@Param("filter") Long filter);

    @Query(value = "select distinct new api.internalrepository.to.BankTransactionListTo(bankTransactionEntity, license) " +
            "from BankTransactionEntity as bankTransactionEntity " +
            "inner join CommerceEntity as com on com.idCommerce = bankTransactionEntity.commerceEntity.idCommerce " +
            "inner join LicenseEntity as license on com.idCommerce = license.commerceEntity.idCommerce " +
            "inner join PlanEntity as plan on plan.idPlan = license.planEntity.idPlan " +
            "inner join ClientEntity as client on com.idCommerce = client.commerceEntity.idCommerce " +
            "inner join TypeCommerceEntity as type on type.idTypeCommerce = com.typeCommerceEntity.idTypeCommerce " +
            "where (com.commerceDocument like %:rif% OR :rif IS NULL) " +
            "and bankTransactionEntity.bankEntity.idBank = :filter  " +
            "and cast( bankTransactionEntity.registerDate as DATE) BETWEEN (:startDate) AND (:endDate) " +
            "order by bankTransactionEntity.registerDate DESC ")
    List<BankTransactionListTo> getBankTransactionsAndProductByFilterBankCommerce
            (@Param("rif") String rif,@Param("startDate") LocalDate startDate,@Param("endDate") LocalDate endDate,@Param("filter") Long filter);

    @Query(value = "select distinct new api.internalrepository.to.BankTransactionListTo(bankTransactionEntity, license) " +
            "from BankTransactionEntity as bankTransactionEntity " +
            "inner join CommerceEntity as com on com.idCommerce = bankTransactionEntity.commerceEntity.idCommerce " +
            "inner join LicenseEntity as license on com.idCommerce = license.commerceEntity.idCommerce " +
            "inner join PlanEntity as plan on plan.idPlan = license.planEntity.idPlan " +
            "inner join ClientEntity as client on com.idCommerce = client.commerceEntity.idCommerce " +
            "inner join TypeCommerceEntity as type on type.idTypeCommerce = com.typeCommerceEntity.idTypeCommerce " +
            "where (com.commerceDocument like %:rif% OR :rif IS NULL) " +
            "and bankTransactionEntity.transactionStatusEntity.idTransactionStatus = :filter  " +
            "and cast( bankTransactionEntity.registerDate as DATE) BETWEEN (:startDate) AND (:endDate) " +
            "order by bankTransactionEntity.registerDate DESC ")
    List<BankTransactionListTo> getBankTransactionsAndStatus
            (@Param("rif") String rif,@Param("startDate") LocalDate startDate,@Param("endDate") LocalDate endDate,@Param("filter") Long filter);

    @Query(value = "select distinct new api.internalrepository.to.BankTransactionListTo(bankTransactionEntity, license) " +
            "from BankTransactionEntity as bankTransactionEntity " +
            "inner join CommerceEntity as com on com.idCommerce = bankTransactionEntity.commerceEntity.idCommerce " +
            "inner join LicenseEntity as license on com.idCommerce = license.commerceEntity.idCommerce " +
            "inner join PlanEntity as plan on plan.idPlan = license.planEntity.idPlan " +
            "inner join ClientEntity as client on com.idCommerce = client.commerceEntity.idCommerce " +
            "inner join TypeCommerceEntity as type on type.idTypeCommerce = com.typeCommerceEntity.idTypeCommerce " +
            "where (com.commerceDocument like %:rif% OR :rif IS NULL) " +
            "and bankTransactionEntity.paymentMethodEntity.idPaymentMethod = :filter  " +
            "and cast( bankTransactionEntity.registerDate as DATE) BETWEEN (:startDate) AND (:endDate) " +
            "order by bankTransactionEntity.registerDate DESC ")
    List<BankTransactionListTo> getBankTransactionsAndPaymentMethod
            (@Param("rif") String rif,@Param("startDate") LocalDate startDate,@Param("endDate") LocalDate endDate,@Param("filter") Long filter);

    @Query(value = "select distinct new api.internalrepository.to.BankTransactionDailyTo(channel.idPaymentChannel, channel.bankCode, " +
            "com.idCommerce, com.commerceDocument, com.commerceName, method.idPaymentMethod, method.paymentMethod, " +
            "tranStatus.idTransactionStatus, " +
            "bankTransactionEntity.idBankTransaction, bankTransactionEntity.referenceNumber, bankTransactionEntity.amount, " +
            "bankTransactionEntity.registerDate, plan.idPlan, plan.planName, bankTransactionEntity.registerDate, license.activationDate) " +
            "from BankTransactionEntity as bankTransactionEntity " +
            "inner join CommerceEntity as com on com.idCommerce = bankTransactionEntity.commerceEntity.idCommerce " +
            "inner join LicenseEntity as license on com.idCommerce = license.commerceEntity.idCommerce " +
            "inner join PlanEntity as plan on plan.idPlan = license.planEntity.idPlan " +
            "inner join ClientEntity as client on com.idCommerce = client.commerceEntity.idCommerce " +
            "inner join PaymentMethodEntity as method on method.idPaymentMethod = bankTransactionEntity.paymentMethodEntity.idPaymentMethod " +
            "inner join TransactionStatusEntity as tranStatus on tranStatus.idTransactionStatus = bankTransactionEntity.transactionStatusEntity.idTransactionStatus " +
            "inner join PaymentChannel as channel on channel.idPaymentChannel = bankTransactionEntity.paymentChannel.idPaymentChannel " +
            "join TypeCommerceEntity as type on type.idTypeCommerce = com.typeCommerceEntity.idTypeCommerce " +
            "where cast( bankTransactionEntity.registerDate as DATE) = :currentDate " +
            "and bankTransactionEntity.transactionStatusEntity.idTransactionStatus = 1" +
            "order by bankTransactionEntity.registerDate DESC ")
    List<BankTransactionDailyTo> getDailyBankTransactions(@Param("currentDate") LocalDate currentDate);

    @Query(value = "select distinct new api.internalrepository.to.BankTransactionDailyTo(channel.idPaymentChannel, channel.bankCode, " +
            "com.idCommerce, com.commerceDocument, com.commerceName, method.idPaymentMethod, method.paymentMethod, " +
            "tranStatus.idTransactionStatus, " +
            "bankTransactionEntity.idBankTransaction, bankTransactionEntity.referenceNumber, bankTransactionEntity.amount, " +
            "bankTransactionEntity.registerDate, plan.idPlan, plan.planName, bankTransactionEntity.registerDate, license.activationDate) " +
            "from BankTransactionEntity as bankTransactionEntity " +
            "inner join CommerceEntity as com on com.idCommerce = bankTransactionEntity.commerceEntity.idCommerce " +
            "inner join LicenseEntity as license on com.idCommerce = license.commerceEntity.idCommerce " +
            "inner join PlanEntity as plan on plan.idPlan = license.planEntity.idPlan " +
            "inner join ClientEntity as client on com.idCommerce = client.commerceEntity.idCommerce " +
            "inner join PaymentMethodEntity as method on method.idPaymentMethod = bankTransactionEntity.paymentMethodEntity.idPaymentMethod " +
            "inner join TransactionStatusEntity as tranStatus on tranStatus.idTransactionStatus = bankTransactionEntity.transactionStatusEntity.idTransactionStatus " +
            "inner join PaymentChannel as channel on channel.idPaymentChannel = bankTransactionEntity.paymentChannel.idPaymentChannel " +
            "join TypeCommerceEntity as type on type.idTypeCommerce = com.typeCommerceEntity.idTypeCommerce " +
            "where cast( bankTransactionEntity.registerDate as DATE) = :currentDate " +
            "and bankTransactionEntity.transactionStatusEntity.idTransactionStatus = 1 " +
            "and (com.commerceDocument = :rif OR :rif IS NULL) " +
            "order by bankTransactionEntity.registerDate DESC ")
    List<BankTransactionDailyTo> getDailyBankTransactionsByCommerce(@Param("currentDate") LocalDate currentDate, @Param("rif") String rif);
}
