package api.internalrepository.controller;

import api.internalrepository.entity.*;
import api.internalrepository.repository.UnlinkCommerceRepository;
import api.internalrepository.service.BankCommerceService;
import api.internalrepository.service.CommerceService;
import api.internalrepository.service.FilterService;
import api.internalrepository.to.*;
import api.logsClass.LogsClass;
import api.logsClass.ManageLogs;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.List;

@RestController
@RequestMapping(value = "/core")
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST})
public class CommerceController {

    private static final Logger logger = LoggerFactory.getLogger(CommerceController.class);

    private final CommerceService commerceService;

    private final ManageLogs manageLogs;

    private final BankCommerceService bankCommerceService;

    private final FilterService filterService;

    private final UnlinkCommerceRepository unlinkCommerceRepository;

    public CommerceController(CommerceService commerceService, FilterService filterService, ManageLogs manageLogs, BankCommerceService bankCommerceService, UnlinkCommerceRepository unlinkCommerceRepository) {
        this.commerceService = commerceService;
        this.manageLogs = manageLogs;
        this.bankCommerceService = bankCommerceService;
        this.filterService = filterService;
        this.unlinkCommerceRepository = unlinkCommerceRepository;
    }

    @PostMapping("/saveCommerce")
    public LinkedHashMap<String, Object> saveCommerce(@RequestBody final String dataForm,
                                                      final HttpServletRequest httpServletRequest) {

        LogsClass logTo = new LogsClass(httpServletRequest, new Object() {}.
                getClass().getEnclosingMethod().getName());

        LinkedHashMap<String, Object> response = new LinkedHashMap<>();
        ObjectMapper objectMapper = new ObjectMapper();

        try {

            LinkedHashMap<String, Object> requestMap = objectMapper.readValue(dataForm, LinkedHashMap.class);

            RegisterCommerceTo registerCommerceTo = objectMapper.convertValue(requestMap.get("preRegisterTo"), RegisterCommerceTo.class);

            Long idEconomicActivity = Long.parseLong((new JSONObject(dataForm)
                    .get("idEconomicActivity")).toString());

            Long idCity = Long.parseLong((new JSONObject(dataForm)
                    .get("idCity")).toString());

            Boolean result = commerceService.saveCommerce(registerCommerceTo, idEconomicActivity, idCity);
            response.put("SUCCESS", result);

            manageLogs.infoLogger(logTo,httpServletRequest,
                    "Successfully: " + logTo.getMethodName(),
                    "user",true);

        } catch (Exception e) {
            response.put("ERROR", false);
            response.put("message", "An error occurred: " + e.getMessage());

            manageLogs.severeErrorLogger(logTo,httpServletRequest,
                    "Error: "+ logTo.getMethodName() + ": "+ e,
                    e,"user",true);

        }
        return response;
    }

    @GetMapping("/getCommerceById/{idCommerce}")
    public LinkedHashMap<String, Object> getCommerceById(@PathVariable final Long idCommerce,
                                                         final HttpServletRequest httpServletRequest) {
        LogsClass logTo = new LogsClass(httpServletRequest, new Object() {}.
                getClass().getEnclosingMethod().getName());
        LinkedHashMap<String, Object> response = new LinkedHashMap<>();

        try {

            CommerceEntity commerceEntity = commerceService.getCommerceById(idCommerce);

            response.put("commerceEntity", commerceEntity);

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

    @PostMapping("/getCommerceInformationDetailByRif")
    public LinkedHashMap<String, Object> getCommerceInformationDetailByRif(@RequestBody final String dataForm,
                                                                           HttpServletRequest httpServletRequest) {

        LogsClass logTo = new LogsClass(httpServletRequest, new Object() {}.
                getClass().getEnclosingMethod().getName());

        LinkedHashMap<String, Object> response = new LinkedHashMap<>();

        try {

            String commerceDocument = (new JSONObject(dataForm).get("rif")).toString();

            CommerceEntity commerceEntity = commerceService.
                    getCommerceInformationDetailByRif(commerceDocument);

            if (commerceEntity != null) {
                PlanEntity planEntity = commerceService.
                        getPlanByIdCommerce(commerceEntity.getIdCommerce());

                BankCommerceEntity bankCommerceEntity = bankCommerceService.
                        getBankCommerceInformationByCommerce(commerceEntity.getIdCommerce());

                LicenseEntity licenseEntity = commerceService.findLicenceByCommerce(commerceEntity.getIdCommerce());

                response.put("commerceEntity", commerceEntity);
                response.put("economicActivity", commerceEntity.getEconomicActivityEntity());
                response.put("idPlan", planEntity.getIdPlan());
                response.put("planName", planEntity.getPlanName());
                response.put("bankCommerceEntity", bankCommerceEntity);
                response.put("idTypeCommerce", planEntity.getTypeCommerceEntity().getIdTypeCommerce());
                response.put("typeCommerce", planEntity.getTypeCommerceEntity().getTypeCommerce());
                response.put("activationDate", licenseEntity.getActivationDate());

                if (commerceEntity.getStatusCommerce().getIdStatusCommerce() == 6L) {
                    UnlinkCommerceEntity unlinkCommerceEntity = unlinkCommerceRepository.
                            findUnlinkCommerceByIdCommerceAndStatusIsTrue(commerceEntity.getIdCommerce());

                    response.put("unlinkReason", unlinkCommerceEntity.getUnlinkReason());
                    response.put("unlinkDate", unlinkCommerceEntity.getRegisterDate());
                }

            } else {
                response.put("ERROR", null);
                logger.info("Commerce doesn't exist");
            }

        } catch (Exception e) {
            response.put("ERROR", null);
            manageLogs.severeErrorLogger(logTo,httpServletRequest,"Error: "+ logTo.getMethodName() + ": "+ e,
                    e,"user",false);
        }

        return response;
    }

    @PostMapping("/getCommerceInformationDetailByIdUser")
    public LinkedHashMap<String, Object> getCommerceInformationDetailByIdUser(@RequestBody final String dataForm,
                                                                           HttpServletRequest httpServletRequest) {

        LogsClass logTo = new LogsClass(httpServletRequest, new Object() {}.
                getClass().getEnclosingMethod().getName());

        LinkedHashMap<String, Object> response = new LinkedHashMap<>();

        try {

            Long idUser = Long.parseLong((new JSONObject(dataForm).get("idUser")).toString());

            CommerceEntity commerceEntity = commerceService.
                    getCommerceInformationDetailByIdUser(idUser);

            if (commerceEntity != null) {
                PlanEntity planEntity = commerceService.
                        getPlanByIdCommerce(commerceEntity.getIdCommerce());

                LicenseEntity licenseEntity = commerceService.findLicenceByCommerce(commerceEntity.getIdCommerce());

                BankCommerceEntity bankCommerceEntity = bankCommerceService.
                        getBankCommerceInformationByCommerce(commerceEntity.getIdCommerce());

                response.put("commerceEntity", commerceEntity);
                response.put("planName", planEntity.getPlanName());
                response.put("bankCommerceEntity", bankCommerceEntity);
                response.put("activationDate",licenseEntity.getActivationDate());

            } else {
                response.put("ERROR", null);
                logger.info("Commerce doesn't exist");
            }

        } catch (Exception e) {
            response.put("ERROR", null);
            manageLogs.severeErrorLogger(logTo,httpServletRequest,"Error: "+ logTo.getMethodName() + ": "+ e,
                    e,"user",false);
        }

        return response;
    }

    @PostMapping("/getCommerceByDocumentAndStatus")
    public LinkedHashMap<String, Object> getCommerceByDocumentAndStatus(@RequestBody final String dataForm,
                                                                        final HttpServletRequest httpServletRequest) {

        LogsClass logTo = new LogsClass(httpServletRequest, new Object() {}.
                getClass().getEnclosingMethod().getName());

        LinkedHashMap<String, Object> response = new LinkedHashMap<>();

        try {

            String commerceDocument = (new JSONObject(dataForm).get("commerceDocument")).toString();
            Long idStatusCommerce = Long.parseLong((new JSONObject(dataForm).get("idStatusCommerce")).toString());

            CommerceEntity commerceEntity = commerceService.
                    getCommerceByDocumentAndStatus(commerceDocument, idStatusCommerce);

            response.put("idCommerce", commerceEntity.getIdCommerce());
            response.put("commerceDocument", commerceEntity.getCommerceDocument());
            response.put("commerceEmail", commerceEntity.getContactPersonEmail());
            response.put("phoneNumberCommerce", commerceEntity.getPhoneNumberCommerce());
            response.put("idTypeCommerce", commerceEntity.getTypeCommerceEntity().getIdTypeCommerce());

            manageLogs.infoLogger(logTo,httpServletRequest,
                    "Successfully: " + logTo.getMethodName(),
                    "user",false);

        } catch (Exception e){
            response.put("ERROR", "Not found");
            response.put("message", "An error occurred: " + e.getMessage());

            manageLogs.severeErrorLogger(logTo,httpServletRequest,
                    "Error: "+ logTo.getMethodName() + ": "+ e,
                    e,"user",true);

        }
        return response;
    }

    @PostMapping("/generateLicence")
    public LinkedHashMap<String, Object> generateLicence(@RequestBody final String dataForm,
                                                         final HttpServletRequest httpServletRequest) {

        LogsClass logTo = new LogsClass(httpServletRequest, new Object() {}.
                getClass().getEnclosingMethod().getName());

        LinkedHashMap<String, Object> response = new LinkedHashMap<>();

        try {

            Long idCommerce = Long.parseLong((new JSONObject(dataForm).get("idCommerce")).toString());
            Long idPlan = Long.parseLong((new JSONObject(dataForm).get("idPlan")).toString());
            /*String activationDate = ((new JSONObject(dataForm).get("activationDate")).toString());*/

            Boolean result = commerceService.generateLicence(idCommerce, idPlan);
            response.put("SUCCESS", result);

            logger.info("License saved successfully");

            manageLogs.infoLogger(logTo,httpServletRequest,
                    "Successfully: " + logTo.getMethodName(),
                    "user",true);

        } catch (Exception e){
            logger.error(e.getMessage(), e);
            response.put("ERROR", false);
            response.put("message", "An error occurred: " + e.getMessage());

            manageLogs.severeErrorLogger(logTo,httpServletRequest,
                    "Error: "+ logTo.getMethodName() + ": "+ e,
                    e,"user",true);

        }

        return response;
    }

    @GetMapping("/getAllBanks")
    public LinkedHashMap<String, Object> getAllBanks(final HttpServletRequest httpServletRequest) {

        LogsClass logTo = new LogsClass(httpServletRequest, new Object() {}.
                getClass().getEnclosingMethod().getName());

        LinkedHashMap<String, Object> response = new LinkedHashMap<>();

        try {
           List<BankEntity> bankEntities = commerceService.getAllBanksByStatusTrue();

            response.put("bankEntities", bankEntities);
            logger.info("BankList find successfully");

            manageLogs.infoLogger(logTo,httpServletRequest,
                    "Successfully: " + logTo.getMethodName(),
                    "user",false);

            return response;

        } catch (Exception e) {
            response.put("ERROR", false);
            logger.error(e.getMessage(), e);

            manageLogs.severeErrorLogger(logTo,httpServletRequest,
                    "Error: "+ logTo.getMethodName() + ": "+ e,
                    e,"user",false);
        }
        return response;
    }

    @PostMapping("/getCommerceBankInformation")
    public LinkedHashMap<String, Object> getCommerceBankInformation(@RequestBody final String dataForm,
                                                                    final HttpServletRequest httpServletRequest) {

        LogsClass logTo = new LogsClass(httpServletRequest, new Object() {}.
                getClass().getEnclosingMethod().getName());

        LinkedHashMap<String, Object> response = new LinkedHashMap<>();

        try {
            String rif = (String) new JSONObject(dataForm).get("rif");

            CommerceBankInformationTo commerceBankInformationTo = commerceService.getCommerceBankInformation(rif);
            response.put("commerceBankInformationTo", commerceBankInformationTo);
            logger.info("BankList find successfully");

            manageLogs.infoLogger(logTo,httpServletRequest,
                    "Successfully: " + logTo.getMethodName(),
                    "user",false);

            return response;

        } catch (Exception e) {
            response.put("ERROR", false);
            logger.error(e.getMessage(), e);

            manageLogs.severeErrorLogger(logTo,httpServletRequest,
                    "Error: "+ logTo.getMethodName() + ": "+ e,
                    e,"user",false);
        }
        return response;
    }


    @PostMapping("/saveSupportContact")
    public LinkedHashMap<String, Object> supportContact(@RequestBody final String dataForm,
                                                         final HttpServletRequest httpServletRequest) {

        LogsClass logTo = new LogsClass(httpServletRequest, new Object() {}.
                getClass().getEnclosingMethod().getName());

        LinkedHashMap<String, Object> response = new LinkedHashMap<>();

        try {

            Long idUser = Long.parseLong((new JSONObject(dataForm).get("idUser")).toString());
            String phoneNumberCommerce = (new JSONObject(dataForm).get("phoneNumberCommerce")).toString();
            String contactPersonEmail = (new JSONObject(dataForm).get("contactPersonEmail")).toString();
            String description = (new JSONObject(dataForm).get("description")).toString();

            SupportContactTo result = commerceService.getSupportContactInformation(idUser,phoneNumberCommerce,contactPersonEmail,description);
            response.put("clientEntity", result.getClientEntity());
            response.put("commerceEntity", result.getCommerceEntity());
            response.put("supportEntity",result.getSupportEntity());

            logger.info("Support saved successfully");

            manageLogs.infoLogger(logTo,httpServletRequest,
                    "Successfully: " + logTo.getMethodName(),
                    "user",true);

        } catch (Exception e){
            logger.error(e.getMessage(), e);
            response.put("ERROR", true);
            response.put("message", "An error occurred: " + e.getMessage());

            manageLogs.severeErrorLogger(logTo,httpServletRequest,
                    "Error: "+ logTo.getMethodName() + ": "+ e,
                    e,"user",true);

        }

        return response;
    }


    @PostMapping("/getFilterCommerceByDate")
    public PaginatedResponseTo<CommerceTo> getFilterCommerceByDate(@RequestBody LinkedHashMap body, final HttpServletRequest httpServletRequest) {
        LogsClass logTo = new LogsClass(httpServletRequest, new Object() {}.
                getClass().getEnclosingMethod().getName());
        LinkedHashMap<String, Object> response = new LinkedHashMap<>();
        try {
            ObjectMapper objectMapper = new ObjectMapper();

            String startDateStr = objectMapper.convertValue(body.get("startDate"), String.class);
            String endDateStr = objectMapper.convertValue(body.get("endDate"), String.class);
            int index = objectMapper.convertValue(body.get("index"), int.class);
            int quantity = objectMapper.convertValue(body.get("quantity"), int.class);
            String filter = objectMapper.convertValue(body.get("filter"), String.class);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate startDateLocal = LocalDate.parse(startDateStr, formatter);
            LocalDate endDateLocal = LocalDate.parse(endDateStr, formatter);

            Date startDate = Date.valueOf(startDateLocal);
            Date endDate = Date.valueOf(endDateLocal);
            Page<CommerceTo> page = commerceService.getAllCommerceByDate(PageRequest.of(index, quantity),startDate,endDate, filter);
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

    @PostMapping("/getFilterCommerceByDateExport")
    public LinkedHashMap<String, Object> getFilterCommerceByDateExport(@RequestBody LinkedHashMap body, final HttpServletRequest httpServletRequest) {
        LogsClass logTo = new LogsClass(httpServletRequest, new Object() {}.
                getClass().getEnclosingMethod().getName());
        LinkedHashMap<String, Object> response = new LinkedHashMap<>();
        try {
            ObjectMapper objectMapper = new ObjectMapper();

            String startDateStr = objectMapper.convertValue(body.get("startDate"), String.class);
            String endDateStr = objectMapper.convertValue(body.get("endDate"), String.class);
            String filter = objectMapper.convertValue(body.get("filter"), String.class);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate startDateLocal = LocalDate.parse(startDateStr, formatter);
            LocalDate endDateLocal = LocalDate.parse(endDateStr, formatter);

            Date startDate = Date.valueOf(startDateLocal);
            Date endDate = Date.valueOf(endDateLocal);
            List<CommerceTo> page = commerceService.getAllCommerceByDateExport(startDate,endDate, filter);
            response.put("commerceFilter", page);
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

    @PostMapping("/editProfileByCommerce")
    public LinkedHashMap<String, Object> editProfileByCommerce(@RequestBody final String dataForm,
                                                                    final HttpServletRequest httpServletRequest) {

        LogsClass logTo = new LogsClass(httpServletRequest, new Object() {}.
                getClass().getEnclosingMethod().getName());

        LinkedHashMap<String, Object> response = new LinkedHashMap<>();

        try {

            JSONObject jsonObject = new JSONObject(dataForm);
            String activationDate = null;

            Long idCommerce = jsonObject.getLong("idCommerce");
            String commerceName = jsonObject.getString("commerceName");
            String contactPerson = jsonObject.getString("contactPerson");
            String contactPersonEmail = jsonObject.getString("contactPersonEmail");
            String phoneNumber = jsonObject.getString("phoneNumber");
            Long idStatusCommerce = jsonObject.getLong("idStatusCommerce");
            Long idPlan = jsonObject.getLong("idPlan");
            Long idCity = jsonObject.getLong("idCity");
            String address = jsonObject.getString("address");
            Long idCommerceActivity = jsonObject.getLong("idCommerceActivity");
            if(jsonObject.has("activationDate")) {
                 activationDate = jsonObject.getString("activationDate");
            }

            String reasonStatus = jsonObject.
                    getString("reasonStatus") != null ? jsonObject.getString("reasonStatus") : null;

            CommerceEntity commerceEntity = commerceService.editProfileByCommerce(idCommerce, commerceName,
                    contactPerson, contactPersonEmail, phoneNumber,
                    idStatusCommerce, idPlan, reasonStatus,idCity,address, idCommerceActivity,activationDate ,httpServletRequest);

            response.put("commerceEntity", commerceEntity);
            logger.info("Commerce information changed successfully");

            manageLogs.infoLogger(logTo,httpServletRequest,
                    "Successfully: " + logTo.getMethodName(),
                    "user",true);

        } catch (Exception e) {
            response.put("ERROR", e.getMessage());
            logger.error(e.getMessage(), e);

            manageLogs.severeErrorLogger(logTo,httpServletRequest,
                    "Error: "+ logTo.getMethodName() + ": "+ e,
                    e,"user",true);
        }

        return response;
    }

    @GetMapping("/getStatusCommerce/{idStatusCommerce}")
    public LinkedHashMap<String, Object> getStatusCommerce(@PathVariable final Long idStatusCommerce,
                                                           final HttpServletRequest httpServletRequest) {

        LogsClass logTo = new LogsClass(httpServletRequest, new Object() {}.
                getClass().getEnclosingMethod().getName());

        LinkedHashMap<String, Object> response = new LinkedHashMap<>();

        try {

            List<StatusCommerceEntity> statusCommerceEntities = commerceService.
                    getAllStatusCommerceByIdStatus(idStatusCommerce);

            response.put("statusCommerceEntities", statusCommerceEntities);

        } catch (Exception e) {
            response.put("ERROR", e.getMessage());
            logger.error(e.getMessage(), e);

            manageLogs.severeErrorLogger(logTo,httpServletRequest,
                    "Error: "+ logTo.getMethodName() + ": "+ e,
                    e,"user",true);
        }

        return response;
    }

    @PostMapping("/validateExistCommerceDocument")
    public LinkedHashMap<String, Object> validateExistCommerceDocument(@RequestBody final String dataForm,
                                                                       final HttpServletRequest httpServletRequest) {

        LogsClass logTo = new LogsClass(httpServletRequest, new Object() {}.
                getClass().getEnclosingMethod().getName());

        LinkedHashMap<String, Object> response = new LinkedHashMap<>();

        try {

            JSONObject jsonObject = new JSONObject(dataForm);
            String commerceDocument = jsonObject.getString("commerceDocument");

            CommerceEntity commerceEntity = commerceService.
                    validateExistByCommerceDocument(commerceDocument);

            if (commerceEntity == null) {
                response.put("SUCCESS", true);
            } else {
                response.put("ERROR", false);
            }

            manageLogs.infoLogger(logTo,httpServletRequest,
                    "Successfully: " + logTo.getMethodName(),
                    "user",false);

            return response;

        } catch (Exception e) {
            response.put("ERROR", e.getMessage());

            manageLogs.severeErrorLogger(logTo,httpServletRequest,
                    "Error: "+ logTo.getMethodName() + ": "+ e,
                    e,"user",false);
        }

        return response;
    }

    @PostMapping("/validateCommerceLicence")
    public LinkedHashMap<String, Object> validateCommerceLicence(@RequestBody final String dataForm,
                                                                       final HttpServletRequest httpServletRequest) {

        LogsClass logTo = new LogsClass(httpServletRequest, new Object() {}.
                getClass().getEnclosingMethod().getName());

        LinkedHashMap<String, Object> response = new LinkedHashMap<>();

        try {

            JSONObject jsonObject = new JSONObject(dataForm);
            String commerceDocument = jsonObject.getString("rif");

            String commerceMessage = commerceService.
                    validateCommerceLicence(commerceDocument);

            if (commerceMessage == "SUCCESS") {
                response.put("SUCCESS", true);
            } else {
                response.put("ERROR", commerceMessage);
            }

            manageLogs.infoLogger(logTo,httpServletRequest,
                    "Successfully: " + logTo.getMethodName(),
                    "user",false);

            return response;

        } catch (Exception e) {
            response.put("ERROR", e.getMessage());

            manageLogs.severeErrorLogger(logTo,httpServletRequest,
                    "Error: "+ logTo.getMethodName() + ": "+ e,
                    e,"user",false);
        }

        return response;
    }


    @PostMapping("/findCommerceInformation")
    public LinkedHashMap<String, Object> findCommerceInformation(final HttpServletRequest httpServletRequest) {
        LogsClass logTo = new LogsClass(httpServletRequest, new Object() {}.
                getClass().getEnclosingMethod().getName());
        LinkedHashMap<String, Object> response = new LinkedHashMap<>();
        try {
            ObjectMapper objectMapper = new ObjectMapper();

            List<CommerceInformationTo> commerceInformationTos = commerceService.findAllCommerces();
            response.put("commerceInformationList", commerceInformationTos);
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

    @PostMapping("/getAllCommerceByFilter")
    public LinkedHashMap<String, Object> getAllCommerceByFilter(@RequestBody LinkedHashMap body,
                                                                       final HttpServletRequest httpServletRequest) {
        LogsClass logTo = new LogsClass(httpServletRequest, new Object() {}.
                getClass().getEnclosingMethod().getName());
        LinkedHashMap<String, Object> response = new LinkedHashMap<>();
        try {
            ObjectMapper objectMapper = new ObjectMapper();

            List<CommerceTo> commerceToList = filterService.getAllCommerceFilter(body, objectMapper);
            response.put("commerceFilter", commerceToList);
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

}
