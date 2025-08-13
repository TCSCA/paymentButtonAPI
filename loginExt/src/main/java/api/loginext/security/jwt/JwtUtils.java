package api.loginext.security.jwt;

import api.loginext.entity.UserEntity;
import api.loginext.util.SecurityUtils;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Arrays;
import java.util.Date;

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

    public String generateToken(UserEntity userEntity) {
        Key key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));

        String prefix = SecurityUtils.AUTH_TOKEN_PREFIX;

         return prefix.concat(Jwts.builder()
                 .setSubject(userEntity.getUserName())
                 .claim("profile", userEntity.getProfileEntity())
                 .claim("idUser", userEntity.getIdUser())
                 .setExpiration(new Date(System.currentTimeMillis() + timeout))
                 .signWith(key, SignatureAlgorithm.HS512)
                 .compact());
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
}
