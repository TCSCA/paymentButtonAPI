package api.apiP2c.service;

import api.apiP2c.entity.MessageApiEntity;
import api.apiP2c.repository.MessageBankRepository;

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
