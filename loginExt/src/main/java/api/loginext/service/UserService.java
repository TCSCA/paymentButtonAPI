package api.loginext.service;

import api.loginext.entity.HistoryPasswordEntity;
import api.loginext.entity.UserEntity;
import api.loginext.repository.HistoryPasswordRepository;
import api.loginext.repository.UserRepository;
import api.loginext.util.Response;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    private final HistoryPasswordRepository historyPasswordRepository;
    public UserService(UserRepository userRepository, HistoryPasswordRepository historyPasswordRepository) {
        this.userRepository = userRepository;
        this.historyPasswordRepository = historyPasswordRepository;
    }

    public Optional<UserEntity> getUserByUsername(String username) {
       return userRepository.findByUserName(username);
    }

    public Response setResetPasswordUser(Long idUser, String password, Integer days) {

        UserEntity userEntity;
        Response response;
        HistoryPasswordEntity historyPasswordEntity = new HistoryPasswordEntity();
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        userEntity = userRepository.findByIdUser(idUser);

        if (historyPassword(password, idUser)) {
            return new Response("ERROR","La contraseña debe ser diferente a las últimas 5 registradas");
        }

        historyPasswordEntity.setPassword(passwordEncoder.encode(password));
        historyPasswordEntity.setUserEntity(new UserEntity(userEntity.getIdUser()));
        historyPasswordEntity.setRegisterDate(OffsetDateTime.now(ZoneId.of("America/Caracas")));
        OffsetDateTime expirationDate = setExpirationDate(days);
        historyPasswordEntity.setExipartionDate(expirationDate);
        historyPasswordRepository.save(historyPasswordEntity);

        userEntity.setTemporalPassword(null);
        userRepository.save(userEntity);
        return new Response("SUCCESS", "Usuario actualizado exitosamente");
    }

    public boolean historyPassword(String password, Long idUser) {

        List<HistoryPasswordEntity> historyPasswordEntities = historyPasswordRepository.getLastPasswordFromIdUser(PageRequest.of(0,5), idUser);

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        for (HistoryPasswordEntity historyPassword : historyPasswordEntities) {
            if (passwordEncoder.matches(password,historyPassword.getPassword())) {
                return true;
            }
        }
        return false;
    }
    public OffsetDateTime setExpirationDate(Integer days) {
        OffsetDateTime currentDate = OffsetDateTime.now(ZoneId.of("America/Caracas")).plusDays(days);
        return currentDate;
    }

    public String searchConfigurationById(final Long idConfiguration, final String apiKey, final String token) throws JsonProcessingException {

        HttpHeaders headers = new HttpHeaders();
        RestTemplate restTemplate = new RestTemplate();

        headers.add("API_KEY", apiKey);
        headers.add("token", token);

        String uri = "http://localhost:8091/horizonte";

        LinkedHashMap<String, Object> requestBody = new LinkedHashMap<>();

        requestBody.put("idConfiguration", idConfiguration);

        HttpEntity<LinkedHashMap<String, Object>> request = new HttpEntity<>(requestBody, headers);

        ResponseEntity<String> responseEntity = restTemplate.exchange(uri.
                concat("/searchConfigurationById"), HttpMethod.POST, request, String.class);

        if (!Objects.requireNonNull(responseEntity.getBody()).contains("ERROR")) {

            ObjectMapper objectMapper = new ObjectMapper();

            LinkedHashMap<String, Object> responseMap = objectMapper.readValue(responseEntity.getBody(), LinkedHashMap.class);

            String configurationValue = objectMapper.convertValue(responseMap.get("configurationValue"), String.class);

            return configurationValue;

        } else {
            return null;
        }

    }
}
