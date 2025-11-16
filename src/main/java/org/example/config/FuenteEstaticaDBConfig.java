package org.example.config;

import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
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
        entityManagerFactoryRef = "fuenteEstaticaEntityManagerFactory",
        transactionManagerRef = "fuenteEstaticaTransactionManager",
        basePackages = {"org.example.models.repository.repoFuenteEstatica"}
)
public class FuenteEstaticaDBConfig {

    @Primary
    @Bean(name = "fuenteEstaticaDataSource")
    @ConfigurationProperties(prefix = "db1.datasource")
    public DataSource dataSource() {
        return DataSourceBuilder.create().build();
    }

    @Primary
    @Bean(name = "fuenteEstaticaEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(
            EntityManagerFactoryBuilder builder,
            @Qualifier("fuenteEstaticaDataSource") DataSource dataSource) {

        // Propiedades JPA específicas para esta BD (leídas de db1.jpa.*)
        HashMap<String, Object> properties = new HashMap<>();
        properties.put("hibernate.hbm2ddl.auto", "update");
        properties.put("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");

        return builder
                .dataSource(dataSource)
                .properties(properties)
                .packages("org.example.models.entities.fuenteEstatica") // <-- Paquete de la Entidad 1
                .persistenceUnit("FuentePU")
                .build();
    }

    @Primary
    @Bean(name = "fuenteEstaticaTransactionManager")
    public PlatformTransactionManager transactionManager(
            @Qualifier("fuenteEstaticaEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }

}
