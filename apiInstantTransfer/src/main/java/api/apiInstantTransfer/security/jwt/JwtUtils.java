package api.apiInstantTransfer.security.jwt;

import api.apiInstantTransfer.entity.BankCommerceEntity;
import api.apiInstantTransfer.entity.CommerceEntity;
import api.apiInstantTransfer.repository.BankCommerceRepository;
import api.apiInstantTransfer.repository.CommerceRepository;
import api.apiInstantTransfer.util.SecurityUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
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
import java.time.LocalDate;
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

    private final BankCommerceRepository bankCommerceInstancedRepository;

    private static BankCommerceRepository bankCommerceRepository;

    private final CommerceRepository commerceInstancedRepository;

    private static CommerceRepository commerceRepository;

    public JwtUtils(BankCommerceRepository bankCommerceInstancedRepository, CommerceRepository commerceInstancedRepository) {
        this.bankCommerceInstancedRepository = bankCommerceInstancedRepository;
        this.commerceInstancedRepository = commerceInstancedRepository;
    }

    @PostConstruct
    public void init(){
        commerceRepository = commerceInstancedRepository;
        bankCommerceRepository = bankCommerceInstancedRepository;
        secret = JWT_SECRET;
        timeout = JWT_EXPIRATION_IN_MS;
        apiHeader = apiKeyHeader;
        apiValue = apiKeyValue;
        gatewayHeader = apiGatewayHeader;
        gatewayValue = apiGatewayKeyValue;
    }

    public static String generateToken() {
        Key key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));

        String prefix = SecurityUtils.AUTH_TOKEN_PREFIX;

        OffsetDateTime currentDate = OffsetDateTime.now(ZoneId.of("America/Caracas"));
        OffsetDateTime expirationDate = currentDate.plusMonths(1);

        return prefix.concat(Jwts.builder()
                .setSubject("ADMIN")
                .claim("profile", "ADMIN")
                .claim("idUser", 0)
                .setExpiration(Date.from(expirationDate.toInstant()))
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

    public static BankCommerceEntity validateBankCommerceInformation(String rif) {
        CommerceEntity commerceEntity = commerceRepository.findByCommerceDocumentAndStatusCommerce_IdStatusPreRegister
                (rif, 2L);
        if (commerceEntity != null) {
            LocalDate currentDate = LocalDate.now();
            BankCommerceEntity bankCommerceEntity = bankCommerceRepository.
                    getBankCommerceInformationByIdCommerceAndLicenseValid(commerceEntity.getIdCommerce(), currentDate);
            if(bankCommerceEntity != null) {
                return bankCommerceEntity;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }
}
