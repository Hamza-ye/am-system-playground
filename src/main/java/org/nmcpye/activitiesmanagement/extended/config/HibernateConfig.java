package org.nmcpye.activitiesmanagement.extended.config;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.orm.jpa.hibernate.SpringImplicitNamingStrategy;
import org.springframework.boot.orm.jpa.hibernate.SpringPhysicalNamingStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * Created by Hamza on 09/11/2021.
 */
@Configuration
@EnableTransactionManagement
public class HibernateConfig {

    private final Environment environment;

    public HibernateConfig(Environment environment) {
        this.environment = environment;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(ObjectProvider<DataSource> dataSource) {
        LocalContainerEntityManagerFactoryBean em
            = new LocalContainerEntityManagerFactoryBean();
//        em.setDataSource(dataSource());
        em.setDataSource(dataSource.getIfUnique());
        em.setPackagesToScan("org.nmcpye.activitiesmanagement");
        JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);
        em.setJpaProperties(additionalProperties());

        return em;
    }

    @Bean
    public PlatformTransactionManager transactionManager(ObjectProvider<DataSource> dataSource) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory(dataSource).getObject());
//        transactionManager.setEntityManagerFactory(entityManagerFactory.getObject());

        return transactionManager;
    }

    @Bean
    public PersistenceExceptionTranslationPostProcessor exceptionTranslation() {
        return new PersistenceExceptionTranslationPostProcessor();
    }

    Properties additionalProperties() {
        Properties properties = new Properties();
//        properties.setProperty("hibernate.hbm2ddl.auto", "create-drop");
//        properties.setProperty("hibernate.dialect", "tech.jhipster.domain.util.FixedPostgreSQL10Dialect");
        properties.setProperty("hibernate.dialect", environment.getProperty("spring.jpa.database-platform"));
        properties.setProperty("hibernate.physical_naming_strategy", SpringPhysicalNamingStrategy.class.getName());
        properties.setProperty("hibernate.implicit_naming_strategy", SpringImplicitNamingStrategy.class.getName());

        return properties;
    }
//    private final Environment environment;
//
//    private final DataSource dataSource;    // It will automatically read database properties from application.properties and create DataSource object
//
////    @PersistenceContext
////    EntityManagerFactory entityManagerFactory;
//
//    @Autowired
//    public HibernateConfig(Environment environment, DataSource dataSource) {
//        this.environment = environment;
//        this.dataSource = dataSource;
//    }
//
//    //    @Bean(name = "sessionFactory")
//    @Bean(name = "entityManagerFactory")
//    public LocalSessionFactoryBean getSessionFactory() {            // creating session factory
//        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
//        sessionFactory.setDataSource(dataSource);
//        sessionFactory.setPackagesToScan(new String[]{"org.nmcpye.activitiesmanagement.domain"});
//        sessionFactory.setHibernateProperties(hibernateProperties());
//        sessionFactory.setImplicitNamingStrategy(new org.springframework.boot.orm.jpa.hibernate.SpringImplicitNamingStrategy());
//        return sessionFactory;
//    }
//
//    private Properties hibernateProperties() {                  // configure hibernate properties
//        Properties properties = new Properties();
////        properties.put("hibernate.dialect", environment.getRequiredProperty("hibernate.dialect"));
////        properties.put("hibernate.show_sql", environment.getRequiredProperty("hibernate.show_sql"));
////        properties.put("hibernate.format_sql", environment.getRequiredProperty("hibernate.format_sql"));
////        properties.put("hibernate.hbm2ddl.auto", "update");
//        return properties;
//    }
//
//    @Bean(name = "transactionManager")                      // creating transaction manager factory
//    public HibernateTransactionManager getTransactionManager(
//        SessionFactory sessionFactory) {
//        HibernateTransactionManager transactionManager = new HibernateTransactionManager(
//            sessionFactory);
//        return transactionManager;
//    }
//    @Bean
//    public LocalSessionFactoryBean sessionFactory(ObjectProvider<DataSource> dataSource)
//        throws Exception {
//        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
//        sessionFactory.setDataSource(Objects.requireNonNull(dataSource.getIfUnique()));
//
//        return sessionFactory;
//    }
//
//    @Bean
//    public HibernateTransactionManager hibernateTransactionManager(ObjectProvider<DataSource> dataSource,
//                                                                   SessionFactory sessionFactory) {
//        HibernateTransactionManager transactionManager = new HibernateTransactionManager();
//        transactionManager.setSessionFactory(sessionFactory);
//        transactionManager.setDataSource(dataSource.getIfUnique());
//
//        return transactionManager;
//    }
//
//    @Bean(name = "entityManagerFactory")
//    @Primary
//    public LocalContainerEntityManagerFactoryBean customerEntityManagerFactory(
//        EntityManagerFactoryBuilder builder, ObjectProvider<DataSource> dataSource) {
//        return builder
//            .dataSource(dataSource.getIfUnique())
//            //            .packages(Input.class)
////                .persistenceUnit("spring")
//            .build();
//    }
}
