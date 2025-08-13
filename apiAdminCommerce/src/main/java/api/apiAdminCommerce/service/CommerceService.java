package api.apiAdminCommerce.service;

import api.apiAdminCommerce.entity.*;
import api.apiAdminCommerce.repository.intDbRepository.*;
import api.apiAdminCommerce.security.jwt.JwtUtils;

import api.apiAdminCommerce.to.CommerceDetailTo;
import api.apiAdminCommerce.to.CommerceInformationDetailTo;
import api.apiAdminCommerce.to.CommerceTo;
import api.apiAdminCommerce.to.PreRegisterTo;
import api.apiAdminCommerce.to.StatusCommerceTo;
import api.apiAdminCommerce.util.Response;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class CommerceService {

    private static final Logger logger = LoggerFactory.getLogger(CommerceService.class);

    private final CommerceRepository commerceRepository;

    private final DirectionRepository directionRepository;

    private final EconomicActivityRepository economicActivityRepository;

    private final Environment environment;

    public CommerceService(CommerceRepository commerceRepository, DirectionRepository directionRepository,
                           EconomicActivityRepository economicActivityRepository, Environment environment) {
        this.commerceRepository = commerceRepository;
        this.directionRepository = directionRepository;
        this.economicActivityRepository = economicActivityRepository;
        this.environment = environment;
    }

    @Transactional(transactionManager = "secondTransactionManager")
    public Response getAllCommerce(int page, int quantity, Boolean export) {
        List<CommerceTo> commerceEntities = new ArrayList<>();
        Page<CommerceTo> commerceEntitiesPaged = null;
        Pageable pageable = PageRequest.of(page, quantity);

        if(export) {
            commerceEntities = commerceRepository.findAllByStatusCommerce_IdStatusExport();
        } else {
            commerceEntitiesPaged = commerceRepository.findAllByStatusCommerce_IdStatus(pageable);
            commerceEntities = commerceEntitiesPaged.getContent();
        }

        if (commerceEntities.isEmpty()) {
            return new Response("SUCCESS", "No data");
        } else {
            return new Response("SUCCESS",export == true ?commerceEntities: commerceEntitiesPaged);
        }
    }

    public Response getAllCommerceByFilter(final String apiKey, final String token,
                                             final int index,final int quantity,
                                             final String startDate,final String endDate,
                                             final String filter, final Integer typeFilter, final Boolean export){

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
            requestBody.put("typeFilter", typeFilter);
            requestBody.put("index", index);
            requestBody.put("quantity", quantity);
            requestBody.put("export", export);

            HttpEntity<LinkedHashMap<String, Object>> request = new HttpEntity<>(requestBody, headers);

            ResponseEntity<String> responseEntity = restTemplate.exchange(uri.
                    concat("/getAllCommerceByFilter"), HttpMethod.POST, request, String.class);

            if (!Objects.requireNonNull(responseEntity.getBody()).contains("ERROR")) {
                LinkedHashMap<String, Object> responseMap = objectMapper.readValue(responseEntity.getBody(), LinkedHashMap.class);
                List<CommerceTo> commerceToList = (List<CommerceTo>) responseMap.get("commerceFilter");

                if(export) {
                    return new Response("SUCCESS", commerceToList);
                } else {

                    Pageable pageList = PageRequest.of(index, quantity);
                    int start = (int) pageList.getOffset();
                    int end = Math.min((start + pageList.getPageSize()), commerceToList.size());

                    Page<CommerceTo> commerceToPage =  new
                            PageImpl<>(commerceToList.subList(start, end), pageList, commerceToList.size());

                    return new Response("SUCCESS", commerceToPage);
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

    @Transactional(transactionManager = "secondTransactionManager")
    public Boolean saveCommerce(PreRegisterEntity preRegisterEntity, Long idEconomicActivity) {

        try {
            CommerceEntity commerceEntity = new CommerceEntity();
            DirectionEntity directionEntity = new DirectionEntity();

            commerceEntity.setCommerceName(preRegisterEntity.getCommerceName());
            commerceEntity.setCommerceDocument(preRegisterEntity.getCommerceDocument());
            commerceEntity.setCommerceEmail(preRegisterEntity.getCommerceEmail());
            commerceEntity.setPhoneNumberCommerce(preRegisterEntity.getPhoneNumberCommerce());
            commerceEntity.setContactPerson(preRegisterEntity.getContactPerson());
            commerceEntity.setContactPersonEmail(preRegisterEntity.getContactPersonEmail());
            commerceEntity.setEconomicActivityEntity(new EconomicActivityEntity(idEconomicActivity));
            commerceEntity.setStatusCommerce(new StatusCommerceEntity(2L));

            directionEntity.setStatus(true);
            directionEntity.setCityEntity(preRegisterEntity.getDirectionEntity().getCityEntity());
            directionEntity.setAddress(preRegisterEntity.getDirectionEntity().getAddress());
            directionRepository.save(directionEntity);

            commerceEntity.setDirectionEntity(directionEntity);
            commerceEntity.setRegisterDate(OffsetDateTime.now(ZoneId.of("America/Caracas")));
            commerceEntity.setUpdateDate(OffsetDateTime.now(ZoneId.of("America/Caracas")));
            commerceRepository.save(commerceEntity);
            return true;

        } catch (Exception e) {
            return false;
        }

    }


    @Transactional
    public Boolean saveCommerceGetWay(PreRegisterTo preRegisterTo, Long idEconomicActivity, final Long idCity,
                                      final String apiKey, final String token) {

        try {

            HttpHeaders headers = new HttpHeaders();
            RestTemplate restTemplate = new RestTemplate();

            headers.add("API_KEY", apiKey);
            headers.add("token", token);

            String uri = environment.getProperty("api.route.internal");

            LinkedHashMap<String, Object> requestBody = new LinkedHashMap<>();

            requestBody.put("preRegisterTo", preRegisterTo);
            requestBody.put("idEconomicActivity", idEconomicActivity);
            requestBody.put("idCity", idCity);

            HttpEntity<LinkedHashMap<String, Object>> request = new HttpEntity<>(requestBody, headers);

            ResponseEntity<String> responseEntity = restTemplate.exchange(Objects.requireNonNull(uri).
                    concat("/saveCommerce"), HttpMethod.POST, request, String.class);

            if (!Objects.requireNonNull(responseEntity.getBody()).contains("ERROR")) {
                logger.info("Commerce saved successfully");
                return true;
            } else {
                logger.error("Response Status: " + responseEntity.getBody());
                return false;
            }

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return false;
        }

    }

    public CommerceTo getCommerceByDocumentAndStatus(final String commerceDocument, final Long idStatusCommerce,
                                                     final String apiKey, final String token){

        try {

            HttpHeaders headers = new HttpHeaders();
            RestTemplate restTemplate = new RestTemplate();

            headers.add("API_KEY", apiKey);
            headers.add("token", token);

            String uri = environment.getProperty("api.route.internal");

            LinkedHashMap<String, Object> requestBody = new LinkedHashMap<>();

            requestBody.put("commerceDocument", commerceDocument);
            requestBody.put("idStatusCommerce", idStatusCommerce);

            HttpEntity<LinkedHashMap<String, Object>> request = new HttpEntity<>(requestBody, headers);

            ResponseEntity<String> responseEntity = restTemplate.exchange(Objects.requireNonNull(uri).
                    concat("/getCommerceByDocumentAndStatus"), HttpMethod.POST, request, String.class);

            if (!Objects.requireNonNull(responseEntity.getBody()).contains("ERROR")) {
                ObjectMapper objectMapper = new ObjectMapper();

                LinkedHashMap<String, Object> responseMap = objectMapper.readValue(responseEntity.getBody(), LinkedHashMap.class);
                return new CommerceTo(objectMapper, responseMap);

            } else {
                logger.error("Response Status: " + responseEntity.getStatusCode());
                return null;
            }

        } catch (Exception e){
            logger.error(e.getMessage(), e);
            return null;
        }

    }

    public Response getCommerceInformationByRif(String rif) {
        CommerceInformationDetailTo commerceInformationDetailTo = commerceRepository.
                findCommerceByCommerceDocument(rif);

        if(commerceInformationDetailTo == null) {
            commerceInformationDetailTo = commerceRepository.findCommerceByCommerceDocumentNoBankInformation(rif);
        }

        if(commerceInformationDetailTo == null) {
            return new Response("ERROR", "No data");
        } else {
            return new Response("SUCCESS", commerceInformationDetailTo);
        }
    }

    public Response getCommerceInformationByIdUser(final Long idUser) {
        CommerceInformationDetailTo commerceInformationDetailTo = commerceRepository.
                findCommerceByIdUser(idUser);
        if(commerceInformationDetailTo == null) {
            commerceInformationDetailTo = commerceRepository.findCommerceByIdUserNoBankInformation(idUser);
        }

        if(commerceInformationDetailTo == null) {
            return new Response("ERROR", "No data");
        } else {
            return new Response("SUCCESS", commerceInformationDetailTo);
        }
    }

    public Response getCommerceInformationDetailByRif(String rif, final String apiKey,
                                                      final String token) {

        try {

            HttpHeaders headers = new HttpHeaders();
            RestTemplate restTemplate = new RestTemplate();

            headers.add("API_KEY", apiKey);
            headers.add("token", token);

            String uri = environment.getProperty("api.route.internal");

            LinkedHashMap<String, Object> requestBody = new LinkedHashMap<>();

            requestBody.put("rif", rif);

            HttpEntity<LinkedHashMap<String, Object>> request = new HttpEntity<>(requestBody, headers);

            ResponseEntity<String> responseEntity = restTemplate.exchange(Objects.requireNonNull(uri).
                            concat("/getCommerceInformationDetailByRif"), HttpMethod.POST,
                    request, String.class);

            if (!Objects.requireNonNull(responseEntity.getBody()).contains("ERROR")) {

                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.registerModule(new JavaTimeModule());

                LinkedHashMap<String, Object> responseMap =
                        objectMapper.readValue(responseEntity.getBody(), LinkedHashMap.class);

                logger.info("Commerce search successfully");

                return new Response("SUCCESS",
                        new CommerceDetailTo(objectMapper, responseMap));

            } else {
                logger.error("ERROR: commerce doesn't exist. " + responseEntity.getBody());
                return null;
            }

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return null;
        }

    }

    public Response getCommerceInformationDetailByIdUser(Long idUser, final String apiKey,
                                                      final String token) {

        try {

            HttpHeaders headers = new HttpHeaders();
            RestTemplate restTemplate = new RestTemplate();

            headers.add("API_KEY", apiKey);
            headers.add("token", token);

            String uri = environment.getProperty("api.route.internal");

            LinkedHashMap<String, Object> requestBody = new LinkedHashMap<>();

            requestBody.put("idUser", idUser);

            HttpEntity<LinkedHashMap<String, Object>> request = new HttpEntity<>(requestBody, headers);

            ResponseEntity<String> responseEntity = restTemplate.exchange(Objects.requireNonNull(uri).
                            concat("/getCommerceInformationDetailByIdUser"), HttpMethod.POST,
                    request, String.class);

            if (!Objects.requireNonNull(responseEntity.getBody()).contains("ERROR")) {

                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.registerModule(new JavaTimeModule());

                LinkedHashMap<String, Object> responseMap =
                        objectMapper.readValue(responseEntity.getBody(), LinkedHashMap.class);

                logger.info("Commerce search successfully");

                return new Response("SUCCESS",
                        new CommerceDetailTo(objectMapper, responseMap));

            } else {
                logger.error("ERROR: commerce doesn't exist. " + responseEntity.getBody());
                return null;
            }

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return null;
        }

    }

    @Transactional
    public Boolean generateLicenceGetWay(final Long idCommerce, final String apiKey, final String token,
                                         final Long idPlan) {

        try {

            HttpHeaders headers = new HttpHeaders();
            RestTemplate restTemplate = new RestTemplate();

            headers.add("API_KEY", apiKey);
            headers.add("token", token);

            String uri = environment.getProperty("api.route.internal");

            LinkedHashMap<String, Object> requestBody = new LinkedHashMap<>();

            requestBody.put("idCommerce", idCommerce);
            requestBody.put("idPlan", idPlan);
            /*requestBody.put("activationDate",activationDate);*/

            HttpEntity<LinkedHashMap<String, Object>> request = new HttpEntity<>(requestBody, headers);

            ResponseEntity<String> responseEntity = restTemplate.exchange(Objects.requireNonNull(uri).
                    concat("/generateLicence"), HttpMethod.POST,
                    request, String.class);

            if (!Objects.requireNonNull(responseEntity.getBody()).contains("ERROR")) {
                logger.info("License saved successfully");
                return true;
            } else {
                logger.error("Response Status: " + responseEntity.getStatusCode());
                return false;
            }

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return false;
        }

    }

    @Transactional
    public Long createUserAdminGetWay(final String rif, final String apiKey, final String token){

            try {

            String configurationValue = searchConfigurationById(1L, apiKey, token);

            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

            String password = passwordEncoder.encode(configurationValue);

            HttpHeaders headers = new HttpHeaders();
            RestTemplate restTemplate = new RestTemplate();

            headers.add("API_KEY", apiKey);
            headers.add("token", token);

            String uri = environment.getProperty("api.route.internal");

            LinkedHashMap<String, Object> requestBody = new LinkedHashMap<>();

            requestBody.put("commerceDocument", rif);
            requestBody.put("password", password);

            HttpEntity<LinkedHashMap<String, Object>> request = new HttpEntity<>(requestBody, headers);

            ResponseEntity<String> responseEntity = restTemplate.exchange(Objects.requireNonNull(uri).
                            concat("/createUserAdmin"), HttpMethod.POST,
                    request, String.class);

            if (!Objects.requireNonNull(responseEntity.getBody()).contains("ERROR")) {

                ObjectMapper objectMapper = new ObjectMapper();

                LinkedHashMap<String, Object> responseMap = objectMapper.readValue(responseEntity.getBody(), LinkedHashMap.class);

                Long idUser = objectMapper.convertValue(responseMap.get("idUser"), Long.class);

                logger.info("User saved successfully");
                return idUser;
            } else {
                logger.error("Response Status: " + responseEntity.getBody());
                return null;
            }

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return null;
        }

    }

    @Transactional
    public Long createClientGetWay(final CommerceTo commerceTo, final String apiKey, final String token,
                                  final HttpServletRequest httpServletRequest,
                                  final Long idUser,
                                   final String contactPersonDocument) {

        try {

            HttpHeaders headers = new HttpHeaders();
            RestTemplate restTemplate = new RestTemplate();

            LocalDate currentDate = LocalDate.now();

            headers.add("API_KEY", apiKey);
            headers.add("token", token);

            String uri = environment.getProperty("api.route.internal");

            LinkedHashMap<String, Object> requestBody = new LinkedHashMap<>();

            requestBody.put("clientName", commerceTo.getContactPerson());
            requestBody.put("identificationDocument", contactPersonDocument);
            requestBody.put("phoneNumber", commerceTo.getPhoneNumberCommerce());
            requestBody.put("email", commerceTo.getCommerceEmail());
            requestBody.put("registerBy", getIdUserByToken(httpServletRequest));
            requestBody.put("idCommerce", commerceTo.getIdCommerce());
            requestBody.put("idUser", idUser);
            requestBody.put("registerDate", currentDate);

            HttpEntity<LinkedHashMap<String, Object>> request = new HttpEntity<>(requestBody, headers);

            ResponseEntity<String> responseEntity = restTemplate.exchange(Objects.requireNonNull(uri).
                            concat("/createClient"), HttpMethod.POST,
                    request, String.class);

            if (!Objects.requireNonNull(responseEntity.getBody()).contains("ERROR")) {

                ObjectMapper objectMapper = new ObjectMapper();

                LinkedHashMap<String, Object> responseMap = objectMapper.readValue(responseEntity.getBody(), LinkedHashMap.class);

                Long idClient = objectMapper.convertValue(responseMap.get("idClient"), Long.class);

                logger.info("Client saved successfully");
                return idClient;

            } else {
                logger.error("Response Status: " + responseEntity.getBody());
                return null;
            }

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return null;
        }

    }

    @Transactional(transactionManager = "secondTransactionManager")
    public Response getAllEconomicActivities() {
        List<EconomicActivityEntity> economicActivityEntities = new ArrayList<>();
        economicActivityEntities = economicActivityRepository.findAllByStatusTrue();

        if (economicActivityEntities.isEmpty()) {
            return new Response("SUCCESS", "No data");
        } else {
            return new Response("SUCCESS", economicActivityEntities);
        }
    }

    public static Long getIdUserByToken(HttpServletRequest httpServletRequest){
        Claims claims = JwtUtils.extractClaims(httpServletRequest);
        return Objects.requireNonNull(claims).get("idUser", Long.class);
    }

    public String searchConfigurationById(final Long idConfiguration, final String apiKey, final String token) throws JsonProcessingException {

        HttpHeaders headers = new HttpHeaders();
        RestTemplate restTemplate = new RestTemplate();

        headers.add("API_KEY", apiKey);
        headers.add("token", token);

        String uri = environment.getProperty("api.route.external");

        LinkedHashMap<String, Object> requestBody = new LinkedHashMap<>();

        requestBody.put("idConfiguration", idConfiguration);

        HttpEntity<LinkedHashMap<String, Object>> request = new HttpEntity<>(requestBody, headers);

        ResponseEntity<String> responseEntity = restTemplate.exchange(Objects.requireNonNull(uri).
                concat("/searchConfigurationById"), HttpMethod.POST, request, String.class);

        if (!Objects.requireNonNull(responseEntity.getBody()).contains("ERROR")) {

            ObjectMapper objectMapper = new ObjectMapper();

            LinkedHashMap<String, Object> responseMap = objectMapper.readValue(responseEntity.getBody(), LinkedHashMap.class);

            String configurationValue = objectMapper.convertValue(responseMap.get("configurationValue"), String.class);

            logger.info("Configuration search successfully");
            return configurationValue;

        } else {
            logger.error("Response Status: " + responseEntity.getStatusCode());
            return null;
        }

    }

    @Transactional
    public Long createClientForUser(final CommerceTo commerceTo, final String apiKey, final String token,
                                   final HttpServletRequest httpServletRequest,
                                   final Long idUser,
                                   final String contactPersonDocument,final String fullName, final String phoneNumber,
                                   final String email ) {

        try {

            HttpHeaders headers = new HttpHeaders();
            RestTemplate restTemplate = new RestTemplate();

            headers.add("API_KEY", apiKey);
            headers.add("token", token);

            String uri = environment.getProperty("api.route.internal");
            LocalDate currentDate = LocalDate.now();
            LinkedHashMap<String, Object> requestBody = new LinkedHashMap<>();

            requestBody.put("clientName", fullName);
            requestBody.put("identificationDocument", contactPersonDocument);
            requestBody.put("phoneNumber", phoneNumber);
            requestBody.put("email", email);
            requestBody.put("registerBy", getIdUserByToken(httpServletRequest));
            requestBody.put("idCommerce", commerceTo.getIdCommerce());
            requestBody.put("idUser", idUser);
            requestBody.put("registerDate",currentDate);

            HttpEntity<LinkedHashMap<String, Object>> request = new HttpEntity<>(requestBody, headers);

            ResponseEntity<String> responseEntity = restTemplate.exchange(Objects.requireNonNull(uri).
                            concat("/createClient"), HttpMethod.POST,
                    request, String.class);

            if (!Objects.requireNonNull(responseEntity.getBody()).contains("ERROR")) {

                ObjectMapper objectMapper = new ObjectMapper();

                LinkedHashMap<String, Object> responseMap = objectMapper.readValue(responseEntity.getBody(), LinkedHashMap.class);

                Long idClient = objectMapper.convertValue(responseMap.get("idClient"), Long.class);

                logger.info("Client saved successfully");
                return idClient;

            } else {
                logger.error("Response Status: " + responseEntity.getBody());
                return null;
            }

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return null;
        }

    }

    @Transactional
    public Long createUserInt(final String rif, final String apiKey, final String token, final Long idProfile){

        try {

            String configurationValue = searchConfigurationById(1L, apiKey, token);

            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

            String password = passwordEncoder.encode(configurationValue);

            HttpHeaders headers = new HttpHeaders();
            RestTemplate restTemplate = new RestTemplate();

            headers.add("API_KEY", apiKey);
            headers.add("token", token);

            String uri = environment.getProperty("api.route.internal");

            LinkedHashMap<String, Object> requestBody = new LinkedHashMap<>();

            requestBody.put("commerceDocument", rif);
            requestBody.put("password", password);
            requestBody.put("idProfile", idProfile);

            HttpEntity<LinkedHashMap<String, Object>> request = new HttpEntity<>(requestBody, headers);

            ResponseEntity<String> responseEntity = restTemplate.exchange(Objects.requireNonNull(uri).
                            concat("/createUserInt"), HttpMethod.POST,
                    request, String.class);

            if (!Objects.requireNonNull(responseEntity.getBody()).contains("ERROR")) {

                ObjectMapper objectMapper = new ObjectMapper();

                LinkedHashMap<String, Object> responseMap = objectMapper.readValue(responseEntity.getBody(), LinkedHashMap.class);

                Long idUser = objectMapper.convertValue(responseMap.get("idUser"), Long.class);

                logger.info("User saved successfully");
                return idUser;
            } else {
                logger.error("Response Status: " + responseEntity.getBody());
                return null;
            }

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return null;
        }

    }

    public Response editProfileByCommerce(final Long idCommerce, final String commerceName,
            final String contactPerson, final String contactPersonEmail, final String phoneNumber,
            final Long idStatusCommerce, final Long idPlan, final String reasonStatus,
                                          final Long idCity,final String address, final Long idCommerceActivity,final String activationDate
            ,final HttpServletRequest httpServletRequest) {

        try {

            HttpHeaders headers = new HttpHeaders();
            RestTemplate restTemplate = new RestTemplate();

            headers.add("API_KEY", httpServletRequest.getHeader("API_KEY"));
            headers.add("token", httpServletRequest.getHeader("token"));

            String uri = environment.getProperty("api.route.internal");

            LinkedHashMap<String, Object> requestBody = new LinkedHashMap<>();

            requestBody.put("idCommerce", idCommerce);
            requestBody.put("commerceName", commerceName);
            requestBody.put("contactPerson", contactPerson);
            requestBody.put("contactPersonEmail", contactPersonEmail);
            requestBody.put("phoneNumber", phoneNumber);
            requestBody.put("idStatusCommerce", idStatusCommerce);
            requestBody.put("idPlan", idPlan);
            requestBody.put("reasonStatus", reasonStatus);
            requestBody.put("idCity", idCity);
            requestBody.put("address", address);
            requestBody.put("idCommerceActivity", idCommerceActivity);
            if(activationDate != null) {
                requestBody.put("activationDate", activationDate);
            }


            HttpEntity<LinkedHashMap<String, Object>> request = new HttpEntity<>(requestBody, headers);

            ResponseEntity<String> responseEntity = restTemplate.exchange(Objects.requireNonNull(uri).
                            concat("/editProfileByCommerce"), HttpMethod.POST,
                    request, String.class);

            if (!Objects.requireNonNull(responseEntity.getBody()).contains("ERROR")) {
                logger.info("Commerce information changed successfully");
                return new Response("SUCCESS", "Transacción exitosa");
            } else {
                logger.error("Response Status: " + responseEntity.getBody());
                return null;
            }

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return null;
        }

    }

    public Response getStatusCommerce(final Long idStatusCommerce,
                                      final HttpServletRequest httpServletRequest) {

        try {

            HttpHeaders headers = new HttpHeaders();
            RestTemplate restTemplate = new RestTemplate();

            headers.add("API_KEY", httpServletRequest.getHeader("API_KEY"));
            headers.add("token", httpServletRequest.getHeader("token"));

            String uri = environment.getProperty("api.route.internal");

            HttpEntity<LinkedHashMap<String, Object>> request = new HttpEntity<>(headers);

            ResponseEntity<String> responseEntity = restTemplate.exchange(Objects.requireNonNull(uri).
                            concat("/getStatusCommerce/"+idStatusCommerce), HttpMethod.GET,
                    request, String.class);

            if (!Objects.requireNonNull(responseEntity.getBody()).contains("ERROR")) {
                ObjectMapper objectMapper = new ObjectMapper();

                LinkedHashMap<String, Object> responseMap = objectMapper.readValue(responseEntity.getBody(), LinkedHashMap.class);
                List<LinkedHashMap<String, Object>> statusCommerceEntityList = (List<LinkedHashMap<String, Object>>) responseMap.get("statusCommerceEntities");

                List<StatusCommerceTo> statusCommerceTos = statusCommerceEntityList.stream().map(map -> {
                    StatusCommerceTo statusCommerceTo = new StatusCommerceTo();
                    statusCommerceTo.setIdStatusCommerce(((Number) map.get("idStatusCommerce")).longValue());
                    statusCommerceTo.setStatusDescription((String) map.get("statusDescription"));
                    return statusCommerceTo;
                }).collect(Collectors.toList());

                return new Response("SUCCESS", statusCommerceTos);

            } else {
                return new Response("SUCCESS", "No data");
            }

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return null;
        }

    }


    @Transactional
    public Long createAdminUser(final String apiKey, final String token,
                                    final HttpServletRequest httpServletRequest,
                                    final Long idUser,
                                    final String contactPersonDocument,final String fullName, final String phoneNumber,
                                    final String email ) {

        try {

            HttpHeaders headers = new HttpHeaders();
            RestTemplate restTemplate = new RestTemplate();

            headers.add("API_KEY", apiKey);
            headers.add("token", token);

            String uri = environment.getProperty("api.route.internal");
            LocalDate currentDate = LocalDate.now();
            LinkedHashMap<String, Object> requestBody = new LinkedHashMap<>();

            requestBody.put("clientName", fullName);
            requestBody.put("identificationDocument", contactPersonDocument);
            requestBody.put("phoneNumber", phoneNumber);
            requestBody.put("email",email);
            requestBody.put("registerBy", getIdUserByToken(httpServletRequest));
            requestBody.put("idUser", idUser);
            requestBody.put("registerDate", currentDate);

            HttpEntity<LinkedHashMap<String, Object>> request = new HttpEntity<>(requestBody, headers);

            ResponseEntity<String> responseEntity = restTemplate.exchange(Objects.requireNonNull(uri).
                            concat("/createUserAdminForUsers"), HttpMethod.POST,
                    request, String.class);

            if (!Objects.requireNonNull(responseEntity.getBody()).contains("ERROR")) {

                ObjectMapper objectMapper = new ObjectMapper();

                LinkedHashMap<String, Object> responseMap = objectMapper.readValue(responseEntity.getBody(), LinkedHashMap.class);

                Long idUserResponse = objectMapper.convertValue(responseMap.get("idUser"), Long.class);

                logger.info("UserAdmin saved successfully");
                return idUserResponse;

            } else {
                logger.error("Response Status: " + responseEntity.getBody());
                return null;
            }

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return null;
        }

    }

}
