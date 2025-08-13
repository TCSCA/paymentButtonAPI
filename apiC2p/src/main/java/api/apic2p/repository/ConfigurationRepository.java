package api.apic2p.repository;


import api.apic2p.entity.ConfigurationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConfigurationRepository extends JpaRepository<ConfigurationEntity, Long> {

    ConfigurationEntity findConfigurationEntityByIdConfiguration(long idConfiguration);

    ConfigurationEntity findByPassword(final String key);

}
