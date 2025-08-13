package api.externalrepository.controller;

import api.externalrepository.entity.ResultFileEntity;
import api.externalrepository.service.ResultFileService;
import api.logsClass.LogsClass;
import api.logsClass.ManageLogs;
import jakarta.servlet.http.HttpServletRequest;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.List;

@RestController
@RequestMapping(value = "/horizonte")
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST})
public class ResultFileController {

    private final ResultFileService resultFileService;

    private final ManageLogs manageLogs;

    public ResultFileController(ResultFileService resultFileService, ManageLogs manageLogs) {
        this.resultFileService = resultFileService;
        this.manageLogs = manageLogs;
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
            Long idPreRegister = Long.parseLong((new JSONObject(dataForm).get("idPreRegister")).toString());
            Long idRequirement = Long.parseLong((new JSONObject(dataForm).get("idRequirement")).toString());
            String fileName = (new JSONObject(dataForm).get("fileName")).toString();

            Boolean result = resultFileService.saveResultFile(size, chargedBy, idPreRegister, idRequirement,
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

    @PostMapping("/getResultFileByIdPreRegisterAndIdRequirement")
    public LinkedHashMap<String, Object> getResultFileByIdPreRegisterAndIdRequirement(@RequestBody final String dataForm,
                                                                                      HttpServletRequest httpServletRequest) {

        LogsClass logTo = new LogsClass(httpServletRequest, new Object() {}.
                getClass().getEnclosingMethod().getName());

        LinkedHashMap<String, Object> response = new LinkedHashMap<>();

        Long idPreRegister = Long.parseLong((new JSONObject(dataForm).get("idPreRegister")).toString());
        Long idRequirement = Long.parseLong((new JSONObject(dataForm).get("idRequirement")).toString());

        ResultFileEntity resultFileEntity = resultFileService.
                getResultFileByIdPreRegisterAndIdRequirement(idPreRegister, idRequirement);

        response.put("fileName", resultFileEntity.getFileName());
        response.put("rif", resultFileEntity.getPreRegisterEntity().getCommerceDocument());

        manageLogs.infoLogger(logTo,httpServletRequest,
                "Successfully: " + resultFileEntity.getFileName() + " Method: " +
                        logTo.getMethodName(), "user",false);

        return response;
    }

    @GetMapping("/getAllResultFileByIdPreRegister/{idPreRegister}")
    public LinkedHashMap<String, Object> getAllResultFileByIdPreRegister(
            @PathVariable("idPreRegister") final Long idPreRegister, HttpServletRequest httpServletRequest) {

        LogsClass logTo = new LogsClass(httpServletRequest, new Object() {}.
                getClass().getEnclosingMethod().getName());

        LinkedHashMap<String, Object> response = new LinkedHashMap<>();

        List<ResultFileEntity> resultFileEntities = resultFileService.
                getAllResultFileByIdPreRegister(idPreRegister);

        response.put("resultFileEntities", resultFileEntities);

        manageLogs.infoLogger(logTo,httpServletRequest,
                "Successfully: " + logTo.getMethodName(),
                "user",false);

        return response;

    }

}
