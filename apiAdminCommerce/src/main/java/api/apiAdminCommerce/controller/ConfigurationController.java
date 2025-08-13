package api.apiAdminCommerce.controller;

import api.apiAdminCommerce.service.CommerceService;
import api.apiAdminCommerce.service.ConfigurationService;
import api.apiAdminCommerce.util.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/api")
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST})
public class ConfigurationController {

    private static final Logger logger = LoggerFactory.getLogger(AdminCommerceController.class);

    private final CommerceService commerceService;

    private final ConfigurationService configurationService;

    public ConfigurationController(CommerceService commerceService, ConfigurationService configurationService) {
        this.commerceService = commerceService;
        this.configurationService = configurationService;
    }


    @PostMapping("/getEmailsConfiguration")
    public String getEmailsConfiguration(HttpServletRequest httpServletRequest) {

        GsonBuilder b = new GsonBuilder();
        b.registerTypeAdapterFactory(HibernateProxyTypeAdapter.FACTORY);
        b.registerTypeAdapter(GregorianCalendar.class, new CalendarSerializer());
        b.registerTypeAdapter(OffsetDateTime.class, new OffsetDateTimeSerializer());
        Gson gson = b.setPrettyPrinting().create();
        String apiKey = httpServletRequest.getHeader("API_KEY");
        String token = httpServletRequest.getHeader("Token");

        if (!ValidationUtil.validateApiKey(httpServletRequest)) {
            return gson.toJson(new Response("ERROR", "UNAUTHORIZED"));
        }

        try {
            String emailSupport = commerceService.searchConfigurationById(8L, apiKey, token);
            String emailReceipts = commerceService.searchConfigurationById(11L, apiKey, token);
            Map<String, String> emails = new HashMap<>();
            emails.put("emailSupport", emailSupport);
            emails.put("emailReceipts", emailReceipts);

            return gson.toJson(new Response("SUCCESS", emails));

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return gson.toJson(new Response("ERROR", e.getMessage()));
        }
    }

    @PostMapping("/editEmailsConfiguration")
    public String editEmailsConfiguration(@RequestBody LinkedHashMap requestBody, HttpServletRequest httpServletRequest) {

        GsonBuilder b = new GsonBuilder();
        b.registerTypeAdapterFactory(HibernateProxyTypeAdapter.FACTORY);
        b.registerTypeAdapter(GregorianCalendar.class, new CalendarSerializer());
        b.registerTypeAdapter(OffsetDateTime.class, new OffsetDateTimeSerializer());
        Gson gson = b.setPrettyPrinting().create();
        ObjectMapper objectMapper = new ObjectMapper();
        String token = httpServletRequest.getHeader("Token");
        String apiKey = httpServletRequest.getHeader("API_KEY");
        String emailSupport = objectMapper.convertValue(requestBody.get("emailSupport"), String.class);
        String emailReceipt = objectMapper.convertValue(requestBody.get("emailReceipt"), String.class);

        if(!ValidationUtil.validateApiKey(httpServletRequest)) {
            return gson.toJson(new Response("ERROR", "UNAUTHORIZED"));
        }

        try {
            configurationService.editEmailsAdmin(emailReceipt,apiKey,token,emailSupport);
            configurationService.editEmailsAdminExt(emailReceipt,apiKey,emailSupport);
            return gson.toJson(new Response("SUCCESS", "Transacción exitosa"));

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return gson.toJson(new Response("ERROR", e.getMessage()));
        }
    }


}
