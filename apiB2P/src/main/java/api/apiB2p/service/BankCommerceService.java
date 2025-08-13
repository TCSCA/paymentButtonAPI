package api.apiB2p.service;

import api.apiB2p.entity.BankCommerceEntity;
import api.apiB2p.repository.BankCommerceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.time.ZoneId;

@Service
public class BankCommerceService {

    private final BankCommerceRepository bankCommerceRepository;

    public BankCommerceService(BankCommerceRepository bankCommerceRepository) {
        this.bankCommerceRepository = bankCommerceRepository;
    }


    public BankCommerceEntity getBankCommerceByDocument(String document) {
        BankCommerceEntity bankCommerceEntity = new BankCommerceEntity();
        return bankCommerceEntity;
    }

    @Transactional
    public void saveBankCommerceToken(String token, Long idBankCommerce, Long seconds) {
        Long minutes = seconds / 60;
        OffsetDateTime currentDate = OffsetDateTime.now(ZoneId.of("America/Caracas")).plusMinutes(minutes);
        bankCommerceRepository.updateBankCommerceToken(token,idBankCommerce, currentDate);
    }
}
