package api.preRegistro.service;

import api.logsClass.LogsClass;
import api.logsClass.ManageLogs;
import api.preRegistro.to.BankTo;
import api.preRegistro.util.Response;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class BankService {

    private final ManageLogs manageLogs;

    private final Environment environment;

    public BankService(ManageLogs manageLogs, Environment environment) {
        this.manageLogs = manageLogs;
        this.environment = environment;
    }

    public Response getAllBanksByStatusTrue(final String apiKey, final HttpServletRequest httpServletRequest) {

        LogsClass logTo = new LogsClass(httpServletRequest, new Object() {}.
                getClass().getEnclosingMethod().getName());

        try {

            HttpHeaders headers = new HttpHeaders();
            RestTemplate restTemplate = new RestTemplate();

            headers.add("API_KEY", apiKey);

            String uri = environment.getProperty("app.route.internal");

            HttpEntity<LinkedHashMap<String, Object>> request = new HttpEntity<>(headers);

            ResponseEntity<String> responseEntity = restTemplate.exchange(uri.
                    concat("/getAllBanks"), HttpMethod.GET, request, String.class);

            if (!Objects.requireNonNull(responseEntity.getBody()).contains("ERROR")) {
                ObjectMapper objectMapper = new ObjectMapper();

                LinkedHashMap<String, Object> responseMap = objectMapper.readValue(responseEntity.getBody(), LinkedHashMap.class);
                List<LinkedHashMap<String, Object>> bankCommerceList = (List<LinkedHashMap<String, Object>>) responseMap.get("bankEntities");

                List<BankTo> bankTos = bankCommerceList.stream().map(map -> {
                    BankTo bankTo = new BankTo();
                    bankTo.setIdBank(((Number) map.get("idBank")).longValue());
                    bankTo.setBank((String) map.get("bankName"));
                    bankTo.setBankCode((String) map.get("bankCode"));
                    bankTo.setStatus((Boolean) map.get("status"));
                    return bankTo;
                }).collect(Collectors.toList());

                manageLogs.infoLogger(logTo,httpServletRequest,
                        "Successfully: " + logTo.getMethodName(),
                        "user",false);

                return new Response("SUCCESS", bankTos);

            } else {
                manageLogs.errorLogger(logTo, httpServletRequest,
                        "Error mapeando la lista de bancos", "user", false);

                return new Response("SUCCESS", "No data");
            }

        } catch (Exception e){
            manageLogs.severeErrorLogger(logTo,httpServletRequest,"Error: "+ logTo.
                    getMethodName() + ": "+ e, e,"user",false);
            return null;
        }

    }

}
