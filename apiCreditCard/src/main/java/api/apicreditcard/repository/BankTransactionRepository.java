package api.apicreditcard.repository;

import api.apicreditcard.entity.BankTransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BankTransactionRepository extends JpaRepository<BankTransactionEntity, Long> {
}
