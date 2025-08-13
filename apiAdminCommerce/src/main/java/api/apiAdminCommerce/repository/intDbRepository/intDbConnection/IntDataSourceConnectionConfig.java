package api.apiAdminCommerce.repository.intDbRepository.intDbConnection;

import api.apiAdminCommerce.security.dbConfig.SecondaryDataSource;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.HashMap;

@Configuration
@PropertySource(value = "classpath:db-configuration.properties")
@EnableJpaRepositories( basePackages = "api.apiAdminCommerce.repository.intDbRepository",
        entityManagerFactoryRef = "secondaryEntityManager",
        transactionManagerRef = "secondTransactionManager")
@EntityScan(basePackages = "api.apiAdminCommerce.entity")
public class IntDataSourceConnectionConfig {

    private final Environment env;

    private final SecondaryDataSource secondaryDataSource;

    public IntDataSourceConnectionConfig(Environment env, SecondaryDataSource secondaryDataSource) {
        this.env = env;
        this.secondaryDataSource = secondaryDataSource;
    }


    @Bean
    public LocalContainerEntityManagerFactoryBean secondaryEntityManager() {
        final LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(secondaryDataSource.secondaryDataSourceBean());
        em.setPackagesToScan("api.apiAdminCommerce.entity");

        final HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);
        final HashMap<String, Object> properties = new HashMap<>();
        properties.put("hibernate.hbm2ddl.auto", env.getProperty("hibernate.hbm2ddl.auto"));
        properties.put("hibernate.dialect", env.getProperty("hibernate.dialect"));
        properties.put("hibernate.proc.param_null_passing",true);
        em.setJpaPropertyMap(properties);

        return em;
    }

    @Bean
    public PlatformTransactionManager secondTransactionManager() {
        final JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(secondaryEntityManager().getObject());
        return transactionManager;
    }
}
