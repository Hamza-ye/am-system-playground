package org.nmcpye.activitiesmanagement.extended.config;

import org.nmcpye.activitiesmanagement.domain.JobConfiguration;
import org.nmcpye.activitiesmanagement.domain.person.PeopleGroup;
import org.nmcpye.activitiesmanagement.extended.common.hibernate.HibernateIdentifiableObjectStore;
import org.nmcpye.activitiesmanagement.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.persistence.EntityManagerFactory;

/**
 * Created by Hamza on 14/10/2021.
 */
@Configuration("coreStoreConfig")
//@EnableTransactionManagement
public class StoreConfig {

    private final JdbcTemplate jdbcTemplate;

    private final UserService userService;

    @Autowired
    public StoreConfig(JdbcTemplate jdbcTemplate, UserService userService) {
        this.jdbcTemplate = jdbcTemplate;
        this.userService = userService;
    }

    @Bean("org.nmcpye.activitiesmanagement.extended.person.PeopleGroupStore")
    public HibernateIdentifiableObjectStore<PeopleGroup> userGroupStore() {
        return new HibernateIdentifiableObjectStore<>(jdbcTemplate, PeopleGroup.class, userService, true);
    }

    @Bean("org.nmcpye.activitiesmanagement.extended.scheduling.JobConfigurationStore")
    public HibernateIdentifiableObjectStore<JobConfiguration> jobConfigurationStore() {
        return new HibernateIdentifiableObjectStore<>(jdbcTemplate, JobConfiguration.class, userService, true);
    }

    //    @Bean
    //    public SessionFactory sessionFactory(HibernateEntityManagerFactory hemf) {
    //        return hemf.getSession();
    //    }
    //
    //    @PersistenceContext
    //    EntityManager entityManager;
    //
    ////    @Bean
    ////    @Primary
    ////    public EntityManagerFactory entityManagerFactory() {
    ////        return entityManager.getEntityManagerFactory();
    ////    }

    //    @Bean
    //    public SessionFactory sessionFactory() {
    ////        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
    ////        sessionFactory.setDataSource(defaultDataSource());
    //////        sessionFactory.setPackagesToScan(
    //////            {"org.nmcpye.activitiesmanagement" });
    //////        sessionFactory.setHibernateProperties(hibernateProperties());
    ////
    ////        return sessionFactory;
    //        return entityManager.getEntityManagerFactory().unwrap(SessionFactory.class);
    //    }

    //    @Bean
    //    @Primary
    //    @ConfigurationProperties("spring.datasource")
    //    public DataSourceProperties defaultDataSourceProperties() {
    //        return new DataSourceProperties();
    //    }
    //
    //    @Bean
    //    @Primary
    ////    @ConfigurationProperties("spring.datasource")
    //    public DataSource defaultDataSource() {
    //        return defaultDataSourceProperties().initializeDataSourceBuilder().build();
    //    }

    //    @Bean(name = "entityManagerFactory")
    //    @Primary
    //    public LocalContainerEntityManagerFactoryBean customerEntityManagerFactory(
    //        EntityManagerFactoryBuilder builder) {
    //        return builder
    //            .dataSource(defaultDataSource())
    ////            .packages(Input.class)
    //            .persistenceUnit("spring")
    //            .build();
    //    }

    //    @Bean(name = "transactionManager")
    //    @Primary
    //    public JpaTransactionManager db2TransactionManager(@Qualifier("entityManagerFactory") final EntityManagerFactory emf) {
    //        JpaTransactionManager transactionManager = new JpaTransactionManager();
    //        transactionManager.setEntityManagerFactory(emf);
    //        return transactionManager;
    //    }
    //    @Bean
    //    @Primary
    //    public PlatformTransactionManager hibernateTransactionManager() {
    //        HibernateTransactionManager transactionManager
    //            = new HibernateTransactionManager();
    //        transactionManager.setSessionFactory(sessionFactory());
    //        return transactionManager;
    //    }
    //
    //    @Bean("jdbcTemplate")
    //    @Primary
    //    public JdbcTemplate jdbcTemplate()
    //        throws PropertyVetoException {
    //        JdbcTemplate jdbcTemplate = new JdbcTemplate(defaultDataSource());
    //        jdbcTemplate.setFetchSize(1000);
    //        return jdbcTemplate;
    //    }

}
