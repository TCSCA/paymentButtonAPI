package api.apiPaymentButton;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
@EnableFeignClients
@ComponentScan({"api.apiPaymentButton.*"})
@EnableWebMvc
@AutoConfigurationPackage
@ImportAutoConfiguration({FeignAutoConfiguration.class})
public class apiPaymentButton extends SpringBootServletInitializer {
    public static void main(String[] args) {
        SpringApplication.run(apiPaymentButton.class,args);
        System.out.print("Iniciado apiPaymentButton \n ");
    }
}
