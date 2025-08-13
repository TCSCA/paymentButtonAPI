package api.apiPaymentButton.config;

import api.apiPaymentButton.util.LogTo;
import api.apiPaymentButton.util.ManageLog;
import api.apiPaymentButton.util.Response;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.LinkedHashMap;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ManageLog manageLog;

    public CustomAuthenticationEntryPoint(ManageLog manageLog) {
        this.manageLog = manageLog;
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException e) throws IOException {


        String requestURI = request.getRequestURI();
        String[] ignoringRequest = new String[]{"/css/**", "/js/**", "/img/**", "/lib/**", "/favicon.ico"};
        Thread currentThread = Thread.currentThread();
        LogTo logTo = new LogTo(currentThread.getId(),request,requestURI, LocalDateTime.now(ZoneId.of("America/Caracas")));


        GsonBuilder b = new GsonBuilder();
        Gson gson = b.create();

        LinkedHashMap<String, Object> logTrace = new LinkedHashMap<>();
        logTrace.put("requestURI", request.getRequestURI());
        Response jsonResponse = new Response("ERROR","Metodo invalido", logTrace);
        manageLog.infoLogger(logTo,request,gson.toJson(jsonResponse),false);

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        if (!Arrays.asList(ignoringRequest).contains(requestURI)) {
            response.setContentType("application/json");

            response.getWriter().write(gson.toJson(jsonResponse));
        }
    }
}
