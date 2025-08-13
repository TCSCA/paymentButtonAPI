package api.internalrepository.service;

import api.internalrepository.entity.*;
import api.internalrepository.repository.BankRepository;
import api.internalrepository.repository.BankTransactionRepository;
import api.internalrepository.repository.ConfigurationRepository;
import api.internalrepository.request.ManualPaymentRequest;
import api.internalrepository.request.PaymentLinkRequest;
import api.internalrepository.to.BankTransactionListTo;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

@Service
public class BankTransactionService {

    private static final Logger logger = LoggerFactory.getLogger(BankTransactionService.class);

    private final BankTransactionRepository bankTransactionRepository;

    private final CommerceService commerceService;

    private final BankRepository bankRepository;

    private final ConfigurationRepository configurationRepository;

    private static final Long PENDING_STATUS_ID = 4L;
    private static final Long EXPIRED_STATUS_ID = 5L;

    private static final Long PAYMENT_CHANNEL_ID = 2L;

    public BankTransactionService(BankTransactionRepository bankTransactionRepository, CommerceService commerceService, BankRepository bankRepository, ConfigurationRepository configurationRepository) {
        this.bankTransactionRepository = bankTransactionRepository;
        this.commerceService = commerceService;
        this.bankRepository = bankRepository;
        this.configurationRepository = configurationRepository;
    }

    @Transactional
    public BankTransactionEntity generateBankTransactionByPaymentLink(
            final PaymentLinkRequest paymentLinkRequest) {

        CommerceEntity commerceEntity = commerceService.
                getCommerceByDocumentAndStatus(paymentLinkRequest.getRif(), 2L);

        BankTransactionEntity bankTransactionEntity = new BankTransactionEntity();

        bankTransactionEntity.setRegisterDate(OffsetDateTime.now(ZoneId.of("America/Caracas")));

        //Link de pago = 2
        bankTransactionEntity.setTransactionTypeEntity(new TransactionTypeEntity(2L));

        bankTransactionEntity.setCommerceEntity(commerceEntity);

        //Bolivares = 2 - solo se estan manejando bolivares por los momentos
        bankTransactionEntity.setCurrencyEntity(new CurrencyEntity(2L));

        bankTransactionEntity.setPaymentChannel(new PaymentChannel(paymentLinkRequest.getIdPaymentChannel()));
        bankTransactionEntity.setPaymentMethodEntity(new PaymentMethodEntity(paymentLinkRequest.getIdPaymentMethod()));
        bankTransactionEntity.setAmount(paymentLinkRequest.getAmount());
        bankTransactionEntity.setSenderIdentificationDocument(paymentLinkRequest.getSenderIdentificationDocument());
        bankTransactionEntity.setSenderPhoneNumber(paymentLinkRequest.getSenderPhoneNumber());
        bankTransactionEntity.setTransactionStatusEntity(new TransactionStatusEntity(4L));
        bankTransactionEntity.setReasonOfCollection(paymentLinkRequest.getReasonOfCollection());

        return bankTransactionRepository.save(bankTransactionEntity);
    }

    public BankTransactionEntity getInformationByIdBankTransaction(final Long idBankTransaction) {
        return bankTransactionRepository.findByIdBankTransaction(idBankTransaction);
    }

    public void saveManualPaymentInformation(ManualPaymentRequest manualPaymentRequest) {

        try {
            BankTransactionEntity bankTransactionEntity = new BankTransactionEntity();

            BankEntity bankEntity = bankRepository.findByBankCode(manualPaymentRequest.getBankCode());

            CommerceEntity commerceEntity = commerceService.getCommerceByDocumentAndStatus(manualPaymentRequest.getRif(), 2L);

            bankTransactionEntity.setCommerceEntity(commerceEntity);
            bankTransactionEntity.setPaymentChannel(new PaymentChannel(manualPaymentRequest.getPaymentChannel()));
            bankTransactionEntity.setSenderPhoneNumber(manualPaymentRequest.getDebitPhone());
            bankTransactionEntity.setBankEntity(bankEntity);
            bankTransactionEntity.setAmount(new BigDecimal(manualPaymentRequest.getTransactionAmount()));
            bankTransactionEntity.setSenderIdentificationDocument(manualPaymentRequest.getPayerDocument());
            bankTransactionEntity.setReferenceNumber(Long.valueOf(manualPaymentRequest.getReferenceNumber()));
            bankTransactionEntity.setTransactionCode("No aplica");
            bankTransactionEntity.setRegisterBy(manualPaymentRequest.getPayerDocument());
            bankTransactionEntity.setUpdateBy(manualPaymentRequest.getPayerDocument());
            bankTransactionEntity.setCurrencyEntity(new CurrencyEntity(2L));
            bankTransactionEntity.setTransactionStatusEntity(new TransactionStatusEntity(4L));
            bankTransactionEntity.setPaymentMethodEntity(new PaymentMethodEntity(8L));

            bankTransactionEntity.setRegisterDate(OffsetDateTime.now(ZoneId.of("America/Caracas")));
            bankTransactionEntity.setUpdateDate(OffsetDateTime.now(ZoneId.of("America/Caracas")));

            bankTransactionRepository.save(bankTransactionEntity);
            logger.info("BankTransaction saved successfully");

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

    }

    public Page<BankTransactionEntity> getAllManualPaymentsByIntervalDate(final LinkedHashMap<String, Object> requestData,
                                                                    ObjectMapper objectMapper) {

        String startDate = objectMapper.convertValue(requestData.get("startDate"), String.class);
        String endDate = objectMapper.convertValue(requestData.get("endDate"), String.class);
        Integer page = objectMapper.convertValue(requestData.get("page"), Integer.class);
        Integer quantity = objectMapper.convertValue(requestData.get("quantity"), Integer.class);
        String rif = objectMapper.convertValue(requestData.get("rif"), String.class);

        Page<BankTransactionEntity> bankTransactionEntitiesPaged;
        Pageable pageable = PageRequest.of(page, quantity);

        LocalDate startDateParsed = LocalDate.parse(startDate);
        LocalDate endDateParsed = LocalDate.parse(endDate);

        if(rif != null) {
            CommerceEntity commerceEntity = commerceService.getCommerceByDocumentAndStatus(rif, 2L);
            bankTransactionEntitiesPaged = bankTransactionRepository.getBankTransactionsByDateIntervalManualPaymentByCommerce(pageable,
                    startDateParsed,endDateParsed, commerceEntity.getIdCommerce());
        } else {
            bankTransactionEntitiesPaged = bankTransactionRepository.getBankTransactionsByDateIntervalManualPayment(pageable,
                    startDateParsed,endDateParsed);
        }

        return bankTransactionEntitiesPaged;
    }

    public List<BankTransactionListTo> getAllManualPaymentsByIntervalDateExport(final LinkedHashMap<String, Object> requestData,
                                                                                ObjectMapper objectMapper) {

        String startDate = objectMapper.convertValue(requestData.get("startDate"), String.class);
        String endDate = objectMapper.convertValue(requestData.get("endDate"), String.class);
        Integer page = objectMapper.convertValue(requestData.get("page"), Integer.class);
        Integer quantity = objectMapper.convertValue(requestData.get("quantity"), Integer.class);
        String rif = objectMapper.convertValue(requestData.get("rif"), String.class);

        LocalDate startDateParsed = LocalDate.parse(startDate);
        LocalDate endDateParsed = LocalDate.parse(endDate);


        List<BankTransactionEntity> bankTransactionEntities;
        List<BankTransactionListTo> bankTransactionEntitiesTo = new ArrayList<>();


        if(rif != null) {
            CommerceEntity commerceEntity = commerceService.getCommerceByDocumentAndStatus(rif, 2L);
            bankTransactionEntitiesTo = bankTransactionRepository.
                    getBankTransactionsByManualPaymentByCommerceExport(startDateParsed,endDateParsed, commerceEntity.getIdCommerce());

        } else {
            bankTransactionEntitiesTo = bankTransactionRepository.getBankTransactionsManualPaymentExport(startDateParsed, endDateParsed);
        }

        return bankTransactionEntitiesTo;
    }

    public void changeStatusManualPayment(LinkedHashMap<String, Object> requestMap, ObjectMapper objectMapper) {

        Long idBankTransaction = objectMapper.convertValue(requestMap.get("idBankTransaction"), Long.class);
        Long statusBankTransaction = objectMapper.convertValue(requestMap.get("statusBankTransaction"), Long.class);
        String username = objectMapper.convertValue(requestMap.get("username"), String.class);

        BankTransactionEntity bankTransactionEntity = bankTransactionRepository.findByIdBankTransaction(idBankTransaction);

        bankTransactionEntity.setUpdateBy(username);
        bankTransactionEntity.setTransactionStatusEntity(new TransactionStatusEntity(statusBankTransaction));
        bankTransactionEntity.setUpdateDate(OffsetDateTime.now(ZoneId.of("America/Caracas")));

        bankTransactionRepository.save(bankTransactionEntity);
    }

    @Scheduled(cron = "#{@getCronValueRQ25}")
    public void validateIfExpiredBankTransaction() {

        OffsetDateTime currentDate =OffsetDateTime.now();

        try {

            String configurationValue = configurationRepository
                    .findByPassword("expirationMinutesBankTransaction").getValue();

            int expirationMinutes = Integer.parseInt(configurationValue);

            Set<BankTransactionEntity> bankTransactionEntities =
                    bankTransactionRepository.
                            getAllBankTransactionByStatus(PENDING_STATUS_ID, PAYMENT_CHANNEL_ID);

            for (BankTransactionEntity transaction : bankTransactionEntities) {
                if (Duration.between(transaction.getRegisterDate(), currentDate).toMinutes() > expirationMinutes) {
                    transaction.setTransactionStatusEntity(new TransactionStatusEntity(EXPIRED_STATUS_ID));
                    bankTransactionRepository.save(transaction);
                    logger.info("Transaction {} marked as expired", transaction.getIdBankTransaction());
                }
            }

        } catch (Exception e) {
            logger.error("Error validating bank transactions for expiration: {}", e.getMessage(), e);
        }

    }

}
