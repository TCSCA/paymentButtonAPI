package api.preRegistro.util;


import api.preRegistro.entity.ConfigurationEntity;
import api.preRegistro.repository.ConfigurationRepository;
import api.preRegistro.service.GmailApiService;
import com.google.api.client.auth.oauth2.TokenResponse;
import jakarta.activation.DataHandler;
import jakarta.activation.DataSource;
import jakarta.activation.URLDataSource;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import jakarta.mail.util.ByteArrayDataSource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Properties;

@Service
public class EmailGoogleService {

    private final Environment environment;

    private final GmailApiService gmailApiService;

    private final ConfigurationRepository configurationRepository;

    public EmailGoogleService(Environment environment, GmailApiService gmailApiService, ConfigurationRepository configurationRepository) {
        this.environment = environment;
        this.gmailApiService = gmailApiService;
        this.configurationRepository = configurationRepository;
    }


    public Message setMailInitialConfiguration(final String [] emailDestination , final String body, final String subject, final Multipart multipart) throws MessagingException {
        try {
            MimeBodyPart mimeBodyPart = new MimeBodyPart();

            // Obtén la configuración de Gmail
            ConfigurationEntity configurationGoogleRefreshTokenEntity = configurationRepository.findByPassword("googleRefreshToken");
            ConfigurationEntity configurationEmail = configurationRepository.findByPassword("emailServicesGoogle");
            String email = configurationEmail.getValue();

            // Obtén el token de acceso a través de OAuth
            TokenResponse token = gmailApiService.refreshAccessToken(configurationGoogleRefreshTokenEntity.getValue());

            Properties props = new Properties();

            // Configuración para SMTP usando OAuth 2.0
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", "smtp.gmail.com");
            props.put("mail.smtp.port", "587");
            props.put("mail.smtp.ssl.trust", "smtp.gmail.com");


            // Propiedades específicas para autenticación con OAuth 2.0 en SMTP
            props.put("mail.smtp.sasl.enable", "true");
            props.put("mail.smtp.sasl.mechanisms", "XOAUTH2");

            Session session = Session.getInstance(props);


            String adressList = "";

            if (emailDestination.length > 1) {
                adressList = String.join(",", emailDestination);
            } else {
                adressList = emailDestination[0];
            }

            // Crea el mensaje
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(email));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(adressList, true));
            message.setSubject(subject);
            message.setSubject(subject);
            mimeBodyPart.setContent(body, "text/html; charset=utf-8");
            multipart.addBodyPart(mimeBodyPart);

            // Configura el contenido del mensaje
            message.setContent(multipart);

            // Autenticación con el token de acceso
            Transport transport = session.getTransport("smtp");
            transport.connect("smtp.gmail.com", email, token.getAccessToken());
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();

            return message;

        } catch (MessagingException e) {
            throw new RuntimeException(e);
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
