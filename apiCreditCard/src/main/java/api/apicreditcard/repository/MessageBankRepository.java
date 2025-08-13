package api.apicreditcard.repository;

import api.apicreditcard.entity.MessageApiEntity;
import api.apicreditcard.entity.MessageBankEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MessageBankRepository extends JpaRepository<MessageBankEntity, Long> {

    MessageBankEntity findByCodeAndBankEntity_IdBank(String code, Long idBank);

    @Query(value = "select messageApiEntity from MessageApiEntity messageApiEntity " +
            "inner join MessageApiBankEntity messageApiBankEntity on " +
            "messageApiBankEntity.messageApiEntity.idMessageApi = messageApiEntity.idMessageApi " +
            "inner join MessageBankEntity message on message.idBankMessage = messageApiBankEntity.messageBankEntity.idBankMessage " +
            "where messageApiBankEntity.messageBankEntity.code = :bankCode " +
            "and message.bankEntity.idBank = :bankId")
    MessageApiEntity getMessageApiFromMessageBankCode(@Param("bankCode") String bankCode,
                                                      @Param("bankId") Long bankId);

    @Query(value = "select messageApiEntity from MessageApiEntity messageApiEntity " +
                   "where messageApiEntity.code = :bankCode ")
    MessageApiEntity getMessageApiFromMessageBankCodeMSG003(@Param("bankCode") String bankCode);

    MessageApiEntity findByCodeAndStatusTrue(String code);
}
