package org.nmcpye.activitiesmanagement.extended.demographicdata.hibernate;

import org.nmcpye.activitiesmanagement.domain.demographicdata.DemographicData;
import org.nmcpye.activitiesmanagement.extended.common.hibernate.HibernateIdentifiableObjectStore;
import org.nmcpye.activitiesmanagement.extended.demographicdata.DemographicDataStore;
import org.nmcpye.activitiesmanagement.extended.serviceaclmodule.security.acl.AclService;
import org.nmcpye.activitiesmanagement.service.UserService;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * Created by Hamza on 17/11/2021.
 */
public class DemographicDataStoreImpl extends HibernateIdentifiableObjectStore<DemographicData> implements DemographicDataStore {
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    public DemographicDataStoreImpl(JdbcTemplate jdbcTemplate, UserService userService, AclService aclService) {
        super(jdbcTemplate, DemographicData.class, userService, aclService, true);
    }
}
