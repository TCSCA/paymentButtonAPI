package api.apiAdminCommerce.service;

import api.apiAdminCommerce.entity.ConfigurationEntity;
import api.apiAdminCommerce.entity.LicenseEntity;
import api.apiAdminCommerce.repository.intDbRepository.ConfigurationRepository;
import api.apiAdminCommerce.repository.intDbRepository.LicenceRepository;
import api.apiAdminCommerce.util.EmailGoogleService;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Multipart;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMultipart;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.Scanner;

@Service
public class ConfirmationCommerceEmailService {

    private final EmailGoogleService emailGoogleService;

    private final ConfigurationRepository configurationRepository;

    private final LicenceRepository licenceRepository;

    private final UserService userService;


    public ConfirmationCommerceEmailService(EmailGoogleService emailGoogleService, ConfigurationRepository configurationRepository, LicenceRepository licenceRepository, UserService userService) {
        this.emailGoogleService = emailGoogleService;

        this.configurationRepository = configurationRepository;
        this.licenceRepository = licenceRepository;
        this.userService = userService;
    }

    public Message sendConfirmationCommerceEmail(String sendTo, String fullname, String subject, String username,
                                                 String password,String licenceCode,final String token,final String apiKey)
            throws MessagingException, IOException {


        String body = "";

        MimeMultipart multipart = new MimeMultipart();

        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("templates/confirmationCommerceEmail.html");

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

        String emailSupport = userService.searchConfigurationById(11L,token,apiKey);

//        setImageToMailFromResources("logo_intelipay.png", multipart);
        emailGoogleService.setImageToMailFromURL("logo_intelipay.png",multipart,logoUrl);
        body = body.replace("$userName", username).replace("$fullName",fullname).replace("$password",password)
                .replace("$licenceCode",licenceCode).replace("$emailSupport",emailSupport);


        Message message= emailGoogleService.setMailInitialConfiguration(sendTo,body,subject,multipart);

//        helper.setFrom(from);
//        helper.setTo(sendTo);
//        helper.setText(body, true);
//        helper.setSubject(subject);
//
//        javaMailSender.send(msg);
        return message;
    }

    @Async("asyncExecutor")
    public void generateConfirmationCommerceEmail(final String sendTo,final String userName, final LicenseEntity licenseEntity,
                                                 final String fullName,final String token,final String apiKey) {

        Instant date = Instant.now().truncatedTo(ChronoUnit.SECONDS);
        ZoneId caracasAmerica = ZoneId.of("America/Caracas");
        ZonedDateTime issuedAt = ZonedDateTime.ofInstant(date, caracasAmerica);
        ZonedDateTime expiryDate = issuedAt.plus(2000, ChronoUnit.MINUTES);
        LocalDate expireTime = expiryDate.toOffsetDateTime().toLocalDate();
//        licenseEntityList = licenceRepository.findAll();
//        String subject = messageUtil.getMessageByKey("MSG-013").get("message");
        try {
            ConfigurationEntity configurationEntity =new ConfigurationEntity();
            configurationEntity.setIdConfiguration(1L);
            configurationEntity= configurationRepository.findConfigurationEntityByIdConfiguration(configurationEntity.getIdConfiguration());
            configurationEntity.setValue(configurationEntity.getValue());
            sendConfirmationCommerceEmail
                    (sendTo,fullName, "Bienvenido a Intelipay", userName,
                            configurationEntity.getValue(),licenseEntity.getLicenseCode(),token,apiKey);

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }

    /*@Async("asyncExecutor")
    public void sendConfirmationCommerceEmail(HttpServletRequest httpServletRequest, CommerceEntity commerceEntity) {
        LicenseEntity licenseEntity = licenceRepository.getLicenseEntityByCommerceEntity_IdCommerce(commerceEntity.getIdCommerce());
        generateConfirmationCommerceEmail(commerceEntity.getCommerceEmail(), commerceEntity.getCommerceDocument(),licenseEntity);

    }*/

    @Async("asyncExecutor")
    public void sendConfirmationCommerceEmailInt(final Long idCommerce, final String commerceEmail,
                                                 final String commerceDocument, final String fullName,final String token, final String apiKey) {
        LicenseEntity licenseEntity = licenceRepository.getLicenseEntityByCommerceEntity_IdCommerce(idCommerce);
        generateConfirmationCommerceEmail(commerceEmail, commerceDocument, licenseEntity, fullName,token,apiKey);

    }


    private  void setImageToMailFromResources(final String fileName, final Multipart multipart) throws MessagingException, IOException {

        final String path = "/static/img/";

        URL inputStreamUpdateProfileIcon = getClass().getResource(path.concat(fileName));

        MimeBodyPart mimeBodyPart = new MimeBodyPart();

        mimeBodyPart.attachFile(inputStreamUpdateProfileIcon.getFile());

        mimeBodyPart.setHeader("Content-ID", "<" + fileName + ">");

        multipart.addBodyPart(mimeBodyPart);
    }

    @Async("asyncExecutor")
    public void sendRejectCommerceEmail(final String sendTo, final String token, final String apiKey, final String subject,
                                        final String rif, final String companyName)
            throws MessagingException, IOException {


        String body = "";

        MimeMultipart multipart = new MimeMultipart();

        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("templates/rejectCommerceEmail.html");

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


        String emailSupport = userService.searchConfigurationById(11L,token,apiKey);

        body = body.replace("$emailSupport", emailSupport).replace("$rif",rif).replace("$companyName",companyName);

        emailGoogleService.setImageToMailFromURL("logo_intelipay.png",multipart,logoUrl);

        Message message= emailGoogleService.setMailInitialConfiguration(sendTo,body,subject,multipart);

    }
}
