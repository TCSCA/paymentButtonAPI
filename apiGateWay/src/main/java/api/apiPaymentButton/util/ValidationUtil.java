package api.apiPaymentButton.util;

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

    private static String apiHeader;

    private static String apiValue;

    @PostConstruct
    public void init(){
        apiHeader = apiKeyHeader;
        apiValue = apiKeyValue;
    }

    public static boolean validateApiKey(HttpServletRequest httpServletRequest){
        String apiKey = httpServletRequest.getHeader(apiHeader);
        if(apiKey!=null && apiValue.equals(apiKey)){
            return true;
        }else{
            return false;
        }
    }

}
