package api.apic2p.repository;

import api.apic2p.entity.BankTransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BankTransactionRepository extends JpaRepository<BankTransactionEntity, Long> {

    @Query(value = "select bt from BankTransactionEntity as bt " +
            "where bt.idBankTransaction =:idBankTransaction and bt.transactionCode is null")
    BankTransactionEntity findByIdBankTransaction(@Param("idBankTransaction") final Long idBankTransaction);

}
