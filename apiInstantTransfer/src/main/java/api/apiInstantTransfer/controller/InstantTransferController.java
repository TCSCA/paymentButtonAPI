package api.apiInstantTransfer.controller;

import api.apiInstantTransfer.entity.BankCommerceEntity;
import api.apiInstantTransfer.request.InstantTransferRequest;
import api.apiInstantTransfer.security.jwt.JwtUtils;
import api.apiInstantTransfer.service.InstantTransferService;
import api.apiInstantTransfer.util.HibernateProxyTypeAdapter;
import api.apiInstantTransfer.util.LogTo;
import api.apiInstantTransfer.util.ManageLog;
import api.apiInstantTransfer.util.Response;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api")
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST})
public class InstantTransferController {

    private final InstantTransferService instantTransferService;

    private final ManageLog manageLog;

    public InstantTransferController(InstantTransferService instantTransferService, ManageLog manageLog) {
        this.instantTransferService = instantTransferService;
        this.manageLog = manageLog;
    }

    @PostMapping("/instantTransferPayment")
    public String InstantTransferPayment( @RequestBody InstantTransferRequest instantTransferRequest,
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

        BankCommerceEntity bankCommerceEntity = JwtUtils.validateBankCommerceInformation(instantTransferRequest.getRif());
        if(bankCommerceEntity == null) {
            httpServletResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return gson.toJson(new Response("ERROR", "UNAUTHORIZED"));
        }

        instantTransferRequest.setCustomerIpAddress(httpServletRequest.getRemoteAddr());

        try {
            Response validations = instantTransferService.validateInstantTransferRequestData(instantTransferRequest,
                    manageLog, logTo, httpServletRequest);
            if(!validations.getStatus().equals("SUCCESS")) {
                httpServletResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                manageLog.infoLogger(logTo,httpServletRequest, String.valueOf(HttpServletResponse.SC_INTERNAL_SERVER_ERROR),false);
                return gson.toJson(validations);
            }

            response = instantTransferService.validateInstantTransferPayment(instantTransferRequest, httpServletResponse,
                    bankCommerceEntity);
            String jsonResponse = gson.toJson(response);
            manageLog.infoLogger(logTo,httpServletRequest, jsonResponse,false);
            return jsonResponse;

        }catch (Exception e){
            String jsonResponse = gson.toJson(new Response("ERROR", "error del controller"));
            manageLog.severeErrorLogger(logTo, httpServletRequest, jsonResponse, e, false);
        }

        return gson.toJson(response);
    }
}
