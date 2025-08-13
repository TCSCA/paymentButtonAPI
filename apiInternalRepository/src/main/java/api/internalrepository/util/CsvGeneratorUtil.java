package api.internalrepository.util;

import api.internalrepository.entity.ConfigurationEntity;
import api.internalrepository.repository.ConfigurationRepository;
import api.internalrepository.service.BankTransactionService;
import api.internalrepository.to.BankTransactionDailyTo;
import api.logsClass.ManageLogs;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.nio.file.Paths;

@Component
public class CsvGeneratorUtil {

    private final ManageLogs manageLogs;

    private static final Logger logger = LoggerFactory.getLogger(BankTransactionService.class);

    private final ConfigurationRepository configurationRepository;

    public CsvGeneratorUtil(ManageLogs manageLogs, ConfigurationRepository configurationRepository) {
        this.manageLogs = manageLogs;
        this.configurationRepository = configurationRepository;
    }


    public Boolean generateBillingXlsx(List<BankTransactionDailyTo> bankTransactionDailyTos,
                                       String directory, String fileName, LocalDate generationDate, Boolean prod) throws IOException {

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("facturacion");
        String filePath = Paths.get(directory, fileName).toString();

        String[] rowsName = { "id_canal_pago", "Nombre del Canal", "Id Comercio,", "Rif", "Nombre_Comercio", "id_metodo_pago",
                              "Nombre del Metodo", "Id_estado_transaccion", "ID_transaccion", "Referencia", "Monto",
                              "fecha_transaccion","Id_Plan", "Nombre Plan", "Fecha_generacion_archivo", "Fecha_activación_tienda" };

        try {
            logger.info("Metodo de generación archivo xlsx ");

            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < rowsName.length; i++) {
                headerRow.createCell(i).setCellValue(rowsName[i]);
            }

            int rowNum = 1;
            for (BankTransactionDailyTo bank : bankTransactionDailyTos) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(bank.getIdPaymentChannel());
                row.createCell(1).setCellValue(bank.getPaymentChannelDescription());
                row.createCell(2).setCellValue(bank.getCommerceId());
                row.createCell(3).setCellValue(bank.getCommerceDocument());
                row.createCell(4).setCellValue(bank.getCommerceName());
                row.createCell(5).setCellValue(bank.getIdPaymentMethod());
                row.createCell(6).setCellValue(bank.getPaymentMethodName());
                row.createCell(7).setCellValue(bank.getIdBankTransactionStatus());
                row.createCell(8).setCellValue(bank.getIdBankTransaction());
                row.createCell(9).setCellValue(bank.getReference() != null && bank.getReference() != 0 ? bank.getReference().toString() : " " );
                Cell cell = row.createCell(10);
                cell.setCellType(CellType.NUMERIC);
                cell.setCellValue(bank.getAmount().floatValue());
                row.createCell(11).setCellValue(bank.getTransactionDate().toString());
                row.createCell(12).setCellValue(bank.getIdPlan());
                row.createCell(13).setCellValue(bank.getPlanName());
                row.createCell(14).setCellValue(generationDate.toString());
                row.createCell(15).setCellValue(bank.getActivationDate().toString() != null ? bank.getActivationDate().toString() : " ");

            }

            FileOutputStream outputStream = new FileOutputStream(filePath);
            workbook.write(outputStream);
            outputStream.close();
            workbook.close();

            logger.info("Se creo archivo local: " + fileName + "en ruta:" + filePath);

            if(prod) {

                logger.info("Archivo temporal: " + fileName + "en ruta:" + filePath);

                if(createFtpConnection(fileName, filePath)) {
                    logger.info("Se creo correctamente archivo excel en ftp server");
                } else {
                    logger.error("Se creo correctamente archivo excel en ftp server");
                }

            }

                logger.info("Se creo correctamente archivo excel" + fileName + "en ruta:" + directory);



            return true;

        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            logger.error("Error creando archivo excel" + fileName + "en ruta:" + directory);
            return false;
        }
    }

    private Boolean createFtpConnection( String fileName, String filePath) throws IOException {
        try {
            logger.info("Método createFtpConnection");
            List<Long> idConfiguration = new ArrayList<>();
            idConfiguration.add(18L);
            idConfiguration.add(19L);
            idConfiguration.add(20L);
            idConfiguration.add(21L);
            idConfiguration.add(22L);

            List<ConfigurationEntity> configurationEntities = configurationRepository.findAllByIdConfigurationIn(idConfiguration);

            LinkedHashMap<String, String> properties = new LinkedHashMap<>();
            properties.put("Ip Ftp server: ",  configurationEntities.get(0).getValue());
            properties.put("Ruta Ftp server: ", configurationEntities.get(4).getValue());

            logger.info("Propiedades de server ftp: " + properties);

            File firstLocalFile = new File(filePath);


            FTPClient ftpClient = new FTPClient();
            ftpClient.connect(configurationEntities.get(0).getValue(), Integer.valueOf(configurationEntities.get(3).getValue()));
            ftpClient.login(configurationEntities.get(1).getValue(), configurationEntities.get(2).getValue());
            ftpClient.enterLocalPassiveMode();

            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);

            logger.info("Conexión exitosa a server Ftp");

            FileInputStream inputStream = new FileInputStream(firstLocalFile);

            logger.info("Ruta archivo local : " + filePath);

            logger.info("Ruta archivo remoto : " + configurationEntities.get(4).getValue().concat(fileName));

            ftpClient.storeFile(configurationEntities.get(4).getValue().concat(fileName), inputStream);
            inputStream.close();

            logger.info("Se guardo correctamente archivo en server Ftp");

            ftpClient.logout();
            ftpClient.disconnect();

            logger.info("Archivo guardado en: " + configurationEntities.get(4).getValue());

            return true;

        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            logger.error("Error conectando a server Ftp");
            return false;
        }

    }

}
