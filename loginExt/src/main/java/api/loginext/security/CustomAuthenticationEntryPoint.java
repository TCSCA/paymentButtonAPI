package api.loginext.security;

import api.loginext.util.Response;
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

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException e) throws IOException {
        String requestURI = request.getServletPath();
        String[] ignoringRequest = new String[]{"/css/**", "/js/**", "/img/**", "/lib/**", "/favicon.ico"};
        Thread currentThread = Thread.currentThread();
/*
        LogTo logTo = new LogTo(currentThread.getId(),request,requestURI, LocalDateTime.now(ZoneId.of("America/Caracas")));
*/
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        if (!Arrays.asList(ignoringRequest).contains(requestURI)) {
            response.setContentType("application/json");
            GsonBuilder b = new GsonBuilder();
            Gson gson = b.create();
            Response jsonResponse = new Response("ERROR","MSG-000");

            //TODO agregar logsClass
            /*manageLog.infoLogger(logTo,request,gson.toJson(jsonResponse),false);*/

            response.getWriter().write(gson.toJson(jsonResponse));
        }
    }
}
