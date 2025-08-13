package api.apiB2p.service;

import api.apiB2p.entity.BankServiceEntity;
import api.apiB2p.repository.BankServiceRepository;
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
