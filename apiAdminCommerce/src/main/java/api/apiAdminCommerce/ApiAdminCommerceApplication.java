package api.apiAdminCommerce;

import api.apiAdminCommerce.utilFile.FileStorageProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@ComponentScan(basePackages = {"api.*"})
@SpringBootApplication
@EnableWebMvc
@AutoConfigurationPackage
@EnableConfigurationProperties(FileStorageProperties.class)
public class ApiAdminCommerceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiAdminCommerceApplication.class, args);
    }

}
