package api.apicommerce.repository;

import api.apicommerce.entity.BankCommerceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BankCommerceRepository extends JpaRepository<BankCommerceEntity, Long> {

    List<BankCommerceEntity> findAllByCommerceEntity_IdCommerceAndStatusTrue(Long idCommerce);
}
