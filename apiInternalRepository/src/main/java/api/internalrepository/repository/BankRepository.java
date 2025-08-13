package api.internalrepository.repository;

import api.internalrepository.entity.BankEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BankRepository extends JpaRepository<BankEntity, Long> {

    List<BankEntity> findAllByStatusTrue();

    BankEntity findByBankCode(String bankCode);
}
