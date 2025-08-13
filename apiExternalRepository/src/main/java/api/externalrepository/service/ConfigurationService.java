package api.externalrepository.service;

import api.externalrepository.entity.ConfigurationEntity;
import api.externalrepository.repository.ConfigurationRepository;
import api.externalrepository.to.EmailCredentials;
import api.externalrepository.util.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ConfigurationService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    private final ConfigurationRepository configurationRepository;

    public ConfigurationService(ConfigurationRepository configurationRepository) {
        this.configurationRepository = configurationRepository;
    }

    public Response editEmailsExt(final String emailSupport, final String emailReceipts){

        try {

            ConfigurationEntity configurationEntity = configurationRepository.findConfigurationEntityByIdConfiguration(8L);
            ConfigurationEntity configurationEntityEmail= configurationRepository.findConfigurationEntityByIdConfiguration(11L);

            if(!emailSupport.equals("null")){
                configurationEntity.setValue(emailSupport);
            }

            if (!emailReceipts.equals("null")){
                configurationEntityEmail.setValue(emailReceipts);
            }


            configurationRepository.save(configurationEntity);
            configurationRepository.save(configurationEntityEmail);
            return new Response("SUCCESS","Se actualizo la información correctamente");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new Response("ERROR","Error actualizando la información");
        }
    }

    public EmailCredentials getEmailCredentials() {

        EmailCredentials emailCredentials = new EmailCredentials();

        try {

            emailCredentials.setClientId(configurationRepository.getConfigurationByKey("googleClientId"));
            emailCredentials.setClientSecret(configurationRepository.getConfigurationByKey("googleClientSecret"));
            emailCredentials.setRefreshToken(configurationRepository.getConfigurationByKey("googleRefreshToken"));

            return emailCredentials;

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return null;
        }

    }

}
