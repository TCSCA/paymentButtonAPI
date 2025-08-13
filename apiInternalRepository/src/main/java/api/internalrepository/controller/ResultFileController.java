package api.internalrepository.controller;

import api.internalrepository.entity.CommerceEntity;
import api.internalrepository.entity.RequirementEntity;
import api.internalrepository.entity.ResultFileEntity;
import api.internalrepository.entity.TermsAndConditionsEntity;
import api.internalrepository.service.ResultFileService;
import api.internalrepository.to.ResultFileDetailTo;
import api.logsClass.LogsClass;
import api.logsClass.ManageLogs;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.http.HttpServletRequest;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/core")
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST})
public class ResultFileController {

    private static final Logger logger = LoggerFactory.getLogger(ResultFileService.class);

    private final ManageLogs manageLogs;

    private final ResultFileService resultFileService;

    public ResultFileController(ManageLogs manageLogs, ResultFileService resultFileService) {
        this.manageLogs = manageLogs;
        this.resultFileService = resultFileService;
    }

    @PostMapping("/saveResultFile")
    public LinkedHashMap<String, Object> saveResultFile(@RequestBody final String dataForm,
                                                        HttpServletRequest httpServletRequest) {

        LogsClass logTo = new LogsClass(httpServletRequest, new Object() {}.
                getClass().getEnclosingMethod().getName());

        LinkedHashMap<String, Object> response = new LinkedHashMap<>();

        try {

            Long size = Long.parseLong((new JSONObject(dataForm).get("size")).toString());
            Long chargedBy = Long.parseLong((new JSONObject(dataForm).get("chargedBy")).toString());
            Long idCommerce = Long.parseLong((new JSONObject(dataForm).get("idCommerce")).toString());
            Long idRequirement = Long.parseLong((new JSONObject(dataForm).get("idRequirement")).toString());
            String fileName = (new JSONObject(dataForm).get("fileName")).toString();

            Boolean result = resultFileService.saveResultFile(size, chargedBy, idCommerce, idRequirement,
                    fileName, httpServletRequest);

            response.put("status", result);

            manageLogs.infoLogger(logTo,httpServletRequest,
                    "Successfully: " + logTo.getMethodName(),
                    "user",false);

        } catch (Exception e) {
            manageLogs.severeErrorLogger(logTo,httpServletRequest,
                    "Error: "+ logTo.getMethodName() + ": "+ e,
                    e,"user",false);

            response.put("ERROR", false);
        }

        return response;
    }

    @PostMapping("/saveAllResultFileByCommerce")
    public LinkedHashMap<String, Object> saveAllResultFileByCommerce(
            @RequestBody final LinkedHashMap<String, Object> dataForm,
            HttpServletRequest httpServletRequest) {

        LogsClass logTo = new LogsClass(httpServletRequest, new Object() {}.
                getClass().getEnclosingMethod().getName());

        LinkedHashMap<String, Object> response = new LinkedHashMap<>();

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());

            List<LinkedHashMap<String, Object>> resultFileDetailToListMap =
                    (List<LinkedHashMap<String, Object>>) dataForm.get("resultFileDetailToList");

            List<ResultFileDetailTo> resultFileDetailToList = resultFileDetailToListMap.stream()
                    .map(map -> objectMapper.convertValue(map, ResultFileDetailTo.class)).toList();

            List<ResultFileEntity> resultFileEntities = resultFileDetailToList.stream().map(to -> {
                ResultFileEntity entity = new ResultFileEntity();
                entity.setSize(to.getSizeFile());
                entity.setChargedBy(to.getChargedBy());
                entity.setCommerceEntity(new CommerceEntity(to.getIdCommerce()));
                entity.setChargedDate(to.getChargedDate());
                entity.setRequirementEntity(new RequirementEntity(to.getIdRequirement()));
                entity.setFileName(to.getFileName());
                return entity;
            }).collect(Collectors.toList());

            resultFileService.saveAll(resultFileEntities);

            response.put("SUCCESS", true);

            manageLogs.infoLogger(logTo,httpServletRequest,
                    "Successfully: " + logTo.getMethodName(),
                    "user",false);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            manageLogs.severeErrorLogger(logTo,httpServletRequest,
                    "Error: "+ logTo.getMethodName() + ": "+ e,
                    e,"user",false);

            response.put("ERROR", false);

        }

        return response;
    }

    @PostMapping("/getResultFileByIdCommerceAndIdRequirement")
    public LinkedHashMap<String, Object> getResultFileByIdCommerceAndIdRequirement(@RequestBody final String dataForm,
                                                                                      HttpServletRequest httpServletRequest) {

        LogsClass logTo = new LogsClass(httpServletRequest, new Object() {}.
                getClass().getEnclosingMethod().getName());

        LinkedHashMap<String, Object> response = new LinkedHashMap<>();

        Long idCommerce = Long.parseLong((new JSONObject(dataForm).get("idCommerce")).toString());
        Long idRequirement = Long.parseLong((new JSONObject(dataForm).get("idRequirement")).toString());

        ResultFileEntity resultFileEntity = resultFileService.
                getResultFileByIdCommerceAndIdRequirement(idCommerce, idRequirement);

        response.put("fileName", resultFileEntity.getFileName());
        response.put("rif", resultFileEntity.getCommerceEntity().getCommerceDocument());

        manageLogs.infoLogger(logTo,httpServletRequest,
                "Successfully: " + logTo.getMethodName(),
                "user",false);

        return response;
    }

    @PostMapping("/getResultFileByTermsOrPolicies")
    public LinkedHashMap<String, Object> getResultFileByTermsOrPolicies(@RequestBody final String dataForm,
                                                                                      HttpServletRequest httpServletRequest) {

        LogsClass logTo = new LogsClass(httpServletRequest, new Object() {}.
                getClass().getEnclosingMethod().getName());

        LinkedHashMap<String, Object> response = new LinkedHashMap<>();

        JSONObject jsonObject = new JSONObject(dataForm);
        String termsOrPolicies = jsonObject.getString("termsOrPolices");

        TermsAndConditionsEntity termsAndConditionsEntity = resultFileService.
                getLastTermsOrPolicies(termsOrPolicies);

        response.put("fileName", termsAndConditionsEntity.getUrlFile());

        manageLogs.infoLogger(logTo,httpServletRequest,
                "Successfully: " + termsAndConditionsEntity.getFileName() + " Method: " +
                        logTo.getMethodName(), "user",false);

        return response;
    }

}
