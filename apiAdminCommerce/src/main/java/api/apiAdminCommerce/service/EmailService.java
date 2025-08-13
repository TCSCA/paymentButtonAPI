package api.apiAdminCommerce.service;

import api.apiAdminCommerce.util.EmailGoogleService;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Multipart;
import jakarta.mail.internet.MimeMultipart;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.Scanner;

@Service
public class EmailService {

    private final UserService userService;

    private final EmailGoogleService emailGoogleService;

    public EmailService(UserService userService, EmailGoogleService emailGoogleService) {
        this.userService = userService;
        this.emailGoogleService = emailGoogleService;
    }

    public String getFileContent(
            InputStream fis,
            String encoding) throws IOException {
        try (BufferedReader br =
                     new BufferedReader(new InputStreamReader(fis, encoding))) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
                sb.append('\n');
            }
            return sb.toString();
        }
    }


    @Async("asyncExecutor")
    public void sendWelcomeEmail(String fullName, String username, String password
            , String sendTo, final String apiKey, final String token)
            throws MessagingException, IOException {


        String body = "";

        String subject = "Bienvenido a Intelipay";

        MimeMultipart multipart = new MimeMultipart();

        String emailSupport = userService.searchConfigurationById(8L, apiKey, token);


        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("templates/welcomeEmail.html");

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

        String logoUrl = userService.searchConfigurationById(10L,apiKey,token);

        emailGoogleService.setImageToMailFromURL("logo_intelipay.png",multipart,logoUrl);
        body = body
                .replace("$fullName", fullName)
                .replace("$userName",username)
                .replace("$password",password)
                .replace("$emailSupport",emailSupport);

        Message message= emailGoogleService.setMailInitialConfiguration(sendTo,body,subject,multipart);

    }
}
