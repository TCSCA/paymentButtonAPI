package api.authentication.security.jwt;

import api.authentication.security.UserPrincipal;
import api.authentication.util.SecurityUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class JwtUtils {

    @Value("${app.jwt.secret}")
    private String JWT_SECRET;

    @Value("${app.jwt.expiration-in-ms}")
    private Long JWT_EXPIRATION_IN_MS;

    @Value("${app.api.key}")
    private String apiKeyValue;

    @Value("${app.gateway.key}")
    private String apiGatewayKeyValue;

    @Value("API_KEY")
    private String apiKeyHeader;

    @Value("GATEWAY_KEY")
    private String apiGatewayHeader;

    private static String secret;

    private static Long timeout;

    private static String apiHeader;

    private static String apiValue;

    private static String gatewayHeader;

    private static String gatewayValue;

    public JwtUtils() {}

    @PostConstruct
    public void init(){
        secret = JWT_SECRET;
        timeout = JWT_EXPIRATION_IN_MS;
        apiHeader = apiKeyHeader;
        apiValue = apiKeyValue;
        gatewayHeader = apiGatewayHeader;
        gatewayValue = apiGatewayKeyValue;
    }
    public static Authentication getAuthentication(Claims claims) {

        Set<GrantedAuthority> authorities = Arrays.stream(claims.get("profile").toString().split(","))
                .map(SecurityUtils::convertToAuthority)
                .collect(Collectors.toSet());

        UserDetails userDetails = UserPrincipal.builder()
                .username(claims.getSubject())
                .authorities(authorities)
                .id(claims.get("idUser", Long.class))
                .build();

        if(claims.getSubject() == null) {
            return null;
        }
        return new UsernamePasswordAuthenticationToken(userDetails, null, authorities);
    }

    public static boolean isTokenValid(Claims claims) {
        if(claims.getExpiration().before(new Date(System.currentTimeMillis()))) {
            return false;
        }
        return true;
    }

    public static Claims extractClaims(HttpServletRequest request) {
        String token = SecurityUtils.extractAuthTokenFromRequest(request);
        if(token==null) {
            return null;
        }
        Key key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public static Claims extractClaimsFromToken(String token) {

        Key key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public static boolean validateApiKey(HttpServletRequest httpServletRequest){
        String apiKey = httpServletRequest.getHeader(apiHeader.toLowerCase());
        String gatewayKey = httpServletRequest.getHeader(gatewayHeader.toLowerCase());

        if(apiKey!=null && gatewayKey!= null) {
            if(apiValue.equals(apiKey) && gatewayValue.equals(gatewayKey)) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public static OffsetDateTime setExpirationDate() {
        OffsetDateTime currentDate = OffsetDateTime.now(ZoneId.of("America/Caracas"));
        OffsetDateTime expirationDate = currentDate.plusDays(1);
        return expirationDate;
    }
}
