package api.apiAdminCommerce.repository.intDbRepository;

import api.apiAdminCommerce.entity.EconomicActivityEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EconomicActivityRepository extends JpaRepository<EconomicActivityEntity, Long> {


    List<EconomicActivityEntity> findAllByStatusTrue();
}
