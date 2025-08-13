package api.apic2p.service;

import api.apic2p.entity.BankEntity;
import api.apic2p.repository.BankRepository;
import api.apic2p.request.C2pRequest;
import api.apic2p.response.ResponseC2p;
import api.apic2p.to.CommerceBankInformationTo;
import api.apic2p.util.EmailGoogleService;
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
import java.util.*;

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
            , final String apiKey, final C2pRequest c2pRequest,final ResponseC2p responseC2p)
            throws MessagingException, IOException {

        String body = "";

        BankEntity bankEntity = bankRepository.findByBankCodeAndStatusTrue(c2pRequest.getBankPayment());

        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("templates/paymentReceiptC2P.html");

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

        CommerceBankInformationTo banCommerceInformation = bankCommerceService.getCommerceBankInformationByRif(c2pRequest.getRif(),
                apiKey);

        String first4 = banCommerceInformation.getBankAccount().substring(0, 4);
        String last4 = banCommerceInformation.getBankAccount().substring(banCommerceInformation.getBankAccount().length() - 4);
        String maskedAccount = first4 + "************" + last4;


        String logoUrl= searchConfigurationById(10L,apiKey);

        setImageToMailFromURL("logo_intelipay.png",multipart,logoUrl);

        body = body.replace("$platform", subject).replace("$commerceName", c2pRequest.getNameCommerce())
        .replace("$commerceDocument",c2pRequest.getRif()).replace("$phoneNumberCommerce",c2pRequest.getPhoneNumberCommerce())
        .replace("$bankCommerce", banCommerceInformation.getBankName()).replace("$accountNumber", maskedAccount)
        .replace("$identificationDocument",c2pRequest.getIdentificationDocument()).replace("$bank",bankEntity.getBankName())
        .replace("$referenceNumber", String.valueOf(responseC2p.getEnvelope().getBody().getRegistrarPagoC2PAPIResponse().getOut().getSecuencial()))
        .replace("$accountNumber", maskedAccount).replace("$phoneNumber", c2pRequest.getPhoneNumber())
        .replace("$reason",c2pRequest.getConcept()).replace("$amount",c2pRequest.getTransactionAmount());

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
