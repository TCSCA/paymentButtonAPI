package api.apiAdminCommerce.service;

import api.apiAdminCommerce.util.Response;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.xml.transform.sax.SAXResult;
import java.util.LinkedHashMap;
import java.util.Objects;

@Service
public class BillingService {

    public Response manualBillingMethod(String apiKey, String token, String rif, String billingDate) {

        try {

            HttpHeaders headers = new HttpHeaders();
            RestTemplate restTemplate = new RestTemplate();

            headers.add("API_KEY", apiKey);
            headers.add("token", token);

            String uri = "http://localhost:8090/core";

            LinkedHashMap<String, Object> requestBody = new LinkedHashMap<>();

            requestBody.put("rif", rif);
            requestBody.put("billingDate", billingDate);


            HttpEntity<LinkedHashMap<String, Object>> request = new HttpEntity<>(requestBody, headers);

            ResponseEntity<String> responseEntity = restTemplate.exchange(uri.
                    concat("/dairyBillingByCommerce"), HttpMethod.POST, request, String.class);

            LinkedHashMap<String, Object> response = new LinkedHashMap<>();

            if (!Objects.requireNonNull(responseEntity.getBody()).contains("ERROR")) {
                return new Response("SUCCESS", "Transacción exitosa");

            } else {

                ObjectMapper objectMapper = new ObjectMapper();
                LinkedHashMap<String, Object> responseMap = objectMapper.readValue(responseEntity.getBody(), LinkedHashMap.class);
                String error = objectMapper.convertValue(responseMap.get("ERROR"), String.class);
                response.put("ERROR", error);

                return new Response("ERROR", error);
            }

        } catch (Exception e) {
            return null;
        }
    }
}
