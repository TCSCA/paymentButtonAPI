package api.apicommerce.security.jwt;

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

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        try {
            if(!JwtUtils.validateApiKey(request)) {
                SecurityContextHolder.clearContext();
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                return;
            }

            Claims claims  = JwtUtils.extractClaims(request);
            if(claims == null) {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                SecurityContextHolder.clearContext();
                return;
            }
            Authentication authentication = JwtUtils.getAuthentication(claims);
            if(!JwtUtils.isTokenValid(claims)) {
                SecurityContextHolder.clearContext();
                return;
            }
            if(!JwtUtils.validateAuthToken(request, claims.get("idUser", Long.class))) {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                SecurityContextHolder.clearContext();
                return;
            }
            SecurityContextHolder.getContext().setAuthentication(authentication);
            filterChain.doFilter(request, response);

        } catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException e) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.sendError(HttpServletResponse.SC_FORBIDDEN, e.getMessage());
        }

    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request)
            throws ServletException {
        String path = request.getRequestURI();
        List<String> permitedUrl = new ArrayList<>();
        permitedUrl.add("/api/saveCommerceConfig");
        permitedUrl.add("/api/getCommerceBankInformation");
        permitedUrl.add("/api/validateCommerceLicence");
        Boolean pathEqual = false;
        for (String route : permitedUrl) {
            if(route.equals(path)) {
                pathEqual = true;
            }
        }
        return pathEqual;
    }

}
