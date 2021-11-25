package org.nmcpye.activitiesmanagement.extended.config;

import org.hibernate.cfg.AvailableSettings;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateProperties;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateSettings;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.orm.hibernate5.SpringBeanContainer;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.util.ClassUtils;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.ArrayList;
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
                           ConfigurableListableBeanFactory beanFactory,
                           ObjectProvider<HibernatePropertiesCustomizer> hibernatePropertiesCustomizers) {

        this.jpaProperties = jpaProperties;
        this.hibernateProperties = hibernateProperties;
        this.hibernatePropertiesCustomizers = determineHibernatePropertiesCustomizers(beanFactory,
            hibernatePropertiesCustomizers.orderedStream().collect(Collectors.toList()));
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(EntityManagerFactoryBuilder factoryBuilder, ObjectProvider<DataSource> dataSource) {

        final Map<String, Object> vendorProperties = getVendorProperties();

        return factoryBuilder
            .dataSource(dataSource.getIfUnique())
            .packages("org.nmcpye.activitiesmanagement")
            .properties(vendorProperties)
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

    private List<HibernatePropertiesCustomizer> determineHibernatePropertiesCustomizers(
        ConfigurableListableBeanFactory beanFactory,
        List<HibernatePropertiesCustomizer> hibernatePropertiesCustomizers) {

        List<HibernatePropertiesCustomizer> customizers = new ArrayList<>();
        if (ClassUtils.isPresent("org.hibernate.resource.beans.container.spi.BeanContainer",
            getClass().getClassLoader())) {
            customizers.add((properties) -> properties.put(AvailableSettings.BEAN_CONTAINER, new SpringBeanContainer(beanFactory)));
        }
        customizers.addAll(hibernatePropertiesCustomizers);
        return customizers;
    }

    private Map<String, Object> getVendorProperties() {
        return new LinkedHashMap<>(
            this.hibernateProperties
                .determineHibernateProperties(jpaProperties.getProperties(),
                    new HibernateSettings()
                        // Spring Boot's HibernateDefaultDdlAutoProvider is not available here
                        .hibernatePropertiesCustomizers(this.hibernatePropertiesCustomizers)
                )
        );
    }
}
