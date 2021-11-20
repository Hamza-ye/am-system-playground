package org.nmcpye.activitiesmanagement.extended.organisationunit.hibernate;

import org.nmcpye.activitiesmanagement.domain.organisationunit.OrganisationUnitGroupSet;
import org.nmcpye.activitiesmanagement.extended.common.hibernate.HibernateIdentifiableObjectStore;
import org.nmcpye.activitiesmanagement.extended.organisationunit.OrganisationUnitGroupSetStore;
import org.nmcpye.activitiesmanagement.extended.serviceaclmodule.security.acl.AclService;
import org.nmcpye.activitiesmanagement.service.UserService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.jdbc.core.JdbcTemplate;

//@Repository("org.nmcpye.activitiesmanagement.extended.organisationunit.OrganisationUnitGroupSetStore")
public class OrganisationUnitGroupSetStoreImpl
    extends HibernateIdentifiableObjectStore<OrganisationUnitGroupSet>
    implements OrganisationUnitGroupSetStore {

    public OrganisationUnitGroupSetStoreImpl(JdbcTemplate jdbcTemplate, ApplicationEventPublisher publisher,
                                             UserService userService, AclService aclService) {
        super(jdbcTemplate, publisher, OrganisationUnitGroupSet.class, userService, aclService, true);
    }
}
