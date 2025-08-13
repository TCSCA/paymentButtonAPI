package api.apiAdminCommerce.controller;


import api.apiAdminCommerce.service.CommerceService;
import api.apiAdminCommerce.service.EmailService;
import api.apiAdminCommerce.service.UserService;
import api.apiAdminCommerce.to.CommerceTo;
import api.apiAdminCommerce.util.*;
import api.logsClass.LogsClass;
import api.logsClass.ManageLogs;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;
import java.util.GregorianCalendar;
import java.util.LinkedHashMap;

@RestController
@RequestMapping(value = "/api")
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST})
public class UserController {

    private final CommerceService commerceService;

    private final ManageLog manageLog;

    private final UserService userService;

    private final ManageLogs manageLogs;

    private final EmailService emailService;

    public UserController(CommerceService commerceService, ManageLog manageLog, UserService userService, ManageLogs manageLogs, EmailService emailService) {
        this.commerceService = commerceService;
        this.manageLog = manageLog;
        this.userService = userService;
        this.manageLogs = manageLogs;
        this.emailService = emailService;
    }

    @PostMapping("/createUser")
    public String createUser(@Valid @RequestBody LinkedHashMap requestBody,
                                                     HttpServletRequest httpServletRequest,
                                                     HttpServletResponse httpServletResponse) {

        LogTo logTo = new LogTo(httpServletRequest, "createUser");
        String token = httpServletRequest.getHeader("Token");
        String apiKey = httpServletRequest.getHeader("API_KEY");
        Gson gson = new Gson();
        ObjectMapper objectMapper = new ObjectMapper();
        String rif = objectMapper.convertValue(requestBody.get("rif"), String.class);
        Long idProfile = objectMapper.convertValue(requestBody.get("idProfile"), Long.class);
        String username = objectMapper.convertValue(requestBody.get("username"), String.class);
        String email = objectMapper.convertValue(requestBody.get("email"), String.class);
        String fullName = objectMapper.convertValue(requestBody.get("fullName"), String.class);
        String phoneNumber = objectMapper.convertValue(requestBody.get("phoneNumber"), String.class);
        String identificationDocument = objectMapper.convertValue(requestBody.get("identificationDocument"), String.class);

        try {


            Long idUser = commerceService.createUserInt(username, apiKey, token, idProfile);

            if (idUser == null) {
                manageLog.errorLogger(logTo, httpServletRequest, "Error creando el usuario interno");
                return gson.toJson(new Response("ERROR", "Usuario ya existe"));
            }

            if (rif != null) {

            CommerceTo commerceTo = commerceService.getCommerceByDocumentAndStatus
                    (rif, 2L, apiKey, token);

            Long idClient = commerceService.
                    createClientForUser(commerceTo, apiKey, token, httpServletRequest,
                            idUser, identificationDocument, fullName, phoneNumber, email);

            if (idClient == null) {
                manageLog.errorLogger(logTo, httpServletRequest, "Error creando cliente");
                return gson.toJson(new Response("ERROR", "Error creando cliente"));
            }
            }else {

                Long idAdministrative = commerceService.createAdminUser(apiKey, token, httpServletRequest,
                        idUser, identificationDocument, fullName, phoneNumber, email);

                if (idAdministrative == null){
                    manageLog.errorLogger(logTo, httpServletRequest, "Error creando usuario Administrativo");
                    return gson.toJson(new Response("ERROR", "Error creando usuario Administrativo"));
                }

            }
            if(!userService.createUserExtWithProfile(username, apiKey, token, idUser, idProfile , email)) {
                manageLog.errorLogger(logTo,httpServletRequest,"Error creando el usuario externo");
                return gson.toJson(new Response("ERROR", "Error creando el usuario externo"));
            }

            emailService.sendWelcomeEmail(fullName,username,"Comercio01.",email,apiKey,token);
            httpServletResponse.setStatus(HttpServletResponse.SC_OK);
            return gson.toJson(new Response("SUCCESS", "Usuario registrado exitosamense"));

        } catch (Exception e){
            manageLog.severeErrorLogger(logTo,httpServletRequest,e.getMessage(),e,false);
            return gson.toJson(new Response("ERROR", "Error creando el usuario"));
        }
    }

    @PostMapping("/getProfilesById")
    public String getAllTypeCommerce(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                                     @RequestBody LinkedHashMap requestBody) {

        GsonBuilder b = new GsonBuilder();
        b.registerTypeAdapterFactory(HibernateProxyTypeAdapter.FACTORY);
        b.registerTypeAdapter(GregorianCalendar.class, new CalendarSerializer());
        b.registerTypeAdapter(OffsetDateTime.class, new OffsetDateTimeSerializer());
        Gson gson = b.setPrettyPrinting().create();
        ObjectMapper objectMapper = new ObjectMapper();
        Long idProfile = objectMapper.convertValue(requestBody.get("idProfile"), Long.class);
        Response response;


        try {

            response = userService.searchProfileById(httpServletRequest,idProfile);
            return gson.toJson(response);

        } catch (Exception e){
            String jsonResponse = gson.toJson(new Response("ERROR","Error en controller"));
            httpServletResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return gson.toJson(new Response("Error", jsonResponse));
        }

    }

    @PostMapping("/editProfileClient")
    public String editProfileClient(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                                     @RequestBody LinkedHashMap requestBody) {

        GsonBuilder b = new GsonBuilder();
        b.registerTypeAdapterFactory(HibernateProxyTypeAdapter.FACTORY);
        b.registerTypeAdapter(GregorianCalendar.class, new CalendarSerializer());
        b.registerTypeAdapter(OffsetDateTime.class, new OffsetDateTimeSerializer());
        Gson gson = b.setPrettyPrinting().create();
        ObjectMapper objectMapper = new ObjectMapper();
        String token = httpServletRequest.getHeader("Token");
        String apiKey = httpServletRequest.getHeader("API_KEY");
        Long idUser = objectMapper.convertValue(requestBody.get("idUser"), Long.class);
        String email = objectMapper.convertValue(requestBody.get("email"), String.class);
        String fullName = objectMapper.convertValue(requestBody.get("fullName"), String.class);
        String phoneNumber = objectMapper.convertValue(requestBody.get("phoneNumber"), String.class);
        Response response;


        try {

            response = userService.editProfileClient(fullName,apiKey,token,phoneNumber,email,idUser);
            return gson.toJson(response);

        } catch (Exception e){
            String jsonResponse = gson.toJson(new Response("ERROR","Error en controller"));
            httpServletResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return gson.toJson(new Response("Error", jsonResponse));

        }

    }

    @PostMapping("/getUsersBlocked")
    public String getUsersBlocked(@RequestBody final String dataForm, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {

        GsonBuilder b = new GsonBuilder();
        b.registerTypeAdapterFactory(HibernateProxyTypeAdapter.FACTORY);
        b.registerTypeAdapter(GregorianCalendar.class, new CalendarSerializer());
        b.registerTypeAdapter(OffsetDateTime.class, new OffsetDateTimeSerializer());
        Gson gson = b.setPrettyPrinting().create();
        Response response;
        int index = Integer.parseInt((new JSONObject(dataForm).get("index")).toString());
        int quantity = Integer.parseInt((new JSONObject(dataForm).get("quantity")).toString());

        try {

            response = userService.searchUsersBlocked(httpServletRequest,index,quantity);
            return gson.toJson(response);

        } catch (Exception e){
            String jsonResponse = gson.toJson(new Response("ERROR","Error en controller"));
            httpServletResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return gson.toJson(new Response("Error", jsonResponse));
        }

    }

    @PostMapping("/getUsersByCommerce")
    public String getUsersByCommerce(@RequestBody final String dataForm,HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {

        GsonBuilder b = new GsonBuilder();
        b.registerTypeAdapterFactory(HibernateProxyTypeAdapter.FACTORY);
        b.registerTypeAdapter(GregorianCalendar.class, new CalendarSerializer());
        b.registerTypeAdapter(OffsetDateTime.class, new OffsetDateTimeSerializer());
        Gson gson = b.setPrettyPrinting().create();
        Response response;
        ObjectMapper objectMapper = new ObjectMapper();
        String rif = (new JSONObject(dataForm).get("rif")).toString();
        String token = httpServletRequest.getHeader("Token");
        String apiKey = httpServletRequest.getHeader("API_KEY");
        int index = Integer.parseInt((new JSONObject(dataForm).get("index")).toString());
        int quantity = Integer.parseInt((new JSONObject(dataForm).get("quantity")).toString());

        try {

            response = userService.getAllUsersByCommerce(index,quantity,rif,apiKey,token);
            return gson.toJson(response);

        } catch (Exception e){
            String jsonResponse = gson.toJson(new Response("ERROR","Error en controller"));
            return gson.toJson(new Response("Error", jsonResponse));
        }

    }


    @PostMapping("/unlockUsers")
    public String unlockUsersExt(@RequestBody final String dataForm, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {

        GsonBuilder b = new GsonBuilder();
        b.registerTypeAdapterFactory(HibernateProxyTypeAdapter.FACTORY);
        b.registerTypeAdapter(GregorianCalendar.class, new CalendarSerializer());
        b.registerTypeAdapter(OffsetDateTime.class, new OffsetDateTimeSerializer());
        Gson gson = b.setPrettyPrinting().create();
        Response response;
        String token = httpServletRequest.getHeader("Token");
        String apiKey = httpServletRequest.getHeader("API_KEY");
        long idUser = Long.parseLong((new JSONObject(dataForm).get("idUser")).toString());
        String reasonStatus = ((new JSONObject(dataForm).get("reasonStatus")).toString());

        try {

            Boolean statusInt = userService.unlockUsersInt(idUser,apiKey,token,reasonStatus);
            response = userService.unlockUsers(idUser,apiKey,token);
            return gson.toJson(response);

        } catch (Exception e){
            String jsonResponse = gson.toJson(new Response("ERROR","Error en controller"));
            return gson.toJson(new Response("Error", jsonResponse));
        }

    }

    @PostMapping("/getFilterByUsers")
    public String getFilterByDocument(@RequestBody final String dataForm,HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {

        GsonBuilder b = new GsonBuilder();
        b.registerTypeAdapterFactory(HibernateProxyTypeAdapter.FACTORY);
        b.registerTypeAdapter(GregorianCalendar.class, new CalendarSerializer());
        b.registerTypeAdapter(OffsetDateTime.class, new OffsetDateTimeSerializer());
        Gson gson = b.setPrettyPrinting().create();
        Response response;
        JSONObject jsonObject = new JSONObject(dataForm);

        String filterField = (new JSONObject(dataForm).get("filterField")).toString();
        String rif = jsonObject.has("rif") ? jsonObject.get("rif").toString() : null;
        String filterFieldEnd = jsonObject.has("endDate") ? jsonObject.get("endDate").toString() : null;
        String token = httpServletRequest.getHeader("Token");
        String apiKey = httpServletRequest.getHeader("API_KEY");
        int index = Integer.parseInt((new JSONObject(dataForm).get("index")).toString());
        int typeServices = Integer.parseInt((new JSONObject(dataForm).get("typeServices")).toString());
        int quantity = Integer.parseInt((new JSONObject(dataForm).get("quantity")).toString());

        try {

            response = userService.filterByDocument(index,quantity,filterField,filterFieldEnd,rif,typeServices,apiKey,token);
            return gson.toJson(response);

        } catch (Exception e){
            String jsonResponse = gson.toJson(new Response("ERROR","Error en controller"));
            return gson.toJson(new Response("Error", jsonResponse));
        }

    }

    @PostMapping("/blockUsersForCommerce")
    public String blockUsersForCommerce(@RequestBody final String dataForm,final HttpServletRequest httpServletRequest) {

        LogsClass logTo = new LogsClass(httpServletRequest, new Object() {}.
                getClass().getEnclosingMethod().getName());

        GsonBuilder b = new GsonBuilder();
        b.registerTypeAdapterFactory(HibernateProxyTypeAdapter.FACTORY);
        b.registerTypeAdapter(GregorianCalendar.class, new CalendarSerializer());
        b.registerTypeAdapter(OffsetDateTime.class, new OffsetDateTimeSerializer());
        Gson gson = b.setPrettyPrinting().create();
        Response response = null;
        Long idUser = Long.valueOf((new JSONObject(dataForm).get("idUser")).toString());
        String reasonStatus = ((new JSONObject(dataForm).get("reasonStatus")).toString());
        String token = httpServletRequest.getHeader("Token");
        String apiKey = httpServletRequest.getHeader("API_KEY");


        try {
            String username = userService.blockUserWithReasonStatus(idUser,apiKey,token,reasonStatus);
            if(username != null) {
                response = userService.blockUserById(idUser,apiKey,token, logTo, httpServletRequest, username);
            }

            manageLogs.infoLogger(logTo,httpServletRequest,
                    "Usuario suspendido: " + idUser,
                    "user",false);

            return gson.toJson(response);

        } catch (Exception e){
            String jsonResponse = gson.toJson(new Response("ERROR","Error en controller"));

            manageLogs.severeErrorLogger(logTo,httpServletRequest,"Error: "+ logTo.
                    getMethodName() + ": "+ e, e,"user",false);

            return gson.toJson(new Response("Error", jsonResponse));
        }

    }


    @PostMapping("/getAllUsers")
    public String getAllUsers(@RequestBody final String dataForm,HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {

        GsonBuilder b = new GsonBuilder();
        b.registerTypeAdapterFactory(HibernateProxyTypeAdapter.FACTORY);
        b.registerTypeAdapter(GregorianCalendar.class, new CalendarSerializer());
        b.registerTypeAdapter(OffsetDateTime.class, new OffsetDateTimeSerializer());
        Gson gson = b.setPrettyPrinting().create();
        Response response;
        ObjectMapper objectMapper = new ObjectMapper();
        String token = httpServletRequest.getHeader("Token");
        String apiKey = httpServletRequest.getHeader("API_KEY");
        int index = Integer.parseInt((new JSONObject(dataForm).get("index")).toString());
        int quantity = Integer.parseInt((new JSONObject(dataForm).get("quantity")).toString());

        try {

            response = userService.getAllUsers(index,quantity,apiKey,token);
            return gson.toJson(response);

        } catch (Exception e){
            String jsonResponse = gson.toJson(new Response("ERROR","Error en controller"));
            return gson.toJson(new Response("Error", jsonResponse));
        }

    }
}
