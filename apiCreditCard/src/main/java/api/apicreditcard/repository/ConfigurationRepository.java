package api.apicreditcard.repository;

import api.apicreditcard.entity.ConfigurationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConfigurationRepository extends JpaRepository<ConfigurationEntity, Long> {

    ConfigurationEntity findConfigurationEntityByIdConfiguration(long idConfiguration);

    ConfigurationEntity findByPassword(final String key);

}
