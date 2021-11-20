package org.nmcpye.activitiesmanagement.extended.person.hibernate;

import org.nmcpye.activitiesmanagement.domain.person.PersonAuthorityGroup;
import org.nmcpye.activitiesmanagement.extended.common.hibernate.HibernateIdentifiableObjectStore;
import org.nmcpye.activitiesmanagement.extended.person.PersonAuthorityGroupStore;
import org.nmcpye.activitiesmanagement.extended.serviceaclmodule.security.acl.AclService;
import org.nmcpye.activitiesmanagement.service.UserService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class HibernatePersonAuthorityGroupStore
    extends HibernateIdentifiableObjectStore<PersonAuthorityGroup>
    implements PersonAuthorityGroupStore {

    public HibernatePersonAuthorityGroupStore(JdbcTemplate jdbcTemplate, ApplicationEventPublisher publisher,
                                              UserService userService, AclService aclService) {
        super(jdbcTemplate, publisher, PersonAuthorityGroup.class, userService, aclService, true);
    }
}
