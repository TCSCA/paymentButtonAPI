package api.internalrepository.service;

import api.internalrepository.entity.ConfigurationEntity;
import api.internalrepository.repository.BankTransactionRepository;
import api.internalrepository.repository.CommerceRepository;
import api.internalrepository.repository.ConfigurationRepository;
import api.internalrepository.to.BankTransactionListTo;
import api.internalrepository.to.CommerceTo;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.data.domain.Limit;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

@Service
public class FilterService {

    private final  CommerceRepository commerceRepository;

    private final BankTransactionRepository bankTransactionRepository;

    private final ConfigurationRepository configurationRepository;

    public FilterService(CommerceRepository commerceRepository, BankTransactionRepository bankTransactionRepository,
                         ConfigurationRepository configurationRepository) {
        this.commerceRepository = commerceRepository;
        this.bankTransactionRepository = bankTransactionRepository;
        this.configurationRepository = configurationRepository;
    }

    public List<CommerceTo> getAllCommerceFilter(LinkedHashMap requestBody, ObjectMapper objectMapper) {

        Integer typeFilter = objectMapper.convertValue(requestBody.get("typeFilter"), Integer.class);
        String filter = objectMapper.convertValue(requestBody.get("filter"), String.class);
        String startDateStr = objectMapper.convertValue(requestBody.get("startDate"), String.class);
        String endDateStr = objectMapper.convertValue(requestBody.get("endDate"), String.class);

        LocalDate startDateLocal;
        LocalDate endDateLocal;

        if(startDateStr != null && endDateStr!= null) {

             startDateLocal = LocalDate.parse(startDateStr);
             endDateLocal = LocalDate.parse(endDateStr);

        } else {
            startDateLocal = LocalDate.parse("1900-01-01");
            endDateLocal = LocalDate.parse("9999-12-31");
        }

        List<CommerceTo> commerceToList = new ArrayList<>();

        switch (typeFilter) {
            case 0:
                commerceToList = commerceRepository.findCommerceByFilterDateFilter(startDateLocal, endDateLocal );
                break;
            case 1:
                commerceToList = commerceRepository.getAllCommerceToByRifFilter(filter.toLowerCase(), startDateLocal, endDateLocal );
                break;
            case 2:
                commerceToList =  commerceRepository.getAllCommerceToByCommerceNameFilter(filter.toLowerCase(), startDateLocal, endDateLocal );
                break;
            case 3:
                commerceToList = commerceRepository.getAllCommerceToByStatusCommerceFilter(Long.valueOf(filter), startDateLocal, endDateLocal );
                break;
            case 4:
                commerceToList = commerceRepository.getAllCommerceToByPlanCommerceFilter(Long.valueOf(filter), startDateLocal, endDateLocal );
                break;
            case 5:
                commerceToList = commerceRepository.getAllCommerceToByTypeCommerceFilter(Long.valueOf(filter), startDateLocal, endDateLocal );
                break;
            case 6:
                commerceToList = commerceRepository.getAllCommerceToByStateFilter(Long.valueOf(filter), startDateLocal, endDateLocal );
                break;
        }

        return commerceToList;


    }

    public List<BankTransactionListTo> getAllBankTransactionsByFilter(LinkedHashMap linkedHashMap, ObjectMapper objectMapper) {

        Integer typeFilter = objectMapper.convertValue(linkedHashMap.get("typeFilter"), Integer.class);
        String filter = objectMapper.convertValue(linkedHashMap.get("filter"), String.class);
        String startDateStr = objectMapper.convertValue(linkedHashMap.get("startDate"), String.class);
        String endDateStr = objectMapper.convertValue(linkedHashMap.get("endDate"), String.class);
        String rif = objectMapper.convertValue(linkedHashMap.get("rif"), String.class);

        LocalDate startDateLocal;
        LocalDate endDateLocal;

        List<BankTransactionListTo> bankTransactionListTo = new ArrayList<>();

        if(startDateStr != null && endDateStr!= null) {

            startDateLocal = LocalDate.parse(startDateStr);
            endDateLocal = LocalDate.parse(endDateStr);

        } else {
            startDateLocal = LocalDate.parse("1900-01-01");
            endDateLocal = LocalDate.parse("9999-12-31");
        }

        switch (typeFilter) {
            case 0:
                ConfigurationEntity configurationEntity = configurationRepository.findConfigurationEntityByIdConfiguration(11L);
                int limit = Integer.parseInt(configurationEntity.getValue());

                Pageable pageable = PageRequest.of(0, limit);
                Limit limitTable = pageable.toLimit();
                bankTransactionListTo =
                        bankTransactionRepository.getAllBankTransactionsByFilter(startDateLocal, endDateLocal, rif, limitTable);
                break;

            case 1:
                bankTransactionListTo =
                        bankTransactionRepository.getAllBankTransactionsByFilterRif(startDateLocal, endDateLocal, filter.toLowerCase(), rif);
                break;

            case 2:
                bankTransactionListTo =
                        bankTransactionRepository.getAllBankTransactionsByFilterCommerceName(startDateLocal, endDateLocal, filter.toLowerCase(), rif);
                break;

            case 3:
                Float amount = Float.valueOf(filter);
                bankTransactionListTo =
                        bankTransactionRepository.getAllBankTransactionsByFilterAmount(startDateLocal, endDateLocal, amount, rif);

                break;

            case 4:
                bankTransactionListTo =
                        bankTransactionRepository.getAllBankTransactionsByFilterProductType(startDateLocal, endDateLocal, Long.valueOf(filter), rif);
                break;

            case 5:
                bankTransactionListTo =
                        bankTransactionRepository.getAllBankTransactionsByFilterBankCommerce(startDateLocal, endDateLocal, Long.valueOf(filter), rif);
                break;

            case 6:
                bankTransactionListTo =
                        bankTransactionRepository.getAllBankTransactionsByFilterStatusTransaction(startDateLocal, endDateLocal, Long.valueOf(filter), rif);
                break;
        }

        return bankTransactionListTo;
    }


    public List<BankTransactionListTo> getAllBankTransactionsAndStatusByFilter(LinkedHashMap linkedHashMap, ObjectMapper objectMapper) {

        Integer typeFilter = objectMapper.convertValue(linkedHashMap.get("typeFilter"), Integer.class);
        String filter = objectMapper.convertValue(linkedHashMap.get("filter"), String.class);
        String startDateStr = objectMapper.convertValue(linkedHashMap.get("startDate"), String.class);
        String endDateStr = objectMapper.convertValue(linkedHashMap.get("endDate"), String.class);
        String rif = objectMapper.convertValue(linkedHashMap.get("rif"), String.class);
        Long idStatusTransaction =objectMapper.convertValue(linkedHashMap.get("idStatusTransaction"), Long.class);

        LocalDate startDateLocal;
        LocalDate endDateLocal;

        List<BankTransactionListTo> bankTransactionListTo = new ArrayList<>();

        if(startDateStr != null && endDateStr!= null) {

            startDateLocal = LocalDate.parse(startDateStr);
            endDateLocal = LocalDate.parse(endDateStr);

        } else {
            startDateLocal = LocalDate.parse("1900-01-01");
            endDateLocal = LocalDate.parse("9999-12-31");
        }

        switch (typeFilter) {
            case 0:
                ConfigurationEntity configurationEntity = configurationRepository.findConfigurationEntityByIdConfiguration(11L);
                int limit = Integer.parseInt(configurationEntity.getValue());

                Pageable pageable = PageRequest.of(0, limit);
                Limit limitTable = pageable.toLimit();
                bankTransactionListTo =
                        bankTransactionRepository.getBankTransactionsByDateIntervalPaginatedByStatusTransactionLast(idStatusTransaction,rif, startDateLocal, endDateLocal,limitTable);
                break;

            case 1:
                bankTransactionListTo =
                        bankTransactionRepository.getBankTransactionsAndStatusByRif(idStatusTransaction,rif,startDateLocal, endDateLocal, filter.toLowerCase());
                break;

            case 2:
                bankTransactionListTo =
                        bankTransactionRepository.getBankTransactionsAndStatusByFilterCommerceName(idStatusTransaction,rif,startDateLocal, endDateLocal, filter.toLowerCase());
                break;

            case 3:
                Float amount = Float.valueOf(filter);
                bankTransactionListTo =
                        bankTransactionRepository.getBankAndTransactionsByFilterAmount(idStatusTransaction,rif,startDateLocal, endDateLocal, amount);
                break;

            case 4:
                bankTransactionListTo =
                        bankTransactionRepository.getBankTransactionsAndStatusByFilterBankCommerce(idStatusTransaction,rif,startDateLocal, endDateLocal, Long.valueOf(filter));
                break;

        }

        return bankTransactionListTo;
    }


    public List<BankTransactionListTo> getAllBankTransactionsAndProductByFilter(LinkedHashMap linkedHashMap, ObjectMapper objectMapper) {

        Integer typeFilter = objectMapper.convertValue(linkedHashMap.get("typeFilter"), Integer.class);
        String filter = objectMapper.convertValue(linkedHashMap.get("filter"), String.class);
        String startDateStr = objectMapper.convertValue(linkedHashMap.get("startDate"), String.class);
        String endDateStr = objectMapper.convertValue(linkedHashMap.get("endDate"), String.class);
        String rif = objectMapper.convertValue(linkedHashMap.get("rif"), String.class);

        LocalDate startDateLocal;
        LocalDate endDateLocal;

        List<BankTransactionListTo> bankTransactionListTo = new ArrayList<>();

        if(startDateStr != null && endDateStr!= null) {

            startDateLocal = LocalDate.parse(startDateStr);
            endDateLocal = LocalDate.parse(endDateStr);

        } else {
            startDateLocal = LocalDate.parse("1900-01-01");
            endDateLocal = LocalDate.parse("9999-12-31");
        }

        switch (typeFilter) {
            case 0:
                ConfigurationEntity configurationEntity = configurationRepository.findConfigurationEntityByIdConfiguration(11L);
                int limit = Integer.parseInt(configurationEntity.getValue());

                Pageable pageable = PageRequest.of(0, limit);
                Limit limitTable = pageable.toLimit();
                bankTransactionListTo =
                        bankTransactionRepository.getBankTransactionsIntervalPaginatedLast(rif, startDateLocal, endDateLocal,limitTable);
                break;

            case 1:
                bankTransactionListTo =
                        bankTransactionRepository.getBankTransactionsByDateAndRif(rif,startDateLocal, endDateLocal, filter.toLowerCase());
                break;

            case 2:
                bankTransactionListTo =
                        bankTransactionRepository.getBankTransactionsByDateAndCommerceName(rif,startDateLocal, endDateLocal, filter.toLowerCase());
                break;

            case 3:
                Float amount = Float.valueOf(filter);
                bankTransactionListTo =
                        bankTransactionRepository.getBankTransactionsByDateAndAmount(rif,startDateLocal, endDateLocal, amount);
                break;

            case 4:
                bankTransactionListTo =
                        bankTransactionRepository.getBankTransactionsByDateAndIdTypeProduct(rif,startDateLocal, endDateLocal, Long.valueOf(filter));
                break;


            case 5:
                bankTransactionListTo =
                        bankTransactionRepository.getBankTransactionsAndPaymentMethod(rif,startDateLocal, endDateLocal, Long.valueOf(filter));
                break;

            case 6:
                bankTransactionListTo =
                        bankTransactionRepository.getBankTransactionsAndProductByFilterBankCommerce(rif,startDateLocal, endDateLocal, Long.valueOf(filter));
                break;
            case 7:
                bankTransactionListTo =
                        bankTransactionRepository.getBankTransactionsAndStatus(rif,startDateLocal, endDateLocal, Long.valueOf(filter));
                break;
        }

        return bankTransactionListTo;
    }

    public List<BankTransactionListTo> getAllBankTransactionsPaymentMethodByFilter(LinkedHashMap linkedHashMap, ObjectMapper objectMapper) {

        Integer typeFilter = objectMapper.convertValue(linkedHashMap.get("typeFilter"), Integer.class);
        String filter = objectMapper.convertValue(linkedHashMap.get("filter"), String.class);
        String startDateStr = objectMapper.convertValue(linkedHashMap.get("startDate"), String.class);
        String endDateStr = objectMapper.convertValue(linkedHashMap.get("endDate"), String.class);
        String rif = objectMapper.convertValue(linkedHashMap.get("rif"), String.class);

        LocalDate startDateLocal;
        LocalDate endDateLocal;

        List<BankTransactionListTo> bankTransactionListTo = new ArrayList<>();

        if(startDateStr != null && endDateStr!= null) {

            startDateLocal = LocalDate.parse(startDateStr);
            endDateLocal = LocalDate.parse(endDateStr);

        } else {
            startDateLocal = LocalDate.parse("1900-01-01");
            endDateLocal = LocalDate.parse("9999-12-31");
        }

        switch (typeFilter) {
            case 0:
                ConfigurationEntity configurationEntity = configurationRepository.findConfigurationEntityByIdConfiguration(11L);
                int limit = Integer.parseInt(configurationEntity.getValue());

                Pageable pageable = PageRequest.of(0, limit);
                Limit limitTable = pageable.toLimit();
                bankTransactionListTo =
                        bankTransactionRepository.getAllBankTransactionsByFilter(startDateLocal, endDateLocal, rif, limitTable);

                break;

            case 1:
                bankTransactionListTo =
                        bankTransactionRepository.getAllBankTransactionsByFilterRif(startDateLocal, endDateLocal, filter.toLowerCase(), rif);
                break;

            case 2:
                bankTransactionListTo =
                        bankTransactionRepository.getAllBankTransactionsByFilterCommerceName(startDateLocal, endDateLocal, filter.toLowerCase(), rif);
                break;

            case 3:
                Float amount = Float.valueOf(filter);
                bankTransactionListTo =
                        bankTransactionRepository.getAllBankTransactionsByFilterAmount(startDateLocal, endDateLocal, amount, rif);
                break;

            case 4:
                bankTransactionListTo =
                        bankTransactionRepository.getAllBankTransactionsByFilterPaymentMethod(startDateLocal, endDateLocal, Long.valueOf(filter), rif);
                break;

            case 5:
                bankTransactionListTo =
                        bankTransactionRepository.getAllBankTransactionsByFilterBankCommerce(startDateLocal, endDateLocal, Long.valueOf(filter), rif);
                break;

            case 6:
                bankTransactionListTo =
                        bankTransactionRepository.getAllBankTransactionsByFilterStatusTransaction(startDateLocal, endDateLocal, Long.valueOf(filter), rif);
                break;
        }

        return bankTransactionListTo;
    }

    public List<BankTransactionListTo> getAllBankTransactionsManualPaymentByFilter(LinkedHashMap linkedHashMap, ObjectMapper objectMapper) {

        Integer typeFilter = objectMapper.convertValue(linkedHashMap.get("typeFilter"), Integer.class);
        String filter = objectMapper.convertValue(linkedHashMap.get("filter"), String.class);
        String startDateStr = objectMapper.convertValue(linkedHashMap.get("startDate"), String.class);
        String endDateStr = objectMapper.convertValue(linkedHashMap.get("endDate"), String.class);
        String rif = objectMapper.convertValue(linkedHashMap.get("rif"), String.class);

        LocalDate startDateLocal;
        LocalDate endDateLocal;

        List<BankTransactionListTo> bankTransactionListTo = new ArrayList<>();

        if(startDateStr != null && endDateStr!= null) {

            startDateLocal = LocalDate.parse(startDateStr);
            endDateLocal = LocalDate.parse(endDateStr);

        } else {
            startDateLocal = LocalDate.parse("1900-01-01");
            endDateLocal = LocalDate.parse("9999-12-31");
        }

        switch (typeFilter) {
            case 0:
                ConfigurationEntity configurationEntity = configurationRepository.findConfigurationEntityByIdConfiguration(11L);
                int limit = Integer.parseInt(configurationEntity.getValue());

                Pageable pageable = PageRequest.of(0, limit);
                Limit limitTable = pageable.toLimit();

                bankTransactionListTo =
                        bankTransactionRepository.getAllBankTransactionsByFilterManualPayment(startDateLocal, endDateLocal, rif, limitTable);
                break;

            case 1:
                bankTransactionListTo =
                        bankTransactionRepository.getAllBankTransactionsByFilterRifManualPayment
                                (startDateLocal, endDateLocal, filter.toLowerCase(), rif);
                break;

            case 2:
                bankTransactionListTo =
                        bankTransactionRepository.getAllBankTransactionsByFilterCommerceNameManualPayment
                                (startDateLocal, endDateLocal, filter.toLowerCase(), rif);
                break;

            case 3:
                Float amount = Float.valueOf(filter);
                bankTransactionListTo =
                        bankTransactionRepository.getAllBankTransactionsByFilterAmountManualPayment
                                (startDateLocal, endDateLocal, amount, rif);
                break;

            case 4:
                bankTransactionListTo =
                        bankTransactionRepository.getAllBankTransactionsByFilterStatusTransactionManualPayment(startDateLocal, endDateLocal, Long.valueOf(filter), rif);
                break;
        }

        return bankTransactionListTo;
    }
}
