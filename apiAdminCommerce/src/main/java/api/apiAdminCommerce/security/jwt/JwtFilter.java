package api.apiAdminCommerce.security.jwt;

import api.logsClass.LogsClass;
import api.logsClass.ManageLogs;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JwtFilter extends OncePerRequestFilter {

    private final ManageLogs manageLogs;

    public JwtFilter(ManageLogs manageLogs) {
        this.manageLogs = manageLogs;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        LogsClass logTo = new LogsClass(request, new Object() {}.
                getClass().getEnclosingMethod().getName());

        try {
            if(!JwtUtils.validateApiKey(request)) {
                SecurityContextHolder.clearContext();
                manageLogs.errorLogger(logTo, request, "ERROR: validando la apiKey",
                        "unlogged user", false);
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                return;
            }

            Claims claims  = JwtUtils.extractClaims(request);
            if(claims == null) {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            }
            Authentication authentication = JwtUtils.getAuthentication(claims);
            if(authentication != null && JwtUtils.isTokenValid(claims)) {
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else {
                SecurityContextHolder.clearContext();
                return;
            }
            if(!JwtUtils.validateAuthToken(request, claims.get("idUser", Long.class))) {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                SecurityContextHolder.clearContext();
                return;
            }
            filterChain.doFilter(request, response);

        } catch (ExpiredJwtException e) {
            manageLogs.severeErrorLogger(logTo, request, "Expired JWT token", e, "user", false);
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Expired JWT token");
        } catch (UnsupportedJwtException e) {
            manageLogs.severeErrorLogger(logTo, request, "Unsupported JWT token", e, "user", false);
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Unsupported JWT token");
        } catch (MalformedJwtException e) {
            manageLogs.severeErrorLogger(logTo, request, "Malformed JWT token", e, "user", false);
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Malformed JWT token");
        } catch (Exception e) {
            manageLogs.severeErrorLogger(logTo, request, "An unexpected error occurred", e, "user", false);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }

    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request)
            throws ServletException {
        String path = request.getRequestURI();
        List<String> permitedUrl = new ArrayList<>();
        permitedUrl.add("/api/resetPassword");
        permitedUrl.add("/api/downloadQrByCommerce");
        Boolean pathEqual = false;
        for (String route : permitedUrl) {
            if(route.equals(path)) {
                pathEqual = true;
            }
        }
        return pathEqual;
    }

}
