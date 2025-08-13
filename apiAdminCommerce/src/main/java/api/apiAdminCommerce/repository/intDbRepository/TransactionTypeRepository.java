package api.apiAdminCommerce.repository.intDbRepository;

import api.apiAdminCommerce.entity.PaymentMethodEntity;
import api.apiAdminCommerce.entity.TransactionTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionTypeRepository extends JpaRepository<TransactionTypeEntity, Long> {

    List<TransactionTypeEntity> findAllByStatusTrue();
}
