package org.nmcpye.activitiesmanagement.extended.chv.hibernate;

import org.nmcpye.activitiesmanagement.domain.chv.CHV;
import org.nmcpye.activitiesmanagement.extended.chv.ChvStore;
import org.nmcpye.activitiesmanagement.extended.common.hibernate.HibernateIdentifiableObjectStore;
import org.nmcpye.activitiesmanagement.extended.serviceaclmodule.security.acl.AclService;
import org.nmcpye.activitiesmanagement.service.UserService;
import org.springframework.jdbc.core.JdbcTemplate;

public class ChvStoreImpl
    extends HibernateIdentifiableObjectStore<CHV>
    implements ChvStore {
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    public ChvStoreImpl(JdbcTemplate jdbcTemplate, UserService currentUserService, AclService aclService) {
        super(jdbcTemplate, CHV.class, currentUserService, aclService, true);
    }
}
