package api.internalrepository.configuration;

import api.internalrepository.utilFile.FileStorageProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@ComponentScan(basePackages = {"api.*"})
@EnableJpaRepositories(basePackages = "api.*.repository")
@EntityScan(basePackages = "api.*.entity")
@SpringBootApplication
@AutoConfigurationPackage
@EnableScheduling
@EnableConfigurationProperties(FileStorageProperties.class)
public class ApiInternalRepositoryApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiInternalRepositoryApplication.class, args);
    }

}
