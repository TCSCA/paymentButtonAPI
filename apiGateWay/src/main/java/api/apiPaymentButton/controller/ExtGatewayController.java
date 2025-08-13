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
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.OffsetDateTime;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedHashMap;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST})
public class ExtGatewayController {

    private final GatewayRequest gatewayRequest;

    private final Environment environment;

    private final GatewayService gatewayService;

    private final UrlService urlService;

    private final ValidationUtil validationUtil;

    private final ManageLog manageLog;

    public ExtGatewayController(GatewayRequest gatewayRequest, Environment environment, GatewayService gatewayService, UrlService urlService, ValidationUtil validationUtil, ManageLog manageLog) {
        this.gatewayRequest = gatewayRequest;
        this.environment = environment;
        this.gatewayService = gatewayService;
        this.urlService = urlService;
        this.validationUtil = validationUtil;
        this.manageLog = manageLog;
    }


    @PostMapping("/{dynamicEndpoint}")
    public String getGatewayEndPointPost(@PathVariable String dynamicEndpoint,
                                         @RequestBody  Optional<LinkedHashMap> jsonRequest,
                                         HttpServletRequest httpServletRequest,
                                         HttpServletResponse httpServletResponse) {

        LogTo logTo = new LogTo(httpServletRequest, dynamicEndpoint);
        GsonBuilder b = new GsonBuilder();
        b.registerTypeAdapterFactory(HibernateProxyTypeAdapter.FACTORY);
        b.registerTypeAdapter(GregorianCalendar.class, new CalendarSerializer());
        b.registerTypeAdapter(OffsetDateTime.class, new OffsetDateTimeSerializer());
        Gson gson = b.setPrettyPrinting().create();
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();

        Response response;
        String apikey = httpServletRequest.getHeader(validationUtil.apiKeyHeader);

        try {
            URI basePathUri = urlService.getUrlFromRoutePropertie(dynamicEndpoint);
            if(basePathUri == null) {
                httpServletResponse.setStatus(HttpServletResponse.SC_BAD_GATEWAY);
                return gson.toJson(new Response("ERROR", "Invalid Method"));
            }

            String linkedHashMap = gatewayRequest.dynamicEndpointRequest(basePathUri,dynamicEndpoint,apikey
                                                                         ,jsonRequest);
            logResponse(linkedHashMap, logTo, httpServletRequest, manageLog,dynamicEndpoint);
            return linkedHashMap;

        } catch (FeignException.FeignClientException e) {
            httpServletResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response = gatewayService.getResponseGatewayByHttpStatus(e, dynamicEndpoint, httpServletResponse);
            manageLog.severeErrorLogger(logTo,httpServletRequest,e.getMessage(),e,false);
        }
        return gson.toJson(response);
    }

    @GetMapping("/api-version")
    public String apiVersion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse){
        GsonBuilder b = new GsonBuilder();
        b.registerTypeAdapterFactory(HibernateProxyTypeAdapter.FACTORY);
        Gson gson = b.setPrettyPrinting().create();
        String date = new Date().toString();
        String apiVersion = environment.getProperty("app.name").concat(" ").concat(environment.getProperty("app.platform")).concat(environment.getProperty("api.version"));
        Response response = new Response("SUCCESS", "Version ".concat(apiVersion).concat(" ").concat(date));
        return gson.toJson(response);
    }

    @GetMapping("/{dynamicEndpoint}")
    public String getGatewayEndPointGet(@PathVariable String dynamicEndpoint,
                                        @RequestBody Optional<LinkedHashMap>  jsonRequest,
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

        manageLog.infoLogger(logTo,httpServletRequest,"External Gateway",false);

        String apikey = httpServletRequest.getHeader(validationUtil.apiKeyHeader);

        try {
            URI basePathUri = urlService.getUrlFromRoutePropertie(dynamicEndpoint);

            if(basePathUri == null) {
                httpServletResponse.setStatus(HttpServletResponse.SC_BAD_GATEWAY);
                return gson.toJson(new Response("ERROR", "Invalid Method"));
            }

            LinkedHashMap<String, Object> logTrace = new LinkedHashMap<>();
            logTrace.put("method", dynamicEndpoint);
            logTrace.put("url", basePathUri != null ? basePathUri.toString(): "no encuentra ruta para este metodo");
            logTrace.put("apiKey", apikey);
            manageLog.infoLogger(logTo,httpServletRequest,logTrace.toString(),false);

            if(dynamicEndpoint.contains("-")) {
                String[] paramArr = dynamicEndpoint.split("-", 2);
                dynamicEndpoint = paramArr[0];
                dynamicEndpoint = dynamicEndpoint.replace("-", "/");
                microServiceResponse = gatewayRequest.dynamicEndpointRequestGetWithParam(basePathUri,dynamicEndpoint,paramArr[1], apikey);
            } else {
                microServiceResponse = gatewayRequest.dynamicEndpointRequestGet(basePathUri,dynamicEndpoint,apikey);
            }

            manageLog.infoLogger(logTo,httpServletRequest,microServiceResponse,false);

            return microServiceResponse;

        } catch (FeignException.FeignClientException e) {
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
