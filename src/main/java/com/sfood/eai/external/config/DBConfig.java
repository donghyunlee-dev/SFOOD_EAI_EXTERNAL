package com.sfood.eai.external.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.Properties;

@DependsOn("AppConfig")
@Configuration
@EnableJpaRepositories(
        basePackages = {"com.sfood.eai.external.repository"}
        , transactionManagerRef = "exTransactionManager"
        , entityManagerFactoryRef = "exEntityManagerFactory"
)
public class DBConfig {

    private final SshTunnelingInitializer initializer;

    @Value("${sfood.it.dev.db.schema}")
    private String dbSchema;

    public DBConfig(SshTunnelingInitializer initializer) {
        this.initializer = initializer;
    }

    @Bean(name = "dataSourceProperties")
    @ConfigurationProperties(prefix = "spring.datasource")
    DataSourceProperties dataSourceProperties() {
        return new DataSourceProperties();
    }

    @Primary
    @Bean(name = "omsDataSource")
    DataSource omsDataSource(@Qualifier("dataSourceProperties") DataSourceProperties dataSourceProperties) throws Exception {

        String url = getConnectionUrl(dataSourceProperties);

        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setDriverClassName(dataSourceProperties.getDriverClassName());
        hikariConfig.setJdbcUrl(url);
        hikariConfig.setUsername(dataSourceProperties.getUsername());
        hikariConfig.setPassword(dataSourceProperties.getPassword());
        hikariConfig.setSchema(dbSchema);
        hikariConfig.setMaximumPoolSize(50);
        hikariConfig.setConnectionTimeout(10000);
        hikariConfig.setIdleTimeout(600000);
        hikariConfig.setMaxLifetime(900000);
        hikariConfig.setKeepaliveTime(300000);
        hikariConfig.setValidationTimeout(3000);
        hikariConfig.setAutoCommit(false);
        hikariConfig.setLeakDetectionThreshold(5000);
        return new HikariDataSource(hikariConfig);
    }

    @Bean(name = "quartzDataSource")
    DataSource quartzDataSource(@Qualifier("dataSourceProperties") DataSourceProperties dataSourceProperties) throws Exception {

        String url = getConnectionUrl(dataSourceProperties);

        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setDriverClassName(dataSourceProperties.getDriverClassName());
        hikariConfig.setJdbcUrl(url);
        hikariConfig.setUsername(dataSourceProperties.getUsername());
        hikariConfig.setPassword(dataSourceProperties.getPassword());
        hikariConfig.setSchema(dbSchema);
        hikariConfig.setMaximumPoolSize(10);
        hikariConfig.setConnectionTimeout(10000);
        hikariConfig.setIdleTimeout(600000);
        hikariConfig.setMaxLifetime(1800000);
        hikariConfig.setKeepaliveTime(300000);
        hikariConfig.setValidationTimeout(3000);
        hikariConfig.setAutoCommit(true);
        hikariConfig.setLeakDetectionThreshold(5000);
        hikariConfig.setConnectionTestQuery("SELECT 1 FROM DUAL");

        return new HikariDataSource(hikariConfig);
    }

    @Primary
    @Bean(name = "exEntityManagerFactory")
    LocalContainerEntityManagerFactoryBean entityManagerFactory(EntityManagerFactoryBuilder builder, @Qualifier("omsDataSource") DataSource dataSource) {
        return builder
                .dataSource(dataSource)
                .packages("com.sfood.eai.external.repository", "com.sfood.eai.external.entity")
                .build();
    }

    @Primary
    @Bean(name = "exTransactionManager")
    PlatformTransactionManager transactionManager(@Qualifier("exEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }

    @Bean
    public JdbcTemplate jdbcTemplate(@Qualifier("omsDataSource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    private String getConnectionUrl(DataSourceProperties dataSourceProperties) {
        Integer forwardedPort;
        String url;
        if (AppConfig.isLocal) {
            forwardedPort = initializer.buildSshConnection();
            url = dataSourceProperties.getUrl().replace("[forwardedPort]", Integer.toString(forwardedPort));
        } else {
            url = dataSourceProperties.getUrl();
        }
        return url;
    }
}
