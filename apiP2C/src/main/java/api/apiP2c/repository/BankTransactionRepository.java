package api.apiP2c.repository;

import api.apiP2c.entity.BankTransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BankTransactionRepository extends JpaRepository<BankTransactionEntity, Long> {
   @Query(value = "select bankTransaction from BankTransactionEntity bankTransaction " +
                  "where bankTransaction.referenceNumber = :referenceNumber and " +
                  "bankTransaction.senderIdentificationDocument = :payerDocument and " +
                  "bankTransaction.senderPhoneNumber = :debitPhone and " +
                  "bankTransaction.transactionStatusEntity.idTransactionStatus = 1 and " +
                  "bankTransaction.paymentChannel.idPaymentChannel <> 6 and " +
                  "bankTransaction.paymentMethodEntity.idPaymentMethod = :paymentMethod")
   BankTransactionEntity validateIfPaymentAlreadyExist(@Param("referenceNumber") Long referenceNumber,
                                                       @Param("payerDocument") String payerDocument,
                                                       @Param("debitPhone") String debitPhone,
                                                       @Param("paymentMethod") Long paymentMethod);

   @Query(value = "select bankTransaction from BankTransactionEntity bankTransaction " +
           "where cast(bankTransaction.referenceNumber as STRING) = :referenceNumber " +
           "and bankTransaction.senderPhoneNumber = :debitPhone " +
           "and bankTransaction.transactionStatusEntity.idTransactionStatus = 1 " +
           "and bankTransaction.paymentMethodEntity.idPaymentMethod = :paymentMethod " +
           "and bankTransaction.amount BETWEEN :amount - 0.0001 AND :amount + 0.0001 " +
           "and bankTransaction.bankEntity.bankCode = :bankCode " +
           "and bankTransaction.paymentChannel.idPaymentChannel = 6 ")
   List<BankTransactionEntity> validateIfExternalPaymentAlreadyExistP2C(@Param("referenceNumber") String referenceNumber,
                                                                  @Param("debitPhone") String debitPhone,
                                                                  @Param("bankCode") String bankCode,
                                                                  @Param("amount") Float amount,
                                                                  @Param("paymentMethod") Long paymentMethod);

   @Query(value = "select bankTransaction from BankTransactionEntity bankTransaction " +
           "where cast(bankTransaction.referenceNumber as string) = :referenceNumber and " +
           "bankTransaction.transactionStatusEntity.idTransactionStatus = 1 and " +
           "bankTransaction.amount BETWEEN :amount - 0.0001 AND :amount + 0.0001 and " +
           "bankTransaction.paymentMethodEntity.idPaymentMethod = :paymentMethod and " +
           "bankTransaction.bankEntity.bankCode = :bankCode and " +
           "bankTransaction.paymentChannel.idPaymentChannel = 6")
   List<BankTransactionEntity>validateIfExternalPaymentAlreadyExistTransfer(@Param("referenceNumber") String referenceNumber,
                                                                            @Param("amount") Float amount,
                                                                            @Param("bankCode") String bankCode,
                                                                            @Param("paymentMethod") Long paymentMethod);

   @Query(value = "select bt from BankTransactionEntity as bt " +
           "where bt.idBankTransaction =:idBankTransaction and bt.transactionCode is null")
   BankTransactionEntity findByIdBankTransaction(@Param("idBankTransaction") final Long idBankTransaction);
}
