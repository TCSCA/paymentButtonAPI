package api.apic2p.controller;

import api.apic2p.entity.BankCommerceEntity;
import api.apic2p.request.C2pRequest;
import api.apic2p.security.jwt.JwtUtils;
import api.apic2p.service.C2pService;
import api.apic2p.util.HibernateProxyTypeAdapter;
import api.apic2p.util.LogTo;
import api.apic2p.util.ManageLog;
import api.apic2p.util.Response;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api")
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST})
public class C2pController {

    private final ManageLog manageLog;

    private final C2pService c2pService;


    public C2pController(ManageLog manageLog, C2pService c2pService) {
        this.manageLog = manageLog;
        this.c2pService = c2pService;
    }

    @PostMapping("/purchaseC2P")
    public String purchaseC2P( @RequestBody C2pRequest c2pRequest,
                               HttpServletRequest httpServletRequest,
                               HttpServletResponse httpServletResponse){

        GsonBuilder b = new GsonBuilder();
        b.registerTypeAdapterFactory(HibernateProxyTypeAdapter.FACTORY);
        Gson gson = b.setPrettyPrinting().create();
        Response response = null;
        final String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        final LogTo logTo = new LogTo(httpServletRequest, methodName);
        String apiKey = httpServletRequest.getHeader("API_KEY");


        try {

            BankCommerceEntity bankCommerceEntity = JwtUtils.validateBankCommerceInformation(c2pRequest.getRif());
            if(bankCommerceEntity == null) {
                c2pService.setStatusPaymentLink(c2pRequest);
                return gson.toJson(new Response("ERROR", "Comercio no posee informacion Bancaria"));
            }

            Response validations = c2pService.validateC2pRequestData(c2pRequest, manageLog, logTo, httpServletRequest);
            if(!validations.getStatus().equals("SUCCESS")) {
                httpServletResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                manageLog.infoLogger(logTo,httpServletRequest, String.valueOf(HttpServletResponse.SC_INTERNAL_SERVER_ERROR),false);
                return gson.toJson(validations);
            }

            response = c2pService.validateBankPayment(c2pRequest, bankCommerceEntity,apiKey);
            String jsonResponse = gson.toJson(response);
            manageLog.infoLogger(logTo,httpServletRequest, jsonResponse,false);
            return jsonResponse;

        }catch (Exception e){
            String jsonResponse = gson.toJson(new Response("ERROR", "Error al conectar con el banco"));
            c2pService.setStatusPaymentLink(c2pRequest);
            manageLog.severeErrorLogger(logTo, httpServletRequest, jsonResponse, e, false);
            return jsonResponse;
        }

    }


}
