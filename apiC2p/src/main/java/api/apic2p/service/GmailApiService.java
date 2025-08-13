package api.apic2p.service;

import api.apic2p.util.GmailCredential;
import api.apic2p.util.GoogleTokenResponse;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.services.gmail.model.Message;
import jakarta.activation.DataHandler;
import jakarta.mail.BodyPart;
import jakarta.mail.MessagingException;
import jakarta.mail.Multipart;
import jakarta.mail.Session;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import jakarta.mail.util.ByteArrayDataSource;
import org.apache.commons.codec.binary.Base64;
import org.apache.pdfbox.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.util.Properties;

@Service
public class GmailApiService {

    private HttpTransport httpTransport;

    private GmailCredential gmailCredential;
    @Value("#{@getClientId}")
    private String clientId;
    @Value("#{@getClientSecret}")
    private String secretKey;
    @Value("#{@getRefreshToken}")
    private String refreshToken;
    @Value("#{@getFromEmail}")
    private String fromEmail;

    public GmailApiService() throws GeneralSecurityException, IOException {

        this.httpTransport = GoogleNetHttpTransport.newTrustedTransport();

        this.gmailCredential = new GmailCredential(
                clientId,
                secretKey,
                refreshToken,
                null,
                null,
                fromEmail
        );

    }

    /*public boolean sendMessage(
            String toEmail,
            String subject,
            String body,
            MultipartFile attachment) throws MessagingException, IOException {

        //refreshAccessToken();

        Message message = createMessageWithEmail(
                createEmail(toEmail, gmailCredential.getUserEmail(), subject, body, attachment));

        return createGmail()
                .users()
                .messages()
                .send(gmailCredential.getUserEmail(), message)
                .execute()
                .getLabelIds()
                .contains("SENT");

    }*/

    /*private Gmail createGmail() {

        Credential credential = authorize();

        return new Gmail.Builder(httpTransport, JSON_FACTORY, credential)
                .build();

    }*/

    private MimeMessage createEmail(
            String to,
            String from,
            String subject,
            String bodyText,
            MultipartFile attachment) throws MessagingException {

        MimeMessage email = new MimeMessage(Session.getDefaultInstance(new Properties(), null));

        email.setFrom(new InternetAddress(from));

        email.addRecipient(jakarta.mail.Message.RecipientType.TO, new InternetAddress(to));

        email.setSubject(subject);

        email.setText(bodyText);

        // This mail has 2 part, the BODY and the embedded image
        MimeMultipart multipart = new MimeMultipart("related");

        // first part (the html)
        BodyPart messageBodyPart = new MimeBodyPart();

        messageBodyPart.setContent(bodyText, "text/html");
        // add it
        multipart.addBodyPart(messageBodyPart);

        // second part (the image)
        messageBodyPart = new MimeBodyPart();
        InputStream imageStream = new ByteArrayInputStream( "../static/img/welcomeIcon.png".getBytes() );

        ByteArrayDataSource fds = null;
        try {
            fds = new ByteArrayDataSource(IOUtils.toByteArray(imageStream), "image/png");
            //fds = new FileDataSource(ResourceUtils.getFile("../static/img/welcomeIcon.png").getCanonicalPath());
        } catch (Exception e) {
            e.printStackTrace();
        }
        messageBodyPart.setDataHandler(new DataHandler(fds));
        messageBodyPart.setHeader("Content-ID", "<welcomeIcon.png>");

        // add image to the multipart
        multipart.addBodyPart(messageBodyPart);

        // put everything together
        email.setContent(multipart);

        //email = addAttachmentToEmail(email, bodyText, attachment);

        return email;

    }

    private MimeMessage addAttachmentToEmail(MimeMessage email, String bodyText,
                                             MultipartFile attachment) {

        if (attachment == null) {
            return email;
        }

        try {

            Multipart multipart = new MimeMultipart();

            MimeBodyPart mimeBodyPart = new MimeBodyPart();

            mimeBodyPart.setContent(bodyText, "text/html");

            multipart.addBodyPart(mimeBodyPart);

            mimeBodyPart = new MimeBodyPart();

            ByteArrayDataSource ds = new ByteArrayDataSource(attachment.getBytes(), attachment.getContentType());
            mimeBodyPart.setDataHandler(new DataHandler(ds));
            mimeBodyPart.setFileName(attachment.getOriginalFilename());

            multipart.addBodyPart(mimeBodyPart);

            email.setContent(multipart);

        } catch (Exception e) {

            e.printStackTrace();

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Not able to process request. ERROR: " + e);

        }

        return email;

    }

    private Message createMessageWithEmail(MimeMessage emailContent)
            throws MessagingException, IOException {

        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        emailContent.writeTo(buffer);

        return new Message()
                .setRaw(Base64.encodeBase64URLSafeString(buffer.toByteArray()));
    }

    /*private Credential authorize() {

        try {

            //TokenResponse tokenResponse = refreshAccessToken();

            return new Credential(BearerToken.authorizationHeaderAccessMethod()).setFromTokenResponse(
                    tokenResponse);

        } catch (Exception e) {

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Not able to process request.");

        }

    }*/

    public TokenResponse refreshAccessToken(final String refreshToken) {

        RestTemplate restTemplate = new RestTemplate();

        GmailCredential gmailCredentialsDto = new GmailCredential(
                clientId,
                secretKey,
                refreshToken,
                "refresh_token",
                null,
                null
        );

        HttpEntity<GmailCredential> entity = new HttpEntity(gmailCredentialsDto);

        try {

            GoogleTokenResponse response = restTemplate.postForObject(
                    "https://www.googleapis.com/oauth2/v4/token",
                    entity,
                    GoogleTokenResponse.class);

            gmailCredential = new GmailCredential(
                    clientId,
                    secretKey,
                    refreshToken,
                    null,
                    response.getAccessToken(),
                    fromEmail
            );

            return response;

        } catch (Exception e) {

            e.printStackTrace();

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Not able to process request. ERROR: " + e);

        }
    }

}
