package api.apic2p.util;

import api.apic2p.entity.ConfigurationEntity;
import api.apic2p.repository.ConfigurationRepository;
import api.apic2p.service.GmailApiService;
import com.google.api.client.auth.oauth2.TokenResponse;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URL;
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


    public Message setMailInitialConfiguration(final String emailDestination, final String body, final String subject, final Multipart multipart) throws MessagingException {
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

            // Crea el mensaje
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(email));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(emailDestination, true));
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


}
