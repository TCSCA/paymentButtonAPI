package api.internalrepository.service;

import api.internalrepository.entity.ClientEntity;
import api.internalrepository.entity.HistoryStatusUserEntity;
import api.internalrepository.entity.StatusUserEntity;
import api.internalrepository.repository.ClientRepository;
import api.internalrepository.repository.CommerceRepository;
import api.internalrepository.repository.HistoryStatusUserRepository;
import api.internalrepository.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.time.ZoneId;

@Service
public class ClientService {

    private final ClientRepository clientRepository;

    private final CommerceRepository commerceRepository;

    private final UserRepository userRepository;

    private final HistoryStatusUserRepository historyStatusUserRepository;

    public ClientService(ClientRepository clientRepository, CommerceRepository commerceRepository, UserRepository userRepository, HistoryStatusUserRepository historyStatusUserRepository) {
        this.clientRepository = clientRepository;
        this.commerceRepository = commerceRepository;
        this.userRepository = userRepository;
        this.historyStatusUserRepository = historyStatusUserRepository;
    }


    public Long createClient(final String clientName, final String identificationDocument,
                             final String phoneNumber, final String email, final Long registerBy,
                             final Long idCommerce, final Long idUser, final OffsetDateTime registerDate) {

        ClientEntity clientEntity = new ClientEntity();

        clientEntity.setClientName(clientName);
        clientEntity.setIdentificationDocument(identificationDocument);
        clientEntity.setPhoneNumber(phoneNumber);
        clientEntity.setEmail(email);
        clientEntity.setRegisterBy(registerBy);
        clientEntity.setCommerceEntity(commerceRepository.findByIdCommerce(idCommerce));
        clientEntity.setUserEntity(userRepository.findByIdUser(idUser));
        clientEntity.setRegisterDate(registerDate);
        HistoryStatusUserEntity historyStatusUserEntity = new HistoryStatusUserEntity();
        historyStatusUserEntity.setReasonStatus(null);
        historyStatusUserEntity.setUserEntity(clientEntity.getUserEntity());
        historyStatusUserEntity.setRegisterDate(OffsetDateTime.now(ZoneId.of("America/Caracas")));
        historyStatusUserEntity.setStatusUserEntity(new StatusUserEntity(1L));
        historyStatusUserRepository.save(historyStatusUserEntity);

        clientRepository.save(clientEntity);

        if (clientEntity.getIdClient() != null) {
            return clientEntity.getIdClient();
        } else {
            return null;
        }

    }

}
