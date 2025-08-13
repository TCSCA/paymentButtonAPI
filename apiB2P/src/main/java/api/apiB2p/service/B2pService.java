package api.apiB2p.service;

import api.apiB2p.entity.*;
import api.apiB2p.repository.BankRepository;
import api.apiB2p.repository.BankTransactionRepository;
import api.apiB2p.repository.CommerceRepository;
import api.apiB2p.request.B2pRequest;
import api.apiB2p.response.B2pBankResponse;
import api.apiB2p.util.LogTo;
import api.apiB2p.util.ManageLog;
import api.apiB2p.util.Response;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import okhttp3.*;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.LinkedHashMap;
import java.util.concurrent.TimeUnit;

@Service
public class B2pService {

    private final BankServiceService bankServiceService;

    private final MessageService messageService;

    private final BankCommerceService bankCommerceService;

    private final BankRepository bankRepository;

    private final CommerceRepository commerceRepository;

    private final BankTransactionRepository bankTransactionRepository;

    public B2pService(BankServiceService bankServiceService, MessageService messageService,
                      BankCommerceService bankCommerceService,
                      BankRepository bankRepository,
                      CommerceRepository commerceRepository,
                      BankTransactionRepository bankTransactionRepository) {
        this.bankServiceService = bankServiceService;
        this.messageService = messageService;
        this.bankCommerceService = bankCommerceService;
        this.bankRepository = bankRepository;
        this.commerceRepository = commerceRepository;
        this.bankTransactionRepository = bankTransactionRepository;
    }

    public Response validateB2pRequestData(B2pRequest b2pRequest, ManageLog manageLog, LogTo logTo,
                                           HttpServletRequest httpServletRequest) {
        return new Response("SUCCESS", "VALID");
    }

    public Response validateB2pPayment(B2pRequest b2pRequest, HttpServletResponse httpServletResponse,
                                       BankCommerceEntity bankCommerceEntity) throws IOException {

        B2pBankResponse responseB2p;

        if(bankCommerceEntity.getToken()== null) {
            String bankToken = getBankToken(bankCommerceEntity.getConsumerKey(), bankCommerceEntity.getConsumerSecret(), bankCommerceEntity);
            if(bankToken == null) {
                httpServletResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                return new Response("ERROR", "Error en metodo token");
            }

            responseB2p = callToBank(b2pRequest, bankToken, bankCommerceEntity);

            if(responseB2p == null) {
                httpServletResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                return new Response("ERROR", "Error en metodo de pago");
            }

            if (saveBankTransaction(b2pRequest, responseB2p)) {
                String responseMsg = messageService.returnResponseError(responseB2p.getCodigoError().toString(), 5L);
                if(responseB2p.getCodigoError() == 0) {
                    return new Response("SUCCESS", responseMsg);
                } else {
                    LinkedHashMap<String,Object> responseBank = new LinkedHashMap<>();
                    responseBank.put("code",responseB2p.getCodigoError());
                    responseBank.put("message",responseB2p.getMessage());
                    Response response = new Response("ERROR", "mensaje sistema");
                    response.setProperties(responseBank);
                    return response;
                }

            } else {
                return new Response("ERROR", "Error en transaccion B2P");
            }

        } else {
            if(!validateBankToken(bankCommerceEntity)) {
                String newBankToken = getBankToken(bankCommerceEntity.getConsumerKey(), bankCommerceEntity.getConsumerSecret(), bankCommerceEntity);
                if(newBankToken == null) {
                    httpServletResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    return new Response("ERROR", "Error en metodo token");
                }
                responseB2p = callToBank(b2pRequest, newBankToken, bankCommerceEntity);
                if(responseB2p == null) {
                    httpServletResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    return new Response("ERROR", "Error en metodo de pago");
                }
                if (saveBankTransaction(b2pRequest, responseB2p)) {
                    String responseMsg = messageService.returnResponseError(responseB2p.getCodigoError().toString(), 5L);
                    if(responseB2p.getCodigoError() == 0) {
                        return new Response("SUCCESS", responseMsg);
                    } else {
                        LinkedHashMap<String,Object> responseBank = new LinkedHashMap<>();
                        responseBank.put("code",responseB2p.getCodigoError());
                        responseBank.put("message",responseMsg);
                        Response response = new Response("ERROR", "mensaje sistema");
                        response.setProperties(responseBank);
                        return response;
                    }

                } else {
                    return new Response("ERROR", "Error en transaccion B2P");
                }

            } else {
                responseB2p = callToBank(b2pRequest, bankCommerceEntity.getToken(), bankCommerceEntity);
                if(responseB2p == null) {
                    httpServletResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    return new Response("ERROR", "Error en metodo de pago");
                }
                if (saveBankTransaction(b2pRequest, responseB2p)) {
                    String responseMsg = messageService.returnResponseError(responseB2p.getCodigoError().toString(), 5L);
                    if(responseB2p.getCodigoError() == 0) {
                        return new Response("SUCCESS", responseMsg);
                    } else {
                        LinkedHashMap<String,Object> responseBank = new LinkedHashMap<>();
                        responseBank.put("code",responseB2p.getCodigoError());
                        responseBank.put("message",responseMsg);
                        Response response = new Response("ERROR", "mensaje sistema");
                        response.setProperties(responseBank);
                        return response;
                    }
                } else {
                    return new Response("ERROR", "Error en transaccion B2P");
                }
            }
        }

    }

    private String getBankToken(final String consumerKey, final String consumerSecret, BankCommerceEntity bankCommerceEntity) {

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

        } catch ( Exception e) {
            return null;
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

    private B2pBankResponse callToBank(B2pRequest b2pRequest, String token, BankCommerceEntity bankCommerceEntity) throws IOException {

            BankServiceEntity bankServiceEntity = bankServiceService.getUrlByIdPaymentMethodAndIdBank("b2p", 5L);
            if(bankServiceEntity == null) {
                return null;
            }
            String url = bankServiceEntity.getUrl();

            GsonBuilder b = new GsonBuilder();
            Gson gson = b.setPrettyPrinting().create();
            ObjectMapper objectMapper = new ObjectMapper();
            b2pRequest.setPayingBank("0114");

            String requestBody = "{\"montoTransaccion\":\"" + b2pRequest.getTransactionAmount() +"\""+
                    ", \"bancoCredito\":\"" + b2pRequest.getCreditBank() + "\"" +
                    ", \"canalVirtual\":\"" + "1" + "\"" +
                    ", \"oficina\":\"" + "800" + "\"" +
                    ", \"identificadorPersona\":\"" + b2pRequest.getIdentificationDocument() + "\"" +
                    ", \"telefonoCredito\":\"" + b2pRequest.getCreditPhone() + "\"" +
                    ", \"vendedor\":\"" + "2525" + "\"" +
                    ", \"concepto\":\"" + b2pRequest.getConcept() + "\"" +
                    ", \"direccionInternet\":\"" + b2pRequest.getIpDirection() + "\"" +
                    ", \"bancoPagador\":\"" + b2pRequest.getPayingBank() + "\"" +
                    ", \"cajaTerminal\":\"" + "1" + "\"" +
                    ", \"codigoMoneda\":\"" + "928" + "\"" +
                    ", \"nombreComercio\":\"" + bankCommerceEntity.getCommerceEntity().getCommerceName() + "\"" +
                    ", \"rif\":\"" + b2pRequest.getRif() + "\"" +
                    ", \"sucursal\":\"" + "1" + "\"" +
                    ", \"telefonoDebito\":\"" + bankCommerceEntity.getCommercePhone() + "\"" +
                    ", \"tipoTerminal\":\"" + "WEB" + "\"" +
                    ", \"factura\":\"" + "0" + "".concat("\"").concat("}");



            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(requestBody, mediaType);

            Request request = new Request.Builder()
                    .url(url)
                    .addHeader("Authorization", "Bearer ".concat(token))
                    .addHeader("Content-Type", "application/json").post(body)
                    .build();

            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.connectTimeout(10, TimeUnit.SECONDS);
            builder.readTimeout(30, TimeUnit.SECONDS);
            builder.writeTimeout(30, TimeUnit.SECONDS);

            OkHttpClient client = builder.build();


            okhttp3.Response response1 = client.newCall(request).execute();
            ResponseBody responseBody = response1.body();
            String responseBodyString = responseBody.string();

            Type type = new com.google.gson.reflect.TypeToken<LinkedHashMap<String, Object>>(){}.getType();
            LinkedHashMap<String, Object> responseBodyParsed = gson.fromJson(responseBodyString, type);
            B2pBankResponse linkedHashMapResponse = (objectMapper.convertValue(responseBodyParsed, B2pBankResponse.class));

            response1.close();

            return linkedHashMapResponse;

    }

    private Boolean saveBankTransaction(B2pRequest b2pRequest, B2pBankResponse b2pBankResponse) {
        try {
            BankTransactionEntity bankTransactionEntity = new BankTransactionEntity();

            BankEntity bankEntity = bankRepository.findByBankCodeAndStatusTrue(b2pRequest.getPayingBank());
            bankTransactionEntity.setBankEntity(bankEntity);
            bankTransactionEntity.setTransactionCode(b2pBankResponse.getCodigoError().toString());
            bankTransactionEntity.setReferenceNumber(b2pBankResponse.getSecuencial());
            bankTransactionEntity.setAmount(new BigDecimal(b2pRequest.getTransactionAmount()));
            CommerceEntity commerceEntity = commerceRepository.findByCommerceDocument(b2pRequest.getRif());
            bankTransactionEntity.setCommerceEntity(commerceEntity);
            bankTransactionEntity.setPaymentChannel(new PaymentChannel(b2pRequest.getPaymentChannel()));
            bankTransactionEntity.setCurrencyEntity(new CurrencyEntity(2L));
            bankTransactionEntity.setPaymentMethodEntity(new PaymentMethodEntity(5L));
            if(b2pBankResponse.getCodigoError() == 0) {
                bankTransactionEntity.setTransactionStatusEntity(new TransactionStatusEntity(1L));
            } else {
                bankTransactionEntity.setTransactionStatusEntity(new TransactionStatusEntity(2L));

            }


            bankTransactionEntity.setRegisterDate(OffsetDateTime.now(ZoneId.of("America/Caracas")));
            bankTransactionEntity.setUpdateDate(OffsetDateTime.now(ZoneId.of("America/Caracas")));

            bankTransactionRepository.save(bankTransactionEntity);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private String convertKeyToBase64(final String consumerKey, final String consumerSecret) {
        String requestKey = consumerKey.concat(":").concat(consumerSecret);
        byte[] bytesEncoded = Base64.encodeBase64(requestKey.getBytes(), false);
        return new String(bytesEncoded);
    }
}
