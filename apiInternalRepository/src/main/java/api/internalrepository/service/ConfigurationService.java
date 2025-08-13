package api.internalrepository.service;

import api.internalrepository.entity.ConfigurationEntity;
import api.internalrepository.repository.ConfigurationRepository;
import api.internalrepository.util.Response;
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

    public Response editEmails(final String emailSupport, final String emailReceipts){

        try {

            ConfigurationEntity configurationEntity = configurationRepository.findConfigurationEntityByIdConfiguration(7L);
            ConfigurationEntity configurationEntityEmail= configurationRepository.findConfigurationEntityByIdConfiguration(12L);

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
}
