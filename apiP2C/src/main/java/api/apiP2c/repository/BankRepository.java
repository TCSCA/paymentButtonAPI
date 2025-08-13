package api.apiP2c.repository;

import api.apiP2c.entity.BankEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BankRepository extends JpaRepository<BankEntity, Long> {
    BankEntity findByBankCodeAndStatusTrue(String bankCode);

    BankEntity findByIdBank(Long idBank);

}
