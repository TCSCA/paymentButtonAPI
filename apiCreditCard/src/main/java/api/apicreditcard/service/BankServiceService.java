package api.apicreditcard.service;

import api.apicreditcard.entity.BankServiceEntity;
import api.apicreditcard.repository.BankServiceRepository;
import org.springframework.stereotype.Service;

@Service
public class BankServiceService {

    private final BankServiceRepository bankServiceRepository;

    public BankServiceService(BankServiceRepository bankServiceRepository) {
        this.bankServiceRepository = bankServiceRepository;
    }

    public BankServiceEntity getUrlByIdPaymentMethodAndIdBank(String bankService, Long idBank) {
        BankServiceEntity bankServiceEntity = bankServiceRepository.findByBankServiceAndBankEntity_IdBankAndStatusTrue(bankService, idBank);
        return bankServiceEntity;
    }
}
