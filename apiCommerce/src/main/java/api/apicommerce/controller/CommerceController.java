package api.apicommerce.controller;

import api.apicommerce.request.CommerceConfigRequest;
import api.apicommerce.request.CommerceUpdateRequest;
import api.apicommerce.service.CommerceService;
import api.apicommerce.service.LicenceService;
import api.apicommerce.to.CommerceBankInformationTo;
import api.apicommerce.util.*;
import api.logsClass.LogsClass;
import api.logsClass.ManageLogs;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;


@RestController
@RequestMapping(value = "/api")
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST})
public class CommerceController {

    private final CommerceService commerceService;

    private final LicenceService licenceService;

    private final ManageLogs manageLogs;

    public CommerceController(CommerceService commerceService, LicenceService licenceService, ManageLogs manageLogs) {
        this.commerceService = commerceService;
        this.licenceService = licenceService;
        this.manageLogs = manageLogs;
    }

    @PostMapping("/saveCommerceConfig")
    public String saveCommerceConfig(
            @Valid @RequestBody CommerceConfigRequest commerceConfigRequest,
            HttpServletRequest httpServletRequest) {

        LogsClass logTo = new LogsClass(httpServletRequest, new Object() {}.
                getClass().getEnclosingMethod().getName());

        GsonBuilder b = new GsonBuilder();
        b.registerTypeAdapterFactory(HibernateProxyTypeAdapter.FACTORY);
        Gson gson = b.setPrettyPrinting().create();

        if(!ValidationUtil.validateApiKey(httpServletRequest)) {
            manageLogs.errorLogger(logTo, httpServletRequest, "Error validating apiKey",
                    "user", false);
            return gson.toJson(new Response("ERROR", "UNAUTHORIZED"));
        }

        try {

            Response response;

            response = licenceService.validateCommerceHasConfirmationCode(httpServletRequest, commerceConfigRequest.getRif());

            if(response.getStatus().equals("ERROR")) {
                manageLogs.errorLogger(logTo, httpServletRequest, "Error validating commerce and license with the confirmation code",
                        "user", false);
                return gson.toJson(response);
            }

            if(!commerceService.saveCommerceConfig(commerceConfigRequest)) {
                manageLogs.errorLogger(logTo, httpServletRequest, "Error saving the configuration of commerce",
                        "user", false);
                return gson.toJson(new Response("ERROR", "Error saving the configuration of commerce"));
            }

            String jsonResponse = gson.toJson(new Response("SUCCESS", "data guardada"));

            manageLogs.infoLogger(logTo,httpServletRequest,
                    "Successfully: " + logTo.getMethodName(),
                    "user",false);

            return jsonResponse;

        } catch (Exception e) {
            manageLogs.severeErrorLogger(logTo,httpServletRequest,"Error: "+ logTo.
                            getMethodName() + ": "+ e, e,"user",false);

            return gson.toJson(new Response("ERROR", "Error en controller "));
        }
    }

    @PostMapping("/editCommerceInformation")
    public String editCommerceInformation(
            @Valid @RequestBody CommerceUpdateRequest commerceUpdateRequest,
            HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse) {

        LogsClass logTo = new LogsClass(httpServletRequest, new Object() {}.
                getClass().getEnclosingMethod().getName());

        GsonBuilder b = new GsonBuilder();
        b.registerTypeAdapterFactory(HibernateProxyTypeAdapter.FACTORY);
        Gson gson = b.setPrettyPrinting().create();

        if(!ValidationUtil.validateApiKey(httpServletRequest)) {
            return gson.toJson(new Response("ERROR", "UNAUTHORIZED"));
        }

        Long idUser = commerceService.getIdUserByToken(httpServletRequest);

        String username = commerceService.getUsernameByIdUser(idUser,
                httpServletRequest.getHeader("API_KEY"),
                httpServletRequest.getHeader("token"),
                httpServletRequest);

        try {

            if(!commerceService.updateCommerceInfo(commerceUpdateRequest)) {
                manageLogs.errorLogger(logTo, httpServletRequest, "Error updating commerce information",
                        username, true);
                return gson.toJson(new Response("ERROR", "UNAUTHORIZED"));
            }

            String jsonResponse = gson.toJson(new Response("SUCCESS", "Transacción Exitosa"));

            manageLogs.infoLogger(logTo,httpServletRequest,
                    "Successfully: " + logTo.getMethodName(),
                    username,true);

            return jsonResponse;

        } catch (Exception e) {
            manageLogs.severeErrorLogger(logTo,httpServletRequest,"Error: "+ logTo.
                            getMethodName() + ": "+ e, e, username,true);

            return gson.toJson(new Response("ERROR", "Error en controller"));
        }

    }

    @PostMapping("/getCommerceBankInformation")
    public String getCommerceBankInformation(
            @RequestBody LinkedHashMap requestBody,
            HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse) {

        LogsClass logTo = new LogsClass(httpServletRequest, new Object() {}.
                getClass().getEnclosingMethod().getName());

        GsonBuilder b = new GsonBuilder();
        b.registerTypeAdapterFactory(HibernateProxyTypeAdapter.FACTORY);
        Gson gson = b.setPrettyPrinting().create();

        ObjectMapper objectMapper = new ObjectMapper();

        if(!ValidationUtil.validateApiKey(httpServletRequest)) {
            httpServletResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return gson.toJson(new Response("ERROR", "UNAUTHORIZED"));
        }

        String rif = objectMapper.convertValue(requestBody.get("rif"), String.class);

        try {
            CommerceBankInformationTo banCommerceInformation = commerceService.getCommerceBankInformationByRif(rif,
                    httpServletRequest.getHeader("API_KEY"),
                    httpServletRequest.getHeader("token"),
                    httpServletRequest);

            String jsonResponse = gson.toJson(new Response("SUCCESS", banCommerceInformation));

            manageLogs.infoLogger(logTo,httpServletRequest,
                    "Successfully: " + logTo.getMethodName(),
                    "unlogged user",false);

            return jsonResponse;


        } catch (Exception e) {
            manageLogs.severeErrorLogger(logTo,httpServletRequest,"Error: "+ logTo.
                    getMethodName() + ": "+ e, e, "unlogged user",false);

            return gson.toJson(new Response("ERROR", "Error en controller"));
        }

    }

    @PostMapping("/validateCommerceLicence")
    public String validateCommerceLicence(
            @RequestBody LinkedHashMap requestBody,
            HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse) {

        LogsClass logTo = new LogsClass(httpServletRequest, new Object() {}.
                getClass().getEnclosingMethod().getName());

        GsonBuilder b = new GsonBuilder();
        b.registerTypeAdapterFactory(HibernateProxyTypeAdapter.FACTORY);
        Gson gson = b.setPrettyPrinting().create();

        ObjectMapper objectMapper = new ObjectMapper();

        if(!ValidationUtil.validateApiKey(httpServletRequest)) {
            httpServletResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return gson.toJson(new Response("ERROR", "UNAUTHORIZED"));
        }

        String rif = objectMapper.convertValue(requestBody.get("rif"), String.class);

        try {
            Response response = commerceService.validateCommerceLicence(rif,
                    httpServletRequest.getHeader("API_KEY"),
                    httpServletRequest.getHeader("token") != null ?  httpServletRequest.getHeader("token") : "",
                    httpServletRequest);

            String jsonResponse = gson.toJson(response);

            manageLogs.infoLogger(logTo,httpServletRequest,
                    "Successfully: " + logTo.getMethodName(),
                    "unlogged user",false);

            return jsonResponse;


        } catch (Exception e) {
            manageLogs.severeErrorLogger(logTo,httpServletRequest,"Error: "+ logTo.
                    getMethodName() + ": "+ e, e, "unlogged user",false);

            return gson.toJson(new Response("ERROR", "Error en controller"));
        }

    }
}
