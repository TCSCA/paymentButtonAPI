package api.internalrepository.utilFile;


import api.internalrepository.entity.ConfigurationEntity;
import api.internalrepository.repository.ConfigurationRepository;
import api.internalrepository.to.BankTransactionDailyTo;
import api.internalrepository.util.CsvGeneratorUtil;

import api.logsClass.ManageLogs;

import org.apache.commons.net.ftp.FTPClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class FileStorageService {

    private final Environment environment;

    private static final Logger logger = LoggerFactory.getLogger(FileStorageService.class);

    private final Path fileStorageLocation;

    private final ManageLogs manageLogs;

    private final CsvGeneratorUtil csvGeneratorUtil;

    private final ConfigurationRepository configurationRepository;


    @Autowired
    public FileStorageService(FileStorageProperties fileStorageProperties, Environment environment, ManageLogs manageLogs, CsvGeneratorUtil csvGeneratorUtil, ConfigurationRepository configurationRepository) {
        this.fileStorageLocation = Paths.get(fileStorageProperties.getUploadDir())
                .toAbsolutePath().normalize();

        this.environment = environment;
        this.manageLogs = manageLogs;
        this.csvGeneratorUtil = csvGeneratorUtil;
        this.configurationRepository = configurationRepository;

        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new FileStorageException("Could not create the directory where the uploaded files will be stored.", ex);
        }
    }

    public Boolean storeBillingFile(List<BankTransactionDailyTo> bankTransactionDailyTos, LocalDate generationDate, String rif) {

        try {

            String formatPattern = "yyyyMMdd";
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(formatPattern);
            String formattedDate = generationDate.format(formatter);

            ConfigurationEntity configurationEntity = configurationRepository.findConfigurationEntityByIdConfiguration(17L);
            String fileName;

            if(rif != null) {
                fileName = configurationEntity.getValue().concat(rif).concat("_").concat(formattedDate).concat(".xlsx");
            } else {
                fileName  = configurationEntity.getValue().concat(formattedDate).concat(".xlsx");
            }

            if (fileName.contains("..")) {
                throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
            }

            logger.info("Ruta del directorio: " + fileStorageLocation.toString());

            Boolean prod = environment.getProperty("app.platform").contains("prod");

           if(csvGeneratorUtil.generateBillingXlsx(bankTransactionDailyTos,
                   fileStorageLocation.toString(), fileName,generationDate, prod)) {

               if (!prod) {
                   logger.info("Archivo guardado en: " + fileStorageLocation.toString());
               }
               return true;

           } else {
               logger.error("Error guardando archivo en: " + fileStorageLocation.toString());
               return false;
           }


        } catch (FileStorageException e) {
            logger.error(e.getMessage(), e);
            return false;
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            return false;
        }
    }



}
