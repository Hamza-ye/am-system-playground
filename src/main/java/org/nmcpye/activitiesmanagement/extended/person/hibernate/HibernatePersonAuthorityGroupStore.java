package org.nmcpye.activitiesmanagement.extended.person.hibernate;

import org.nmcpye.activitiesmanagement.domain.person.PersonAuthorityGroup;
import org.nmcpye.activitiesmanagement.extended.common.hibernate.HibernateIdentifiableObjectStore;
import org.nmcpye.activitiesmanagement.extended.person.PersonAuthorityGroupStore;
import org.nmcpye.activitiesmanagement.service.UserService;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository("org.nmcpye.activitiesmanagement.extended.person.PersonAuthorityGroupStore")
public class HibernatePersonAuthorityGroupStore
    extends HibernateIdentifiableObjectStore<PersonAuthorityGroup>
    implements PersonAuthorityGroupStore {

    public HibernatePersonAuthorityGroupStore(JdbcTemplate jdbcTemplate, UserService userService) {
        super(jdbcTemplate, PersonAuthorityGroup.class, userService, true);
    }
}
