package api.authentication.util;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ValidationUtil {

    @Value("${app.api.key}")
    private String apiKeyValue;

    @Value("API_KEY")
    private String apiKeyHeader;

    public boolean validateApiKey(HttpServletRequest httpServletRequest){
        String apiKey = httpServletRequest.getHeader(apiKeyHeader.toLowerCase());
        if(apiKey!=null && apiKeyValue.equals(apiKey)){
            return true;
        }else{
            return false;
        }
    }
}
