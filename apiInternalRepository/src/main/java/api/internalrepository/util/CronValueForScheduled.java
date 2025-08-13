package api.internalrepository.util;

import api.internalrepository.repository.ConfigurationRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

@Service
public class CronValueForScheduled {

    private final ConfigurationRepository configurationRepository;

    public CronValueForScheduled(ConfigurationRepository configurationRepository) {
        this.configurationRepository = configurationRepository;
    }

    @Bean
    public String getCronValueRQ25(){
        return configurationRepository.findByPassword("expirationBankTransactionScheduled").getValue();
    }

    @Bean
    public String getCronValueBillingService(){
        return configurationRepository.findByPassword("billingServiceExecuteScheduled").getValue();
    }

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
