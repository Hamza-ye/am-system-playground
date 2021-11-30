package org.nmcpye.activitiesmanagement.extended.fileresource.hibernate;

import com.google.common.collect.ImmutableSet;
import org.joda.time.DateTime;
import org.nmcpye.activitiesmanagement.domain.fileresource.FileResource;
import org.nmcpye.activitiesmanagement.extended.common.hibernate.HibernateIdentifiableObjectStore;
import org.nmcpye.activitiesmanagement.extended.fileresource.FileResourceDomain;
import org.nmcpye.activitiesmanagement.extended.fileresource.FileResourceStore;
import org.nmcpye.activitiesmanagement.extended.serviceaclmodule.security.acl.AclService;
import org.nmcpye.activitiesmanagement.service.UserService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public class HibernateFileResourceStore
    extends HibernateIdentifiableObjectStore<FileResource>
    implements FileResourceStore {
    private static final Set<String> IMAGE_CONTENT_TYPES = new ImmutableSet.Builder<String>()
        .add("image/jpg")
        .add("image/png")
        .add("image/jpeg")
        .build();

    public HibernateFileResourceStore(JdbcTemplate jdbcTemplate, ApplicationEventPublisher publisher,
                                      UserService userService, AclService aclService) {
        super(jdbcTemplate, publisher, FileResource.class, userService, aclService, true);
    }

    @Override
    public List<FileResource> getExpiredFileResources(DateTime expires) {
        List<FileResource> results = getSession()
            .createNativeQuery("select fr.* " +
                "from file_resource fr " +
                "inner join (select dva.value " +
                "from datavalueaudit dva " +
                "where dva.created < :date " +
                "and dva.audittype in ('DELETE', 'UPDATE') " +
                "and dva.dataelementid in " +
                "(select dataelementid from dataelement where valuetype = 'FILE_RESOURCE')) dva " +
                "on dva.value = fr.uid " +
                "where fr.isassigned = true; ", FileResource.class)
            .setParameter("date", expires.toDate())
            .getResultList();

        return results;
    }

    @Override
    public List<FileResource> getAllUnProcessedImages() {
        return getQuery(
            "FROM FileResource fr WHERE fr.domain IN ( :domains ) AND fr.contentType IN ( :contentTypes ) AND hasMultipleStorageFiles = :hasMultipleStorageFiles")
            .setParameter("domains", FileResourceDomain.getDomainForMultipleImages())
            .setParameter("contentTypes", IMAGE_CONTENT_TYPES)
            .setParameter("hasMultipleStorageFiles", false)
            .setMaxResults(50).getResultList();
    }
}
