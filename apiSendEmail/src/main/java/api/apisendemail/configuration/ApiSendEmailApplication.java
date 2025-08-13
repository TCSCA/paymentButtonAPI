package api.apisendemail.configuration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@ComponentScan(basePackages = {"api.*"})
@SpringBootApplication
@AutoConfigurationPackage
@EnableScheduling
public class ApiSendEmailApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiSendEmailApplication.class, args);
    }

}
