package api.apiPaymentButton.controller;

import api.apiPaymentButton.request.GatewayRequest;
import api.apiPaymentButton.service.GatewayService;
import api.apiPaymentButton.service.UrlService;
import api.apiPaymentButton.util.CalendarSerializer;
import api.apiPaymentButton.util.HibernateProxyTypeAdapter;
import api.apiPaymentButton.util.LogTo;
import api.apiPaymentButton.util.ManageLog;
import api.apiPaymentButton.util.OffsetDateTimeSerializer;
import api.apiPaymentButton.util.Response;
import api.apiPaymentButton.util.ValidationUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import feign.FeignException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.GregorianCalendar;
import java.util.LinkedHashMap;

@RestController
@RequestMapping(value = "/api")
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST})
public class FileUploadController {

    private static final Logger logger = LoggerFactory.getLogger(FileUploadController.class);

    private final GatewayRequest gatewayRequest;
    private final UrlService urlService;
    private final ValidationUtil validationUtil;
    private final ManageLog manageLog;
    private final GatewayService gatewayService;

    public FileUploadController(GatewayRequest gatewayRequest, UrlService urlService, ValidationUtil validationUtil, ManageLog manageLog, GatewayService gatewayService) {
        this.gatewayRequest = gatewayRequest;
        this.urlService = urlService;
        this.validationUtil = validationUtil;
        this.manageLog = manageLog;
        this.gatewayService = gatewayService;
    }

    @PostMapping("/uploadFiles")
    public String uploadFile(@RequestParam("file") MultipartFile file,
                             @RequestParam("idPreRegister") Long idPreRegister,
                             @RequestParam("idRequirement") Long idRequirement,
                             HttpServletRequest httpServletRequest,
                             HttpServletResponse httpServletResponse) {

        LogTo logTo = new LogTo(httpServletRequest, "uploadFile");
        GsonBuilder b = new GsonBuilder();
        b.registerTypeAdapterFactory(HibernateProxyTypeAdapter.FACTORY);
        b.registerTypeAdapter(GregorianCalendar.class, new CalendarSerializer());
        b.registerTypeAdapter(OffsetDateTime.class, new OffsetDateTimeSerializer());
        Gson gson = b.setPrettyPrinting().create();

        Response response;
        String apikey = httpServletRequest.getHeader(validationUtil.apiKeyHeader);
        String token = httpServletRequest.getHeader("token");

        if (apikey == null) {
            httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return gson.toJson(new Response("ERROR", "UNAUTHORIZED"));
        }

        try {
            URI basePathUri = urlService.getUrlFromFile("uploadFile", manageLog);
            if (basePathUri == null) {
                httpServletResponse.setStatus(HttpServletResponse.SC_BAD_GATEWAY);
                return gson.toJson(new Response("ERROR", "Invalid Method"));
            }

            LinkedHashMap<String, String> linkedHashMap = gatewayRequest.dynamicEndpointRequestUploadFile(file, apikey, token, idPreRegister, idRequirement, basePathUri);
            String responseJson = gson.toJson(linkedHashMap);
            manageLog.infoLogger(logTo, httpServletRequest, responseJson, false);
            return responseJson;

        } catch (FeignException.FeignClientException | FileNotFoundException e) {
            logger.error(e.getMessage(), e);
            httpServletResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response = gatewayService.getResponseGatewayByHttpStatus(e, "uploadFile", httpServletResponse);
        }

        return gson.toJson(response);
    }

}
