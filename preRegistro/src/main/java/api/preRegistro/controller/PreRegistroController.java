package api.preRegistro.controller;

import api.logsClass.LogsClass;
import api.logsClass.ManageLogs;
import api.preRegistro.entity.PreRegistroEntity;
import api.preRegistro.service.PreRegisterEmailService;
import api.preRegistro.service.PreRegistroService;
import api.preRegistro.util.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedHashMap;

@RestController
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST})
public class PreRegistroController {

    private static final Logger logger = LoggerFactory.getLogger(PreRegistroController.class);

    private final PreRegistroService preRegistroService;

    private final PreRegisterEmailService preRegisterEmailService;

    private final Environment environment;

    private final ManageLogs manageLogs;


    public PreRegistroController(PreRegistroService preRegistroService, PreRegisterEmailService preRegisterEmailService, Environment environment, ManageLogs manageLogs) {
        this.preRegistroService = preRegistroService;
        this.preRegisterEmailService = preRegisterEmailService;
        this.environment = environment;
        this.manageLogs = manageLogs;
    }


    @PostMapping("/savePreRegistroCommerce")
    public String savePreRegistroCommerce(@Valid @RequestBody PreRegistroEntity preRegistroEntity,
                                          HttpServletRequest httpServletRequest) {

        LogsClass logTo = new LogsClass(httpServletRequest, new Object() {}.
                getClass().getEnclosingMethod().getName());

        GsonBuilder b = new GsonBuilder();
        b.registerTypeAdapterFactory(HibernateProxyTypeAdapter.FACTORY);
        b.registerTypeAdapter(GregorianCalendar.class, new CalendarSerializer());
        b.registerTypeAdapter(OffsetDateTime.class, new OffsetDateTimeSerializer());
        Gson gson = b.setPrettyPrinting().create();

        Response response;
        if (!ValidationUtil.validateApiKey(httpServletRequest)) {
            manageLogs.errorLogger(logTo, httpServletRequest, "Error validando la apiKey",
                    "user", false);
            return gson.toJson(new Response("ERROR", "UNAUTHORIZED"));
        }

        try {

            if (!preRegistroService.verifyCommerceDocument(preRegistroEntity.getCommerceDocument())) {
                return gson.toJson(new Response("Error", "Rif registrado"));
            }

            response = preRegistroService.savePreRegistro(preRegistroEntity, httpServletRequest);
            String sendTo = preRegistroEntity.getCommerceEmail();
            String sendTo2 = preRegistroEntity.getContactPersonEmail();
            String fullName= preRegistroEntity.getContactPerson();
            preRegisterEmailService.generatePreregisterEmail(sendTo,sendTo2,fullName,httpServletRequest.getHeader("API_KEY"));

            manageLogs.infoLogger(logTo, httpServletRequest,
                    "Successfully: " + logTo.getMethodName(),
                    "user", false);

            return gson.toJson(response);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            manageLogs.severeErrorLogger(logTo, httpServletRequest, "Error: " + logTo.
                    getMethodName() + ": " + e, e, "user", false);

            return gson.toJson(new Response("ERROR", "Error en controller"));
        }
    }


    @GetMapping("/getPreRegistroById/{idPreRegistro}")
    public String getPreRegistroById(@PathVariable Long idPreRegistro,
                                     HttpServletRequest httpServletRequest,
                                     HttpServletResponse httpServletResponse) {

        LogsClass logTo = new LogsClass(httpServletRequest, new Object() {
        }.
                getClass().getEnclosingMethod().getName());

        GsonBuilder b = new GsonBuilder();
        b.registerTypeAdapterFactory(HibernateProxyTypeAdapter.FACTORY);
        b.registerTypeAdapter(GregorianCalendar.class, new CalendarSerializer());
        b.registerTypeAdapter(OffsetDateTime.class, new OffsetDateTimeSerializer());
        Gson gson = b.setPrettyPrinting().create();

        Response response;

        if (!ValidationUtil.validateApiKey(httpServletRequest)) {
            manageLogs.errorLogger(logTo, httpServletRequest, "Error validando la apiKey",
                    "user", false);
            return gson.toJson(new Response("ERROR", "UNAUTHORIZED"));
        }

        try {
            response = preRegistroService.getPreRegistroById(idPreRegistro, httpServletRequest);

          manageLogs.infoLogger(logTo,httpServletRequest,
                    "Successfully: " + logTo.getMethodName(),
                    "user",false);

          return gson.toJson(response);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            manageLogs.severeErrorLogger(logTo,httpServletRequest,"Error: "+ logTo.
                    getMethodName() + ": "+ e, e,"user",false);

            return gson.toJson(new Response("ERROR","Error en controller"));
        }
    }

    @GetMapping("/getAllPreRegistros")
    public String getAllPreRegistros(HttpServletRequest httpServletRequest) {

        LogsClass logTo = new LogsClass(httpServletRequest, new Object() {}.
                getClass().getEnclosingMethod().getName());

        GsonBuilder b = new GsonBuilder();
        b.registerTypeAdapterFactory(HibernateProxyTypeAdapter.FACTORY);
        b.registerTypeAdapter(GregorianCalendar.class, new CalendarSerializer());
        b.registerTypeAdapter(OffsetDateTime.class, new OffsetDateTimeSerializer());
        Gson gson = b.setPrettyPrinting().create();

        Response response;

        if(!ValidationUtil.validateApiKey(httpServletRequest)) {
            manageLogs.errorLogger(logTo, httpServletRequest, "Error validando la apiKey",
                    "user", false);
            return gson.toJson(new Response("ERROR", "UNAUTHORIZED"));
        }

        try {

            response = preRegistroService.getAllPreRegistros();

            manageLogs.infoLogger(logTo,httpServletRequest,
                    "Successfully: " + logTo.getMethodName(),
                    "user",false);

            return gson.toJson(response);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            manageLogs.severeErrorLogger(logTo,httpServletRequest,"Error: "+ logTo.
                    getMethodName() + ": "+ e, e,"user",false);

            return gson.toJson(new Response("ERROR","Error en controller"));
        }
    }

    @GetMapping("/api-version")
    public String apiVersion(){
        GsonBuilder b = new GsonBuilder();
        b.registerTypeAdapterFactory(HibernateProxyTypeAdapter.FACTORY);
        Gson gson = b.setPrettyPrinting().create();
        String date = new Date().toString();
        String apiVersion = environment.getProperty("app.name").concat(" ").concat(environment.getProperty("app.platform"))
                .concat(environment.getProperty("api.version"));
        Response response = new Response("SUCCESS", "Version ".concat(apiVersion).concat(" ").concat(date));
        return gson.toJson(response);
    }

    @PostMapping("/getAllPreRegisterCommerceByStatus")
    public String getAllPreRegisterCommerceByStatus(@RequestBody LinkedHashMap body,
                                     HttpServletRequest httpServletRequest,
                                     HttpServletResponse httpServletResponse) {

        LogsClass logTo = new LogsClass(httpServletRequest, new Object() {}.
                getClass().getEnclosingMethod().getName());

        GsonBuilder b = new GsonBuilder();
        b.registerTypeAdapterFactory(HibernateProxyTypeAdapter.FACTORY);
        b.registerTypeAdapter(GregorianCalendar.class, new CalendarSerializer());
        b.registerTypeAdapter(OffsetDateTime.class, new OffsetDateTimeSerializer());
        Gson gson = b.setPrettyPrinting().create();
        String token = httpServletRequest.getHeader("Token");
        String apiKey = httpServletRequest.getHeader("API_KEY");

        Response response;

        if(!ValidationUtil.validateApiKey(httpServletRequest)) {
            manageLogs.errorLogger(logTo, httpServletRequest, "Error validando la apiKey",
                    "user", false);
            return gson.toJson(new Response("ERROR", "UNAUTHORIZED"));
        }

        ObjectMapper objectMapper = new ObjectMapper();
        int index = objectMapper.convertValue(body.get("index"), int.class);
        int quantity = objectMapper.convertValue(body.get("quantity"), int.class);
        Boolean export = objectMapper.convertValue(body.get("export"), Boolean.class);
        String filter = objectMapper.convertValue(body.get("filter"), String.class);
        Integer typeFilter = objectMapper.convertValue(body.get("typeFilter"), Integer.class);
        String startDate = objectMapper.convertValue(body.get("startDate"), String.class);
        String endDate = objectMapper.convertValue(body.get("endDate"), String.class);
        Long idStatusPreregister = objectMapper.convertValue(body.get("idStatusPreRegister"), Long.class);

        try {

            response = preRegistroService.
                    getAllPreregistroFilter(apiKey, token,index, quantity,startDate, endDate, filter, typeFilter, export, idStatusPreregister);


            manageLogs.infoLogger(logTo, httpServletRequest,
                    "Successfully: " + logTo.getMethodName(),
                    "user", false);

            return gson.toJson(response);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            manageLogs.severeErrorLogger(logTo, httpServletRequest, "Error: " + logTo.
                    getMethodName() + ": " + e, e, "user", false);

            return gson.toJson(new Response("ERROR", "Error en controller"));
        }
    }

    @GetMapping("/getAllTypeCommerce")
    public String getAllTypeCommerce(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {

        LogsClass logTo = new LogsClass(httpServletRequest, new Object() {
        }.
                getClass().getEnclosingMethod().getName());

        GsonBuilder b = new GsonBuilder();
        b.registerTypeAdapterFactory(HibernateProxyTypeAdapter.FACTORY);
        b.registerTypeAdapter(GregorianCalendar.class, new CalendarSerializer());
        b.registerTypeAdapter(OffsetDateTime.class, new OffsetDateTimeSerializer());
        Gson gson = b.setPrettyPrinting().create();
        String apiKey = httpServletRequest.getHeader("API_KEY");
        Response response;

        try {

            response = preRegistroService.getAllTypeDocument(apiKey, httpServletRequest, logTo);

            manageLogs.infoLogger(logTo,httpServletRequest,
                    "Successfully: " + logTo.getMethodName(),
                    "user",false);

            return gson.toJson(response);

        } catch (Exception e){
            logger.error(e.getMessage(), e);
            manageLogs.severeErrorLogger(logTo,httpServletRequest,"Error: "+ logTo.
                    getMethodName() + ": "+ e, e,"user",false);

            return gson.toJson(new Response("ERROR","Error en controller"));
        }

    }

    @GetMapping("/getAllPlanByIdTypeCommerce/{idTypeCommerce}")
    public String getAllPlanByIdTypeCommerce(@PathVariable("idTypeCommerce") Long idTypeCommerce,
                                             HttpServletRequest httpServletRequest,
                                             HttpServletResponse httpServletResponse) {

        LogsClass logTo = new LogsClass(httpServletRequest, new Object() {}.
                getClass().getEnclosingMethod().getName());

        GsonBuilder b = new GsonBuilder();
        b.registerTypeAdapterFactory(HibernateProxyTypeAdapter.FACTORY);
        b.registerTypeAdapter(GregorianCalendar.class, new CalendarSerializer());
        b.registerTypeAdapter(OffsetDateTime.class, new OffsetDateTimeSerializer());
        Gson gson = b.setPrettyPrinting().create();
        String apiKey = httpServletRequest.getHeader("API_KEY");
        Response response;

        try {

             response = preRegistroService.getAllPlanByIdTypeCommerce(apiKey, idTypeCommerce,
                    httpServletRequest, logTo);

            manageLogs.infoLogger(logTo,httpServletRequest,
                    "Successfully: " + logTo.getMethodName(),
                    "user",false);

            return gson.toJson(response);

        } catch (Exception e){
            logger.error(e.getMessage(), e);
            manageLogs.severeErrorLogger(logTo,httpServletRequest,"Error: "+ logTo.
                    getMethodName() + ": "+ e, e,"user",false);

            return gson.toJson(new Response("ERROR","Error en controller"));
        }

    }

    @PostMapping("/validateRif")
    public String validateRif(@RequestBody LinkedHashMap requestData,
                              HttpServletRequest httpServletRequest,
                              HttpServletResponse httpServletResponse) {

        GsonBuilder b = new GsonBuilder();
        b.registerTypeAdapterFactory(HibernateProxyTypeAdapter.FACTORY);
        b.registerTypeAdapter(GregorianCalendar.class, new CalendarSerializer());
        b.registerTypeAdapter(OffsetDateTime.class, new OffsetDateTimeSerializer());
        Gson gson = b.setPrettyPrinting().create();
        String apiKey = httpServletRequest.getHeader("API_KEY");
        Response response;

        try {
            if(!ValidationUtil.validateApiKey(httpServletRequest)) {
                return gson.toJson(new Response("ERROR", "UNAUTHORIZED"));
            }

            ObjectMapper objectMapper = new ObjectMapper();
            String rif = objectMapper.convertValue(requestData.get("rif"), String.class);

            if(!preRegistroService.verifyCommerceDocument(rif)) {
                response = new Response("ERROR", "Rif registrado");
            } else {
                response = new Response("SUCCESS", "Rif valido");
            }

            return gson.toJson(response);

        } catch (IllegalArgumentException e) {
            String jsonResponse = gson.toJson(new Response("ERROR","Error en controller"));
            httpServletResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return gson.toJson(new Response("Error", jsonResponse));
        }
    }

    @PostMapping("/editProfileByPreRegister")
    public String editProfileByPreRegister(@RequestBody final String dataForm,
                                        final HttpServletRequest httpServletRequest) {

        LogsClass logTo = new LogsClass(httpServletRequest, new Object() {}.
                getClass().getEnclosingMethod().getName());

        GsonBuilder b = new GsonBuilder();
        b.registerTypeAdapterFactory(HibernateProxyTypeAdapter.FACTORY);
        b.registerTypeAdapter(GregorianCalendar.class, new CalendarSerializer());
        b.registerTypeAdapter(OffsetDateTime.class, new OffsetDateTimeSerializer());
        Gson gson = b.setPrettyPrinting().create();

        Response response;

        try {

            JSONObject jsonObject = new JSONObject(dataForm);
            Long idPreRegister = jsonObject.getLong("idPreRegister");
            String commerceName = jsonObject.getString("commerceName");
            String commerceDocument = jsonObject.getString("commerceDocument");
            String contactPerson = jsonObject.getString("contactPerson");
            String address = jsonObject.getString("address");
            Long idState = jsonObject.getLong("idState");
            String contactPersonEmail = jsonObject.getString("contactPersonEmail");
            String phoneNumber = jsonObject.getString("phoneNumberCommerce");
            Long idPlan = jsonObject.getLong("idPlan");

            response = preRegistroService.
                    editProfileByPreRegister(idPreRegister, commerceName, commerceDocument,
                            contactPerson, address, idState, contactPersonEmail, phoneNumber,
                            idPlan, httpServletRequest);

            manageLogs.infoLogger(logTo,httpServletRequest,
                    "Successfully: " + logTo.getMethodName(),
                    "user",false);

            return gson.toJson(response);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            manageLogs.severeErrorLogger(logTo,httpServletRequest,"Error: "+ logTo.
                    getMethodName() + ": "+ e, e,"user",false);

            return gson.toJson(new Response("ERROR","Error en controller"));
        }

    }

}