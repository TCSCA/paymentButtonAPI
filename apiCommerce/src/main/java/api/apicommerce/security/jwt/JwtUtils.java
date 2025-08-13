package api.apicommerce.security.jwt;

import api.apicommerce.entity.TokenEntity;
import api.apicommerce.repository.TokenRepository;
import api.apicommerce.util.SecurityUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
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

    private final TokenRepository tokenInstancedRepository;

    private static TokenRepository tokenRepository;

    public JwtUtils(TokenRepository tokenInstancedRepository) {
        this.tokenInstancedRepository = tokenInstancedRepository;
    }

    @PostConstruct
    public void init(){
        tokenRepository = tokenInstancedRepository;
        secret = JWT_SECRET;
        timeout = JWT_EXPIRATION_IN_MS;
        apiHeader = apiKeyHeader;
        apiValue = apiKeyValue;
        gatewayHeader = apiGatewayHeader;
        gatewayValue = apiGatewayKeyValue;
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

    public static Authentication getAuthentication(Claims claims) {

        Set<GrantedAuthority> authorities = Arrays.stream(claims.get("profile").toString().split(","))
                .map(SecurityUtils::convertToAuthority)
                .collect(Collectors.toSet());

        if(claims.getSubject() == null) {
            return null;
        }
        return new UsernamePasswordAuthenticationToken("ADMIN", null, authorities);
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

    public static Boolean validateAuthToken(HttpServletRequest httpServletRequest, Long idUser) {
        String token = SecurityUtils.extractAuthTokenFromRequestWithPrefix(httpServletRequest);
        TokenEntity tokenEntity = tokenRepository.findByUserEntity_IdUserAndTokenAndActiveTrue(idUser, token);
        if(tokenEntity == null) {
            return false;
        }
        return true;
    }

}
