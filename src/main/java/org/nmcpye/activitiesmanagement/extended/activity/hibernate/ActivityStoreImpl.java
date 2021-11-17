package org.nmcpye.activitiesmanagement.extended.activity.hibernate;

import org.hibernate.query.Query;
import org.nmcpye.activitiesmanagement.domain.activity.Activity;
import org.nmcpye.activitiesmanagement.extended.activity.ActivityQueryParams;
import org.nmcpye.activitiesmanagement.extended.activity.ActivityStore;
import org.nmcpye.activitiesmanagement.extended.common.hibernate.HibernateIdentifiableObjectStore;
import org.nmcpye.activitiesmanagement.extended.common.util.SqlHelper;
import org.nmcpye.activitiesmanagement.extended.serviceaclmodule.security.acl.AclService;
import org.nmcpye.activitiesmanagement.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ActivityStoreImpl
    extends HibernateIdentifiableObjectStore<Activity> implements ActivityStore {

    private final Logger log = LoggerFactory.getLogger(ActivityStoreImpl.class);

    public ActivityStoreImpl(JdbcTemplate jdbcTemplate, UserService userService, AclService aclService) {
        super(jdbcTemplate, Activity.class, userService, aclService, true);
    }

    //////////////////////////////////////
    //
    // Extended ActivityStore Methods
    //
    //////////////////////////////////////

    @Override
    public List<Activity> getActivities(ActivityQueryParams params, @Nullable List<String> orders) {
        return extractActivityQueryActivities(getActivityQuery(params, orders, false).list());
    }

    @Override
    public List<Activity> getActivities(ActivityQueryParams params) {
        return getActivities(params, null);
    }

    @Override
    public int getActivityCount(ActivityQueryParams params) {
        Long count = (Long) getActivityQuery(params, null, true).uniqueResult();
        return count != null ? count.intValue() : 0;
    }

    @Nonnull
    private List<Activity> extractActivityQueryActivities(@Nonnull List<?> result) {
        if (result.isEmpty()) {
            return Collections.emptyList();
        }

        final List<Activity> people = new ArrayList<>(result.size());
        for (Object o : result) {
            if (o instanceof Activity) {
                people.add((Activity) o);
            } else if (o.getClass().isArray()) {
                people.add((Activity) ((Object[]) o)[0]);
            }
        }
        return people;
    }

    private Query getActivityQuery(ActivityQueryParams params, List<String> orders, boolean count) {
        SqlHelper hlp = new SqlHelper();

        String hql = null;

//        if (count) {
//            hql = "select count(distinct u) ";
//        } else {
//            //            Schema userSchema = schemaService.getSchema( User.class );
//            //            convertedOrder = QueryUtils.convertOrderStrings( orders, userSchema );
//
//            hql = Stream.of("select distinct u").filter(Objects::nonNull).collect(Collectors.joining(","));
//            hql += " ";
//        }
//
//        hql += "from Person u " + "left join u.personAuthorityGroups pa ";
//        //        "inner join u.personAuthorityGroups uc ";
//
//        if (params.isPrefetchUserGroups() && !count) {
//            hql += "left join fetch u.groups g ";
//        } else {
//            hql += "left join u.groups g ";
//        }
//
//        if (params.hasUser()) {
//            hql += "inner join u.userInfo ui ";
//            //            hql += hlp.whereAnd() + " ui.login = :userLogin ";
//            //            hql += hlp.whereAnd() + " ui.person.id = u.id ";
//        }
//
//        if (params.hasOrganisationUnits()) {
//            hql += "left join u.organisationUnits ou ";
//
//            if (params.isIncludeOrgUnitChildren()) {
//                hql += hlp.whereAnd() + " (";
//
//                for (int i = 0; i < params.getOrganisationUnits().size(); i++) {
//                    hql += String.format("ou.path like :ouUid%d or ", i);
//                }
//
//                hql = TextUtils.removeLastOr(hql) + ")";
//            } else {
//                hql += hlp.whereAnd() + " ou.id in (:ouIds) ";
//            }
//        }
//
//        if (params.hasUserGroups()) {
//            hql += hlp.whereAnd() + " g.id in (:userGroupIds) ";
//        }
//
//        if (params.getDisabled() != null) {
//            //            hql += hlp.whereAnd() + " uc.disabled = :disabled ";
//            hql += hlp.whereAnd() + " u.disabled = :disabled ";
//        }
//
//        if (params.getQuery() != null) {
//            hql += hlp.whereAnd() + " (" + "lower(u.login) like :key " + ")"; // +
//            //                "or lower(u.surname) like :key " +
//            //                "or lower(u.email) like :key " +
//            //                "or lower(u.firstName) like :key) ";
//        }
//
//        if (params.getMobile() != null) {
//            hql += hlp.whereAnd() + " u.mobile = :mobile ";
//        }
//
//        if (params.isCanManage() && params.getPerson() != null) {
//            hql += hlp.whereAnd() + " g.id in (:ids) ";
//        }
//
//        if (params.isAuthSubset() && params.getPerson() != null) {
//            //            hql += hlp.whereAnd() + " not exists (" +
//            //                "select pa2 from PersonAuthorityGroup pa2 " +
//            //                "inner join pa2.authorities a " +
//            //                "where pa2.id = pa.id " +
//            //                "and a.name not in (:auths) ) ";
//            hql +=
//                hlp.whereAnd() +
//                " not exists (" +
//                "select uc2 from Person uc2 " +
//                "inner join uc2.personAuthorityGroups ag2 " +
//                "inner join ag2.authorities a " +
//                //                "where uc2.id = uc.id " +
//                "where uc2.id = u.id " +
//                "and a.name not in (:auths) ) ";
//        }
//
//        // TODO handle users with no user roles
//
//        if (params.isDisjointRoles() && params.getPerson() != null) {
//            hql +=
//                hlp.whereAnd() +
//                " not exists (" +
//                "select uc3 from Person uc3 " +
//                "inner join uc3.personAuthorityGroups ag3 " +
//                //                "where uc3.id = uc.id " +
//                "where uc3.id = u.id " +
//                "and ag3.id in (:roles) ) ";
//        }
//
//        if (params.getLastLogin() != null) {
//            //            hql += hlp.whereAnd() + " uc.lastLogin >= :lastLogin ";
//            hql += hlp.whereAnd() + " u.lastLogin >= :lastLogin ";
//        }
//
//        if (params.isSelfRegistered()) {
//            //            hql += hlp.whereAnd() + " uc.selfRegistered = true ";
//            hql += hlp.whereAnd() + " u.selfRegistered = true ";
//        }
//
//        //        if ( !count )
//        //        {
//        //            hql += "order by u.surname, u.firstName";
//        //        }
//
//        // ---------------------------------------------------------------------
//        // Query parameters
//        // ---------------------------------------------------------------------
//
//        log.debug("User query HQL: '{}'", hql);

        Query query = getQuery(hql);

        if (params.getQuery() != null) {
            query.setParameter("key", "%" + params.getQuery().toLowerCase() + "%");
        }

        //        if (params.hasUser()) {
        //            query.setParameter( "userLogin", params.getUser().getLogin() );
        //        }
//
//        if (params.getMobile() != null) {
//            query.setParameter("mobile", params.getMobile());
//        }
//
//        if (params.isCanManage() && params.getPerson() != null) {
//            // TODO Check
//            Collection<Long> managedGroups = IdentifiableObjectUtils.getIdentifiers(params.getPerson().getManagedGroups());
//
//            query.setParameterList("ids", managedGroups);
//        }
//
//        if (params.getDisabled() != null) {
//            query.setParameter("disabled", params.getDisabled());
//        }
//
//        if (params.isAuthSubset() && params.getPerson() != null) {
//            // TODO Check
//            //            Set<String> auths = params.getUser().getUserCredentials().getAllAuthorities();
//            Set<String> auths = params.getPerson().getAllAuthorities();
//
//            query.setParameterList("auths", auths);
//        }
//
//        if (params.isDisjointRoles() && params.getPerson() != null) {
//            // TODO Check
//            Collection<Long> roles = IdentifiableObjectUtils.getIdentifiers(params.getPerson().getPersonAuthorityGroups());
//
//            query.setParameterList("roles", roles);
//        }
//
//        if (params.getLastLogin() != null) {
//            query.setParameter("lastLogin", params.getLastLogin());
//        }
//
//        if (params.hasOrganisationUnits()) {
//            if (params.isIncludeOrgUnitChildren()) {
//                for (int i = 0; i < params.getOrganisationUnits().size(); i++) {
//                    query.setParameter(String.format("ouUid%d", i), "%/" + params.getOrganisationUnits().get(i).getUid() + "%");
//                }
//            } else {
//                Collection<Long> ouIds = IdentifiableObjectUtils.getIdentifiers(params.getOrganisationUnits());
//
//                query.setParameterList("ouIds", ouIds);
//            }
//        }
//
//        if (params.hasUserGroups()) {
//            Collection<Long> userGroupIds = IdentifiableObjectUtils.getIdentifiers(params.getPeopleGroups());
//
//            query.setParameterList("userGroupIds", userGroupIds);
//        }
//
//        if (params.getFirst() != null) {
//            query.setFirstResult(params.getFirst());
//        }
//
//        if (params.getMax() != null) {
//            query.setMaxResults(params.getMax()).list();
//        }

        return query;
    }

    @Override
    public int getActivityCount() {
        Query<Long> query = getTypedQuery("select count(*) from Person");
        return query.uniqueResult().intValue();
    }

    @Override
    public Activity getActivity(Long id) {
        return getSession().get(Activity.class, id);
    }
}
