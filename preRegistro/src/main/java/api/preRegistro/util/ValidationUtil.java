package api.preRegistro.util;

import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ValidationUtil {

    @Value("${app.api.key}")
    private  String apiKeyValue;

    @Value("API_KEY")
    public  String apiKeyHeader;

    @Value("GATEWAY_KEY")
    private String apiGatewayHeader;

    @Value("${app.gateway.key}")
    private String apiGatewayKeyValue;

    private static String apiHeader;

    private static String apiValue;

    private static String gatewayHeader;

    private static String gatewayValue;

    @PostConstruct
    public void init(){
        apiHeader = apiKeyHeader;
        apiValue = apiKeyValue;
        gatewayHeader = apiGatewayHeader;
        gatewayValue = apiGatewayKeyValue;
    }

    public static boolean validateApiKey(HttpServletRequest httpServletRequest){
        String apiKey = httpServletRequest.getHeader(apiHeader.toLowerCase());
        String gatewayKey = httpServletRequest.getHeader(gatewayHeader.toLowerCase());

        if(apiKey!=null && gatewayKey!= null) {
            if(apiValue.equals(apiKey) && gatewayValue.equals(gatewayKey)) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

}
