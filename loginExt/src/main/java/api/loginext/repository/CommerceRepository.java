package api.loginext.repository;

import api.loginext.entity.PreRegistroEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface CommerceRepository extends JpaRepository<PreRegistroEntity, Long> {


    PreRegistroEntity findByCommerceDocument(String commerceDocument);
}
