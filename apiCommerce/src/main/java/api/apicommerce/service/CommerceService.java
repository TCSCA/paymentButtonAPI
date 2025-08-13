package api.apicommerce.service;

import api.apicommerce.entity.*;
import api.apicommerce.repository.BankCommerceRepository;
import api.apicommerce.repository.BankRepository;
import api.apicommerce.repository.CommerceRepository;
import api.apicommerce.request.CommerceConfigRequest;
import api.apicommerce.request.CommerceUpdateRequest;
import api.apicommerce.security.jwt.JwtUtils;
import api.apicommerce.to.CommerceBankInformationTo;
import api.apicommerce.util.Response;
import api.logsClass.LogsClass;
import api.logsClass.ManageLogs;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;

@Service
public class CommerceService {

    private static final Logger logger = LoggerFactory.getLogger(CommerceService.class);

    private final CommerceRepository commerceRepository;

    private final BankCommerceRepository bankCommerceRepository;

    private final ManageLogs manageLogs;

    private final BankRepository bankRepository;

    public CommerceService(CommerceRepository commerceRepository, BankCommerceRepository bankCommerceRepository,
                           ManageLogs manageLogs, BankRepository bankRepository) {
        this.commerceRepository = commerceRepository;
        this.bankCommerceRepository = bankCommerceRepository;
        this.manageLogs = manageLogs;
        this.bankRepository = bankRepository;
    }

    public Boolean saveCommerceConfig(CommerceConfigRequest commerceConfigRequest) {

        try {
            CommerceEntity commerceEntity = commerceRepository.findByCommerceDocument(commerceConfigRequest.getRif());
            BankEntity bankEntity = bankRepository.findByBankCode(commerceConfigRequest.getIdBank());
            validateCommerceInformation(commerceEntity);

            if (commerceEntity != null) {
                BankCommerceEntity bankCommerceEntity = new BankCommerceEntity();

                if(bankEntity.getIdBank() == 5L) {
                bankCommerceEntity.setConsumerKey(commerceConfigRequest.getConsumerKey());
                bankCommerceEntity.setConsumerSecret(commerceConfigRequest.getConsumerSecret());
                bankCommerceEntity.setBankHash(commerceConfigRequest.getHash());
                }
                bankCommerceEntity.setCommerceEntity(new CommerceEntity(commerceEntity.getIdCommerce()));
                bankCommerceEntity.setBankEntity(bankEntity);
                bankCommerceEntity.setCommercePhone(commerceConfigRequest.getCommercePhone());
                if(commerceConfigRequest.getBankAccount()!= null) {
                bankCommerceEntity.setBankAccount(commerceConfigRequest.getBankAccount());
                }
                bankCommerceEntity.setStatus(true);
                bankCommerceRepository.save(bankCommerceEntity);

                if(commerceConfigRequest.getConsumerSecretCreditCard() != null) {
                    if(commerceConfigRequest.getConsumerKeyCreditCard().length() > 0
                            && commerceConfigRequest.getConsumerSecretCreditCard().length() > 0) {
                        saveCreditCardInformation(commerceConfigRequest, commerceEntity);
                    }
                }


                return true;
            } else {
                return false;
            }

        } catch (Exception e) {
            return false;
        }

    }

    public void validateCommerceInformation(CommerceEntity commerceEntity) {
     List<BankCommerceEntity> bankCommerceEntities = bankCommerceRepository.
             findAllByCommerceEntity_IdCommerceAndStatusTrue
                (commerceEntity.getIdCommerce());

     for(BankCommerceEntity bankCommerceEntity: bankCommerceEntities) {
         bankCommerceEntity.setStatus(false);
         bankCommerceRepository.save(bankCommerceEntity);
     }
    }

    public void saveCreditCardInformation(CommerceConfigRequest commerceConfigRequest,
                                             CommerceEntity commerceEntity) {

        BankCommerceEntity bankCommerceEntityCreditCard = new BankCommerceEntity();
        bankCommerceEntityCreditCard.setCommerceEntity(new CommerceEntity(commerceEntity.getIdCommerce()));
        bankCommerceEntityCreditCard.setBankEntity(new BankEntity(99L));
        bankCommerceEntityCreditCard.setConsumerKey(commerceConfigRequest.getConsumerKeyCreditCard());
        bankCommerceEntityCreditCard.setConsumerSecret(commerceConfigRequest.getConsumerSecretCreditCard());
        bankCommerceEntityCreditCard.setCommercePhone(commerceConfigRequest.getCommercePhone());
        bankCommerceEntityCreditCard.setStatus(true);
        bankCommerceRepository.save(bankCommerceEntityCreditCard);
    }

    public Boolean updateCommerceInfo(CommerceUpdateRequest commerceUpdateRequest) {
        try {
            CommerceEntity commerceEntity = commerceRepository.findByCommerceDocument(commerceUpdateRequest.getRif());
            if(commerceEntity != null) {

                if(commerceUpdateRequest.getDirectionEntity() != null) {
                    commerceEntity.getDirectionEntity().setAddress(commerceUpdateRequest.getDirectionEntity().getAddress()
                            != null ? commerceUpdateRequest.getDirectionEntity().getAddress() :
                            commerceEntity.getDirectionEntity().getAddress());

                    if(commerceUpdateRequest.getDirectionEntity().getCityEntity().getIdCity() != null) {
                        commerceEntity.getDirectionEntity().setCityEntity(new CityEntity(commerceUpdateRequest
                                .getDirectionEntity().getCityEntity().getIdCity()));
                    }
                }
                commerceEntity.setUpdateDate(OffsetDateTime.now(ZoneId.of("America/Caracas")));
                commerceRepository.save(commerceEntity);

            } else {
                return false;
            }

            return true;
        } catch (Exception e) {
            return false;
        }

    }

    public Long getIdUserByToken(HttpServletRequest httpServletRequest){
        Claims claims = JwtUtils.extractClaims(httpServletRequest);
        return Objects.requireNonNull(claims).get("idUser", Long.class);
    }

    public CommerceBankInformationTo getCommerceBankInformationByRif(final String rif, final String apiKey, final String token,
                                                    HttpServletRequest httpServletRequest) {

        LogsClass logTo = new LogsClass(httpServletRequest, new Object() {}.
                getClass().getEnclosingMethod().getName());

        try {

            HttpHeaders headers = new HttpHeaders();
            RestTemplate restTemplate = new RestTemplate();

            headers.add("API_KEY", apiKey);
            headers.add("token", token);

            String uri = "http://localhost:8090/core";

            LinkedHashMap<String, Object> requestBody = new LinkedHashMap<>();

            requestBody.put("rif", rif);

            HttpEntity<LinkedHashMap<String, Object>> request = new HttpEntity<>(requestBody, headers);

            ResponseEntity<String> responseEntity = restTemplate.exchange(uri.
                    concat("/getCommerceBankInformation"), HttpMethod.POST, request, String.class);

            if (!Objects.requireNonNull(responseEntity.getBody()).contains("ERROR")) {
                manageLogs.infoLogger(logTo,httpServletRequest,
                        "Successfully: " + logTo.getMethodName(),
                        "unlogged user",false);
                logger.info("User found successfully");

                ObjectMapper objectMapper = new ObjectMapper();

                LinkedHashMap<String, Object> responseMap = objectMapper.readValue(responseEntity.getBody(), LinkedHashMap.class);
                return new CommerceBankInformationTo(objectMapper, responseMap);

            } else {
                manageLogs.errorLogger(logTo, httpServletRequest, responseEntity.getBody(),
                        "unlogged user", false);
                logger.error("User not found");
                return null;
            }

        } catch (Exception e) {
            manageLogs.severeErrorLogger(logTo,httpServletRequest,"Error: "+ logTo.
                    getMethodName() + ": "+ e, e,"unlogged user",false);
            logger.error(e.getMessage(), e);

            return null;
        }

    }

    public Response validateCommerceLicence(final String rif, String apiKey, String token, HttpServletRequest httpServletRequest) {
        LogsClass logTo = new LogsClass(httpServletRequest, new Object() {}.
                getClass().getEnclosingMethod().getName());

        try {
            HttpHeaders headers = new HttpHeaders();
            RestTemplate restTemplate = new RestTemplate();

            headers.add("API_KEY", apiKey);
            headers.add("token", token);

            String uri = "http://localhost:8090/core";

            LinkedHashMap<String, Object> requestBody = new LinkedHashMap<>();

            requestBody.put("rif", rif);

            HttpEntity<LinkedHashMap<String, Object>> request = new HttpEntity<>(requestBody, headers);

            ResponseEntity<String> responseEntity = restTemplate.exchange(uri.
                    concat("/validateCommerceLicence"), HttpMethod.POST, request, String.class);

            manageLogs.infoLogger(logTo,httpServletRequest,
                    "Successfully: " + logTo.getMethodName(),
                    "user",false);
            logger.info("Metodo de validacion de licencia Successfully");

            if (Objects.requireNonNull(responseEntity.getBody()).contains("ERROR")) {
                ObjectMapper objectMapper = new ObjectMapper();
                LinkedHashMap<String, Object> responseMap = objectMapper.readValue(responseEntity.getBody(), LinkedHashMap.class);

                return new Response("ERROR", responseMap.get("ERROR"));
            } else {
                return new Response("SUCCESS", "Licencia válida");
            }

        } catch (Exception e) {
            manageLogs.severeErrorLogger(logTo,httpServletRequest,"Error: "+ logTo.
                    getMethodName() + ": "+ e, e,"user",false);
            logger.error(e.getMessage(), e);

            return null;
        }
    }

    public String getUsernameByIdUser(final Long idUser, final String apiKey, final String token,
                                      HttpServletRequest httpServletRequest) {

        LogsClass logTo = new LogsClass(httpServletRequest, new Object() {}.
                getClass().getEnclosingMethod().getName());

        try {

            HttpHeaders headers = new HttpHeaders();
            RestTemplate restTemplate = new RestTemplate();

            headers.add("API_KEY", apiKey);
            headers.add("token", token);

            String uri = "http://localhost:8091/horizonte";

            LinkedHashMap<String, Object> requestBody = new LinkedHashMap<>();

            requestBody.put("idUser", idUser);

            HttpEntity<LinkedHashMap<String, Object>> request = new HttpEntity<>(requestBody, headers);

            ResponseEntity<String> responseEntity = restTemplate.exchange(uri.
                    concat("/getUserByIdUser"), HttpMethod.POST, request, String.class);

            if (!Objects.requireNonNull(responseEntity.getBody()).contains("ERROR")) {
                manageLogs.infoLogger(logTo,httpServletRequest,
                        "Successfully: " + logTo.getMethodName(),
                        "user",true);
                logger.info("User found successfully");

                ObjectMapper objectMapper = new ObjectMapper();

                LinkedHashMap<String, Object> responseMap = objectMapper.readValue(responseEntity.
                        getBody(), LinkedHashMap.class);

                return objectMapper.
                        convertValue(((LinkedHashMap<String, Object>) responseMap.get("userEntity")).
                                get("username"), String.class);

            } else {
                manageLogs.errorLogger(logTo, httpServletRequest, responseEntity.getBody(),
                        "user", true);
                logger.error("User not found");
                return null;
            }

        } catch (Exception e) {
            manageLogs.severeErrorLogger(logTo,httpServletRequest,"Error: "+ logTo.
                            getMethodName() + ": "+ e, e,"user",true);
            logger.error(e.getMessage(), e);

            return null;
        }

    }

}
