package api.preRegistro.service;

import api.logsClass.LogsClass;
import api.logsClass.ManageLogs;
import api.preRegistro.entity.DirectionEntity;
import api.preRegistro.entity.PreRegistroEntity;
import api.preRegistro.entity.StatusPreRegistroEntity;
import api.preRegistro.repository.DirectionRepository;
import api.preRegistro.repository.PreRegistroRepository;
import api.preRegistro.to.PlanTo;
import api.preRegistro.to.TypeCommerceTo;
import api.preRegistro.util.Response;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class PreRegistroService {

    private static final Logger logger = LoggerFactory.getLogger(PreRegistroService.class);

    private final PreRegistroRepository preRegistroRepository;

    private final DirectionRepository directionRepository;

    private final PreRegisterEmailService preRegisterEmailService;

    private final ManageLogs manageLogs;

    private final Environment environment;

    public PreRegistroService(PreRegistroRepository preRegistroRepository, DirectionRepository directionRepository, PreRegisterEmailService preRegisterEmailService, ManageLogs manageLogs, Environment environment) {
        this.preRegistroRepository = preRegistroRepository;
        this.directionRepository = directionRepository;
        this.preRegisterEmailService = preRegisterEmailService;
        this.manageLogs = manageLogs;
        this.environment = environment;
    }

    @Transactional
    public Response savePreRegistro(PreRegistroEntity preRegistroEntity, final HttpServletRequest httpServletRequest) {

        LogsClass logTo = new LogsClass(httpServletRequest, new Object() {}.
                getClass().getEnclosingMethod().getName());

        try {

            preRegistroEntity.setStatusPreRegistroEntity(new StatusPreRegistroEntity(1L));

            PreRegistroEntity preRegistroEntitySave = setAddressInformation(preRegistroEntity);

            preRegistroEntitySave.setRegisterDate(OffsetDateTime.now(ZoneId.of("America/Caracas")));
            preRegistroEntitySave.setUpdateDate(OffsetDateTime.now(ZoneId.of("America/Caracas")));


            preRegistroRepository.save(preRegistroEntitySave);
//            preRegisterEmailService.generatePreregisterEmail(sendTo,sendTo2,fullName,httpServletRequest.getHeader("API_KEY"));

            return new Response("SUCCESS", "Se guardo correctamente");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            manageLogs.severeErrorLogger(logTo,httpServletRequest,"Error: "+ logTo.
                    getMethodName() + ": "+ e, e,"user",false);

            return new Response("Error", e);
        }
    }

    @Transactional
    public PreRegistroEntity setAddressInformation(PreRegistroEntity preRegistroEntity) {

        DirectionEntity directionEntity = new DirectionEntity();
        directionEntity.setStatus(true);
        directionEntity.setStateEntity(preRegistroEntity.getDirectionEntity().getStateEntity());
        directionEntity.setAddress(preRegistroEntity.getDirectionEntity().getAddress());
        directionRepository.save(directionEntity);
        preRegistroEntity.setDirectionEntity(directionEntity);
        return preRegistroEntity;
    }

    public Response getPreRegistroById(Long idPreRegistro, final HttpServletRequest httpServletRequest) {

        LogsClass logTo = new LogsClass(httpServletRequest, new Object() {}.
                getClass().getEnclosingMethod().getName());

        try {
            PreRegistroEntity preRegistroEntity = preRegistroRepository.findByIdPreRegistro(idPreRegistro);
            return new Response("SUCCESS", preRegistroEntity);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            manageLogs.severeErrorLogger(logTo,httpServletRequest,"Error: "+ logTo.
                    getMethodName() + ": "+ e, e,"user",false);

            return new Response("Error", e);
        }
    }

    public Response getAllPreRegistros() {

        List<PreRegistroEntity> preRegistroEntityList;
        preRegistroEntityList = preRegistroRepository.findAll();

        if (preRegistroEntityList.isEmpty()) {
            return new Response("ERROR", "No data");
        } else {
            return new Response("SUCCESS", preRegistroEntityList);
        }

    }

    public Response getAllPreRegistrosByStatus(final Long idStatus, final int index, final int quantity,
                                               final String apiKey,
                                               final String token,
                                               final Boolean export,
                                               final HttpServletRequest httpServletRequest) {

        LogsClass logTo = new LogsClass(httpServletRequest, new Object() {
        }.
                getClass().getEnclosingMethod().getName());
        ObjectMapper objectMapper = new ObjectMapper();
        try {

            HttpHeaders headers = new HttpHeaders();
            RestTemplate restTemplate = new RestTemplate();

            headers.add("API_KEY", apiKey);
            headers.add("token", token);

            String uri = "http://localhost:8091/horizonte";

            LinkedHashMap<String, Object> requestBody = new LinkedHashMap<>();

            requestBody.put("idStatusPreRegister", idStatus);
            requestBody.put("index", index);
            requestBody.put("quantity", quantity);
            requestBody.put("export", export);


            HttpEntity<LinkedHashMap<String, Object>> request = new HttpEntity<>(requestBody, headers);

            if (export) {
                ResponseEntity<String> responseEntity = restTemplate.exchange(uri.
                        concat("/getAllPreRegistersByStatusExport"), HttpMethod.POST, request, String.class);

                if (!Objects.requireNonNull(responseEntity.getBody()).contains("ERROR")) {
                    LinkedHashMap<String, Object> responseMap = objectMapper.readValue(responseEntity.getBody(), LinkedHashMap.class);
                    List<LinkedHashMap<String, Object>> linkedHashMaps = (List<LinkedHashMap<String, Object>>) responseMap.get("preRegisterEntities");

                    return new Response("SUCCESS", linkedHashMaps);
                } else {
                    logger.error("Response Status: " + responseEntity.getStatusCode());
                    return new Response("ERROR", "No se pudo guardar la información");
                }

            } else {
                ResponseEntity<String> responseEntity = restTemplate.exchange(uri.
                        concat("/getAllPreRegistersByStatus"), HttpMethod.POST, request, String.class);

                if (!Objects.requireNonNull(responseEntity.getBody()).contains("ERROR")) {
                    LinkedHashMap<String, Object> responseMap = objectMapper.readValue(responseEntity.getBody(), LinkedHashMap.class);
                    LinkedHashMap<String, Object> paginatedResponse = new LinkedHashMap<>();
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
            }


        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            manageLogs.severeErrorLogger(logTo, httpServletRequest, "Error: " + logTo.
                    getMethodName() + ": " + e, e, "user", false);
            return null;

        }
    }

    public Response getAllPreregistroFilter(final String apiKey, final String token,
                                           final int index,final int quantity,
                                           final String startDate,final String endDate,
                                           final String filter, final Integer typeFilter, final Boolean export, final Long idStatus){

        try {

            HttpHeaders headers = new HttpHeaders();
            RestTemplate restTemplate = new RestTemplate();
            ObjectMapper objectMapper = new ObjectMapper();

            headers.add("API_KEY", apiKey);
            headers.add("token", token);

            String uri = environment.getProperty("api.route.external");

            LinkedHashMap<String, Object> requestBody = new LinkedHashMap<>();

            requestBody.put("filter", filter);
            requestBody.put("typeFilter", typeFilter);
            requestBody.put("startDate", startDate);
            requestBody.put("endDate", endDate);
            requestBody.put("typeFilter", typeFilter);
            requestBody.put("index", index);
            requestBody.put("quantity", quantity);
            requestBody.put("export", export);
            requestBody.put("idStatusPreregister", idStatus);

            HttpEntity<LinkedHashMap<String, Object>> request = new HttpEntity<>(requestBody, headers);

            ResponseEntity<String> responseEntity = restTemplate.exchange(uri.
                    concat("/getAllPreRegistersByFilter"), HttpMethod.POST, request, String.class);

            if (!Objects.requireNonNull(responseEntity.getBody()).contains("ERROR")) {
                LinkedHashMap<String, Object> responseMap = objectMapper.readValue(responseEntity.getBody(), LinkedHashMap.class);
                List<PreRegistroEntity> preRegistroEntityList = (List<PreRegistroEntity>) responseMap.get("preregistroFilter");

                if(export) {
                    return new Response("SUCCESS", preRegistroEntityList);
                } else {

                    Pageable pageList = PageRequest.of(index, quantity);
                    int start = (int) pageList.getOffset();
                    int end = Math.min((start + pageList.getPageSize()), preRegistroEntityList.size());

                    Page<PreRegistroEntity> preRegistroEntityPage =  new
                            PageImpl<>(preRegistroEntityList.subList(start, end), pageList, preRegistroEntityList.size());

                    return new Response("SUCCESS", preRegistroEntityPage);
                }

            } else {
                logger.error("Response Status: " + responseEntity.getStatusCode());
                return null;
            }

        } catch (Exception e){
            logger.error(e.getMessage(), e);
            return null;
        }

    }

    public Boolean verifyCommerceDocument(String commerceDocument) {
        PreRegistroEntity preRegistroEntity = preRegistroRepository.
                getPreregisterByCommerceDocumentAndIdStatusDocument(3L, commerceDocument);
        if(preRegistroEntity == null) {
            return true;
        } else {
            return false;
        }
    }

    public Response getAllTypeDocument(final String apiKey, final HttpServletRequest httpServletRequest,
                                       final LogsClass logTo) {

        try {

            HttpHeaders headers = new HttpHeaders();
            RestTemplate restTemplate = new RestTemplate();

            headers.add("API_KEY", apiKey);

            String uri = environment.getProperty("app.route.external");

            HttpEntity<LinkedHashMap<String, Object>> request = new HttpEntity<>(headers);

            ResponseEntity<String> responseEntity = restTemplate.exchange(uri.
                    concat("/getAllTypeDocument"), HttpMethod.GET, request, String.class);

            if (!Objects.requireNonNull(responseEntity.getBody()).contains("ERROR")) {
                ObjectMapper objectMapper = new ObjectMapper();

                LinkedHashMap<String, Object> responseMap = objectMapper.readValue(responseEntity.getBody(), LinkedHashMap.class);
                List<LinkedHashMap<String, Object>> typeCommerceList = (List<LinkedHashMap<String, Object>>) responseMap.get("typeCommerceEntities");

                List<TypeCommerceTo> typeCommerceTos = typeCommerceList.stream()
                        .map(map -> objectMapper.convertValue(map, TypeCommerceTo.class))
                        .collect(Collectors.toList());

                return new Response("SUCCESS", typeCommerceTos);

            } else {
                return new Response("SUCCESS", "No data");
            }

        } catch (Exception e){
            logger.error(e.getMessage(), e);
            manageLogs.severeErrorLogger(logTo,httpServletRequest,"Error: "+ logTo.
                    getMethodName() + ": "+ e, e,"user",false);
            return null;
        }

    }

    public Response getAllPlanByIdTypeCommerce(final String apiKey, final Long idTypeCommerce,
                                               final HttpServletRequest httpServletRequest,
                                               final LogsClass logTo) {

        try {

            HttpHeaders headers = new HttpHeaders();
            RestTemplate restTemplate = new RestTemplate();

            headers.add("API_KEY", apiKey);

            String uri = environment.getProperty("app.route.external");

            HttpEntity<LinkedHashMap<String, Object>> request = new HttpEntity<>(headers);

            ResponseEntity<String> responseEntity = restTemplate.exchange(uri.
                    concat("/getAllPlanByIdTypeCommerce/"+idTypeCommerce), HttpMethod.GET, request, String.class);

            if (!Objects.requireNonNull(responseEntity.getBody()).contains("ERROR")) {
                ObjectMapper objectMapper = new ObjectMapper();

                LinkedHashMap<String, Object> responseMap = objectMapper.readValue(responseEntity.getBody(), LinkedHashMap.class);
                List<LinkedHashMap<String, Object>> planList = (List<LinkedHashMap<String, Object>>) responseMap.get("planEntities");

                List<PlanTo> planTos = planList.stream().map(map -> {
                    PlanTo planTo = new PlanTo();
                    planTo.setIdPlan(((Number) map.get("idPlan")).longValue());
                    planTo.setPlanName((String) map.get("planName"));
                    planTo.setStatus((Boolean) map.get("status"));
                    LinkedHashMap<String, Object> typeCommerceEntity = (LinkedHashMap<String, Object>) map.get("typeCommerceEntity");
                    Long idTypeCommerceInner = ((Number) typeCommerceEntity.get("idTypeCommerce")).longValue();
                    planTo.setIdTypeCommerce(idTypeCommerceInner);
                    return planTo;
                }).collect(Collectors.toList());

                return new Response("SUCCESS", planTos);

            } else {
                return new Response("SUCCESS", "No data");
            }

        } catch (Exception e){
            logger.error(e.getMessage(), e);
            manageLogs.severeErrorLogger(logTo,httpServletRequest,"Error: "+ logTo.
                    getMethodName() + ": "+ e, e,"user",false);
            return null;
        }

    }

    public Response editProfileByPreRegister(final Long idPreRegister, final String commerceName,
            final String commerceDocument, final String contactPerson,
            final String address, final Long idState, final String contactPersonEmail,
            final String phoneNumber, final Long idPlan, final HttpServletRequest httpServletRequest) {

        try {

            HttpHeaders headers = new HttpHeaders();
            RestTemplate restTemplate = new RestTemplate();

            headers.add("API_KEY", httpServletRequest.getHeader("API_KEY"));
            headers.add("token", httpServletRequest.getHeader("token"));

            String uriInternal = environment.getProperty("api.route.internal");

            LinkedHashMap<String, Object> requestBodyInternal = new LinkedHashMap<>();

            requestBodyInternal.put("commerceDocument", commerceDocument);

            HttpEntity<LinkedHashMap<String, Object>> requestInternal = new HttpEntity<>(requestBodyInternal, headers);

            ResponseEntity<String> responseEntityInternal = restTemplate.exchange(Objects.requireNonNull(uriInternal).
                            concat("/validateExistCommerceDocument"), HttpMethod.POST,
                    requestInternal, String.class);

            if (!Objects.requireNonNull(responseEntityInternal.getBody()).contains("ERROR")) {

                String uri = environment.getProperty("api.route.external");

                LinkedHashMap<String, Object> requestBody = new LinkedHashMap<>();

                requestBody.put("idPreRegister", idPreRegister);
                requestBody.put("commerceName", commerceName);
                requestBody.put("commerceDocument", commerceDocument);
                requestBody.put("contactPerson", contactPerson);
                requestBody.put("address", address);
                requestBody.put("idState", idState);
                requestBody.put("contactPersonEmail", contactPersonEmail);
                requestBody.put("phoneNumberCommerce", phoneNumber);
                requestBody.put("idPlan", idPlan);

                HttpEntity<LinkedHashMap<String, Object>> request = new HttpEntity<>(requestBody, headers);

                ResponseEntity<String> responseEntity = restTemplate.exchange(Objects.requireNonNull(uri).
                                concat("/editProfileByPreRegister"), HttpMethod.POST,
                        request, String.class);

                if (!Objects.requireNonNull(responseEntity.getBody()).contains("ERROR")) {

                    ObjectMapper objectMapper = new ObjectMapper();

                    LinkedHashMap<String, Object> responseMap = objectMapper.readValue(responseEntity.getBody(), LinkedHashMap.class);

                    String message = objectMapper.convertValue(responseMap.get("message"), String.class);

                    logger.info("PreRegister information changed successfully");
                    return new Response("SUCCESS", message);

                } else {

                    ObjectMapper objectMapper = new ObjectMapper();

                    LinkedHashMap<String, Object> responseMap = objectMapper.readValue(responseEntity.getBody(), LinkedHashMap.class);

                    String message = objectMapper.convertValue(responseMap.get("ERROR"), String.class);

                    logger.error("Response Status: " + responseEntity.getBody());
                    return new Response("ERROR", message);
                }

            } else {
                logger.error("El RIF ingresado pertenece a un comercio que esta aprobado");
                return new Response("ERROR", "El RIF ingresado pertenece a un comercio que esta aprobado");
            }

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return null;
        }

    }


    public Response getPreregisterByDateExport(final String apiKey, final HttpServletRequest httpServletRequest,
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
                    concat("/getFilterPreregisterByDateExport"), HttpMethod.POST, request, String.class);

            if (!Objects.requireNonNull(responseEntity.getBody()).contains("ERROR")) {
                LinkedHashMap<String, Object> responseMap = objectMapper.readValue(responseEntity.getBody(), LinkedHashMap.class);
                List<LinkedHashMap<String, Object>> preRegisterByDate = (List<LinkedHashMap<String, Object>>) responseMap.get("preRegisterByDate");




                return new Response("SUCCESS", preRegisterByDate);
            } else {
                logger.error("Response Status: " + responseEntity.getStatusCode());
                return new Response("ERROR", "No se pudo guardar la información");
            }


        } catch (Exception e){
            logger.error(e.getMessage(), e);
            return new Response("ERROR", e.getMessage());
        }

    }


    public Response getPreregisterByDate(final String apiKey, final HttpServletRequest httpServletRequest,
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
                    concat("/getFilterPreregisterByDate"), HttpMethod.POST, request, String.class);

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

}
