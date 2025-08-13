package api.apiAdminCommerce.security.dbConfig;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.util.Objects;

@Configuration
@PropertySource(value = "classpath:db-configuration.properties")
public class PrimaryDataSource {

    private final Environment env;

    public PrimaryDataSource(Environment env) {
        this.env = env;
    }

    @Primary
    @Bean
    public DataSource primaryDataSourceBean() {

        DriverManagerDataSource dataSource
                = new DriverManagerDataSource();
        dataSource.setDriverClassName(
                Objects.requireNonNull(env.getProperty("jdbc.driverClassName")));
        dataSource.setUrl(env.getProperty("user.jdbc.url"));
        dataSource.setUsername(env.getProperty("jdbc.user"));
        dataSource.setPassword(env.getProperty("jdbc.pass"));
        dataSource.setSchema(env.getProperty("jdbc.schema"));

        return dataSource;
    }
}
