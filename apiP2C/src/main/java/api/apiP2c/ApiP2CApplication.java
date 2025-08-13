package api.apiP2c;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
@EnableWebMvc
@AutoConfigurationPackage
public class ApiP2CApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiP2CApplication.class, args);
    }

}
