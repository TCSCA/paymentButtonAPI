package api.apiB2p.service;

import api.apiB2p.entity.MessageApiEntity;
import api.apiB2p.entity.MessageBankEntity;
import api.apiB2p.repository.MessageBankRepository;
import api.apiB2p.util.Response;
import org.springframework.stereotype.Service;

@Service
public class MessageService {

   private final MessageBankRepository messageBankRepository;

    public MessageService(MessageBankRepository messageBankRepository) {
        this.messageBankRepository = messageBankRepository;
    }

    public String returnResponseError(String code, Long idBank) {
        MessageApiEntity messageApiEntity = messageBankRepository.getMessageApiFromMessageBankCode(code);
        if( messageApiEntity == null) {
            messageApiEntity = messageBankRepository.getMessageApiFromMessageBankCodeMSG003("MSG-003");
        }
        return messageApiEntity.getMessage();
    }

}
