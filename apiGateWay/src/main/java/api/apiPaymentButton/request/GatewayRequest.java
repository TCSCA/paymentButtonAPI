package api.apiPaymentButton.request;

import feign.Headers;
import feign.Param;
import feign.RequestLine;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;
import java.util.LinkedHashMap;
import java.util.Optional;

@FeignClient(
        value = "gateway-service",
        url= "http://localhost:4444",
        configuration = FeignConfiguration.class
)
public interface GatewayRequest {

    @PostMapping("/{dynamicEndpoint}")
    String dynamicEndpointRequest(URI baseUrl, @PathVariable String dynamicEndpoint,
                                               @RequestHeader("API_KEY") String apiKey,
                                               @RequestBody Optional<LinkedHashMap> jsonRequest);

    @PostMapping("/{dynamicEndpoint}")
    String dynamicEndpointRequestToken(URI baseUrl, @PathVariable String dynamicEndpoint,
                                       @RequestHeader("API_KEY") String apiKey,
                                       @RequestHeader("token") String token,
                                       @RequestBody Optional<LinkedHashMap> jsonRequest);

    @PostMapping("/{dynamicEndpoint}")
    String dynamicEndpointRequestConfirmationKey(URI baseUrl, @PathVariable String dynamicEndpoint,
                                       @RequestHeader("API_KEY") String apiKey,
                                       @RequestHeader("CONFIRMATION_KEY") String confirmationKey,
                                       @RequestBody Optional<LinkedHashMap> jsonRequest);

    @GetMapping("/{dynamicEndpoint}")
    String dynamicEndpointRequestGet(URI baseUrl, @PathVariable String dynamicEndpoint,
                                     @RequestHeader("API_KEY") String apiKey);


    @GetMapping("/{dynamicEndpoint}")
    String dynamicEndpointRequestGetToken(URI baseUrl, @PathVariable String dynamicEndpoint,
                                     @RequestHeader("API_KEY") String apiKey,
                                     @RequestHeader("token") String token);

    //Metodos con pathVariables

    @PostMapping("/{dynamicEndpoint}/{param}")
    String dynamicEndpointRequestWithParam(URI baseUrl, @PathVariable String dynamicEndpoint,
                                        @PathVariable String param,
                                        @RequestHeader("API_KEY") String apiKey,
                                        @RequestBody Optional<LinkedHashMap> jsonRequest);

    @PostMapping("/{dynamicEndpoint}/{param}")
    String dynamicEndpointRequestTokenWithParam(URI baseUrl, @PathVariable String dynamicEndpoint,
                                       @PathVariable String param,
                                       @RequestHeader("API_KEY") String apiKey,
                                       @RequestHeader("token") String token,
                                       @RequestBody Optional<LinkedHashMap> jsonRequest);

    @GetMapping("/{dynamicEndpoint}/{param}")
    String dynamicEndpointRequestGetWithParam(URI baseUrl, @PathVariable String dynamicEndpoint,
                                     @PathVariable String param,
                                     @RequestHeader("API_KEY") String apiKey);


    @GetMapping("/{dynamicEndpoint}/{param}")
    String dynamicEndpointRequestGetTokenWithParam(URI baseUrl, @PathVariable String dynamicEndpoint,
                                          @PathVariable String param,
                                          @RequestHeader("API_KEY") String apiKey,
                                          @RequestHeader("token") String token);

    @PostMapping(value = "/uploadFile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Headers("Content-Type: multipart/form-data")
    LinkedHashMap<String, String> dynamicEndpointRequestUploadFile(
            @RequestPart("file") MultipartFile file,
            @RequestHeader("API_KEY") String apikey,
            @RequestHeader("token") String token,
            @RequestPart("idPreRegister") Long idPreRegister,
            @RequestPart("idRequirement") Long idRequirement,
            URI basePathUri
    );


}
