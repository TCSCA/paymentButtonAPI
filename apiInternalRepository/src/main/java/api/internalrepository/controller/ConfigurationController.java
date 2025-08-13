package api.internalrepository.controller;


import api.internalrepository.service.ConfigurationService;
import api.internalrepository.util.Response;
import api.logsClass.LogsClass;
import api.logsClass.ManageLogs;
import jakarta.servlet.http.HttpServletRequest;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;

@RestController
@RequestMapping(value = "/core")
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST})
public class ConfigurationController {

    private static final Logger logger = LoggerFactory.getLogger(ConfigurationController.class);

    private final ManageLogs manageLogs;
    private final ConfigurationService configurationService;

    public ConfigurationController(ManageLogs manageLogs, ConfigurationService configurationService) {
        this.manageLogs = manageLogs;
        this.configurationService = configurationService;
    }

    @PostMapping("/editEmailForAdmin")
    public LinkedHashMap<String, Object> editProfile(@RequestBody final String dataForm,
                                                     final HttpServletRequest httpServletRequest) {

        LogsClass logTo = new LogsClass(httpServletRequest, new Object() {}.
                getClass().getEnclosingMethod().getName());

        LinkedHashMap<String, Object> response = new LinkedHashMap<>();

        try {

            String emailSupport = (new JSONObject(dataForm).get("emailSupport")).toString();
            String emailReceipts = (new JSONObject(dataForm).get("emailReceipt")).toString();

            Response result = configurationService.editEmails(emailSupport,emailReceipts);
            logger.info("Information saved successfully");

            manageLogs.infoLogger(logTo,httpServletRequest,
                    "Successfully: " + logTo.getMethodName(),
                    "user",true);

        } catch (Exception e){
            response.put("ERROR", null);
            logger.error(e.getMessage(), e);
            response.put("message", "An error occurred: " + e.getMessage());

            manageLogs.severeErrorLogger(logTo,httpServletRequest,
                    "Error: "+ logTo.getMethodName() + ": "+ e,
                    e,"user",true);
        }

        return response;
    }
}
