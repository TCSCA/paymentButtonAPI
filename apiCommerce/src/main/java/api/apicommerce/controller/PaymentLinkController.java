package api.apicommerce.controller;

import api.apicommerce.request.PaymentLinkRequest;
import api.apicommerce.service.PaymentLinkService;
import api.apicommerce.util.HibernateProxyTypeAdapter;
import api.apicommerce.util.Response;
import api.logsClass.LogsClass;
import api.logsClass.ManageLogs;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api")
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST})
public class PaymentLinkController {

    private final PaymentLinkService paymentLinkService;

    private final ManageLogs manageLogs;

    public PaymentLinkController(PaymentLinkService paymentLinkService, ManageLogs manageLogs) {
        this.paymentLinkService = paymentLinkService;
        this.manageLogs = manageLogs;
    }

    @PostMapping("/generatePaymentLink")
    public String generatePaymentLink(@RequestBody PaymentLinkRequest paymentLinkRequest,
                                      HttpServletRequest httpServletRequest) {

        GsonBuilder b = new GsonBuilder();
        b.registerTypeAdapterFactory(HibernateProxyTypeAdapter.FACTORY);
        Gson gson = b.setPrettyPrinting().create();
        Response response;

        LogsClass logTo = new LogsClass(httpServletRequest, new Object() {}.
                getClass().getEnclosingMethod().getName());

        try {

            response = paymentLinkService.generatePaymentLink(paymentLinkRequest, httpServletRequest);

            manageLogs.infoLogger(logTo,httpServletRequest,
                    "Successfully: " + logTo.getMethodName(),
                    "user",true);

            return gson.toJson(response);

        } catch (Exception e) {
            manageLogs.severeErrorLogger(logTo,httpServletRequest,"Error: "+ logTo.
                    getMethodName() + ": "+ e, e,"user",true);

            return gson.toJson(new Response("ERROR", "Error salvando el link de pago"));
        }

    }

}
