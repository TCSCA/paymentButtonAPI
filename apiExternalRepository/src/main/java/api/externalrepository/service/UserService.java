package api.externalrepository.service;

import api.externalrepository.entity.ConfigurationEntity;
import api.externalrepository.entity.HistoryPasswordEntity;
import api.externalrepository.entity.ProfileEntity;
import api.externalrepository.entity.StatusUserEntity;
import api.externalrepository.entity.UserEntityExt;
import api.externalrepository.repository.ConfigurationRepository;
import api.externalrepository.repository.HistoryPasswordRepository;
import api.externalrepository.repository.UserExtRepository;
import api.externalrepository.to.UsersBlockedTo;
import api.externalrepository.util.Response;
import api.logsClass.LogsClass;
import api.logsClass.ManageLogs;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/*import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;*/
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.LinkedHashMap;
import java.util.List;

@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private final ConfigurationRepository configurationRepository;

    private final UserExtRepository userExtRepository;

    private final HistoryPasswordRepository historyPasswordRepository;

    private final ManageLogs manageLogs;

    public UserService(ConfigurationRepository configurationRepository, UserExtRepository userExtRepository, HistoryPasswordRepository historyPasswordRepository, ManageLogs manageLogs) {
        this.configurationRepository = configurationRepository;
        this.userExtRepository = userExtRepository;
        this.historyPasswordRepository = historyPasswordRepository;
        this.manageLogs = manageLogs;
    }

    public Boolean createUserAdminExt(final String rif, final String password, final Long idUser,final String email){

        try {
            ConfigurationEntity configurationEntity = configurationRepository.
                    findConfigurationEntityByIdConfiguration(1L);

            configurationEntity.setValue(configurationEntity.getValue());
            UserEntityExt userEntity= new UserEntityExt();
            userEntity.setFirstLogin(true);
            userEntity.setUserName(rif);
            userEntity.setProfileEntity(new ProfileEntity(1L));
            userEntity.setEmail(email);
            StatusUserEntity statusUserEntity = new StatusUserEntity(1L);
            userEntity.setStatusUserEntity(statusUserEntity);

            userExtRepository.save(userEntity);

            HistoryPasswordEntity historyPasswordEntity = new HistoryPasswordEntity();

            historyPasswordEntity.setPassword(password);
            historyPasswordEntity.setUserEntity(new UserEntityExt(userEntity.getIdUser()));
            historyPasswordEntity.setRegisterDate(OffsetDateTime.now());
            historyPasswordEntity.setExpirationDate(OffsetDateTime.now().plusDays(30));
            historyPasswordRepository.save(historyPasswordEntity);

            return true;
        } catch (Exception e) {
            logger.error(e.getMessage());
            return false;
        }

    }

    public String searchConfigurationById(final Long idConfiguration) {
        return (configurationRepository.findConfigurationEntityByIdConfiguration(idConfiguration)).getValue();
    }

    public String searchConfigurationByKey(final String key) {
        return configurationRepository.getConfigurationByKey(key);
    }

    public Boolean createUserExtWithProfile(final String rif, final String password, final Long idUser, final Long idProfile,final String email){

        try {
            ConfigurationEntity configurationEntity = configurationRepository.
                    findConfigurationEntityByIdConfiguration(1L);

            configurationEntity.setValue(configurationEntity.getValue());
            UserEntityExt userEntity= new UserEntityExt();
            userEntity.setFirstLogin(true);
            userEntity.setUserName(rif);
            userEntity.setProfileEntity(new ProfileEntity(idProfile));
            userEntity.setEmail(email);
            StatusUserEntity statusUserEntity = new StatusUserEntity(1L);
            userEntity.setStatusUserEntity(statusUserEntity);

            userExtRepository.save(userEntity);

            HistoryPasswordEntity historyPasswordEntity = new HistoryPasswordEntity();

            historyPasswordEntity.setPassword(password);
            historyPasswordEntity.setUserEntity(new UserEntityExt(userEntity.getIdUser()));
            historyPasswordEntity.setRegisterDate(OffsetDateTime.now());
            historyPasswordEntity.setExpirationDate(OffsetDateTime.now().plusDays(30));
            historyPasswordRepository.save(historyPasswordEntity);

            return true;
        } catch (Exception e) {
            logger.error(e.getMessage());
            return false;
        }

    }


    public Page<UsersBlockedTo> usersBlocked(HttpServletRequest httpServletRequest, Pageable pageable){

        LinkedHashMap<String, Object> response = new LinkedHashMap<>();
        LogsClass logTo = new LogsClass(httpServletRequest, new Object() {}.
                getClass().getEnclosingMethod().getName());
        try {

            Page<UsersBlockedTo> userEntityExtList = userExtRepository.findUsersBlocked(pageable);
            response.put("userEntityExtList",userEntityExtList);

            logger.info("UsersBlocked list get successfully");
            manageLogs.infoLogger(logTo,httpServletRequest,
                    "Successfully: " + logTo.getMethodName(),
                    "user",true);
            return userEntityExtList;

        } catch (Exception e) {
            logger.error(e.getMessage());
            return null;
        }

    }


    public Response unlockUser(HttpServletRequest httpServletRequest,final long idUser){

        LogsClass logTo = new LogsClass(httpServletRequest, new Object() {}.
                getClass().getEnclosingMethod().getName());
        try {

            UserEntityExt userEntityExt = userExtRepository.findByIdUser(idUser);
            userEntityExt.setStatusUserEntity(new StatusUserEntity(1L));
            userEntityExt.setUpdateDate(OffsetDateTime.now(ZoneId.of("America/Caracas")));
            userEntityExt.setFailedAttempts(0);

            userExtRepository.save(userEntityExt);
            logger.info("User unlock successfully");
            manageLogs.infoLogger(logTo,httpServletRequest,
                    "Successfully: " + logTo.getMethodName(),
                    "user",true);
            return new Response("SUCCESS","Transacción exitosa");

        } catch (Exception e) {
            logger.error(e.getMessage());
            return new Response("ERROR","No se pudo desbloquear el usuario");
        }

    }

    public Response findByUsername(final HttpServletRequest httpServletRequest,final String username,final String resetPassword){

        LogsClass logTo = new LogsClass(httpServletRequest, new Object() {}.
                getClass().getEnclosingMethod().getName());
        try {

            UserEntityExt userEntityExt = userExtRepository.findByUserName(username);
            userEntityExt.setTemporalPassword(resetPassword);
            userExtRepository.save(userEntityExt);
            logger.info("User save successfully");
            manageLogs.infoLogger(logTo,httpServletRequest,
                    "Successfully: " + logTo.getMethodName(),
                    "user",false);
            return new Response("SUCCESS",userEntityExt);

        } catch (Exception e) {
            logger.error(e.getMessage());
            return new Response("ERROR","No se pudo desbloquear el usuario");
        }

    }

    public Response findByIdUser(final HttpServletRequest httpServletRequest,final Long idUser){

        LogsClass logTo = new LogsClass(httpServletRequest, new Object() {}.
                getClass().getEnclosingMethod().getName());
        try {

            UserEntityExt userEntityExt = userExtRepository.findByIdUser(idUser);
            logger.info("User find successfully");
            manageLogs.infoLogger(logTo,httpServletRequest,
                    "Successfully: " + logTo.getMethodName(),
                    "user",false);
            return new Response("SUCCESS",userEntityExt);

        } catch (Exception e) {
            logger.error(e.getMessage());
            return new Response("ERROR","No se pudo desbloquear el usuario");
        }

    }


    public Response blockUser(final String username) {
        UserEntityExt userEntity= userExtRepository.findByUserName(username);
        userEntity.setUpdateDate(OffsetDateTime.now(ZoneId.of("America/Caracas")));
        userEntity.setStatusUserEntity(new StatusUserEntity(4L));
        userExtRepository.save(userEntity);
        return new Response("SUCCESS","Se actualizo la información correctamente");
    }

    @Transactional
    public Boolean updateAllUserStatusByUsername(final Long idStatus, final List<String> usernameList) {

        try {

            if (idStatus == 3) {
                userExtRepository.updateAllUserStatusByUsername(usernameList, idStatus, 1L);
            } else if (idStatus == 1) {
                userExtRepository.updateAllUserStatusByUsername(usernameList, idStatus, 3L);
            }

            logger.info("All users updated by commerceStatus");
            return true;

        } catch (Exception e){
            logger.error(e.getMessage(), e);
            return false;
        }

    }


}
