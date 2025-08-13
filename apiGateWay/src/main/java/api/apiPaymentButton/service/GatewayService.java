package api.apiPaymentButton.service;

import api.apiPaymentButton.util.Response;
import feign.FeignException;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;

@Service
public class GatewayService {

    public Response getResponseGatewayByHttpStatus(Exception e, String dynamicEndpoint, HttpServletResponse httpServletResponse) {
        switch (((FeignException.Forbidden) e).status()) {
            case 403:
                httpServletResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
                return new Response("ERROR","UNAUTHORIZED");

        }
        return new Response("ERROR","Error en el microservicio ".concat(dynamicEndpoint));
    }

    public void redirectToMicroService() {

    }
}
