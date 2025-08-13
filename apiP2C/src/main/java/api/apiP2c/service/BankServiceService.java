package api.apiP2c.service;

import api.apiP2c.entity.BankServiceEntity;
import api.apiP2c.repository.BankServiceRepository;
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
