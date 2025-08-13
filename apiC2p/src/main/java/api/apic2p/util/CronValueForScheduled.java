package api.apic2p.util;



import api.apic2p.repository.ConfigurationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

@Service
public class CronValueForScheduled {

    @Autowired
    private ConfigurationRepository configurationRepository;

    @Bean
    public String getClientId(){
        return configurationRepository.findByPassword("googleClientId").getValue();
    }

    @Bean
    public String getClientSecret(){
        return configurationRepository.findByPassword("googleClientSecret").getValue();
    }

    @Bean
    public String getRefreshToken(){
        return configurationRepository.findByPassword("googleRefreshToken").getValue();
    }

    @Bean
    public String getFromEmail(){
        return configurationRepository.findByPassword("emailServicesGoogle").getValue();
    }
}
