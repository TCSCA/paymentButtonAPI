package api.apiP2c.repository;

import api.apiP2c.entity.CommerceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommerceRepository extends JpaRepository<CommerceEntity, Long> {

    CommerceEntity findByCommerceDocumentAndStatusCommerce_IdStatusPreRegister(String rif, Long idStatusCommerce);

    CommerceEntity findByCommerceDocument(String rif);
}
