package api.apiP2c.service;

import api.apiP2c.request.ManualPaymentRequest;
import api.apiP2c.util.Response;
import jakarta.mail.MessagingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Objects;

@Service
public class ManualPaymentService {

    private static final Logger logger = LoggerFactory.getLogger(ManualPaymentService.class);

    private final Environment environment;

    private final EmailService emailService;

    public ManualPaymentService(Environment environment, EmailService emailService) {
        this.environment = environment;
        this.emailService = emailService;
    }


    public Response callSavePayment(String apiKey, ManualPaymentRequest manualPaymentRequest) throws MessagingException, IOException {
        HttpHeaders headers = new HttpHeaders();
        RestTemplate restTemplate = new RestTemplate();

        headers.add("API_KEY", apiKey);

        String uri = environment.getProperty("app.route.internal");

        LinkedHashMap<String, Object> requestBody = new LinkedHashMap<>();

        requestBody.put("manualPaymentRequest", manualPaymentRequest);

        HttpEntity<LinkedHashMap<String, Object>> request = new HttpEntity<>(requestBody, headers);


        ResponseEntity<String> responseEntity = restTemplate.exchange(uri.
                concat("/saveManualPayment"), HttpMethod.POST, request, String.class);

        if (!Objects.requireNonNull(responseEntity.getBody()).contains("ERROR")) {
            logger.info("Pago registrado");
            emailService.sendManualPaymentReceipt(manualPaymentRequest.getEmail(),"Payment-Recibo de pago-Pago Manual",
                    apiKey,manualPaymentRequest);
            return new Response("SUCCESS","Transacción exitosa");
        } else {
            logger.error("Response Status: " + responseEntity.getBody());
            return new Response("ERROR", "Error registrando pago");
        }
    }
}
