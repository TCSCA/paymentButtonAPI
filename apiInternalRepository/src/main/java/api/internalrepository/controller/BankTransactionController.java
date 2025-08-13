package api.internalrepository.controller;

import api.internalrepository.entity.BankTransactionEntity;
import api.internalrepository.request.ManualPaymentRequest;
import api.internalrepository.request.PaymentLinkRequest;
import api.internalrepository.service.BankTransactionService;
import api.internalrepository.service.FilterService;
import api.internalrepository.to.BankTransactionListTo;
import api.logsClass.LogsClass;
import api.logsClass.ManageLogs;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
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
@RequestMapping(value = "/core")
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST})
public class BankTransactionController {

    private static final Logger logger = LoggerFactory.getLogger(BankTransactionController.class);

    private final BankTransactionService bankTransactionService;

    private final ManageLogs manageLogs;

    private final FilterService filterService;

    public BankTransactionController(BankTransactionService bankTransactionService, ManageLogs manageLogs, FilterService filterService) {
        this.bankTransactionService = bankTransactionService;
        this.manageLogs = manageLogs;
        this.filterService = filterService;
    }

    @PostMapping("/generatePaymentLink")
    public LinkedHashMap<String, Object> generatePaymentLink(@RequestBody final String dataForm,
                                                             final HttpServletRequest httpServletRequest) {

        LogsClass logTo = new LogsClass(httpServletRequest, new Object() {}.
                getClass().getEnclosingMethod().getName());

        LinkedHashMap<String, Object> response = new LinkedHashMap<>();
        ObjectMapper objectMapper = new ObjectMapper();

        try {

            LinkedHashMap<String, Object> requestMap = objectMapper.readValue(dataForm, LinkedHashMap.class);

            PaymentLinkRequest paymentLinkRequest = objectMapper.
                    convertValue(requestMap.get("paymentLinkRequest"), PaymentLinkRequest.class);

            BankTransactionEntity bankTransactionEntity = bankTransactionService.
                    generateBankTransactionByPaymentLink(paymentLinkRequest);

            response.put("idBankTransaction", bankTransactionEntity.getIdBankTransaction());

            manageLogs.infoLogger(logTo,httpServletRequest,
                    "Successfully: " + logTo.getMethodName(),
                    "user",true);

        } catch (Exception e) {
            response.put("ERROR", "error");

            logger.error(e.getMessage(), e);
            manageLogs.severeErrorLogger(logTo,httpServletRequest,
                    "Error: "+ logTo.getMethodName() + ": "+ e,
                    e,"user",true);
        }

        return response;
    }

    @GetMapping("/getInformationByIdBankTransaction/{idBankTransaction}")
    public LinkedHashMap<String, Object> getInformationByIdBankTransaction(
            @PathVariable("idBankTransaction") final Long idBankTransaction,
            final HttpServletRequest httpServletRequest) {

        LogsClass logTo = new LogsClass(httpServletRequest, new Object() {}.
                getClass().getEnclosingMethod().getName());

        LinkedHashMap<String, Object> response = new LinkedHashMap<>();

        try {

            BankTransactionEntity bankTransactionEntity = bankTransactionService.
                    getInformationByIdBankTransaction(idBankTransaction);

            if (bankTransactionEntity.getTransactionStatusEntity().getIdTransactionStatus() == 1) {
                response.put("ERROR", 1);
            } else if (bankTransactionEntity.getTransactionStatusEntity().getIdTransactionStatus() == 2) {
                response.put("ERROR", 2);
            } else if (bankTransactionEntity.getTransactionStatusEntity().getIdTransactionStatus() == 5) {
                response.put("ERROR", 5);
            }

            response.put("bankTransactionEntity", bankTransactionEntity);

            manageLogs.infoLogger(logTo,httpServletRequest,
                    "Successfully: " + logTo.getMethodName(),
                    "user",false);

        } catch (Exception e){
            response.put("ERROR", "error");

            logger.error(e.getMessage(), e);
            manageLogs.severeErrorLogger(logTo,httpServletRequest,
                    "Error: "+ logTo.getMethodName() + ": "+ e,
                    e,"user",false);
        }

        return response;
    }

    @PostMapping("/saveManualPayment")
    public LinkedHashMap<String, Object> saveManualPayment(@RequestBody final String dataForm,
                                                             final HttpServletRequest httpServletRequest) {

        LogsClass logTo = new LogsClass(httpServletRequest, new Object() {}.
                getClass().getEnclosingMethod().getName());

        LinkedHashMap<String, Object> response = new LinkedHashMap<>();
        ObjectMapper objectMapper = new ObjectMapper();

        try {

            LinkedHashMap<String, Object> requestMap = objectMapper.readValue(dataForm, LinkedHashMap.class);

            ManualPaymentRequest manualPaymentRequest = objectMapper.
                    convertValue(requestMap.get("manualPaymentRequest"), ManualPaymentRequest.class);

            bankTransactionService.saveManualPaymentInformation(manualPaymentRequest);

            response.put("SUCCESS", "Transacción Aprobada");

            manageLogs.infoLogger(logTo,httpServletRequest,
                    "Successfully: " + logTo.getMethodName(),
                    "user",false);

        } catch (Exception e) {
            response.put("ERROR", "error");

            logger.error(e.getMessage(), e);
            manageLogs.severeErrorLogger(logTo,httpServletRequest,
                    "Error: "+ logTo.getMethodName() + ": "+ e,
                    e,"user",false);
        }

        return response;
    }

    @PostMapping("/getAllManualPayments")
    public LinkedHashMap<String, Object> getAllManualPayments(@RequestBody final String dataForm,
                                                           final HttpServletRequest httpServletRequest) {

        LogsClass logTo = new LogsClass(httpServletRequest, new Object() {}.
                getClass().getEnclosingMethod().getName());

        LinkedHashMap<String, Object> response = new LinkedHashMap<>();
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            LinkedHashMap<String, Object> requestMap = objectMapper.readValue(dataForm, LinkedHashMap.class);

            Boolean export = objectMapper.convertValue(requestMap.get("export"), Boolean.class);

            if(export) {
                List<BankTransactionListTo>  bankTransactionListTos = bankTransactionService.getAllManualPaymentsByIntervalDateExport(requestMap, objectMapper);
                response.put("bankTransactionEntityExport", bankTransactionListTos);
            } else {
                Page<BankTransactionEntity> bankTransactionList = bankTransactionService.
                        getAllManualPaymentsByIntervalDate(requestMap, objectMapper);
                response.put("bankTransactionEntity", bankTransactionList);
            }



            manageLogs.infoLogger(logTo,httpServletRequest,
                    "Successfully: " + logTo.getMethodName(),
                    "user",false);

        } catch (Exception e) {
            response.put("ERROR", "error");

            logger.error(e.getMessage(), e);
            manageLogs.severeErrorLogger(logTo,httpServletRequest,
                    "Error: "+ logTo.getMethodName() + ": "+ e,
                    e,"user",true);
        }

        return response;
    }

    @PostMapping("/changeStatusBankTransactionManualPayment")
    public LinkedHashMap<String, Object> changeStatusBankTransactionManualPayment(@RequestBody final String dataForm,
                                                              final HttpServletRequest httpServletRequest) {

        LogsClass logTo = new LogsClass(httpServletRequest, new Object() {}.
                getClass().getEnclosingMethod().getName());

        LinkedHashMap<String, Object> response = new LinkedHashMap<>();
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            LinkedHashMap<String, Object> requestMap = objectMapper.readValue(dataForm, LinkedHashMap.class);

            bankTransactionService.changeStatusManualPayment(requestMap, objectMapper);

            response.put("SUCCESS", "Transacción exitosa");

        } catch (Exception e) {
            response.put("ERROR", "error en metodo de cambiar estado");

            logger.error(e.getMessage(), e);
            manageLogs.severeErrorLogger(logTo,httpServletRequest,
                    "Error: "+ logTo.getMethodName() + ": "+ e,
                    e,"user",true);
        }

        return response;
    }

    @PostMapping("/getAllBankTransactionsByFilter")
    public LinkedHashMap<String, Object> getAllBankTransactionsByFilter(@RequestBody LinkedHashMap body,
                                                                        final HttpServletRequest httpServletRequest) {
        LogsClass logTo = new LogsClass(httpServletRequest, new Object() {}.
                getClass().getEnclosingMethod().getName());
        LinkedHashMap<String, Object> response = new LinkedHashMap<>();
        try {
            ObjectMapper objectMapper = new ObjectMapper();

            List<BankTransactionListTo> bankTransactionListTos = filterService.getAllBankTransactionsByFilter(body, objectMapper);
            response.put("bankTransactionListTos", bankTransactionListTos);
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

    @PostMapping("/getAllBankTransactionsByPaymentMethodFilter")
    public LinkedHashMap<String, Object> getAllBankTransactionsByPaymentMethodFilter(@RequestBody LinkedHashMap body,
                                                                        final HttpServletRequest httpServletRequest) {
        LogsClass logTo = new LogsClass(httpServletRequest, new Object() {}.
                getClass().getEnclosingMethod().getName());
        LinkedHashMap<String, Object> response = new LinkedHashMap<>();
        try {
            ObjectMapper objectMapper = new ObjectMapper();

            List<BankTransactionListTo> bankTransactionListTos = filterService.getAllBankTransactionsPaymentMethodByFilter(body, objectMapper);
            response.put("bankTransactionListTos", bankTransactionListTos);
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

    @PostMapping("/getAllBankTransactionsByManualPaymentFilter")
    public LinkedHashMap<String, Object> getAllBankTransactionsByManualPaymentFilter(@RequestBody LinkedHashMap body,
                                                                                     final HttpServletRequest httpServletRequest) {
        LogsClass logTo = new LogsClass(httpServletRequest, new Object() {}.
                getClass().getEnclosingMethod().getName());
        LinkedHashMap<String, Object> response = new LinkedHashMap<>();
        try {
            ObjectMapper objectMapper = new ObjectMapper();

            List<BankTransactionListTo> bankTransactionListTos = filterService.getAllBankTransactionsManualPaymentByFilter(body, objectMapper);
            response.put("bankTransactionListTos", bankTransactionListTos);
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

    @PostMapping("/getAllBankTransactionsAndStatusByFilter")
    public LinkedHashMap<String, Object> getAllBankTransactionsAndStatusByFilter(@RequestBody LinkedHashMap body,
                                                                        final HttpServletRequest httpServletRequest) {
        LogsClass logTo = new LogsClass(httpServletRequest, new Object() {}.
                getClass().getEnclosingMethod().getName());
        LinkedHashMap<String, Object> response = new LinkedHashMap<>();
        try {
            ObjectMapper objectMapper = new ObjectMapper();

            List<BankTransactionListTo> bankTransactionListTos = filterService.getAllBankTransactionsAndStatusByFilter(body, objectMapper);
            response.put("bankTransactionListTos", bankTransactionListTos);
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


    @PostMapping("/getAllBankTransactionsAndTypeProductByFilter")
    public LinkedHashMap<String, Object> getAllBankTransactionsAndTypeProductByFilter(@RequestBody LinkedHashMap body,
                                                                                 final HttpServletRequest httpServletRequest) {
        LogsClass logTo = new LogsClass(httpServletRequest, new Object() {}.
                getClass().getEnclosingMethod().getName());
        LinkedHashMap<String, Object> response = new LinkedHashMap<>();
        try {
            ObjectMapper objectMapper = new ObjectMapper();

            List<BankTransactionListTo> bankTransactionListTos = filterService.getAllBankTransactionsAndProductByFilter(body, objectMapper);
            response.put("bankTransactionListTos", bankTransactionListTos);
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
