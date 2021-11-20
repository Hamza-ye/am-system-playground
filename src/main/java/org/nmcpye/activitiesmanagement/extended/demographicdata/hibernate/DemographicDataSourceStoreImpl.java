package org.nmcpye.activitiesmanagement.extended.demographicdata.hibernate;

import org.nmcpye.activitiesmanagement.domain.demographicdata.DemographicDataSource;
import org.nmcpye.activitiesmanagement.extended.common.hibernate.HibernateIdentifiableObjectStore;
import org.nmcpye.activitiesmanagement.extended.demographicdata.DemographicDataSourceStore;
import org.nmcpye.activitiesmanagement.extended.serviceaclmodule.security.acl.AclService;
import org.nmcpye.activitiesmanagement.service.UserService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * Created by Hamza on 17/11/2021.
 */
public class DemographicDataSourceStoreImpl extends HibernateIdentifiableObjectStore<DemographicDataSource>
    implements DemographicDataSourceStore {
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    public DemographicDataSourceStoreImpl(JdbcTemplate jdbcTemplate, ApplicationEventPublisher publisher,
                                          UserService userService, AclService aclService) {
        super(jdbcTemplate, publisher, DemographicDataSource.class, userService, aclService, true);
    }
}
