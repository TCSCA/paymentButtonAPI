package api.apicreditcard.service;

import api.apicreditcard.repository.BankRepository;
import api.apicreditcard.request.CreditCard;
import api.apicreditcard.request.CreditCardRequest;
import api.apicreditcard.request.DebitCard;
import api.apicreditcard.request.DebitCardRequest;
import api.apicreditcard.util.EmailGoogleService;
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
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import to.CommerceBankInformationTo;

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
    public Message sendPaymentReceiptCredit(final String emailDestination, final String subject
            , final HttpServletRequest httpServletRequest, final CreditCard creditCard, final CreditCardRequest creditCardRequest)
            throws MessagingException, IOException {

        String body = "";


        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("templates/paymentReceiptTDorTC.html");

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

        CommerceBankInformationTo banCommerceInformation = bankCommerceService.getCommerceBankInformationByRif(creditCardRequest.getRif(),
                httpServletRequest.getHeader("API_KEY"));

        String last4 = creditCard.getCardNumber().substring(creditCard.getCardNumber().length() - 4);
        String maskedAccount = "************" + last4;


        String logoUrl= searchConfigurationById(10L,httpServletRequest.getHeader("API_KEY"));

        setImageToMailFromURL("logo_intelipay.png",multipart,logoUrl);

        body = body.replace("$platform", subject).replace("$commerceName", banCommerceInformation.getCommerceName())
        .replace("$commerceDocument",creditCardRequest.getRif()).replace("$phoneNumberCommerce",banCommerceInformation.getCommercePhone())
        .replace("$identificationDocument",creditCard.getHolderId())
        .replace("$reason",creditCardRequest.getReason()).replace("$amount",String.valueOf(creditCardRequest.getAmount()))
        .replace("$fullName",creditCard.getHolderName()).replace("$typeDocument", creditCard.getHolderIdDoc())
        .replace("$cardNumber",maskedAccount);

        try {
            return emailGoogleService.setMailInitialConfiguration(emailDestination, body, subject, multipart, httpServletRequest);
        } catch (MessagingException e) {
            System.err.println("Error al enviar el correo: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }



    @Async("asyncExecutor")
    public Message sendPaymentReceiptDebit(final String emailDestination, final String subject
            , final HttpServletRequest httpServletRequest, final DebitCard debitCard, final DebitCardRequest debitCardRequest)
            throws MessagingException, IOException {

        String body = "";


        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("templates/paymentReceiptTDorTC.html");

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

        CommerceBankInformationTo banCommerceInformation = bankCommerceService.getCommerceBankInformationByRif(debitCardRequest.getRif(),
                httpServletRequest.getHeader("API_KEY"));

        String last4 = debitCard.getCardNumber().substring(debitCard.getCardNumber().length() - 4);
        String maskedAccount = "************" + last4;


        String logoUrl= searchConfigurationById(10L,httpServletRequest.getHeader("API_KEY"));

        setImageToMailFromURL("logo_intelipay.png",multipart,logoUrl);

        body = body.replace("$platform", subject).replace("$commerceName", banCommerceInformation.getCommerceName())
                .replace("$commerceDocument",debitCardRequest.getRif()).replace("$phoneNumberCommerce",banCommerceInformation.getCommercePhone())
                .replace("$identificationDocument",debitCard.getHolderId())
                .replace("$reason",debitCardRequest.getReason()).replace("$amount",String.valueOf(debitCardRequest.getAmount()))
                .replace("$fullName",debitCard.getHolderName()).replace("$typeDocument", debitCard.getHolderIdDoc())
                .replace("$cardNumber",maskedAccount);

        try {
            return emailGoogleService.setMailInitialConfiguration(emailDestination, body, subject, multipart, httpServletRequest);
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
