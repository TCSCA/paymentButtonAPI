package api.apisendemail.controller;

import api.apisendemail.service.EmailService;
import api.logsClass.LogsClass;
import api.logsClass.ManageLogs;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping(value = "/email")
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST})
public class EmailController {

    private final ManageLogs manageLogs;

    private final EmailService emailService;

    public EmailController(ManageLogs manageLogs, EmailService emailService) {
        this.manageLogs = manageLogs;
        this.emailService = emailService;
    }

    @PostMapping("/sendEmail")
    public ResponseEntity<String> sendEmail(@RequestBody Map<String, Object> request,
                                            final HttpServletRequest httpServletRequest) throws JsonProcessingException {

        LogsClass logTo = new LogsClass(httpServletRequest, new Object() {}.
                getClass().getEnclosingMethod().getName());

        String apiKey = httpServletRequest.getHeader("API_KEY");

        // Obtener datos del cuerpo de la solicitud
        String username = request.get("username").toString();
        String htmlTemplatePath = request.get("templatePath").toString();
        String logoUrl = request.get("urlLogo").toString();
        Map<String, String> placeholders = (Map<String, String>) request.get("placeholders");

        // Enviar el correo
        emailService.sendEmail(username, htmlTemplatePath, placeholders, logTo, httpServletRequest, apiKey, logoUrl);

        manageLogs.infoLogger(logTo, httpServletRequest,
                "Successfully: " + logTo.getMethodName(),
                "user", false);

        return ResponseEntity.ok("Correo enviado exitosamente.");
    }

}
