package api.apiP2c.security.jwt;

import api.apiP2c.entity.BankCommerceEntity;
import api.apiP2c.entity.CommerceEntity;
import api.apiP2c.repository.BankCommerceRepository;
import api.apiP2c.repository.CommerceRepository;
import api.apiP2c.util.SecurityUtils;
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
import java.util.*;
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
        secret = JWT_SECRET;
        timeout = JWT_EXPIRATION_IN_MS;
        apiHeader = apiKeyHeader;
        apiValue = apiKeyValue;
        gatewayHeader = apiGatewayHeader;
        gatewayValue = apiGatewayKeyValue;
        bankCommerceRepository = bankCommerceInstancedRepository;
        commerceRepository = commerceInstancedRepository;
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

    public static Boolean validateBankCommerceInformationManualPayment(String rif) {
        CommerceEntity commerceEntity = commerceRepository.findByCommerceDocumentAndStatusCommerce_IdStatusPreRegister
                (rif, 2L);
        List<BankCommerceEntity> bankCommerceEntities = new ArrayList<>();
        if (commerceEntity != null) {
            LocalDate currentDate = LocalDate.now();
            bankCommerceEntities = bankCommerceRepository.
                    getBankCommerceInformationByIdCommerceAndLicenseValidManualPayment(commerceEntity.getIdCommerce(), currentDate);
            if(bankCommerceEntities.isEmpty()) {
                return false;
            } else {
                return true;
            }
        } else {
            return null;
        }
    }
}
