package api.apiP2c.service;

import api.apiP2c.entity.*;
import api.apiP2c.repository.BankRepository;
import api.apiP2c.repository.BankTransactionRepository;
import api.apiP2c.repository.CommerceRepository;
import api.apiP2c.request.P2cRequest;
import api.apiP2c.response.ResponseP2C;
import api.apiP2c.util.Response;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import okhttp3.*;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class P2cService {

    private final BankCommerceService bankCommerceService;

    private final BankServiceService bankServiceService;

    private final BankRepository bankRepository;

    private final BankTransactionRepository bankTransactionRepository;

    private final CommerceRepository commerceRepository;

    private final EmailService emailService;

    private final MessageService messageService;

    public P2cService(BankCommerceService bankCommerceService, BankServiceService bankServiceService,
                      BankRepository bankRepository, BankTransactionRepository bankTransactionRepository,
                      CommerceRepository commerceRepository, EmailService emailService, MessageService messageService) {
        this.bankCommerceService = bankCommerceService;
        this.bankServiceService = bankServiceService;
        this.bankRepository = bankRepository;
        this.bankTransactionRepository = bankTransactionRepository;
        this.commerceRepository = commerceRepository;
        this.emailService = emailService;
        this.messageService = messageService;
    }

    public Response validatePaymentP2c(P2cRequest p2cRequest,
                                       HttpServletRequest httpServletRequest,
                                       HttpServletResponse httpServletResponse,
                                       BankCommerceEntity bankCommerceEntity) throws IOException, MessagingException {

        ResponseP2C p2cBankResponse;

        if(!validateIfPaymentAlreadyExist(p2cRequest)) {
            return new Response("ERROR", "Referencia de pago registrada");
        }
        if(bankCommerceEntity.getToken()== null) {
            String bankToken = getBankToken(bankCommerceEntity.getConsumerKey(), bankCommerceEntity.getConsumerSecret(), bankCommerceEntity);
            if(bankToken == null) {
                return new Response("ERROR", "Error en metodo token");
            }
            p2cBankResponse = callToBank(p2cRequest, bankToken, bankCommerceEntity);
            if(p2cBankResponse == null) {
                return new Response("ERROR", "Error en metodo de pago");
            }

            if (saveBankTransaction(p2cRequest, p2cBankResponse)) {

                if(p2cBankResponse.getSuccess()) {
                    emailService.sendPaymentReceipt(p2cRequest.getEmail(), "payment-Recibo de pago-Pago Móvil P2C",
                            httpServletRequest.getHeader("API_KEY"),p2cRequest,p2cBankResponse);
                    return new Response("SUCCESS", "Transacción exitosa");
                } else {
                    String messageBank = messageService.returnResponseError(p2cBankResponse.getMessage().getCode(), 5L);
                    LinkedHashMap<String,Object> responseBank = new LinkedHashMap<>();
                    responseBank.put("code",p2cBankResponse.getMessage().getCode());
                    responseBank.put("message", messageBank);
                    Response response = new Response("ERROR", "mensaje sistema");
                    response.setProperties(responseBank);
                    return response;
                }
            }

        } else {
            if(!validateBankToken(bankCommerceEntity)) {
                String newBankToken = getBankToken(bankCommerceEntity.getConsumerKey(), bankCommerceEntity.getConsumerSecret(), bankCommerceEntity);
                if(newBankToken == null) {
                    return new Response("ERROR", "Error en metodo token");
                }
                p2cBankResponse = callToBank(p2cRequest, newBankToken, bankCommerceEntity);
                if(p2cBankResponse == null) {
                    return new Response("ERROR", "Error en metodo de pago");
                }
                if (saveBankTransaction(p2cRequest, p2cBankResponse)) {
                    if(p2cBankResponse.getSuccess()) {
                        emailService.sendPaymentReceipt(p2cRequest.getEmail(), "payment-Recibo de pago-Pago Móvil P2C",
                                httpServletRequest.getHeader("API_KEY"),p2cRequest,p2cBankResponse);
                        return new Response("SUCCESS", "Transacción exitosa");
                    } else {
                        String messageBank = messageService.returnResponseError(p2cBankResponse.getMessage().getCode(), 5L);
                        LinkedHashMap<String,Object> responseBank = new LinkedHashMap<>();
                        responseBank.put("code",p2cBankResponse.getMessage().getCode());
                        responseBank.put("message", messageBank);
                        Response response = new Response("ERROR", "mensaje sistema");
                        response.setProperties(responseBank);
                        return response;
                    }
                }
            } else {
                p2cBankResponse = callToBank(p2cRequest, bankCommerceEntity.getToken(), bankCommerceEntity);
                if(p2cBankResponse == null) {
                    httpServletResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    return new Response("ERROR", "Error en metodo de pago");
                }

                if (saveBankTransaction(p2cRequest, p2cBankResponse)) {
                    if(p2cBankResponse.getSuccess()) {
                        emailService.sendPaymentReceipt(p2cRequest.getEmail(), "payment-Recibo de pago-Pago Móvil P2C",
                                httpServletRequest.getHeader("API_KEY"),p2cRequest,p2cBankResponse);
                        return new Response("SUCCESS", "Transacción exitosa");

                    } else {
                        String messageBank = messageService.returnResponseError(p2cBankResponse.getMessage().getCode(), 5L);
                        LinkedHashMap<String,Object> responseBank = new LinkedHashMap<>();
                        responseBank.put("code",p2cBankResponse.getMessage().getCode());
                        responseBank.put("message",messageBank);
                        Response response = new Response("ERROR", "mensaje sistema");
                        response.setProperties(responseBank);
                        return response;
                    }
                }
            }
        }
        return new Response("ERROR", "Informacion bancaria no existe");
    }

    public Response validateExternalPayment(P2cRequest p2cRequest,
                                            HttpServletRequest httpServletRequest,
                                            HttpServletResponse httpServletResponse,
                                            BankCommerceEntity bankCommerceEntity) throws IOException {
        ResponseP2C p2cBankResponse;

        BankTransactionEntity bankTransactionEntityExist = validateIfExternalPaymentAlreadyExist(p2cRequest);
        if(bankTransactionEntityExist != null) {
            return new Response("SUCCESS", bankTransactionEntityExist);
        }

        if(bankCommerceEntity.getToken()== null) {
            String bankToken = getBankToken(bankCommerceEntity.getConsumerKey(), bankCommerceEntity.getConsumerSecret(), bankCommerceEntity);
            if(bankToken == null) {
                return new Response("ERROR", "Error en metodo token");
            }
            p2cBankResponse = callToBank(p2cRequest, bankToken, bankCommerceEntity);
            if(p2cBankResponse == null) {
                return new Response("ERROR", "Error en metodo de pago");
            }
            BankTransactionEntity bankTransactionEntity = saveValidatedTransaction(p2cRequest, p2cBankResponse);
            if (bankTransactionEntity != null) {
                if(p2cBankResponse.getSuccess()) {
                    return new Response("SUCCESS", bankTransactionEntity);
                } else {
                    return new Response("ERROR", "No existe registro asociado a los datos ingresados");
                }
            }

        } else {
            if(!validateBankToken(bankCommerceEntity)) {
                String newBankToken = getBankToken(bankCommerceEntity.getConsumerKey(), bankCommerceEntity.getConsumerSecret(), bankCommerceEntity);
                if(newBankToken == null) {
                    return new Response("ERROR", "Error en metodo token");
                }
                p2cBankResponse = callToBank(p2cRequest, newBankToken, bankCommerceEntity);
                if(p2cBankResponse == null) {
                    return new Response("ERROR", "Error en metodo de pago");
                }
                BankTransactionEntity bankTransactionEntity = saveValidatedTransaction(p2cRequest, p2cBankResponse);
                if (bankTransactionEntity != null) {
                    if(p2cBankResponse.getSuccess()) {
                        return new Response("SUCCESS", bankTransactionEntity);
                    } else {
                        return new Response("ERROR", "No existe registro asociado a los datos ingresados");
                    }
                }

            } else {
                p2cBankResponse = callToBank(p2cRequest, bankCommerceEntity.getToken(), bankCommerceEntity);
                if(p2cBankResponse == null) {
                    return new Response("ERROR", "Error en metodo de pago");
                }
                BankTransactionEntity bankTransactionEntity = saveValidatedTransaction(p2cRequest, p2cBankResponse);
                if (bankTransactionEntity != null) {
                    if(p2cBankResponse.getSuccess()) {
                        return new Response("SUCCESS", bankTransactionEntity);
                    } else {
                        return new Response("ERROR", "No existe registro asociado a los datos ingresados");
                    }
                }

            }
        }
        return new Response("ERROR", "Informacion bancaria no existe");
    }

    private Boolean saveBankTransaction(P2cRequest p2cRequest, ResponseP2C p2cBankResponse) {
        try {
            if(p2cRequest.getPaymentChannel() != 2L) {
                BankTransactionEntity bankTransactionEntity = new BankTransactionEntity();

                BankEntity bankEntity = bankRepository.findByBankCodeAndStatusTrue(p2cRequest.getBankPayment());
                bankTransactionEntity.setBankEntity(bankEntity);
                bankTransactionEntity.setTransactionCode(p2cBankResponse.getMessage() != null? p2cBankResponse.getMessage().getCode(): "0");
                bankTransactionEntity.setReferenceNumber(Long.valueOf(p2cRequest.getReferenceNumber()));
                bankTransactionEntity.setAmount(p2cBankResponse.getMonto() != null?
                        new BigDecimal(p2cBankResponse.getMonto()) :
                        new BigDecimal(p2cRequest.getTransactionAmount()));
                CommerceEntity commerceEntity = commerceRepository.findByCommerceDocument(p2cRequest.getRif());
                bankTransactionEntity.setCommerceEntity(commerceEntity);
                bankTransactionEntity.setPaymentChannel(new PaymentChannel(p2cRequest.getPaymentChannel()));
                bankTransactionEntity.setSenderPhoneNumber(p2cRequest.getDebitPhone());
                bankTransactionEntity.setSenderIdentificationDocument(p2cRequest.getPayerDocument());
                bankTransactionEntity.setCurrencyEntity(new CurrencyEntity(2L));
                bankTransactionEntity.setUpdateBy(p2cRequest.getPayerDocument());
                bankTransactionEntity.setRegisterBy(p2cRequest.getPayerDocument());
                if(p2cRequest.getTypeTransaction() != null) {
                    if(p2cRequest.getTypeTransaction().equals("TRF")) {
                        bankTransactionEntity.setPaymentMethodEntity(new PaymentMethodEntity(7L));
                    } else {
                        bankTransactionEntity.setPaymentMethodEntity(new PaymentMethodEntity(2L));
                    }
                } else {
                    bankTransactionEntity.setPaymentMethodEntity(new PaymentMethodEntity(2L));
                }

                if(p2cBankResponse.getSuccess()) {
                    bankTransactionEntity.setTransactionStatusEntity(new TransactionStatusEntity(1L));
                } else {
                    bankTransactionEntity.setTransactionStatusEntity(new TransactionStatusEntity(2L));

                }
                bankTransactionEntity.setRegisterDate(OffsetDateTime.now(ZoneId.of("America/Caracas")));
                bankTransactionEntity.setUpdateDate(OffsetDateTime.now(ZoneId.of("America/Caracas")));

                bankTransactionRepository.save(bankTransactionEntity);
                return true;

            } else {
                BankTransactionEntity bankTransactionEntity = bankTransactionRepository.
                        findByIdBankTransaction(p2cRequest.getIdBankTransaction());

                if(bankTransactionEntity == null){
                    return false;
                }

                BankEntity bankEntity = bankRepository.findByBankCodeAndStatusTrue(p2cRequest.getBankPayment());
                bankTransactionEntity.setBankEntity(bankEntity);
                bankTransactionEntity.setTransactionCode(p2cBankResponse.getMessage() != null? p2cBankResponse.getMessage().getCode(): "0");
                bankTransactionEntity.setReferenceNumber(Long.valueOf(p2cRequest.getReferenceNumber()));
                bankTransactionEntity.setAmount(p2cBankResponse.getMonto() != null?
                        new BigDecimal(p2cBankResponse.getMonto()) :
                        new BigDecimal(p2cRequest.getTransactionAmount()));

                bankTransactionEntity.setPaymentChannel(new PaymentChannel(p2cRequest.getPaymentChannel()));
                if(p2cBankResponse.getSuccess()) {
                    bankTransactionEntity.setTransactionStatusEntity(new TransactionStatusEntity(1L));
                } else {
                    bankTransactionEntity.setTransactionStatusEntity(new TransactionStatusEntity(2L));
                }

                if(p2cRequest.getTypeTransaction() != null) {
                    if(p2cRequest.getTypeTransaction().equals("TRF")) {
                        bankTransactionEntity.setPaymentMethodEntity(new PaymentMethodEntity(7L));
                    } else {
                        bankTransactionEntity.setPaymentMethodEntity(new PaymentMethodEntity(2L));
                    }
                } else {
                    bankTransactionEntity.setPaymentMethodEntity(new PaymentMethodEntity(2L));
                }
                bankTransactionEntity.setUpdateBy(p2cRequest.getPayerDocument());
                bankTransactionEntity.setRegisterBy(p2cRequest.getPayerDocument());

                bankTransactionEntity.setUpdateDate(OffsetDateTime.now(ZoneId.of("America/Caracas")));
                bankTransactionRepository.save(bankTransactionEntity);
                return true;
            }

        } catch (Exception e) {
            return false;
        }
    }

    private BankTransactionEntity saveValidatedTransaction(P2cRequest p2cRequest, ResponseP2C p2cBankResponse) {
        try {
            BankTransactionEntity bankTransactionEntity = new BankTransactionEntity();
            BankEntity bankEntity = bankRepository.findByBankCodeAndStatusTrue(p2cRequest.getBankPayment());
            bankTransactionEntity.setBankEntity(bankEntity);
            bankTransactionEntity.setTransactionCode(p2cBankResponse.getMessage() != null? p2cBankResponse.getMessage().getCode(): "0");
            bankTransactionEntity.setReferenceNumber(Long.valueOf(p2cRequest.getReferenceNumber()));
            bankTransactionEntity.setAmount(p2cBankResponse.getMonto() != null?
                    new BigDecimal(p2cBankResponse.getMonto()) :
                    new BigDecimal(p2cRequest.getTransactionAmount()));
            CommerceEntity commerceEntity = commerceRepository.findByCommerceDocument(p2cRequest.getRif());
            bankTransactionEntity.setCommerceEntity(commerceEntity);
            bankTransactionEntity.setPaymentChannel(new PaymentChannel(6L));

            bankTransactionEntity.setCurrencyEntity(new CurrencyEntity(2L));

            if(p2cBankResponse.getSuccess()) {
                bankTransactionEntity.setUpdateBy(p2cBankResponse.getCedulaCliente());
                bankTransactionEntity.setRegisterBy(p2cBankResponse.getCedulaCliente());
                bankTransactionEntity.setSenderPhoneNumber(p2cBankResponse.getTlfCliente());
                bankTransactionEntity.setSenderIdentificationDocument(p2cBankResponse.getCedulaCliente());
                bankTransactionEntity.setTransactionStatusEntity(new TransactionStatusEntity(1L));
                bankTransactionEntity.setRegisterDate(OffsetDateTime.now(ZoneId.of("America/Caracas")));
                bankTransactionEntity.setUpdateDate(formatBankDate(p2cBankResponse));
            } else {
                bankTransactionEntity.setTransactionStatusEntity(new TransactionStatusEntity(2L));
                bankTransactionEntity.setRegisterDate(OffsetDateTime.now(ZoneId.of("America/Caracas")));
                bankTransactionEntity.setUpdateDate(OffsetDateTime.now(ZoneId.of("America/Caracas")));
            }

                if(p2cRequest.getTypeTransaction().equals("TRF")) {
                    bankTransactionEntity.setPaymentMethodEntity(new PaymentMethodEntity(7L));
                } else {
                    bankTransactionEntity.setPaymentMethodEntity(new PaymentMethodEntity(2L));
                }

            BankTransactionEntity bankTransactionEntityResponse = bankTransactionRepository.save(bankTransactionEntity);
            return bankTransactionEntityResponse;
        } catch (Exception e) {
            return null;
        }
    }

    private OffsetDateTime formatBankDate(ResponseP2C p2cBankResponse) {
        try {
            String inputString = p2cBankResponse.getFechaTransaccion().substring(0, 10).concat(" ")
                                .concat(p2cBankResponse.getHoraTransaccion());

            String formatPattern = "MM-dd-yyyy HH:mm:ss";

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(formatPattern);
            LocalDateTime localDateTime = LocalDateTime.parse(inputString, formatter);

            OffsetDateTime offsetDateTime = OffsetDateTime.of(localDateTime, ZoneOffset.of("-04:00"));

            return offsetDateTime;

        } catch (Exception e) {
            return formatBankDateWithAnotherFormat(p2cBankResponse);
        }

    }
    private OffsetDateTime formatBankDateWithAnotherFormat(ResponseP2C p2cBankResponse) {
        try {
            String inputString = p2cBankResponse.getFechaTransaccion().substring(0, 10)
                    .concat(p2cBankResponse.getHoraTransaccion().substring(10, 22));

            String formatPattern = "yyyy-MM-dd HH:mm:ss.SS";
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(formatPattern);
            LocalDateTime localDateTime = LocalDateTime.parse(inputString, formatter);

            OffsetDateTime offsetDateTime = OffsetDateTime.of(localDateTime, ZoneOffset.of("-04:00"));

            return offsetDateTime;
        } catch (Exception e) {
            return formatBankDateWithAnotherFormat2(p2cBankResponse);
        }
    }

    private OffsetDateTime formatBankDateWithAnotherFormat2(ResponseP2C p2cBankResponse) {
        try {
            String inputString = p2cBankResponse.getFechaTransaccion().substring(0, 10)
                    .concat(p2cBankResponse.getHoraTransaccion().substring(10, 21).concat("0"));

            String formatPattern = "yyyy-MM-dd HH:mm:ss.SS";
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(formatPattern);
            LocalDateTime localDateTime = LocalDateTime.parse(inputString, formatter);

            OffsetDateTime offsetDateTime = OffsetDateTime.of(localDateTime, ZoneOffset.of("-04:00"));

            return offsetDateTime;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public String getBankToken(String consumerKey, String consumerSecret, BankCommerceEntity bankCommerceEntity) throws IOException {

        try {
            GsonBuilder b = new GsonBuilder();
            Gson gson = b.setPrettyPrinting().create();
            ObjectMapper objectMapper = new ObjectMapper();

            String requestKey = convertKeyToBase64(consumerKey, consumerSecret);
            String basicAuth = "Basic ".concat(requestKey);

            BankServiceEntity bankServiceEntity = bankServiceService.getUrlByIdPaymentMethodAndIdBank("token", 5L);
            String url = bankServiceEntity.getUrl();

            OkHttpClient client = new OkHttpClient();
            RequestBody requestBody = new FormBody.Builder()
                    .add("grant_type", "client_credentials")
                    .build();
            Request request = new Request.Builder()
                    .url(url)
                    .post(requestBody)
                    .addHeader("Authorization", basicAuth)
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .build();
            okhttp3.Response response1 = client.newCall(request).execute();
            ResponseBody responseBody = response1.body();
            String responseBodyString = responseBody.string();

            Type type = new com.google.gson.reflect.TypeToken<LinkedHashMap<String, Object>>(){}.getType();
            LinkedHashMap<String, Object> responseBodyParsed = gson.fromJson(responseBodyString, type);
            LinkedHashMap linkedHashMapResponse = (objectMapper.convertValue(responseBodyParsed, LinkedHashMap.class));

            Long expireSeconds = Math.round((Double) linkedHashMapResponse.get("expires_in"));

            bankCommerceService.saveBankCommerceToken(linkedHashMapResponse.get("access_token").toString(),
                    bankCommerceEntity.getIdBankCommerce(), expireSeconds);

            return linkedHashMapResponse.get("access_token").toString();

        } catch (/*IOException e*/ Exception e) {
            return null;
        } /*catch (JsonSyntaxException e) {
            return null;
        }*/
    }

    private ResponseP2C callToBank(P2cRequest p2cRequest, String token,
                                   BankCommerceEntity bankCommerceEntity) throws IOException {


            BankServiceEntity bankServiceEntity = bankServiceService.getUrlByIdPaymentMethodAndIdBank("p2c", 5L);
            if(bankServiceEntity == null) {
                return null;
            }
            String url = bankServiceEntity.getUrl();

            GsonBuilder b = new GsonBuilder();
            Gson gson = b.setPrettyPrinting().create();
            ObjectMapper objectMapper = new ObjectMapper();

            String formatPattern = "MM-dd-yyyy";
            LocalDate currentDate = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(formatPattern);
            String formattedDate = currentDate.format(formatter);

            String typeTransaction = "";

            if(p2cRequest.getTypeTransaction() != null) {
                typeTransaction = p2cRequest.getTypeTransaction();
            } else {
                typeTransaction = "PM";
            }


            LinkedHashMap<String, Object> p2cBodyRequest = new LinkedHashMap<>();
            p2cBodyRequest.put("rif", p2cRequest.getRif());
            p2cBodyRequest.put("referencia", p2cRequest.getReferenceNumber());
            p2cBodyRequest.put("identificadorPersona", p2cRequest.getPayerDocument());
            p2cBodyRequest.put("telefonoDebito",p2cRequest.getDebitPhone());
            p2cBodyRequest.put("factura", "null");
            p2cBodyRequest.put("tipoTrx", typeTransaction);
            p2cBodyRequest.put("montoTransaccion",p2cRequest.getTransactionAmount());
            /*p2cBodyRequest.put("fecha", formattedDate);*/

            String requestBody = objectMapper.writeValueAsString(p2cBodyRequest);
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(requestBody, mediaType);

            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.connectTimeout(10, TimeUnit.SECONDS);
            builder.readTimeout(30, TimeUnit.SECONDS);
            builder.writeTimeout(30, TimeUnit.SECONDS);

            OkHttpClient client = builder.build();

            Request request = new Request.Builder()
                    .url(url)
                    .addHeader("Authorization", "Bearer ".concat(token))
                    .addHeader("Content-Type", "application/json").post(body)
                    .build();

            okhttp3.Response response1 = client.newCall(request).execute();
            ResponseBody responseBody = response1.body();
            String responseBodyString = responseBody.string();
            Type type = new com.google.gson.reflect.TypeToken<LinkedHashMap<String, Object>>(){}.getType();
            LinkedHashMap<String, Object> responseBodyParsed = gson.fromJson(responseBodyString, type);
            ResponseP2C linkedHashMapResponse = (objectMapper.convertValue(responseBodyParsed, ResponseP2C.class));

            response1.close();

            return linkedHashMapResponse;

    }

    public Boolean validateBankToken(BankCommerceEntity bankCommerceEntity) {
        OffsetDateTime currentDate = OffsetDateTime.now(ZoneId.of("America/Caracas"));
        OffsetDateTime expireDate = bankCommerceEntity.getTokenExpireDate().withOffsetSameInstant(ZoneOffset.of("-04:00"));
        if(currentDate.isBefore(expireDate)) {
            return true;
        } else {
            return false;
        }

    }

    private Boolean validateIfPaymentAlreadyExist(P2cRequest p2cRequest) {
      Long paymentMethod;

      if(p2cRequest.getTypeTransaction() != null) {
          paymentMethod = 7L;
      } else {
          paymentMethod = 2L;
      }
      BankTransactionEntity bankTransactionEntity = bankTransactionRepository.validateIfPaymentAlreadyExist(
                                                    Long.valueOf(p2cRequest.getReferenceNumber()),
                                                    p2cRequest.getPayerDocument(), p2cRequest.getDebitPhone(), paymentMethod);
      if(bankTransactionEntity == null) {
          return true;
      } else {
          return false;
      }
    }
    private BankTransactionEntity validateIfExternalPaymentAlreadyExist(P2cRequest p2cRequest) {
        Long paymentMethod;
        List<BankTransactionEntity>  bankTransactionEntity = null;

        if(p2cRequest.getTypeTransaction().equals("TRF")) {
            paymentMethod = 7L;
            Float amount = Float.valueOf(p2cRequest.getTransactionAmount().concat("00"));
            String referenceNumberFixed = p2cRequest.getReferenceNumber().replaceAll("^0+", "");
             bankTransactionEntity = bankTransactionRepository.
                    validateIfExternalPaymentAlreadyExistTransfer
                            (referenceNumberFixed, amount, p2cRequest.getBankPayment(),paymentMethod);
        } else {
            paymentMethod = 2L;
            Float amount = Float.parseFloat(p2cRequest.getTransactionAmount().concat("00"));
            String referenceNumberFixed = p2cRequest.getReferenceNumber().replaceAll("^0+", "");
             bankTransactionEntity = bankTransactionRepository.
                    validateIfExternalPaymentAlreadyExistP2C
                            (referenceNumberFixed, p2cRequest.getDebitPhone(), p2cRequest.getBankPayment(),
                                    amount, paymentMethod);
        }

        if(bankTransactionEntity.isEmpty()) {
            return null;
        } else {
            return bankTransactionEntity.get(0);
        }
    }

    public void setStatusPaymentLink(P2cRequest p2cRequest) {
        if(p2cRequest.getPaymentChannel() == 2L) {
            BankTransactionEntity bankTransactionEntity = bankTransactionRepository.
                    findByIdBankTransaction(p2cRequest.getIdBankTransaction());

            bankTransactionEntity.setTransactionStatusEntity(new TransactionStatusEntity(2L));
            bankTransactionEntity.setUpdateDate(OffsetDateTime.now(ZoneId.of("America/Caracas")));
            bankTransactionRepository.save(bankTransactionEntity);
        }
    }

    private String convertKeyToBase64(String consumerKey, String consumerSecret) {
        String requestKey = consumerKey.concat(":").concat(consumerSecret);
        byte[] bytesEncoded = Base64.encodeBase64(requestKey.getBytes(), false);
        return new String(bytesEncoded);
    }
}
