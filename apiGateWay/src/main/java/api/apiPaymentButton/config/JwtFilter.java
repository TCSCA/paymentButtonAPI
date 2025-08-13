package api.apiPaymentButton.config;

import api.apiPaymentButton.util.LogTo;
import api.apiPaymentButton.util.ManageLog;
import api.apiPaymentButton.util.ValidationUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JwtFilter extends OncePerRequestFilter {

    private final ManageLog manageLog;

    public JwtFilter(ManageLog manageLog) {
        this.manageLog = manageLog;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain chain) throws ServletException, IOException {

        LogTo logTo = new LogTo(request, "filter");

        try {
            String path = request.getRequestURI();

            // Excluir rutas de Swagger y OpenAPI de la validación de apiKey
            if (!path.startsWith("/v3/api-docs") && !path.startsWith("/swagger-ui") && !path.equals("/swagger-ui.html")) {
                // Validar apiKey para todas las demás rutas
                if (!ValidationUtil.validateApiKey(request)) {
                    SecurityContextHolder.clearContext();
                    manageLog.errorLogger(logTo, request, "ERROR: al validar la apiKey", false);
                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    return;
                }
            }
            chain.doFilter(request, response);

        } catch (ExpiredJwtException e) {
            manageLog.severeErrorLogger(null, request, "Expired JWT token", e, false);
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Expired JWT token");
        } catch (UnsupportedJwtException e) {
            manageLog.severeErrorLogger(null, request, "Unsupported JWT token", e, false);
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Unsupported JWT token");
        } catch (MalformedJwtException e) {
            manageLog.severeErrorLogger(null, request, "Malformed JWT token", e, false);
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Malformed JWT token");
        } catch (Exception e) {
            manageLog.severeErrorLogger(null, request, "An unexpected error occurred", e, false);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

}
