package api.apic2p.service;

import api.apic2p.entity.MessageApiEntity;
import api.apic2p.repository.MessageBankRepository;
import org.springframework.stereotype.Service;

@Service
public class MessageService {

   private final MessageBankRepository messageBankRepository;

    public MessageService(MessageBankRepository messageBankRepository) {
        this.messageBankRepository = messageBankRepository;
    }

    public String returnResponseError(Long code, Long idBank) {

        MessageApiEntity messageApiEntity = messageBankRepository.getMessageApiFromMessageBankCode(code.toString());
        if( messageApiEntity == null) {
            messageApiEntity = messageBankRepository.getMessageApiFromMessageBankCodeMSG003("MSG-003");
        }
        return messageApiEntity.getMessage();
    }

}
