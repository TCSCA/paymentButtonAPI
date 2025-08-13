package api.internalrepository.service;

import api.internalrepository.entity.BankCommerceEntity;
import api.internalrepository.repository.BankCommerceRepository;
import org.springframework.stereotype.Service;

@Service
public class BankCommerceService {

    private final BankCommerceRepository bankCommerceRepository;

    public BankCommerceService(BankCommerceRepository bankCommerceRepository) {
        this.bankCommerceRepository = bankCommerceRepository;
    }

    public BankCommerceEntity getBankCommerceInformationByCommerce(Long idCommerce) {
        return bankCommerceRepository.getBankCommerceInformationByCommerceId(idCommerce);
    }
}
