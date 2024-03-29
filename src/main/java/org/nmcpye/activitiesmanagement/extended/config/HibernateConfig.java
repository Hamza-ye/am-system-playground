package org.nmcpye.activitiesmanagement.extended.config;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateProperties;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateSettings;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by Hamza on 09/11/2021.
 */
@Configuration
@EnableTransactionManagement
public class HibernateConfig {

    private final JpaProperties jpaProperties;
    private final HibernateProperties hibernateProperties;
    private final List<HibernatePropertiesCustomizer> hibernatePropertiesCustomizers;

    public HibernateConfig(JpaProperties jpaProperties, HibernateProperties hibernateProperties,
                           ObjectProvider<HibernatePropertiesCustomizer> hibernatePropertiesCustomizers) {

        this.jpaProperties = jpaProperties;
        this.hibernateProperties = hibernateProperties;

        // we inject hibernatePropertiesCustomizers along with
        // cache customizer we configured in CacheConfiguration.
        this.hibernatePropertiesCustomizers =
            hibernatePropertiesCustomizers
                .orderedStream().collect(Collectors.toList());
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(
        EntityManagerFactoryBuilder factoryBuilder,
        ObjectProvider<DataSource> dataSource) {

        return factoryBuilder
            .dataSource(dataSource.getIfUnique())
            .packages("org.nmcpye.activitiesmanagement")
            .properties(getVendorProperties())
            .build();
    }

    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }

    @Bean
    public PersistenceExceptionTranslationPostProcessor exceptionTranslation() {
        return new PersistenceExceptionTranslationPostProcessor();
    }

    private Map<String, Object> getVendorProperties() {
        return new LinkedHashMap<>(
            this.hibernateProperties
                // bring all default jpa props provided by spring boot and hibernate props
                // so they get injected into the EntityManagerFactory
                .determineHibernateProperties(jpaProperties.getProperties(),
                    new HibernateSettings()
                        // Spring Boot's HibernateDefaultDdlAutoProvider is not available here
                        .hibernatePropertiesCustomizers(this.hibernatePropertiesCustomizers)
                )
        );
    }
}
