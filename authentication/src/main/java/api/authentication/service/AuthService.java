package api.authentication.service;

import api.authentication.entity.*;
import api.authentication.repository.AdministrativeUserRepository;
import api.authentication.repository.ClientRepository;
import api.authentication.repository.TokenRepository;
import api.authentication.repository.UserRepository;
import api.authentication.security.jwt.JwtUtils;
import api.authentication.util.SecurityUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Objects;

@Service
public class AuthService {

    private final TokenRepository tokenRepository;

    private final AdministrativeUserRepository administrativeUserRepository;

    private final ClientRepository clientRepository;

    private final UserRepository userRepository;

    public AuthService(TokenRepository tokenRepository, AdministrativeUserRepository administrativeUserRepository, ClientRepository clientRepository, UserRepository userRepository) {
        this.tokenRepository = tokenRepository;
        this.administrativeUserRepository = administrativeUserRepository;
        this.clientRepository = clientRepository;
        this.userRepository = userRepository;
    }

    public String validateAuthToken(HttpServletRequest httpServletRequest) {
        String token = SecurityUtils.extractAuthTokenFromRequest(httpServletRequest);

        Claims claims  = JwtUtils.extractClaimsFromToken(token);
        if(claims == null) {
            return null;
        }

        String tokenPrefix = SecurityUtils.setTokenPrefix(token);
        TokenEntity tokenEntity = tokenRepository.
                    findByUserEntity_IdUserAndTokenAndStatusTrue(Long.valueOf((Integer) claims.get("idUser")),tokenPrefix);

        if(tokenEntity == null) {
            try {
                String tokenSaved = saveAuthToken(tokenPrefix,Long.valueOf((Integer) claims.get("idUser")));
                return tokenSaved;
            } catch (Exception e) {
                return null;
            }
        } else {
            return null;
        }
    }

    public UserEntity getInternalUserEntity(final Long idUser, final HttpServletRequest httpServletRequest) {

        try {

            HttpHeaders headers = new HttpHeaders();
            RestTemplate restTemplate = new RestTemplate();

            headers.add("API_KEY", httpServletRequest.getHeader("API_KEY"));

            String uri = "http://localhost:8091/horizonte";

            LinkedHashMap<String, Object> requestBody = new LinkedHashMap<>();

            requestBody.put("idUser", idUser);

            HttpEntity<LinkedHashMap<String, Object>> request = new HttpEntity<>(requestBody, headers);

            ResponseEntity<String> responseEntity = restTemplate.exchange(Objects.requireNonNull(uri).
                    concat("/getUserEntityByIdUser"), HttpMethod.POST, request, String.class);

            if (!Objects.requireNonNull(responseEntity.getBody()).contains("ERROR")) {
                ObjectMapper objectMapper = new ObjectMapper();

                LinkedHashMap<String, Object> responseMap = objectMapper.readValue(responseEntity.getBody(), LinkedHashMap.class);
                LinkedHashMap<String, Object> userEntityExt = (LinkedHashMap<String, Object>) responseMap.get("userEntityExt");
                LinkedHashMap<String, Object> data = (LinkedHashMap<String, Object>) userEntityExt.get("data");

                String username = (String) data.get("userName");

                UserEntity userInternal = userRepository.findByUserName(username);

                return userInternal;

            } else {
                return null;
            }

        } catch (Exception e){
            return null;
        }

    }


    public Boolean checkTokenValid(HttpServletRequest httpServletRequest) {

        try {
            String token = SecurityUtils.extractAuthTokenFromRequest(httpServletRequest);

            Claims claims  = JwtUtils.extractClaimsFromToken(token);
            if(claims == null) {
                return null;
            }
            Long idUser = AuthService.getIdUserByToken(httpServletRequest);

            Boolean isValid = getTokenUserByIdUserAndValidate(idUser, httpServletRequest.getHeader("token"), claims);
            if(isValid) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    public String saveAuthToken(String token, Long idUser) {
        TokenEntity tokenEntity = new TokenEntity();
        OffsetDateTime expirationDate = JwtUtils.setExpirationDate();
        tokenEntity.setToken(token);
        tokenEntity.setUserEntity(new UserEntity(idUser));
        tokenEntity.setRegisterDate(OffsetDateTime.now(ZoneId.of("America/Caracas")));
        tokenEntity.setExpirationDate(expirationDate);
        tokenEntity.setStatus(true);

        tokenRepository.save(tokenEntity);
        return token;
    }

    public Boolean validateAdminUser(Long idUser) {
        AdministrativeUserEntity administrativeUserEntity = administrativeUserRepository.findByUserEntity_IdUserAndActiveTrue(idUser);
        if(administrativeUserEntity != null) {
            return true;
        } else {
            return false;
        }
    }

    public static Long getIdUserByToken(HttpServletRequest httpServletRequest){
        Claims claims = JwtUtils.extractClaims(httpServletRequest);
        return Objects.requireNonNull(claims).get("idUser", Long.class);
    }

    @Transactional
    public void invalidateLastTokenByUser(final Long idUser) {
        TokenEntity tokenEntity = tokenRepository.
                findTopByUserEntity_IdUserAndStatusIsTrueOrderByExpirationDateDesc(idUser);
        tokenRepository.deactivateTokenUser(tokenEntity.getIdUser());
    }

    public Boolean getTokenUserByIdUserAndValidate(final Long idUser, final String token, Claims claims) {
        TokenEntity tokenEntity = tokenRepository.
                findByUserEntity_IdUserAndTokenAndStatusTrueOrderByExpirationDateDesc(idUser, token);
        if(tokenEntity == null) {
            return false;
        }
        if(claims.getExpiration().before(new Date(System.currentTimeMillis()))) {
            return false;
        } else {
            return true;
        }

    }

    public String getRifByIdUser(final Long idUser) {

        ClientEntity clientEntity = clientRepository.findByIdUser(idUser);
        if (clientEntity == null) {
            return null;
        }

        CommerceEntity commerceEntity = clientEntity.getCommerceEntity();
        if (commerceEntity == null) {
            return null;
        }

        return commerceEntity.getCommerceDocument();
    }

}
