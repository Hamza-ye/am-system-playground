package org.nmcpye.activitiesmanagement.extended.chv.hibernate;

import org.nmcpye.activitiesmanagement.domain.chv.Chv;
import org.nmcpye.activitiesmanagement.extended.chv.ChvStore;
import org.nmcpye.activitiesmanagement.extended.common.hibernate.HibernateIdentifiableObjectStore;
import org.nmcpye.activitiesmanagement.extended.serviceaclmodule.security.acl.AclService;
import org.nmcpye.activitiesmanagement.service.UserService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * Created by Hamza on 17-11-2021.
 */
public class ChvStoreImpl
    extends HibernateIdentifiableObjectStore<Chv>
    implements ChvStore {
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    public ChvStoreImpl(JdbcTemplate jdbcTemplate, ApplicationEventPublisher publisher,
                        UserService userService, AclService aclService) {
        super(jdbcTemplate, publisher, Chv.class, userService, aclService, true);
    }
}
