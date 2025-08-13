package api.apicreditcard.contoller;


import api.apicreditcard.util.HibernateProxyTypeAdapter;
import api.apicreditcard.util.Response;
import api.apicreditcard.entity.BankCommerceEntity;
import api.apicreditcard.request.DebitCard;
import api.apicreditcard.request.DebitCardRequest;
import api.apicreditcard.security.jwt.JwtUtils;
import api.apicreditcard.service.DebitCardService;
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
public class DebitCardController {

    private final ManageLogs manageLogs;

    private final DebitCardService debitCardService;

    public DebitCardController(ManageLogs manageLogs, DebitCardService debitCardService) {
        this.manageLogs = manageLogs;
        this.debitCardService = debitCardService;
    }

    @PostMapping("/debitCardPayment")
    public String debitCardPayment(@Valid @RequestBody DebitCardRequest debitCardRequest, DebitCard debitCard,
                                    HttpServletRequest httpServletRequest,
                                    HttpServletResponse httpServletResponse){

        Response response = null;
        GsonBuilder b = new GsonBuilder();
        b.registerTypeAdapterFactory(HibernateProxyTypeAdapter.FACTORY);
        Gson gson = b.setPrettyPrinting().create();
        LogsClass logTo = new LogsClass(httpServletRequest, new Object() {}.
                getClass().getEnclosingMethod().getName());

        BankCommerceEntity bankCommerceEntity = JwtUtils.validateBankCommerceInformation(debitCardRequest.getRif());
        if(bankCommerceEntity == null) {
            manageLogs.errorLogger(logTo, httpServletRequest, "Error bankCommerceEntity NULL",
                    "user", false);
            return gson.toJson(new Response("ERROR", "Comercio no posee información bancaria"));
        }
        try {

            Response validations = debitCardService.validateRequestDebitCard(debitCardRequest, logTo,httpServletRequest);
            if(!validations.getStatus().equals("SUCCESS")) {

                manageLogs.infoLogger(logTo,httpServletRequest,
                        "Successfully: " + logTo.getMethodName(),
                        "user",false);
                return gson.toJson(validations);
            }

            response = debitCardService.validateDebitCardPayment(debitCardRequest, httpServletResponse,httpServletRequest,logTo, bankCommerceEntity);
            String jsonResponse = gson.toJson(response);

           return jsonResponse;

        }catch (Exception e){
            manageLogs.severeErrorLogger(logTo,httpServletRequest,"Error: "+ logTo.getMethodName() + ": "+ e,
                    e,"user",false);
            return gson.toJson(gson.toJson(new Response("ERROR", "error del controller")));
        }



    }
}
