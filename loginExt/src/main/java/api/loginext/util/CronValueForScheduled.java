package api.loginext.util;


import api.loginext.repository.ConfigurationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

@Service
public class CronValueForScheduled {

    @Autowired
    private ConfigurationRepository configurationRepository;

//    @Bean
//    public String getCronValueRQ16(){
//        return configurationRepository.findByPassword("cronScheduled").getValue();
//    }

//    @Bean
//    public String getCronValueRQ25(){
//        return configurationRepository.findByKey("expirationOrderTokenScheduler").getValue();
//    }

//    @Bean
//    public String getCronValueRQ30(){
//        return configurationRepository.findByKey("expirationAppointmentTokenScheduler").getValue();
//    }

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
