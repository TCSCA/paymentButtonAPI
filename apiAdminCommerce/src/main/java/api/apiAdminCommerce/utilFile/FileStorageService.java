package api.apiAdminCommerce.utilFile;

import api.apiAdminCommerce.exception.FileStorageException;
import api.apiAdminCommerce.request.FileUploadRequest;
import api.apiAdminCommerce.security.jwt.JwtUtils;
import api.apiAdminCommerce.service.CommerceService;
import api.apiAdminCommerce.to.CommerceTo;
import api.apiAdminCommerce.to.PreRegisterTo;
import api.apiAdminCommerce.to.ResultFileDetailTo;
import api.apiAdminCommerce.to.ResultFileTo;
import api.apiAdminCommerce.util.LogTo;
import api.apiAdminCommerce.util.ManageLog;
import api.logsClass.LogsClass;
import api.logsClass.ManageLogs;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@Service
public class FileStorageService {

    private static final Logger logger = LoggerFactory.getLogger(FileStorageService.class);

    private final Path fileStorageLocation;

    private final CommerceService commerceService;

    private final ManageLog manageLog;

    private final Environment environment;

    private final ManageLogs manageLogs;

    @Autowired
    public FileStorageService(FileStorageProperties fileStorageProperties, CommerceService commerceService, ManageLog manageLog, Environment environment, ManageLogs manageLogs) {
        this.fileStorageLocation = Paths.get(fileStorageProperties.getUploadDir())
                .toAbsolutePath().normalize();

        this.commerceService = commerceService;
        this.manageLog = manageLog;
        this.environment = environment;
        this.manageLogs = manageLogs;

        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new FileStorageException("Could not create the directory where the uploaded files will be stored.", ex);
        }
    }

    public LinkedHashMap<String,String> storeFile(MultipartFile file,
                                                  final FileUploadRequest request,
                                                  final HttpServletRequest httpServletRequest,
                                                  final LogsClass logTo) {


        LinkedHashMap<String, String> linkedHashMap = new LinkedHashMap<>();
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));

        try {
            if (fileName.contains("..")) {
                throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
            }

            //TODO: si se esta subiendo un QR, es de un comercio y no de un pre-registro
            if (fileName.contains("QR") && request.getCommerceDocument() != null) {

                CommerceTo commerceTo = commerceService.
                        getCommerceByDocumentAndStatus(request.getCommerceDocument(), 2L,
                                httpServletRequest.getHeader("API_KEY"),
                                httpServletRequest.getHeader("token"));

                manageLogs.infoLogger(logTo,httpServletRequest,
                        "Rif del comercio: " + Objects.requireNonNull(commerceTo).getCommerceDocument(),
                        "user",false);

                File directory = new File(fileStorageLocation.toString() +
                        File.separator + Objects.requireNonNull(commerceTo).getCommerceDocument());

                manageLogs.infoLogger(logTo,httpServletRequest,
                        "Ruta del directorio: " + directory.getAbsolutePath(),
                        "user",false);

                Long idRequirementQr = null;
                if(request.getIdRequirement() == 11L) {
                    if(commerceTo.getIdTypeCommerce() == 1L) {
                        idRequirementQr = 11L;
                    } else {
                        idRequirementQr = 10L;
                    }
                }

                if (!directory.exists()) {
                    boolean dirsCreated = directory.mkdirs();
                    if (!dirsCreated) {
                        throw new FileStorageException("Could not create the directory where the uploaded files will be stored.");
                    } else {
                        manageLogs.infoLogger(logTo,httpServletRequest,
                                "Directorio creado correctamente: " + directory.getAbsolutePath(),
                                "user",false);
                    }
                }

                Path parentDir = directory.toPath();

                Path targetLocation = directory.toPath().resolve(fileName);

                targetLocation = resolveFileNameConflict(targetLocation, parentDir);

                Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

                logger.info("Archivo guardado en: {}", targetLocation);

                manageLogs.infoLogger(logTo,httpServletRequest,
                        "Archivo guardado en: " + targetLocation,
                        "user",false);

                Boolean resultFileSaved = saveResultFileInt(file.getSize(),
                        Objects.requireNonNull(commerceTo).getIdCommerce(),
                        idRequirementQr, targetLocation.getFileName().toString(), httpServletRequest);

                if (!resultFileSaved) {
                    manageLogs.errorLogger(logTo, httpServletRequest, "Error saving requirement file",
                            "user", false);
                }

                linkedHashMap.put("fileName", targetLocation.getFileName().toString());
                linkedHashMap.put("targetLocation", targetLocation.toString());

            } else {

                PreRegisterTo preRegisterTo = getPreRegisterById(request.getIdPreRegister(), httpServletRequest.
                        getHeader("API_KEY"));

                manageLogs.infoLogger(logTo,httpServletRequest,
                        "Rif del pre-registro: " + Objects.requireNonNull(preRegisterTo).getCommerceDocument(),
                        "user",false);

                File directory = new File(fileStorageLocation.toString() +
                        File.separator + Objects.requireNonNull(preRegisterTo).getCommerceDocument());

                manageLogs.infoLogger(logTo,httpServletRequest,
                        "Ruta del directorio: " + directory.getAbsolutePath(),
                        "user",false);

                if (!directory.exists()) {
                    boolean dirsCreated = directory.mkdirs();
                    if (!dirsCreated) {
                        throw new FileStorageException("Could not create the directory where the uploaded files will be stored.");
                    } else {
                        manageLogs.infoLogger(logTo,httpServletRequest,
                                "Directorio creado correctamente: " + directory.getAbsolutePath(),
                                "user",false);
                    }
                }

                Path parentDir = directory.toPath();

                Path targetLocation = directory.toPath().resolve(fileName);

                targetLocation = resolveFileNameConflict(targetLocation, parentDir);

                Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

                manageLogs.infoLogger(logTo,httpServletRequest,
                        "Archivo guardado en: " + targetLocation,
                        "user",false);

                Boolean resultFileSaved = saveResultFile(file.getSize(),
                        Objects.requireNonNull(preRegisterTo).getIdPreRegistro(),
                        request.getIdRequirement(), targetLocation.getFileName().toString(),
                        httpServletRequest);

                if (!resultFileSaved) {
                    manageLogs.errorLogger(logTo, httpServletRequest, "Error saving requirement file",
                            "user", false);
                }

                linkedHashMap.put("fileName", targetLocation.getFileName().toString());
                linkedHashMap.put("targetLocation", targetLocation.toString());

            }

            return linkedHashMap;

        } catch (IOException ex) {

            manageLogs.severeErrorLogger(logTo,httpServletRequest,"Error: "+ logTo.
                    getMethodName() + ": "+ ex, ex,"user",false);

            throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
        }
    }

    public Boolean storeTermsAndConditionsFile(MultipartFile file,
                                            final HttpServletRequest httpServletRequest,
                                            final FileUploadRequest request,
                                            final Long registerBy) throws IOException {

        try {

            String dateFolder = LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));

            File directory = new File(Paths.get(".."+ File.separator +"uploads") +
                    File.separator + "Terminos&Politicas");

            logger.info("Ruta del directorio: {}", directory.toPath().toRealPath());

            if (!directory.exists()) {
                boolean dirsCreated = directory.mkdirs();
                if (!dirsCreated) {
                    throw new FileStorageException("Could not create the directory where the uploaded files will be stored.");
                } else {
                    logger.info("Directorio creado correctamente: {}", directory.getAbsolutePath());
                }
            }

            Path targetLocation = directory.toPath().resolve(dateFolder);

            if (!Files.exists(targetLocation)) {
                Files.createDirectories(targetLocation);
            }

            String originalFilename = file.getOriginalFilename();
            String extension = originalFilename.substring(originalFilename.lastIndexOf('.'));
            String filename;
            String termsOrPolicies;

            if (request.getIdRequirement() == 1){
                filename = "Terminos&Condiciones" + extension;
                termsOrPolicies = "Terminos";
            } else {
                filename = "PoliticasDePrivacidad" + extension;
                termsOrPolicies = "Politicas";
            }

            Path filePath = targetLocation.resolve(filename);
            int count = 1;

            while (Files.exists(filePath)) {

                if (request.getIdRequirement() == 1){
                    filename = "Terminos&Condiciones(" + count + ")" + extension;
                } else {
                    filename = "PoliticasDePrivacidad(" + count + ")" + extension;
                }
                filePath = targetLocation.resolve(filename);
                count++;
            }

            Boolean saveResult = saveNewTermsAndConditions(httpServletRequest, filePath.toString(),
                    filename, termsOrPolicies, registerBy);

            if (saveResult) {
                Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
                logger.info("Ruta del archivo subido: " + filePath);
                return true;
            } else {
                logger.error("ERROR: saving " + filename);
                return false;
            }

        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            return false;
        }

    }

    public Boolean saveNewTermsAndConditions(final HttpServletRequest httpServletRequest,
                                             final String urlFile, final String fileName,
                                             final String termsOrPolicies,
                                             final Long registerBy) {

        try {

            HttpHeaders headers = new HttpHeaders();
            RestTemplate restTemplate = new RestTemplate();

            headers.add("API_KEY", httpServletRequest.getHeader("API_KEY"));
            headers.add("token", httpServletRequest.getHeader("token"));

            String uri = environment.getProperty("api.route.internal");

            LinkedHashMap<String, Object> requestBody = new LinkedHashMap<>();

            requestBody.put("urlFile", urlFile);
            requestBody.put("fileName", fileName);
            requestBody.put("termsOrPolicies", termsOrPolicies);
            requestBody.put("registerBy", registerBy);

            HttpEntity<LinkedHashMap<String, Object>> request = new HttpEntity<>(requestBody, headers);

            ResponseEntity<String> responseEntity = restTemplate.exchange(Objects.requireNonNull(uri).
                    concat("/saveNewTermsAndConditions"), HttpMethod.POST, request, String.class);

            if (!Objects.requireNonNull(responseEntity.getBody()).contains("ERROR")) {
                logger.info("New termsConditions saved successfully");
                return true;
            } else {
                logger.error("ERROR: " + responseEntity.getBody());
                return false;
            }

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return false;
        }

    }

    public List<ResultFileDetailTo> getAllResultFileByIdPreRegister(final Long idPreRegister,
                                                                    final Long idCommerce,
                                                                    final String apiKey) {

        try {

            HttpHeaders headers = new HttpHeaders();
            RestTemplate restTemplate = new RestTemplate();

            headers.add("API_KEY", apiKey);

            String uri = "http://localhost:8091/horizonte";

            HttpEntity<LinkedHashMap<String, Object>> request = new HttpEntity<>(headers);

            ResponseEntity<String> responseEntity = restTemplate.exchange(uri.
                    concat("/getAllResultFileByIdPreRegister/"+idPreRegister), HttpMethod.GET,
                    request, String.class);

            if (!Objects.requireNonNull(responseEntity.getBody()).contains("ERROR")) {

                ObjectMapper objectMapper = new ObjectMapper();

                LinkedHashMap<String, Object> responseMap = objectMapper.readValue(responseEntity.
                        getBody(), LinkedHashMap.class);

                List<LinkedHashMap<String, Object>> resultFileEntities = (List<LinkedHashMap<String, Object>>) responseMap.get("resultFileEntities");

                return resultFileEntities.stream().map(map -> {
                    ResultFileDetailTo resultFileDetailTo = new ResultFileDetailTo();
                    resultFileDetailTo.setIdResultFile(((Number) map.get("idResultFile")).longValue());
                    resultFileDetailTo.setSizeFile(((Number) map.get("size")).longValue());
                    resultFileDetailTo.setChargedBy(((Number) map.get("chargedBy")).longValue());
                    resultFileDetailTo.setIdCommerce(idCommerce);

                    String chargedDateString = (String) map.get("chargedDate");
                    OffsetDateTime chargedDate = chargedDateString != null ? OffsetDateTime.parse(chargedDateString) : null;
                    resultFileDetailTo.setChargedDate(chargedDate);

                    LinkedHashMap<String, Object> requirementMap = (LinkedHashMap<String, Object>) map.get("requirementEntity");
                    resultFileDetailTo.setIdRequirement(((Number) requirementMap.get("idRequirement")).longValue());
                    resultFileDetailTo.setFileName((String) map.get("fileName"));

                    return resultFileDetailTo;
                }).toList();

            } else {
                logger.error("Response: " + responseEntity.getBody());
                return null;
            }

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return null;
        }

    }

    public Boolean saveAllResultFileByCommerce(final List<ResultFileDetailTo> resultFileDetailToList,
                                               final String apiKey,
                                               final String token) {

        try {

            HttpHeaders headers = new HttpHeaders();
            RestTemplate restTemplate = new RestTemplate();

            headers.add("API_KEY", apiKey);
            headers.add("token", token);

            String uri = "http://localhost:8090/core";

            LinkedHashMap<String, Object> requestBody = new LinkedHashMap<>();

            requestBody.put("resultFileDetailToList", resultFileDetailToList);

            HttpEntity<LinkedHashMap<String, Object>> request = new HttpEntity<>(requestBody, headers);

            ResponseEntity<String> responseEntity = restTemplate.exchange(uri.
                    concat("/saveAllResultFileByCommerce"), HttpMethod.POST, request, String.class);

            if (!Objects.requireNonNull(responseEntity.getBody()).contains("ERROR")) {
                logger.info("All resultFiles saved successfully");
                return true;
            } else {
                logger.error("ERROR: " + responseEntity.getBody());
                return false;
            }

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return false;
        }

    }

    private Path resolveFileNameConflict(Path targetLocation, Path parentDir) {
        int count = 1;
        String fileName = targetLocation.getFileName().toString();
        String baseName = fileName;
        String extension = "";

        int dotIndex = fileName.lastIndexOf('.');
        if (dotIndex != -1) {
            baseName = fileName.substring(0, dotIndex);
            extension = fileName.substring(dotIndex);
        }

        Set<String> existingBaseNames = new HashSet<>();
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(parentDir)) {
            for (Path entry : stream) {
                String existingFileName = entry.getFileName().toString();
                int existingDotIndex = existingFileName.lastIndexOf('.');
                String existingBaseName = existingFileName.substring(0, existingDotIndex != -1 ? existingDotIndex : existingFileName.length());
                existingBaseNames.add(existingBaseName);
            }
        } catch (IOException e) {
            logger.error("Error reading existing files in directory", e);
        }

        String originalBaseName = baseName;
        while (existingBaseNames.contains(baseName)) {
            baseName = originalBaseName + "(" + count + ")";
            String newFileName = baseName + extension;
            targetLocation = parentDir.resolve(newFileName);
            logger.info("Checking for file existence: {}", targetLocation.toString());
            count++;
        }

        logger.info("Nombre de archivo actualizado para evitar conflicto: {}", targetLocation.getFileName().toString());

        return targetLocation;
    }

    private PreRegisterTo getPreRegisterById(final Long idPreRegister, final String apiKey) {

        try {

            HttpHeaders headers = new HttpHeaders();
            RestTemplate restTemplate = new RestTemplate();

            headers.add("API_KEY", apiKey);

            String uri = environment.getProperty("api.route.external");

            HttpEntity<LinkedHashMap<String, Object>> request = new HttpEntity<>(headers);

            ResponseEntity<String> responseEntity = restTemplate.exchange(uri.
                    concat("/getPreRegisterById/"+idPreRegister), HttpMethod.GET, request, String.class);

            if (!Objects.requireNonNull(responseEntity.getBody()).contains("ERROR")) {

                ObjectMapper objectMapper = new ObjectMapper();

                LinkedHashMap<String, Object> responseMap = objectMapper.readValue(responseEntity.
                        getBody(), LinkedHashMap.class);

                logger.info("PreRegister found successfully");

                return new PreRegisterTo(objectMapper, responseMap);
            } else {
                logger.error("Response: " + responseEntity.getBody());
                return null;
            }

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return null;
        }

    }

    @Transactional
    public Boolean saveResultFile(final Long size, final Long idPreRegister,
                                  final Long idRequirement, final String fileName,
                                  final HttpServletRequest httpServletRequest) {

        try {

            HttpHeaders headers = new HttpHeaders();
            RestTemplate restTemplate = new RestTemplate();

            headers.add("API_KEY", httpServletRequest.getHeader("API_KEY"));

            String uri = "http://localhost:8091/horizonte";

            LinkedHashMap<String, Object> requestBody = new LinkedHashMap<>();

            requestBody.put("size", size);
            requestBody.put("chargedBy", getIdUserByToken(httpServletRequest));
            requestBody.put("idPreRegister", idPreRegister);
            requestBody.put("idRequirement", idRequirement);
            requestBody.put("fileName", fileName);

            HttpEntity<LinkedHashMap<String, Object>> request = new HttpEntity<>(requestBody, headers);

            ResponseEntity<String> responseEntity = restTemplate.exchange(uri.
                    concat("/saveResultFile"), HttpMethod.POST, request, String.class);

            if (!Objects.requireNonNull(responseEntity.getBody()).contains("ERROR")) {

                ObjectMapper objectMapper = new ObjectMapper();

                LinkedHashMap<String, Object> responseMap = objectMapper.readValue(responseEntity.
                        getBody(), LinkedHashMap.class);

                logger.info("ResultFile saved successfully");
                return (Boolean) responseMap.get("status");

            } else {
                logger.error("Response: " + responseEntity.getBody());
                return false;
            }

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return false;
        }

    }

    @Transactional
    public Boolean saveResultFileInt(final Long size, final Long idCommerce,
                                     final Long idRequirement, final String fileName,
                                     final HttpServletRequest httpServletRequest) {

        try {

            HttpHeaders headers = new HttpHeaders();
            RestTemplate restTemplate = new RestTemplate();

            headers.add("API_KEY", httpServletRequest.getHeader("API_KEY"));

            String uri = "http://localhost:8090/core";

            LinkedHashMap<String, Object> requestBody = new LinkedHashMap<>();

            requestBody.put("size", size);
            requestBody.put("chargedBy", getIdUserByToken(httpServletRequest));
            requestBody.put("idCommerce", idCommerce);
            requestBody.put("idRequirement", idRequirement);
            requestBody.put("fileName", fileName);

            HttpEntity<LinkedHashMap<String, Object>> request = new HttpEntity<>(requestBody, headers);

            ResponseEntity<String> responseEntity = restTemplate.exchange(uri.
                    concat("/saveResultFile"), HttpMethod.POST, request, String.class);

            if (!Objects.requireNonNull(responseEntity.getBody()).contains("ERROR")) {

                ObjectMapper objectMapper = new ObjectMapper();

                LinkedHashMap<String, Object> responseMap = objectMapper.readValue(responseEntity.
                        getBody(), LinkedHashMap.class);

                logger.info("ResultFile saved successfully");
                return (Boolean) responseMap.get("status");

            } else {
                logger.error("Response: " + responseEntity.getBody());
                return false;
            }

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return false;
        }

    }

    public Optional<ResultFileTo> getResultFileByIdPreRegisterAndIdRequirement(final Long idPreRegister,
                                                                               final Long idRequirement,
                                                                               final HttpServletRequest httpServletRequest,
                                                                               final LogTo logTo) {
        try {
            HttpHeaders headers = new HttpHeaders();
            RestTemplate restTemplate = new RestTemplate();

            headers.add("API_KEY", httpServletRequest.getHeader("API_KEY"));

            String uri = "http://localhost:8091/horizonte";

            LinkedHashMap<String, Object> requestBody = new LinkedHashMap<>();
            requestBody.put("idPreRegister", idPreRegister);
            requestBody.put("idRequirement", idRequirement);

            HttpEntity<LinkedHashMap<String, Object>> request = new HttpEntity<>(requestBody, headers);

            ResponseEntity<String> responseEntity = restTemplate.exchange(uri.concat("/getResultFileByIdPreRegisterAndIdRequirement"),
                    HttpMethod.POST, request, String.class);

            if (!Objects.requireNonNull(responseEntity.getBody()).contains("ERROR")) {
                ObjectMapper objectMapper = new ObjectMapper();
                LinkedHashMap<String, Object> responseMap = objectMapper.readValue(responseEntity.getBody(), LinkedHashMap.class);

                ResultFileTo resultFileTo = new ResultFileTo(responseMap.get("fileName").toString(), responseMap.get("rif").toString());

                return Optional.of(resultFileTo);
            } else {

                manageLog.errorLogger(logTo, httpServletRequest, "ERROR: " + responseEntity.
                        getBody());

                logger.error("ERROR: " + responseEntity.getBody());
                return Optional.empty();
            }

        } catch (Exception e) {
            manageLog.errorLogger(logTo, httpServletRequest, e.getMessage());
            logger.error(e.getMessage(), e);
            return Optional.empty();
        }
    }

    public Optional<ResultFileTo> getResultFileByTermsOrPolicies(final String termsOrPolices,
                                                                 final HttpServletRequest httpServletRequest,
                                                                 final LogTo logTo) {
        try {

            HttpHeaders headers = new HttpHeaders();
            RestTemplate restTemplate = new RestTemplate();

            headers.add("API_KEY", httpServletRequest.getHeader("API_KEY"));

            String uri = "http://localhost:8090/core";

            LinkedHashMap<String, Object> requestBody = new LinkedHashMap<>();
            requestBody.put("termsOrPolices", termsOrPolices);

            HttpEntity<LinkedHashMap<String, Object>> request = new HttpEntity<>(requestBody, headers);

            ResponseEntity<String> responseEntity = restTemplate.exchange(uri.
                            concat("/getResultFileByTermsOrPolicies"),
                    HttpMethod.POST, request, String.class);

            if (!Objects.requireNonNull(responseEntity.getBody()).contains("ERROR")) {
                ObjectMapper objectMapper = new ObjectMapper();
                LinkedHashMap<String, Object> responseMap = objectMapper.readValue(responseEntity.getBody(), LinkedHashMap.class);

                ResultFileTo resultFileTo = new ResultFileTo(responseMap.get("fileName").toString());

                return Optional.of(resultFileTo);

            } else {

                manageLog.errorLogger(logTo, httpServletRequest, "ERROR: " + responseEntity.
                        getBody());

                logger.error("ERROR: " + responseEntity.getBody());
                return Optional.empty();
            }

        } catch (Exception e) {
            manageLog.errorLogger(logTo, httpServletRequest, e.getMessage());
            logger.error(e.getMessage(), e);
            return Optional.empty();
        }
    }

    public Optional<ResultFileTo> getResultFileByIdCommerceAndIdRequirement(final String commerceDocument,
                                                                            Long idRequirement, final HttpServletRequest httpServletRequest) {
        try {

            CommerceTo commerceTo = commerceService.
                    getCommerceByDocumentAndStatus(commerceDocument, 2L, httpServletRequest.
                            getHeader("API_KEY"), httpServletRequest.getHeader("token"));

            if(idRequirement == 11L) {
                if(commerceTo.getIdTypeCommerce() == 1L) {
                    idRequirement = 11L;
                } else {
                    idRequirement = 10L;
                }
            }

            HttpHeaders headers = new HttpHeaders();
            RestTemplate restTemplate = new RestTemplate();

            headers.add("API_KEY", httpServletRequest.getHeader("API_KEY"));

            String uri = "http://localhost:8090/core";

            LinkedHashMap<String, Object> requestBody = new LinkedHashMap<>();
            requestBody.put("idCommerce", commerceTo.getIdCommerce());
            requestBody.put("idRequirement", idRequirement);

            HttpEntity<LinkedHashMap<String, Object>> request = new HttpEntity<>(requestBody, headers);

            ResponseEntity<String> responseEntity = restTemplate.exchange(uri.
                            concat("/getResultFileByIdCommerceAndIdRequirement"),
                    HttpMethod.POST, request, String.class);

            if (!Objects.requireNonNull(responseEntity.getBody()).contains("ERROR")) {
                ObjectMapper objectMapper = new ObjectMapper();
                LinkedHashMap<String, Object> responseMap = objectMapper.readValue(responseEntity.getBody(), LinkedHashMap.class);

                ResultFileTo resultFileTo = new ResultFileTo(responseMap.get("fileName").toString(), responseMap.get("rif").toString());

                return Optional.of(resultFileTo);
            } else {
                return Optional.empty();
            }

        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public Optional<ResultFileTo> getResultFileByIdCommerceAndIdRequirementQr(final String commerceDocument,
                                                                            final HttpServletRequest httpServletRequest) {
        try {

            CommerceTo commerceTo = commerceService.
                    getCommerceByDocumentAndStatus(commerceDocument, 2L, httpServletRequest.
                            getHeader("API_KEY"), httpServletRequest.getHeader("token"));

            Long idRequirement;

            if(commerceTo.getIdTypeCommerce() == 1L) {
                idRequirement = 11L;
            } else {
                idRequirement = 10L;
            }

            HttpHeaders headers = new HttpHeaders();
            RestTemplate restTemplate = new RestTemplate();

            headers.add("API_KEY", httpServletRequest.getHeader("API_KEY"));

            String uri = "http://localhost:8090/core";

            LinkedHashMap<String, Object> requestBody = new LinkedHashMap<>();
            requestBody.put("idCommerce", commerceTo.getIdCommerce());
            requestBody.put("idRequirement", idRequirement);

            HttpEntity<LinkedHashMap<String, Object>> request = new HttpEntity<>(requestBody, headers);

            ResponseEntity<String> responseEntity = restTemplate.exchange(uri.
                            concat("/getResultFileByIdCommerceAndIdRequirement"),
                    HttpMethod.POST, request, String.class);

            if (!Objects.requireNonNull(responseEntity.getBody()).contains("ERROR")) {
                ObjectMapper objectMapper = new ObjectMapper();
                LinkedHashMap<String, Object> responseMap = objectMapper.readValue(responseEntity.getBody(), LinkedHashMap.class);

                ResultFileTo resultFileTo = new ResultFileTo(responseMap.get("fileName").toString(), responseMap.get("rif").toString());

                return Optional.of(resultFileTo);
            } else {
                return Optional.empty();
            }

        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public String getReNameByIdRequirement(final Long idRequirement,
                                           final HttpServletRequest httpServletRequest) {
        try {
            HttpHeaders headers = new HttpHeaders();
            RestTemplate restTemplate = new RestTemplate();

            headers.add("API_KEY", httpServletRequest.getHeader("API_KEY"));

            String uri = "http://localhost:8091/horizonte";

            HttpEntity<LinkedHashMap<String, Object>> request = new HttpEntity<>(headers);

            ResponseEntity<String> responseEntity = restTemplate.exchange(uri.
                            concat("/getReNameByIdRequirement/"+idRequirement),
                    HttpMethod.GET, request, String.class);

            if (!Objects.requireNonNull(responseEntity.getBody()).contains("ERROR")) {
                ObjectMapper objectMapper = new ObjectMapper();
                LinkedHashMap<String, Object> responseMap = objectMapper.readValue(responseEntity.getBody(), LinkedHashMap.class);

                return responseMap.get("reName").toString();
            } else {
                logger.error("Not found" + responseEntity.getBody());
                return null;
            }

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return null;
        }
    }

    public Resource loadFileAsResource(String fileName, final String rif) throws Exception {
        Path filePath = Paths.get(fileStorageLocation.toString() + File.separator + rif).
                resolve(fileName).normalize();

        Resource resource = new UrlResource(filePath.toUri());

        if (resource.exists() && resource.isReadable()) {
            return resource;
        } else {
            throw new Exception("File not found or not readable: " + fileName);
        }
    }

    public Resource loadFileAsResourceTermsOrPolicies(String urlFile) throws Exception {
        Path filePath = Paths.get(urlFile).normalize();

        Resource resource = new UrlResource(filePath.toUri());

        if (resource.exists() && resource.isReadable()) {
            return resource;
        } else {
            return null;
        }
    }

    public Long getIdUserByToken(HttpServletRequest httpServletRequest){
        Claims claims = JwtUtils.extractClaims(httpServletRequest);
        return Objects.requireNonNull(claims).get("idUser", Long.class);
    }

}
