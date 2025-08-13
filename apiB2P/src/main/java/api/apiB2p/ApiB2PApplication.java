package api.apiB2p;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
@EnableWebMvc
@AutoConfigurationPackage
public class ApiB2PApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiB2PApplication.class, args);
    }

}
