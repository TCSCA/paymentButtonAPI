package api.apiB2p.controller;

import api.apiB2p.entity.BankCommerceEntity;
import api.apiB2p.request.B2pRequest;
import api.apiB2p.security.jwt.JwtUtils;
import api.apiB2p.service.B2pService;
import api.apiB2p.util.HibernateProxyTypeAdapter;
import api.apiB2p.util.LogTo;
import api.apiB2p.util.ManageLog;
import api.apiB2p.util.Response;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api")
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST})
public class B2pController {

    private final ManageLog manageLog;

    private final B2pService b2pService;

    public B2pController(ManageLog manageLog, B2pService b2pService) {
        this.manageLog = manageLog;
        this.b2pService = b2pService;
    }

    @PostMapping("/sendPaymentB2P")
    public String sendPaymentB2P(@Valid @RequestBody B2pRequest b2pRequest,
                               HttpServletRequest httpServletRequest,
                               HttpServletResponse httpServletResponse){

        GsonBuilder b = new GsonBuilder();
        b.registerTypeAdapterFactory(HibernateProxyTypeAdapter.FACTORY);
        Gson gson = b.setPrettyPrinting().create();
        Response response = null;
        final String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        final LogTo logTo = new LogTo(httpServletRequest, methodName);

        BankCommerceEntity bankCommerceEntity = JwtUtils.validateBankCommerceInformation(b2pRequest.getRif());
        if(bankCommerceEntity == null) {
            httpServletResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return gson.toJson(new Response("ERROR", "Comercio no posee información bancaria"));
        }

        try {
            Response validations = b2pService.validateB2pRequestData(b2pRequest, manageLog, logTo, httpServletRequest);
            if(!validations.getStatus().equals("SUCCESS")) {
                httpServletResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                manageLog.infoLogger(logTo,httpServletRequest, String.valueOf(HttpServletResponse.SC_OK),true);
                return gson.toJson(validations);
            }

            b2pRequest.setIpDirection(httpServletRequest.getRemoteAddr());

            response = b2pService.validateB2pPayment(b2pRequest, httpServletResponse, bankCommerceEntity);

            String jsonResponse = gson.toJson(response);
            return jsonResponse;

        }catch (Exception e){
            String jsonResponse = gson.toJson(new Response("ERROR", "error del controller"));
            manageLog.severeErrorLogger(logTo, httpServletRequest, jsonResponse, e, false);
        }

        return gson.toJson(response);
    }

}
