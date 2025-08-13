package api.preRegistro.service;

import api.logsClass.LogsClass;
import api.logsClass.ManageLogs;
import api.preRegistro.to.BankTransactionTo;
import api.preRegistro.util.Response;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.LinkedHashMap;
import java.util.Objects;

@Service
public class PaymentLinkService {

    private static final Logger logger = LoggerFactory.getLogger(PaymentLinkService.class);

    private final ManageLogs manageLogs;

    private final Environment environment;

    public PaymentLinkService(ManageLogs manageLogs, Environment environment) {
        this.manageLogs = manageLogs;
        this.environment = environment;
    }

    public Response getInformationByIdBankTransaction(final Long idBankTransaction,
                                                      HttpServletRequest httpServletRequest) {

        LogsClass logTo = new LogsClass(httpServletRequest, new Object() {}.
                getClass().getEnclosingMethod().getName());

        try {

            HttpHeaders headers = new HttpHeaders();
            RestTemplate restTemplate = new RestTemplate();

            headers.add("API_KEY", httpServletRequest.getHeader("API_KEY"));

            String uri = environment.getProperty("app.route.internal");

            LinkedHashMap<String, Object> requestBody = new LinkedHashMap<>();

            requestBody.put("idBankTransaction", idBankTransaction);

            HttpEntity<LinkedHashMap<String, Object>> request = new HttpEntity<>(requestBody, headers);

            ResponseEntity<String> responseEntity = restTemplate.exchange(Objects.requireNonNull(uri).
                            concat("/getInformationByIdBankTransaction/"+idBankTransaction), HttpMethod.GET,
                    request, String.class);

            if (!Objects.requireNonNull(responseEntity.getBody()).contains("ERROR")) {
                manageLogs.infoLogger(logTo,httpServletRequest,
                        "Successfully: " + logTo.getMethodName(),
                        "user",false);
                logger.info("Bank transaction information found successfully");

                ObjectMapper objectMapper = new ObjectMapper();

                LinkedHashMap<String, Object> responseMap = objectMapper.readValue(responseEntity.getBody(), LinkedHashMap.class);

                return new Response("SUCCESS", new BankTransactionTo(objectMapper, responseMap));

            } else {

                String message;
                ObjectMapper objectMapper = new ObjectMapper();
                LinkedHashMap<String, Object> responseMap = objectMapper.readValue(responseEntity.getBody(), LinkedHashMap.class);

                if (Long.parseLong(responseMap.get("ERROR").toString()) == 1) {
                    message = "El link de pago al que hace referencia se encuentra procesado";
                } else if (Long.parseLong(responseMap.get("ERROR").toString()) == 2) {
                    message = "El link de pago al que hace referencia se encuentra rechazado";
                } else if (Long.parseLong(responseMap.get("ERROR").toString()) == 5) {
                    message = "El link de pago al que hace referencia se encuentra expirado";
                } else {
                    message = "Plataforma no disponible. Por favor, intente más tarde.";
                }

                manageLogs.errorLogger(logTo, httpServletRequest, responseEntity.getBody(),
                        "user", false);

                logger.error("Bank transaction not saved");
                return new Response("ERROR", message);
            }

        } catch (Exception e) {
            manageLogs.severeErrorLogger(logTo,httpServletRequest,"Error: "+ logTo.
                    getMethodName() + ": "+ e, e,"user",false);
            logger.error(e.getMessage(), e);

            return new Response("ERROR", null);
        }

    }

}
