package api.apiPaymentButton.controller;

import api.apiPaymentButton.request.GatewayRequest;
import api.apiPaymentButton.service.GatewayService;
import api.apiPaymentButton.service.UrlService;
import api.apiPaymentButton.util.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import feign.FeignException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONObject;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.FileNotFoundException;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.GregorianCalendar;
import java.util.LinkedHashMap;
import java.util.Optional;

@RestController
@RequestMapping(value = "/api")
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST})
public class IntGatewayController {

    private final GatewayRequest gatewayRequest;

    private final GatewayService gatewayService;

    private final UrlService urlService;

    private final ValidationUtil validationUtil;

    private final ManageLog manageLog;


    public IntGatewayController(GatewayRequest gatewayRequest, GatewayService gatewayService, UrlService urlService, ValidationUtil validationUtil, ManageLog manageLog) {
        this.gatewayRequest = gatewayRequest;
        this.gatewayService = gatewayService;
        this.urlService = urlService;
        this.validationUtil = validationUtil;
        this.manageLog = manageLog;
    }


    @PostMapping("/{dynamicEndpoint}")
    public String gatewayEndPointPost(@PathVariable String dynamicEndpoint,
                                      @RequestBody  Optional<LinkedHashMap> jsonRequest,
                                      HttpServletRequest httpServletRequest,
                                      HttpServletResponse httpServletResponse) {

        LogTo logTo = new LogTo(httpServletRequest, dynamicEndpoint);
        GsonBuilder b = new GsonBuilder();
        b.registerTypeAdapterFactory(HibernateProxyTypeAdapter.FACTORY);
        b.registerTypeAdapter(GregorianCalendar.class, new CalendarSerializer());
        b.registerTypeAdapter(OffsetDateTime.class, new OffsetDateTimeSerializer());
        Gson gson = b.setPrettyPrinting().create();

        Response response;

        String apikey = httpServletRequest.getHeader(validationUtil.apiKeyHeader);
        String token = httpServletRequest.getHeader("token");
        String confirmationKey = httpServletRequest.getHeader("CONFIRMATION_KEY");
        if(apikey == null) {
            httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return gson.toJson(new Response("ERROR", "UNAUTHORIZED"));
        }

        try {
            manageLog.infoLogger(logTo,httpServletRequest,"IntGateway method busca ruta",false);
            URI basePathUri = urlService.getUrlFromFile(dynamicEndpoint, manageLog);
            if(basePathUri == null) {
                httpServletResponse.setStatus(HttpServletResponse.SC_BAD_GATEWAY);
                return gson.toJson(new Response("ERROR", "Invalid Method"));
            }
            if(token == null && confirmationKey == null) {
                String linkedHashMap = gatewayRequest.dynamicEndpointRequest(basePathUri,dynamicEndpoint,apikey,jsonRequest);
                manageLog.infoLogger(logTo,httpServletRequest,linkedHashMap,false);
                return linkedHashMap;
            }

            if(confirmationKey != null) {
                String linkedHashMap = gatewayRequest.dynamicEndpointRequestConfirmationKey(basePathUri,dynamicEndpoint,apikey,
                        confirmationKey, jsonRequest);
                logResponse(linkedHashMap, logTo, httpServletRequest, manageLog,dynamicEndpoint);
                return linkedHashMap;
            } else {
                String linkedHashMap = gatewayRequest.dynamicEndpointRequestToken(basePathUri,dynamicEndpoint,apikey,
                        token, jsonRequest);
                logResponse(linkedHashMap, logTo, httpServletRequest, manageLog,dynamicEndpoint);
                return linkedHashMap;
            }

        } catch (FeignException.FeignClientException | FileNotFoundException e) {
            httpServletResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response = gatewayService.getResponseGatewayByHttpStatus(e, dynamicEndpoint, httpServletResponse);
            manageLog.severeErrorLogger(logTo,httpServletRequest,e.getMessage(),e,false);
        }

        return gson.toJson(response);
    }

    @GetMapping("/{dynamicEndpoint}")
    public String gatewayEndPointGet(@PathVariable String dynamicEndpoint,
                                     @RequestBody Optional<LinkedHashMap> jsonRequest,
                                     HttpServletRequest httpServletRequest,
                                     HttpServletResponse httpServletResponse) {


        LogTo logTo = new LogTo(httpServletRequest,dynamicEndpoint);
        GsonBuilder b = new GsonBuilder();
        b.registerTypeAdapterFactory(HibernateProxyTypeAdapter.FACTORY);
        b.registerTypeAdapter(GregorianCalendar.class, new CalendarSerializer());
        b.registerTypeAdapter(OffsetDateTime.class, new OffsetDateTimeSerializer());
        Gson gson = b.setPrettyPrinting().create();

        Response response;
        String microServiceResponse = null;

        String apikey = httpServletRequest.getHeader(validationUtil.apiKeyHeader);
        String token = httpServletRequest.getHeader("token");
        if(token == null) {
            httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return gson.toJson(new Response("ERROR", "UNAUTHORIZED"));
        }

        try {
            URI basePathUri = urlService.getUrlFromFile(dynamicEndpoint, manageLog);
            if(basePathUri == null) {
                httpServletResponse.setStatus(HttpServletResponse.SC_BAD_GATEWAY);
                return gson.toJson(new Response("ERROR", "Invalid Method"));
            }
            if(dynamicEndpoint.contains("-")) {
                String[] paramArr = dynamicEndpoint.split("-", 2);
                dynamicEndpoint = paramArr[0];
                dynamicEndpoint = dynamicEndpoint.replace("-", "/");

                microServiceResponse = gatewayRequest.
                        dynamicEndpointRequestGetTokenWithParam(basePathUri,dynamicEndpoint,paramArr[1], apikey, token);
            } else {
                microServiceResponse = gatewayRequest.dynamicEndpointRequestGetToken(basePathUri, dynamicEndpoint, apikey,
                                                                                    token);
            }

            manageLog.infoLogger(logTo,httpServletRequest,microServiceResponse,false);
            return microServiceResponse;

        } catch (FeignException.FeignClientException | FileNotFoundException e) {
            httpServletResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response = gatewayService.getResponseGatewayByHttpStatus(e, dynamicEndpoint, httpServletResponse);
            manageLog.severeErrorLogger(logTo,httpServletRequest,e.getMessage(),e,false);
        }

        return gson.toJson(response);
    }

    private void logResponse(String response, LogTo logTo, HttpServletRequest request, ManageLog manageLog, String methodName) {
        try {
            JSONObject jsonResponse = new JSONObject(response);
            String status = jsonResponse.optString("status");

            if ("SUCCESS".equalsIgnoreCase(status)) {
                manageLog.infoLogger(logTo, request, "Method: " + methodName + " -Status: SUCCESS", false);
            } else {
                manageLog.infoLogger(logTo, request, "Method: " + methodName + " -Response: " + response, false);
            }
        } catch (Exception e) {
            manageLog.infoLogger(logTo, request, "Error parsing response: " + e.getMessage(), true);
        }
    }
}
