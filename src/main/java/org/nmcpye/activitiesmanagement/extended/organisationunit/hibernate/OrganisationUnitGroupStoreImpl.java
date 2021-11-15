package org.nmcpye.activitiesmanagement.extended.organisationunit.hibernate;

import org.nmcpye.activitiesmanagement.domain.organisationunit.OrganisationUnitGroup;
import org.nmcpye.activitiesmanagement.extended.common.hibernate.HibernateIdentifiableObjectStore;
import org.nmcpye.activitiesmanagement.extended.organisationunit.OrganisationUnitGroupStore;
import org.nmcpye.activitiesmanagement.extended.serviceaclmodule.security.acl.AclService;
import org.nmcpye.activitiesmanagement.service.UserService;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

//@Repository("org.nmcpye.activitiesmanagement.extended.organisationunit.OrganisationUnitGroupStore")
public class OrganisationUnitGroupStoreImpl
    extends HibernateIdentifiableObjectStore<OrganisationUnitGroup>
    implements OrganisationUnitGroupStore {

    public OrganisationUnitGroupStoreImpl(JdbcTemplate jdbcTemplate, UserService userService, AclService aclService) {
        super(jdbcTemplate, OrganisationUnitGroup.class, userService, aclService, true);
    }

    @Override
    public List<OrganisationUnitGroup> getOrganisationUnitGroupsWithGroupSets() {
        return getQuery("from OrganisationUnitGroup o where o.groupSet is not null").list();
    }
}
