package api.apic2p.service;

import api.apic2p.entity.*;
import api.apic2p.repository.*;
import api.apic2p.request.C2pRequest;
import api.apic2p.response.*;
import api.apic2p.util.LogTo;
import api.apic2p.util.ManageLog;
import api.apic2p.util.Response;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
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
public class C2pService {

    private final BankServiceService bankServiceService;

    private final BankCommerceService bankCommerceService;

    private final BankCommerceRepository bankCommerceRepository;

    private final CommerceRepository commerceRepository;

    private final BankRepository bankRepository;

    private final BankTransactionRepository bankTransactionRepository;

    private final EmailService emailService;

    private final MessageService messageService;

    public C2pService(BankServiceService bankServiceService, BankCommerceService bankCommerceService,
                      BankCommerceRepository bankCommerceRepository, CommerceRepository commerceRepository,
                      BankRepository bankRepository, BankTransactionRepository bankTransactionRepository,
                      EmailService emailService, MessageService messageService) {
        this.bankServiceService = bankServiceService;
        this.bankCommerceService = bankCommerceService;
        this.bankCommerceRepository = bankCommerceRepository;
        this.commerceRepository = commerceRepository;
        this.bankRepository = bankRepository;
        this.bankTransactionRepository = bankTransactionRepository;
        this.emailService = emailService;
        this.messageService = messageService;
    }

    public Response validateC2pRequestData(C2pRequest c2pRequest, ManageLog manageLog, LogTo logTo,
                                           HttpServletRequest httpServletRequest) {

            Response response;

        if (!(c2pRequest.getRif().startsWith("J") || (c2pRequest.getRif().startsWith("G")))) {
            response = new Response("ERROR", "Formato de Rif Invalido: "
                    + c2pRequest.getRif() + ". Formato Válido: J|GXXXXXXXXX");
            manageLog.infoLogger(logTo, httpServletRequest, response.toString(), false);

            return response;
        }

        if (!(c2pRequest.getIdentificationDocument().startsWith("V") || (c2pRequest.getIdentificationDocument().startsWith("E")) || (c2pRequest.getIdentificationDocument().startsWith("J")))) {
            response = new Response("ERROR", "Formato de documentId invalido " + c2pRequest.getIdentificationDocument()
                    + ". Formato Válido: VXXXXXXXX");
            manageLog.infoLogger(logTo, httpServletRequest, response.toString(), false);

            return response;
        }
        response = new Response("SUCCESS", "VALID");
        return response;
    }

    public Response validateBankPayment(C2pRequest c2pRequest,
                                        BankCommerceEntity bankCommerceEntity,final String apiKey) throws IOException, MessagingException {

        ResponseC2p responseC2p;

        if(bankCommerceEntity.getToken()== null) {
           String bankToken = getBankToken(bankCommerceEntity.getConsumerKey(), bankCommerceEntity.getConsumerSecret(), bankCommerceEntity);
            if(bankToken == null) {
                return new Response("ERROR", "Error en metodo token");
            }

            responseC2p = callToBank(c2pRequest, bankToken, bankCommerceEntity);
           if(responseC2p == null) {
               return new Response("ERROR", "Error en metodo de pago");
           }

            if (saveBankTransaction(c2pRequest, responseC2p)) {

                Long errorCode = responseC2p.getEnvelope().getBody().getRegistrarPagoC2PAPIResponse().getOut().getCodigoError();

                String messageBank = messageService.returnResponseError(errorCode, 5L);

                if(errorCode == 0) {
                    emailService.sendPaymentReceipt(c2pRequest.getEmail(),"Intelipay-Recibo-Recibo de pago-Pago Móvil C2P",
                            apiKey,c2pRequest,responseC2p);
                    return new Response("SUCCESS", messageBank);
                } else {

                    LinkedHashMap<String,Object> responseBank = new LinkedHashMap<>();
                    responseBank.put("code",responseC2p.getEnvelope().getBody().getRegistrarPagoC2PAPIResponse().getOut().getCodigoError());
                    responseBank.put("message",messageBank);
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
                responseC2p = callToBank(c2pRequest, newBankToken, bankCommerceEntity);
                if(responseC2p == null) {
                    return new Response("ERROR", "Error en metodo de pago");
                }

                if (saveBankTransaction(c2pRequest, responseC2p)) {

                    Long errorCode = responseC2p.getEnvelope().getBody().getRegistrarPagoC2PAPIResponse().getOut().getCodigoError();
                    String messageBank = messageService.returnResponseError(errorCode, 5L);

                    if(errorCode == 0) {
                        emailService.sendPaymentReceipt(c2pRequest.getEmail(),"Intelipay-Recibo-Recibo de pago-Pago Móvil C2P",
                                apiKey,c2pRequest,responseC2p);
                        return new Response("SUCCESS", messageBank);
                    } else {

                        LinkedHashMap<String,Object> responseBank = new LinkedHashMap<>();
                        responseBank.put("code",responseC2p.getEnvelope().getBody().getRegistrarPagoC2PAPIResponse().getOut().getCodigoError());
                        responseBank.put("message",messageBank);
                        Response response = new Response("ERROR", "mensaje sistema");
                        response.setProperties(responseBank);
                        return response;
                    }
                }
            } else {
                responseC2p = callToBank(c2pRequest, bankCommerceEntity.getToken(), bankCommerceEntity);
                if(responseC2p == null) {
                    return new Response("ERROR", "Error en metodo de pago");
                }

                if (saveBankTransaction(c2pRequest, responseC2p)) {
                    Long errorCode = responseC2p.getEnvelope().getBody().getRegistrarPagoC2PAPIResponse().getOut().getCodigoError();
                    String messageBank = messageService.returnResponseError(errorCode, 5L);

                    if(errorCode == 0) {
                        emailService.sendPaymentReceipt(c2pRequest.getEmail(),"Intelipay-Recibo-Recibo de pago-Pago Móvil C2P",
                                apiKey,c2pRequest,responseC2p);
                        return new Response("SUCCESS", messageBank);
                    } else {

                        LinkedHashMap<String,Object> responseBank = new LinkedHashMap<>();
                        responseBank.put("code",responseC2p.getEnvelope().getBody().getRegistrarPagoC2PAPIResponse().getOut().getCodigoError());
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

    private ResponseC2p callToBank(C2pRequest c2pRequest, String token, BankCommerceEntity bankCommerceEntity) throws IOException {


            BankServiceEntity bankServiceEntity = bankServiceService.getUrlByIdPaymentMethodAndIdBank("c2p", 5L);
            if(bankServiceEntity == null) {
                return null;
            }
            String url = bankServiceEntity.getUrl();

            GsonBuilder b = new GsonBuilder();
            Gson gson = b.setPrettyPrinting().create();
            ObjectMapper objectMapper = new ObjectMapper();

            //TODO:Cambiar a LinkedHashMap para enviar una peticion normal

            String requestBody = "{\"requestRegistrarPagoC2p\":".concat("{\"rif\":\"" + c2pRequest.getRif() +"\""+
                    ", \"telefonoCredito\":\"" + bankCommerceEntity.getCommercePhone() + "\"" +
                    ", \"nombreComercio\":\"" + bankCommerceEntity.getCommerceEntity().getCommerceName() + "\"" +
                    ", \"identificadorPersona\":\"" + c2pRequest.getIdentificationDocument() + "\"" +
                    ", \"otp\":\"" + c2pRequest.getOtp() + "\"" +
                    ", \"montoTransaccion\":\"" + c2pRequest.getTransactionAmount() + "\"" +
                    ", \"concepto\":\"" + c2pRequest.getConcept() + "\"" +
                    ", \"anulacion\":\"" + "N" + "\"" +
                    ", \"bancoCredito\":\"" + "0114" + "\"" +
                    ", \"cajaTerminal\":\"" + "1" + "\"" +
                    ", \"canalVirtual\":\"" + "1" + "\"" +
                    ", \"codigoMoneda\":\"" + "928" + "\"" +
                    ", \"direccionInternet\":\"" + "10.100.49.91" + "\"" +
                    ", \"identificadorExterno\":\"" + token + "\"" +
                    ", \"numeroReferencia\":\"" + "0" + "\"" +
                    ", \"sucursal\":\"" + "1" + "\"" +
                    ", \"telefonoPersona\":\"" + c2pRequest.getPhoneNumber() + "\"" +
                    ", \"tipoTerminal\":\"" + "SMS" + "\"" +
                    ", \"vendedor\":\"" + "1" + "\"" +
                    ", \"bancoPagador\":\"" + c2pRequest.getBankPayment() + "".concat("\"").concat("}").concat("}"));


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
            ResponseC2p linkedHashMapResponse = (objectMapper.convertValue(responseBodyParsed, ResponseC2p.class));

            return linkedHashMapResponse;
    }

    private Boolean saveBankTransaction(C2pRequest c2pRequest, ResponseC2p responseC2p) {
        try {

            if (c2pRequest.getPaymentChannel() != 2L) {
                //TODO:revisar esto el mensaje enviado debe ser mapeado correctamente
                BankTransactionEntity bankTransactionEntity = new BankTransactionEntity();

                BankEntity bankEntity = bankRepository.findByBankCodeAndStatusTrue(c2pRequest.getBankPayment());
                bankTransactionEntity.setBankEntity(bankEntity);
                bankTransactionEntity.setTransactionCode(String.valueOf(responseC2p.getEnvelope().getBody()
                        .getRegistrarPagoC2PAPIResponse().getOut()
                        .getCodigoError()));
                bankTransactionEntity.setReferenceNumber(responseC2p.getEnvelope().getBody()
                        .getRegistrarPagoC2PAPIResponse().getOut().getSecuencial());
                bankTransactionEntity.setAmount(new BigDecimal(c2pRequest.getTransactionAmount()));
                CommerceEntity commerceEntity = commerceRepository.findByCommerceDocument(c2pRequest.getRif());
                bankTransactionEntity.setCommerceEntity(commerceEntity);
                bankTransactionEntity.setPaymentChannel(new PaymentChannel(c2pRequest.getPaymentChannel()));
                bankTransactionEntity.setCurrencyEntity(new CurrencyEntity(2L));
                bankTransactionEntity.setPaymentMethodEntity(new PaymentMethodEntity(1L));
                bankTransactionEntity.setUpdateBy(c2pRequest.getIdentificationDocument());
                bankTransactionEntity.setRegisterBy(c2pRequest.getIdentificationDocument());
                if(responseC2p.getEnvelope().getBody().getRegistrarPagoC2PAPIResponse().getOut().getCodigoError() == 0) {
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
                        findByIdBankTransaction(c2pRequest.getIdBankTransaction());

                if(bankTransactionEntity == null){
                    return false;
                }

                BankEntity bankEntity = bankRepository.findByBankCodeAndStatusTrue(c2pRequest.getBankPayment());
                bankTransactionEntity.setBankEntity(bankEntity);

                bankTransactionEntity.setTransactionCode(String.valueOf(responseC2p.getEnvelope().getBody()
                        .getRegistrarPagoC2PAPIResponse().getOut()
                        .getCodigoError()));

                bankTransactionEntity.setReferenceNumber(responseC2p.getEnvelope().getBody()
                        .getRegistrarPagoC2PAPIResponse().getOut().getSecuencial());
                bankTransactionEntity.setAmount(new BigDecimal(c2pRequest.getTransactionAmount()));

                if(responseC2p.getEnvelope().getBody().getRegistrarPagoC2PAPIResponse().getOut().getCodigoError() == 0) {
                    bankTransactionEntity.setTransactionStatusEntity(new TransactionStatusEntity(1L));
                } else {
                    bankTransactionEntity.setTransactionStatusEntity(new TransactionStatusEntity(2L));
                }

                bankTransactionEntity.setUpdateDate(OffsetDateTime.now(ZoneId.of("America/Caracas")));

                bankTransactionRepository.save(bankTransactionEntity);
                return true;
            }

        } catch (Exception e) {
            return false;
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

    public Boolean validateBankToken(BankCommerceEntity bankCommerceEntity) {
        OffsetDateTime currentDate = OffsetDateTime.now(ZoneId.of("America/Caracas"));
        OffsetDateTime expireDate = bankCommerceEntity.getTokenExpireDate().withOffsetSameInstant(ZoneOffset.of("-04:00"));
        if(currentDate.isBefore(expireDate)) {
            return true;
        } else {
            return false;
        }

    }

    public void setStatusPaymentLink(C2pRequest c2pRequest) {
        if(c2pRequest.getPaymentChannel() == 2L) {
            BankTransactionEntity bankTransactionEntity = bankTransactionRepository.
                    findByIdBankTransaction(c2pRequest.getIdBankTransaction());

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
