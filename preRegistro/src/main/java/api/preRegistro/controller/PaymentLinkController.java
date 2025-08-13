package api.preRegistro.controller;

import api.logsClass.LogsClass;
import api.logsClass.ManageLogs;
import api.preRegistro.service.BankService;
import api.preRegistro.service.PaymentLinkService;
import api.preRegistro.util.CalendarSerializer;
import api.preRegistro.util.HibernateProxyTypeAdapter;
import api.preRegistro.util.OffsetDateTimeSerializer;
import api.preRegistro.util.Response;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.time.OffsetDateTime;
import java.util.GregorianCalendar;

@RestController
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST})
public class PaymentLinkController {

    private final ManageLogs manageLogs;

    private final PaymentLinkService paymentLinkService;

    private final BankService bankService;

    public PaymentLinkController(ManageLogs manageLogs, PaymentLinkService paymentLinkService, BankService bankService) {
        this.manageLogs = manageLogs;
        this.paymentLinkService = paymentLinkService;
        this.bankService = bankService;
    }

    @GetMapping("/getInformationByPaymentLink/{idBankTransaction}")
    public String getInformationByPaymentLink(@PathVariable("idBankTransaction") final Long idBankTransaction,
                                              final HttpServletRequest httpServletRequest) {

        LogsClass logTo = new LogsClass(httpServletRequest, new Object() {}.
                getClass().getEnclosingMethod().getName());

        GsonBuilder b = new GsonBuilder();
        b.registerTypeAdapterFactory(HibernateProxyTypeAdapter.FACTORY);
        Gson gson = b.setPrettyPrinting().create();
        Response response;

        try {

            response = paymentLinkService.
                    getInformationByIdBankTransaction(idBankTransaction, httpServletRequest);

            manageLogs.infoLogger(logTo,httpServletRequest,
                    "Successfully: " + logTo.getMethodName(),
                    "user",false);

            return gson.toJson(response);

        } catch (Exception e) {
            manageLogs.severeErrorLogger(logTo,httpServletRequest,"Error: "+ logTo.
                    getMethodName() + ": "+ e, e,"user",false);

            return gson.toJson(new Response("ERROR", "Error trayendo informacion de la transaccion"));
        }

    }

    @GetMapping("/getAllBanks")
    public String getAllBanks(HttpServletRequest httpServletRequest) {

        LogsClass logTo = new LogsClass(httpServletRequest, new Object() {}.
                getClass().getEnclosingMethod().getName());

        GsonBuilder b = new GsonBuilder();
        b.registerTypeAdapterFactory(HibernateProxyTypeAdapter.FACTORY);
        b.registerTypeAdapter(GregorianCalendar.class, new CalendarSerializer());
        b.registerTypeAdapter(OffsetDateTime.class, new OffsetDateTimeSerializer());
        Gson gson = b.setPrettyPrinting().create();

        Response response;

        try {
            response = bankService.getAllBanksByStatusTrue(httpServletRequest.getHeader("API_KEY"),
                    httpServletRequest);

            manageLogs.infoLogger(logTo,httpServletRequest,
                    "Successfully: " + logTo.getMethodName(),
                    "user",false);

            return gson.toJson(response);

        } catch (Exception e) {
            manageLogs.severeErrorLogger(logTo,httpServletRequest,"Error: "+ logTo.
                    getMethodName() + ": "+ e, e,"user",false);

            return gson.toJson(new Response("ERROR", "Error trayendo informacion de la transaccion"));
        }
    }

}
