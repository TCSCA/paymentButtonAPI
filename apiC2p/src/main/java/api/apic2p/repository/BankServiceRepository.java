package api.apic2p.repository;

import api.apic2p.entity.BankServiceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BankServiceRepository extends JpaRepository<BankServiceEntity, Long> {

    BankServiceEntity findByBankServiceAndBankEntity_IdBankAndStatusTrue(String bankService, Long idbank);
}
