package api.internalrepository.service;

import api.internalrepository.repository.BankTransactionRepository;
import api.internalrepository.to.BankTransactionDailyTo;
import api.internalrepository.utilFile.FileStorageService;
import api.logsClass.LogsClass;
import api.logsClass.ManageLogs;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

@Service
public class BillingService {

    private final BankTransactionRepository bankTransactionRepository;

    private final FileStorageService fileStorageService;

    private static final Logger logger = LoggerFactory.getLogger(BankTransactionService.class);

    private final ManageLogs manageLogs;

    public BillingService(BankTransactionRepository bankTransactionRepository, FileStorageService fileStorageService, ManageLogs manageLogs) {
        this.bankTransactionRepository = bankTransactionRepository;
        this.fileStorageService = fileStorageService;
        this.manageLogs = manageLogs;
    }


    @Scheduled(cron = "#{@getCronValueBillingService}")
    public void getDailyBankTransactions() throws IOException {

        List<BankTransactionDailyTo> bankTransactionDailyTos = new ArrayList<>();
        LocalDate currentDate = LocalDate.now();
        bankTransactionDailyTos = bankTransactionRepository.getDailyBankTransactions(currentDate.minusDays(1));

        logger.info("Metodo de facturación automático " + OffsetDateTime.now(ZoneId.of("America/Caracas")));
        logger.info("Se ejecuta facturacion TIME: " + OffsetDateTime.now(ZoneId.of("America/Caracas")));

        if(!bankTransactionDailyTos.isEmpty()) {
            logger.info("Facturacion transacciones aprobadas dia: " + currentDate.minusDays(1));

            fileStorageService.storeBillingFile(bankTransactionDailyTos,
                    currentDate.minusDays(1), null);

        } else {
            logger.info("No se encontraron transacciones aprobadas dia: " + currentDate.minusDays(1));
        }

    }

    public LinkedHashMap<String, Object> getDailyBankTransactionsByCommerce(LinkedHashMap<String, Object> request,
                                                   HttpServletRequest httpServletRequest,
                                                   LogsClass logsClass) throws IOException {

        List<BankTransactionDailyTo> bankTransactionDailyTos = new ArrayList<>();
        ObjectMapper objectMapper = new ObjectMapper();
        LinkedHashMap<String, Object> response = new LinkedHashMap<>();

        String billingDate = objectMapper.convertValue(request.get("billingDate"), String.class);
        String rif = objectMapper.convertValue(request.get("rif"), String.class);

        LocalDate billingDateParsed = LocalDate.parse(billingDate);
        if(rif != null) {
            bankTransactionDailyTos = bankTransactionRepository.getDailyBankTransactionsByCommerce(billingDateParsed, rif);
        } else {
            bankTransactionDailyTos = bankTransactionRepository.getDailyBankTransactions(billingDateParsed);
        }

        if(!bankTransactionDailyTos.isEmpty()) {

            manageLogs.infoLogger(logsClass,httpServletRequest,
                    "Facturacion transacciones aprobadas dia: " + billingDate,
                    "user",false);

            fileStorageService.storeBillingFile(bankTransactionDailyTos,billingDateParsed, rif);
            response.put("SUCCESS", "Transación Aprobada");
        } else {

            manageLogs.infoLogger(logsClass,httpServletRequest,
                    "No se encontraron transacciones aprobadas dia: " + billingDate,
                    "user",false);

            response.put("ERROR", "No se encontraron transacciones aprobadas");
        }

        return response;
    }
}
