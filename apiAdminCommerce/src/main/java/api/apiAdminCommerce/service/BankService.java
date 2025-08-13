package api.apiAdminCommerce.service;

import api.apiAdminCommerce.repository.intDbRepository.BankRepository;
import api.apiAdminCommerce.to.BankTo;
import api.apiAdminCommerce.util.Response;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class BankService {

    private final BankRepository bankRepository;

    public BankService(BankRepository bankRepository) {
        this.bankRepository = bankRepository;
    }

    /*@Transactional(transactionManager = "secondTransactionManager")
    public Response getAllBanksByStatusTrue() {
        *//*List<BankEntity> bankEntities = bankRepository.findAllByStatusTrue();*//*

        *//*if (bankEntities.isEmpty()) {
            return new Response("SUCCESS", "No data");
        } else {
            return new Response("SUCCESS", bankEntities);
        }*//*
        return new Response();
    }*/

    public Response getAllBanksByStatusTrue(final String apiKey, final String token) {

        try {

            HttpHeaders headers = new HttpHeaders();
            RestTemplate restTemplate = new RestTemplate();

            headers.add("API_KEY", apiKey);
            headers.add("token", token);

            String uri = "http://localhost:8090/core";

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

                return new Response("SUCCESS", bankTos);

            } else {
                return new Response("SUCCESS", "No data");
            }

        } catch (Exception e){
            /*logger.error(e.getMessage(), e);*/
            return null;
        }

    }

}
