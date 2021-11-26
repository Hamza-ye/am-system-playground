package org.nmcpye.activitiesmanagement.extended.fileresource.hibernate;

import org.nmcpye.activitiesmanagement.domain.fileresource.ExternalFileResource;
import org.nmcpye.activitiesmanagement.extended.common.hibernate.HibernateIdentifiableObjectStore;
import org.nmcpye.activitiesmanagement.extended.fileresource.ExternalFileResourceStore;
import org.nmcpye.activitiesmanagement.extended.serviceaclmodule.security.acl.AclService;
import org.nmcpye.activitiesmanagement.service.UserService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class HibernateExternalFileResourceStore
    extends HibernateIdentifiableObjectStore<ExternalFileResource>
    implements ExternalFileResourceStore {

    public HibernateExternalFileResourceStore(JdbcTemplate jdbcTemplate, ApplicationEventPublisher publisher,
                                              UserService userService, AclService aclService) {
        super(jdbcTemplate, publisher, ExternalFileResource.class, userService, aclService, true);
    }

    @Override
    public ExternalFileResource getExternalFileResourceByAccessToken(String accessToken) {
        return getQuery("from ExternalFileResource where accessToken = :accessToken")
            .setParameter("accessToken", accessToken).uniqueResult();
    }
}
