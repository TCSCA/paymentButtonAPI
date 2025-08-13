package api.apiAdminCommerce.repository.intDbRepository;

import api.apiAdminCommerce.entity.ConfigurationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConfigurationRepository extends JpaRepository<ConfigurationEntity, Long> {

    ConfigurationEntity findConfigurationEntityByIdConfiguration(long idConfiguration);

    ConfigurationEntity findByPassword(final String key);

}
