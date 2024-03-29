package org.nmcpye.activitiesmanagement.extended.organisationunit.hibernate;

import org.nmcpye.activitiesmanagement.domain.organisationunit.OrganisationUnitLevel;
import org.nmcpye.activitiesmanagement.extended.common.hibernate.HibernateIdentifiableObjectStore;
import org.nmcpye.activitiesmanagement.extended.organisationunit.OrganisationUnitLevelStore;
import org.nmcpye.activitiesmanagement.extended.serviceaclmodule.security.acl.AclService;
import org.nmcpye.activitiesmanagement.service.UserService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;

@Repository("org.nmcpye.activitiesmanagement.extended.organisationunit.OrganisationUnitLevelStore")
public class OrganisationUnitLevelStoreImpl
    extends HibernateIdentifiableObjectStore<OrganisationUnitLevel>
    implements OrganisationUnitLevelStore {

    public OrganisationUnitLevelStoreImpl(JdbcTemplate jdbcTemplate, ApplicationEventPublisher publisher,
                                          UserService userService, AclService aclService) {
        super(jdbcTemplate, publisher, OrganisationUnitLevel.class, userService, aclService, true);
    }

    @Override
    public void deleteAll() {
        String hql = "delete from OrganisationUnitLevel";

        getQuery(hql).executeUpdate();
    }

    @Override
    public OrganisationUnitLevel getByLevel(int level) {
        CriteriaBuilder builder = getCriteriaBuilder();

        return getSingleResult(builder, newJpaParameters().addPredicate(root -> builder.equal(root.get("level"), level)));
    }
}
