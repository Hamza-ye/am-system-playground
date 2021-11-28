package org.nmcpye.activitiesmanagement.extended.contentpage.hibernate;

import org.nmcpye.activitiesmanagement.domain.ContentPage;
import org.nmcpye.activitiesmanagement.extended.common.hibernate.HibernateIdentifiableObjectStore;
import org.nmcpye.activitiesmanagement.extended.contentpage.ContentPageStore;
import org.nmcpye.activitiesmanagement.extended.serviceaclmodule.security.acl.AclService;
import org.nmcpye.activitiesmanagement.service.UserService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * Created by Hamza on 17/11/2021.
 */
@Repository
public class ContentPageStoreImpl
    extends HibernateIdentifiableObjectStore<ContentPage>
    implements ContentPageStore {
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    public ContentPageStoreImpl(JdbcTemplate jdbcTemplate, ApplicationEventPublisher publisher,
                                UserService userService, AclService aclService) {
        super(jdbcTemplate, publisher, ContentPage.class, userService, aclService, true);
    }
}
