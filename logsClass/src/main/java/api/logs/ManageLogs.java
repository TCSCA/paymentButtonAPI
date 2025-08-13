package api.logsClass;

import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;

@Component
public class ManageLogs {
    @Value("${app.security.name}")
    private String name;

    @Value("${artefact.name}")
    private String artefactName;

    private static String appName;

    @PostConstruct
    public void init(){
        appName = name;
    }

    @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE,
            proxyMode = ScopedProxyMode.TARGET_CLASS)
    public void infoLogger(final LogsClass logTo, final HttpServletRequest httpServletRequest,
                           final String message, final String userName, final boolean authenticated){
        String userAgent = httpServletRequest.getHeader("User-Agent")!=null?httpServletRequest.getHeader("User-Agent").split(" ")[0]:"undefined";
        LinkedHashMap<String,String> linkedHashMap;
        if (!logTo.getMethodName().equalsIgnoreCase("login")) {
            linkedHashMap = initPidAndPlatform(httpServletRequest, authenticated);
        } else {
            linkedHashMap = new LinkedHashMap<>();
            String platform = httpServletRequest.getHeader("Platform")!=null?httpServletRequest.getHeader("Platform"):userAgent;
            linkedHashMap.put("platform",platform);
            linkedHashMap.put("pid",httpServletRequest.getRequestedSessionId());
        }
        setValuesMDC(logTo, linkedHashMap, userName);
        Logger logger = LoggerFactory.getLogger(logTo.getMethodName());
        logger.info(message);
        MDC.clear();
    }

    private void setValuesMDC(LogsClass logTo, LinkedHashMap<String, String> linkedHashMap, String uName) {
        inicialiceMDC();
        LocalDateTime localDateTime = LocalDateTime.now(ZoneId.of("America/Caracas"));
        MDC.put("PID",linkedHashMap.get("pid"));
        MDC.put("Platf",linkedHashMap.get("platform"));
        MDC.put("UName",uName);
        MDC.put("ServerIp",logTo.getServerIp());
        MDC.put("UserIp",logTo.getUserIp());
        MDC.put("StartTime",getDateInFormat(logTo.getInitialTime()));
        MDC.put("Time",getDateInFormat(localDateTime));
        MDC.put("ElapsedSecond",logTo.getTimeInSeconds(localDateTime).toString());
        MDC.put("rqst",logTo.getMethodName());
        MDC.put("AName", artefactName);
    }

    @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE,
            proxyMode = ScopedProxyMode.TARGET_CLASS)
    public void severeErrorLogger(final LogsClass logTo, final HttpServletRequest httpServletRequest,
                                  final String message, final Throwable e, final String userName,
                                  final boolean authenticated){
        LinkedHashMap<String,String> linkedHashMap = initPidAndPlatform(httpServletRequest, authenticated);
        initPidAndPlatform(httpServletRequest, authenticated);
        setValuesMDC(logTo, linkedHashMap, userName);
        MDC.put("Exc",e.getMessage());
        Logger logger = LoggerFactory.getLogger(logTo.getMethodName());
        logger.error(message,e);
        MDC.clear();
    }

    @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE,
            proxyMode = ScopedProxyMode.TARGET_CLASS)
    public void errorLogger(final LogsClass logTo, final HttpServletRequest httpServletRequest,
                            final String message, final String userName, final Boolean authenticated){
        LinkedHashMap<String,String> linkedHashMap = initPidAndPlatform(httpServletRequest, authenticated);
        initPidAndPlatform(httpServletRequest, authenticated);
        setValuesMDC(logTo, linkedHashMap, userName);
        Logger logger = LoggerFactory.getLogger(logTo.getMethodName());
        logger.error(message);
        MDC.clear();
    }

    private LinkedHashMap<String,String> initPidAndPlatform(HttpServletRequest httpServletRequest,
                                                            Boolean authenticated) {
        LinkedHashMap<String,String> linkedHashMap = new LinkedHashMap<>();

        if (authenticated) {
            String token = httpServletRequest.getHeader("Token");
            linkedHashMap.put("platform", token.substring(appName.length() + 1, appName.length() + 4));
            linkedHashMap.put("pid",token.substring(token.length()-10));
        } else {
            String platform = httpServletRequest.getHeader("Platform")!=null?httpServletRequest.getHeader("Platform"):httpServletRequest.getHeader("User-Agent").split(" ")[0];
            linkedHashMap.put("platform",platform);
            linkedHashMap.put("pid",httpServletRequest.getRequestedSessionId());
        }
        return linkedHashMap;
    }

    @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE,
            proxyMode = ScopedProxyMode.TARGET_CLASS)
    private void inicialiceMDC(){
        MDC.put("PID","");
        MDC.put("UName","");
        MDC.put("ServerIp","");
        MDC.put("UserIp","");
        MDC.put("InputData","");
        MDC.put("Exc","");
        MDC.put("Platf","");
        MDC.put("AName", artefactName);
    }

    private String getDateInFormat(LocalDateTime localDateTime){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return localDateTime.format(formatter);
    }
}
