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
public class SecondaryDataSource {

    private final Environment env;

    public SecondaryDataSource(Environment env) {
        this.env = env;
    }


    @Primary
    @Bean
    public DataSource secondaryDataSourceBean() {

        DriverManagerDataSource dataSource
                = new DriverManagerDataSource();
        dataSource.setDriverClassName(
                Objects.requireNonNull(env.getProperty("jdbc2.driverClassName")));
        dataSource.setUrl(env.getProperty("user2.jdbc.url"));
        dataSource.setUsername(env.getProperty("jdbc2.user"));
        dataSource.setPassword(env.getProperty("jdbc2.pass"));
        dataSource.setSchema(env.getProperty("jdbc2.schema"));

        return dataSource;
    }
}
