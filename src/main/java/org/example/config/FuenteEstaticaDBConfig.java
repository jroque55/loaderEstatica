package org.example.config;

import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
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
        basePackages = "org.example.models.repository.repoFuenteEstatica"
)
public class FuenteEstaticaDBConfig {

    // 1. Definimos las propiedades específicas (prefijo db1)
    @Bean
    @ConfigurationProperties(prefix = "db1.datasource")
    public DataSourceProperties fuenteEstaticaDataSourceProperties() {
        return new DataSourceProperties();
    }

    // 2. Creamos el DataSource usando esas propiedades
    // ELIMINÉ EL METODO DUPLICADO Y FUSIONÉ LA LÓGICA AQUÍ
    @Primary
    @Bean(name = "fuenteEstaticaDataSource")
    public DataSource fuenteEstaticaDataSource() {
        return fuenteEstaticaDataSourceProperties()
                .initializeDataSourceBuilder()
                .build();
    }

    // 3. EntityManagerFactory
    @Primary
    @Bean(name = "fuenteEstaticaEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(
            EntityManagerFactoryBuilder builder,
            @Qualifier("fuenteEstaticaDataSource") DataSource dataSource) {

        HashMap<String, Object> properties = new HashMap<>();
        properties.put("hibernate.hbm2ddl.auto", "create");
        properties.put("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");

        return builder
                .dataSource(dataSource)
                .properties(properties)
                .packages("org.example.models.entities.fuenteEstatica")
                .persistenceUnit("FuentePU")
                .build();
    }

    // 4. TransactionManager
    @Primary
    @Bean(name = "fuenteEstaticaTransactionManager")
    public PlatformTransactionManager transactionManager(
            @Qualifier("fuenteEstaticaEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }
}