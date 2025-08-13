package api.loginext;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
@ComponentScan({"api.loginext.*"})
@EnableWebMvc
@AutoConfigurationPackage
public class LoginExtApplication {

    public static void main(String[] args) {
        SpringApplication.run(LoginExtApplication.class, args);
    }

}
