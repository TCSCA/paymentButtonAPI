package api.authentication.controller;

import api.authentication.entity.UserEntity;
import api.authentication.service.AuthService;
import api.authentication.service.MenuService;
import api.authentication.util.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/api")
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST})
public class AuthController {

    private final AuthService authService;

    private final ManageLog manageLog;

    private final MenuService menuService;

    public AuthController(AuthService authService, ManageLog manageLog, MenuService menuService) {
        this.authService = authService;
        this.manageLog = manageLog;
        this.menuService = menuService;
    }

    @PostMapping("/authUser")
    public String authUser(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {

        LogTo logTo = new LogTo(httpServletRequest,"authUser");
        GsonBuilder b = new GsonBuilder();
        b.registerTypeAdapterFactory(HibernateProxyTypeAdapter.FACTORY);
        b.registerTypeAdapter(GregorianCalendar.class, new CalendarSerializer());
        b.registerTypeAdapter(OffsetDateTime.class, new OffsetDateTimeSerializer());
        Gson gson = b.setPrettyPrinting().create();

        try {
            String tokenValidated = authService.validateAuthToken(httpServletRequest);
            Long idUser = menuService.getIdUserByToken(httpServletRequest);
            UserEntity userEntity = authService.getInternalUserEntity(idUser, httpServletRequest);
            String rif = authService.getRifByIdUser(userEntity.getIdUser());
            if (rif == null && tokenValidated != null){
                httpServletResponse.setStatus(HttpServletResponse.SC_OK);
                manageLog.infoLogger(logTo,httpServletRequest, String.valueOf(HttpServletResponse.SC_OK),true);
                Map<String, String> responseData = new HashMap<>();
                responseData.put("token", tokenValidated);
                responseData.put("idUser", userEntity.getIdUser().toString());
                return gson.toJson(new Response("SUCCESS", responseData));
            }
            if( tokenValidated != null) {
                httpServletResponse.setStatus(HttpServletResponse.SC_OK);
                manageLog.infoLogger(logTo,httpServletRequest, String.valueOf(HttpServletResponse.SC_OK),true);
                Map<String, String> responseData = new HashMap<>();
                responseData.put("token", tokenValidated);
                responseData.put("rif", rif);
                responseData.put("idUser", userEntity.getIdUser().toString());
                return gson.toJson(new Response("SUCCESS", responseData));
            } else {
                manageLog.infoLogger(logTo,httpServletRequest, "ERROR",true);
                return gson.toJson(new Response("ERROR", "Invalid token"));
            }
        } catch (Exception e) {
            manageLog.severeErrorLogger(logTo,httpServletRequest,e.getMessage(),e,true);
            return gson.toJson(new Response("Error", e));
        }
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest httpServletRequest,
                         HttpServletResponse httpServletResponse) {

        LogTo logTo = new LogTo(httpServletRequest,"logout");
        GsonBuilder b = new GsonBuilder();
        b.registerTypeAdapterFactory(HibernateProxyTypeAdapter.FACTORY);
        b.registerTypeAdapter(GregorianCalendar.class, new CalendarSerializer());
        b.registerTypeAdapter(OffsetDateTime.class, new OffsetDateTimeSerializer());
        Gson gson = b.setPrettyPrinting().create();

        try {

            Long idUser = AuthService.getIdUserByToken(httpServletRequest);
            authService.invalidateLastTokenByUser(idUser);

            manageLog.infoLogger(logTo,httpServletRequest, String.valueOf(HttpServletResponse.SC_OK),true);
            return gson.toJson(new Response("SUCCESS", "User successfully logged out"));

        } catch (Exception e){
            httpServletResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            manageLog.severeErrorLogger(logTo,httpServletRequest,e.getMessage(),e,true);
            return gson.toJson(new Response("Error", e));
        }

    }

    @PostMapping("/validateAuthToken")
    public String validateAuthToken(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {

        LogTo logTo = new LogTo(httpServletRequest,"validateAuthToken");
        GsonBuilder b = new GsonBuilder();
        b.registerTypeAdapterFactory(HibernateProxyTypeAdapter.FACTORY);
        b.registerTypeAdapter(GregorianCalendar.class, new CalendarSerializer());
        b.registerTypeAdapter(OffsetDateTime.class, new OffsetDateTimeSerializer());
        Gson gson = b.setPrettyPrinting().create();

        try {
            if(authService.checkTokenValid(httpServletRequest)) {
                return gson.toJson(new Response("SUCCESS", "Token válido"));
            }
                return gson.toJson(new Response("ERROR", "Token inválido"));

        } catch (Exception e) {
            manageLog.severeErrorLogger(logTo,httpServletRequest,e.getMessage(),e,true);
            return gson.toJson(new Response("Error", e));
        }
    }
}
