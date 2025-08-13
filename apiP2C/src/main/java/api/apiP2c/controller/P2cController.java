package api.apiP2c.controller;

import api.apiP2c.entity.BankCommerceEntity;
import api.apiP2c.request.ManualPaymentRequest;
import api.apiP2c.request.P2cRequest;
import api.apiP2c.security.jwt.JwtUtils;
import api.apiP2c.service.ManualPaymentService;
import api.apiP2c.service.P2cService;
import api.apiP2c.util.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;
import java.util.LinkedHashMap;
import java.util.List;

@RestController
@RequestMapping(value = "/api")
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST})
public class P2cController {

    private final P2cService p2cService;

    private final ManageLog manageLog;

    private final ManualPaymentService manualPaymentService;

    public P2cController(P2cService p2cService, ManageLog manageLog, ManualPaymentService manualPaymentService) {
        this.p2cService = p2cService;
        this.manageLog = manageLog;
        this.manualPaymentService = manualPaymentService;
    }

    @PostMapping("/validatePaymentP2c")
    public String validatePaymentP2c( @Valid @RequestBody P2cRequest p2cRequest,
                               HttpServletRequest httpServletRequest,
                               HttpServletResponse httpServletResponse){

        GsonBuilder b = new GsonBuilder();
        b.registerTypeAdapterFactory(HibernateProxyTypeAdapter.FACTORY);
        Gson gson = b.setPrettyPrinting().create();
        Response response;
        final String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        final LogTo logTo = new LogTo(httpServletRequest, methodName);

        try {
            BankCommerceEntity bankCommerceEntity = JwtUtils.validateBankCommerceInformation(p2cRequest.getRif());
            if(bankCommerceEntity == null) {
                p2cService.setStatusPaymentLink(p2cRequest);
                return gson.toJson(new Response("ERROR", "Comercio no posee Informacion Bancaria"));
            }

            response = p2cService.validatePaymentP2c(p2cRequest, httpServletRequest, httpServletResponse, bankCommerceEntity);
            String jsonResponse = gson.toJson(response);
            manageLog.infoLogger(logTo,httpServletRequest, jsonResponse,false);
            return jsonResponse;

        } catch (Exception e) {
            String jsonResponse = gson.toJson(new Response("ERROR", "Error al conectar con el banco"));
            p2cService.setStatusPaymentLink(p2cRequest);
            manageLog.severeErrorLogger(logTo, httpServletRequest, jsonResponse, e, false);
            return jsonResponse;
        }

    }

    @PostMapping("/validateManualPayment")
    public String validateManualPayment( @Valid @RequestBody ManualPaymentRequest manualPaymentRequest,
                               HttpServletRequest httpServletRequest,
                               HttpServletResponse httpServletResponse){

        GsonBuilder b = new GsonBuilder();
        b.registerTypeAdapterFactory(HibernateProxyTypeAdapter.FACTORY);
        Gson gson = b.setPrettyPrinting().create();
        Response response = null;
        List<String> headerValues;
        final String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        final LogTo logTo = new LogTo(httpServletRequest, methodName);

        try {
            Boolean bankCommerceInfo = JwtUtils.validateBankCommerceInformationManualPayment(manualPaymentRequest.getRif());
            if(bankCommerceInfo == false) {
                return gson.toJson(new Response("ERROR", "Comercio no posee Informacion Bancaria"));
            }

            String apiKey = httpServletRequest.getHeader("API_KEY");

            response = manualPaymentService.callSavePayment(apiKey, manualPaymentRequest);
            String jsonResponse = gson.toJson(response);
            manageLog.infoLogger(logTo,httpServletRequest, jsonResponse,false);
            return jsonResponse;

        } catch (Exception e) {
            String jsonResponse = gson.toJson(new Response("ERROR", "Error al registrar el pago manual"));
            manageLog.severeErrorLogger(logTo, httpServletRequest, jsonResponse, e, false);
            return jsonResponse;
        }

    }

    @PostMapping("/validateExternalPayment")
    public String validateExternalPayment(@RequestBody P2cRequest p2cRequest,
                                         HttpServletRequest httpServletRequest,
                                         HttpServletResponse httpServletResponse){

        GsonBuilder b = new GsonBuilder();
        b.registerTypeAdapterFactory(HibernateProxyTypeAdapter.FACTORY);
        Gson gson = b.setPrettyPrinting().create();
        Response response = null;
        List<String> headerValues;
        final String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        final LogTo logTo = new LogTo(httpServletRequest, methodName);
        ObjectMapper objectMapper = new ObjectMapper();

         gson = new GsonBuilder()
                .registerTypeAdapter(OffsetDateTime.class, new OffsetDateTimeAdapter())
                .create();

        try {

            BankCommerceEntity bankCommerceEntity = JwtUtils.validateBankCommerceInformation(p2cRequest.getRif());
            if(bankCommerceEntity == null) {
                return gson.toJson(new Response("ERROR", "Comercio no posee Informacion Bancaria"));
            }

            response = p2cService.validateExternalPayment(p2cRequest, httpServletRequest, httpServletResponse, bankCommerceEntity);
            String jsonResponse = gson.toJson(response);
            manageLog.infoLogger(logTo,httpServletRequest, jsonResponse,false);
            return jsonResponse;


        } catch (Exception e) {
            String jsonResponse = gson.toJson(new Response("ERROR", "Error al conectar con el banco"));
            manageLog.severeErrorLogger(logTo, httpServletRequest, jsonResponse, e, false);
            return jsonResponse;
        }

    }
}
