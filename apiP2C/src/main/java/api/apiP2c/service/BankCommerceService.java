package api.apiP2c.service;

import api.apiP2c.entity.BankCommerceEntity;
import api.apiP2c.repository.BankCommerceRepository;
import api.apiP2c.to.CommerceBankInformationTo;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.LinkedHashMap;
import java.util.Objects;

@Service
public class BankCommerceService {

    private final BankCommerceRepository bankCommerceRepository;

    public BankCommerceService(BankCommerceRepository bankCommerceRepository) {
        this.bankCommerceRepository = bankCommerceRepository;
    }


    public BankCommerceEntity getBankCommerceByDocument(String document) {
        BankCommerceEntity bankCommerceEntity = new BankCommerceEntity();
        return bankCommerceEntity;
    }

    @Transactional
    public void saveBankCommerceToken(String token, Long idBankCommerce, Long seconds) {
        Long minutes = seconds / 60;
        OffsetDateTime currentDate = OffsetDateTime.now(ZoneId.of("America/Caracas")).plusMinutes(minutes);
        bankCommerceRepository.updateBankCommerceToken(token,idBankCommerce, currentDate);
    }

    public CommerceBankInformationTo getCommerceBankInformationByRif(final String rif, final String apiKey) {

        try {

            HttpHeaders headers = new HttpHeaders();
            RestTemplate restTemplate = new RestTemplate();

            headers.add("API_KEY", apiKey);

            String uri = "http://localhost:8090/core";

            LinkedHashMap<String, Object> requestBody = new LinkedHashMap<>();

            requestBody.put("rif", rif);

            HttpEntity<LinkedHashMap<String, Object>> request = new HttpEntity<>(requestBody, headers);

            ResponseEntity<String> responseEntity = restTemplate.exchange(uri.
                    concat("/getCommerceBankInformation"), HttpMethod.POST, request, String.class);

            if (!Objects.requireNonNull(responseEntity.getBody()).contains("ERROR")) {


                ObjectMapper objectMapper = new ObjectMapper();

                LinkedHashMap<String, Object> responseMap = objectMapper.readValue(responseEntity.getBody(), LinkedHashMap.class);
                return new CommerceBankInformationTo(objectMapper, responseMap);

            } else {

                return null;
            }

        } catch (Exception e) {


            return null;
        }

    }

}
