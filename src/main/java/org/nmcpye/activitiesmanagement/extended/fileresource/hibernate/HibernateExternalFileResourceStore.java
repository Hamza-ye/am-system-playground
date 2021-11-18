package org.nmcpye.activitiesmanagement.extended.fileresource.hibernate;

import org.nmcpye.activitiesmanagement.domain.fileresource.ExternalFileResource;
import org.nmcpye.activitiesmanagement.extended.common.hibernate.HibernateIdentifiableObjectStore;
import org.nmcpye.activitiesmanagement.extended.fileresource.ExternalFileResourceStore;
import org.nmcpye.activitiesmanagement.extended.serviceaclmodule.security.acl.AclService;
import org.nmcpye.activitiesmanagement.service.UserService;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository("org.nmcpye.activitiesmanagement.extended.fileresource.ExternalFileResourceStore")
public class HibernateExternalFileResourceStore
    extends HibernateIdentifiableObjectStore<ExternalFileResource>
    implements ExternalFileResourceStore {
    public HibernateExternalFileResourceStore(JdbcTemplate jdbcTemplate, UserService currentUserService,
                                              AclService aclService) {
        super(jdbcTemplate, ExternalFileResource.class, currentUserService, aclService,
            false);
    }

    @Override
    public ExternalFileResource getExternalFileResourceByAccessToken(String accessToken) {
        return getQuery("from ExternalFileResource where accessToken = :accessToken")
            .setParameter("accessToken", accessToken).uniqueResult();
    }
}
