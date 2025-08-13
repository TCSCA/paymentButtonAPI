package api.apicreditcard.contoller;


import api.apicreditcard.entity.BankCommerceEntity;
import api.apicreditcard.request.CreditCardRequest;
import api.apicreditcard.security.jwt.JwtUtils;
import api.apicreditcard.service.CreditCardService;
import api.apicreditcard.util.HibernateProxyTypeAdapter;
import api.apicreditcard.util.Response;
import api.logsClass.LogsClass;
import api.logsClass.ManageLogs;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api")
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST})
public class CreditCardController {

    private final CreditCardService creditCardService;

    private final ManageLogs manageLogs;

    public CreditCardController(CreditCardService creditCardService, ManageLogs manageLogs) {
        this.creditCardService = creditCardService;

        this.manageLogs = manageLogs;
    }

    @PostMapping("/creditCardPayment")
    public String creditCardPayment(@Valid @RequestBody CreditCardRequest creditCardRequest,
                                    HttpServletRequest httpServletRequest,
                                    HttpServletResponse httpServletResponse){

        Response response = null;
        GsonBuilder b = new GsonBuilder();
        b.registerTypeAdapterFactory(HibernateProxyTypeAdapter.FACTORY);
        Gson gson = b.setPrettyPrinting().create();
        LogsClass logTo = new LogsClass(httpServletRequest, new Object() {}.
                getClass().getEnclosingMethod().getName());

        BankCommerceEntity bankCommerceEntity = JwtUtils.validateBankCommerceInformation(creditCardRequest.getRif());
        if(bankCommerceEntity == null) {
            return gson.toJson(new Response("ERROR", "Comercio no posee información bancaria"));
        }
        try {

            Response validations = creditCardService.validateRequestCreditCard(creditCardRequest, logTo,httpServletRequest);
            if(!validations.getStatus().equals("SUCCESS")) {
                manageLogs.infoLogger(logTo,httpServletRequest,
                        "Successfully: " + logTo.getMethodName(),
                        "user",false);
                return gson.toJson(validations);
            }

            response = creditCardService.validateCreditCardPayment(creditCardRequest,httpServletRequest, httpServletResponse, bankCommerceEntity,logTo);
            String jsonResponse = gson.toJson(response);

            return jsonResponse;

        }catch (Exception e){
            manageLogs.severeErrorLogger(logTo,httpServletRequest,"Error: "+ logTo.getMethodName() + ": "+ e,
                    e,"user",false);
            return gson.toJson(gson.toJson(new Response("ERROR", "error del controller")));
        }

    }
}
