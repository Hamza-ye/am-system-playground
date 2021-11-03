package org.nmcpye.activitiesmanagement.extended.organisationunit.hibernate;

import org.nmcpye.activitiesmanagement.domain.organisationunit.OrganisationUnitGroupSet;
import org.nmcpye.activitiesmanagement.extended.common.hibernate.HibernateIdentifiableObjectStore;
import org.nmcpye.activitiesmanagement.extended.organisationunit.OrganisationUnitGroupSetStore;
import org.nmcpye.activitiesmanagement.service.UserService;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository("org.nmcpye.activitiesmanagement.extended.organisationunit.OrganisationUnitGroupSetStore")
public class HibernateOrganisationUnitGroupSetStore
    extends HibernateIdentifiableObjectStore<OrganisationUnitGroupSet>
    implements OrganisationUnitGroupSetStore {

    public HibernateOrganisationUnitGroupSetStore(JdbcTemplate jdbcTemplate, UserService userService) {
        super(jdbcTemplate, OrganisationUnitGroupSet.class, userService, true);
    }
}
