package api.internalrepository.controller;

import api.internalrepository.entity.CommerceEntity;
import api.internalrepository.entity.ProfileEntity;
import api.internalrepository.entity.TermsAndConditionsEntity;
import api.internalrepository.entity.UserEntity;
import api.internalrepository.repository.ProfileRepository;
import api.internalrepository.service.ApprovalUserService;
import api.internalrepository.service.TermsAndConditionsService;
import api.internalrepository.service.UserService;
import api.internalrepository.to.ApprovalUserTo;
import api.internalrepository.to.UsersByCommerceTo;
import api.internalrepository.util.Response;
import api.logsClass.LogsClass;
import api.logsClass.ManageLogs;
import jakarta.servlet.http.HttpServletRequest;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

@RestController
@RequestMapping(value = "/core")
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST})
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    private final ManageLogs manageLogs;

    private final ProfileRepository profileRepository;

    private final TermsAndConditionsService termsAndConditionsService;

    private final ApprovalUserService approvalUserService;

    public UserController(UserService userService, ManageLogs manageLogs, ProfileRepository profileRepository, TermsAndConditionsService termsAndConditionsService, ApprovalUserService approvalUserService) {
        this.userService = userService;
        this.manageLogs = manageLogs;
        this.profileRepository = profileRepository;
        this.termsAndConditionsService = termsAndConditionsService;
        this.approvalUserService = approvalUserService;
    }

    @PostMapping("/createUserAdmin")
    public LinkedHashMap<String, Object> createUserAdmin(@RequestBody final String dataForm,
                                                         final HttpServletRequest httpServletRequest) {

        LogsClass logTo = new LogsClass(httpServletRequest, new Object() {}.
                getClass().getEnclosingMethod().getName());

        LinkedHashMap<String, Object> response = new LinkedHashMap<>();

        try {

            String commerceDocument = (new JSONObject(dataForm).get("commerceDocument")).toString();
            String password = (new JSONObject(dataForm).get("password")).toString();

            Long result = userService.createUserAdmin(commerceDocument, password);
            response.put("idUser", result);
            logger.info("User saved successfully");

            manageLogs.infoLogger(logTo,httpServletRequest,
                    "Successfully: " + logTo.getMethodName(),
                    "user",true);

        } catch (Exception e){
            response.put("ERROR", null);
            logger.error(e.getMessage(), e);
            response.put("message", "An error occurred: " + e.getMessage());

            manageLogs.severeErrorLogger(logTo,httpServletRequest,
                    "Error: "+ logTo.getMethodName() + ": "+ e,
                    e,"user",true);
        }

        return response;
    }

    @PostMapping("/getUserByIdUser")
    public LinkedHashMap<String, Object> getUserByIdUser(@RequestBody final String dataForm,
                                                         final HttpServletRequest httpServletRequest) {

        LogsClass logTo = new LogsClass(httpServletRequest, new Object() {}.
                getClass().getEnclosingMethod().getName());

        LinkedHashMap<String, Object> response = new LinkedHashMap<>();

        try {

            Long idUser = Long.parseLong((new JSONObject(dataForm)
                    .get("idUser")).toString());

            UserEntity userEntity = userService.findByIdUser(idUser);

            response.put("userEntity", userEntity);

            logger.info("User found successfully");

            manageLogs.infoLogger(logTo,httpServletRequest,
                    "Successfully: " + logTo.getMethodName(),
                    "user",true);

        } catch (Exception e) {
            response.put("ERROR", null);
            logger.error(e.getMessage(), e);
            response.put("message", "An error occurred: " + e.getMessage());

            manageLogs.severeErrorLogger(logTo,httpServletRequest,
                    "Error: "+ logTo.getMethodName() + ": "+ e,
                    e,"user",true);
        }

        return response;
    }

    @PostMapping("/getAllProfile")
    public LinkedHashMap<String, Object> getAllProfile(@RequestBody final String dataForm,
                                                         final HttpServletRequest httpServletRequest) {

        LogsClass logTo = new LogsClass(httpServletRequest, new Object() {}.
                getClass().getEnclosingMethod().getName());

        LinkedHashMap<String, Object> response = new LinkedHashMap<>();

        try {

            Long idProfile = Long.parseLong((new JSONObject(dataForm)
                    .get("idProfile")).toString());

            if(idProfile == 1){
                List<ProfileEntity> profileEntities = profileRepository.findProfilesForCommerce();
                response.put("profileEntities", profileEntities);
            } else if (idProfile == 2) {
                List<ProfileEntity> profileEntities = profileRepository.findProfilesForIntelipay();
                response.put("profileEntities", profileEntities);
            }


            logger.info("Profile list get successfully");

            manageLogs.infoLogger(logTo,httpServletRequest,
                    "Successfully: " + logTo.getMethodName(),
                    "user",true);

        } catch (Exception e) {
            response.put("ERROR", null);
            logger.error(e.getMessage(), e);
            response.put("message", "An error occurred: " + e.getMessage());

            manageLogs.severeErrorLogger(logTo,httpServletRequest,
                    "Error: "+ logTo.getMethodName() + ": "+ e,
                    e,"user",true);
        }

        return response;
    }


    @PostMapping("/createUserInt")
    public LinkedHashMap<String, Object> createUserInt(@RequestBody final String dataForm,
                                                         final HttpServletRequest httpServletRequest) {

        LogsClass logTo = new LogsClass(httpServletRequest, new Object() {}.
                getClass().getEnclosingMethod().getName());

        LinkedHashMap<String, Object> response = new LinkedHashMap<>();

        try {

            String commerceDocument = (new JSONObject(dataForm).get("commerceDocument")).toString();
            String password = (new JSONObject(dataForm).get("password")).toString();
            Long idProfile = Long.parseLong((new JSONObject(dataForm)
                    .get("idProfile")).toString());

            Long result = userService.createUserInt(commerceDocument, password, idProfile);
            response.put("idUser", result);
            logger.info("User saved successfully");

            manageLogs.infoLogger(logTo,httpServletRequest,
                    "Successfully: " + logTo.getMethodName(),
                    "user",true);

        } catch (Exception e){
            response.put("ERROR", null);
            logger.error(e.getMessage(), e);
            response.put("message", "An error occurred: " + e.getMessage());

            manageLogs.severeErrorLogger(logTo,httpServletRequest,
                    "Error: "+ logTo.getMethodName() + ": "+ e,
                    e,"user",true);
        }

        return response;
    }

    @PostMapping("/editProfile")
    public LinkedHashMap<String, Object> editProfile(@RequestBody final String dataForm,
                                                       final HttpServletRequest httpServletRequest) {

        LogsClass logTo = new LogsClass(httpServletRequest, new Object() {}.
                getClass().getEnclosingMethod().getName());

        LinkedHashMap<String, Object> response = new LinkedHashMap<>();

        try {

            Long idUser = Long.parseLong((new JSONObject(dataForm).get("idUser")).toString());
            String email = (new JSONObject(dataForm).get("email")).toString();
            String phoneNumber = (new JSONObject(dataForm).get("phoneNumber")).toString();
            String fullName = (new JSONObject(dataForm).get("fullName")).toString();

            UserEntity userEntity = userService.findByIdUser(idUser);

            if (userEntity.getProfileEntity().getIdProfile() == 2 || userEntity.getProfileEntity().getIdProfile() == 3 ||
                    userEntity.getProfileEntity().getIdProfile() == 4){
                Response result = userService.editProfileAdmin(idUser,fullName, phoneNumber, email);
            }else{
                Response result = userService.editProfile(idUser,fullName, phoneNumber, email);
            }

            logger.info("Information saved successfully");

            manageLogs.infoLogger(logTo,httpServletRequest,
                    "Successfully: " + logTo.getMethodName(),
                    "user",true);

        } catch (Exception e){
            response.put("ERROR", null);
            logger.error(e.getMessage(), e);
            response.put("message", "An error occurred: " + e.getMessage());

            manageLogs.severeErrorLogger(logTo,httpServletRequest,
                    "Error: "+ logTo.getMethodName() + ": "+ e,
                    e,"user",true);
        }

        return response;
    }


    @PostMapping("/getAllUsersByCommerce")
    public LinkedHashMap<String, Object> getUsersByCommerce(@RequestBody final String dataForm,HttpServletRequest httpServletRequest) {

        LogsClass logTo = new LogsClass(httpServletRequest, new Object() {}.
                getClass().getEnclosingMethod().getName());

        LinkedHashMap<String, Object> response = new LinkedHashMap<>();
        String rif = (new JSONObject(dataForm).get("rif")).toString();
        int index = Integer.parseInt((new JSONObject(dataForm).get("page")).toString());
        int quantity = Integer.parseInt((new JSONObject(dataForm).get("quantity")).toString());
        Pageable pageable = PageRequest.of(index, quantity);

        try {

            CommerceEntity commerceEntity = userService.getCommerceEntityByDocument(rif);
            Page<UsersByCommerceTo> result = userService.getAllUsersByCommerce(commerceEntity.getIdCommerce(),pageable);
            response.put("usersByCommerce", result);



        } catch (Exception e){
            response.put("ERROR", null);
            logger.error(e.getMessage(), e);
            response.put("message", "An error occurred: " + e.getMessage());

            manageLogs.severeErrorLogger(logTo,httpServletRequest,
                    "Error: "+ logTo.getMethodName() + ": "+ e,
                    e,"user",true);
        }

        return response;
    }

    @PostMapping("/getFilterByUsersByCommerce")
    public LinkedHashMap<String, Object> getFilterByIdentificationDocument(@RequestBody final String dataForm,HttpServletRequest httpServletRequest) {

        LogsClass logTo = new LogsClass(httpServletRequest, new Object() {}.
                getClass().getEnclosingMethod().getName());

        LinkedHashMap<String, Object> response = new LinkedHashMap<>();
        String filterField = (new JSONObject(dataForm).get("filterField")).toString();
        String filterFieldEnd = (new JSONObject(dataForm).get("filterFieldEnd")).toString();
        int index = Integer.parseInt((new JSONObject(dataForm).get("page")).toString());
        int typeService = Integer.parseInt((new JSONObject(dataForm).get("typeServices")).toString());
        int quantity = Integer.parseInt((new JSONObject(dataForm).get("quantity")).toString());
        Pageable pageable = PageRequest.of(index, quantity);
        String rif = (new JSONObject(dataForm).get("rif")).toString();

        try {

            if (rif.equals("null")){
                Page<Object> result = userService.getFilterForUsersAdmin(typeService,filterField,filterFieldEnd,pageable);
                response.put("userEntity", result);
            }else {
                Page<UsersByCommerceTo> result = userService.getFilterForUser(typeService,filterField,filterFieldEnd,rif,pageable);
                response.put("userEntity", result);
            }




        } catch (Exception e){
            response.put("ERROR", null);
            logger.error(e.getMessage(), e);
            response.put("message", "An error occurred: " + e.getMessage());

            manageLogs.severeErrorLogger(logTo,httpServletRequest,
                    "Error: "+ logTo.getMethodName() + ": "+ e,
                    e,"user",true);
        }

        return response;
    }

    @PostMapping("/saveNewTermsAndConditions")
    public LinkedHashMap<String, Object> saveNewTermsAndConditions(@RequestBody final String dataForm,
                                                                   final HttpServletRequest httpServletRequest) {

        LogsClass logTo = new LogsClass(httpServletRequest, new Object() {}.
                getClass().getEnclosingMethod().getName());

        LinkedHashMap<String, Object> response = new LinkedHashMap<>();

        try {

            JSONObject jsonObject = new JSONObject(dataForm);
            String urlFile = jsonObject.getString("urlFile");
            String termsOrPolicies = jsonObject.getString("termsOrPolicies");
            Long idUser = jsonObject.getLong("registerBy");

            termsAndConditionsService.saveNewTermsAndConditions(urlFile, termsOrPolicies, idUser, httpServletRequest);

            response.put("status", "SUCCESS");

            manageLogs.infoLogger(logTo,httpServletRequest,
                    "Successfully: " + logTo.getMethodName(),
                    "user",false);

        } catch (Exception e) {
            response.put("ERROR", null);
            logger.error(e.getMessage(), e);
            response.put("message", "An error occurred: " + e.getMessage());

            manageLogs.severeErrorLogger(logTo,httpServletRequest,
                    "Error: "+ logTo.getMethodName() + ": "+ e,
                    e,"user",false);
        }

        return response;
    }

    @GetMapping("/getTermsAndConditions")
    public LinkedHashMap<String, Object> getTermsAndConditions(final HttpServletRequest httpServletRequest) {

        LogsClass logTo = new LogsClass(httpServletRequest, new Object() {}.
                getClass().getEnclosingMethod().getName());

        LinkedHashMap<String, Object> response = new LinkedHashMap<>();

        try {

            List<TermsAndConditionsEntity> termsAndConditionsEntityList = termsAndConditionsService.
                    getLastTermsConditionsAndPolicies();

            response.put("termsAndConditionsEntityList", termsAndConditionsEntityList);

            manageLogs.infoLogger(logTo,httpServletRequest,
                    "Successfully: " + logTo.getMethodName(),
                    "user",false);

        } catch (Exception e) {

            logger.error(e.getMessage(), e);

            response.put("ERROR", null);
            response.put("message", "An error occurred: " + e.getMessage());

            manageLogs.severeErrorLogger(logTo,httpServletRequest,
                    "Error: "+ logTo.getMethodName() + ": "+ e,
                    e,"user",false);
        }

        return response;
    }

    @GetMapping("/getStatusApprovalByUser/{idUser}")
    LinkedHashMap<String, Object> getStatusApprovalByUser(@PathVariable final Long idUser,
                                                          final HttpServletRequest httpServletRequest) {

        LogsClass logTo = new LogsClass(httpServletRequest, new Object() {}.
                getClass().getEnclosingMethod().getName());

        LinkedHashMap<String, Object> response = new LinkedHashMap<>();

        try {

            List<ApprovalUserTo> approvalUserToList = approvalUserService.
                    getApprovalUserByIdUser(idUser);

            response.put("approvalUserToList", approvalUserToList);

            manageLogs.infoLogger(logTo,httpServletRequest,
                    "Successfully: " + logTo.getMethodName(),
                    "user",false);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);

            response.put("ERROR", null);
            response.put("message", "An error occurred: " + e.getMessage());

            manageLogs.severeErrorLogger(logTo,httpServletRequest,
                    "Error: "+ logTo.getMethodName() + ": "+ e,
                    e,"user",false);
        }

        return response;
    }

    @PostMapping("/setApprovalUserStatus")
    LinkedHashMap<String, Object> setApprovalUserStatus(@RequestBody final String dataForm,
                                                        final HttpServletRequest httpServletRequest) {

        LogsClass logTo = new LogsClass(httpServletRequest, new Object() {}.
                getClass().getEnclosingMethod().getName());

        LinkedHashMap<String, Object> response = new LinkedHashMap<>();

        try {

            JSONObject jsonObject = new JSONObject(dataForm);
            Long idUser = jsonObject.getLong("idUser");
            JSONArray termsOrPoliciesArray = jsonObject.getJSONArray("termsOrPolicies");

            List<String> termsOrPolicies = new ArrayList<>();
            for (int i = 0; i < termsOrPoliciesArray.length(); i++) {
                termsOrPolicies.add(termsOrPoliciesArray.getString(i));
            }

            if(termsOrPolicies.size() == 2){
                approvalUserService.changeStatusApprovalUser(idUser, termsOrPolicies.get(0));
                approvalUserService.changeStatusApprovalUser(idUser, termsOrPolicies.get(1));
            } else {
                approvalUserService.changeStatusApprovalUser(idUser, termsOrPolicies.get(0));
            }

            manageLogs.infoLogger(logTo,httpServletRequest,
                    "Successfully: " + logTo.getMethodName(),
                    "user",false);

            response.put("status", "SUCCESS");

        } catch (Exception e){
            logger.error(e.getMessage(), e);

            response.put("ERROR", null);
            response.put("message", "An error occurred: " + e.getMessage());

            manageLogs.severeErrorLogger(logTo,httpServletRequest,
                    "Error: "+ logTo.getMethodName() + ": "+ e,
                    e,"user",false);
        }

        return response;
    }


    @PostMapping("/getAllUsersByAdmin")
    public LinkedHashMap<String, Object> getAllUsersByAdmin(@RequestBody final String dataForm,HttpServletRequest httpServletRequest) {

        LogsClass logTo = new LogsClass(httpServletRequest, new Object() {}.
                getClass().getEnclosingMethod().getName());

        LinkedHashMap<String, Object> response = new LinkedHashMap<>();
        int index = Integer.parseInt((new JSONObject(dataForm).get("page")).toString());
        int quantity = Integer.parseInt((new JSONObject(dataForm).get("quantity")).toString());
        Pageable pageable = PageRequest.of(index, quantity);

        try {

            List<Object> result = userService.getAllUsersByAdmin(pageable);
            response.put("usersByAdmin", result);



        } catch (Exception e){
            response.put("ERROR", null);
            logger.error(e.getMessage(), e);
            response.put("message", "An error occurred: " + e.getMessage());

            manageLogs.severeErrorLogger(logTo,httpServletRequest,
                    "Error: "+ logTo.getMethodName() + ": "+ e,
                    e,"user",true);
        }

        return response;
    }


    @PostMapping("/blockUserIntWithReason")
    public LinkedHashMap<String, Object> blockUserIntWithReason(@RequestBody final String dataForm,HttpServletRequest httpServletRequest) {

        LogsClass logTo = new LogsClass(httpServletRequest, new Object() {}.
                getClass().getEnclosingMethod().getName());

        LinkedHashMap<String, Object> response = new LinkedHashMap<>();
        Long idUser = Long.valueOf((new JSONObject(dataForm).get("idUser")).toString());
        String reasonStatus = (new JSONObject(dataForm).get("reasonStatus")).toString();


        try {

            Response result = userService.blockUser(idUser,reasonStatus);
            manageLogs.infoLogger(logTo,httpServletRequest,
                    "Successfully: " + logTo.getMethodName(),
                    "user",false);

            response.put("SUCCESS", result.getData());



        } catch (Exception e){
            response.put("ERROR", e.getMessage());
            logger.error(e.getMessage(), e);
            response.put("message", "An error occurred: " + e.getMessage());

            manageLogs.severeErrorLogger(logTo,httpServletRequest,
                    "Error: "+ logTo.getMethodName() + ": "+ e,
                    e,"user",false);
        }

        return response;
    }


    @PostMapping("/unlockUserInt")
    public LinkedHashMap<String, Object> unlockUserInt(@RequestBody final String dataForm,HttpServletRequest httpServletRequest) {

        LogsClass logTo = new LogsClass(httpServletRequest, new Object() {}.
                getClass().getEnclosingMethod().getName());

        LinkedHashMap<String, Object> response = new LinkedHashMap<>();
        Long idUser = Long.valueOf((new JSONObject(dataForm).get("idUser")).toString());
        String reasonStatus = (new JSONObject(dataForm).get("reasonStatus")).toString();


        try {

            Response result = userService.unlockUserWithReason(idUser,reasonStatus);
            manageLogs.infoLogger(logTo,httpServletRequest,
                    "Successfully: " + logTo.getMethodName(),
                    "user",false);

            response.put("status", "SUCCESS");



        } catch (Exception e){
            response.put("ERROR", e.getMessage());
            logger.error(e.getMessage(), e);
            response.put("message", "An error occurred: " + e.getMessage());

            manageLogs.severeErrorLogger(logTo,httpServletRequest,
                    "Error: "+ logTo.getMethodName() + ": "+ e,
                    e,"user",false);
        }

        return response;
    }


    @PostMapping("/createUserAdminForUsers")
    public LinkedHashMap<String, Object> createUserAdmincreateUserAdmin(@RequestBody final String dataForm,
                                                       final HttpServletRequest httpServletRequest) {

        LogsClass logTo = new LogsClass(httpServletRequest, new Object() {}.
                getClass().getEnclosingMethod().getName());

        LinkedHashMap<String, Object> response = new LinkedHashMap<>();

        try {

            String clientName = (new JSONObject(dataForm).get("clientName")).toString();
            String identificationDocument = (new JSONObject(dataForm).get("identificationDocument")).toString();
            String phoneNumber = (new JSONObject(dataForm).get("phoneNumber")).toString();
            String email = (new JSONObject(dataForm).get("email")).toString();
            Long idUser = Long.parseLong((new JSONObject(dataForm).get("idUser")).toString());

            Long result = userService.createUserAdminForUser(identificationDocument,phoneNumber,
                    idUser,email,clientName);
            response.put("idUser", result);
            logger.info("User saved successfully");

            manageLogs.infoLogger(logTo,httpServletRequest,
                    "Successfully: " + logTo.getMethodName(),
                    "user",true);

        } catch (Exception e){
            response.put("ERROR", null);
            logger.error(e.getMessage(), e);
            response.put("message", "An error occurred: " + e.getMessage());

            manageLogs.severeErrorLogger(logTo,httpServletRequest,
                    "Error: "+ logTo.getMethodName() + ": "+ e,
                    e,"user",true);
        }

        return response;
    }


}
