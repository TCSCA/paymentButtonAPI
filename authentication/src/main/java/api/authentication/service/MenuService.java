package api.authentication.service;

import api.authentication.projection.MenuProjection;
import api.authentication.projection.MenuTransaccionProjection;
import api.authentication.repository.MenuRepository;
import api.authentication.security.jwt.JwtUtils;
import api.authentication.to.ApprovalUserTo;
import api.authentication.to.MenuTo;
import api.authentication.to.MenuTransaccionTo;
import api.authentication.to.TermsAndConditionsTo;
import api.authentication.util.Response;
import api.authentication.utilFile.FileStorageProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class MenuService {

    private static final Logger logger = LoggerFactory.getLogger(MenuService.class);

    private final MenuRepository menuRepository;

    private final Path fileStorageLocation;


    public MenuService(MenuRepository menuRepository, FileStorageProperties fileStorageProperties) {
        this.menuRepository = menuRepository;
        this.fileStorageLocation = Paths.get(fileStorageProperties.getUploadDir())
                .toAbsolutePath().normalize();
    }

    public Response getMenuByIdProfile(final Long idProfile) {

        List<MenuTransaccionProjection> menuEntities;
        List<MenuTransaccionTo> menuTos = new ArrayList<>();

        menuEntities = menuRepository.findMenuEntityByIdProfile(idProfile);

        List<MenuTransaccionTo> menuTransaccionTos = menuEntities.stream()
                .map(projection -> new MenuTransaccionTo(projection.getIdMenu(), projection.getOpcionMenu(),
                        projection.getOrden(), projection.getIcon())).toList();

        for (MenuTransaccionTo menuTransaccionTo : menuTransaccionTos){
            List<MenuProjection> menu = menuRepository.findByIdMenuPadre(menuTransaccionTo.getIdMenu(), idProfile);

            List<MenuTo> menuHijos = menu.stream()
                    .map(projection -> new MenuTo(projection.getIdMenu(), projection.getOpcionMenu(),
                            projection.getOrden(), projection.getUrl(), projection.getIcon(),
                            projection.getIdSeccion())).toList();

            menuTransaccionTo.setMenuToList(menuHijos);

            menuTos.add(menuTransaccionTo);
        }

        if (menuTos.size() == 0){
            return new Response("ERROR", menuTos);
        }

        return new Response("SUCCESS", menuTos);
    }

    public List<ApprovalUserTo> getApprovalStatusByUser(final Long idUser, final String apiKey) {

        try {

            HttpHeaders headers = new HttpHeaders();
            RestTemplate restTemplate = new RestTemplate();

            headers.add("API_KEY", apiKey);

            String uri = "http://localhost:8090/core";

            HttpEntity<LinkedHashMap<String, Object>> request = new HttpEntity<>(headers);

            ResponseEntity<String> responseEntity = restTemplate.exchange(Objects.requireNonNull(uri).
                    concat("/getStatusApprovalByUser/"+idUser), HttpMethod.GET, request, String.class);

            ObjectMapper objectMapper = new ObjectMapper();

            LinkedHashMap<String, Object> responseMap = objectMapper.readValue(responseEntity.getBody(), LinkedHashMap.class);

            List<LinkedHashMap<String, Object>> approvalUserToList =
                    (List<LinkedHashMap<String, Object>>) responseMap.get("approvalUserToList");

            if (approvalUserToList != null){

                if (approvalUserToList.get(0).get("urlFile") == null){

                    logger.info("Usuario administrador Intellipay ingresando");

                    return approvalUserToList.stream().map(map -> {
                        ApprovalUserTo approvalUserTo = new ApprovalUserTo();
                        approvalUserTo.setStatus((Boolean) map.get("approvalStatus"));
                        approvalUserTo.setUrl(null);
                        approvalUserTo.setName(null);
                        return approvalUserTo;
                    }).collect(Collectors.toList());

                } else {

                    return approvalUserToList.stream().map(map -> {
                        ApprovalUserTo approvalUserTo = new ApprovalUserTo();
                        approvalUserTo.setStatus((Boolean) map.get("approvalStatus"));
                        approvalUserTo.setUrl((String) map.get("urlFile"));
                        approvalUserTo.setName((String) map.get("fileName"));
                        return approvalUserTo;
                    }).collect(Collectors.toList());

                }

            } else {
                logger.error("ERROR: " + responseEntity.getBody());
                return null;
            }

        } catch (Exception e){
            logger.error(e.getMessage(), e);
            return null;
        }

    }

    public Resource loadFileAsResource(String urlFile) throws Exception {
        Path baseDirectory = Paths.get(fileStorageLocation.toString());

        Path filePath = baseDirectory.resolve(urlFile).normalize();

        Resource resource = new UrlResource(filePath.toUri());

        if (resource.exists() && resource.isReadable()) {
            return resource;
        } else {
            return null;
        }
    }

    public Response getTermsAndConditions(final HttpServletRequest httpServletRequest) {

        try {

            HttpHeaders headers = new HttpHeaders();
            RestTemplate restTemplate = new RestTemplate();

            headers.add("API_KEY", httpServletRequest.getHeader("API_KEY"));

            String uri = "http://localhost:8090/core";

            HttpEntity<LinkedHashMap<String, Object>> request = new HttpEntity<>(headers);

            ResponseEntity<String> responseEntity = restTemplate.exchange(Objects.requireNonNull(uri).
                    concat("/getTermsAndConditions"), HttpMethod.GET, request, String.class);

            ObjectMapper objectMapper = new ObjectMapper();

            LinkedHashMap<String, Object> responseMap = objectMapper.readValue(responseEntity.getBody(), LinkedHashMap.class);

            List<LinkedHashMap<String, Object>> termsAndConditionsEntities =
                    (List<LinkedHashMap<String, Object>>) responseMap.get("termsAndConditionsEntityList");

            List<TermsAndConditionsTo> termsAndConditionsEntityList = termsAndConditionsEntities.stream().map(map -> {
                TermsAndConditionsTo termsAndConditionsTo = new TermsAndConditionsTo();
                termsAndConditionsTo.setUrlFile((String) map.get("urlFile"));
                termsAndConditionsTo.setType((String) map.get("fileName"));
                termsAndConditionsTo.setStatus((Boolean) map.get("status"));
                return termsAndConditionsTo;
            }).collect(Collectors.toList());

            return new Response("SUCCESS", termsAndConditionsEntityList);

        } catch (Exception e){
            logger.error(e.getMessage(), e);
            return null;
        }

    }

    public Response setApprovalUserStatus(final Long idUser,
            final List<String> termsOrPolicies, final HttpServletRequest httpServletRequest) {

        try {

            HttpHeaders headers = new HttpHeaders();
            RestTemplate restTemplate = new RestTemplate();

            headers.add("API_KEY", httpServletRequest.getHeader("API_KEY"));

            String uri = "http://localhost:8090/core";

            LinkedHashMap<String, Object> requestBody = new LinkedHashMap<>();

            requestBody.put("idUser", idUser);
            requestBody.put("termsOrPolicies", termsOrPolicies);

            HttpEntity<LinkedHashMap<String, Object>> request = new HttpEntity<>(requestBody, headers);

            ResponseEntity<String> responseEntity = restTemplate.exchange(Objects.requireNonNull(uri).
                    concat("/setApprovalUserStatus"), HttpMethod.POST, request, String.class);

            if (!Objects.requireNonNull(responseEntity.getBody()).contains("ERROR")) {
                return new Response("SUCCESS", "Transacción exitosa");
            } else {
                return new Response("ERROR", responseEntity.getBody());
            }

        } catch (Exception e){
            logger.error(e.getMessage(), e);
            return new Response("ERROR", e.getMessage());
        }

    }

    public Long getIdUserByToken(HttpServletRequest httpServletRequest){
        Claims claims = JwtUtils.extractClaims(httpServletRequest);
        return Objects.requireNonNull(claims).get("idUser", Long.class);
    }
}
