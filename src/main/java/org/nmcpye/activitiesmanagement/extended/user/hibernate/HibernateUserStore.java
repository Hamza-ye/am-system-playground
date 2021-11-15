package org.nmcpye.activitiesmanagement.extended.user.hibernate;

import org.hibernate.annotations.QueryHints;
import org.hibernate.query.Query;
import org.nmcpye.activitiesmanagement.domain.User;
import org.nmcpye.activitiesmanagement.domain.person.Person;
import org.nmcpye.activitiesmanagement.extended.common.IdentifiableObjectUtils;
import org.nmcpye.activitiesmanagement.extended.common.hibernate.HibernateIdentifiableObjectStore;
import org.nmcpye.activitiesmanagement.extended.common.util.SqlHelper;
import org.nmcpye.activitiesmanagement.extended.common.util.TextUtils;
import org.nmcpye.activitiesmanagement.extended.serviceaclmodule.security.acl.AclService;
import org.nmcpye.activitiesmanagement.extended.servicecoremodule.query.QueryUtils;
import org.nmcpye.activitiesmanagement.extended.user.UserQueryParams;
import org.nmcpye.activitiesmanagement.extended.user.UserStore;
import org.nmcpye.activitiesmanagement.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.persistence.TypedQuery;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Repository("org.nmcpye.activitiesmanagement.extended.user.UserStore")
public class HibernateUserStore extends HibernateIdentifiableObjectStore<User> implements UserStore {

    private final Logger log = LoggerFactory.getLogger(HibernateUserStore.class);

    public HibernateUserStore(JdbcTemplate jdbcTemplate, ApplicationEventPublisher publisher,
                              UserService userService, AclService aclService) {
        super(jdbcTemplate, User.class, userService, aclService, true);
    }

    @Override
    public List<User> getUsers(UserQueryParams params, @Nullable List<String> orders) {
        return extractUserQueryUsers(getUserQuery(params, orders, false).list());
    }

    @Override
    public List<User> getUsers(UserQueryParams params) {
        return getUsers(params, null);
    }

    @Override
    public List<User> getExpiringUsers(UserQueryParams params) {
        return extractUserQueryUsers(getUserQuery(params, null, false).list());
    }

    @Override
    public int getUserCount(UserQueryParams params) {
        Long count = (Long) getUserQuery(params, null, true).uniqueResult();
        return count != null ? count.intValue() : 0;
    }

    @Nonnull
    private List<User> extractUserQueryUsers(@Nonnull List<?> result) {
        if (result.isEmpty()) {
            return Collections.emptyList();
        }

        final List<User> users = new ArrayList<>(result.size());
        for (Object o : result) {
            if (o instanceof User) {
                users.add((User) o);
            } else if (o.getClass().isArray()) {
                users.add((User) ((Object[]) o)[0]);
            }
        }
        return users;
    }

    private Query getUserQuery(UserQueryParams params, List<String> orders, boolean count) {
        SqlHelper hlp = new SqlHelper();

        //        List<Order> convertedOrder = null;
        String hql = null;

        if (count) {
            hql = "select count(distinct u) ";
        } else {
            //            Schema userSchema = schemaService.getSchema( User.class );
            //            convertedOrder = QueryUtils.convertOrderStrings( orders, userSchema );

            hql = Stream.of("select distinct u").filter(Objects::nonNull).collect(Collectors.joining(","));
            hql += " ";
        }

        hql += "from User u " + "inner join u.userCredentials uc ";

        if (params.isPrefetchUserGroups() && !count) {
            hql += "left join fetch u.groups g ";
        } else {
            hql += "left join u.groups g ";
        }

        if (params.hasOrganisationUnits()) {
            hql += "left join u.organisationUnits ou ";

            if (params.isIncludeOrgUnitChildren()) {
                hql += hlp.whereAnd() + " (";

                for (int i = 0; i < params.getOrganisationUnits().size(); i++) {
                    hql += String.format("ou.path like :ouUid%d or ", i);
                }

                hql = TextUtils.removeLastOr(hql) + ")";
            } else {
                hql += hlp.whereAnd() + " ou.id in (:ouIds) ";
            }
        }

        if (params.hasUserGroups()) {
            hql += hlp.whereAnd() + " g.id in (:userGroupIds) ";
        }

        if (params.getDisabled() != null) {
            hql += hlp.whereAnd() + " uc.disabled = :disabled ";
        }

        if (params.getQuery() != null) {
            hql +=
                hlp.whereAnd() +
                " (" +
                "lower(u.firstName) like :key " +
                "or lower(u.surname) like :key " +
                "or lower(u.email) like :key " +
                "or lower(uc.username) like :key) ";
        }

        if (params.getMobile() != null) {
            hql += hlp.whereAnd() + " u.mobile = :mobile ";
        }

        if (params.isCanManage() && params.getUser() != null) {
            hql += hlp.whereAnd() + " g.id in (:ids) ";
        }

        if (params.isAuthSubset() && params.getUser() != null) {
            hql +=
                hlp.whereAnd() +
                " not exists (" +
                "select uc2 from UserCredentials uc2 " +
                "inner join uc2.userAuthorityGroups ag2 " +
                "inner join ag2.authorities a " +
                "where uc2.id = uc.id " +
                "and a not in (:auths) ) ";
        }

        // TODO handle users with no user roles

        if (params.isDisjointRoles() && params.getUser() != null) {
            hql +=
                hlp.whereAnd() +
                " not exists (" +
                "select uc3 from UserCredentials uc3 " +
                "inner join uc3.userAuthorityGroups ag3 " +
                "where uc3.id = uc.id " +
                "and ag3.id in (:roles) ) ";
        }

        if (params.getLastLogin() != null) {
            hql += hlp.whereAnd() + " uc.lastLogin >= :lastLogin ";
        }

        if (!count) {
            //            String orderExpression = JpaQueryUtils.createOrderExpression( convertedOrder, "u" );
            hql += "order by u.surname, u.firstName";
        }

        // ---------------------------------------------------------------------
        // Query parameters
        // ---------------------------------------------------------------------

        log.debug("User query HQL: '{}'", hql);

        Query query = getQuery(hql);

        if (params.getQuery() != null) {
            query.setParameter("key", "%" + params.getQuery().toLowerCase() + "%");
        }

        if (params.getMobile() != null) {
            query.setParameter("mobile", params.getMobile());
        }

        if (params.isCanManage() && params.getUser() != null && params.getUser().getPerson() != null) {
            Collection<Long> managedGroups = IdentifiableObjectUtils.getIdentifiers(params.getUser().getPerson().getManagedGroups());

            query.setParameterList("ids", managedGroups);
        }

        if (params.getDisabled() != null) {
            query.setParameter("disabled", params.getDisabled());
        }

        if (params.isAuthSubset() && params.getUser() != null && params.getUser().getPerson() != null) {
            Set<String> auths = params.getUser().getPerson().getAllAuthorities();

            query.setParameterList("auths", auths);
        }

        if (params.isDisjointRoles() && params.getUser() != null && params.getUser().getPerson() != null) {
            Collection<Long> roles = IdentifiableObjectUtils.getIdentifiers(params.getUser().getPerson().getPersonAuthorityGroups());

            query.setParameterList("roles", roles);
        }

        if (params.getLastLogin() != null) {
            query.setParameter("lastLogin", params.getLastLogin());
        }

        if (params.hasOrganisationUnits()) {
            if (params.isIncludeOrgUnitChildren()) {
                for (int i = 0; i < params.getOrganisationUnits().size(); i++) {
                    query.setParameter(String.format("ouUid%d", i), "%/" + params.getOrganisationUnits().get(i).getUid() + "%");
                }
            } else {
                Collection<Long> ouIds = IdentifiableObjectUtils.getIdentifiers(params.getOrganisationUnits());

                query.setParameterList("ouIds", ouIds);
            }
        }

        if (params.hasUserGroups()) {
            Collection<Long> userGroupIds = IdentifiableObjectUtils.getIdentifiers(params.getUserGroups());

            query.setParameterList("userGroupIds", userGroupIds);
        }

        if (params.getFirst() != null) {
            query.setFirstResult(params.getFirst());
        }

        if (params.getMax() != null) {
            query.setMaxResults(params.getMax()).list();
        }

        return query;
    }

    @Override
    public int getUserCount() {
        Query<Long> query = getTypedQuery("select count(*) from User");
        return query.uniqueResult().intValue();
    }

    @Override
    public User getUser(long id) {
        return getSession().get(User.class, id);
    }

    @Override
    public Person getUserCredentialsByUsername(String login) {
        if (login == null) {
            return null;
        }

        String hql = "from Person uc where uc.login = :login";

        TypedQuery<Person> typedQuery = getSession().createQuery(hql, Person.class);
        typedQuery.setParameter("login", login);
        typedQuery.setHint(QueryHints.CACHEABLE, true);

        return QueryUtils.getSingleResult(typedQuery);
    }
}
