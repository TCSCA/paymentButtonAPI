package api.apiP2c.service;

import api.apiP2c.entity.BankEntity;
import api.apiP2c.repository.BankRepository;
import api.apiP2c.request.ManualPaymentRequest;
import api.apiP2c.request.P2cRequest;
import api.apiP2c.response.P2cBankResponse;
import api.apiP2c.response.ResponseP2C;
import api.apiP2c.to.CommerceBankInformationTo;
import api.apiP2c.util.EmailGoogleService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.activation.DataHandler;
import jakarta.activation.DataSource;
import jakarta.activation.URLDataSource;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Multipart;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMultipart;
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
import java.util.LinkedHashMap;
import java.util.Objects;
import java.util.Scanner;

@Service
public class EmailService {

    private final EmailGoogleService emailGoogleService;

    private final BankRepository bankRepository;

    private final BankCommerceService bankCommerceService;

    public EmailService(EmailGoogleService emailGoogleService, BankRepository bankRepository, BankCommerceService bankCommerceService) {
        this.emailGoogleService = emailGoogleService;
        this.bankRepository = bankRepository;
        this.bankCommerceService = bankCommerceService;
    }



    @Async("asyncExecutor")
    public Message sendPaymentReceipt(final String emailDestination, final String subject
            , final String apiKey, final P2cRequest p2cRequest, final ResponseP2C p2cBankResponse)
            throws MessagingException, IOException {

        String body = "";

        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("templates/paymentReceiptP2C.html");

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

        CommerceBankInformationTo banCommerceInformation = bankCommerceService.getCommerceBankInformationByRif(p2cRequest.getRif(),
                apiKey);

        String first4 = banCommerceInformation.getBankAccount().substring(0, 4);
        String last4 = banCommerceInformation.getBankAccount().substring(banCommerceInformation.getBankAccount().length() - 4);
        String maskedAccount = first4 + "************" + last4;

        String first4number = banCommerceInformation.getCommercePhone().substring(0, 2);
        String last4number = banCommerceInformation.getCommercePhone().substring(banCommerceInformation.getCommercePhone().length() - 4);
        String maskedNumber = first4number + "*****" + last4number;

        String logoUrl= searchConfigurationById(10L,apiKey);

        setImageToMailFromURL("example.png",multipart,logoUrl);

        body = body.replace("$platform", subject).replace("$commerceName", banCommerceInformation.getCommerceName())
        .replace("$commerceDocument",p2cRequest.getRif()).replace("$phoneNumberCommerce",maskedNumber)
        .replace("$bankCommerce", banCommerceInformation.getBankName()).replace("$accountNumber", maskedAccount)
        .replace("$identificationDocument",p2cRequest.getPayerDocument())
        .replace("$referenceNumber",p2cRequest.getReferenceNumber()).replace("$phoneNumber", p2cRequest.getDebitPhone())
        .replace("$amount",p2cRequest.getTransactionAmount());

        try {
            return emailGoogleService.setMailInitialConfiguration(emailDestination, body, subject, multipart);
        } catch (MessagingException e) {
            System.err.println("Error al enviar el correo: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }


    @Async("asyncExecutor")
    public Message sendManualPaymentReceipt(final String emailDestination, final String subject
            , final String apiKey, final ManualPaymentRequest p2cRequest)
            throws MessagingException, IOException {

        String body = "";

        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("templates/manualPaymentReceiptP2C.html");

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

        CommerceBankInformationTo banCommerceInformation = bankCommerceService.getCommerceBankInformationByRif(p2cRequest.getRif(),
                apiKey);

        String first4 = banCommerceInformation.getBankAccount().substring(0, 4);
        String last4 = banCommerceInformation.getBankAccount().substring(banCommerceInformation.getBankAccount().length() - 4);
        String maskedAccount = first4 + "************" + last4;

        String first4number = banCommerceInformation.getCommercePhone().substring(0, 2);
        String last4number = banCommerceInformation.getCommercePhone().substring(banCommerceInformation.getCommercePhone().length() - 4);
        String maskedNumber = first4number + "*****" + last4number;

        String logoUrl= searchConfigurationById(10L,apiKey);

        setImageToMailFromURL("exmaple.png",multipart,logoUrl);

        body = body.replace("$platform", subject).replace("$commerceName", banCommerceInformation.getCommerceName())
                .replace("$commerceDocument",p2cRequest.getRif()).replace("$phoneNumberCommerce",maskedNumber)
                .replace("$bankCommerce", banCommerceInformation.getBankName()).replace("$accountNumber", maskedAccount)
                .replace("$identificationDocument",p2cRequest.getPayerDocument())
                .replace("$referenceNumber",p2cRequest.getReferenceNumber()).replace("$phoneNumber", p2cRequest.getDebitPhone())
                .replace("$amount",p2cRequest.getTransactionAmount().replace("$paymentType","Pago Manual"));

        try {
            return emailGoogleService.setMailInitialConfiguration(emailDestination, body, subject, multipart);
        } catch (MessagingException e) {
            System.err.println("Error al enviar el correo: " + e.getMessage());
            e.printStackTrace();
            throw e;
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

    public String searchConfigurationById(final Long idConfiguration, final String apiKey) throws JsonProcessingException {

        HttpHeaders headers = new HttpHeaders();
        RestTemplate restTemplate = new RestTemplate();

        headers.add("API_KEY", apiKey);

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
