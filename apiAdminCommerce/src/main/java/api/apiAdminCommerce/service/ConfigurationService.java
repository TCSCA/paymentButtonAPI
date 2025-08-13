package api.apiAdminCommerce.service;

import api.apiAdminCommerce.util.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.LinkedHashMap;
import java.util.Objects;

@Service
public class ConfigurationService {


    private static final Logger logger = LoggerFactory.getLogger(UserService.class);


    public Response editEmailsAdmin(final String emailReceipt, final String apiKey, final String token,
                                      final String emailSupport){

        try {

            HttpHeaders headers = new HttpHeaders();
            RestTemplate restTemplate = new RestTemplate();

            headers.add("API_KEY", apiKey);
            headers.add("token", token);

            String uri = "http://localhost:8090/core";

            LinkedHashMap<String, Object> requestBody = new LinkedHashMap<>();

            requestBody.put("emailSupport", emailSupport);
            requestBody.put("emailReceipt", emailReceipt);


            HttpEntity<LinkedHashMap<String, Object>> request = new HttpEntity<>(requestBody, headers);

            ResponseEntity<String> responseEntity = restTemplate.exchange(uri.
                    concat("/editEmailForAdmin"), HttpMethod.POST, request, String.class);

            if (!Objects.requireNonNull(responseEntity.getBody()).contains("ERROR")) {
                logger.info("Information saved successfully");
                return new Response("SUCCESS","Información  guardada exitosamente");
            } else {
                logger.error("Response Status: " + responseEntity.getStatusCode());
                return new Response("ERROR","No se pudo guardar la información");
            }

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new Response("ERROR",e.getMessage());
        }

    }

    public Response editEmailsAdminExt(final String emailReceipt, final String apiKey,
                                    final String emailSupport){

        try {

            HttpHeaders headers = new HttpHeaders();
            RestTemplate restTemplate = new RestTemplate();

            headers.add("API_KEY", apiKey);


            String uri = "http://localhost:8091/horizonte";

            LinkedHashMap<String, Object> requestBody = new LinkedHashMap<>();

            requestBody.put("emailSupport", emailSupport);
            requestBody.put("emailReceipt", emailReceipt);


            HttpEntity<LinkedHashMap<String, Object>> request = new HttpEntity<>(requestBody, headers);

            ResponseEntity<String> responseEntity = restTemplate.exchange(uri.
                    concat("/editEmailForAdminExt"), HttpMethod.POST, request, String.class);

            if (!Objects.requireNonNull(responseEntity.getBody()).contains("ERROR")) {
                logger.info("Information saved successfully");
                return new Response("SUCCESS","Información  guardada exitosamente");
            } else {
                logger.error("Response Status: " + responseEntity.getStatusCode());
                return new Response("ERROR","No se pudo guardar la información");
            }

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new Response("ERROR",e.getMessage());
        }

    }

}
