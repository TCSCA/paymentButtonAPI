package api.apiInstantTransfer.repository;

import api.apiInstantTransfer.entity.BankServiceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BankServiceRepository extends JpaRepository<BankServiceEntity, Long> {

    BankServiceEntity findByBankServiceAndBankEntity_IdBankAndStatusTrue(String bankService, Long idbank);
}
