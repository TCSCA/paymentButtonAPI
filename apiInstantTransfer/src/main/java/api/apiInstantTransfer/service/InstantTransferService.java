package api.apiInstantTransfer.service;

import api.apiInstantTransfer.entity.*;
import api.apiInstantTransfer.repository.BankRepository;
import api.apiInstantTransfer.repository.BankTransactionRepository;
import api.apiInstantTransfer.repository.CommerceRepository;
import api.apiInstantTransfer.request.InstantTransferRequest;
import api.apiInstantTransfer.response.ResponseInstantTransfer;
import api.apiInstantTransfer.util.LogTo;
import api.apiInstantTransfer.util.ManageLog;
import api.apiInstantTransfer.util.Response;
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
public class InstantTransferService {

    private final BankServiceService bankServiceService;

    private final BankCommerceService bankCommerceService;

    private final BankRepository bankRepository;

    private final CommerceRepository commerceRepository;

    private final BankTransactionRepository bankTransactionRepository;

    public InstantTransferService(BankServiceService bankServiceService, BankCommerceService bankCommerceService,
                                  BankRepository bankRepository, CommerceRepository commerceRepository, BankTransactionRepository bankTransactionRepository) {
        this.bankServiceService = bankServiceService;
        this.bankCommerceService = bankCommerceService;
        this.bankRepository = bankRepository;
        this.commerceRepository = commerceRepository;
        this.bankTransactionRepository = bankTransactionRepository;
    }

    public Response validateInstantTransferRequestData(InstantTransferRequest instantTransferRequest, ManageLog manageLog, LogTo logTo,
                                           HttpServletRequest httpServletRequest) {
        Response response;

        if (!(instantTransferRequest.getRif().startsWith("J") || (instantTransferRequest.getRif().startsWith("G")))) {
            response = new Response("ERROR", "Formato de Rif Invalido: "
                    + instantTransferRequest.getRif() + ". Formato Válido: J|GXXXXXXXXX");
            manageLog.infoLogger(logTo, httpServletRequest, response.toString(), false);

            return response;
        }

        if (!(instantTransferRequest.getPaymentDocument().startsWith("V") || (instantTransferRequest.getPaymentDocument().startsWith("E"))
                || (instantTransferRequest.getPaymentDocument().startsWith("J")) || (instantTransferRequest.getPaymentDocument().startsWith("G"))) ) {
            response = new Response("ERROR", "Formato de documentId invalido " + instantTransferRequest.getPaymentDocument()
                    + ". Formato Válido: VXXXXXXXX");
            manageLog.infoLogger(logTo, httpServletRequest, response.toString(), false);

            return response;
        }
        response = new Response("SUCCESS", "VALID");
        return response;
    }

    public Response validateInstantTransferPayment(InstantTransferRequest instantTransferRequest,
                                                   HttpServletResponse httpServletResponse,
                                                   BankCommerceEntity bankCommerceEntity) throws IOException {
        ResponseInstantTransfer responseInstantTransfer;

        if(bankCommerceEntity.getToken()== null) {
            String bankToken = getBankToken(bankCommerceEntity.getConsumerKey(), bankCommerceEntity.getConsumerSecret(), bankCommerceEntity);
            if(bankToken == null) {
                httpServletResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                return new Response("ERROR", "Error en metodo token");
            }
            responseInstantTransfer = callToBank(instantTransferRequest, bankToken, bankCommerceEntity);
            if(responseInstantTransfer == null) {
                httpServletResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                return new Response("ERROR", "Error en metodo de pago");
            }

            if (saveBankTransaction(instantTransferRequest, responseInstantTransfer)) {
                if(responseInstantTransfer.getEnvelope().getBody().getRegistrarPagoC2PAPIResponse().getOut().getCodigoError() == 0) {
                    return new Response("SUCCESS", "Transacción exitosa");
                } else {
                    LinkedHashMap<String,Object> responseBank = new LinkedHashMap<>();
                    responseBank.put("code",responseInstantTransfer.getEnvelope().getBody().
                            getRegistrarPagoC2PAPIResponse().getOut().getCodigoError());

                    responseBank.put("message",responseInstantTransfer.getEnvelope().getBody().
                            getRegistrarPagoC2PAPIResponse().getOut().getDescripcionError());

                    Response response = new Response("ERROR", "mensaje sistema");
                    response.setProperties(responseBank);
                    return response;
                }
            }
        } else {
            if(!validateBankToken(bankCommerceEntity)) {
                String newBankToken = getBankToken(bankCommerceEntity.getConsumerKey(), bankCommerceEntity.getConsumerSecret(), bankCommerceEntity);
                if(newBankToken == null) {
                    httpServletResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    return new Response("ERROR", "Error en metodo token");
                }
                responseInstantTransfer = callToBank(instantTransferRequest, newBankToken, bankCommerceEntity);
                if(responseInstantTransfer == null) {
                    httpServletResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    return new Response("ERROR", "Error en metodo de pago");
                }
                if (saveBankTransaction(instantTransferRequest, responseInstantTransfer)) {
                    if(responseInstantTransfer.getEnvelope().getBody().getRegistrarPagoC2PAPIResponse().getOut().getCodigoError() == 0) {
                        return new Response("SUCCESS", "Transacción exitosa");
                    } else {
                        LinkedHashMap<String,Object> responseBank = new LinkedHashMap<>();
                        responseBank.put("code",responseInstantTransfer.getEnvelope().getBody().
                                getRegistrarPagoC2PAPIResponse().getOut().getCodigoError());

                        responseBank.put("message",responseInstantTransfer.getEnvelope().getBody().
                                getRegistrarPagoC2PAPIResponse().getOut().getDescripcionError());

                        Response response = new Response("ERROR", "mensaje sistema");
                        response.setProperties(responseBank);
                        return response;
                    }
                }

            } else {
                responseInstantTransfer = callToBank(instantTransferRequest, bankCommerceEntity.getToken(), bankCommerceEntity);
                if(responseInstantTransfer == null) {
                    httpServletResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    return new Response("ERROR", "Error en metodo de pago");
                }
                if (saveBankTransaction(instantTransferRequest, responseInstantTransfer)) {
                    if(responseInstantTransfer.getEnvelope().getBody().getRegistrarPagoC2PAPIResponse().getOut().getCodigoError() == 0) {
                        return new Response("SUCCESS", "Transacción exitosa");
                    } else {
                        LinkedHashMap<String,Object> responseBank = new LinkedHashMap<>();
                        responseBank.put("code",responseInstantTransfer.getEnvelope().getBody().
                                getRegistrarPagoC2PAPIResponse().getOut().getCodigoError());

                        responseBank.put("message",responseInstantTransfer.getEnvelope().getBody().
                                getRegistrarPagoC2PAPIResponse().getOut().getDescripcionError());

                        Response response = new Response("ERROR", "mensaje sistema");
                        response.setProperties(responseBank);
                        return response;
                    }
                }
            }
        }
        return new Response("ERROR", "Informacion bancaria no existe");
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

        } catch (Exception e) {
            return null;
        }
    }

    private ResponseInstantTransfer callToBank(InstantTransferRequest instantTransferRequest, String token,
                                               BankCommerceEntity bankCommerceEntity) throws IOException {

        try {
            BankServiceEntity bankServiceEntity = bankServiceService.getUrlByIdPaymentMethodAndIdBank("instantTransfer", 5L);
            if(bankServiceEntity == null) {
                return null;
            }
            String url = bankServiceEntity.getUrl();

            GsonBuilder b = new GsonBuilder();
            Gson gson = b.setPrettyPrinting().create();
            ObjectMapper objectMapper = new ObjectMapper();

            //TODO: agregar ip dinamicamente

            String requestBody = "{\"requestIniciarCredInmediato\":".concat("{\"bancoDestino\":\"" + instantTransferRequest.getBankPayment() +"\""+
                    ", \"cedulaRifBeneficiario\":\"" + instantTransferRequest.getPaymentDocument() + "\"" +
                    ", \"cedulaRifEmisor\":\"" + instantTransferRequest.getRif() + "\"" +
                    ", \"concepto\":\"" + instantTransferRequest.getConcept() + "\"" +
                    ", \"cuentaDestino\":\"" + instantTransferRequest.getAccountPayment() + "\"" +
                    ", \"monedaDestino\":\"" + "0" + "\"" +
                    ", \"hash\":\"" + bankCommerceEntity.getBankHash() + "\"" +
                    ", \"identificadorExterno\":\"" + token + "\"" +
                    ", \"ipOrigen\":\"" + instantTransferRequest.getCustomerIpAddress() + "\"" +
                    ", \"monto\":\"" + instantTransferRequest.getAmount() + "\"" +
                    ", \"nombreBeneficiario\":\"" + instantTransferRequest.getPaymentName() + "\"" +
                    ", \"nombreEmisor\":\"" + bankCommerceEntity.getCommerceEntity().getCommerceName() + "\"" +
                    ", \"terminal\":\"" + "TEMINAL1" + "\"" +
                    ", \"tipoCtaCele\":\"" + "CNTA" + "".concat("\"").concat("}").concat("}"));

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
            ResponseInstantTransfer linkedHashMapResponse = (objectMapper.convertValue(responseBodyParsed, ResponseInstantTransfer.class));

            return linkedHashMapResponse;
        } catch (IOException e) {
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

    private Boolean saveBankTransaction(InstantTransferRequest instantTransferRequest, ResponseInstantTransfer responseInstantTransfer) {
        try {
            BankTransactionEntity bankTransactionEntity = new BankTransactionEntity();

            BankEntity bankEntity = bankRepository.findByBankCodeAndStatusTrue(instantTransferRequest.getBankPayment());
            bankTransactionEntity.setBankEntity(bankEntity);
            bankTransactionEntity.setTransactionCode(String.valueOf(responseInstantTransfer.getEnvelope().getBody()
                    .getRegistrarPagoC2PAPIResponse().getOut().getCodigoError()));
            bankTransactionEntity.setReferenceNumber(responseInstantTransfer.getEnvelope().getBody()
                    .getRegistrarPagoC2PAPIResponse().getOut().getSecuencial());
            bankTransactionEntity.setAmount(new BigDecimal(String.valueOf(instantTransferRequest.getAmount())));
            CommerceEntity commerceEntity = commerceRepository.findByCommerceDocument(instantTransferRequest.getRif());
            bankTransactionEntity.setCommerceEntity(commerceEntity);
            bankTransactionEntity.setPaymentChannel(new PaymentChannel(instantTransferRequest.getPaymentChannel()));
            bankTransactionEntity.setCurrencyEntity(new CurrencyEntity(2L));
            bankTransactionEntity.setPaymentMethodEntity(new PaymentMethodEntity(3L));
            bankTransactionEntity.setUpdateBy(instantTransferRequest.getPaymentDocument());
            bankTransactionEntity.setRegisterBy(instantTransferRequest.getPaymentDocument());
            if(responseInstantTransfer.getEnvelope().getBody().getRegistrarPagoC2PAPIResponse().getOut().getCodigoError() == 0) {
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
    private String convertKeyToBase64(String consumerKey, String consumerSecret) {
        String requestKey = consumerKey.concat(":").concat(consumerSecret);
        byte[] bytesEncoded = Base64.encodeBase64(requestKey.getBytes(), false);
        return new String(bytesEncoded);
    }
}
