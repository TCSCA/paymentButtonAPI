package api.externalrepository.controller;

import api.externalrepository.entity.RequirementEntity;
import api.externalrepository.service.RequirementService;
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
public class RequirementController {

    private final RequirementService requirementService;

    private final ManageLogs manageLogs;

    public RequirementController(RequirementService requirementService, ManageLogs manageLogs) {
        this.requirementService = requirementService;
        this.manageLogs = manageLogs;
    }

    @PostMapping("/getAllRequirement")
    public LinkedHashMap<String, Object> getAllRequirement(@RequestBody final String dataForm,
                                                           HttpServletRequest httpServletRequest) {

        LogsClass logTo = new LogsClass(httpServletRequest, new Object() {}.
                getClass().getEnclosingMethod().getName());

        LinkedHashMap<String, Object> response = new LinkedHashMap<>();

        try {

            Long idTypeCommerce = Long.parseLong((new JSONObject(dataForm).
                    get("idTypeCommerce")).toString());

            Long idPreRegister = Long.parseLong((new JSONObject(dataForm).
                    get("idPreRegister")).toString());

            List<RequirementEntity> requirementEntitiesCharged = requirementService.
                    getAllRequirementChargedByIdTypeCommerce(idTypeCommerce, idPreRegister);

            response.put("requirementEntitiesCharged", requirementEntitiesCharged);

            List<RequirementEntity> requirementEntitiesNoCharged = requirementService.
                    getAllRequirementNoChargedByIdTypeCommerce(idTypeCommerce, idPreRegister);

            response.put("requirementEntitiesNoCharged", requirementEntitiesNoCharged);

            manageLogs.infoLogger(logTo,httpServletRequest,
                    "Successfully: " + logTo.getMethodName(),
                    "user",false);

        } catch (Exception e){

            response.put("ERROR", null);

            manageLogs.severeErrorLogger(logTo,httpServletRequest,
                    "Error: "+ logTo.getMethodName() + ": "+ e,
                    e,"user",false);
        }

        return response;
    }

    @GetMapping("/getReNameByIdRequirement/{idRequirement}")
    public LinkedHashMap<String, Object> getReNameByIdRequirement(@PathVariable("idRequirement") Long idRequirement,
                                                                  final HttpServletRequest httpServletRequest) {

        LogsClass logTo = new LogsClass(httpServletRequest, new Object() {}.
                getClass().getEnclosingMethod().getName());

        LinkedHashMap<String, Object> response = new LinkedHashMap<>();

        try {

            response.put("reName", requirementService.
                    getRequirementByIdRequirement(idRequirement));

            manageLogs.infoLogger(logTo,httpServletRequest,
                    "Successfully: " + logTo.getMethodName(),
                    "user",false);

        } catch (Exception e){

            response.put("ERROR", null);

            manageLogs.severeErrorLogger(logTo,httpServletRequest,
                    "Error: "+ logTo.getMethodName() + ": "+ e,
                    e,"user",false);
        }

        return response;
    }

}
