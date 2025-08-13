package api.apiAdminCommerce.to;

import api.apiAdminCommerce.entity.BankTransactionEntity;
import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
public class ResponseBankTransactionsTo {

   private List<BankTransactionEntity> bankTransactionEntities;

   private Long transactionsAmount;

}
