package api.internalrepository.repository;

import api.internalrepository.entity.ConfigurationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConfigurationRepository extends JpaRepository<ConfigurationEntity, Long> {

    ConfigurationEntity findConfigurationEntityByIdConfiguration(final Long idConfiguration);

    ConfigurationEntity findByPassword(final String password);

    List<ConfigurationEntity> findAllByIdConfigurationIn(final List<Long> idConfiguration);

}
