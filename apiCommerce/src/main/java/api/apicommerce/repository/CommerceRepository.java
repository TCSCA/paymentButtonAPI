package api.apicommerce.repository;

import api.apicommerce.entity.CommerceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommerceRepository extends JpaRepository<CommerceEntity, Long> {

    List<CommerceEntity> findAllByStatusCommerce_IdStatusPreRegister(Long idStatus);

    CommerceEntity findByCommerceDocument(String rif);
}
