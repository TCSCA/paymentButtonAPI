package api.loginext.service;

import api.loginext.entity.PreRegistroEntity;
import api.loginext.entity.ProfileEntity;
import api.loginext.entity.UserEntity;
import api.loginext.repository.CommerceRepository;
import api.loginext.repository.UserRepository;
import api.loginext.util.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import jakarta.activation.DataHandler;
import jakarta.activation.DataSource;
import jakarta.activation.URLDataSource;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Multipart;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMultipart;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.time.OffsetDateTime;
import java.util.*;

@Service
public class EmailService {

    private final EmailGoogleService emailGoogleService;

    private final UserService userService;

    public EmailService(EmailGoogleService emailGoogleService, UserService userService) {
        this.emailGoogleService = emailGoogleService;
        this.userService = userService;
    }


    public Response generateForgotPasswordEmail(final String username, HttpServletRequest httpServletRequest
            ,final String token,final String apiKey) {

        try {

            String resetPassword = generatePassword();
            HttpHeaders headers = new HttpHeaders();
            ObjectMapper objectMapper = new ObjectMapper();
            RestTemplate restTemplate = new RestTemplate();

            headers.add("API_KEY", apiKey);

            String uri = "http://localhost:8091/horizonte";

            LinkedHashMap<String, Object> requestBody = new LinkedHashMap<>();

            requestBody.put("username", username);
            requestBody.put("resetPassword", resetPassword);



            HttpEntity<LinkedHashMap<String, Object>> request = new HttpEntity<>(requestBody, headers);

            ResponseEntity<String> responseEntity = restTemplate.exchange(uri.
                    concat("/getUserEntityByUsername"), HttpMethod.POST, request, String.class);

            if (!Objects.requireNonNull(responseEntity.getBody()).contains("ERROR")) {
                LinkedHashMap<String, Object> responseMap = objectMapper.readValue(responseEntity.getBody(), LinkedHashMap.class);
                LinkedHashMap<String, Object> userEntityExt = (LinkedHashMap<String, Object>) responseMap.get("userEntityExt");
                LinkedHashMap<String, Object> data = (LinkedHashMap<String, Object>) userEntityExt.get("data");

                String sendTo= (String) data.get("email");

                sendForgotPasswordEmail
                    (sendTo,"Intelipay-Soporte", resetPassword,token,apiKey,username);

            } else {
                return new Response("ERROR", "Usuario inválido o no existe");
            }


        } catch (Exception e){
            return new Response("ERROR",e.getMessage());
        }

        return new Response("SUCCESS","Transacción Exitosa");
    }

//    @Async("asyncExecutor")
    public Message sendForgotPasswordEmail(final String emailDestinations,final String subject
            ,final String resetPassword,final String token,final String apiKey,final String username)
            throws MessagingException, IOException {

        String body = "";

        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("templates/forgotPassword.html");

        Multipart multipart = new MimeMultipart();

        if (inputStream != null) {

            try (Scanner scanner = new Scanner(inputStream, "UTF-8")) {
                StringBuilder contentBuilder = new StringBuilder();
                while (scanner.hasNextLine()) {
                    contentBuilder.append(scanner.nextLine());
                    contentBuilder.append(System.lineSeparator());
                }
                body = contentBuilder.toString();

            }
        }

        String logoUrl= userService.searchConfigurationById(10L,token,apiKey);

        setImageToMailFromURL("logo_intelipay.png",multipart,logoUrl);

        body = body.replace("$platform", subject).replace("$temporalPassword", resetPassword)
                .replace("$username", username);

        return emailGoogleService.setMailInitialConfiguration(emailDestinations,body,subject,multipart);
    }

    private static final String LETTERS = "abcdefghijklmnopqrstuvwxyz";
    private static final String UPPERCASE_LETTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String DIGITS = "0123456789";
    private static final String SPECIAL_CHARACTERS = ".,_-#";
    private static final int PASSWORD_LENGTH = 8;

    public String generatePassword() {
        Random random = new Random();
        StringBuilder password = new StringBuilder(PASSWORD_LENGTH);
        Set<Integer> requiredPositions = new HashSet<>();

        // Fill the StringBuilder with placeholder characters
        for (int i = 0; i < PASSWORD_LENGTH; i++) {
            password.append(' ');
        }

        // Ensure there is at least one special character
        int specialCharPosition = random.nextInt(PASSWORD_LENGTH);
        requiredPositions.add(specialCharPosition);
        password.setCharAt(specialCharPosition, SPECIAL_CHARACTERS.charAt(random.nextInt(SPECIAL_CHARACTERS.length())));

        // Ensure there is at least one digit
        int digitPosition;
        do {
            digitPosition = random.nextInt(PASSWORD_LENGTH);
        } while (requiredPositions.contains(digitPosition));
        requiredPositions.add(digitPosition);
        password.setCharAt(digitPosition, DIGITS.charAt(random.nextInt(DIGITS.length())));

        // Ensure there is at least one uppercase letter
        int uppercasePosition;
        do {
            uppercasePosition = random.nextInt(PASSWORD_LENGTH);
        } while (requiredPositions.contains(uppercasePosition));
        requiredPositions.add(uppercasePosition);
        password.setCharAt(uppercasePosition, UPPERCASE_LETTERS.charAt(random.nextInt(UPPERCASE_LETTERS.length())));

        // Fill the remaining positions with letters, digits, or special characters
        for (int i = 0; i < PASSWORD_LENGTH; i++) {
            if (!requiredPositions.contains(i)) {
                String combinedCharacters = LETTERS + DIGITS + SPECIAL_CHARACTERS;
                password.setCharAt(i, combinedCharacters.charAt(random.nextInt(combinedCharacters.length())));
            }
        }

        return password.toString();
    }


    public void setImageToMailFromURL(final String fileName, final Multipart multipart, final String logo) throws MessagingException, IOException, IOException {

        URL imageUrl = new URL(logo);
        DataSource dataSource = new URLDataSource(imageUrl);
        MimeBodyPart mimeBodyPart = new MimeBodyPart();
        mimeBodyPart.setDataHandler(new DataHandler(dataSource));
        mimeBodyPart.setHeader("Content-ID", "<" + fileName + ">");

        multipart.addBodyPart(mimeBodyPart);

    }
}
