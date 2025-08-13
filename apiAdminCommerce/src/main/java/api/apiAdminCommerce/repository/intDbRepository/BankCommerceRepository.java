package api.apiAdminCommerce.repository.intDbRepository;

import api.apiAdminCommerce.entity.BankCommerceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BankCommerceRepository extends JpaRepository<BankCommerceEntity, Long> {
}
