package api.apiAdminCommerce.service;

import api.apiAdminCommerce.to.AllRequirementsTo;
import api.apiAdminCommerce.to.PreRegisterTo;
import api.apiAdminCommerce.to.RequirementTo;
import api.apiAdminCommerce.util.ManageLog;
import api.apiAdminCommerce.util.Response;
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

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;

@Service
public class AdminCommerceService {

    private static final Logger logger = LoggerFactory.getLogger(AdminCommerceService.class);

    private final ManageLog manageLog;

    private final ManageLogs manageLogs;

    public AdminCommerceService(ManageLog manageLog, ManageLogs manageLogs) {
        this.manageLog = manageLog;
        this.manageLogs = manageLogs;
    }

    public Boolean setStatusPreRegisterExtGetWay(Long idStatus, Long idPreRegister, String apiKey,
                                                 String token, LogsClass logTo,
                                                 HttpServletRequest httpServletRequest) {

        try {

            HttpHeaders headers = new HttpHeaders();
            RestTemplate restTemplate = new RestTemplate();

            headers.add("API_KEY", apiKey);
            headers.add("token", token);

            String uri = "http://localhost:8091/horizonte";

            LinkedHashMap<String, Object> requestBody = new LinkedHashMap<>();

            requestBody.put("idStatus", idStatus);
            requestBody.put("idPreRegister", idPreRegister);

            HttpEntity<LinkedHashMap<String, Object>> request = new HttpEntity<>(requestBody, headers);

            ResponseEntity<String> responseEntity = restTemplate.exchange(uri.
                    concat("/setStatusPreRegisterExt"), HttpMethod.POST, request, String.class);

            if (!Objects.requireNonNull(responseEntity.getBody()).contains("ERROR")) {
                manageLogs.infoLogger(logTo,httpServletRequest,
                        "Successfully: PreRegisterStatus changed",
                        "user",true);

                logger.info("PreRegisterStatus changed succesfully");
                return true;
            } else {
                manageLogs.errorLogger(logTo, httpServletRequest, responseEntity.getBody(),
                        "unlogged user", false);

                logger.error("Response Status: " + responseEntity.getStatusCode());
                logger.error("Response Headers: " + responseEntity.getHeaders());
                return false;
            }

        } catch (Exception e) {

            manageLogs.severeErrorLogger(logTo,httpServletRequest,"Error actualizando el status del pre-registro "+ logTo.
                    getMethodName() + ": "+ e, e,"user",false);

            e.printStackTrace();
            return false;
        }
    }

    @Transactional
    public PreRegisterTo getPreregisterByIdGetWay(final Long idPreRegister, final String apiKey,
                                                  final String token) {

        try {

            HttpHeaders headers = new HttpHeaders();
            RestTemplate restTemplate = new RestTemplate();

            headers.add("API_KEY", apiKey);
            headers.add("token", token);

            String uri = "http://localhost:8091/horizonte";

            HttpEntity<LinkedHashMap<String, Object>> request = new HttpEntity<>(headers);

            ResponseEntity<String> responseEntity = restTemplate.exchange(uri.
                    concat("/getPreRegisterById/"+idPreRegister), HttpMethod.GET, request, String.class);

            if (!Objects.requireNonNull(responseEntity.getBody()).contains("ERROR")) {
                ObjectMapper objectMapper = new ObjectMapper();

                LinkedHashMap<String, Object> responseMap = objectMapper.readValue(responseEntity.getBody(), LinkedHashMap.class);

                return new PreRegisterTo(objectMapper, responseMap);

            } else {
                logger.error("Bad response");
                return null;
            }

        } catch (Exception e){
            logger.error(e.getMessage(), e);
            return null;
        }

    }

    @Transactional
    public ResponseEntity<String> changePreRegisterStatusGetWay(Long idPreRegister, Long statusPreRegister,
                                                 String rejectMotive, final String apiKey,
                                                 final String token, final LogsClass logTo,
                                                 final HttpServletRequest httpServletRequest) {
        try {
            HttpHeaders headers = new HttpHeaders();
            RestTemplate restTemplate = new RestTemplate();

            headers.add("API_KEY", apiKey);
            headers.add("token", token);

            String uri = "http://localhost:8091/horizonte";

            LinkedHashMap<String, Object> requestBody = new LinkedHashMap<>();

            requestBody.put("idPreRegister", idPreRegister);
            requestBody.put("statusPreRegister", statusPreRegister);
            requestBody.put("rejectMotive", rejectMotive);

            HttpEntity<LinkedHashMap<String, Object>> request = new HttpEntity<>(requestBody, headers);

            ResponseEntity<String> responseEntity = restTemplate.exchange(uri.
                    concat("/changePreRegisterStatus"), HttpMethod.POST, request, String.class);

            return responseEntity;

        } catch (Exception e) {
            logger.error(e.getMessage(), e);

            manageLogs.severeErrorLogger(logTo,httpServletRequest,"Error: "+ logTo.
                    getMethodName() + ": "+ e, e,"user",false);

            return null;
        }
    }

    public Response getAllRequirement(final Long idTypeCommerce,
                                      final Long idPreRegister,
                                      final HttpServletRequest httpServletRequest) {

        try {

            HttpHeaders headers = new HttpHeaders();
            RestTemplate restTemplate = new RestTemplate();

            headers.add("API_KEY", httpServletRequest.getHeader("API_KEY"));
            headers.add("token", httpServletRequest.getHeader("token"));

            String uri = "http://localhost:8091/horizonte";

            LinkedHashMap<String, Object> requestBody = new LinkedHashMap<>();

            requestBody.put("idPreRegister", idPreRegister);
            requestBody.put("idTypeCommerce", idTypeCommerce);

            HttpEntity<LinkedHashMap<String, Object>> request = new HttpEntity<>(requestBody, headers);

            ResponseEntity<String> responseEntity = restTemplate.exchange(uri.
                    concat("/getAllRequirement"), HttpMethod.POST, request, String.class);

            if (!Objects.requireNonNull(responseEntity.getBody()).contains("ERROR")) {
                ObjectMapper objectMapper = new ObjectMapper();

                LinkedHashMap<String, Object> responseMap = objectMapper.readValue(responseEntity.
                        getBody(), LinkedHashMap.class);

                List<LinkedHashMap<String, Object>> requirementChargedList =
                        (List<LinkedHashMap<String, Object>>) responseMap.get("requirementEntitiesCharged");

                List<RequirementTo> requirementChargedTos;

                if (requirementChargedList.size() == 0){
                    requirementChargedTos = new ArrayList<>();
                } else {

                    requirementChargedTos = requirementChargedList.stream().map(map -> {
                        RequirementTo requirementChargedTo = new RequirementTo();
                        requirementChargedTo.setIdRequirement(((Number) map.get("idRequirement")).longValue());
                        requirementChargedTo.setRecaudo((String) map.get("recaudo"));
                        requirementChargedTo.setRequired((Boolean) map.get("required"));
                        requirementChargedTo.setStatus((Boolean) map.get("status"));

                        return requirementChargedTo;
                    }).toList();

                }

                List<LinkedHashMap<String, Object>> requirementNoChargedList =
                        (List<LinkedHashMap<String, Object>>) responseMap.get("requirementEntitiesNoCharged");

                List<RequirementTo> requirementNoChargedTos;

                if (requirementNoChargedList.size() == 0){
                    requirementNoChargedTos = new ArrayList<>();
                } else {

                    requirementNoChargedTos = requirementNoChargedList.stream().map(map -> {
                        RequirementTo requirementNoChargedTo = new RequirementTo();
                        requirementNoChargedTo.setIdRequirement(((Number) map.get("idRequirement")).longValue());
                        requirementNoChargedTo.setRecaudo((String) map.get("recaudo"));
                        requirementNoChargedTo.setRequired((Boolean) map.get("required"));
                        requirementNoChargedTo.setStatus((Boolean) map.get("status"));

                        return requirementNoChargedTo;
                    }).toList();

                }

                return new Response("SUCCESS", new AllRequirementsTo(requirementChargedTos, requirementNoChargedTos));

            } else {
                return new Response("SUCCESS", "No data");
            }

        } catch (Exception e){
            logger.error(e.getMessage(), e);
            return null;
        }

    }

    public Boolean validateChargedRequiredFilesByIdPreRegister(final Long idPreRegister,
                                                               final HttpServletRequest httpServletRequest,
                                                               final LogsClass logTo) {

        try {

            HttpHeaders headers = new HttpHeaders();
            RestTemplate restTemplate = new RestTemplate();

            headers.add("API_KEY", httpServletRequest.getHeader("API_KEY"));
            headers.add("token", httpServletRequest.getHeader("token"));

            String uri = "http://localhost:8091/horizonte";

            HttpEntity<LinkedHashMap<String, Object>> request = new HttpEntity<>(headers);

            ResponseEntity<String> responseEntity = restTemplate.exchange(uri.
                    concat("/validateChargedRequiredFilesByIdPreRegister/" + idPreRegister),
                    HttpMethod.GET, request, String.class);

            if (!Objects.requireNonNull(responseEntity.getBody()).contains("ERROR")) {

                ObjectMapper objectMapper = new ObjectMapper();

                LinkedHashMap<String, Object> responseMap = objectMapper.readValue(responseEntity.
                        getBody(), LinkedHashMap.class);

                manageLogs.infoLogger(logTo,httpServletRequest,
                        "Successfully: " + logTo.getMethodName(),
                        "user",true);

                return (Boolean) responseMap.get("SUCCESS");
            } else {
                manageLogs.errorLogger(logTo, httpServletRequest, responseEntity.getBody(),
                        "unlogged user", false);

                logger.error("All necessary files haven't been uploaded");
                return false;
            }

        } catch (Exception e) {
            logger.error(e.getMessage(), e);

            manageLogs.severeErrorLogger(logTo,httpServletRequest,"Error: Obteniendo los archivos requeridos. "+ logTo.
                    getMethodName() + ": "+ e, e,"user",false);

            return false;
        }

    }


    public Response getCommerceByDate(final String apiKey, final HttpServletRequest httpServletRequest,
                                      final int index,final int quantity,
                                      final String startDate,final String endDate, final String filter) {

        try {

            HttpHeaders headers = new HttpHeaders();
            ObjectMapper objectMapper = new ObjectMapper();
            RestTemplate restTemplate = new RestTemplate();

            headers.add("API_KEY", apiKey);

            String uri = "http://localhost:8090/core";

            LinkedHashMap<String, Object> requestBody = new LinkedHashMap<>();

            requestBody.put("startDate", startDate);
            requestBody.put("endDate", endDate);
            requestBody.put("page", index);
            requestBody.put("quantity", quantity);
            requestBody.put("filter", filter.toLowerCase());



            HttpEntity<LinkedHashMap<String, Object>> request = new HttpEntity<>(requestBody, headers);

            ResponseEntity<String> responseEntity = restTemplate.exchange(uri.
                    concat("/getFilterCommerceByDate"), HttpMethod.POST, request, String.class);

            if (!Objects.requireNonNull(responseEntity.getBody()).contains("ERROR")) {
                LinkedHashMap<String, Object> responseMap = objectMapper.readValue(responseEntity.getBody(), LinkedHashMap.class);
                LinkedHashMap<String,Object> paginatedResponse = new LinkedHashMap<>();
                LinkedHashMap linkedHashMapResponse = responseMap;
                paginatedResponse.put("content", linkedHashMapResponse.get("content"));
                paginatedResponse.put("pageable", linkedHashMapResponse.get("pageable"));
                paginatedResponse.put("total", linkedHashMapResponse.get("totalElements"));
                paginatedResponse.put("totalPages", linkedHashMapResponse.get("totalPages"));


                return new Response("SUCCESS", paginatedResponse);
            } else {
                logger.error("Response Status: " + responseEntity.getStatusCode());
                return new Response("ERROR", "No se pudo guardar la información");
            }


        } catch (Exception e){
            logger.error(e.getMessage(), e);
            return new Response("ERROR",e.getMessage());
        }

    }

    public Response getCommerceByDateExport(final String apiKey, final HttpServletRequest httpServletRequest,
                                            final int index,final int quantity,
                                            final String startDate,final String endDate,
                                            final Boolean export, final String filter) {

        try {

            HttpHeaders headers = new HttpHeaders();
            ObjectMapper objectMapper = new ObjectMapper();
            RestTemplate restTemplate = new RestTemplate();

            headers.add("API_KEY", apiKey);

            String uri = "http://localhost:8090/core";

            LinkedHashMap<String, Object> requestBody = new LinkedHashMap<>();

            requestBody.put("startDate", startDate);
            requestBody.put("endDate", endDate);
            requestBody.put("page", index);
            requestBody.put("quantity", quantity);
            requestBody.put("export", export);
            requestBody.put("filter", filter.toLowerCase());

            HttpEntity<LinkedHashMap<String, Object>> request = new HttpEntity<>(requestBody, headers);

            ResponseEntity<String> responseEntity = restTemplate.exchange(uri.
                    concat("/getFilterCommerceByDateExport"), HttpMethod.POST, request, String.class);

            if (!Objects.requireNonNull(responseEntity.getBody()).contains("ERROR")) {
                LinkedHashMap<String, Object> responseMap = objectMapper.readValue(responseEntity.getBody(), LinkedHashMap.class);
                List<LinkedHashMap<String, Object>> commerceFilter = (List<LinkedHashMap<String, Object>>) responseMap.get("commerceFilter");


                return new Response("SUCCESS", commerceFilter);

            } else {
                logger.error("Response Status: " + responseEntity.getStatusCode());
                return new Response("ERROR", "No se pudo guardar la información");
            }


        } catch (Exception e){
            logger.error(e.getMessage(), e);
            return new Response("ERROR",e.getMessage());
        }

    }

    public Response getRejectedByDate(final String apiKey, final HttpServletRequest httpServletRequest,
                                         final int index,final int quantity,
                                         final String startDate,final String endDate) {

        try {

            HttpHeaders headers = new HttpHeaders();
            ObjectMapper objectMapper = new ObjectMapper();
            RestTemplate restTemplate = new RestTemplate();

            headers.add("API_KEY", apiKey);

            String uri = "http://localhost:8091/horizonte";

            LinkedHashMap<String, Object> requestBody = new LinkedHashMap<>();

            requestBody.put("startDate", startDate);
            requestBody.put("endDate", endDate);
            requestBody.put("page", index);
            requestBody.put("quantity", quantity);



            HttpEntity<LinkedHashMap<String, Object>> request = new HttpEntity<>(requestBody, headers);

            ResponseEntity<String> responseEntity = restTemplate.exchange(uri.
                    concat("/getFilterRejectedByDate"), HttpMethod.POST, request, String.class);

            if (!Objects.requireNonNull(responseEntity.getBody()).contains("ERROR")) {
                LinkedHashMap<String, Object> responseMap = objectMapper.readValue(responseEntity.getBody(), LinkedHashMap.class);
                LinkedHashMap<String,Object> paginatedResponse = new LinkedHashMap<>();
                LinkedHashMap linkedHashMapResponse = responseMap;
                paginatedResponse.put("content", linkedHashMapResponse.get("content"));
                paginatedResponse.put("pageable", linkedHashMapResponse.get("pageable"));
                paginatedResponse.put("total", linkedHashMapResponse.get("totalElements"));
                paginatedResponse.put("totalPages", linkedHashMapResponse.get("totalPages"));


                return new Response("SUCCESS", paginatedResponse);
            } else {
                logger.error("Response Status: " + responseEntity.getStatusCode());
                return new Response("ERROR", "No se pudo guardar la información");
            }


        } catch (Exception e){
            logger.error(e.getMessage(), e);
            return new Response("ERROR", e.getMessage());
        }

    }

    public Response getRejectedByDateExport(final String apiKey, final HttpServletRequest httpServletRequest,
                                      final int index,final int quantity,
                                      final String startDate,final String endDate,final Boolean export) {

        try {

            HttpHeaders headers = new HttpHeaders();
            ObjectMapper objectMapper = new ObjectMapper();
            RestTemplate restTemplate = new RestTemplate();

            headers.add("API_KEY", apiKey);

            String uri = "http://localhost:8091/horizonte";

            LinkedHashMap<String, Object> requestBody = new LinkedHashMap<>();

            requestBody.put("startDate", startDate);
            requestBody.put("endDate", endDate);
            requestBody.put("page", index);
            requestBody.put("quantity", quantity);
            requestBody.put("export", export);



            HttpEntity<LinkedHashMap<String, Object>> request = new HttpEntity<>(requestBody, headers);

            ResponseEntity<String> responseEntity = restTemplate.exchange(uri.
                    concat("/getFilterRejectedByDateExport"), HttpMethod.POST, request, String.class);

            if (!Objects.requireNonNull(responseEntity.getBody()).contains("ERROR")) {
                LinkedHashMap<String, Object> responseMap = objectMapper.readValue(responseEntity.getBody(), LinkedHashMap.class);
                List<LinkedHashMap<String, Object>> rejectedByDate = (List<LinkedHashMap<String, Object>>) responseMap.get("rejectedByDate");



                return new Response("SUCCESS", rejectedByDate);
            } else {
                logger.error("Response Status: " + responseEntity.getStatusCode());
                return new Response("ERROR", "No se pudo guardar la información");
            }


        } catch (Exception e){
            logger.error(e.getMessage(), e);
            return new Response("ERROR", e.getMessage());
        }

    }


    public Response getAllCommerceInformation(final String apiKey, final HttpServletRequest httpServletRequest) {

        try {

            HttpHeaders headers = new HttpHeaders();
            ObjectMapper objectMapper = new ObjectMapper();
            RestTemplate restTemplate = new RestTemplate();

            headers.add("API_KEY", apiKey);

            String uri = "http://localhost:8090/core";

            LinkedHashMap<String, Object> requestBody = new LinkedHashMap<>();



            HttpEntity<LinkedHashMap<String, Object>> request = new HttpEntity<>(requestBody, headers);

            ResponseEntity<String> responseEntity = restTemplate.exchange(uri.
                    concat("/findCommerceInformation"), HttpMethod.POST, request, String.class);

            if (!Objects.requireNonNull(responseEntity.getBody()).contains("ERROR")) {
                LinkedHashMap<String, Object> responseMap = objectMapper.readValue(responseEntity.getBody(), LinkedHashMap.class);
                List<LinkedHashMap<String, Object>> commerceInformationList = (List<LinkedHashMap<String, Object>>) responseMap.get("commerceInformationList");



                return new Response("SUCCESS", commerceInformationList);
            } else {
                logger.error("Response Status: " + responseEntity.getStatusCode());
                return new Response("ERROR", "No se pudo guardar la información");
            }


        } catch (Exception e){
            logger.error(e.getMessage(), e);
            return new Response("ERROR",e.getMessage());
        }

    }

}
