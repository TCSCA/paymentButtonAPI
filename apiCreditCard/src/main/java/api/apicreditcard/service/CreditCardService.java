package api.apicreditcard.service;

import api.apicreditcard.entity.*;
import api.apicreditcard.request.CreditCardRequest;
import api.logsClass.LogsClass;
import api.logsClass.ManageLogs;
import api.apicreditcard.repository.BankRepository;
import api.apicreditcard.repository.BankTransactionRepository;
import api.apicreditcard.repository.CommerceRepository;
import api.apicreditcard.response.CreditCardBankResponse;
import api.apicreditcard.util.Response;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import com.google.gson.reflect.TypeToken;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import okhttp3.*;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.stereotype.Service;


import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.lang.reflect.Type;
import java.security.cert.X509Certificate;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.LinkedHashMap;


@Service
public class CreditCardService {

    private final BankServiceService bankServiceService;

    private final ManageLogs manageLog;

    private final BankCommerceService bankCommerceService;

    private final CommerceRepository commerceRepository;

    private final BankRepository bankRepository;

    private final BankTransactionRepository bankTransactionRepository;

    private final EmailService emailService;

    private final MessageService messageService;

    public CreditCardService(BankServiceService bankServiceService, ManageLogs manageLog, BankCommerceService bankCommerceService, CommerceRepository commerceRepository, BankRepository bankRepository, BankTransactionRepository bankTransactionRepository, EmailService emailService, MessageService messageService) {
        this.bankServiceService = bankServiceService;
        this.manageLog = manageLog;
        this.bankCommerceService = bankCommerceService;
        this.commerceRepository = commerceRepository;
        this.bankRepository = bankRepository;
        this.bankTransactionRepository = bankTransactionRepository;
        this.emailService = emailService;
        this.messageService = messageService;
    }

    public String getBankAuthorize(String consumerKey, String consumerSecret) throws IOException {
        try {
            GsonBuilder b = new GsonBuilder();
            Gson gson = b.setPrettyPrinting().create();
            ObjectMapper objectMapper = new ObjectMapper();

            String requestKey = convertKeyToBase64(consumerKey, consumerSecret);
            String basicAuth = "Basic ".concat(requestKey);

            BankServiceEntity bankServiceEntity = bankServiceService.getUrlByIdPaymentMethodAndIdBank("ccr-auth", 5L);
            String url = bankServiceEntity.getUrl();

            TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(X509Certificate[] chain, String authType) {
                        }

                        @Override
                        public void checkServerTrusted(X509Certificate[] chain, String authType) {
                        }

                        @Override
                        public X509Certificate[] getAcceptedIssuers() {
                            return new X509Certificate[]{};
                        }
                    }
            };

            SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            OkHttpClient client = new OkHttpClient.Builder()
                    .sslSocketFactory(sslContext.getSocketFactory(), (X509TrustManager) trustAllCerts[0])
                    .hostnameVerifier((hostname, session) -> true)
                    .build();

            RequestBody requestBody = new FormBody.Builder()
                    .add("grant_type", "client_credentials")
                    .add("client_id", consumerKey)
                    .add("client_secret", consumerSecret)
                    .build();

            Request request = new Request.Builder()
                    .url(url)
                    .header("Authorization", basicAuth)
                    .post(requestBody)
                    .build();

            okhttp3.Response response1 = client.newCall(request).execute();
            if (!response1.isSuccessful()) {
                throw new IOException("Unexpected code " + response1);
            }

            String responseBodyString;
            try (ResponseBody responseBody = response1.body()) {
                if (responseBody == null) {
                    throw new IOException("Response body is null");
                }
                responseBodyString = responseBody.string();
            }

            Type type = new com.google.gson.reflect.TypeToken<LinkedHashMap<String, Object>>() {}.getType();
            LinkedHashMap<String, Object> responseBodyParsed = gson.fromJson(responseBodyString, type);
            LinkedHashMap linkedHashMapResponse = objectMapper.convertValue(responseBodyParsed, LinkedHashMap.class);

            String accessToken = (String) linkedHashMapResponse.get("access_token");


            return accessToken;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private String convertKeyToBase64(String consumerKey, String consumerSecret) {
        String requestKey = consumerKey.concat(":").concat(consumerSecret);
        byte[] bytesEncoded = Base64.encodeBase64(requestKey.getBytes(), false);
        return new String(bytesEncoded);
    }

    public Response validateRequestCreditCard(CreditCardRequest creditCardRequest, LogsClass logTo, HttpServletRequest httpServletRequest) {

        Response response;
        if (!(creditCardRequest.getRif().startsWith("J") || (creditCardRequest.getRif().startsWith("G")))) {
            response = new Response("ERROR", "Formato de Rif Invalido: "
                    + creditCardRequest.getRif() + ". Formato Válido: J|GXXXXXXXXX");
            manageLog.errorLogger(logTo, httpServletRequest, "Error Rif invalid",
                    "user", false);

            return response;
        }

        if (!(creditCardRequest.getCreditCard().getHolderId().startsWith("V") || (creditCardRequest.getCreditCard().getHolderId().startsWith("E")) || (creditCardRequest.getCreditCard().getHolderId().startsWith("J")))) {
            response = new Response("ERROR", "Formato de documentId invalido " + creditCardRequest.getCreditCard().getHolderId()
                    + ". Formato Válido: VXXXXXXXX");
            manageLog.errorLogger(logTo, httpServletRequest, "Error documentId invalid",
                    "user", false);

            return response;
        }
        response = new Response("SUCCESS", "VALID");
        return response;
    }

    public Response validateCreditCardPayment (CreditCardRequest creditCardRequest, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, BankCommerceEntity bankCommerceEntity, LogsClass logTo) throws IOException, MessagingException {

        CreditCardBankResponse creditCardRequestPayment;

        String bankToken = getBankAuthorize(bankCommerceEntity.getConsumerKey(), bankCommerceEntity.getConsumerSecret());

        if (bankCommerceEntity.getToken() == null) {
            if (bankToken == null) {
                manageLog.errorLogger(logTo, httpServletRequest, "Error bankToken null",
                        "user", false);
                return new Response("ERROR", "Error en metodo token");
            }
            bankCommerceService.saveBankCommerceToken(bankToken, bankCommerceEntity.getIdBankCommerce());
            creditCardRequestPayment = callToBankCredit(creditCardRequest,bankToken,httpServletRequest);
            if (creditCardRequestPayment == null){
                manageLog.errorLogger(logTo, httpServletRequest, "Error creditCardRequestPayment null",
                        "user", false);
                return new Response("ERROR", "Error en metodo de pago");
            }

            if (saveBankTransactionCredit(creditCardRequest, creditCardRequestPayment)) {
                Long errorCode = (creditCardRequestPayment.getCause() != null &&
                        !creditCardRequestPayment.getCause().isEmpty() &&
                        creditCardRequestPayment.getCause().get(0).matches("\\d+"))
                        ? Long.valueOf(creditCardRequestPayment.getCause().get(0))
                        : null;

                if (errorCode == null){
                    LinkedHashMap<String,Object> responseBank = new LinkedHashMap<>();
                    responseBank.put("code",creditCardRequestPayment.getCode());
                    responseBank.put("message"," En estos momentos estamos presentando problemas con la plataforma.  Por favor Intente mas tarde. "+creditCardRequestPayment.getCause());                    Response response = new Response("ERROR", "mensaje sistema");
                    response.setProperties(responseBank);
                    manageLog.errorLogger(logTo,httpServletRequest,
                            "ERROR: " + creditCardRequestPayment,
                            "user",false);
                    return response;
                }

                String messageBank = messageService.returnResponseError(errorCode, 99L);



                if(errorCode == 0L) {
                    emailService.sendPaymentReceiptCredit(creditCardRequest.getEmail(),"Intelipay-Recibo de pago-Tarjeta de Crédito",
                            httpServletRequest, creditCardRequest.getCreditCard(), creditCardRequest);
                    manageLog.infoLogger(logTo,httpServletRequest,"Susccesfully","user",false);
                    return new Response("SUCCESS", messageBank);
                }
                else {
                    LinkedHashMap<String,Object> responseBank = new LinkedHashMap<>();
                    responseBank.put("code",errorCode);
                    responseBank.put("message",messageBank);
                    Response response = new Response("ERROR", "mensaje sistema");
                    response.setProperties(responseBank);
                    manageLog.errorLogger(logTo,httpServletRequest,
                            "ERROR: " + creditCardRequestPayment,
                            "user",false);
                    return response;
                }
            }

        }else {
            if(!validateBankToken(bankCommerceEntity)) {
                String newBankToken = getBankAuthorize(bankCommerceEntity.getConsumerKey(), bankCommerceEntity.getConsumerSecret());
                if(newBankToken == null) {
                    manageLog.errorLogger(logTo, httpServletRequest, "Error en metodo token",
                            "user", false);
                    return new Response("ERROR", "Error en metodo token");
                }
                bankCommerceService.saveBankCommerceToken(newBankToken, bankCommerceEntity.getIdBankCommerce());
                creditCardRequestPayment = callToBankCredit(creditCardRequest, newBankToken, httpServletRequest);
                if(creditCardRequestPayment == null) {
                    manageLog.errorLogger(logTo, httpServletRequest, "Error  null",
                            "user", false);
                    return new Response("ERROR", "Error en metodo de pago");
                }
                if (saveBankTransactionCredit(creditCardRequest, creditCardRequestPayment)) {
                    Long errorCode = (creditCardRequestPayment.getCause() != null &&
                            !creditCardRequestPayment.getCause().isEmpty() &&
                            creditCardRequestPayment.getCause().get(0).matches("\\d+"))
                            ? Long.valueOf(creditCardRequestPayment.getCause().get(0))
                            : null;

                    if (errorCode == null){
                        LinkedHashMap<String,Object> responseBank = new LinkedHashMap<>();
                        responseBank.put("code",creditCardRequestPayment.getCode());
                        responseBank.put("message"," En estos momentos estamos presentando problemas con la plataforma.  Por favor Intente mas tarde. "+creditCardRequestPayment.getCause());
                        Response response = new Response("ERROR", "mensaje sistema");
                        response.setProperties(responseBank);
                        manageLog.errorLogger(logTo,httpServletRequest,
                                "ERROR: " + creditCardRequestPayment,
                                "user",false);
                        return response;
                    }
                    String messageBank = messageService.returnResponseError(errorCode, 99L);

                    if(errorCode == 0) {
                        emailService.sendPaymentReceiptCredit(creditCardRequest.getEmail(),"Intelipay-Recibo de pago-Tarjeta de Crédito",
                                httpServletRequest, creditCardRequest.getCreditCard(), creditCardRequest);
                        manageLog.infoLogger(logTo,httpServletRequest,"Susccesfully","user",false);
                        return new Response("SUCCESS", messageBank);
                    } else  {
                        LinkedHashMap<String,Object> responseBank = new LinkedHashMap<>();
                        responseBank.put("code",errorCode);
                        responseBank.put("message",messageBank);
                        Response response = new Response("ERROR", "mensaje sistema");
                        response.setProperties(responseBank);
                        manageLog.errorLogger(logTo,httpServletRequest,
                                "ERROR: " + creditCardRequestPayment,
                                "user",false);
                        return response;
                    }
                }
            } else {
                creditCardRequestPayment = callToBankCredit(creditCardRequest,bankToken,httpServletRequest);
                if(creditCardRequestPayment == null) {
                    manageLog.errorLogger(logTo, httpServletRequest, "Error en el metodo del banco null",
                            "user", false);
                    return new Response("ERROR", "Error en metodo de pago");
                }
                if (saveBankTransactionCredit(creditCardRequest, creditCardRequestPayment)) {
                    Long errorCode = (creditCardRequestPayment.getCause() != null &&
                            !creditCardRequestPayment.getCause().isEmpty() &&
                            creditCardRequestPayment.getCause().get(0).matches("\\d+"))
                            ? Long.valueOf(creditCardRequestPayment.getCause().get(0))
                            : null;


                    if (errorCode == null){
                        LinkedHashMap<String,Object> responseBank = new LinkedHashMap<>();
                        responseBank.put("code",creditCardRequestPayment.getCode());
                        responseBank.put("message"," En estos momentos estamos presentando problemas con la plataforma.  Por favor Intente mas tarde. "+creditCardRequestPayment.getCause());
                        Response response = new Response("ERROR", "mensaje sistema");
                        response.setProperties(responseBank);
                        manageLog.errorLogger(logTo,httpServletRequest,
                                "ERROR: " + creditCardRequestPayment,
                                "user",false);
                        return response;
                    }

                    String messageBank = messageService.returnResponseError(errorCode, 99L);

                    if(errorCode == 0L) {
                        emailService.sendPaymentReceiptCredit(creditCardRequest.getEmail(),"Intelipay-Recibo de pago-Tarjeta de Crédito",
                                httpServletRequest, creditCardRequest.getCreditCard(), creditCardRequest);
                        manageLog.infoLogger(logTo,httpServletRequest,"Susccesfully","user",false);
                        return new Response("SUCCESS", messageBank);
                    }
                    else {
                        LinkedHashMap<String,Object> responseBank = new LinkedHashMap<>();
                        responseBank.put("code",errorCode);
                        responseBank.put("message",messageBank);
                        Response response = new Response("ERROR", "mensaje sistema");
                        response.setProperties(responseBank);
                        manageLog.errorLogger(logTo,httpServletRequest,
                                "ERROR: " + creditCardRequestPayment,
                                "user",false);
                        return response;
                    }


                }
            }
        }
        return new Response("ERROR", "Informacion bancaria no existe");
    }

    private CreditCardBankResponse callToBankCredit(CreditCardRequest creditCardRequest, String token,
                                                    HttpServletRequest httpServletRequest) throws IOException {

        try {
            BankServiceEntity bankServiceEntity = bankServiceService.getUrlByIdPaymentMethodAndIdBank("ccr-payment", 5L);
            if(bankServiceEntity == null) {
                return null;
            }

            String url = bankServiceEntity.getUrl();

            GsonBuilder b = new GsonBuilder();
            Gson gson = b.setPrettyPrinting().create();
            ObjectMapper objectMapper = new ObjectMapper();


            LinkedHashMap<String, Object> paymentDetails = new LinkedHashMap<>();

            paymentDetails.put("currency",creditCardRequest.getCurrency());
            paymentDetails.put("amount", creditCardRequest.getAmount());
            paymentDetails.put("country", "VE");
            paymentDetails.put("reason",creditCardRequest.getReason());

            // Crear el LinkedHashMap para la tarjeta de crédito
            LinkedHashMap<String, Object> creditCardDetails = new LinkedHashMap<>();
            creditCardDetails.put("holder_name", creditCardRequest.getCreditCard().getHolderName());
            creditCardDetails.put("holder_id", creditCardRequest.getCreditCard().getHolderId());
            creditCardDetails.put("holder_id_doc", creditCardRequest.getCreditCard().getHolderIdDoc());
            creditCardDetails.put("card_number", creditCardRequest.getCreditCard().getCardNumber());
            creditCardDetails.put("cvc", creditCardRequest.getCreditCard().getCvc());
            creditCardDetails.put("expiration_month",creditCardRequest.getCreditCard().getExpirationMonth());
            creditCardDetails.put("expiration_year", creditCardRequest.getCreditCard().getExpirationYear());
            creditCardDetails.put("card_type", creditCardRequest.getCreditCard().getCardType());


            // Añadir el LinkedHashMap de tarjeta de crédito al LinkedHashMap principal
            paymentDetails.put("credit_card", creditCardDetails);

            String requestBody = objectMapper.writeValueAsString(paymentDetails);

            TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(X509Certificate[] chain, String authType) {
                        }

                        @Override
                        public void checkServerTrusted(X509Certificate[] chain, String authType) {
                        }

                        @Override
                        public X509Certificate[] getAcceptedIssuers() {
                            return new X509Certificate[]{};
                        }
                    }
            };

            SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            OkHttpClient client = new OkHttpClient.Builder()
                    .sslSocketFactory(sslContext.getSocketFactory(), (X509TrustManager) trustAllCerts[0])
                    .hostnameVerifier((hostname, session) -> true)
                    .build();

            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(requestBody, mediaType);

            Request request = new Request.Builder()
                    .url(url)
                    .addHeader("Authorization", "Bearer ".concat(token))
                    .addHeader("Content-Type", "application/json").post(body)
                    .build();

            okhttp3.Response response1 = client.newCall(request).execute();
            ResponseBody responseBody = response1.body();
            String responseBodyString = responseBody.string();
            Type type = new TypeToken<LinkedHashMap<String, Object>>(){}.getType();
            LinkedHashMap<String, Object> linkedHashMap1 = gson.fromJson(responseBodyString, type);
            if ("APPROVED".equals(linkedHashMap1.get("state"))) {
                CreditCardBankResponse bankResponse = new CreditCardBankResponse();
                bankResponse.setCode(0L);
                bankResponse.setMessage("SUCCESS");
                bankResponse.setState("APPROVED");
                bankResponse.setCause(Arrays.asList("0", "Transacción Exitosa"));
                return bankResponse;
            }

            CreditCardBankResponse linkedHashMapResponse = (objectMapper.convertValue(linkedHashMap1, CreditCardBankResponse.class));
            return linkedHashMapResponse;

        } catch (IOException e) {
            return null;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Boolean saveBankTransactionCredit(CreditCardRequest creditCardRequest, CreditCardBankResponse creditCardBankResponse) {
        try {
            //TODO:revisar esto el mensaje enviado debe ser mapeado correctamente
            BankTransactionEntity bankTransactionEntity = new BankTransactionEntity();

            BankEntity bankEntity = bankRepository.findByBankCodeAndStatusTrue(creditCardRequest.getBankPayment());
            bankTransactionEntity.setBankEntity(bankEntity);
            bankTransactionEntity.setTransactionCode("No aplica");
            bankTransactionEntity.setReferenceNumber(null);
            bankTransactionEntity.setAmount(creditCardRequest.getAmount());
            CommerceEntity commerceEntity = commerceRepository.findByCommerceDocument(creditCardRequest.getRif());
            bankTransactionEntity.setCommerceEntity(commerceEntity);
            bankTransactionEntity.setPaymentChannel(new PaymentChannel(creditCardRequest.getPaymentChannel()));
            bankTransactionEntity.setSenderIdentificationDocument(creditCardRequest.getCreditCard().getHolderId());
            bankTransactionEntity.setRegisterBy(creditCardRequest.getCreditCard().getHolderId());
            bankTransactionEntity.setCurrencyEntity(new CurrencyEntity(2L));
            bankTransactionEntity.setPaymentMethodEntity(new PaymentMethodEntity(6L));
            if(creditCardBankResponse.getCode() == 202 ||creditCardBankResponse.getCode() == 422) {
                bankTransactionEntity.setTransactionStatusEntity(new TransactionStatusEntity(2L));
            } else {
                bankTransactionEntity.setTransactionStatusEntity(new TransactionStatusEntity(1L));
            }
            bankTransactionEntity.setRegisterDate(OffsetDateTime.now(ZoneId.of("America/Caracas")));
            bankTransactionEntity.setUpdateDate(OffsetDateTime.now(ZoneId.of("America/Caracas")));

            bankTransactionRepository.save(bankTransactionEntity);
            return true;
        } catch (Exception e) {
            return false;
        }
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

}
