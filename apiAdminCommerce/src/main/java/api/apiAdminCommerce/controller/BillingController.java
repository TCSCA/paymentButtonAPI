package api.apiAdminCommerce.controller;

import api.apiAdminCommerce.service.BillingService;
import api.apiAdminCommerce.util.CalendarSerializer;
import api.apiAdminCommerce.util.HibernateProxyTypeAdapter;
import api.apiAdminCommerce.util.OffsetDateTimeSerializer;
import api.apiAdminCommerce.util.Response;
import api.logsClass.LogsClass;
import api.logsClass.ManageLogs;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;
import java.util.GregorianCalendar;
import java.util.LinkedHashMap;

@RestController
@RequestMapping(value = "/api")
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST})
public class BillingController {

    public final BillingService billingService;

    public final ManageLogs manageLogs;

    public BillingController(BillingService billingService, ManageLogs manageLogs) {
        this.billingService = billingService;
        this.manageLogs = manageLogs;
    }


    @PostMapping("/dairyBillingByCommerce")
    public String dairyBillingByCommerce(@RequestBody LinkedHashMap linkedHashMap, HttpServletRequest httpServletRequest) {

        GsonBuilder b = new GsonBuilder();
        b.registerTypeAdapterFactory(HibernateProxyTypeAdapter.FACTORY);
        b.registerTypeAdapter(GregorianCalendar.class, new CalendarSerializer());
        b.registerTypeAdapter(OffsetDateTime.class, new OffsetDateTimeSerializer());
        String token = httpServletRequest.getHeader("Token");
        String apiKey = httpServletRequest.getHeader("API_KEY");
        Gson gson = new Gson();

        LogsClass logTo = new LogsClass(httpServletRequest, new Object() {}.
                getClass().getEnclosingMethod().getName());

        Response response;

        ObjectMapper objectMapper = new ObjectMapper();

        try {

            String billingDate = objectMapper.convertValue(linkedHashMap.get("billingDate"), String.class);
            String rif = objectMapper.convertValue(linkedHashMap.get("rif"), String.class);

            response = billingService.manualBillingMethod(apiKey, token, rif, billingDate);

            manageLogs.infoLogger(logTo,httpServletRequest,
                    "Succesfully: se guardo facturacion por metodo Manual",
                    "user",true);

            return gson.toJson(response);

        } catch (IllegalArgumentException e) {
            manageLogs.severeErrorLogger(logTo,httpServletRequest,"Error: "+ logTo.
                    getMethodName() + ": "+ e, e,"user",false);

            return gson.toJson(new Response("ERROR", "En estos momentos estamos presentado inconvenientes. " +
                    "Favor comuníquese con el Administrador o intente mas tarde."));


        }

    }
}
