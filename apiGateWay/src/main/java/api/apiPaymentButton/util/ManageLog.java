package api.apiPaymentButton.util;

import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;

@Component
public class ManageLog {

    @Value("${app.security.name}")
    private String name;

    private static String appName;

    @PostConstruct
    public void init(){
        appName = name;
    }

    @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE,
            proxyMode = ScopedProxyMode.TARGET_CLASS)
    public void infoLogger(final LogTo logTo, final HttpServletRequest httpServletRequest, final String message, final boolean authenticated){
        Authentication authentication = null;
        String userAgent = httpServletRequest.getHeader("User-Agent")!=null?httpServletRequest.getHeader("User-Agent").split(" ")[0]:"undefined";
        if (authenticated) {
            authentication = SecurityContextHolder.getContext().getAuthentication();
        }
        LinkedHashMap<String,String> linkedHashMap = null;
        if (!logTo.getMethodName().equalsIgnoreCase("login")) {
            linkedHashMap = initPidAndPlatform(httpServletRequest, logTo, authentication);
        } else {
            linkedHashMap = new LinkedHashMap<>();
            String platform = httpServletRequest.getHeader("Platform")!=null?httpServletRequest.getHeader("Platform"):userAgent;
            linkedHashMap.put("platform",platform);
            linkedHashMap.put("pid",httpServletRequest.getRequestedSessionId());
        }
        String uName = authentication!=null?authentication.getName():"Unlogged User";

        setValuesMDC(logTo, linkedHashMap, uName);
        Logger logger = LoggerFactory.getLogger(logTo.getMethodName());
        logger.info(message);
        MDC.clear();
    }

    private void setValuesMDC(LogTo logTo, LinkedHashMap<String, String> linkedHashMap, String uName) {
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
    }

    @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE,
            proxyMode = ScopedProxyMode.TARGET_CLASS)
    public void severeErrorLogger(final LogTo logTo, final HttpServletRequest httpServletRequest, final String message, final Throwable e,final boolean authenticated){
        Authentication authentication = null;
        if (authenticated) {
            authentication = SecurityContextHolder.getContext().getAuthentication();
        }
        LinkedHashMap<String,String> linkedHashMap = initPidAndPlatform(httpServletRequest, logTo, authentication);
        String uName = authentication!=null?authentication.getName():"Unlogged User";

        initPidAndPlatform(httpServletRequest, logTo, authentication);
        setValuesMDC(logTo, linkedHashMap, uName);
        MDC.put("Exc",e.getMessage());
        Logger logger = LoggerFactory.getLogger(logTo.getMethodName());
        logger.error(message,e);
        MDC.clear();
    }

    @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE,
            proxyMode = ScopedProxyMode.TARGET_CLASS)
    public void errorLogger(final LogTo logTo, final HttpServletRequest httpServletRequest, final String message,final Boolean authenticated){
        Authentication authentication = null;
        if (authenticated) {
            authentication = SecurityContextHolder.getContext().getAuthentication();
        }
        LinkedHashMap<String,String> linkedHashMap = initPidAndPlatform(httpServletRequest, logTo, authentication);
        String uName = authentication!=null?authentication.getName():"Unlogged User";

        initPidAndPlatform(httpServletRequest, logTo, authentication);

        setValuesMDC(logTo, linkedHashMap, uName);
        Logger logger = LoggerFactory.getLogger(logTo.getMethodName());
        logger.error(message);
        MDC.clear();
    }

    private LinkedHashMap<String,String> initPidAndPlatform(HttpServletRequest httpServletRequest,LogTo logTo, Authentication authentication) {
        LinkedHashMap<String,String> linkedHashMap = new LinkedHashMap<>();

        if (authentication!=null) {
            String token = httpServletRequest.getHeader("Token");
            linkedHashMap.put("platform", token.substring(appName.length() + 1, appName.length() + 4));
            linkedHashMap.put("pid",token.substring(token.length()-10));
        } else {
            if(httpServletRequest.getHeader("User-Agent") != null) {
                String platform = httpServletRequest.getHeader("Platform")!=null?httpServletRequest.getHeader("Platform"):httpServletRequest.getHeader("User-Agent").split(" ")[0];
            }
            linkedHashMap.put("platform","gateway");
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
    }

    private String getDateInFormat(LocalDateTime localDateTime){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return localDateTime.format(formatter);
    }
}
