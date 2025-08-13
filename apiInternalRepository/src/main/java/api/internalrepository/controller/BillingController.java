package api.internalrepository.controller;

import api.internalrepository.service.BillingService;
import api.logsClass.LogsClass;
import api.logsClass.ManageLogs;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.LinkedHashMap;

@RestController
@RequestMapping(value = "/core")
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST})
public class BillingController {

    private final BillingService billingService;

    private final ManageLogs manageLogs;

    public BillingController(BillingService billingService, ManageLogs manageLogs) {
        this.billingService = billingService;
        this.manageLogs = manageLogs;
    }

    @PostMapping("/dairyBillingByCommerce")
    public LinkedHashMap<String, Object> dairyBillingByCommerce(@RequestBody final String body, HttpServletRequest httpServletRequest) {

        LogsClass logTo = new LogsClass(httpServletRequest, new Object() {}.
                getClass().getEnclosingMethod().getName());


        ObjectMapper objectMapper = new ObjectMapper();
        LinkedHashMap<String, Object> response = new LinkedHashMap<>();

        try {
            LinkedHashMap<String, Object> requestMap = objectMapper.readValue(body, LinkedHashMap.class);

            response = billingService.getDailyBankTransactionsByCommerce(requestMap, httpServletRequest, logTo);

            manageLogs.infoLogger(logTo,httpServletRequest,
                    "Successfull: " + logTo.getMethodName(),
                    "user",false);

            manageLogs.infoLogger(logTo,httpServletRequest,
                    "Se ejecuto correctamente metodo de facturación: " + logTo.getMethodName(),
                    "user",false);

            return response;

        } catch (IOException e) {
            manageLogs.severeErrorLogger(logTo,httpServletRequest,"Error: "+ logTo.
                    getMethodName() + ": "+ e, e,"user",false);

            manageLogs.severeErrorLogger(logTo,httpServletRequest,"Error metodo de facturación: "+ logTo.
                    getMethodName() + ": "+ e, e,"user",false);

            response.put("ERROR", e);
            return response;
        }

    }
}
