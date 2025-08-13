package api.apiAdminCommerce.service;

import api.apiAdminCommerce.entity.*;
import api.apiAdminCommerce.repository.extDbRepository.HistoryPasswordRepository;
import api.apiAdminCommerce.repository.extDbRepository.UserExtRepository;
import api.apiAdminCommerce.repository.intDbRepository.*;
import api.apiAdminCommerce.to.ContactPersonTo;
import api.apiAdminCommerce.to.ProfileTo;
import api.apiAdminCommerce.to.UsersRequiredTo;
import api.apiAdminCommerce.util.EmailGoogleService;
import api.apiAdminCommerce.util.Response;
import api.logsClass.LogsClass;
import api.logsClass.ManageLogs;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMultipart;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.InputStream;
import java.time.OffsetDateTime;
import java.util.*;

import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private final TokenRepository tokenRepository;

    private final UserRepository userRepository;

    private final UserExtRepository userExtRepository;

    private final HistoryPasswordRepository historyPasswordRepository;

    private final ConfigurationRepository configurationRepository;

    private final EmailGoogleService emailGoogleService;

    private final CommerceRepository commerceRepository;

    private final ClientRepository clientRepository;

    private final ManageLogs manageLogs;


    public UserService(TokenRepository tokenRepository, UserRepository userRepository,
                       UserExtRepository userExtRepository, CommerceRepository commerceRepository,
                       ConfigurationRepository configurationRepository, HistoryPasswordRepository historyPasswordRepository, EmailGoogleService emailGoogleService, ClientRepository clientRepository, ClientRepository clientRepository1, ManageLogs manageLogs) {
        this.tokenRepository = tokenRepository;
        this.userRepository = userRepository;
        this.userExtRepository = userExtRepository;
        this.historyPasswordRepository = historyPasswordRepository ;
        this.configurationRepository = configurationRepository;
        this.emailGoogleService = emailGoogleService;
        this.commerceRepository = commerceRepository;
        this.clientRepository = clientRepository1;
        this.manageLogs = manageLogs;
    }

    public UserEntity findUserByToken(String token) {
        UserEntity userEntity;

        TokenEntity tokenEntity = tokenRepository.findByTokenAndActiveTrue(token);
        userEntity = userRepository.findByIdUser(tokenEntity.getUserEntity().getIdUser());

        return userEntity;
    }

    public boolean validateUserProfileByToken(String token) {
        UserEntity userEntity;

        userEntity = userRepository.getUserByToken(token);
        if(userEntity.getProfileEntity().getIdProfile().toString().equals("1")) {
            return true;
        } else {
            return false;
        }
    }

    public boolean setUserCommerceFirstLogin(Long idUser, String password, Integer days) {
        UserEntity userEntity;
        UserEntityExt userEntityExt;

        userEntityExt = userExtRepository.findByIdUser(idUser);
        userEntity = userRepository.findByUserName(userEntityExt.getUserName());


        if(userEntity == null || userEntityExt == null) {
            return false;
        }
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        userEntity.setPassword(passwordEncoder.encode(password));

        userEntity.setFirstLogin(false);
        userEntityExt.setFirstLogin(false);

        userRepository.save(userEntity);

        HistoryPasswordEntity historyPasswordEntity = new HistoryPasswordEntity();

        historyPasswordEntity.setUserEntity(userEntityExt);
        historyPasswordEntity.setRegisterDate(OffsetDateTime.now());
        OffsetDateTime expirationDate = OffsetDateTime.now(ZoneId.of("America/Caracas")).plusDays(days);
        historyPasswordEntity.setExpirationDate(expirationDate);
        historyPasswordEntity.setPassword(passwordEncoder.encode(password));

        historyPasswordRepository.save(historyPasswordEntity);
        userExtRepository.save(userEntityExt);
        return true;
    }

    @Transactional(transactionManager = "primaryTransactionManager")
    public Boolean createUserAdminExt(final String rif){

        try {
            ConfigurationEntity configurationEntity = configurationRepository.
                    findConfigurationEntityByIdConfiguration(1L);

            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            configurationEntity.setValue(configurationEntity.getValue());
            UserEntityExt userEntity= new UserEntityExt();
            userEntity.setFirstLogin(true);
            userEntity.setUserName(rif);
            userEntity.setProfileEntity(new ProfileEntity(1L));
            StatusUserEntity statusUserEntity = new StatusUserEntity();
            statusUserEntity.setIdStatusUser(1L);
            userEntity.setStatusUserEntity(statusUserEntity);

            userExtRepository.save(userEntity);


            return true;
        } catch (Exception e) {
            return false;
        }

    }

    @Transactional
    public Boolean createUserAdminExtGetWay(final String rif, final String apiKey, final String token,
                                            final Long idUser,final String email){

        try {

            String configurationValue = searchConfigurationById(1L, apiKey, token);

            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

            String password = passwordEncoder.encode(configurationValue);

            HttpHeaders headers = new HttpHeaders();
            RestTemplate restTemplate = new RestTemplate();

            headers.add("API_KEY", apiKey);
            headers.add("token", token);

            String uri = "http://localhost:8091/horizonte";

            LinkedHashMap<String, Object> requestBody = new LinkedHashMap<>();

            requestBody.put("commerceDocument", rif);
            requestBody.put("password", password);
            requestBody.put("idUser", idUser);
            requestBody.put("email", email);

            HttpEntity<LinkedHashMap<String, Object>> request = new HttpEntity<>(requestBody, headers);

            ResponseEntity<String> responseEntity = restTemplate.exchange(uri.
                    concat("/createUserAdminExt"), HttpMethod.POST, request, String.class);

            if (!Objects.requireNonNull(responseEntity.getBody()).contains("ERROR")) {
                logger.info("UserExt saved successfully");
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

    public String searchConfigurationById(final Long idConfiguration, final String apiKey, final String token) throws JsonProcessingException {

        HttpHeaders headers = new HttpHeaders();
        RestTemplate restTemplate = new RestTemplate();

        headers.add("API_KEY", apiKey);
        headers.add("token", token);

        String uri = "http://localhost:8091/horizonte";

        LinkedHashMap<String, Object> requestBody = new LinkedHashMap<>();

        requestBody.put("idConfiguration", idConfiguration);

        HttpEntity<LinkedHashMap<String, Object>> request = new HttpEntity<>(requestBody, headers);

        ResponseEntity<String> responseEntity = restTemplate.exchange(uri.
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

    public Response setResetPasswordUser(Long idUser, String password, Integer days) {

        UserEntity userEntity;
        UserEntityExt userEntityExt;
        Response response;
        HistoryPasswordEntity historyPasswordEntity = new HistoryPasswordEntity();
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        userEntity = userRepository.findByIdUser(idUser);
        userEntityExt = userExtRepository.findByUserName(userEntity.getUserName());

        if (historyPassword(password, idUser)) {
            return new Response("ERROR","La contraseña debe ser diferente a las últimas 5 registradas");
        }

        historyPasswordEntity.setPassword(passwordEncoder.encode(password));
        historyPasswordEntity.setUserEntity(new UserEntityExt(userEntity.getIdUser()));
        historyPasswordEntity.setRegisterDate(OffsetDateTime.now(ZoneId.of("America/Caracas")));
        OffsetDateTime expirationDate = setExpirationDate(days);
        historyPasswordEntity.setExpirationDate(expirationDate);
        historyPasswordRepository.save(historyPasswordEntity);

        userEntityExt.setTemporalPassword(null);
        saveInternalUser(userEntity, password);
        userExtRepository.save(userEntityExt);
        return new Response("SUCCESS", "Transacción exitosa");
    }

    @Transactional(transactionManager = "secondTransactionManager")
    public void saveInternalUser(UserEntity user, String password) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);
    }


    @Transactional(transactionManager = "primaryTransactionManager")
    public boolean historyPassword(String password, Long idUser) {

        List<HistoryPasswordEntity> historyPasswordEntities = historyPasswordRepository.getLastPasswordFromIdUser(PageRequest.of(0,5), idUser);

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        for (HistoryPasswordEntity historyPassword : historyPasswordEntities) {
            if (passwordEncoder.matches(password,historyPassword.getPassword())) {
                return true;
            }
        }
        return false;
    }
    public OffsetDateTime setExpirationDate(Integer days) {
        OffsetDateTime currentDate = OffsetDateTime.now(ZoneId.of("America/Caracas"));
        currentDate.plusDays(days);
        return currentDate;
    }

    public ContactPersonTo saveContactPersonInformation(HttpServletRequest httpServletRequest ,final Long idUser,
                                                        final String phoneNumberCommerce,final String contactPersonEmail,final String description){

        try {

            HttpHeaders headers = new HttpHeaders();
            RestTemplate restTemplate = new RestTemplate();

            headers.add("API_KEY", httpServletRequest.getHeader("API_KEY"));
            headers.add("token",  httpServletRequest.getHeader("token"));

            String uri = "http://localhost:8090/core";

            LinkedHashMap<String, Object> requestBody = new LinkedHashMap<>();

            requestBody.put("idUser", idUser);
            requestBody.put("phoneNumberCommerce", phoneNumberCommerce);
            requestBody.put("contactPersonEmail", contactPersonEmail);
            requestBody.put("description", description);

            HttpEntity<LinkedHashMap<String, Object>> request = new HttpEntity<>(requestBody,headers);


            ResponseEntity<String> responseEntity = restTemplate.exchange(uri.
                    concat("/saveSupportContact"), HttpMethod.POST, request, String.class);



            if (!Objects.requireNonNull(responseEntity.getBody()).contains("ERROR")) {
                ObjectMapper objectMapper = new ObjectMapper();


                LinkedHashMap<String, Object> responseMap = objectMapper.readValue(responseEntity.getBody(), LinkedHashMap.class);

                return new  ContactPersonTo(objectMapper, responseMap);


            } else {
                return null;
            }

        } catch (Exception e){
            logger.error(e.getMessage(), e);
            return null;
        }

    }

    @Async("asyncExecutor")
    public void sendSupportEmail(String commerceName, String subject, String phoneNumber, String email, String description
                                    , String sendTo, final String apiKey, final String token)
            throws MessagingException, IOException {


        String body = "";

        MimeMultipart multipart = new MimeMultipart();


        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("templates/supportContactEmail.html");

        if (inputStream != null) {

            try (Scanner scanner = new Scanner(inputStream, "UTF-8")) {
                StringBuilder contentBuilder = new StringBuilder();
                while (scanner.hasNextLine()) {
                    contentBuilder.append(scanner.nextLine());
                    contentBuilder.append(System.lineSeparator());
                }
                body = contentBuilder.toString();

            }
        }

        String logoUrl = searchConfigurationById(10L,apiKey,token);
//        emailGoogleService.setImageToMailFromResources("logo_intelipay.png", multipart);
        emailGoogleService.setImageToMailFromURL("logo_intelipay.png",multipart,logoUrl);
        body = body.replace("$commerceName", commerceName).replace("$phoneNumber",phoneNumber).replace("$email",email).replace("$description",description);

        Message message= emailGoogleService.setMailInitialConfiguration(sendTo,body,subject,multipart);

    }


    public Response searchProfileById(HttpServletRequest httpServletRequest ,final Long idProfile){

        try {

            HttpHeaders headers = new HttpHeaders();
            RestTemplate restTemplate = new RestTemplate();

            headers.add("API_KEY", httpServletRequest.getHeader("API_KEY"));
            headers.add("token",  httpServletRequest.getHeader("token"));

            String uri = "http://localhost:8090/core";

            LinkedHashMap<String, Object> requestBody = new LinkedHashMap<>();

            requestBody.put("idProfile", idProfile);

            HttpEntity<LinkedHashMap<String, Object>> request = new HttpEntity<>(requestBody,headers);


            ResponseEntity<String> responseEntity = restTemplate.exchange(uri.
                    concat("/getAllProfile"), HttpMethod.POST, request, String.class);



            if (!Objects.requireNonNull(responseEntity.getBody()).contains("ERROR")) {
                ObjectMapper objectMapper = new ObjectMapper();

                LinkedHashMap<String, Object> responseMap = objectMapper.readValue(responseEntity.getBody(), LinkedHashMap.class);
                List<LinkedHashMap<String, Object>> profileList = (List<LinkedHashMap<String, Object>>) responseMap.get("profileEntities");

                List<ProfileTo> profileTos = profileList.stream().map(map -> {
                    ProfileTo profileTo = new ProfileTo();
                    profileTo.setIdProfile(((Number) map.get("idProfile")).longValue());
                    profileTo.setProfileDescription((String) map.get("profileDescription"));
                    profileTo.setStatus(((Boolean) map.get("status")));
                    return profileTo;
                }).collect(Collectors.toList());

                return new Response("SUCCESS", profileTos);


            } else {
                return new Response("SUCCESS", "No data");
            }

        } catch (Exception e){
            logger.error(e.getMessage(), e);
            return null;
        }

    }

    @Transactional
    public Boolean createUserExtWithProfile(final String rif, final String apiKey, final String token,
                                            final Long idUser, final Long idProfile,final String email){

        try {

            String configurationValue = searchConfigurationById(1L, apiKey, token);

            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

            String password = passwordEncoder.encode(configurationValue);

            HttpHeaders headers = new HttpHeaders();
            RestTemplate restTemplate = new RestTemplate();

            headers.add("API_KEY", apiKey);
            headers.add("token", token);

            String uri = "http://localhost:8091/horizonte";

            LinkedHashMap<String, Object> requestBody = new LinkedHashMap<>();

            requestBody.put("commerceDocument", rif);
            requestBody.put("password", password);
            requestBody.put("idUser", idUser);
            requestBody.put("idProfile", idProfile);
            requestBody.put("email", email);

            HttpEntity<LinkedHashMap<String, Object>> request = new HttpEntity<>(requestBody, headers);

            ResponseEntity<String> responseEntity = restTemplate.exchange(uri.
                    concat("/createUserExtWithProfile"), HttpMethod.POST, request, String.class);

            if (!Objects.requireNonNull(responseEntity.getBody()).contains("ERROR")) {
                logger.info("UserExt saved successfully");
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

    public Response editProfileClient(final String fullName, final String apiKey, final String token,
                                            final String phoneNumber, final String email,final Long idUser){

        try {

            HttpHeaders headers = new HttpHeaders();
            RestTemplate restTemplate = new RestTemplate();

            headers.add("API_KEY", apiKey);
            headers.add("token", token);

            String uri = "http://localhost:8090/core";

            LinkedHashMap<String, Object> requestBody = new LinkedHashMap<>();

            requestBody.put("idUser", idUser);
            requestBody.put("fullName", fullName);
            requestBody.put("phoneNumber", phoneNumber);
            requestBody.put("email", email);


            HttpEntity<LinkedHashMap<String, Object>> request = new HttpEntity<>(requestBody, headers);

            ResponseEntity<String> responseEntity = restTemplate.exchange(uri.
                    concat("/editProfile"), HttpMethod.POST, request, String.class);

            if (!Objects.requireNonNull(responseEntity.getBody()).contains("ERROR")) {
                logger.info("Profile saved successfully");
                return new Response("SUCCESS","Transacción exitosa");
            } else {
                logger.error("Response Status: " + responseEntity.getStatusCode());
                return new Response("ERROR","No se pudo guardar la información");
            }

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
                return new Response("ERROR",e.getMessage());
        }

    }


    public Response searchUsersBlocked(HttpServletRequest httpServletRequest,final int index, final int quantity){

        try {

            LinkedHashMap<String, Object> requestBody = new LinkedHashMap<>();
            HttpHeaders headers = new HttpHeaders();
            RestTemplate restTemplate = new RestTemplate();
            requestBody.put("page", index);
            requestBody.put("quantity", quantity);

            headers.add("API_KEY", httpServletRequest.getHeader("API_KEY"));
            headers.add("token",  httpServletRequest.getHeader("token"));

            String uri = "http://localhost:8091/horizonte";



            HttpEntity<LinkedHashMap<String, Object>> request = new HttpEntity<>(requestBody,headers);


            ResponseEntity<String> responseEntity = restTemplate.exchange(uri.
                    concat("/getAllUsersBlocked"), HttpMethod.POST, request, String.class);



            if (!Objects.requireNonNull(responseEntity.getBody()).contains("ERROR")) {
                ObjectMapper objectMapper = new ObjectMapper();
                LinkedHashMap<String, Object> responseMap = objectMapper.readValue(responseEntity.getBody(), LinkedHashMap.class);
                LinkedHashMap<String,Object> paginatedResponse = new LinkedHashMap<>();
                LinkedHashMap linkedHashMapResponse = (objectMapper.convertValue(responseMap.get("userBlockedExtList"), LinkedHashMap.class));
                paginatedResponse.put("content", linkedHashMapResponse.get("content"));
                paginatedResponse.put("pageable", linkedHashMapResponse.get("pageable"));
                paginatedResponse.put("total", linkedHashMapResponse.get("totalElements"));
                paginatedResponse.put("totalPages", linkedHashMapResponse.get("totalPages"));

                return new Response("SUCCESS", paginatedResponse);


            } else {
                return new Response("SUCCESS", "No data");
            }

        } catch (Exception e){
            logger.error(e.getMessage(), e);
            return new Response("ERROR",e.getMessage());
        }

    }


    public Response getAllUsersByCommerce(final int index, final int quantity,
                                          final String rif, final String apiKey, final String token){

        try {

            ObjectMapper objectMapper = new ObjectMapper();
            HttpHeaders headers = new HttpHeaders();
            RestTemplate restTemplate = new RestTemplate();

            headers.add("API_KEY", apiKey);
            headers.add("token", token);

            String uri = "http://localhost:8090/core";

            LinkedHashMap<String, Object> requestBody = new LinkedHashMap<>();

            requestBody.put("rif", rif);
            requestBody.put("page", index);
            requestBody.put("quantity", quantity);



            HttpEntity<LinkedHashMap<String, Object>> request = new HttpEntity<>(requestBody, headers);

            ResponseEntity<String> responseEntity = restTemplate.exchange(uri.
                    concat("/getAllUsersByCommerce"), HttpMethod.POST, request, String.class);

            if (!Objects.requireNonNull(responseEntity.getBody()).contains("ERROR")) {
                LinkedHashMap<String, Object> responseMap = objectMapper.readValue(responseEntity.getBody(), LinkedHashMap.class);
                LinkedHashMap<String,Object> paginatedResponse = new LinkedHashMap<>();
                LinkedHashMap linkedHashMapResponse = (objectMapper.convertValue(responseMap.get("usersByCommerce"), LinkedHashMap.class));
                paginatedResponse.put("content", linkedHashMapResponse.get("content"));
                paginatedResponse.put("pageable", linkedHashMapResponse.get("pageable"));
                paginatedResponse.put("total", linkedHashMapResponse.get("totalElements"));
                paginatedResponse.put("totalPages", linkedHashMapResponse.get("totalPages"));


                return new Response("SUCCESS", paginatedResponse);
            } else {
                logger.error("Response Status: " + responseEntity.getStatusCode());
                return new Response("ERROR", "No se pudo guardar la información");
            }

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new Response("ERROR",e.getMessage());
        }

    }


    public Response unlockUsers(final long idUser, final String apiKey,final String token){

        try {

            LinkedHashMap<String, Object> requestBody = new LinkedHashMap<>();
            HttpHeaders headers = new HttpHeaders();
            RestTemplate restTemplate = new RestTemplate();
            requestBody.put("idUser", idUser);

            headers.add("API_KEY", apiKey);
            headers.add("token",  token);

            String uri = "http://localhost:8091/horizonte";



            HttpEntity<LinkedHashMap<String, Object>> request = new HttpEntity<>(requestBody,headers);


            ResponseEntity<String> responseEntity = restTemplate.exchange(uri.
                    concat("/unlocksUsersExternal"), HttpMethod.POST, request, String.class);



            if (!Objects.requireNonNull(responseEntity.getBody()).contains("ERROR")) {

                return new Response("SUCCESS", "Transacción exitosa");


            } else {
                return new Response("SUCCESS", "No data");
            }

        } catch (Exception e){
            logger.error(e.getMessage(), e);
            return new Response("ERROR",e.getMessage());
        }

    }

    public Response filterByDocument(final int index, final int quantity,
                                          final String filterField,final String endDate,final String rif,final int typeServices, final String apiKey, final String token){

        try {

            ObjectMapper objectMapper = new ObjectMapper();
            HttpHeaders headers = new HttpHeaders();
            RestTemplate restTemplate = new RestTemplate();

            headers.add("API_KEY", apiKey);
            headers.add("token", token);

            String uri = "http://localhost:8090/core";

            LinkedHashMap<String, Object> requestBody = new LinkedHashMap<>();

            requestBody.put("filterField", filterField);
            requestBody.put("filterFieldEnd",endDate);
            requestBody.put("rif",rif);
            requestBody.put("typeServices", typeServices);
            requestBody.put("page", index);
            requestBody.put("quantity", quantity);



            HttpEntity<LinkedHashMap<String, Object>> request = new HttpEntity<>(requestBody, headers);

            ResponseEntity<String> responseEntity = restTemplate.exchange(uri.
                    concat("/getFilterByUsersByCommerce"), HttpMethod.POST, request, String.class);

            if (!Objects.requireNonNull(responseEntity.getBody()).contains("ERROR")) {
                LinkedHashMap<String, Object> responseMap = objectMapper.readValue(responseEntity.getBody(), LinkedHashMap.class);
                LinkedHashMap<String,Object> paginatedResponse = new LinkedHashMap<>();
                LinkedHashMap linkedHashMapResponse = (objectMapper.convertValue(responseMap.get("userEntity"), LinkedHashMap.class));
                paginatedResponse.put("content", linkedHashMapResponse.get("content"));
                paginatedResponse.put("pageable", linkedHashMapResponse.get("pageable"));
                paginatedResponse.put("total", linkedHashMapResponse.get("totalElements"));
                paginatedResponse.put("totalPages", linkedHashMapResponse.get("totalPages"));


                return new Response("SUCCESS", paginatedResponse);
            } else {
                logger.error("Response Status: " + responseEntity.getStatusCode());
                return new Response("ERROR", "No se pudo guardar la información");
            }

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new Response("ERROR",e.getMessage());
        }

    }


    public Response blockUserById(final Long idUser, final String apiKey, final String token,
                                  final LogsClass logTo, final HttpServletRequest httpServletRequest, final String username){

        try {

            HttpHeaders headers = new HttpHeaders();
            RestTemplate restTemplate = new RestTemplate();

            headers.add("API_KEY", apiKey);
            headers.add("token", token);

            String uri = "http://localhost:8091/horizonte";

            LinkedHashMap<String, Object> requestBody = new LinkedHashMap<>();

            requestBody.put("username", username);


            HttpEntity<LinkedHashMap<String, Object>> request = new HttpEntity<>(requestBody, headers);

            ResponseEntity<String> responseEntity = restTemplate.exchange(uri.
                    concat("/blockUserExt"), HttpMethod.POST, request, String.class);

            if (!Objects.requireNonNull(responseEntity.getBody()).contains("ERROR")) {
                logger.info("block user successfully");
                return new Response("SUCCESS","Transacción exitosa");
            } else {
                logger.error("Response Status: " + responseEntity.getStatusCode());
                return new Response("ERROR","No se pudo guardar la información");
            }

        } catch (Exception e) {
            logger.error(e.getMessage(), e);

            manageLogs.severeErrorLogger(logTo,httpServletRequest,"Error: "+ logTo.
                    getMethodName() + ": "+ e, e,"user",false);

            return new Response("ERROR",e.getMessage());
        }

    }


    public Response getAllUsers(final int index, final int quantity,
                                final String apiKey, final String token){

        try {

            ObjectMapper objectMapper = new ObjectMapper();
            HttpHeaders headers = new HttpHeaders();
            RestTemplate restTemplate = new RestTemplate();

            headers.add("API_KEY", apiKey);
            headers.add("token", token);

            String uri = "http://localhost:8090/core";

            LinkedHashMap<String, Object> requestBody = new LinkedHashMap<>();

            requestBody.put("page", index);
            requestBody.put("quantity", quantity);



            HttpEntity<LinkedHashMap<String, Object>> request = new HttpEntity<>(requestBody, headers);

            ResponseEntity<String> responseEntity = restTemplate.exchange(uri.
                    concat("/getAllUsersByAdmin"), HttpMethod.POST, request, String.class);

            if (!Objects.requireNonNull(responseEntity.getBody()).contains("ERROR")) {
                LinkedHashMap<String, Object> responseMap = objectMapper.readValue(responseEntity.getBody(), LinkedHashMap.class);
                LinkedHashMap<String,Object> paginatedResponse = new LinkedHashMap<>();
                List <UsersRequiredTo>  usersRequiredTos = (List<UsersRequiredTo>) responseMap.get("usersByAdmin");
                Pageable pageList = PageRequest.of(index, quantity);
                int start = (int) pageList.getOffset();
                int end = Math.min((start + pageList.getPageSize()), usersRequiredTos.size());
                Page<UsersRequiredTo> usersToPage =  new
                        PageImpl<>(usersRequiredTos.subList(start, end), pageList, usersRequiredTos.size());


                return new Response("SUCCESS", usersToPage);
            } else {
                logger.error("Response Status: " + responseEntity.getStatusCode());
                return new Response("ERROR", "No se pudo guardar la información");
            }

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new Response("ERROR",e.getMessage());
        }

    }


    public String blockUserWithReasonStatus(final Long idUser, final String apiKey, final String token,final String reasonStatus){

        try {

            HttpHeaders headers = new HttpHeaders();
            RestTemplate restTemplate = new RestTemplate();

            headers.add("API_KEY", apiKey);
            headers.add("token", token);

            String uri = "http://localhost:8090/core";

            LinkedHashMap<String, Object> requestBody = new LinkedHashMap<>();

            requestBody.put("idUser", idUser);
            requestBody.put("reasonStatus", reasonStatus);


            HttpEntity<LinkedHashMap<String, Object>> request = new HttpEntity<>(requestBody, headers);

            ResponseEntity<String> responseEntity = restTemplate.exchange(uri.
                    concat("/blockUserIntWithReason"), HttpMethod.POST, request, String.class);

            if (!Objects.requireNonNull(responseEntity.getBody()).contains("ERROR")) {
                ObjectMapper objectMapper = new ObjectMapper();

                LinkedHashMap<String, Object> responseMap = objectMapper.readValue(responseEntity.
                        getBody(), LinkedHashMap.class);

                String username = responseMap.get("SUCCESS").toString();

                logger.info("block user successfully");
                return username;
            } else {
                logger.error("Response Status: " + responseEntity.getStatusCode());
                return null;
            }

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return null;
        }

    }

    public Boolean unlockUsersInt(final long idUser, final String apiKey,final String token,final String reasonStatus){

        try {

            LinkedHashMap<String, Object> requestBody = new LinkedHashMap<>();
            HttpHeaders headers = new HttpHeaders();
            RestTemplate restTemplate = new RestTemplate();
            requestBody.put("idUser", idUser);
            requestBody.put("reasonStatus", reasonStatus);

            headers.add("API_KEY", apiKey);
            headers.add("token",  token);

            String uri = "http://localhost:8090/core";



            HttpEntity<LinkedHashMap<String, Object>> request = new HttpEntity<>(requestBody,headers);


            ResponseEntity<String> responseEntity = restTemplate.exchange(uri.
                    concat("/unlockUserInt"), HttpMethod.POST, request, String.class);



            if (!Objects.requireNonNull(responseEntity.getBody()).contains("ERROR")) {

                return true;


            } else {
                return false;
            }

        } catch (Exception e){
            logger.error(e.getMessage(), e);
            return false;
        }

    }
}
