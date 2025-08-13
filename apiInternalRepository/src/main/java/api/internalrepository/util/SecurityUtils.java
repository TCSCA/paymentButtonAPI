package api.internalrepository.util;

import jakarta.servlet.http.HttpServletRequest;
/*import org.springframework.security.core.authority.SimpleGrantedAuthority;*/
import org.springframework.util.StringUtils;

public class SecurityUtils {

    public static final String ROLE_PREFIX = "ROLE_";
    public static final String AUTH_HEADER = "token";
    public static final String AUTH_TOKEN_TYPE = "inteligensa";
    public static final String AUTH_TOKEN_PREFIX = AUTH_TOKEN_TYPE + " ";

    /*public static SimpleGrantedAuthority convertToAuthority(String role) {
        String formattedRole = role.startsWith(ROLE_PREFIX) ? role : ROLE_PREFIX + role;
        return new SimpleGrantedAuthority(formattedRole);
    }

    public static String extractAuthTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTH_HEADER);

        if(StringUtils.hasLength(bearerToken) && bearerToken.startsWith(AUTH_TOKEN_PREFIX)) {
            return bearerToken.substring(12);
        }
        return null;
    }

    public static String extractAuthTokenFromRequestWithPrefix(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTH_HEADER);

        if(StringUtils.hasLength(bearerToken) && bearerToken.startsWith(AUTH_TOKEN_PREFIX)) {
            return bearerToken;
        }
        return null;
    }*/

}
