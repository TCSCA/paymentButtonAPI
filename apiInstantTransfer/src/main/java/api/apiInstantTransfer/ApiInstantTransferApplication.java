package api.apiInstantTransfer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
@EnableWebMvc
@AutoConfigurationPackage
public class ApiInstantTransferApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiInstantTransferApplication.class, args);
    }

}
