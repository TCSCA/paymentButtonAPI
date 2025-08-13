package api.apicreditcard.repository;


import api.apicreditcard.entity.BankEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BankRepository extends JpaRepository<BankEntity, Long> {

    BankEntity findByBankCodeAndStatusTrue(String bankCode);
}
