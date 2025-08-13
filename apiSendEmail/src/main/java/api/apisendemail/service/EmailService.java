package api.apisendemail.service;

import api.logsClass.LogsClass;
import api.logsClass.ManageLogs;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.activation.DataHandler;
import jakarta.activation.DataSource;
import jakarta.activation.URLDataSource;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.*;

@Service
public class EmailService {

    private final OAuth2AccessTokenService tokenService;

    private final ManageLogs manageLogs;

    public EmailService(OAuth2AccessTokenService tokenService, ManageLogs manageLogs) {
        this.tokenService = tokenService;
        this.manageLogs = manageLogs;
    }

    public void sendEmail(String username, String htmlTemplatePath, Map<String, String> placeholders,
                          LogsClass logTo, HttpServletRequest httpServletRequest, String apiKey,
                          String logoUrl) throws JsonProcessingException {

        String sendTo = "";
        String hostEmail = "";
        HttpHeaders headers = new HttpHeaders();
        ObjectMapper objectMapper = new ObjectMapper();
        RestTemplate restTemplate = new RestTemplate();

        headers.add("API_KEY", apiKey);

        String uri = "http://localhost:8091/horizonte";

        LinkedHashMap<String, Object> requestBody = new LinkedHashMap<>();

        requestBody.put("username", username);
        requestBody.put("resetPassword", placeholders.get("$temporalPassword"));

        HttpEntity<LinkedHashMap<String, Object>> request = new HttpEntity<>(requestBody, headers);

        ResponseEntity<String> responseEntity = restTemplate.exchange(uri.
                concat("/getUserEntityByUsername"), HttpMethod.POST, request, String.class);

        if (!Objects.requireNonNull(responseEntity.getBody()).contains("ERROR")) {
            LinkedHashMap<String, Object> responseMap = objectMapper.readValue(responseEntity.getBody(), LinkedHashMap.class);
            LinkedHashMap<String, Object> userEntityExt = (LinkedHashMap<String, Object>) responseMap.get("userEntityExt");
            LinkedHashMap<String, Object> data = (LinkedHashMap<String, Object>) userEntityExt.get("data");

            sendTo = (String) data.get("email");
            hostEmail = responseMap.get("hostEmail").toString();

        } else {
            manageLogs.errorLogger(logTo, httpServletRequest, "Error usuario no existe",
                    "user", false);
        }

        String accessToken = tokenService.getAccessToken(logTo, httpServletRequest, apiKey);

        // Configuración SMTP
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth.mechanisms", "XOAUTH2");

        Session session = Session.getInstance(props, null);

        try {
            // Cargar plantilla HTML desde la ruta proporcionada
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream(htmlTemplatePath);
            if (inputStream == null) {
                throw new RuntimeException("No se encontró la plantilla en la ruta: " + htmlTemplatePath);
            }

            String body;
            try (Scanner scanner = new Scanner(inputStream, "UTF-8")) {
                StringBuilder contentBuilder = new StringBuilder();
                while (scanner.hasNextLine()) {
                    contentBuilder.append(scanner.nextLine()).append(System.lineSeparator());
                }
                body = contentBuilder.toString();
            }

            // Reemplazar placeholders en la plantilla
            for (Map.Entry<String, String> entry : placeholders.entrySet()) {
                body = body.replace(entry.getKey(), entry.getValue());
            }

            // Crear mensaje de correo
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(hostEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(sendTo));
            message.setSubject("Notificación de Intelipay");

            // Configurar contenido HTML
            MimeBodyPart htmlPart = new MimeBodyPart();
            htmlPart.setContent(body, "text/html; charset=utf-8");

            // Adjuntar partes al mensaje
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(htmlPart);

            message.setContent(multipart);

            setImageToMailFromURL("logo_intelipay.png",multipart,logoUrl);

            // Enviar correo
            Transport transport = session.getTransport("smtp");
            transport.connect("smtp.gmail.com", hostEmail, accessToken);
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();

            manageLogs.infoLogger(logTo, httpServletRequest,
                    "Successfully: correo enviado exitosamente",
                    "user", false);

            System.out.println("Correo enviado exitosamente");
        } catch (Exception e) {
            manageLogs.severeErrorLogger(logTo, httpServletRequest,
                    "Error: " + logTo.getMethodName() + ": " + e,
                    e, "user", false);

            throw new RuntimeException("Error al enviar el correo: " + e.getMessage(), e);
        }

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
