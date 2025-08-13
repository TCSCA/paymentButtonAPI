package api.apiAdminCommerce.controller;

import api.apiAdminCommerce.adapter.LocalDateAdapter;
import api.apiAdminCommerce.adapter.ThrowableTypeAdapter;
import api.apiAdminCommerce.service.BankTransactionService;
import api.apiAdminCommerce.util.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.GregorianCalendar;
import java.util.LinkedHashMap;

@RestController
@RequestMapping(value = "/api")
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST})
public class ReportsAdminController {

    private final BankTransactionService bankTransactionService;

    private final ManageLog manageLog;

    public ReportsAdminController(BankTransactionService bankTransactionService, ManageLog manageLog) {
        this.bankTransactionService = bankTransactionService;
        this.manageLog = manageLog;
    }

    @PostMapping("/getAllBankTransactionsByDate")
    public String getAllBankTransactionsByDate(@RequestBody LinkedHashMap body,
                                               HttpServletRequest httpServletRequest,
                                               HttpServletResponse httpServletResponse) {

        LogTo logTo = new LogTo(httpServletRequest,"getAllBankTransactionsByDate");
        GsonBuilder b = new GsonBuilder();
        b.registerTypeAdapterFactory(HibernateProxyTypeAdapter.FACTORY);
        b.registerTypeAdapter(GregorianCalendar.class, new CalendarSerializer());
        b.registerTypeAdapter(OffsetDateTime.class, new OffsetDateTimeSerializer());
        b.registerTypeAdapter(LocalDate.class, new LocalDateAdapter());
        Gson gson = b.setPrettyPrinting().create();

        Response response;

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

            String token = httpServletRequest.getHeader("token");
            String apiKey = httpServletRequest.getHeader("API_KEY");

            String startDate = objectMapper.convertValue(body.get("startDate"), String.class);
            String endDate = objectMapper.convertValue(body.get("endDate"), String.class);
            int index = objectMapper.convertValue(body.get("index"), int.class);
            int quantity = objectMapper.convertValue(body.get("quantity"), int.class);
            String rif = objectMapper.convertValue(body.get("rif"), String.class);
            Integer typeFilter = objectMapper.convertValue(body.get("typeFilter"), Integer.class);
            String filter = objectMapper.convertValue(body.get("filter"), String.class);
            Boolean export = objectMapper.convertValue(body.get("export"), Boolean.class);

            response = bankTransactionService.getAllBankTransactionsByFilter
                    (apiKey, token, index, quantity, startDate, endDate, filter,typeFilter, export, rif);

            manageLog.infoLogger(logTo,httpServletRequest,"getAllBankTransactionsByDate Susccesfully",false);
            return gson.toJson(response);


        } catch (Exception e) {
            String jsonResponse = gson.toJson(new Response("ERROR","Error en controller"));
            manageLog.severeErrorLogger(logTo,httpServletRequest,jsonResponse,e,false);
            return gson.toJson(new Response("Error", jsonResponse));
        }
    }

    @PostMapping("/getAllBankTransactionsByDateAndStatusTransaction")
    public String getAllBankTransactionsByDateAndStatusTransaction(@RequestBody LinkedHashMap body,
                                                                   HttpServletRequest httpServletRequest,
                                                                   HttpServletResponse httpServletResponse) {

        LogTo logTo = new LogTo(httpServletRequest,"getAllBankTransactionsByDateAndStatusTransaction");
        GsonBuilder b = new GsonBuilder();
        b.registerTypeAdapterFactory(HibernateProxyTypeAdapter.FACTORY);
        b.registerTypeAdapter(GregorianCalendar.class, new CalendarSerializer());
        b.registerTypeAdapter(OffsetDateTime.class, new OffsetDateTimeSerializer());
        b.registerTypeAdapter(LocalDate.class, new LocalDateAdapter());
        Gson gson = b.setPrettyPrinting().create();

        Response response;

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            String token = httpServletRequest.getHeader("token");
            String apiKey = httpServletRequest.getHeader("API_KEY");

            String startDate = objectMapper.convertValue(body.get("startDate"), String.class);
            String endDate = objectMapper.convertValue(body.get("endDate"), String.class);
            int index = objectMapper.convertValue(body.get("index"), int.class);
            Long idStatusTransaction = objectMapper.convertValue(body.get("idStatusTransaction"), Long.class);
            int quantity = objectMapper.convertValue(body.get("quantity"), int.class);
            String rif = objectMapper.convertValue(body.get("rif"), String.class);
            Integer typeFilter = objectMapper.convertValue(body.get("typeFilter"), Integer.class);
            String filter = objectMapper.convertValue(body.get("filter"), String.class);
            Boolean export = objectMapper.convertValue(body.get("export"), Boolean.class);


            response = bankTransactionService.getAllBankTransactionsAndStatusByFilter
                    (apiKey,token,index,quantity,startDate,endDate,filter,typeFilter,export,rif,idStatusTransaction);

            manageLog.infoLogger(logTo,httpServletRequest,"getAllBankTransactionsByDateAndStatusTransaction Susccesfully",false);
            return gson.toJson(response);


        } catch (Exception e) {
            String jsonResponse = gson.toJson(new Response("ERROR","Error en controller"));
            manageLog.severeErrorLogger(logTo,httpServletRequest,jsonResponse,e,false);
            return gson.toJson(new Response("Error", jsonResponse));
        }
    }

    @PostMapping("/getAllBankTransactionsByDateAndPaymentMethod")
    public String getAllBankTransactionsByDateAndPaymentMethod(@RequestBody LinkedHashMap body,
                                                               HttpServletRequest httpServletRequest,
                                                               HttpServletResponse httpServletResponse) {

        LogTo logTo = new LogTo(httpServletRequest,"getAllBankTransactionsByDateAndPaymentMethod");
        GsonBuilder b = new GsonBuilder();
        b.registerTypeAdapterFactory(HibernateProxyTypeAdapter.FACTORY);
        b.registerTypeAdapter(GregorianCalendar.class, new CalendarSerializer());
        b.registerTypeAdapter(OffsetDateTime.class, new OffsetDateTimeSerializer());
        b.registerTypeAdapter(LocalDate.class, new LocalDateAdapter());
        Gson gson = b.setPrettyPrinting().create();

        Response response;

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

            String startDate = objectMapper.convertValue(body.get("startDate"), String.class);
            String endDate = objectMapper.convertValue(body.get("endDate"), String.class);
            int index = objectMapper.convertValue(body.get("index"), int.class);
            int quantity = objectMapper.convertValue(body.get("quantity"), int.class);
            String rif = objectMapper.convertValue(body.get("rif"), String.class);
            String filter = objectMapper.convertValue(body.get("filter"), String.class);
            Boolean export = objectMapper.convertValue(body.get("export"), Boolean.class) ;
            Integer typeFilter = objectMapper.convertValue(body.get("typeFilter"), Integer.class);

            String token = httpServletRequest.getHeader("token");
            String apiKey = httpServletRequest.getHeader("API_KEY");

            response = bankTransactionService.getAllBankTransactionsByFilterPaymentMethod
                    (apiKey, token, index, quantity, startDate, endDate, filter,typeFilter, export, rif);

            manageLog.infoLogger(logTo,httpServletRequest,"getAllBankTransactionsByDateAndPaymentMethod Susccesfully",false);
            return gson.toJson(response);


        } catch (Exception e) {
            String jsonResponse = gson.toJson(new Response("ERROR","Error en controller"));
            manageLog.severeErrorLogger(logTo,httpServletRequest,jsonResponse,e,false);
            return gson.toJson(new Response("Error", jsonResponse));
        }
    }

    @PostMapping("/getAllBankTransactionsByDateAndTypeProduct")
    public String getAllBankTransactionsByDateAndTypeProduct(@RequestBody LinkedHashMap body,
                                                             HttpServletRequest httpServletRequest,
                                                             HttpServletResponse httpServletResponse) {

        LogTo logTo = new LogTo(httpServletRequest,"getAllBankTransactionsByDateAndTypeProduct");
        GsonBuilder b = new GsonBuilder();
        b.registerTypeAdapterFactory(HibernateProxyTypeAdapter.FACTORY);
        b.registerTypeAdapter(GregorianCalendar.class, new CalendarSerializer());
        b.registerTypeAdapter(OffsetDateTime.class, new OffsetDateTimeSerializer());
        b.registerTypeAdapter(LocalDate.class, new LocalDateAdapter());
        Gson gson = b.setPrettyPrinting().create();

        Response response;

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

            String token = httpServletRequest.getHeader("token");
            String apiKey = httpServletRequest.getHeader("API_KEY");
            String startDate = objectMapper.convertValue(body.get("startDate"), String.class);
            String endDate = objectMapper.convertValue(body.get("endDate"), String.class);
            int index = objectMapper.convertValue(body.get("index"), int.class);
            int quantity = objectMapper.convertValue(body.get("quantity"), int.class);
            String rif = objectMapper.convertValue(body.get("rif"), String.class);
            Integer typeFilter = objectMapper.convertValue(body.get("typeFilter"), Integer.class);
            String filter = objectMapper.convertValue(body.get("filter"), String.class);
            Boolean export = objectMapper.convertValue(body.get("export"), Boolean.class);

            response = bankTransactionService.getAllBankTransactionsAndTypeProductByFilter(apiKey,token,index,
                    quantity,startDate,endDate, filter,typeFilter,export,rif);


            manageLog.infoLogger(logTo,httpServletRequest,"getAllBankTransactionsByDateAndTypeProduct",false);
            return gson.toJson(response);


        } catch (Exception e) {
            String jsonResponse = gson.toJson(new Response("ERROR","Error en controller"));
            manageLog.severeErrorLogger(logTo,httpServletRequest,jsonResponse,e,false);
            return gson.toJson(new Response("Error", jsonResponse));
        }
    }

    @PostMapping("/getAllManualPayments")
    public String getAllManualPayments(@RequestBody LinkedHashMap body,
                                       HttpServletRequest httpServletRequest,
                                       HttpServletResponse httpServletResponse) {

        LogTo logTo = new LogTo(httpServletRequest,"getAllManualPayments");
        GsonBuilder b = new GsonBuilder();
        b.registerTypeAdapterFactory(HibernateProxyTypeAdapter.FACTORY);
        b.registerTypeAdapter(GregorianCalendar.class, new CalendarSerializer());
        b.registerTypeAdapter(OffsetDateTime.class, new OffsetDateTimeSerializer());
        b.registerTypeAdapter(LocalDate.class, new LocalDateAdapter());
        b.registerTypeAdapter(Throwable.class, new ThrowableTypeAdapter());
        Gson gson = b.setPrettyPrinting().create();

        Response response;

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

            String startDate = objectMapper.convertValue(body.get("startDate"), String.class);
            String endDate = objectMapper.convertValue(body.get("endDate"), String.class);
            int index = objectMapper.convertValue(body.get("index"), int.class);
            int quantity = objectMapper.convertValue(body.get("quantity"), int.class);
            String rif = objectMapper.convertValue(body.get("rif"), String.class);
            String filter = objectMapper.convertValue(body.get("filter"), String.class);
            Boolean export = objectMapper.convertValue(body.get("export"), Boolean.class);
            Integer typeFilter = objectMapper.convertValue(body.get("typeFilter"), Integer.class);

            String token = httpServletRequest.getHeader("token");
            String apiKey = httpServletRequest.getHeader("API_KEY");

            response = bankTransactionService.getAllBankTransactionsByFilterManualPayment
                    (apiKey, token, index, quantity, startDate, endDate, filter,typeFilter, export, rif);


            manageLog.infoLogger(logTo,httpServletRequest,"getAllManualPayments Susccesfully",false);
            return gson.toJson(response);


        } catch (Exception e) {
            String jsonResponse = gson.toJson(new Response("ERROR","Error en controller"));
            manageLog.severeErrorLogger(logTo,httpServletRequest,jsonResponse,e,false);
            return gson.toJson(new Response("Error", jsonResponse));
        }
    }
}
