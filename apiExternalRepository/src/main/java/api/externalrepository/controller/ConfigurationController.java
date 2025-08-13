package api.externalrepository.controller;


import api.externalrepository.service.ConfigurationService;
import api.externalrepository.to.EmailCredentials;
import api.externalrepository.util.Response;
import api.logsClass.LogsClass;
import api.logsClass.ManageLogs;
import jakarta.servlet.http.HttpServletRequest;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;

@RestController
@RequestMapping(value = "/horizonte")
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST})
public class ConfigurationController {

    private static final Logger logger = LoggerFactory.getLogger(ConfigurationController.class);

    private final ManageLogs manageLogs;
    private final ConfigurationService configurationService;

    public ConfigurationController(ManageLogs manageLogs, ConfigurationService configurationService) {
        this.manageLogs = manageLogs;
        this.configurationService = configurationService;
    }

    @PostMapping("/editEmailForAdminExt")
    public LinkedHashMap<String, Object> editProfile(@RequestBody final String dataForm,
                                                     final HttpServletRequest httpServletRequest) {

        LogsClass logTo = new LogsClass(httpServletRequest, new Object() {}.
                getClass().getEnclosingMethod().getName());

        LinkedHashMap<String, Object> response = new LinkedHashMap<>();

        try {

            String emailSupport = (new JSONObject(dataForm).get("emailSupport")).toString();
            String emailReceipts = (new JSONObject(dataForm).get("emailReceipt")).toString();

            Response result = configurationService.editEmailsExt(emailSupport,emailReceipts);
            logger.info("Information saved successfully");

            manageLogs.infoLogger(logTo,httpServletRequest,
                    "Successfully: " + logTo.getMethodName(),
                    "user",false);

        } catch (Exception e){
            response.put("ERROR", e.getMessage());
            logger.error(e.getMessage(), e);
            response.put("message", "An error occurred: " + e.getMessage());

            manageLogs.severeErrorLogger(logTo,httpServletRequest,
                    "Error: "+ logTo.getMethodName() + ": "+ e,
                    e,"user",false);
        }

        return response;
    }

    @GetMapping("/getEmailCredentials")
    public ResponseEntity<EmailCredentials> getEmailCredentials(final HttpServletRequest httpServletRequest) {

        LogsClass logTo = new LogsClass(httpServletRequest, new Object() {}.
                getClass().getEnclosingMethod().getName());

        EmailCredentials emailCredentials = configurationService.getEmailCredentials();

        // Verifica si las credenciales son nulas
        if (emailCredentials == null) {
            manageLogs.errorLogger(logTo, httpServletRequest,
                    "Error: No se pudieron obtener las credenciales del correo",
                    "user", false);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        // Log exitoso
        manageLogs.infoLogger(logTo, httpServletRequest,
                "Credenciales del correo obtenidas correctamente",
                "user", false);

        // Retornar las credenciales con estado HTTP 200
        return ResponseEntity.ok(emailCredentials);

    }
}
