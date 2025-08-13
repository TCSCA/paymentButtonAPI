package api.apicommerce.repository;

import api.apicommerce.entity.BankEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BankRepository extends JpaRepository<BankEntity, Long> {

    BankEntity findByBankCode(String bankCode);
}
