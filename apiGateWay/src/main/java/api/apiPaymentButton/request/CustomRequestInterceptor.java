package api.apiPaymentButton.request;


import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;

public class CustomRequestInterceptor implements RequestInterceptor {

    @Value("${gateway.api.key.header}")
    private String gatewayHeader;

    @Value("${gateway.api.key.value}")
    private String gatewayHeaderValue;

    @Override
    public void apply(RequestTemplate requestTemplate) {
        if (HttpMethod.POST.toString().equals(requestTemplate.method()) ||
            HttpMethod.GET.toString().equals(requestTemplate.method())) {
            requestTemplate.header(gatewayHeader, gatewayHeaderValue);
        }
    }
}
