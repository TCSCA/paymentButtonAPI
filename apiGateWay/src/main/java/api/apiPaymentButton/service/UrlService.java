package api.apiPaymentButton.service;

import api.apiPaymentButton.util.CryptoUtils;
import api.apiPaymentButton.util.ManageLog;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.net.URI;

@Service
@PropertySource(value = "classpath:routes.properties")
public class UrlService {

    private final Environment environment;

    private final String key = "route.gateway.ms.";

    public UrlService(Environment environment) {
        this.environment = environment;
    }

    public URI getUrlByEndpointName(String methodName, String microServiceName) {
        URI basePathUri = URI.create("http://localhost:4444/".concat(microServiceName));
        return basePathUri;
    }

    public URI getUrlFromRoutePropertie(String methodName) {
        if(methodName.contains("-")) {
            String[] paramArr = methodName.split("-", 2);
            methodName = paramArr[0];
        }
        String routeKey = key.concat(methodName);
        String urlValue = environment.getProperty(routeKey);

        if(urlValue == null) {
            return null;
        }
        URI basePathUri = URI.create(urlValue);
        return basePathUri;
    }

    public URI getUrlFromFile(String methodName, ManageLog manageLog) throws FileNotFoundException {

        if(methodName.contains("-")) {
            String[] paramArr = methodName.split("-", 2);
            methodName = paramArr[0];
        }

        String decryptedFile = decryptFileRoutes(methodName);
        if(decryptedFile == null) {
            return null;
        }
        URI basePathUri = URI.create(decryptedFile);
        return basePathUri;

    }

    private String decryptFileRoutes(String methodName) throws FileNotFoundException {
        String fileName = environment.getProperty("route.int.file.directory.output");
        String content = CryptoUtils.decrypt(fileName);

        if(content.contains(methodName)) {
            String[] arrOfStr = content.split("\\|");
            for (String route : arrOfStr) {
                if(route.startsWith(methodName)) {
                    String[] routeArr = route.split("=", 2);
                    if(routeArr[0].equals(methodName)) {
                        return routeArr[1];
                    }
                }
            }
        } else {
            return null;
        }
        return null;
    }
}
