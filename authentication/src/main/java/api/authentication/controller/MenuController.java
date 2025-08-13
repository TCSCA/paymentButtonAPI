package api.authentication.controller;

import api.authentication.service.MenuService;
import api.authentication.service.TransaccionService;
import api.authentication.to.ApprovalUserTo;
import api.authentication.util.CalendarSerializer;
import api.authentication.util.HibernateProxyTypeAdapter;
import api.authentication.util.LogTo;
import api.authentication.util.ManageLog;
import api.authentication.util.OffsetDateTimeSerializer;
import api.authentication.util.Response;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.pdfbox.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.nio.file.Files;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.GregorianCalendar;
import java.util.List;

@RestController
@RequestMapping(value = "/api")
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST})
public class MenuController {

    private static final Logger logger = LoggerFactory.getLogger(MenuController.class);

    private final MenuService menuService;

    private final TransaccionService transaccionService;

    private final ManageLog manageLog;

    public MenuController(MenuService menuService, TransaccionService transaccionService, ManageLog manageLog) {
        this.menuService = menuService;
        this.transaccionService = transaccionService;
        this.manageLog = manageLog;
    }

    @GetMapping("/getMenuByProfile/{idProfile}")
    public String getMenuByProfile(@PathVariable Long idProfile, HttpServletRequest httpServletRequest) {

        LogTo logTo = new LogTo(httpServletRequest,"getMenuByProfile");
        GsonBuilder b = new GsonBuilder();
        b.registerTypeAdapterFactory(HibernateProxyTypeAdapter.FACTORY);
        b.registerTypeAdapter(GregorianCalendar.class, new CalendarSerializer());
        b.registerTypeAdapter(OffsetDateTime.class, new OffsetDateTimeSerializer());
        Gson gson = b.setPrettyPrinting().create();

        Response response;
        String responseJson;

        try {

            response = menuService.getMenuByIdProfile(idProfile);

            Long idUser = menuService.getIdUserByToken(httpServletRequest);

            List<ApprovalUserTo> approvalUserToList = menuService.getApprovalStatusByUser(idUser,
                    httpServletRequest.getHeader("API_KEY"));

            if (approvalUserToList != null){

                for (ApprovalUserTo a : approvalUserToList) {

                    if (a.getUrl() != null) {

                        if (a.getName().contains("Terminos")){

                            Resource resource = menuService.loadFileAsResource(a.getUrl());

                            if (resource != null) {
                                byte[] fileContent = IOUtils.toByteArray(resource.getInputStream());
                                String mimeType = Files.probeContentType(resource.getFile().toPath());

                                String base64Prefix = "data:" + mimeType + ";base64,";
                                String base64Content = Base64.getEncoder().encodeToString(fileContent);

                                a.setFile(base64Prefix + base64Content);

                                logger.info("Terminos y condiciones sin aceptar");

                                response.setTermsAndConditions(a);
                            }

                        }

                        if (a.getName().contains("Politicas")){

                            Resource resource = menuService.loadFileAsResource(a.getUrl());

                            if (resource != null) {
                                byte[] fileContent = IOUtils.toByteArray(resource.getInputStream());
                                String mimeType = Files.probeContentType(resource.getFile().toPath());

                                String base64Prefix = "data:" + mimeType + ";base64,";
                                String base64Content = Base64.getEncoder().encodeToString(fileContent);

                                a.setFile(base64Prefix + base64Content);

                                logger.info("Politicas de privacidad sin aceptar");

                                response.setPrivacyPolicy(a);
                            }

                        }

                    }

                }

            }

            responseJson = gson.toJson(response);

            manageLog.infoLogger(logTo,httpServletRequest, String.valueOf(HttpServletResponse.SC_OK),true);
            return responseJson;

        } catch (Exception e){
            manageLog.severeErrorLogger(logTo,httpServletRequest,e.getMessage(),e,true);
            return gson.toJson(new Response("ERROR", e));
        }

    }

    @GetMapping("/getTermsAndConditions")
    public String getTermsAndConditions(final HttpServletRequest httpServletRequest) {

        LogTo logTo = new LogTo(httpServletRequest,"getTermsAndConditions");
        GsonBuilder b = new GsonBuilder();
        b.registerTypeAdapterFactory(HibernateProxyTypeAdapter.FACTORY);
        b.registerTypeAdapter(GregorianCalendar.class, new CalendarSerializer());
        b.registerTypeAdapter(OffsetDateTime.class, new OffsetDateTimeSerializer());
        Gson gson = b.setPrettyPrinting().create();

        Response response;
        String responseJson;

        try {

            response = menuService.getTermsAndConditions(httpServletRequest);

            responseJson = gson.toJson(response);

            manageLog.infoLogger(logTo,httpServletRequest, String.valueOf(HttpServletResponse.SC_OK),true);
            return responseJson;

        } catch (Exception e) {
            manageLog.severeErrorLogger(logTo,httpServletRequest,e.getMessage(),e,true);
            return gson.toJson(new Response("ERROR", e));
        }

    }

    @PostMapping("/setApprovalUserStatus")
    public String setApprovalUserStatus(@RequestBody final String data,
                                        HttpServletRequest httpServletRequest) {

        LogTo logTo = new LogTo(httpServletRequest,"getTermsAndConditions");
        GsonBuilder b = new GsonBuilder();
        b.registerTypeAdapterFactory(HibernateProxyTypeAdapter.FACTORY);
        b.registerTypeAdapter(GregorianCalendar.class, new CalendarSerializer());
        b.registerTypeAdapter(OffsetDateTime.class, new OffsetDateTimeSerializer());
        Gson gson = b.setPrettyPrinting().create();

        Response response;
        String responseJson;

        try {

            JSONObject jsonObject = new JSONObject(data);
            Long idUser = jsonObject.getLong("idUser");
            JSONArray termsOrPoliciesArray = jsonObject.getJSONArray("termsOrPolicies");

            List<String> termsOrPolicies = new ArrayList<>();
            for (int i = 0; i < termsOrPoliciesArray.length(); i++) {
                termsOrPolicies.add(termsOrPoliciesArray.getString(i));
            }

            response = menuService.setApprovalUserStatus(idUser, termsOrPolicies, httpServletRequest);

            responseJson = gson.toJson(response);

            manageLog.infoLogger(logTo,httpServletRequest, String.valueOf(HttpServletResponse.SC_OK),true);
            return responseJson;

        } catch (Exception e) {
            manageLog.severeErrorLogger(logTo,httpServletRequest,e.getMessage(),e,true);
            return gson.toJson(new Response("ERROR", e));
        }

    }

    @PostMapping("/getTransactionByUserAndSection")
    public String getTransactionByUserAndSection(@RequestBody final String data,
                                                    HttpServletRequest httpServletRequest,
                                                    HttpServletResponse httpServletResponse) {

        LogTo logTo = new LogTo(httpServletRequest,"getTransactionByUserAndSection");
        GsonBuilder b = new GsonBuilder();
        b.registerTypeAdapterFactory(HibernateProxyTypeAdapter.FACTORY);
        b.registerTypeAdapter(GregorianCalendar.class, new CalendarSerializer());
        b.registerTypeAdapter(OffsetDateTime.class, new OffsetDateTimeSerializer());
        Gson gson = b.setPrettyPrinting().create();

        Response response;
        String responseJson;

        try {

            Long idUser = Long.parseLong((new JSONObject(data).get("idUser")).toString());
            Long idSeccion = Long.parseLong((new JSONObject(data).get("idSeccion")).toString());

            response = transaccionService.getTransaccionByProfileAndSeccion(idUser, idSeccion);
            responseJson = gson.toJson(response);

            manageLog.infoLogger(logTo,httpServletRequest, String.valueOf(HttpServletResponse.SC_OK),true);
            return responseJson;


        } catch (Exception e){
            httpServletResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            manageLog.severeErrorLogger(logTo,httpServletRequest,e.getMessage(),e,true);
            return gson.toJson(new Response("ERROR", e));
        }

    }

}
