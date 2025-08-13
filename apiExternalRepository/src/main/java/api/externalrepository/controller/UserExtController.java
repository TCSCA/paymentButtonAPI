package api.externalrepository.controller;

import api.externalrepository.service.UserService;
import api.externalrepository.to.UserStatusTo;
import api.externalrepository.to.UsersBlockedTo;
import api.externalrepository.util.Response;
import api.logsClass.LogsClass;
import api.logsClass.ManageLogs;
import jakarta.servlet.http.HttpServletRequest;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;

@RestController
@RequestMapping(value = "/horizonte")
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST})
public class UserExtController {

    private final UserService userService;

    private final ManageLogs manageLogs;

    private static final Logger logger = LoggerFactory.getLogger(UserExtController.class);

    public UserExtController(UserService userService,ManageLogs manageLogs) {
        this.userService = userService;
        this.manageLogs = manageLogs;
    }

    @PostMapping("/createUserAdminExt")
    public LinkedHashMap<String, Object> createUserAdminExt(@RequestBody final String dataForm, final HttpServletRequest httpServletRequest) {
        LogsClass logTo = new LogsClass(httpServletRequest, new Object() {}.
                getClass().getEnclosingMethod().getName());
        LinkedHashMap<String, Object> response = new LinkedHashMap<>();

        try {

            String commerceDocument = (new JSONObject(dataForm).get("commerceDocument")).toString();
            String password = (new JSONObject(dataForm).get("password")).toString();
            Long idUser = Long.parseLong((new JSONObject(dataForm).get("idUser")).toString());
            String email = (new JSONObject(dataForm).get("email")).toString();

            Boolean result = userService.createUserAdminExt(commerceDocument, password, idUser,email);
            response.put("SUCCESS", result);
            manageLogs.infoLogger(logTo,httpServletRequest,
                    "Successfull: " + logTo.getMethodName(),
                    "user",true);

        } catch (Exception e){
            response.put("ERROR", false);
            response.put("message", "An error occurred: " + e.getMessage());
            manageLogs.severeErrorLogger(logTo,httpServletRequest,"Error: "+ logTo.getMethodName() + ": "+ e,
                    e,"user",true);
        }

        return response;
    }

    @PostMapping("/searchConfigurationById")
    public LinkedHashMap<String, Object> searchConfigurationById(@RequestBody final String dataForm, final HttpServletRequest httpServletRequest) {
        LogsClass logTo = new LogsClass(httpServletRequest, new Object() {}.
                getClass().getEnclosingMethod().getName());
        LinkedHashMap<String, Object> response = new LinkedHashMap<>();

        try {

            Long idConfiguration = Long.parseLong(new JSONObject(dataForm).get("idConfiguration").toString());

            String result = userService.searchConfigurationById(idConfiguration);
            response.put("configurationValue", result);
            manageLogs.infoLogger(logTo,httpServletRequest,
                    "Successfull: " + logTo.getMethodName(),
                    "user",false);

        } catch (Exception e){
            response.put("idConfig", null);
            response.put("message", "An error occurred: " + e.getMessage());
            manageLogs.severeErrorLogger(logTo,httpServletRequest,"Error: "+ logTo.getMethodName() + ": "+ e,
                    e,"user",false);
        }

        return response;

    }

    @PostMapping("/createUserExtWithProfile")
    public LinkedHashMap<String, Object> createUserExtWithProfile(@RequestBody final String dataForm, final HttpServletRequest httpServletRequest) {
        LogsClass logTo = new LogsClass(httpServletRequest, new Object() {}.
                getClass().getEnclosingMethod().getName());
        LinkedHashMap<String, Object> response = new LinkedHashMap<>();

        try {

            String commerceDocument = (new JSONObject(dataForm).get("commerceDocument")).toString();
            String password = (new JSONObject(dataForm).get("password")).toString();
            Long idUser = Long.parseLong((new JSONObject(dataForm).get("idUser")).toString());
            Long idProfile = Long.parseLong((new JSONObject(dataForm).get("idProfile")).toString());
            String email = (new JSONObject(dataForm).get("email")).toString();

            Boolean result = userService.createUserExtWithProfile(commerceDocument, password, idUser,idProfile,email);
            response.put("SUCCESS", result);
            manageLogs.infoLogger(logTo,httpServletRequest,
                    "Successfully: " + logTo.getMethodName(),
                    "user",true);

        } catch (Exception e){
            response.put("ERROR", false);
            response.put("message", "An error occurred: " + e.getMessage());
            manageLogs.severeErrorLogger(logTo,httpServletRequest,"Error: "+ logTo.getMethodName() + ": "+ e,
                    e,"user",true);
        }

        return response;
    }


    @PostMapping("/getAllUsersBlocked")
    public LinkedHashMap<String, Object> getAllUsersBlocked(final HttpServletRequest httpServletRequest,@RequestBody final String dataForm) {
        LogsClass logTo = new LogsClass(httpServletRequest, new Object() {}.
                getClass().getEnclosingMethod().getName());
        LinkedHashMap<String, Object> response = new LinkedHashMap<>();
        int index = Integer.parseInt((new JSONObject(dataForm).get("page")).toString());
        int quantity = Integer.parseInt((new JSONObject(dataForm).get("quantity")).toString());
        Pageable pageable = PageRequest.of(index, quantity);

        try {

            Page<UsersBlockedTo> result = userService.usersBlocked(httpServletRequest,pageable);
            response.put("userBlockedExtList", result);
            manageLogs.infoLogger(logTo,httpServletRequest,
                    "Successfully: " + logTo.getMethodName(),
                    "user",true);

        } catch (Exception e){
            response.put("idConfig", null);
            response.put("message", "An error occurred: " + e.getMessage());
            manageLogs.severeErrorLogger(logTo,httpServletRequest,"Error: "+ logTo.getMethodName() + ": "+ e,
                    e,"user",true);
        }

        return response;

    }

    @PostMapping("/unlocksUsersExternal")
    public LinkedHashMap<String, Object> unlocksUsersExternal(final HttpServletRequest httpServletRequest,@RequestBody final String dataForm) {
        LogsClass logTo = new LogsClass(httpServletRequest, new Object() {}.
                getClass().getEnclosingMethod().getName());
        LinkedHashMap<String, Object> response = new LinkedHashMap<>();
        long idUser = Long.parseLong((new JSONObject(dataForm).get("idUser")).toString());

        try {

            Response result = userService.unlockUser(httpServletRequest,idUser);

            logger.info("Information saved successfully");
            manageLogs.infoLogger(logTo,httpServletRequest,
                    "Successfully: " + logTo.getMethodName(),
                    "user",true);

        } catch (Exception e){
            response.put("idConfig", null);
            response.put("message", "An error occurred: " + e.getMessage());
            manageLogs.severeErrorLogger(logTo,httpServletRequest,"Error: "+ logTo.getMethodName() + ": "+ e,
                    e,"user",true);
        }

        return response;

    }


    @PostMapping("/getUserEntityByUsername")
    public LinkedHashMap<String, Object> getUserEntytyByUsername(final HttpServletRequest httpServletRequest,@RequestBody final String dataForm) {
        LogsClass logTo = new LogsClass(httpServletRequest, new Object() {}.
                getClass().getEnclosingMethod().getName());
        LinkedHashMap<String, Object> response = new LinkedHashMap<>();
        String username = (new JSONObject(dataForm).get("username")).toString();
        String resetPassword = (new JSONObject(dataForm).get("resetPassword")).toString();

        try {

            Response result = userService.findByUsername(httpServletRequest,username,resetPassword);
            String hostEmail = userService.searchConfigurationByKey("emailServicesGoogle");

            response.put("userEntityExt", result);
            response.put("hostEmail", hostEmail);
            logger.info("Information saved successfully");
            manageLogs.infoLogger(logTo,httpServletRequest,
                    "Successfully: " + logTo.getMethodName(),
                    "user",false);

        } catch (Exception e){
            response.put("idConfig", null);
            response.put("message", "An error occurred: " + e.getMessage());
            manageLogs.severeErrorLogger(logTo,httpServletRequest,"Error: "+ logTo.getMethodName() + ": "+ e,
                    e,"user",false);
        }

        return response;

    }

    @PostMapping("/getUserEntityByIdUser")
    public LinkedHashMap<String, Object> getUserEntityByIdUser(final HttpServletRequest httpServletRequest,@RequestBody final String dataForm) {
        LogsClass logTo = new LogsClass(httpServletRequest, new Object() {}.
                getClass().getEnclosingMethod().getName());
        LinkedHashMap<String, Object> response = new LinkedHashMap<>();
        String idUser = (new JSONObject(dataForm).get("idUser")).toString();


        try {

            Response result = userService.findByIdUser(httpServletRequest, Long.valueOf(idUser));
            response.put("userEntityExt", result);
            logger.info("Information saved successfully");
            manageLogs.infoLogger(logTo,httpServletRequest,
                    "Successfully: " + logTo.getMethodName(),
                    "user",false);

        } catch (Exception e){
            response.put("idConfig", null);
            response.put("message", "An error occurred: " + e.getMessage());
            manageLogs.severeErrorLogger(logTo,httpServletRequest,"Error: "+ logTo.getMethodName() + ": "+ e,
                    e,"user",false);
        }

        return response;

    }



    @PostMapping("/blockUserExt")
    public LinkedHashMap<String, Object> blockUserExt(@RequestBody final String dataForm,HttpServletRequest httpServletRequest) {

        LogsClass logTo = new LogsClass(httpServletRequest, new Object() {}.
                getClass().getEnclosingMethod().getName());

        LinkedHashMap<String, Object> response = new LinkedHashMap<>();
        String username = (new JSONObject(dataForm).get("username")).toString();


        try {

            Response result = userService.blockUser(username);
            manageLogs.infoLogger(logTo,httpServletRequest,
                    "Successfully: " + logTo.getMethodName(),
                    "user",false);

            response.put("status", "SUCCESS");



        } catch (Exception e){
            response.put("ERROR", null);
            logger.error(e.getMessage(), e);
            response.put("message", "An error occurred: " + e.getMessage());

            manageLogs.severeErrorLogger(logTo,httpServletRequest,
                    "Error: "+ logTo.getMethodName() + ": "+ e,
                    e,"user",false);
        }

        return response;
    }

    @PostMapping("/updateAllUserStatusByUsername")
    public LinkedHashMap<String, Object> updateAllUserStatusByUsername(@RequestBody final UserStatusTo dataForm,
                                                                       HttpServletRequest httpServletRequest) {

        LogsClass logTo = new LogsClass(httpServletRequest, new Object() {}.
                getClass().getEnclosingMethod().getName());

        LinkedHashMap<String, Object> response = new LinkedHashMap<>();

        try {

            Boolean result = userService.
                    updateAllUserStatusByUsername(dataForm.getIdStatus(), dataForm.getUsernameList());

            manageLogs.infoLogger(logTo,httpServletRequest,
                    "Successfully: " + logTo.getMethodName(),
                    "user",false);

            response.put("status", result);

        } catch (Exception e){
            response.put("status", "ERROR");
            logger.error(e.getMessage(), e);
            response.put("message", "An error occurred: " + e.getMessage());

            manageLogs.severeErrorLogger(logTo,httpServletRequest,
                    "Error: "+ logTo.getMethodName() + ": "+ e,
                    e,"user",false);
        }

        return response;
    }


}
