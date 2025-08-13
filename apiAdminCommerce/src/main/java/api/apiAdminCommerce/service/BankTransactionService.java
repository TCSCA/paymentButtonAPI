package api.apiAdminCommerce.service;

import api.apiAdminCommerce.entity.*;
import api.apiAdminCommerce.repository.intDbRepository.*;
import api.apiAdminCommerce.repository.intDbRepository.intDbConnection.PaymentChannelRepository;
import api.apiAdminCommerce.to.BankTransactionListTo;
import api.apiAdminCommerce.to.CommerceTo;
import api.apiAdminCommerce.util.Response;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.text.ParseException;
import java.time.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;

@Service
public class BankTransactionService {

    private final BankTransactionRepository bankTransactionRepository;

    private final CommerceRepository commerceRepository;

    private final PaymentMethodRepository paymentMethodRepository;

    private final TransactionTypeRepository transactionTypeRepository;

    private final ConfigurationRepository configurationRepository;

    private final PaymentChannelRepository paymentChannelRepository;

    private final Environment environment;

    private static final Logger logger = LoggerFactory.getLogger(BankTransactionService.class);

    public BankTransactionService(BankTransactionRepository bankTransactionRepository, CommerceRepository commerceRepository,
                                  PaymentMethodRepository paymentMethodRepository, TransactionTypeRepository transactionTypeRepository, ConfigurationRepository configurationRepository, PaymentChannelRepository paymentChannelRepository, Environment environment) {
        this.bankTransactionRepository = bankTransactionRepository;
        this.commerceRepository = commerceRepository;
        this.paymentMethodRepository = paymentMethodRepository;
        this.transactionTypeRepository = transactionTypeRepository;
        this.configurationRepository = configurationRepository;
        this.paymentChannelRepository = paymentChannelRepository;
        this.environment = environment;
    }

    public Response getBankTransactionsByDateIntervalPaginated(int page, int quantity,  String startDate, String endDate, Boolean export) throws ParseException {

        Page<BankTransactionListTo> bankTransactionEntitiesPaged = null;
        List<BankTransactionListTo> bankTransactionEntities;
        Pageable pageable = PageRequest.of(page, quantity);

        LocalDate startDateParsed = LocalDate.parse(startDate);

        LocalDate endDateParsed = LocalDate.parse(endDate);

        if (export) {
            bankTransactionEntities = bankTransactionRepository.getBankTransactionsExport(startDateParsed,endDateParsed);

        } else {
            bankTransactionEntitiesPaged = bankTransactionRepository.getBankTransactionsByDateIntervalPaginated(pageable,
                    startDateParsed,endDateParsed);
            bankTransactionEntities = bankTransactionEntitiesPaged.getContent();
        }

        if (bankTransactionEntities.isEmpty() && bankTransactionEntities.isEmpty()) {
            return new Response("SUCCESS", "No data");
        } else {
            return new Response("SUCCESS", export == true ?bankTransactionEntities : bankTransactionEntitiesPaged);
        }

    }

    public Response getBankTransactionsByDateIntervalPaginatedAndPaymentMethod(int page, int quantity,  String startDate, String endDate,
                                                                               Long paymentMethod, Boolean export)
            throws ParseException {

        Page<BankTransactionEntity> bankTransactionEntitiesPaged = null;
        List<BankTransactionEntity> bankTransactionEntities = new ArrayList<>();
        List<BankTransactionListTo> bankTransactionListTos = new ArrayList<>();
        Pageable pageable = PageRequest.of(page, quantity);

        LocalDate startDateParsed = LocalDate.parse(startDate);

        LocalDate endDateParsed = LocalDate.parse(endDate);

        if(export) {
            bankTransactionListTos = bankTransactionRepository.getBankTransactionsByPaymentMethodExport(startDateParsed,endDateParsed,
                    paymentMethod);
        } else {
            bankTransactionEntitiesPaged = bankTransactionRepository.getBankTransactionsByDateIntervalPaginatedAndByPaymentMethod(pageable,
                    startDateParsed,endDateParsed, paymentMethod);
            bankTransactionEntities = bankTransactionEntitiesPaged.getContent();
        }


        if (bankTransactionListTos.isEmpty() && bankTransactionEntities.isEmpty()) {
            return new Response("SUCCESS", "No data");
        } else {
            return new Response("SUCCESS", export == true ?bankTransactionListTos : bankTransactionEntitiesPaged);
        }

    }

    public Response getBankTransactionsByDateIntervalPaginatedAndIdCommerce
            (int page, int quantity,  String startDate, String endDate, String rif, Boolean export) throws ParseException {

        Page<BankTransactionEntity> bankTransactionEntitiesPaged = null;
        List<BankTransactionEntity> bankTransactionEntities = new ArrayList<>();
        List<BankTransactionListTo> bankTransactionListTos = new ArrayList<>();
        Pageable pageable = PageRequest.of(page, quantity);

        LocalDate startDateParsed = LocalDate.parse(startDate);
        LocalDate endDateParsed = LocalDate.parse(endDate);

        CommerceEntity commerceEntity = commerceRepository.findByCommerceDocument(rif);
        if(commerceEntity == null) {
            return new Response("ERROR", "comercio no registrado");
        }

        if(export) {
            bankTransactionListTos = bankTransactionRepository.getBankTransactionsByIdCommerceExport
                    (startDateParsed,endDateParsed, commerceEntity.getIdCommerce());
        } else {
            bankTransactionEntitiesPaged = bankTransactionRepository.getBankTransactionsByDateIntervalPaginatedAndByIdCommerce(pageable,
                    startDateParsed,endDateParsed, commerceEntity.getIdCommerce());
            bankTransactionEntities = bankTransactionEntitiesPaged.getContent();
        }


        if (bankTransactionEntities.isEmpty() && bankTransactionListTos.isEmpty()) {
            return new Response("SUCCESS", "No data");
        } else {
            return new Response("SUCCESS", export == true ? bankTransactionListTos : bankTransactionEntitiesPaged);
        }

    }

    public Response getBankTransactionsByDateIntervalPaginatedAndIdCommerceAndPaymentMethod
            (int page, int quantity,  String startDate, String endDate, String rif, Long paymentMethod, Boolean export) throws ParseException {

        Page<BankTransactionEntity> bankTransactionEntitiesPaged = null;
        List<BankTransactionEntity> bankTransactionEntities = new ArrayList<>();
        List<BankTransactionListTo> bankTransactionListTos = new ArrayList<>();
        Pageable pageable = PageRequest.of(page, quantity);

        LocalDate startDateParsed = LocalDate.parse(startDate);
        LocalDate endDateParsed = LocalDate.parse(endDate);

        CommerceEntity commerceEntity = commerceRepository.findByCommerceDocument(rif);
        if(commerceEntity == null) {
            return new Response("ERROR", "comercio no registrado");
        }

        if(export) {
            bankTransactionListTos = bankTransactionRepository.getBankTransactionsByIdCommerceAndPaymentMethodExport
                    (startDateParsed,endDateParsed, commerceEntity.getIdCommerce(), paymentMethod);
        } else {
            bankTransactionEntitiesPaged =
                    bankTransactionRepository.getBankTransactionsByDateIntervalPaginatedAndByIdCommerceAndPaymentMethod(pageable,
                            startDateParsed,endDateParsed, commerceEntity.getIdCommerce(), paymentMethod);
            bankTransactionEntities = bankTransactionEntitiesPaged.getContent();
        }

        if (bankTransactionListTos.isEmpty() && bankTransactionEntities.isEmpty()) {
            return new Response("SUCCESS", "No data");
        } else {
            return new Response("SUCCESS", export == true ? bankTransactionListTos : bankTransactionEntitiesPaged);
        }

    }

    public Response getAllBankTransactionsByDateAndStatusTransactionByCommerce
            (int page, int quantity,  String startDate, String endDate, String rif, Long statusPayment, Boolean export) throws ParseException {

        Page<BankTransactionEntity> bankTransactionEntitiesPaged = null;
        List<BankTransactionEntity> bankTransactionEntities = new ArrayList<>();
        List<BankTransactionListTo> bankTransactionListTos = new ArrayList<>();
        Pageable pageable = PageRequest.of(page, quantity);

        LocalDate startDateParsed = LocalDate.parse(startDate);
        LocalDate endDateParsed = LocalDate.parse(endDate);

        CommerceEntity commerceEntity = commerceRepository.findByCommerceDocument(rif);
        if(commerceEntity == null) {
            return new Response("ERROR", "comercio no registrado");
        }

        if(export) {
            bankTransactionListTos = bankTransactionRepository.getBankTransactionsByIdCommerceAndStatusTransactionExport
                    (startDateParsed,endDateParsed, commerceEntity.getIdCommerce(), statusPayment);
        } else {
            bankTransactionEntitiesPaged = bankTransactionRepository.
                    getBankTransactionsByDateIntervalPaginatedAndByIdCommerceAndStatusTransaction(pageable,
                            startDateParsed,endDateParsed, commerceEntity.getIdCommerce(), statusPayment);
            bankTransactionEntities = bankTransactionEntitiesPaged.getContent();
        }


        if (bankTransactionEntities.isEmpty() && bankTransactionListTos.isEmpty()) {
            return new Response("SUCCESS", "No data");
        } else {
            return new Response("SUCCESS", export == true ?bankTransactionListTos : bankTransactionEntitiesPaged);
        }

    }

    public Response getAllBankTransactionsByDateAndStatusTransaction
            (int page, int quantity,  String startDate, String endDate, Long statusPayment, Boolean export) throws ParseException {

        Page<BankTransactionEntity> bankTransactionEntitiesPaged = null;
        List<BankTransactionEntity> bankTransactionEntities = new ArrayList<>();
        List<BankTransactionListTo> bankTransactionListTos = new ArrayList<>();
        Pageable pageable = PageRequest.of(page, quantity);

        LocalDate startDateParsed = LocalDate.parse(startDate);
        LocalDate endDateParsed = LocalDate.parse(endDate);

        if(export) {
            bankTransactionListTos = bankTransactionRepository.getBankTransactionsByStatusTransactionExport(startDateParsed,
                    endDateParsed, statusPayment);
        } else {
            bankTransactionEntitiesPaged = bankTransactionRepository.getBankTransactionsByDateIntervalPaginatedByStatusTransaction(pageable,
                    startDateParsed,endDateParsed, statusPayment);
            bankTransactionEntities = bankTransactionEntitiesPaged.getContent();
        }

        if (bankTransactionEntities.isEmpty() && bankTransactionListTos.isEmpty()) {
            return new Response("SUCCESS", "No data");
        } else {
            return new Response("SUCCESS", export == true ? bankTransactionListTos : bankTransactionEntitiesPaged);
        }

    }

    public Response getAllPaymentMethods() {
        List<PaymentMethodEntity> paymentMethodEntities = new ArrayList<>();
        paymentMethodEntities = paymentMethodRepository.findAllByStatusTrue();

        if(paymentMethodEntities == null) {
            return new Response("ERROR", "No data");
        } else {
            return new Response("SUCCESS", paymentMethodEntities);
        }
    }

    public Response getAllProductType() {
        List<PaymentChannel> paymentChannels = new ArrayList<>();

        paymentChannels = paymentChannelRepository.findAllByStatusTrue();

        if(paymentChannels == null) {
            return new Response("ERROR", "No data");
        } else {
            return new Response("SUCCESS", paymentChannels);
        }
    }

    public Response getBankTransactionsByDateIntervalPaginatedAndProductType(int page, int quantity,  String startDate, String endDate,
                                                                               Long productType, Boolean export)
            throws ParseException {

        Page<BankTransactionEntity> bankTransactionEntitiesPaged = null;
        List<BankTransactionEntity> bankTransactionEntities = new ArrayList<>();
        Pageable pageable = PageRequest.of(page, quantity);
        List<BankTransactionListTo> bankTransactionListTos = new ArrayList<>();

        LocalDate startDateParsed = LocalDate.parse(startDate);

        LocalDate endDateParsed = LocalDate.parse(endDate);

        if(export) {
            bankTransactionListTos = bankTransactionRepository.getBankTransactionsByProductTypeExport
                    (startDateParsed,endDateParsed, productType);
        } else {
            bankTransactionEntitiesPaged = bankTransactionRepository.getBankTransactionsByDateIntervalPaginatedAndByProductType(pageable,
                    startDateParsed,endDateParsed, productType);
            bankTransactionEntities = bankTransactionEntitiesPaged.getContent();
        }

        if (bankTransactionListTos.isEmpty() && bankTransactionEntities.isEmpty()) {
            return new Response("SUCCESS", "No data");
        } else {
            return new Response("SUCCESS", export == true? bankTransactionListTos : bankTransactionEntitiesPaged);
        }
    }

    public Response getBankTransactionsByDateIntervalPaginatedAndIdCommerceAndProductType
            (int page, int quantity,  String startDate, String endDate, String rif, Long productType, Boolean export) throws ParseException {

        Page<BankTransactionEntity> bankTransactionEntitiesPaged = null;
        List<BankTransactionEntity> bankTransactionEntities = new ArrayList<>();
        Pageable pageable = PageRequest.of(page, quantity);
        List<BankTransactionListTo> bankTransactionListTos = new ArrayList<>();

        LocalDate startDateParsed = LocalDate.parse(startDate);
        LocalDate endDateParsed = LocalDate.parse(endDate);

        CommerceEntity commerceEntity = commerceRepository.findByCommerceDocument(rif);
        if(commerceEntity == null) {
            return new Response("ERROR", "comercio no registrado");
        }

        if(export) {
            bankTransactionListTos = bankTransactionRepository.getBankTransactionsByIdCommerceAndProductTypeExport
                    (startDateParsed,endDateParsed, commerceEntity.getIdCommerce(), productType);
        } else {
            bankTransactionEntitiesPaged = bankTransactionRepository.getBankTransactionsByDateIntervalPaginatedAndByIdCommerceAndProductType(pageable,
                    startDateParsed,endDateParsed, commerceEntity.getIdCommerce(), productType);

            bankTransactionEntities = bankTransactionEntitiesPaged.getContent();
        }

        if (bankTransactionListTos.isEmpty() && bankTransactionEntities.isEmpty()) {
            return new Response("SUCCESS", "No data");
        } else {
            return new Response("SUCCESS", export == true ? bankTransactionListTos : bankTransactionEntitiesPaged);
        }

    }

    public Response getAllManualPaymentsByDateInterval(int page,
                                                       int quantity,
                                                       String startDate,
                                                       String endDate,
                                                       String apiKey,
                                                       String token,
                                                       Boolean export, String rif) {
        try {

            HttpHeaders headers = new HttpHeaders();
            RestTemplate restTemplate = new RestTemplate();

            headers.add("API_KEY", apiKey);

            String uri = "http://localhost:8090/core";

            LinkedHashMap<String, Object> requestBody = new LinkedHashMap<>();

            requestBody.put("startDate", startDate);
            requestBody.put("endDate", endDate);
            requestBody.put("page", page);
            requestBody.put("quantity", quantity);
            requestBody.put("export", export);
            requestBody.put("rif", rif);

            HttpEntity<LinkedHashMap<String, Object>> request = new HttpEntity<>(requestBody, headers);

            ResponseEntity<String> responseEntity = restTemplate.exchange(uri.
                            concat("/getAllManualPayments"), HttpMethod.POST,
                    request, String.class);

            if (!Objects.requireNonNull(responseEntity.getBody()).contains("ERROR")) {

                ObjectMapper objectMapper = new ObjectMapper();

                LinkedHashMap<String, Object> responseMap = objectMapper.readValue(responseEntity.getBody(), LinkedHashMap.class);

                if(export) {

                    List<LinkedHashMap<String, Object>> linkedHashMapResponse =
                            (List<LinkedHashMap<String, Object>>) responseMap.get("bankTransactionEntityExport");

                    return new Response("SUCCESS", linkedHashMapResponse);


                } else {
                    LinkedHashMap linkedHashMapResponse =
                            (objectMapper.convertValue(responseMap.get("bankTransactionEntity"), LinkedHashMap.class));

                    LinkedHashMap<String,Object> paginatedResponse = new LinkedHashMap<>();
                    paginatedResponse.put("content", linkedHashMapResponse.get("content"));
                    paginatedResponse.put("pageable", linkedHashMapResponse.get("pageable"));
                    paginatedResponse.put("total", linkedHashMapResponse.get("totalElements"));
                    paginatedResponse.put("totalPages", linkedHashMapResponse.get("totalPages"));
                    return new Response("SUCCESS", paginatedResponse);
                }

            } else {
                return new Response("ERROR", null);
            }

        } catch (Exception e) {
            return new Response("ERROR", e);
        }

    }

    public Response changeStatusBankTransactionManualPayment(Long statusBankTransaction,
                                                             String username,
                                                             Long idBankTransaction,
                                                             String apiKey,
                                                             String token) {

        try {

            HttpHeaders headers = new HttpHeaders();
            RestTemplate restTemplate = new RestTemplate();

            headers.add("API_KEY", apiKey);
            headers.add("token", token);

            String uri = "http://localhost:8090/core";

            LinkedHashMap<String, Object> requestBody = new LinkedHashMap<>();

            requestBody.put("statusBankTransaction", statusBankTransaction);
            requestBody.put("username", username);
            requestBody.put("idBankTransaction", idBankTransaction);

            HttpEntity<LinkedHashMap<String, Object>> request = new HttpEntity<>(requestBody, headers);

            ResponseEntity<String> responseEntity = restTemplate.exchange(uri.
                            concat("/changeStatusBankTransactionManualPayment"), HttpMethod.POST,
                    request, String.class);

            if (!Objects.requireNonNull(responseEntity.getBody()).contains("ERROR")) {

                ObjectMapper objectMapper = new ObjectMapper();

                LinkedHashMap<String, Object> responseMap = objectMapper.readValue(responseEntity.getBody(), LinkedHashMap.class);

                return new Response("SUCCESS", responseMap.get("SUCCESS"));

            } else {
                return new Response("ERROR", "Error cambiando estado de pago manual");
            }

        } catch (Exception e) {
            return new Response("ERROR", e);
        }

    }

    public Response getAllBankTransactionsByFilter(final String apiKey, final String token,
                                           final int index,final int quantity,
                                           final String startDate,final String endDate,
                                           final String filter, final Integer typeFilter, final Boolean export, final String rif){

        try {

            HttpHeaders headers = new HttpHeaders();
            RestTemplate restTemplate = new RestTemplate();
            ObjectMapper objectMapper = new ObjectMapper();

            headers.add("API_KEY", apiKey);
            headers.add("token", token);

            String uri = environment.getProperty("api.route.internal");

            LinkedHashMap<String, Object> requestBody = new LinkedHashMap<>();

            requestBody.put("filter", filter);
            requestBody.put("typeFilter", typeFilter);
            requestBody.put("startDate", startDate);
            requestBody.put("endDate", endDate);
            requestBody.put("index", index);
            requestBody.put("quantity", quantity);
            requestBody.put("export", export);
            requestBody.put("rif", rif);

            HttpEntity<LinkedHashMap<String, Object>> request = new HttpEntity<>(requestBody, headers);

            ResponseEntity<String> responseEntity = restTemplate.exchange(uri.
                    concat("/getAllBankTransactionsByFilter"), HttpMethod.POST, request, String.class);

            if (!Objects.requireNonNull(responseEntity.getBody()).contains("ERROR")) {
                LinkedHashMap<String, Object> responseMap = objectMapper.readValue(responseEntity.getBody(), LinkedHashMap.class);
                List<BankTransactionListTo> bankTransactionListTos = (List<BankTransactionListTo>) responseMap.get("bankTransactionListTos");

                if(export) {
                    return new Response("SUCCESS", bankTransactionListTos);
                } else {

                    Pageable pageList = PageRequest.of(index, quantity);
                    int start = (int) pageList.getOffset();
                    int end = Math.min((start + pageList.getPageSize()), bankTransactionListTos.size());

                    Page<BankTransactionListTo> bankTransactionListToPage =  new
                            PageImpl<>(bankTransactionListTos.subList(start, end), pageList, bankTransactionListTos.size());

                    return new Response("SUCCESS", bankTransactionListToPage);
                }


            } else {
                logger.error("Response Status: " + responseEntity.getStatusCode());
                return null;
            }

        } catch (Exception e){
            logger.error(e.getMessage(), e);
            return null;
        }

    }

    public Response getAllBankTransactionsByFilterPaymentMethod(final String apiKey, final String token,
                                                   final int index,final int quantity,
                                                   final String startDate,final String endDate,
                                                   final String filter, final Integer typeFilter, final Boolean export, final String rif){

        try {

            HttpHeaders headers = new HttpHeaders();
            RestTemplate restTemplate = new RestTemplate();
            ObjectMapper objectMapper = new ObjectMapper();

            headers.add("API_KEY", apiKey);
            headers.add("token", token);

            String uri = environment.getProperty("api.route.internal");

            LinkedHashMap<String, Object> requestBody = new LinkedHashMap<>();

            requestBody.put("filter", filter);
            requestBody.put("typeFilter", typeFilter);
            requestBody.put("startDate", startDate);
            requestBody.put("endDate", endDate);
            requestBody.put("index", index);
            requestBody.put("quantity", quantity);
            requestBody.put("export", export);
            requestBody.put("rif", rif);

            HttpEntity<LinkedHashMap<String, Object>> request = new HttpEntity<>(requestBody, headers);

            ResponseEntity<String> responseEntity = restTemplate.exchange(uri.
                    concat("/getAllBankTransactionsByPaymentMethodFilter"), HttpMethod.POST, request, String.class);

            if (!Objects.requireNonNull(responseEntity.getBody()).contains("ERROR")) {
                LinkedHashMap<String, Object> responseMap = objectMapper.readValue(responseEntity.getBody(), LinkedHashMap.class);
                List<BankTransactionListTo> bankTransactionListTos = (List<BankTransactionListTo>) responseMap.get("bankTransactionListTos");

                if(export) {
                    return new Response("SUCCESS", bankTransactionListTos);
                } else {

                    Pageable pageList = PageRequest.of(index, quantity);
                    int start = (int) pageList.getOffset();
                    int end = Math.min((start + pageList.getPageSize()), bankTransactionListTos.size());

                    Page<BankTransactionListTo> bankTransactionListToPage =  new
                            PageImpl<>(bankTransactionListTos.subList(start, end), pageList, bankTransactionListTos.size());

                    return new Response("SUCCESS", bankTransactionListToPage);
                }


            } else {
                logger.error("Response Status: " + responseEntity.getStatusCode());
                return null;
            }

        } catch (Exception e){
            logger.error(e.getMessage(), e);
            return null;
        }

    }

    public Response getAllBankTransactionsByFilterManualPayment(final String apiKey, final String token,
                                                                final int index,final int quantity,
                                                                final String startDate,final String endDate,
                                                                final String filter, final Integer typeFilter, final Boolean export, final String rif){

        try {

            HttpHeaders headers = new HttpHeaders();
            RestTemplate restTemplate = new RestTemplate();
            ObjectMapper objectMapper = new ObjectMapper();

            headers.add("API_KEY", apiKey);
            headers.add("token", token);

            String uri = environment.getProperty("api.route.internal");

            LinkedHashMap<String, Object> requestBody = new LinkedHashMap<>();

            requestBody.put("filter", filter);
            requestBody.put("typeFilter", typeFilter);
            requestBody.put("startDate", startDate);
            requestBody.put("endDate", endDate);
            requestBody.put("index", index);
            requestBody.put("quantity", quantity);
            requestBody.put("export", export);
            requestBody.put("rif", rif);

            HttpEntity<LinkedHashMap<String, Object>> request = new HttpEntity<>(requestBody, headers);

            ResponseEntity<String> responseEntity = restTemplate.exchange(uri.
                    concat("/getAllBankTransactionsByManualPaymentFilter"), HttpMethod.POST, request, String.class);

            if (!Objects.requireNonNull(responseEntity.getBody()).contains("ERROR")) {
                LinkedHashMap<String, Object> responseMap = objectMapper.readValue(responseEntity.getBody(), LinkedHashMap.class);
                List<BankTransactionListTo> bankTransactionListTos = (List<BankTransactionListTo>) responseMap.get("bankTransactionListTos");

                if(export) {
                    return new Response("SUCCESS", bankTransactionListTos);
                } else {

                    Pageable pageList = PageRequest.of(index, quantity);
                    int start = (int) pageList.getOffset();
                    int end = Math.min((start + pageList.getPageSize()), bankTransactionListTos.size());

                    Page<BankTransactionListTo> bankTransactionListToPage =  new
                            PageImpl<>(bankTransactionListTos.subList(start, end), pageList, bankTransactionListTos.size());

                    return new Response("SUCCESS", bankTransactionListToPage);
                }


            } else {
                logger.error("Response Status: " + responseEntity.getStatusCode());
                return null;
            }

        } catch (Exception e){
            logger.error(e.getMessage(), e);
            return null;
        }

    }


    public Response getLastTransactions(final int index, final int quantity, final Boolean export)
            throws ParseException {

        ConfigurationEntity configurationEntity = configurationRepository.findConfigurationEntityByIdConfiguration(11L);
        final int limit = Integer.parseInt(configurationEntity.getValue());

        Page<BankTransactionListTo> bankTransactionListTos = bankTransactionRepository.
                getBankTransactionsByDateIntervalPaginatedLast(PageRequest.of(0, limit));

        Pageable pageList = PageRequest.of(index, quantity);

        final int start = (int) pageList.getOffset();
        final int end = Math.min((start + pageList.getPageSize()), bankTransactionListTos.getContent().size());

        List<BankTransactionListTo> bankTransactionListToList = List.copyOf(bankTransactionListTos.getContent().subList(start, end));

        Page<BankTransactionListTo> bankTransactionListToPage = new
                PageImpl<>(bankTransactionListToList, pageList, bankTransactionListTos.getContent().size());

        if(bankTransactionListTos.isEmpty()) {
            return new Response("SUCCESS", "No data");
        } else {
            return new Response("SUCCESS",export == true ? bankTransactionListToPage.getContent() :  bankTransactionListToPage);
        }
    }

    public Response getLastTransactionsByStatusTransaction(int index, int quantity, Long idStatus, Boolean export)
            throws ParseException {

        ConfigurationEntity configurationEntity = configurationRepository.findConfigurationEntityByIdConfiguration(11L);
        int limit = Integer.parseInt(configurationEntity.getValue());

        Page<BankTransactionListTo> bankTransactionListTos = bankTransactionRepository.
                getBankTransactionsByDateIntervalPaginatedByStatusTransactionLast(PageRequest.of(0, limit), idStatus);

        Pageable pageList = PageRequest.of(index, quantity);

        int start = (int) pageList.getOffset();
        int end = Math.min((start + pageList.getPageSize()), bankTransactionListTos.getContent().size());

        List<BankTransactionListTo> bankTransactionListToList = bankTransactionListTos.getContent().subList(start, end);

        Page<BankTransactionListTo> bankTransactionListToPage = new
                PageImpl<>(bankTransactionListToList, pageList, bankTransactionListTos.getContent().size());

        if(bankTransactionListTos.isEmpty()) {
            return new Response("SUCCESS", "No data");
        } else {
            return new Response("SUCCESS",export == true ? bankTransactionListToPage.getContent() : bankTransactionListToPage);
        }
    }

    public Response getLastTransactionsByStatusTransactionAndCommerce(int index, int quantity, Long idStatus, String rif, Boolean export)
            throws ParseException {

        ConfigurationEntity configurationEntity = configurationRepository.findConfigurationEntityByIdConfiguration(11L);
        int limit = Integer.parseInt(configurationEntity.getValue());

        CommerceEntity commerceEntity = commerceRepository.findByCommerceDocument(rif);
        if(commerceEntity == null) {
            return new Response("ERROR", "comercio no registrado");
        }

        Page<BankTransactionListTo> bankTransactionListTos = bankTransactionRepository.
                getBankTransactionsByIdCommerceAndStatusTransactionExportLast(PageRequest.of(0, limit), idStatus,
                        commerceEntity.getIdCommerce());

        Pageable pageList = PageRequest.of(index, quantity);

        int start = (int) pageList.getOffset();
        int end = Math.min((start + pageList.getPageSize()), bankTransactionListTos.getContent().size());

        List<BankTransactionListTo> bankTransactionListToList = bankTransactionListTos.getContent().subList(start, end);

        Page<BankTransactionListTo> bankTransactionListToPage = new
                PageImpl<>(bankTransactionListToList, pageList, bankTransactionListTos.getContent().size());

        if(bankTransactionListTos.isEmpty()) {
            return new Response("SUCCESS", "No data");
        } else {
            return new Response("SUCCESS",export== true ? bankTransactionListToPage.getContent() : bankTransactionListToPage);
        }
    }

    public Response getLastTransactionsByManualPaymentAndCommerce(int index, int quantity, String rif, Boolean export)
            throws ParseException {

        ConfigurationEntity configurationEntity = configurationRepository.findConfigurationEntityByIdConfiguration(11L);
        int limit = Integer.parseInt(configurationEntity.getValue());

        CommerceEntity commerceEntity = commerceRepository.findByCommerceDocument(rif);
        if(commerceEntity == null) {
            return new Response("ERROR", "comercio no registrado");
        }

        Page<BankTransactionListTo> bankTransactionListTos = bankTransactionRepository.
                getBankTransactionsByDateIntervalPaginatedAndManualPaymentByCommerceLast(PageRequest.of(0, limit),
                        commerceEntity.getIdCommerce());

        Pageable pageList = PageRequest.of(index, quantity);

        int start = (int) pageList.getOffset();
        int end = Math.min((start + pageList.getPageSize()), bankTransactionListTos.getContent().size());

        List<BankTransactionListTo> bankTransactionListToList = bankTransactionListTos.getContent().subList(start, end);

        Page<BankTransactionListTo> bankTransactionListToPage = new
                PageImpl<>(bankTransactionListToList, pageList, bankTransactionListTos.getContent().size());

        if(bankTransactionListTos.isEmpty()) {
            return new Response("SUCCESS", "No data");
        } else {
            return new Response("SUCCESS", export == true ? bankTransactionListToPage.getContent() : bankTransactionListToPage);
        }
    }

    public Response getLastTransactionsByManualPayment(int index, int quantity, Boolean export)
            throws ParseException {

        ConfigurationEntity configurationEntity = configurationRepository.findConfigurationEntityByIdConfiguration(11L);
        int limit = Integer.parseInt(configurationEntity.getValue());

        Page<BankTransactionListTo> bankTransactionListTos = bankTransactionRepository.
                getBankTransactionsByDateIntervalPaginatedAndManualPaymentLast(PageRequest.of(0, limit));

        Pageable pageList = PageRequest.of(index, quantity);

        int start = (int) pageList.getOffset();
        int end = Math.min((start + pageList.getPageSize()), bankTransactionListTos.getContent().size());

        List<BankTransactionListTo> bankTransactionListToList = bankTransactionListTos.getContent().subList(start, end);

        Page<BankTransactionListTo> bankTransactionListToPage = new
                PageImpl<>(bankTransactionListToList, pageList, bankTransactionListTos.getContent().size());

        if(bankTransactionListTos.isEmpty()) {
            return new Response("SUCCESS", "No data");
        } else {
            return new Response("SUCCESS", export==true ? bankTransactionListToPage.getContent() :  bankTransactionListToPage);
        }
    }
    public Response getLastTransactionsByCommerce(String rif, int index, int quantity, Boolean export)
            throws ParseException {

        ConfigurationEntity configurationEntity = configurationRepository.findConfigurationEntityByIdConfiguration(11L);
        int limit = Integer.parseInt(configurationEntity.getValue());

        CommerceEntity commerceEntity = commerceRepository.findByCommerceDocument(rif);

        Page<BankTransactionListTo> bankTransactionListTos = bankTransactionRepository.
                getBankTransactionsByDateIntervalPaginatedLastByCommerce(PageRequest.of(0, limit),
                        commerceEntity.getIdCommerce());

        Pageable pageList = PageRequest.of(index, quantity);

        int start = (int) pageList.getOffset();
        int end = Math.min((start + pageList.getPageSize()), bankTransactionListTos.getContent().size());

        List<BankTransactionListTo> bankTransactionListToList = bankTransactionListTos.getContent().subList(start, end);

        Page<BankTransactionListTo> bankTransactionListToPage = new
                PageImpl<>(bankTransactionListToList, pageList, bankTransactionListTos.getContent().size());

        if(bankTransactionListTos.isEmpty()) {
            return new Response("SUCCESS", "No data");
        } else {
            return new Response("SUCCESS", export == true ? bankTransactionListToPage.getContent() : bankTransactionListToPage);
        }
    }


    public Response getAllBankTransactionsAndStatusByFilter(final String apiKey, final String token,
                                                   final int index,final int quantity,
                                                   final String startDate,final String endDate,
                                                   final String filter, final Integer typeFilter, final Boolean export,
                                                            final String rif,final Long idStatusTransaction){

        try {

            HttpHeaders headers = new HttpHeaders();
            RestTemplate restTemplate = new RestTemplate();
            ObjectMapper objectMapper = new ObjectMapper();

            headers.add("API_KEY", apiKey);
            headers.add("token", token);

            String uri = environment.getProperty("api.route.internal");

            LinkedHashMap<String, Object> requestBody = new LinkedHashMap<>();

            requestBody.put("filter", filter);
            requestBody.put("typeFilter", typeFilter);
            requestBody.put("startDate", startDate);
            requestBody.put("endDate", endDate);
            requestBody.put("index", index);
            requestBody.put("quantity", quantity);
            requestBody.put("export", export);
            requestBody.put("rif", rif);
            requestBody.put("idStatusTransaction",idStatusTransaction);

            HttpEntity<LinkedHashMap<String, Object>> request = new HttpEntity<>(requestBody, headers);

            ResponseEntity<String> responseEntity = restTemplate.exchange(uri.
                    concat("/getAllBankTransactionsAndStatusByFilter"), HttpMethod.POST, request, String.class);

            if (!Objects.requireNonNull(responseEntity.getBody()).contains("ERROR")) {
                LinkedHashMap<String, Object> responseMap = objectMapper.readValue(responseEntity.getBody(), LinkedHashMap.class);
                List<BankTransactionListTo> bankTransactionListTos = (List<BankTransactionListTo>) responseMap.get("bankTransactionListTos");

                if(export) {
                    return new Response("SUCCESS", bankTransactionListTos);
                } else {

                    Pageable pageList = PageRequest.of(index, quantity);
                    int start = (int) pageList.getOffset();
                    int end = Math.min((start + pageList.getPageSize()), bankTransactionListTos.size());

                    Page<BankTransactionListTo> bankTransactionListToPage =  new
                            PageImpl<>(bankTransactionListTos.subList(start, end), pageList, bankTransactionListTos.size());

                    return new Response("SUCCESS", bankTransactionListToPage);
                }


            } else {
                logger.error("Response Status: " + responseEntity.getStatusCode());
                return null;
            }

        } catch (Exception e){
            logger.error(e.getMessage(), e);
            return null;
        }

    }


    public Response getAllBankTransactionsAndTypeProductByFilter(final String apiKey, final String token,
                                                            final int index,final int quantity,
                                                            final String startDate,final String endDate,
                                                            final String filter, final Integer typeFilter, final Boolean export,
                                                            final String rif){

        try {

            HttpHeaders headers = new HttpHeaders();
            RestTemplate restTemplate = new RestTemplate();
            ObjectMapper objectMapper = new ObjectMapper();

            headers.add("API_KEY", apiKey);
            headers.add("token", token);

            String uri = environment.getProperty("api.route.internal");

            LinkedHashMap<String, Object> requestBody = new LinkedHashMap<>();

            requestBody.put("filter", filter);
            requestBody.put("typeFilter", typeFilter);
            requestBody.put("startDate", startDate);
            requestBody.put("endDate", endDate);
            requestBody.put("index", index);
            requestBody.put("quantity", quantity);
            requestBody.put("export", export);
            requestBody.put("rif", rif);

            HttpEntity<LinkedHashMap<String, Object>> request = new HttpEntity<>(requestBody, headers);

            ResponseEntity<String> responseEntity = restTemplate.exchange(uri.
                    concat("/getAllBankTransactionsAndTypeProductByFilter"), HttpMethod.POST, request, String.class);

            if (!Objects.requireNonNull(responseEntity.getBody()).contains("ERROR")) {
                LinkedHashMap<String, Object> responseMap = objectMapper.readValue(responseEntity.getBody(), LinkedHashMap.class);
                List<BankTransactionListTo> bankTransactionListTos = (List<BankTransactionListTo>) responseMap.get("bankTransactionListTos");

                if(export) {
                    return new Response("SUCCESS", bankTransactionListTos);
                } else {

                    Pageable pageList = PageRequest.of(index, quantity);
                    int start = (int) pageList.getOffset();
                    int end = Math.min((start + pageList.getPageSize()), bankTransactionListTos.size());

                    Page<BankTransactionListTo> bankTransactionListToPage =  new
                            PageImpl<>(bankTransactionListTos.subList(start, end), pageList, bankTransactionListTos.size());

                    return new Response("SUCCESS", bankTransactionListToPage);
                }


            } else {
                logger.error("Response Status: " + responseEntity.getStatusCode());
                return null;
            }

        } catch (Exception e){
            logger.error(e.getMessage(), e);
            return null;
        }

    }
}
