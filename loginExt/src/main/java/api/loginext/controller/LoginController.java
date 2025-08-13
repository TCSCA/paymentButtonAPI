package api.loginext.controller;


import api.loginext.entity.UserEntity;
import api.loginext.repository.UserRepository;
import api.loginext.service.EmailService;
import api.loginext.service.LoginService;
import api.loginext.service.UserService;
import api.loginext.to.UserTo;
import api.loginext.util.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.env.Environment;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import org.springframework.web.client.RestTemplate;

import java.time.OffsetDateTime;
import java.util.*;

@RestController
@RequestMapping(value = "/api")
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST})
public class LoginController {

    private final LoginService loginService;

    private final ManageLog manageLog;

    private final Environment environment;

    private final EmailService emailService;

    private final UserService userService;

    private final UserRepository userRepository;

    public LoginController(LoginService loginService, ManageLog manageLog, Environment environment, EmailService emailService, UserService userService, UserRepository userRepository) {
        this.loginService = loginService;
        this.manageLog = manageLog;
        this.environment = environment;
        this.emailService = emailService;
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @PostMapping("/api-version")
    public String apiVersion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse){
        GsonBuilder b = new GsonBuilder();
        b.registerTypeAdapterFactory(HibernateProxyTypeAdapter.FACTORY);
        Gson gson = b.setPrettyPrinting().create();
        String date = new Date().toString();
        String apiVersion = environment.getProperty("app.name").concat(" ").concat(environment.getProperty("app.platform"))
                .concat(environment.getProperty("api.version"));
        Response response = new Response("SUCCESS", "Version ".concat(apiVersion).concat(" ").concat(date));
        return gson.toJson(response);
    }

    @PostMapping("/validateCredentials")
    public String validateCredentials(@Valid @RequestBody UserTo userTo,
                                      HttpServletRequest httpServletRequest,
                                      HttpServletResponse httpServletResponse) {

        LogTo logTo = new LogTo(httpServletRequest,"validateCredentials");
        GsonBuilder b = new GsonBuilder();
        b.registerTypeAdapterFactory(HibernateProxyTypeAdapter.FACTORY);
        b.registerTypeAdapter(GregorianCalendar.class, new CalendarSerializer());
        b.registerTypeAdapter(OffsetDateTime.class, new OffsetDateTimeSerializer());
        Gson gson = b.setPrettyPrinting().create();

        Response response;

        if(!ValidationUtil.validateApiKey(httpServletRequest)) {
            return gson.toJson(new Response("ERROR", "Apikey Invalida"));
        }

        try {
            String validateCreds = loginService.validateCredentials(userTo);
            if(validateCreds.equals("invalid")) {
                manageLog.infoLogger(logTo,httpServletRequest, "ERROR",false);
                return gson.toJson(new Response("ERROR", "Usuario o Contraseña incorrectos"));
            }

            if(validateCreds.equals("suspended")) {
                manageLog.infoLogger(logTo,httpServletRequest, "ERROR",false);
                return gson.toJson(new Response("SUSPENDED", "Usuario suspendido"));
            }

            if(validateCreds.equals("tempValid")) {
                manageLog.infoLogger(logTo,httpServletRequest, "ERROR",false);
                Optional<UserEntity> user = userRepository.findByUserName(userTo.getUserName());
                Response responseLogin = new Response("TEMPASSWORD", user.get().getIdUser());
                responseLogin.setUser(user.get().getIdUser());
                return gson.toJson(responseLogin);
            }

            if(validateCreds.equals("expired")) {
                manageLog.infoLogger(logTo,httpServletRequest, "ERROR",false);
                return gson.toJson(new Response("EXPIRED", "Contraseña expirada. Debe utilizar la opción de Recuperar Contraseña"));
            }

            if(validateCreds.equals("blocked")) {
                manageLog.infoLogger(logTo,httpServletRequest, "ERROR",false);
                return gson.toJson(new Response("BLOCKED", "Usuario bloqueado"));
            }


            UserEntity userAuthenticated = loginService.loginUser(userTo);
            loginService.resetFailedAttempts(userAuthenticated);
            httpServletResponse.setStatus(HttpServletResponse.SC_OK);
            manageLog.infoLogger(logTo,httpServletRequest, "SUCCESS",false);
            Response responseLogin = new Response("SUCCESS", userAuthenticated.getToken());
            responseLogin.setUser(userAuthenticated.getIdUser());
            responseLogin.setFirstLogin(userAuthenticated.getFirstLogin());
            responseLogin.setTemporalPassword(userAuthenticated.getTemporalPassword());
            responseLogin.setUsername(userAuthenticated.getUserName());
            responseLogin.setProfile(userAuthenticated.getProfileEntity().getIdProfile());
            return gson.toJson(responseLogin);

        } catch (Exception e) {
           httpServletResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
           manageLog.severeErrorLogger(logTo,httpServletRequest,e.getMessage(),e,false);
           return gson.toJson(new Response("ERROR", "Error en autenticacion"));
        }

    }

    @PostMapping("/forgotPassword")
    public String forgotPassword(@Valid @RequestBody LinkedHashMap jsonRequest,
                               HttpServletRequest httpServletRequest,
                               HttpServletResponse httpServletResponse){

        final String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        final LogTo logTo = new LogTo(httpServletRequest,methodName);
        ObjectMapper objectMapper = new ObjectMapper();
        GsonBuilder b = new GsonBuilder();
        Gson gson = b.create();
        Response response;
        String token = httpServletRequest.getHeader("token");
        String confirmationKey = httpServletRequest.getHeader("CONFIRMATION_KEY");
        String apiKey = httpServletRequest.getHeader("API_KEY");
        String userName = objectMapper.convertValue(jsonRequest.get("username"),String.class);
        Map<String, String> placeHolders = new HashMap<>();

        try {

            if(!ValidationUtil.validateApiKey(httpServletRequest)) {
                return gson.toJson(new Response("ERROR", "UNAUTHORIZED"));
            }

            HttpHeaders headers = new HttpHeaders();
            RestTemplate restTemplate = new RestTemplate();
            String uri = "http://localhost:9000/email";

            LinkedHashMap<String, Object> requestBody = new LinkedHashMap<>();

            headers.add("API_KEY", apiKey);

            String temporalPassword = emailService.generatePassword();
            String logoUrl = userService.searchConfigurationById(10L,token,apiKey);

            placeHolders.put("$username", userName);
            placeHolders.put("$temporalPassword", temporalPassword);

            requestBody.put("username", userName);
            requestBody.put("urlLogo", logoUrl);
            requestBody.put("templatePath", "templates/forgotPassword.html");
            requestBody.put("placeholders", placeHolders);

            HttpEntity<LinkedHashMap<String, Object>> request = new HttpEntity<>(requestBody, headers);

            ResponseEntity<String> responseEntity = restTemplate.exchange(uri.
                    concat("/sendEmail"), HttpMethod.POST, request, String.class);

            if (!Objects.requireNonNull(responseEntity.getBody()).contains("ERROR")) {
                response = new Response("SUCCESS", responseEntity.getBody());
            } else {
                response = new Response("ERROR", responseEntity.getBody());
            }

            //response= emailService.generateForgotPasswordEmail(userName, httpServletRequest,token,confirmationKey);

            return gson.toJson(response);
        } catch (IllegalArgumentException e) {
            response =  new Response("ERROR", "ERROR En el servicio");
            manageLog.severeErrorLogger(logTo,httpServletRequest,gson.toJson(response),e,false);
            return gson.toJson(response);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

    }

    @PostMapping("/resetPassword")
    public String resetPassword(@RequestBody LinkedHashMap requestBody,
                                HttpServletRequest httpServletRequest,
                                HttpServletResponse httpServletResponse){

        LogTo logTo = new LogTo(httpServletRequest,"resetPassword");
        GsonBuilder b = new GsonBuilder();
        b.registerTypeAdapterFactory(HibernateProxyTypeAdapter.FACTORY);
        b.registerTypeAdapter(GregorianCalendar.class, new CalendarSerializer());
        b.registerTypeAdapter(OffsetDateTime.class, new OffsetDateTimeSerializer());
        Gson gson = b.setPrettyPrinting().create();

        ObjectMapper objectMapper = new ObjectMapper();
        Long idUser = objectMapper.convertValue(requestBody.get("idUser"), Long.class);
        String password = objectMapper.convertValue(requestBody.get("password"), String.class);
        Integer days = objectMapper.convertValue(requestBody.get("expirationDays"), Integer.class);

        if(idUser == null){
            httpServletResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return gson.toJson(new Response("Error", "falta idUser"));
        }

        try {
            Response responseService = userService.setResetPasswordUser(idUser, password, days);
            manageLog.infoLogger(logTo,httpServletRequest,responseService.getData().toString(),false);
            return gson.toJson(responseService);

        } catch (Exception e) {
            httpServletResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            manageLog.severeErrorLogger(logTo,httpServletRequest,e.getMessage(),e,true);
            return gson.toJson(new Response("Error", e));
        }

    }


}
