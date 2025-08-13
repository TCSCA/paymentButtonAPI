package api.internalrepository.controller;

import api.internalrepository.service.ClientService;
import api.logsClass.LogsClass;
import api.logsClass.ManageLogs;
import jakarta.servlet.http.HttpServletRequest;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.LinkedHashMap;

@RestController
@RequestMapping(value = "/core")
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST})
public class ClientController {

    private final ManageLogs manageLogs;

    private final ClientService clientService;

    public ClientController(ManageLogs manageLogs, ClientService clientService) {
        this.manageLogs = manageLogs;
        this.clientService = clientService;
    }

    @PostMapping("/createClient")
    public LinkedHashMap<String, Object> createClient(@RequestBody String dataForm,
                                                      final HttpServletRequest httpServletRequest) {

        LogsClass logTo = new LogsClass(httpServletRequest, new Object() {}.
                getClass().getEnclosingMethod().getName());

        LinkedHashMap<String, Object> response = new LinkedHashMap<>();

        try {

            String clientName = (new JSONObject(dataForm).get("clientName")).toString();
            String identificationDocument = (new JSONObject(dataForm).get("identificationDocument")).toString();
            String phoneNumber = (new JSONObject(dataForm).get("phoneNumber")).toString();
            String email = (new JSONObject(dataForm).get("email")).toString();
            Long registerBy = Long.parseLong((new JSONObject(dataForm).get("registerBy")).toString());
            Long idCommerce = Long.parseLong((new JSONObject(dataForm).get("idCommerce")).toString());
            Long idUser = Long.parseLong((new JSONObject(dataForm).get("idUser")).toString());
            OffsetDateTime registerDate = OffsetDateTime.now(ZoneId.of("America/Caracas"));

            Long result = clientService.createClient(clientName, identificationDocument, phoneNumber,
                    email, registerBy, idCommerce, idUser , registerDate);

            response.put("idClient", result);

            manageLogs.infoLogger(logTo,httpServletRequest,
                    "Successfully: " + logTo.getMethodName(),
                    "user",true);

        } catch (Exception e){
            response.put("ERROR", null);
            response.put("message", "An error occurred: " + e.getMessage());

            manageLogs.severeErrorLogger(logTo,httpServletRequest,
                    "Error: "+ logTo.getMethodName() + ": "+ e,
                    e,"user",true);
        }

        return response;

    }

}
