package org.example.config;

import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.HashMap;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        entityManagerFactoryRef = "agregadorEntityManagerFactory",
        transactionManagerRef = "agregadorTransactionManager",
        basePackages = "org.example.models.repository.repoAgregador" // <-- ¡Paquete del Repositorio 2!
)
public class AgregadorDBConfig {

    @Bean
    @ConfigurationProperties(prefix = "db2.datasource")
    public DataSourceProperties agregadorDataSourceProperties() {
        return new DataSourceProperties();
    }
    @Bean
    public DataSource agregadorDataSource() {
        return agregadorDataSourceProperties()
                .initializeDataSourceBuilder()
                .build();
    }

    @Bean(name = "agregadorEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(
            EntityManagerFactoryBuilder builder,
            @Qualifier("agregadorDataSource") DataSource dataSource) {

        HashMap<String, Object> properties = new HashMap<>();
        properties.put("hibernate.hbm2ddl.auto", "update");
        properties.put("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");

        return builder
                .dataSource(dataSource)
                .properties(properties)
                .packages("org.example.models.entities.fuente") // <-- Paquete de la Entidad 2
                // (OJO: Asegúrate de que este sea el paquete correcto para la entidad 'Fuente')
                .persistenceUnit("AgregadorPU")
                .build();
    }

    @Bean(name = "agregadorTransactionManager")
    public PlatformTransactionManager transactionManager(
            @Qualifier("agregadorEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }
}