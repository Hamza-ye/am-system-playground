package org.nmcpye.activitiesmanagement.extended.relatedlink.hibernate;

import org.nmcpye.activitiesmanagement.domain.RelatedLink;
import org.nmcpye.activitiesmanagement.extended.common.hibernate.HibernateIdentifiableObjectStore;
import org.nmcpye.activitiesmanagement.extended.relatedlink.RelatedLinkStore;
import org.nmcpye.activitiesmanagement.extended.serviceaclmodule.security.acl.AclService;
import org.nmcpye.activitiesmanagement.service.UserService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * Created by Hamza on 17-11-2021.
 */
@Repository
public class RelatedLinkStoreImpl
    extends HibernateIdentifiableObjectStore<RelatedLink>
    implements RelatedLinkStore {
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    public RelatedLinkStoreImpl(JdbcTemplate jdbcTemplate, ApplicationEventPublisher publisher,
                                UserService userService, AclService aclService) {
        super(jdbcTemplate, publisher, RelatedLink.class, userService, aclService, true);
    }
}
