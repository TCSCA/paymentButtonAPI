package api.preRegistro.service;


import api.preRegistro.util.EmailGoogleService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Multipart;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMultipart;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Objects;
import java.util.Scanner;

@Service
public class PreRegisterEmailService {

    private final EmailGoogleService emailGoogleService;

    private final Environment environment;

    public PreRegisterEmailService(EmailGoogleService emailGoogleService, Environment environment) {
        this.emailGoogleService = emailGoogleService;
        this.environment = environment;
    }

    @Async("asyncExecutor")
    public void generatePreregisterEmail(String sendTo,String sendTo2,String fullName,String apikey) {

        String[] emailDestinations = new String[0];

        for (String e : new String[]{sendTo2}) {
            if (e != null) {
                emailDestinations = Arrays.copyOf(emailDestinations, emailDestinations.length + 1);
                emailDestinations[emailDestinations.length - 1] = e;
            }
        }

        Instant date = Instant.now().truncatedTo(ChronoUnit.SECONDS);
        ZoneId caracasAmerica = ZoneId.of("America/Caracas");
        ZonedDateTime issuedAt = ZonedDateTime.ofInstant(date, caracasAmerica);
        ZonedDateTime expiryDate = issuedAt.plus(2000, ChronoUnit.MINUTES);
        LocalDate expireTime = expiryDate.toOffsetDateTime().toLocalDate();
        String subject = "Intelipay-Recaudos";

        try {

            sendPreRegisterEmail
                    (emailDestinations,"Inteligensa", subject, fullName,apikey);

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }

    @Async("asyncExecutor")
    public Message sendPreRegisterEmail(String [] emailDestinations,  String platform, String subject, String fullName,String apikey)
            throws MessagingException, IOException {

        String body = "";

        Multipart multipart = new MimeMultipart();

        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("templates/preRegisterCommerce.html");

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
        String emailSupport = searchConfigurationById(11L,apikey);

        body = body.replace("$platform", platform).replace( "$fullName",fullName).replace( "$emailSupport",emailSupport);


//        setImageToMailFromResources("logo_intelipay.png", multipart);

        String logoUrl = searchConfigurationById(10L,apikey);

        emailGoogleService.setImageToMailFromURL("logo_intelipay.png",multipart,logoUrl);

        return emailGoogleService.setMailInitialConfiguration(emailDestinations,body,subject,multipart);
    }

    private  void setImageToMailFromResources(final String fileName, final Multipart multipart) throws MessagingException, IOException {

        final String path = "/static/img/";

        URL inputStreamUpdateProfileIcon = getClass().getResource(path.concat(fileName));

        MimeBodyPart mimeBodyPart = new MimeBodyPart();

        mimeBodyPart.attachFile(inputStreamUpdateProfileIcon.getFile());

        mimeBodyPart.setHeader("Content-ID", "<" + fileName + ">");

        multipart.addBodyPart(mimeBodyPart);
    }

    public String searchConfigurationById(final Long idConfiguration,final String apiKey) throws JsonProcessingException {

        HttpHeaders headers = new HttpHeaders();
        RestTemplate restTemplate = new RestTemplate();

        headers.add("API_KEY", apiKey);
//        headers.add("token", token);

        String uri = environment.getProperty("app.route.external");;

        LinkedHashMap<String, Object> requestBody = new LinkedHashMap<>();

        requestBody.put("idConfiguration", idConfiguration);

        HttpEntity<LinkedHashMap<String, Object>> request = new HttpEntity<>(requestBody, headers);

        ResponseEntity<String> responseEntity = restTemplate.exchange(uri.
                concat("/searchConfigurationById"), HttpMethod.POST, request, String.class);

        if (!Objects.requireNonNull(responseEntity.getBody()).contains("ERROR")) {

            ObjectMapper objectMapper = new ObjectMapper();

            LinkedHashMap<String, Object> responseMap = objectMapper.readValue(responseEntity.getBody(), LinkedHashMap.class);

            String configurationValue = objectMapper.convertValue(responseMap.get("configurationValue"), String.class);

//            logger.info("Configuration search successfully");
            return configurationValue;

        } else {
//            logger.error("Response Status: " + responseEntity.getStatusCode());
            return null;
        }

    }
}
