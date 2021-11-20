package org.nmcpye.activitiesmanagement.extended.project.hibernate;

import org.hibernate.query.Query;
import org.nmcpye.activitiesmanagement.domain.project.Project;
import org.nmcpye.activitiesmanagement.extended.common.hibernate.HibernateIdentifiableObjectStore;
import org.nmcpye.activitiesmanagement.extended.project.ProjectStore;
import org.nmcpye.activitiesmanagement.extended.serviceaclmodule.security.acl.AclService;
import org.nmcpye.activitiesmanagement.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * Created by Hamza on 17/11/2021.
 */
public class ProjectStoreImpl
    extends HibernateIdentifiableObjectStore<Project>
    implements ProjectStore {

    private final Logger log = LoggerFactory.getLogger(ProjectStoreImpl.class);

    public ProjectStoreImpl(JdbcTemplate jdbcTemplate, ApplicationEventPublisher publisher,
                            UserService userService, AclService aclService) {
        super(jdbcTemplate, publisher, Project.class, userService, aclService, true);
    }

    //////////////////////////////////////
    //
    // Extended ProjectStore Methods
    //
    //////////////////////////////////////

    @Override
    public int getProjectCount() {
        Query<Long> query = getTypedQuery("select count(*) from Person");
        return query.uniqueResult().intValue();
    }

    @Override
    public Project getProject(Long id) {
        return getSession().get(Project.class, id);
    }
}
