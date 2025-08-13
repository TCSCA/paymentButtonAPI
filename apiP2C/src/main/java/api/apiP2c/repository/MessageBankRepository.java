package api.apiP2c.repository;

import api.apiP2c.entity.MessageApiEntity;
import api.apiP2c.entity.MessageBankEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MessageBankRepository extends JpaRepository<MessageBankEntity, Long> {

    MessageBankEntity findByCodeAndBankEntity_IdBank(String code, Long idBank);

    @Query(value = "select messageApiEntity from MessageApiEntity messageApiEntity " +
            "inner join MessageApiBankEntity messageApiBankEntity on " +
            "messageApiBankEntity.messageApiEntity.idMessageApi = messageApiEntity.idMessageApi " +
            "where messageApiBankEntity.messageBankEntity.code = :bankCode ")
    MessageApiEntity getMessageApiFromMessageBankCode(@Param("bankCode") String bankCode);

    @Query(value = "select messageApiEntity from MessageApiEntity messageApiEntity " +
                   "where messageApiEntity.code = :bankCode ")
    MessageApiEntity getMessageApiFromMessageBankCodeMSG003(@Param("bankCode") String bankCode);

    MessageApiEntity findByCodeAndStatusTrue(String code);
}
