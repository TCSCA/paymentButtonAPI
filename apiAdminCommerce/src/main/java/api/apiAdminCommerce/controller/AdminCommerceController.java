package api.apiAdminCommerce.controller;


import api.apiAdminCommerce.converter.Base64ToMultipartFileConverter;

import api.apiAdminCommerce.entity.DirectionEntity;
import api.apiAdminCommerce.request.FileUploadRequest;
import api.apiAdminCommerce.request.ManualPaymentRequest;
import api.apiAdminCommerce.request.PreRegisterRequest;
import api.apiAdminCommerce.service.*;
import api.apiAdminCommerce.to.CommerceTo;
import api.apiAdminCommerce.to.ContactPersonTo;
import api.apiAdminCommerce.to.PreRegisterTo;
import api.apiAdminCommerce.to.ResultFileDetailTo;
import api.apiAdminCommerce.to.ResultFileTo;
import api.apiAdminCommerce.util.*;
import api.apiAdminCommerce.utilFile.FileStorageService;
import api.apiAdminCommerce.utilFile.UploadFileResponse;
import api.logsClass.LogsClass;
import api.logsClass.ManageLogs;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.apache.pdfbox.io.IOUtils;
import org.json.JSONObject;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.time.*;
import java.util.*;

@RestController
@RequestMapping(value = "/api")
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST})
public class AdminCommerceController {

    private static final Logger logger = LoggerFactory.getLogger(AdminCommerceController.class);

    private final AdminCommerceService adminCommerceService;

    private final CommerceService commerceService;

    private final ManageLog manageLog;

    private final UserService userService;

    private final ConfirmationCommerceEmailService confirmationCommerceEmailService;

    private final BankTransactionService bankTransactionService;

    private final FileStorageService fileStorageService;

    private final ManageLogs manageLogs;


    public AdminCommerceController(AdminCommerceService adminCommerceService,
                                   CommerceService commerceService, ManageLog manageLog,
                                   UserService userService,
                                   ConfirmationCommerceEmailService confirmationCommerceEmailService,
                                   BankTransactionService bankTransactionService,
                                   FileStorageService fileStorageService, ManageLogs manageLogs) {
        this.adminCommerceService = adminCommerceService;
        this.commerceService = commerceService;
        this.manageLog = manageLog;
        this.userService = userService;
        this.confirmationCommerceEmailService = confirmationCommerceEmailService;
        this.bankTransactionService = bankTransactionService;
        this.fileStorageService = fileStorageService;
        this.manageLogs = manageLogs;
    }

    @PostMapping("/changeStatusPreRegisterCommerce")
    public String changeStatusPreRegisterCommerceInt(@RequestBody PreRegisterRequest preRegisterRequest,
                                                     HttpServletRequest httpServletRequest,
                                                     HttpServletResponse httpServletResponse) {

        LogsClass logTo = new LogsClass(httpServletRequest, new Object() {}.
                getClass().getEnclosingMethod().getName());

        GsonBuilder b = new GsonBuilder();
        b.registerTypeAdapterFactory(HibernateProxyTypeAdapter.FACTORY);
        b.registerTypeAdapter(GregorianCalendar.class, new CalendarSerializer());
        b.registerTypeAdapter(OffsetDateTime.class, new OffsetDateTimeSerializer());
        String token = httpServletRequest.getHeader("Token");
        String apiKey = httpServletRequest.getHeader("API_KEY");
        Gson gson = new Gson();

        try {

            if (!adminCommerceService.
                    validateChargedRequiredFilesByIdPreRegister(preRegisterRequest.getIdPreregister(),
                            httpServletRequest, logTo) && preRegisterRequest.getStatusPreregister() == 2) {
                manageLogs.infoLogger(logTo,httpServletRequest,
                        "Error: existen recaudos obligatorios que no se han cargado",
                        "user",true);
                return gson.toJson(new Response("ERROR", "Existen recaudos obligatorios que no se han cargado"));
            }

            if(!adminCommerceService.setStatusPreRegisterExtGetWay(preRegisterRequest.getStatusPreregister(),
                    preRegisterRequest.getIdPreregister(), apiKey, token, logTo, httpServletRequest)) {
                manageLogs.errorLogger(logTo, httpServletRequest, "Error actualizando el estatus del pre-Registro",
                        "unlogged user", false);
                return gson.toJson(new Response("ERROR", "Error actualizando status"));
            }

            PreRegisterTo preRegisterTo = adminCommerceService.
                    getPreregisterByIdGetWay(preRegisterRequest.getIdPreregister(), apiKey, token);

            if (preRegisterRequest.getStatusPreregister() == 2L){

                    if(!commerceService.saveCommerceGetWay(preRegisterTo,
                            preRegisterRequest.getIdEconomicActivity(), preRegisterRequest.getIdCity(),
                            apiKey, token)) {
                        manageLogs.errorLogger(logTo, httpServletRequest, "Error salvando el comercio",
                                "unlogged user", false);
                        return gson.toJson(new Response("ERROR", "Error salvando el comercio"));
                    }

                    CommerceTo commerceTo = commerceService.getCommerceByDocumentAndStatus
                            (preRegisterTo.getCommerceDocument(), 2L, apiKey, token);

                    if(!commerceService.generateLicenceGetWay(commerceTo.getIdCommerce(), apiKey, token, preRegisterTo.getIdPlan())) {
                        manageLogs.errorLogger(logTo, httpServletRequest, "Error generando la licencia",
                                "unlogged user", false);
                        return gson.toJson(new Response("ERROR", "Error generando la licencia"));
                    }

                   Long idUser = commerceService.createUserAdminGetWay(commerceTo.
                           getCommerceDocument(), apiKey, token);

                    if(idUser == null) {
                        manageLogs.errorLogger(logTo, httpServletRequest, "Error creando el usuario interno",
                                "unlogged user", false);
                        return gson.toJson(new Response("ERROR", "Error creando el usuario interno"));
                    }

                    Long idClient = commerceService.
                            createClientGetWay(commerceTo, apiKey, token, httpServletRequest,
                                    idUser, preRegisterTo.getContactPersonDocument());

                    if(idClient == null) {
                        manageLogs.errorLogger(logTo, httpServletRequest, "Error creando cliente",
                                "unlogged user", false);
                        return gson.toJson(new Response("ERROR", "Error creando cliente"));
                    }

                    if(!userService.createUserAdminExtGetWay(commerceTo.getCommerceDocument(), apiKey, token, idUser,
                            commerceTo.getCommerceEmail())) {
                        manageLogs.errorLogger(logTo, httpServletRequest, "Error creando el usuario externo",
                                "unlogged user", false);
                        return gson.toJson(new Response("ERROR", "Error creando el usuario externo"));
                    }

                    List<ResultFileDetailTo> resultFileDetailTos = fileStorageService.
                            getAllResultFileByIdPreRegister(preRegisterTo.getIdPreRegistro(),
                                    commerceTo.getIdCommerce(), apiKey);

                    if(!fileStorageService.saveAllResultFileByCommerce(resultFileDetailTos, apiKey, token)) {
                        manageLogs.errorLogger(logTo, httpServletRequest, "Error salvando los recaudos subidos",
                                "unlogged user", false);
                        return gson.toJson(new Response("ERROR", "Error salvando los recaudos subidos"));
                    }

                    confirmationCommerceEmailService.sendConfirmationCommerceEmailInt(commerceTo.getIdCommerce(),
                            commerceTo.getCommerceEmail(), commerceTo.getCommerceDocument(),preRegisterTo.getContactPerson(),token,apiKey);

            } else {

                manageLogs.infoLogger(logTo,httpServletRequest, "Rechazando un pre-registro",
                        "user",true);

                ResponseEntity<String> responseEntity = adminCommerceService.changePreRegisterStatusGetWay(preRegisterRequest.getIdPreregister(),
                        preRegisterRequest.getStatusPreregister(), preRegisterRequest.getRejectMotive(),
                        apiKey, token, logTo, httpServletRequest);

                if(Objects.requireNonNull(responseEntity.getBody()).contains("ERROR")) {
                    manageLogs.errorLogger(logTo, httpServletRequest, "Error actualizando el estatus del preRegistro",
                            "unlogged user", false);
                    return gson.toJson(new Response("ERROR", "Error actualizando el estatus del preRegistro"));
                }

                confirmationCommerceEmailService.sendRejectCommerceEmail(preRegisterTo.getContactPersonEmail()
                        ,token,apiKey,"Intelipay",preRegisterTo.getCommerceDocument(),preRegisterTo.getCommerceName());
            }

            httpServletResponse.setStatus(HttpServletResponse.SC_OK);
            return gson.toJson(new Response("SUCCESS", "Transacción exitosa"));

        } catch (Exception e){
            manageLogs.severeErrorLogger(logTo,httpServletRequest,"Error: "+ logTo.
                    getMethodName() + ": "+ e, e,"user",false);

            return gson.toJson(new Response("ERROR", "En estos momentos estamos presentado inconvenientes. Favor comuníquese con el Administrador o intente mas tarde."));
        }

    }

    @PostMapping("/getAllCommerce")
    public String getAllCommerce(@RequestBody LinkedHashMap body,
                                 HttpServletRequest httpServletRequest,
                                 HttpServletResponse httpServletResponse) {

        LogTo logTo = new LogTo(httpServletRequest, "getAllCommerce");
        GsonBuilder b = new GsonBuilder();
        b.registerTypeAdapterFactory(HibernateProxyTypeAdapter.FACTORY);
        b.registerTypeAdapter(GregorianCalendar.class, new CalendarSerializer());
        b.registerTypeAdapter(OffsetDateTime.class, new OffsetDateTimeSerializer());
        Gson gson = b.setPrettyPrinting().create();

        Response response;

        ObjectMapper objectMapper = new ObjectMapper();
        int index = objectMapper.convertValue(body.get("index"), int.class);
        int quantity = objectMapper.convertValue(body.get("quantity"), int.class);
        Boolean export = objectMapper.convertValue(body.get("export"), Boolean.class);
        String filter = objectMapper.convertValue(body.get("filter"), String.class);
        Integer typeFilter = objectMapper.convertValue(body.get("typeFilter"), Integer.class);
        String startDate = objectMapper.convertValue(body.get("startDate"), String.class);
        String endDate = objectMapper.convertValue(body.get("endDate"), String.class);

        String token = httpServletRequest.getHeader("token");
        String apiKey = httpServletRequest.getHeader("API_KEY");

        try {

            response = commerceService.
                    getAllCommerceByFilter(apiKey, token,index, quantity,startDate, endDate, filter, typeFilter, export);

            return gson.toJson(response);

        } catch (Exception e) {
            String jsonResponse = gson.toJson(new Response("ERROR","Error en controller"));
            manageLog.severeErrorLogger(logTo,httpServletRequest,jsonResponse,e,false);
            return gson.toJson(new Response("Error", jsonResponse));
        }
    }

    @PostMapping("/getCommerceInformationByRif")
    public String getCommerceInformationByRif(@RequestBody LinkedHashMap requestBody,
                                                  HttpServletRequest httpServletRequest,
                                                  HttpServletResponse httpServletResponse) {

        LogTo logTo = new LogTo(httpServletRequest, "getCommerceInformationByRif");
        GsonBuilder b = new GsonBuilder();
        b.registerTypeAdapterFactory(HibernateProxyTypeAdapter.FACTORY);
        b.registerTypeAdapter(GregorianCalendar.class, new CalendarSerializer());
        b.registerTypeAdapter(OffsetDateTime.class, new OffsetDateTimeSerializer());
        Gson gson = b.setPrettyPrinting().create();
        ObjectMapper objectMapper = new ObjectMapper();

        Response response = null;

        try {
            Long idUser = objectMapper.convertValue(requestBody.get("idUser"), Long.class);
            String rif= objectMapper.convertValue(requestBody.get("rif"), String.class);

            if (idUser == null){
                response = commerceService.getCommerceInformationDetailByRif(rif,
                        httpServletRequest.getHeader("API_KEY"),
                        httpServletRequest.getHeader("token"));

            }else if (rif == null){
                response = commerceService.getCommerceInformationDetailByIdUser(idUser,
                        httpServletRequest.getHeader("API_KEY"),
                        httpServletRequest.getHeader("token"));

            }

            return gson.toJson(response);

        } catch (Exception e) {
            String jsonResponse = gson.toJson(new Response("ERROR","Error en controller"));
            manageLog.severeErrorLogger(logTo,httpServletRequest,jsonResponse,e,false);
            return gson.toJson(new Response("Error", jsonResponse));
        }

    }

    @PostMapping("/getProfileByCommerce")
    public String getProfileByCommerce(@RequestBody LinkedHashMap requestBody,
                                       HttpServletRequest httpServletRequest) {

        LogTo logTo = new LogTo(httpServletRequest, "getProfileByCommerce");
        GsonBuilder b = new GsonBuilder();
        b.registerTypeAdapterFactory(HibernateProxyTypeAdapter.FACTORY);
        b.registerTypeAdapter(GregorianCalendar.class, new CalendarSerializer());
        b.registerTypeAdapter(OffsetDateTime.class, new OffsetDateTimeSerializer());
        Gson gson = b.setPrettyPrinting().create();
        ObjectMapper objectMapper = new ObjectMapper();

        Response response = null;

        try {

            String rif = objectMapper.convertValue(requestBody.get("rif"), String.class);
            Long idUser = objectMapper.convertValue(requestBody.get("idUser"), Long.class);

            if (idUser == null){
                response = commerceService.getCommerceInformationByRif(rif);
            } else if (rif == null){
                response = commerceService.getCommerceInformationByIdUser(idUser);
            }

            logger.info("Commerce information successfully");
            return gson.toJson(response);

        } catch (Exception e) {
            String jsonResponse = gson.toJson(new Response("ERROR","Error en controller"));
            manageLog.severeErrorLogger(logTo,httpServletRequest,jsonResponse,e,false);
            return gson.toJson(new Response("Error", jsonResponse));
        }

    }

    @PostMapping("/editProfileByCommerce")
    public String editProfileByCommerce(@RequestBody final String dataForm,
                                        final HttpServletRequest httpServletRequest) {

        LogTo logTo = new LogTo(httpServletRequest, "editProfileByCommerce");
        GsonBuilder b = new GsonBuilder();
        b.registerTypeAdapterFactory(HibernateProxyTypeAdapter.FACTORY);
        b.registerTypeAdapter(GregorianCalendar.class, new CalendarSerializer());
        b.registerTypeAdapter(OffsetDateTime.class, new OffsetDateTimeSerializer());
        Gson gson = b.setPrettyPrinting().create();

        Response response;

        try {
            String activationDate = null;

            JSONObject jsonObject = new JSONObject(dataForm);
            Long idCommerce = jsonObject.getLong("idCommerce");
            String commerceName = jsonObject.getString("commerceName");
            String contactPerson = jsonObject.getString("contactPerson");
            String contactPersonEmail = jsonObject.getString("contactPersonEmail");
            String phoneNumber = jsonObject.getString("phoneNumberCommerce");
            Long idStatusCommerce = jsonObject.getLong("idStatusCommerce");
            Long idPlan = jsonObject.getLong("idPlan");
            Long idCity = jsonObject.getLong("idCity");
            Long idCommerceActivity = jsonObject.getLong("idCommerceActivity");
            String address = jsonObject.getString("address");
            if(jsonObject.getString("activationDate") != "") {
                 activationDate = jsonObject.getString("activationDate");
            }

            String reasonStatus = jsonObject.
                    getString("reasonStatus") != null ? jsonObject.getString("reasonStatus") : null;

            response = commerceService.
                    editProfileByCommerce(idCommerce, commerceName, contactPerson, contactPersonEmail,
                            phoneNumber, idStatusCommerce, idPlan, reasonStatus,idCity,address,idCommerceActivity, activationDate, httpServletRequest);

            return gson.toJson(response);

        } catch (Exception e) {
            String jsonResponse = gson.toJson(new Response("ERROR","Error en controller"));
            manageLog.severeErrorLogger(logTo,httpServletRequest,jsonResponse,e,false);
            return gson.toJson(new Response("Error", jsonResponse));
        }

    }

    @GetMapping("/getStatusCommerce/{idStatusCommerce}")
    public String getStatusCommerce(@PathVariable final Long idStatusCommerce,
                                    final HttpServletRequest httpServletRequest) {

        LogTo logTo = new LogTo(httpServletRequest, "getStatusCommerce");
        GsonBuilder b = new GsonBuilder();
        b.registerTypeAdapterFactory(HibernateProxyTypeAdapter.FACTORY);
        b.registerTypeAdapter(GregorianCalendar.class, new CalendarSerializer());
        b.registerTypeAdapter(OffsetDateTime.class, new OffsetDateTimeSerializer());
        Gson gson = b.setPrettyPrinting().create();

        Response response;

        try {

            response = commerceService.getStatusCommerce(idStatusCommerce, httpServletRequest);
            return gson.toJson(response);

        } catch (Exception e) {
            String jsonResponse = gson.toJson(new Response("ERROR","Error en controller"));
            manageLog.severeErrorLogger(logTo,httpServletRequest,jsonResponse,e,false);
            return gson.toJson(new Response("Error", jsonResponse));
        }

    }

    @GetMapping("/getAllPaymentMethods")
    public String getAllPaymentMethods(
                                          HttpServletRequest httpServletRequest,
                                          HttpServletResponse httpServletResponse) {

        LogTo logTo = new LogTo(httpServletRequest, "getAllPaymentMethods");
        GsonBuilder b = new GsonBuilder();
        b.registerTypeAdapterFactory(HibernateProxyTypeAdapter.FACTORY);
        b.registerTypeAdapter(GregorianCalendar.class, new CalendarSerializer());
        b.registerTypeAdapter(OffsetDateTime.class, new OffsetDateTimeSerializer());
        Gson gson = b.setPrettyPrinting().create();

        Response response = null;
        try {
            response = bankTransactionService.getAllPaymentMethods() ;
            return gson.toJson(response);

        } catch (Exception e) {
            String jsonResponse = gson.toJson(new Response("ERROR","Error en controller"));
            manageLog.severeErrorLogger(logTo,httpServletRequest,jsonResponse,e,false);
            return gson.toJson(new Response("Error", jsonResponse));
        }
    }

    @GetMapping("/getAllProductType")
    public String getAllProductType(
            HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse) {

        LogTo logTo = new LogTo(httpServletRequest, "getAllProductType");
        GsonBuilder b = new GsonBuilder();
        b.registerTypeAdapterFactory(HibernateProxyTypeAdapter.FACTORY);
        b.registerTypeAdapter(GregorianCalendar.class, new CalendarSerializer());
        b.registerTypeAdapter(OffsetDateTime.class, new OffsetDateTimeSerializer());
        Gson gson = b.setPrettyPrinting().create();

        Response response = null;
        try {
            response = bankTransactionService.getAllProductType();
            return gson.toJson(response);

        } catch (Exception e) {
            String jsonResponse = gson.toJson(new Response("ERROR","Error en controller"));
            manageLog.severeErrorLogger(logTo,httpServletRequest,jsonResponse,e,false);
            return gson.toJson(new Response("Error", jsonResponse));
        }
    }
    @PostMapping("/firstLoginUserAdmin")
    public String firstLoginUserAdmin(@RequestBody LinkedHashMap requestBody,
                           HttpServletRequest httpServletRequest,
                           HttpServletResponse httpServletResponse) {

        LogTo logTo = new LogTo(httpServletRequest,"firstLoginUserAdmin");
        GsonBuilder b = new GsonBuilder();
        b.registerTypeAdapterFactory(HibernateProxyTypeAdapter.FACTORY);
        b.registerTypeAdapter(GregorianCalendar.class, new CalendarSerializer());
        b.registerTypeAdapter(OffsetDateTime.class, new OffsetDateTimeSerializer());
        Gson gson = b.setPrettyPrinting().create();

        Response response;


        ObjectMapper objectMapper = new ObjectMapper();
        Long idUser = objectMapper.convertValue(requestBody.get("idUser"), Long.class);
        String password = objectMapper.convertValue(requestBody.get("password"), String.class);
        Integer days = objectMapper.convertValue(requestBody.get("days"), Integer.class);
        DirectionEntity directionEntity = objectMapper.convertValue(requestBody.get("DirectionEntity"), DirectionEntity.class);

        if(idUser == null){
            return gson.toJson(new Response("Error", "falta idUser"));
        }

        try {
            userService.setUserCommerceFirstLogin(idUser, password, days);
            return gson.toJson(new Response("SUCCESS", "Transacción exitosa"));

        } catch (Exception e) {
            manageLog.severeErrorLogger(logTo,httpServletRequest,e.getMessage(),e,true);
            return gson.toJson(new Response("Error", e));
        }
    }

    @GetMapping("/getAllEconomicActivities")
    public String getAllEconomicActivities(
            HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse) {

        LogTo logTo = new LogTo(httpServletRequest, "getAllEconomicActivities");
        GsonBuilder b = new GsonBuilder();
        b.registerTypeAdapterFactory(HibernateProxyTypeAdapter.FACTORY);
        b.registerTypeAdapter(GregorianCalendar.class, new CalendarSerializer());
        b.registerTypeAdapter(OffsetDateTime.class, new OffsetDateTimeSerializer());
        Gson gson = b.setPrettyPrinting().create();

        Response response = null;
        try {
            response = commerceService.getAllEconomicActivities();
            return gson.toJson(response);

        } catch (Exception e) {
            String jsonResponse = gson.toJson(new Response("ERROR","Error en controller"));
            manageLog.severeErrorLogger(logTo,httpServletRequest,jsonResponse,e,false);
            return gson.toJson(new Response("Error", jsonResponse));
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
            return gson.toJson(new Response("Error", "falta idUser"));
        }

        try {
            Response responseService = userService.setResetPasswordUser(idUser, password, days);
            manageLog.infoLogger(logTo,httpServletRequest,responseService.getData().toString(),false);
            return gson.toJson(responseService);

        } catch (Exception e) {
            manageLog.severeErrorLogger(logTo,httpServletRequest,e.getMessage(),e,true);
            return gson.toJson(new Response("Error", e));
        }

    }



    @PostMapping("/supportContact")
    public Response supportContact(@RequestBody LinkedHashMap requestBody,
                                HttpServletRequest httpServletRequest,
                                HttpServletResponse httpServletResponse){

        LogTo logTo = new LogTo(httpServletRequest,"supportContact");
        HttpHeaders headers = new HttpHeaders();
        String token = httpServletRequest.getHeader("Token");
        String apiKey = httpServletRequest.getHeader("API_KEY");
        GsonBuilder b = new GsonBuilder();
        b.registerTypeAdapterFactory(HibernateProxyTypeAdapter.FACTORY);
        b.registerTypeAdapter(GregorianCalendar.class, new CalendarSerializer());
        b.registerTypeAdapter(OffsetDateTime.class, new OffsetDateTimeSerializer());
        Gson gson = b.setPrettyPrinting().create();
        String subject = "Intelipay-Soporte";

        ObjectMapper objectMapper = new ObjectMapper();
        Long idUser = objectMapper.convertValue(requestBody.get("idUser"), Long.class);
        String phoneNumberCommerce = objectMapper.convertValue(requestBody.get("phoneNumberCommerce"), String.class);
        String contactPersonEmail = objectMapper.convertValue(requestBody.get("contactPersonEmail"), String.class);
        String description = objectMapper.convertValue(requestBody.get("description"), String.class);


        if(idUser == null){
            httpServletResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return new Response("Error", "falta idUser");
        }

        try {
            ContactPersonTo responseService = userService.saveContactPersonInformation(httpServletRequest,idUser,
                    phoneNumberCommerce,contactPersonEmail,description);

            String configurationValue = commerceService.searchConfigurationById(8L, apiKey, token);

            userService.sendSupportEmail(responseService.getCommerceName(),subject,responseService.getPhoneNumberCommerce()
                    ,responseService.getContactPersonEmail(),responseService.getDescription(),configurationValue,apiKey,token);

            manageLog.infoLogger(logTo,httpServletRequest,"Susccesfully",false);
            return new Response("SUCCESS", "Su información fue enviada exitosamente.<br>Nos comunicaremos con Usted a la brevedad.");


        } catch (Exception e) {
            httpServletResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            manageLog.severeErrorLogger(logTo,httpServletRequest,e.getMessage(),e,true);
            return new Response("Error", e);
        }

    }

    @PostMapping("/changeStatusManualPayment")
    public String changeStatusManualPayment(@RequestBody ManualPaymentRequest manualPaymentRequest,
                                            HttpServletRequest httpServletRequest,
                                            HttpServletResponse httpServletResponse) {

        LogTo logTo = new LogTo(httpServletRequest, "changeStatusManualPayment");
        GsonBuilder b = new GsonBuilder();
        b.registerTypeAdapterFactory(HibernateProxyTypeAdapter.FACTORY);
        b.registerTypeAdapter(GregorianCalendar.class, new CalendarSerializer());
        b.registerTypeAdapter(OffsetDateTime.class, new OffsetDateTimeSerializer());
        String token = httpServletRequest.getHeader("token");
        String apiKey = httpServletRequest.getHeader("API_KEY");
        Gson gson = new Gson();

        try {

           Response response = bankTransactionService.changeStatusBankTransactionManualPayment(
                    manualPaymentRequest.getStatusBankTransaction(),
                    manualPaymentRequest.getUsername(),
                    manualPaymentRequest.getIdBankTransaction(),
                    apiKey,
                    token);

            manageLog.infoLogger(logTo,httpServletRequest,"Susccesfully",true);
            httpServletResponse.setStatus(HttpServletResponse.SC_OK);
            return gson.toJson(response);

        } catch (Exception e){
            httpServletResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            manageLog.severeErrorLogger(logTo,httpServletRequest,e.getMessage(),e,false);
            return gson.toJson(new Response("ERROR", "Error actualizando status"));
        }

    }

    @PostMapping("/uploadFile")
    public UploadFileResponse uploadFile(@RequestBody FileUploadRequest request,
                                         HttpServletRequest httpServletRequest) {

        LogsClass logTo = new LogsClass(httpServletRequest, new Object() {}.
                getClass().getEnclosingMethod().getName());

        try {
            String reNameRequirement = fileStorageService.
                    getReNameByIdRequirement(request.getIdRequirement(), httpServletRequest);

            String originalFileName = request.getFileName();
            String fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));

            String newFileName = reNameRequirement + fileExtension;

            MultipartFile file = Base64ToMultipartFileConverter.
                    convert(request.getFile(), newFileName);

            LinkedHashMap<String, String> linkedHashMap = fileStorageService.
                    storeFile(file, request, httpServletRequest, logTo);

            String storedFileName = linkedHashMap.get("fileName");

            return new UploadFileResponse(storedFileName, file.getContentType(), file.getSize(),
                    "SUCCESS", "Transacción exitosa");

        } catch (IOException e) {
            logger.error(e.getMessage(), e);

            manageLogs.severeErrorLogger(logTo,httpServletRequest,"Error: "+ logTo.
                    getMethodName() + ": "+ e, e,"user",false);

            throw new RuntimeException("Failed to convert base64 to MultipartFile", e);
        }
    }

    @PostMapping("/uploadTermsAndConditions")
    public UploadFileResponse uploadTermsAndConditions(@RequestBody FileUploadRequest request,
                                         HttpServletRequest httpServletRequest) {

        String reNameRequirement;

        try {

            Long registerBy = fileStorageService.getIdUserByToken(httpServletRequest);

            if (request.getIdRequirement() == 1){
                reNameRequirement = "Terminos&Condiciones";
            } else {
                reNameRequirement = "PoliticasDePrivacidad";
            }

            String originalFileName = request.getFileName();
            String fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));

            String newFileName = reNameRequirement + fileExtension;

            MultipartFile file = Base64ToMultipartFileConverter.
                    convert(request.getFile(), newFileName);

            Boolean result = fileStorageService.
                    storeTermsAndConditionsFile(file, httpServletRequest, request, registerBy);

            if (result) {
                logger.info(reNameRequirement + " subido correctamente");
                return new UploadFileResponse("SUCCESS", "Transacción exitosa");
            } else {
                return new UploadFileResponse("ERROR", "No se ha podido subir el archivo");
            }

        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException("Failed to convert base64 to MultipartFile", e);
        }
    }

    @PostMapping("/download")
    public String downloadFile(@RequestBody final String dataForm,
                               HttpServletRequest httpServletRequest) {

        LogTo logTo = new LogTo(httpServletRequest,"download");
        GsonBuilder b = new GsonBuilder();
        b.registerTypeAdapterFactory(HibernateProxyTypeAdapter.FACTORY);
        b.registerTypeAdapter(GregorianCalendar.class, new CalendarSerializer());
        b.registerTypeAdapter(OffsetDateTime.class, new OffsetDateTimeSerializer());
        Gson gson = b.setPrettyPrinting().create();

        try {

            JSONObject jsonObject = new JSONObject(dataForm);
            Long idRequirement = Long.parseLong((new JSONObject(dataForm).get("idRequirement")).toString());
            Optional<ResultFileTo> resultFileOptional;

            if (jsonObject.has("commerceDocument")) {

                String commerceDocument = (new JSONObject(dataForm).get("commerceDocument").toString());

                manageLog.infoLogger(logTo,
                        httpServletRequest, "Buscar archivo del comercio: " +
                                commerceDocument, false);

                resultFileOptional = fileStorageService
                        .getResultFileByIdCommerceAndIdRequirement(commerceDocument,
                                idRequirement, httpServletRequest);

            } else {

                Long idPreRegister = Long.parseLong((new JSONObject(dataForm).
                        get("idPreRegister")).toString());

                manageLog.infoLogger(logTo,
                        httpServletRequest, "Buscar archivo del pre-registro: " +
                                idPreRegister, false);

                resultFileOptional = fileStorageService
                        .getResultFileByIdPreRegisterAndIdRequirement(idPreRegister,
                                idRequirement, httpServletRequest, logTo);

            }

            manageLog.infoLogger(logTo,
                    httpServletRequest, "FileOptional: " + resultFileOptional.toString(),
                    false);

            if (resultFileOptional.isPresent()) {
                ResultFileTo fileEntity = resultFileOptional.get();

                manageLog.infoLogger(logTo,
                        httpServletRequest, "Archivo a buscar: " + fileEntity.
                                getFileName(), false);

                Resource resource = fileStorageService.
                        loadFileAsResource(fileEntity.getFileName(), fileEntity.getRif());

                byte[] fileContent = IOUtils.toByteArray(resource.getInputStream());
                String mimeType = Files.probeContentType(resource.getFile().toPath());

                logger.info("Archivo encontrado correctamente");

                String base64Prefix = "data:" + mimeType + ";base64,";
                String base64Content = Base64.getEncoder().encodeToString(fileContent);

                return gson.toJson(new Response("SUCCESS", base64Prefix + base64Content));
            } else {
                logger.error("Archivo no encontrado");
                return gson.toJson(new Response("ERROR", "File not found"));
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return gson.toJson(new Response("ERROR", e.getMessage()));
        }
    }

    @PostMapping("/downloadQrByCommerce")
    public String downloadQrByCommerce(@RequestBody final String dataForm,
                               HttpServletRequest httpServletRequest) {

        LogTo logTo = new LogTo(httpServletRequest,"downloadQrByCommerce");
        GsonBuilder b = new GsonBuilder();
        b.registerTypeAdapterFactory(HibernateProxyTypeAdapter.FACTORY);
        b.registerTypeAdapter(GregorianCalendar.class, new CalendarSerializer());
        b.registerTypeAdapter(OffsetDateTime.class, new OffsetDateTimeSerializer());
        Gson gson = b.setPrettyPrinting().create();

        try {

            if(!ValidationUtil.validateApiKey(httpServletRequest)) {
                return gson.toJson(new Response("ERROR", "UNAUTHORIZED"));
            }


            Optional<ResultFileTo> resultFileOptional;

            String commerceDocument = (new JSONObject(dataForm).get("rif").toString());

            manageLog.infoLogger(logTo,
                        httpServletRequest, "Buscar archivo del comercio: " +
                                commerceDocument, false);

            resultFileOptional = fileStorageService
                        .getResultFileByIdCommerceAndIdRequirementQr(commerceDocument, httpServletRequest);

            manageLog.infoLogger(logTo,
                    httpServletRequest, "FileOptional: " + resultFileOptional.toString(),
                    false);

            if (resultFileOptional.isPresent()) {
                ResultFileTo fileEntity = resultFileOptional.get();

                manageLog.infoLogger(logTo,
                        httpServletRequest, "Archivo a buscar: " + fileEntity.
                                getFileName(), false);

                Resource resource = fileStorageService.
                        loadFileAsResource(fileEntity.getFileName(), fileEntity.getRif());

                byte[] fileContent = IOUtils.toByteArray(resource.getInputStream());
                String mimeType = Files.probeContentType(resource.getFile().toPath());

                logger.info("Archivo encontrado correctamente");

                /*String base64Prefix = "data:" + mimeType + ";base64,";*/
                String base64Content = Base64.getEncoder().encodeToString(fileContent);

                return gson.toJson(new Response("SUCCESS", base64Content));
            } else {
                logger.error("Archivo no encontrado");
                return gson.toJson(new Response("ERROR", "El comercio no posee Código QR registrado"));
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return gson.toJson(new Response("ERROR", e.getMessage()));
        }
    }

    @PostMapping("/downloadTermsAndConditions")
    public String downloadTermsAndConditions(@RequestBody final String dataForm,
                                             HttpServletRequest httpServletRequest) {

        LogTo logTo = new LogTo(httpServletRequest,"downloadTermsAndConditions");
        GsonBuilder b = new GsonBuilder();
        b.registerTypeAdapterFactory(HibernateProxyTypeAdapter.FACTORY);
        b.registerTypeAdapter(GregorianCalendar.class, new CalendarSerializer());
        b.registerTypeAdapter(OffsetDateTime.class, new OffsetDateTimeSerializer());
        Gson gson = b.setPrettyPrinting().create();

        try {

            JSONObject jsonObject = new JSONObject(dataForm);
            String termsOrPolicies = jsonObject.getString("termsOrPolicies");

            manageLog.infoLogger(logTo,
                    httpServletRequest, "Buscar ultimo archivo de " + termsOrPolicies + ": ",
                    false);

            Optional<ResultFileTo> resultFileOptional = fileStorageService
                    .getResultFileByTermsOrPolicies(termsOrPolicies, httpServletRequest, logTo);

            if (resultFileOptional.isPresent()) {
                ResultFileTo fileEntity = resultFileOptional.get();

                manageLog.infoLogger(logTo,
                        httpServletRequest, "Archivo a buscar: " + fileEntity.
                                getFileName(), false);

                Resource resource = fileStorageService.
                        loadFileAsResourceTermsOrPolicies(fileEntity.getFileName());

                if (resource == null) {
                    return gson.toJson(new Response("SUCCESS", "Archivo no encontrado"));
                }

                byte[] fileContent = IOUtils.toByteArray(resource.getInputStream());
                String mimeType = Files.probeContentType(resource.getFile().toPath());

                logger.info("Archivo encontrado correctamente");

                String base64Prefix = "data:" + mimeType + ";base64,";
                String base64Content = Base64.getEncoder().encodeToString(fileContent);

                return gson.toJson(new Response("SUCCESS", base64Prefix + base64Content));
            } else {
                logger.error("Archivo no encontrado");
                return gson.toJson(new Response("SUCCESS", "Archivo no encontrado"));
            }

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return gson.toJson(new Response("ERROR", e.getMessage()));
        }

    }

    @PostMapping("/getAllRequirement")
    @Operation(summary = "Metodo para devolver todos los recaudos",
            description = "Metodo para devolver todos los recaudos",
            security = {
                    @SecurityRequirement(name = "API_KEY"),
                    @SecurityRequirement(name = "token")
            })
    public String getAllRequirement(@RequestBody final String dataForm,
                                    HttpServletRequest httpServletRequest) {

        GsonBuilder b = new GsonBuilder();
        b.registerTypeAdapterFactory(HibernateProxyTypeAdapter.FACTORY);
        b.registerTypeAdapter(GregorianCalendar.class, new CalendarSerializer());
        b.registerTypeAdapter(OffsetDateTime.class, new OffsetDateTimeSerializer());
        Gson gson = b.setPrettyPrinting().create();

        Response response;

        try {

            Long idTypeCommerce = Long.parseLong((new JSONObject(dataForm).
                    get("idTypeCommerce")).toString());

            Long idPreRegister = Long.parseLong((new JSONObject(dataForm).
                    get("idPreRegister")).toString());

            response = adminCommerceService.getAllRequirement(idTypeCommerce, idPreRegister,
                    httpServletRequest);

            logger.info("All requirement found successfully");
            return gson.toJson(response);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return gson.toJson(new Response("ERROR",
                    "Error trayendo informacion de la transaccion"));
        }

    }

    @PostMapping("/getCommerceFilterByDate")
    public String getCommerceFilterByDate(@RequestBody LinkedHashMap body,
                                             HttpServletRequest httpServletRequest) {


        GsonBuilder b = new GsonBuilder();
        b.registerTypeAdapterFactory(HibernateProxyTypeAdapter.FACTORY);
        b.registerTypeAdapter(GregorianCalendar.class, new CalendarSerializer());
        b.registerTypeAdapter(OffsetDateTime.class, new OffsetDateTimeSerializer());
        Gson gson = b.setPrettyPrinting().create();
        String token = httpServletRequest.getHeader("Token");
        String apiKey = httpServletRequest.getHeader("API_KEY");

        Response response;


        try {

            ObjectMapper objectMapper = new ObjectMapper();

            String startDate = objectMapper.convertValue(body.get("startDate"), String.class);
            String endDate = objectMapper.convertValue(body.get("endDate"), String.class);
            int index = objectMapper.convertValue(body.get("index"), int.class);
            int quantity = objectMapper.convertValue(body.get("quantity"), int.class);
            Boolean export = objectMapper.convertValue(body.get("export"), Boolean.class);
            String filter = objectMapper.convertValue(body.get("filter"), String.class);

            if (export != null){
                response = adminCommerceService.getCommerceByDateExport(apiKey,httpServletRequest,index, quantity,startDate,endDate,export, "");

            }else {
                response = adminCommerceService.getCommerceByDate(apiKey,httpServletRequest,index, quantity,startDate,endDate, filter);
            }



            return gson.toJson(response);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return gson.toJson(new Response("ERROR",e.getMessage()));
        }
    }


    @PostMapping("/getRejectedFilterByDate")
    public String getRejectedFilterByDate(@RequestBody LinkedHashMap body,
                                             HttpServletRequest httpServletRequest) {


        GsonBuilder b = new GsonBuilder();
        b.registerTypeAdapterFactory(HibernateProxyTypeAdapter.FACTORY);
        b.registerTypeAdapter(GregorianCalendar.class, new CalendarSerializer());
        b.registerTypeAdapter(OffsetDateTime.class, new OffsetDateTimeSerializer());
        Gson gson = b.setPrettyPrinting().create();
        String token = httpServletRequest.getHeader("Token");
        String apiKey = httpServletRequest.getHeader("API_KEY");

        Response response;

        if(!ValidationUtil.validateApiKey(httpServletRequest)) {
            return gson.toJson(new Response("ERROR", "UNAUTHORIZED"));
        }

        try {

            ObjectMapper objectMapper = new ObjectMapper();

            String startDate = objectMapper.convertValue(body.get("startDate"), String.class);
            String endDate = objectMapper.convertValue(body.get("endDate"), String.class);
            int index = objectMapper.convertValue(body.get("index"), int.class);
            int quantity = objectMapper.convertValue(body.get("quantity"), int.class);
            Boolean export = objectMapper.convertValue(body.get("export"), Boolean.class);

            if (export != null){
                response=adminCommerceService.getRejectedByDateExport(apiKey,httpServletRequest,index, quantity,startDate,endDate,export);

            }else {
                response = adminCommerceService.getRejectedByDate(apiKey,httpServletRequest,index, quantity,startDate,endDate);
            }


            return gson.toJson(response);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return gson.toJson(new Response("ERROR",e.getMessage()));
        }
    }


    @PostMapping("/getAllCommerceInformation")
    public String getAllCommerceInformation(HttpServletRequest httpServletRequest) {


        GsonBuilder b = new GsonBuilder();
        b.registerTypeAdapterFactory(HibernateProxyTypeAdapter.FACTORY);
        b.registerTypeAdapter(GregorianCalendar.class, new CalendarSerializer());
        b.registerTypeAdapter(OffsetDateTime.class, new OffsetDateTimeSerializer());
        Gson gson = b.setPrettyPrinting().create();
        String apiKey = httpServletRequest.getHeader("API_KEY");

        Response response;

        if(!ValidationUtil.validateApiKey(httpServletRequest)) {
            return gson.toJson(new Response("ERROR", "UNAUTHORIZED"));
        }

        try {

                response=adminCommerceService.getAllCommerceInformation(apiKey,httpServletRequest);


            return gson.toJson(response);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return gson.toJson(new Response("ERROR",e.getMessage()));
        }
    }

}
