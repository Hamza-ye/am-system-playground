package org.nmcpye.activitiesmanagement.extended.config;

import org.nmcpye.activitiesmanagement.domain.scheduling.JobConfiguration;
import org.nmcpye.activitiesmanagement.domain.person.PeopleGroup;
import org.nmcpye.activitiesmanagement.extended.common.hibernate.HibernateIdentifiableObjectStore;
import org.nmcpye.activitiesmanagement.extended.serviceaclmodule.security.acl.AclService;
import org.nmcpye.activitiesmanagement.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * Created by Hamza on 14/10/2021.
 */
@Configuration("coreStoreConfig")
public class StoreConfig {

    private final JdbcTemplate jdbcTemplate;

    private final UserService userService;

    private final AclService aclService;

    @Autowired
    public StoreConfig(JdbcTemplate jdbcTemplate, UserService userService, AclService aclService) {
        this.jdbcTemplate = jdbcTemplate;
        this.userService = userService;
        this.aclService = aclService;
    }

    @Bean("org.nmcpye.activitiesmanagement.extended.person.PeopleGroupStore")
    public HibernateIdentifiableObjectStore<PeopleGroup> userGroupStore() {
        return new HibernateIdentifiableObjectStore<>(jdbcTemplate, PeopleGroup.class, userService, aclService, true);
    }

    @Bean("org.nmcpye.activitiesmanagement.extended.scheduling.JobConfigurationStore")
    public HibernateIdentifiableObjectStore<JobConfiguration> jobConfigurationStore() {
        return new HibernateIdentifiableObjectStore<>(jdbcTemplate, JobConfiguration.class, userService, aclService, true);
    }
}
