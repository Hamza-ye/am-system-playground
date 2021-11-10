package org.nmcpye.activitiesmanagement.extended.config;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Objects;

/**
 * Created by Hamza on 09/11/2021.
 */
//@Configuration
//@EnableTransactionManagement
public class HibernateConfig {

    @Bean
    public LocalSessionFactoryBean sessionFactory(ObjectProvider<DataSource> dataSource)
        throws Exception {
        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        sessionFactory.setDataSource(Objects.requireNonNull(dataSource.getIfUnique()));

        return sessionFactory;
    }

    @Bean
    public HibernateTransactionManager hibernateTransactionManager(ObjectProvider<DataSource> dataSource,
                                                                   SessionFactory sessionFactory) {
        HibernateTransactionManager transactionManager = new HibernateTransactionManager();
        transactionManager.setSessionFactory(sessionFactory);
        transactionManager.setDataSource(dataSource.getIfUnique());

        return transactionManager;
    }

    @Bean(name = "entityManagerFactory")
    @Primary
    public LocalContainerEntityManagerFactoryBean customerEntityManagerFactory(
        EntityManagerFactoryBuilder builder, ObjectProvider<DataSource> dataSource) {
        return builder
            .dataSource(dataSource.getIfUnique())
            //            .packages(Input.class)
//                .persistenceUnit("spring")
            .build();
    }
}
