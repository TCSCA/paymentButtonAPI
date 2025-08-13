package api.externalrepository.controller;

import api.externalrepository.entity.PlanEntity;
import api.externalrepository.entity.PreRegisterEntity;
import api.externalrepository.entity.TypeCommerceEntity;
import api.externalrepository.service.FilterService;
import api.externalrepository.service.PreRegisterService;
import api.externalrepository.to.PaginatedResponseTo;
import api.externalrepository.to.PreRegisterStatusUpdateTo;
import api.logsClass.LogsClass;
import api.logsClass.ManageLogs;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.json.JSONObject;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.data.domain.Page;

import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.List;

@RestController
@RequestMapping(value = "/horizonte")
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST})
public class PreRegisterController {

    private final PreRegisterService preRegisterService;
    private final ManageLogs manageLogs;
    private final FilterService filterService;

    public PreRegisterController(PreRegisterService preRegisterService,
                                 ManageLogs manageLogs,
                                 FilterService filterService) {
        this.preRegisterService = preRegisterService;
        this.manageLogs = manageLogs;
        this.filterService = filterService;
    }

    @PostMapping("/setStatusPreRegisterExt")
    public LinkedHashMap<String, Object> setStatusPreRegisterExt(@RequestBody PreRegisterStatusUpdateTo request, HttpServletRequest httpServletRequest) {
        LogsClass logTo = new LogsClass(httpServletRequest, new Object() {}.getClass().getEnclosingMethod().getName());
        LinkedHashMap<String, Object> response = new LinkedHashMap<>();

        try {

            Boolean result = preRegisterService.setStatusPreRegisterExt(request.getIdStatus(),
                    request.getIdPreRegister());
            response.put("success", result);
            manageLogs.infoLogger(logTo,httpServletRequest,
                    "Successfull: " + logTo.getMethodName(),
                    "user",true);

        } catch (Exception e) {
            manageLogs.severeErrorLogger(logTo,httpServletRequest,"Error: "+ logTo.getMethodName() + ": "+ e,
                    e,"user",true);
            response.put("ERROR", false);
            response.put("message", "An error occurred: " + e.getMessage());
        }
        return response;
    }

    @GetMapping("/getPreRegisterById/{idPreRegister}")
    public LinkedHashMap<String, Object> getPreRegisterById(@PathVariable final Long idPreRegister, final HttpServletRequest httpServletRequest) {
        LogsClass logTo = new LogsClass(httpServletRequest, new Object() {}.
                getClass().getEnclosingMethod().getName());
        LinkedHashMap<String, Object> response = new LinkedHashMap<>();

        try {

            PreRegisterEntity preRegisterEntity = preRegisterService.getPreRegisterById(idPreRegister);
            response.put("preRegisterEntity", preRegisterEntity);
            manageLogs.infoLogger(logTo,httpServletRequest,
                    "Successfully: " + logTo.getMethodName(),
                    "user",false);

        } catch (Exception e){
            response.put("ERROR", false);
            response.put("message", "An error occurred: " + e.getMessage());
            manageLogs.severeErrorLogger(logTo,httpServletRequest,"Error: "+ logTo.getMethodName() + ": "+ e,
                    e,"user",false);

        }

        return response;
    }

    @PostMapping("/getPreregisterByRifCommerce")
    public LinkedHashMap<String, Object> getPreregisterByRifCommerce(@RequestBody final String dataForm,
                                                                     final HttpServletRequest httpServletRequest) {
        LogsClass logTo = new LogsClass(httpServletRequest, new Object() {}.
                getClass().getEnclosingMethod().getName());
        LinkedHashMap<String, Object> response = new LinkedHashMap<>();

        try {

            String rif =  new JSONObject(dataForm).get("rif").toString();

            PreRegisterEntity preRegisterEntity = preRegisterService.getPreRegisterByRif(rif);
            response.put("preRegisterEntity", preRegisterEntity);
            manageLogs.infoLogger(logTo,httpServletRequest,
                    "Successfully: " + logTo.getMethodName(),
                    "user",false);

        } catch (Exception e){
            response.put("ERROR", false);
            response.put("message", "An error occurred: " + e.getMessage());
            manageLogs.severeErrorLogger(logTo,httpServletRequest,"Error: "+ logTo.getMethodName() + ": "+ e,
                    e,"user",false);

        }

        return response;
    }

    @PostMapping("/changePreRegisterStatus")
    public LinkedHashMap<String, Object> changePreRegisterStatus(@RequestBody final String dataForm, final HttpServletRequest httpServletRequest) {
        LogsClass logTo = new LogsClass(httpServletRequest, new Object() {}.
                getClass().getEnclosingMethod().getName());
        LinkedHashMap<String, Object> response = new LinkedHashMap<>();

        try {

            Boolean result = preRegisterService.changePreRegisterStatus(Long.parseLong((new JSONObject(dataForm)
                    .get("idPreRegister")).toString()), Long.parseLong((new JSONObject(dataForm).
                    get("statusPreRegister")).toString()), (new JSONObject(dataForm).
                    get("rejectMotive")).toString());

            response.put("success", result);
            manageLogs.infoLogger(logTo,httpServletRequest,
                    "Successfull: " + logTo.getMethodName(),
                    "user",true);

        } catch (Exception e) {
            response.put("ERROR", false);
            response.put("message", "An error occurred: " + e.getMessage());
            manageLogs.severeErrorLogger(logTo,httpServletRequest,"Error: "+ logTo.getMethodName() + ": "+ e,
                    e,"user",true);
        }
        return response;
    }

    @PostMapping("/getAllPreRegistersByStatus")
    public PaginatedResponseTo<PreRegisterEntity> getAllPreRegistersByStatus(@RequestBody final String dataForm, final HttpServletRequest httpServletRequest) {
        LogsClass logTo = new LogsClass(httpServletRequest, new Object() {}.
                getClass().getEnclosingMethod().getName());
        LinkedHashMap<String, Object> response = new LinkedHashMap<>();
        try {

            Long idStatus = Long.parseLong((new JSONObject(dataForm).get("idStatusPreRegister")).toString());
            int index = Integer.parseInt((new JSONObject(dataForm).get("index")).toString());
            int quantity = Integer.parseInt((new JSONObject(dataForm).get("quantity")).toString());

            Page<PreRegisterEntity> page = preRegisterService.getAllPreRegistersByStatus(PageRequest.of(index, quantity), idStatus);
//            response.put("preRegisterStatus", page);
            manageLogs.infoLogger(logTo,httpServletRequest,
                    "Successfull: " + logTo.getMethodName(),
                    "user",false);

            return new PaginatedResponseTo<>(page);

        } catch (Exception e){
            manageLogs.severeErrorLogger(logTo,httpServletRequest,"Error: "+ logTo.getMethodName() + ": "+ e,
                    e,"user",false);
            return null;
        }

    }

    @PostMapping("/getAllPreRegistersByStatusExport")
    public LinkedHashMap<String, Object> getAllPreRegistersByStatusExport(@RequestBody final String dataForm, final HttpServletRequest httpServletRequest) {
        LogsClass logTo = new LogsClass(httpServletRequest, new Object() {}.
                getClass().getEnclosingMethod().getName());
        LinkedHashMap<String, Object> response = new LinkedHashMap<>();
        try {

            Long idStatus = Long.parseLong((new JSONObject(dataForm).get("idStatusPreRegister")).toString());
            int index = Integer.parseInt((new JSONObject(dataForm).get("index")).toString());
            int quantity = Integer.parseInt((new JSONObject(dataForm).get("quantity")).toString());
            Boolean export = Boolean.parseBoolean((new JSONObject(dataForm).get("quantity")).toString());

            List<PreRegisterEntity> preRegisterEntities = preRegisterService.getAllPreRegistersByStatusExport(idStatus);
            response.put("preRegisterEntities", preRegisterEntities);
//            response.put("preRegisterStatus", page);
            manageLogs.infoLogger(logTo,httpServletRequest,
                    "Successfull: " + logTo.getMethodName(),
                    "user",false);

            return response;

        } catch (Exception e){
            manageLogs.severeErrorLogger(logTo,httpServletRequest,"Error: "+ logTo.getMethodName() + ": "+ e,
                    e,"user",false);
            return null;
        }

    }

    @PostMapping("/getAllPreRegistersByFilter")
    public LinkedHashMap<String, Object> getAllPreRegistersByFilter(@RequestBody final LinkedHashMap dataForm,
                                                                    final HttpServletRequest httpServletRequest) {
        LogsClass logTo = new LogsClass(httpServletRequest, new Object() {}.
                getClass().getEnclosingMethod().getName());
        LinkedHashMap<String, Object> response = new LinkedHashMap<>();
        try {
            ObjectMapper objectMapper = new ObjectMapper();

            List<PreRegisterEntity> preregistroFilter = filterService.getAllPreregisterFilter(dataForm, objectMapper);
            response.put("preregistroFilter", preregistroFilter);
            manageLogs.infoLogger(logTo,httpServletRequest,
                    "Successfull: " + logTo.getMethodName(),
                    "user",false);

            return response;

        } catch (Exception e){
            manageLogs.severeErrorLogger(logTo,httpServletRequest,"Error: "+ logTo.getMethodName() + ": "+ e,
                    e,"user",false);
            return null;
        }

    }

    @GetMapping("/getAllTypeDocument")
    public LinkedHashMap<String, Object> getAllTypeDocument(final HttpServletRequest httpServletRequest) {
        LogsClass logTo = new LogsClass(httpServletRequest, new Object() {}.
                getClass().getEnclosingMethod().getName());
        LinkedHashMap<String, Object> response = new LinkedHashMap<>();

        try {

            List<TypeCommerceEntity> typeCommerceEntities = preRegisterService.
                    getAllTypeCommerce();

            response.put("typeCommerceEntities", typeCommerceEntities);
            manageLogs.infoLogger(logTo,httpServletRequest,
                    "Successfull: " + logTo.getMethodName(),
                    "user",false);
            return response;

        } catch (Exception e){
            manageLogs.severeErrorLogger(logTo,httpServletRequest,"Error: "+ logTo.getMethodName() + ": "+ e,
                    e,"user",false);
            return null;
        }

    }

    @GetMapping("/getAllPlanByIdTypeCommerce/{idTypeCommerce}")
    public LinkedHashMap<String, Object> getAllPlanByIdTypeCommerce(@PathVariable("idTypeCommerce") final Long idTypeCommerce, final HttpServletRequest httpServletRequest) {
        LogsClass logTo = new LogsClass(httpServletRequest, new Object() {}.
                getClass().getEnclosingMethod().getName());
        LinkedHashMap<String, Object> response = new LinkedHashMap<>();

        try {

            List<PlanEntity> planEntities = preRegisterService.
                    getAllPlanByIdTypeCommerce(idTypeCommerce);

            response.put("planEntities", planEntities);
            manageLogs.infoLogger(logTo,httpServletRequest,
                    "Successfull: " + logTo.getMethodName(),
                    "user",false);
            return response;

        } catch (Exception e){
            manageLogs.severeErrorLogger(logTo,httpServletRequest,
                    "Error: "+ logTo.getMethodName() + ": "+ e,
                    e,"user",false);
            return null;
        }

    }

    @GetMapping("/validateChargedRequiredFilesByIdPreRegister/{idPreRegister}")
    public LinkedHashMap<String, Object> validateChargedRequiredFilesByIdPreRegister(@PathVariable("idPreRegister") final Long idPreRegister,
                                                                                     HttpServletRequest httpServletRequest) {

        LogsClass logTo = new LogsClass(httpServletRequest, new Object() {}.
                getClass().getEnclosingMethod().getName());
        LinkedHashMap<String, Object> response = new LinkedHashMap<>();
        Boolean result = false;

        try {

            result = preRegisterService.
                    validateChargedRequiredFilesByIdPreRegister(idPreRegister);

            if (result){
                manageLogs.infoLogger(logTo,httpServletRequest,
                        "All files required charged",
                        "user",true);
            } else {
                manageLogs.infoLogger(logTo,httpServletRequest,
                        "All necessary files haven't been uploaded",
                        "user",true);
            }

            response.put("SUCCESS", result);

        } catch (Exception e) {
            response.put("ERROR", result);
            response.put("message", "An error occurred: " + e.getMessage());
            manageLogs.severeErrorLogger(logTo,httpServletRequest,"Error: "+ logTo.getMethodName() + ": "+ e,
                    e,"user",true);
        }
        return response;

    }

    @PostMapping("/getFilterPreregisterByDate")
    public PaginatedResponseTo<PreRegisterEntity> getFilterPreregisterByDate(@RequestBody LinkedHashMap body, final HttpServletRequest httpServletRequest) {
        LogsClass logTo = new LogsClass(httpServletRequest, new Object() {}.
                getClass().getEnclosingMethod().getName());
        LinkedHashMap<String, Object> response = new LinkedHashMap<>();
        try {
            ObjectMapper objectMapper = new ObjectMapper();

            String startDateStr = objectMapper.convertValue(body.get("startDate"), String.class);
            String endDateStr = objectMapper.convertValue(body.get("endDate"), String.class);
            int index = objectMapper.convertValue(body.get("index"), int.class);
            int quantity = objectMapper.convertValue(body.get("quantity"), int.class);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate startDateLocal = LocalDate.parse(startDateStr, formatter);
            LocalDate endDateLocal = LocalDate.parse(endDateStr, formatter);

            Date startDate = Date.valueOf(startDateLocal);
            Date endDate = Date.valueOf(endDateLocal);
            Page<PreRegisterEntity> page = preRegisterService.getAllPreRegistersByDate(PageRequest.of(index, quantity),startDate,endDate);
            response.put("preRegisterByDate", page);
            manageLogs.infoLogger(logTo,httpServletRequest,
                    "Successfull: " + logTo.getMethodName(),
                    "user",false);

            return new PaginatedResponseTo<>(page);

        } catch (Exception e){
            manageLogs.severeErrorLogger(logTo,httpServletRequest,"Error: "+ logTo.getMethodName() + ": "+ e,
                    e,"user",false);
            return null;
        }

    }

    @PostMapping("/getFilterPreregisterByDateExport")
    public LinkedHashMap<String, Object> getFilterPreregisterByDateExport(@RequestBody LinkedHashMap body, final HttpServletRequest httpServletRequest) {
        LogsClass logTo = new LogsClass(httpServletRequest, new Object() {}.
                getClass().getEnclosingMethod().getName());
        LinkedHashMap<String, Object> response = new LinkedHashMap<>();
        try {
            ObjectMapper objectMapper = new ObjectMapper();

            String startDateStr = objectMapper.convertValue(body.get("startDate"), String.class);
            String endDateStr = objectMapper.convertValue(body.get("endDate"), String.class);
            int index = objectMapper.convertValue(body.get("index"), int.class);
            int quantity = objectMapper.convertValue(body.get("quantity"), int.class);
            Boolean export = objectMapper.convertValue(body.get("export"), Boolean.class);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate startDateLocal = LocalDate.parse(startDateStr, formatter);
            LocalDate endDateLocal = LocalDate.parse(endDateStr, formatter);

            Date startDate = Date.valueOf(startDateLocal);
            Date endDate = Date.valueOf(endDateLocal);
            List<PreRegisterEntity> page = preRegisterService.getAllPreRegistersByDateExport(startDate,endDate);
            response.put("preRegisterByDate", page);
            manageLogs.infoLogger(logTo,httpServletRequest,
                    "Successfull: " + logTo.getMethodName(),
                    "user",false);

            return response;

        } catch (Exception e){
            manageLogs.severeErrorLogger(logTo,httpServletRequest,"Error: "+ logTo.getMethodName() + ": "+ e,
                    e,"user",false);
            return null;
        }

    }


    @PostMapping("/getFilterRejectedByDate")
    public PaginatedResponseTo<PreRegisterEntity> getFilterRejectedByDate(@RequestBody LinkedHashMap body, final HttpServletRequest httpServletRequest) {
        LogsClass logTo = new LogsClass(httpServletRequest, new Object() {}.
                getClass().getEnclosingMethod().getName());
        LinkedHashMap<String, Object> response = new LinkedHashMap<>();
        try {
            ObjectMapper objectMapper = new ObjectMapper();

            String startDateStr = objectMapper.convertValue(body.get("startDate"), String.class);
            String endDateStr = objectMapper.convertValue(body.get("endDate"), String.class);
            int index = objectMapper.convertValue(body.get("index"), int.class);
            int quantity = objectMapper.convertValue(body.get("quantity"), int.class);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate startDateLocal = LocalDate.parse(startDateStr, formatter);
            LocalDate endDateLocal = LocalDate.parse(endDateStr, formatter);

            Date startDate = Date.valueOf(startDateLocal);
            Date endDate = Date.valueOf(endDateLocal);
            Page<PreRegisterEntity> page = preRegisterService.getAllCommercesByRejectedDate(PageRequest.of(index, quantity),startDate,endDate);
            response.put("preRegisterByDate", page);
            manageLogs.infoLogger(logTo,httpServletRequest,
                    "Successfull: " + logTo.getMethodName(),
                    "user",false);

            return new PaginatedResponseTo<>(page);

        } catch (Exception e){
            manageLogs.severeErrorLogger(logTo,httpServletRequest,"Error: "+ logTo.getMethodName() + ": "+ e,
                    e,"user",false);
            return null;
        }

    }
    @PostMapping("/getFilterRejectedByDateExport")
    public LinkedHashMap<String, Object> getFilterRejectedByDateExport(@RequestBody LinkedHashMap body, final HttpServletRequest httpServletRequest) {
        LogsClass logTo = new LogsClass(httpServletRequest, new Object() {}.
                getClass().getEnclosingMethod().getName());
        LinkedHashMap<String, Object> response = new LinkedHashMap<>();
        try {
            ObjectMapper objectMapper = new ObjectMapper();

            String startDateStr = objectMapper.convertValue(body.get("startDate"), String.class);
            String endDateStr = objectMapper.convertValue(body.get("endDate"), String.class);
            int index = objectMapper.convertValue(body.get("index"), int.class);
            int quantity = objectMapper.convertValue(body.get("quantity"), int.class);
            Boolean export = objectMapper.convertValue(body.get("export"), Boolean.class);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate startDateLocal = LocalDate.parse(startDateStr, formatter);
            LocalDate endDateLocal = LocalDate.parse(endDateStr, formatter);

            Date startDate = Date.valueOf(startDateLocal);
            Date endDate = Date.valueOf(endDateLocal);
            List<PreRegisterEntity> page = preRegisterService.getAllCommercesByRejectedDateExport(startDate,endDate);
            response.put("rejectedByDate", page);
            manageLogs.infoLogger(logTo,httpServletRequest,
                    "Successfull: " + logTo.getMethodName(),
                    "user",false);
//            response.put("preRegisterByDate", page);
            return response;

        } catch (Exception e){
            manageLogs.severeErrorLogger(logTo,httpServletRequest,"Error: "+ logTo.getMethodName() + ": "+ e,
                    e,"user",false);
            return null;
        }

    }

    @PostMapping("/editProfileByPreRegister")
    public LinkedHashMap<String, Object> editProfileByPreRegister(@RequestBody final String dataForm,
                                                               final HttpServletRequest httpServletRequest) {

        LogsClass logTo = new LogsClass(httpServletRequest, new Object() {}.
                getClass().getEnclosingMethod().getName());

        LinkedHashMap<String, Object> response = new LinkedHashMap<>();

        try {

            JSONObject jsonObject = new JSONObject(dataForm);

            Long idPreRegister = jsonObject.getLong("idPreRegister");
            String commerceName = jsonObject.getString("commerceName");
            String commerceDocument = jsonObject.getString("commerceDocument");
            String contactPerson = jsonObject.getString("contactPerson");
            String address = jsonObject.getString("address");
            Long idState = jsonObject.getLong("idState");
            String contactPersonEmail = jsonObject.getString("contactPersonEmail");
            String phoneNumber = jsonObject.getString("phoneNumberCommerce");
            Long idPlan = jsonObject.getLong("idPlan");

            String message = preRegisterService.editProfileByPreRegister(idPreRegister,
                    commerceName, commerceDocument, contactPerson, address, idState,
                    contactPersonEmail, phoneNumber, idPlan);

            if (!message.equals("Transacción exitosa")) {
                response.put("ERROR", message);
            } else {
                response.put("message", message);
            }

            manageLogs.infoLogger(logTo,httpServletRequest,
                    "Successfully: " + logTo.getMethodName(),
                    "user",false);

        } catch (Exception e) {
            response.put("ERROR", e.getMessage());

            manageLogs.severeErrorLogger(logTo,httpServletRequest,
                    "Error: "+ logTo.getMethodName() + ": "+ e,
                    e,"user",false);
        }

        return response;
    }

}
