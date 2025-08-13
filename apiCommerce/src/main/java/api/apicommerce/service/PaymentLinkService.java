package api.apicommerce.service;

import api.apicommerce.request.PaymentLinkRequest;
import api.apicommerce.to.BankTransactionTo;
import api.apicommerce.util.Response;
import api.logsClass.LogsClass;
import api.logsClass.ManageLogs;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.LinkedHashMap;
import java.util.Objects;

@Service
public class PaymentLinkService {

    private static final Logger logger = LoggerFactory.getLogger(PaymentLinkService.class);

    private final ManageLogs manageLogs;

    public PaymentLinkService(ManageLogs manageLogs) {
        this.manageLogs = manageLogs;
    }

    @Transactional
    public Response generatePaymentLink(final PaymentLinkRequest paymentLinkRequest,
                                        final HttpServletRequest httpServletRequest) {

        LogsClass logTo = new LogsClass(httpServletRequest, new Object() {}.
                getClass().getEnclosingMethod().getName());

        try {

            HttpHeaders headers = new HttpHeaders();
            RestTemplate restTemplate = new RestTemplate();

            headers.add("API_KEY", httpServletRequest.getHeader("API_KEY"));
            headers.add("token", httpServletRequest.getHeader("token"));

            String uri = "http://localhost:8090/core";

            LinkedHashMap<String, Object> requestBody = new LinkedHashMap<>();

            requestBody.put("paymentLinkRequest", paymentLinkRequest);

            HttpEntity<LinkedHashMap<String, Object>> request = new HttpEntity<>(requestBody, headers);

            ResponseEntity<String> responseEntity = restTemplate.exchange(uri.
                    concat("/generatePaymentLink"), HttpMethod.POST, request, String.class);

            if (!Objects.requireNonNull(responseEntity.getBody()).contains("ERROR")) {
                manageLogs.infoLogger(logTo,httpServletRequest,
                        "Successfully: " + logTo.getMethodName(),
                        "user",true);
                logger.info("Bank transaction saved successfully");

                ObjectMapper objectMapper = new ObjectMapper();

                LinkedHashMap<String, Object> responseMap = objectMapper.readValue(responseEntity.
                        getBody(), LinkedHashMap.class);

                return new Response("SUCCESS", objectMapper.
                        convertValue(responseMap.get("idBankTransaction"), Long.class));

            } else {
                manageLogs.errorLogger(logTo, httpServletRequest, responseEntity.getBody(),
                        "user", true);
                logger.error("Bank transaction not saved");
                return new Response("ERROR", null);
            }

        } catch (Exception e) {
            manageLogs.severeErrorLogger(logTo,httpServletRequest,"Error: "+ logTo.
                    getMethodName() + ": "+ e, e,"user",true);
            logger.error(e.getMessage(), e);

            return new Response("ERROR", null);
        }

    }

    public Response getInformationByIdBankTransaction(final Long idBankTransaction,
                                                               HttpServletRequest httpServletRequest) {

        LogsClass logTo = new LogsClass(httpServletRequest, new Object() {}.
                getClass().getEnclosingMethod().getName());

        try {

            HttpHeaders headers = new HttpHeaders();
            RestTemplate restTemplate = new RestTemplate();

            headers.add("API_KEY", httpServletRequest.getHeader("API_KEY"));

            String uri = "http://localhost:8090/core";

            LinkedHashMap<String, Object> requestBody = new LinkedHashMap<>();

            requestBody.put("idBankTransaction", idBankTransaction);

            HttpEntity<LinkedHashMap<String, Object>> request = new HttpEntity<>(requestBody, headers);

            ResponseEntity<String> responseEntity = restTemplate.exchange(uri.
                    concat("/getInformationByIdBankTransaction/"+idBankTransaction), HttpMethod.GET,
                    request, String.class);

            if (!Objects.requireNonNull(responseEntity.getBody()).contains("ERROR")) {
                manageLogs.infoLogger(logTo,httpServletRequest,
                        "Successfully: " + logTo.getMethodName(),
                        "user",true);
                logger.info("Bank transaction information found successfully");

                ObjectMapper objectMapper = new ObjectMapper();

                LinkedHashMap<String, Object> responseMap = objectMapper.readValue(responseEntity.getBody(), LinkedHashMap.class);

                return new Response("SUCCESS", new BankTransactionTo(objectMapper, responseMap));

            } else {
                manageLogs.errorLogger(logTo, httpServletRequest, responseEntity.getBody(),
                        "user", true);
                logger.error("Bank transaction not saved");
                return new Response("ERROR", null);
            }

        } catch (Exception e) {
            manageLogs.severeErrorLogger(logTo,httpServletRequest,"Error: "+ logTo.
                    getMethodName() + ": "+ e, e,"user",true);
            logger.error(e.getMessage(), e);

            return new Response("ERROR", null);
        }

    }

}
