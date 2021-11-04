package org.nmcpye.activitiesmanagement.extended.organisationunit.hibernate;

import org.nmcpye.activitiesmanagement.domain.organisationunit.OrganisationUnitGroup;
import org.nmcpye.activitiesmanagement.extended.common.hibernate.HibernateIdentifiableObjectStore;
import org.nmcpye.activitiesmanagement.extended.organisationunit.OrganisationUnitGroupStore;
import org.nmcpye.activitiesmanagement.service.UserService;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("org.nmcpye.activitiesmanagement.extended.organisationunit.OrganisationUnitGroupStore")
public class HibernateOrganisationUnitGroupStore
    extends HibernateIdentifiableObjectStore<OrganisationUnitGroup>
    implements OrganisationUnitGroupStore {

    public HibernateOrganisationUnitGroupStore(JdbcTemplate jdbcTemplate, UserService userService) {
        super(jdbcTemplate, OrganisationUnitGroup.class, userService, true);
    }

    @Override
    public List<OrganisationUnitGroup> getOrganisationUnitGroupsWithGroupSets() {
        return getQuery("from OrganisationUnitGroup o where o.groupSet is not null").list();
    }
}
