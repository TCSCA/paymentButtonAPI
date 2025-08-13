package api.apiB2p.repository;

import api.apiB2p.entity.CommerceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommerceRepository extends JpaRepository<CommerceEntity, Long> {

    CommerceEntity findByCommerceDocumentAndStatusCommerce_IdStatusPreRegister(String rif, Long idStatusCommerce);

    CommerceEntity findByCommerceDocument(String rif);
}
