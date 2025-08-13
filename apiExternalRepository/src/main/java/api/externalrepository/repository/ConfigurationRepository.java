package api.externalrepository.repository;

import api.externalrepository.entity.ConfigurationEntity;
import api.externalrepository.to.EmailCredentials;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConfigurationRepository extends JpaRepository<ConfigurationEntity, Long> {

    ConfigurationEntity findConfigurationEntityByIdConfiguration(final Long idConfiguration);

    @Query(value = "select config " +
            "from ConfigurationEntity as config " +
            "where config.status is true")
    List<ConfigurationEntity> getConfigurations();

    @Query(value = "select config.value from ConfigurationEntity as config " +
            "where config.password like :key")
    String getConfigurationByKey(@Param("key") final String key);

}
