package org.nmcpye.activitiesmanagement.extended.organisationunit.hibernate;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.nmcpye.activitiesmanagement.domain.DataSet;
import org.nmcpye.activitiesmanagement.domain.organisationunit.OrganisationUnit;
import org.nmcpye.activitiesmanagement.extended.common.IdentifiableObjectUtils;
import org.nmcpye.activitiesmanagement.extended.common.hibernate.HibernateIdentifiableObjectStore;
import org.nmcpye.activitiesmanagement.extended.common.util.SqlHelper;
import org.nmcpye.activitiesmanagement.extended.common.util.TextUtils;
import org.nmcpye.activitiesmanagement.extended.organisationunit.OrganisationUnitHierarchy;
import org.nmcpye.activitiesmanagement.extended.organisationunit.OrganisationUnitQueryParams;
import org.nmcpye.activitiesmanagement.extended.organisationunit.OrganisationUnitStore;
import org.nmcpye.activitiesmanagement.extended.serviceaclmodule.security.acl.AclService;
import org.nmcpye.activitiesmanagement.extended.systemmodule.system.objectmapper.OrganisationUnitRelationshipRowMapper;
import org.nmcpye.activitiesmanagement.extended.systemmodule.system.util.SqlUtils;
import org.nmcpye.activitiesmanagement.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.Assert;

import java.sql.Timestamp;
import java.util.*;

//@Repository("org.nmcpye.activitiesmanagement.extended.organisationunit.OrganisationUnitStore")
public class OrganisationUnitStoreImpl
    extends HibernateIdentifiableObjectStore<OrganisationUnit> implements OrganisationUnitStore {

    private final Logger log = LoggerFactory.getLogger(OrganisationUnitStoreImpl.class);

    public OrganisationUnitStoreImpl(JdbcTemplate jdbcTemplate, UserService userService, AclService aclService) {
        super(jdbcTemplate, OrganisationUnit.class, userService, aclService, true);
    }

    // -------------------------------------------------------------------------
    // OrganisationUnit
    // -------------------------------------------------------------------------

    @Override
    public List<OrganisationUnit> getAllOrganisationUnitsByLastUpdated(Date lastUpdated) {
        return getAllGeLastUpdated(lastUpdated);
    }

    @Override
    public List<OrganisationUnit> getRootOrganisationUnits() {
        return getQuery("from OrganisationUnit o where o.parent is null").list();
    }

    @Override
    public List<OrganisationUnit> getOrganisationUnitsWithoutGroups() {
        return getQuery("from OrganisationUnit o where size(o.groups) = 0").list();
    }

    @Override
    public Long getOrganisationUnitHierarchyMemberCount(OrganisationUnit parent, Object member, String collectionName) {
        final String hql =
            "select count(*) from OrganisationUnit o " + "where o.path like :path " + "and :object in elements(o." + collectionName + ")";

        Query<Long> query = getTypedQuery(hql);
        query.setParameter("path", parent.getPath() + "%").setParameter("object", member);

        return query.getSingleResult();
    }

    @Override
    public List<OrganisationUnit> getOrganisationUnits(OrganisationUnitQueryParams params) {
        SqlHelper hlp = new SqlHelper();

        String hql = "select distinct o from OrganisationUnit o ";

        if (params.isFetchChildren()) {
            hql += "left join fetch o.children c ";
        }

        if (params.hasGroups()) {
            hql += "join o.groups og ";
        }

        if (params.hasQuery()) {
            hql += hlp.whereAnd() + " (lower(o.name) like :queryLower or o.code = :query or o.uid = :query) ";
        }

        if (params.hasParents()) {
            hql += hlp.whereAnd() + " (";

            for (OrganisationUnit parent : params.getParents()) {
                hql += "o.path like :" + parent.getUid() + " or ";
            }

            hql = TextUtils.removeLastOr(hql) + ") ";
        }

        if (params.hasGroups()) {
            hql += hlp.whereAnd() + " og.id in (:groupIds) ";
        }

        if (params.hasLevels()) {
            hql += hlp.whereAnd() + " o.hierarchyLevel in (:levels) ";
        }

        if (params.getMaxLevels() != null) {
            hql += hlp.whereAnd() + " o.hierarchyLevel <= :maxLevels ";
        }

        hql += "order by o." + params.getOrderBy().getName();

        Query<OrganisationUnit> query = getQuery(hql);

        if (params.hasQuery()) {
            query.setParameter("queryLower", "%" + params.getQuery().toLowerCase() + "%");
            query.setParameter("query", params.getQuery());
        }

        if (params.hasParents()) {
            for (OrganisationUnit parent : params.getParents()) {
                query.setParameter(parent.getUid(), parent.getPath() + "%");
            }
        }

        if (params.hasGroups()) {
            query.setParameterList("groupIds", IdentifiableObjectUtils.getIdentifiers(params.getGroups()));
        }

        if (params.hasLevels()) {
            query.setParameterList("levels", params.getLevels());
        }

        if (params.getMaxLevels() != null) {
            query.setParameter("maxLevels", params.getMaxLevels());
        }

        if (params.getFirst() != null) {
            query.setFirstResult(params.getFirst());
        }

        if (params.getMax() != null) {
            query.setMaxResults(params.getMax()).list();
        }

        return query.list();
    }

    @Override
    public Map<String, Set<String>> getOrganisationUnitDataSetAssocationMap(Collection<OrganisationUnit> organisationUnits, Collection<DataSet> dataSets) {
        SqlHelper hlp = new SqlHelper();

        String sql = "select ou.uid as ou_uid, array_agg(ds.uid) as ds_uid " +
            "from data_set_source d " +
            "inner join organisation_unit ou on ou.id=d.source_id " +
            "inner join data_set ds on ds.id=d.data_set_id ";

        if (organisationUnits != null) {
            Assert.notEmpty(organisationUnits, "Organisation units cannot be empty");

            sql += hlp.whereAnd() + " (";

            for (OrganisationUnit unit : organisationUnits) {
                sql += "ou.path like '" + unit.getPath() + "%' or ";
            }

            sql = TextUtils.removeLastOr(sql) + ") ";
        }

        if (dataSets != null) {
            Assert.notEmpty(dataSets, "Data sets cannot be empty");

            sql += hlp.whereAnd() + " ds.id in (" + StringUtils.join(IdentifiableObjectUtils.getIdentifiers(dataSets), ",") + ") ";
        }

        sql += "group by ou_uid";

        log.info("Org unit data set association map SQL: " + sql);

        Map<String, Set<String>> map = new HashMap<>();

        jdbcTemplate.query(sql, rs -> {
            String organisationUnitId = rs.getString("ou_uid");
            Set<String> dataSetIds = SqlUtils.getArrayAsSet(rs, "ds_uid");

            map.put(organisationUnitId, dataSetIds);
        });

        return map;
    }

    @Override
    public List<OrganisationUnit> getWithinCoordinateArea(double[] box) {
        // can't use hibernate-spatial 'makeenvelope' function, because not available in
        // current hibernate version
        // see: https://hibernate.atlassian.net/browse/HHH-13083

        if (box != null && box.length == 4) {
            return getSession()
                .createQuery(
                    "from OrganisationUnit ou " + "where within(ou.geometry, " + doMakeEnvelopeSql(box) + ") = true",
                    OrganisationUnit.class
                )
                .getResultList();
        }
        return new ArrayList<>();
    }

    private String doMakeEnvelopeSql(double[] box) {
        // equivalent to: postgis 'ST_MakeEnvelope' (https://postgis.net/docs/ST_MakeEnvelope.html)
        return "ST_MakeEnvelope(" + box[1] + "," + box[0] + "," + box[3] + "," + box[2] + ", 4326)";
    }

    // -------------------------------------------------------------------------
    // OrganisationUnitHierarchy
    // -------------------------------------------------------------------------

    @Override
    public OrganisationUnitHierarchy getOrganisationUnitHierarchy() {
        final String sql = "select organisationunitid, parentid from organisationunit";

        return new OrganisationUnitHierarchy(jdbcTemplate.query(sql, new OrganisationUnitRelationshipRowMapper()));
    }

    @Override
    public void updateOrganisationUnitParent(long organisationUnitId, long parentId) {
        Timestamp now = new Timestamp(new Date().getTime());

        final String sql =
            "update organisationunit " +
            "set parentid=" +
            parentId +
            ", lastupdated='" +
            now +
            "' " +
            "where organisationunitid=" +
            organisationUnitId;

        jdbcTemplate.execute(sql);
    }

    @Override
    public void updatePaths() {
        List<OrganisationUnit> organisationUnits = new ArrayList<>(
            getQuery("from OrganisationUnit ou where ou.path is null or ou.hierarchyLevel is null").list()
        );
        updatePaths(organisationUnits);
    }

    @Override
    public void forceUpdatePaths() {
        List<OrganisationUnit> organisationUnits = new ArrayList<>(getQuery("from OrganisationUnit").list());
        updatePaths(organisationUnits);
    }

    @Override
    public int getMaxLevel() {
        String hql = "select max(ou.hierarchyLevel) from OrganisationUnit ou";

        Query<Integer> query = getTypedQuery(hql);
        Integer maxLength = query.getSingleResult();

        return maxLength != null ? maxLength : 0;
    }

    private void updatePaths(List<OrganisationUnit> organisationUnits) {
        Session session = getSession();
        int counter = 0;

        for (OrganisationUnit organisationUnit : organisationUnits) {
            // TODO Ext
            organisationUnit.setPath(organisationUnit.getPath());
            organisationUnit.setHierarchyLevel(organisationUnit.getHierarchyLevel());

            session.update(organisationUnit);

            if ((counter % 400) == 0) {
                session.flush();
            }

            counter++;
        }
    }
}
