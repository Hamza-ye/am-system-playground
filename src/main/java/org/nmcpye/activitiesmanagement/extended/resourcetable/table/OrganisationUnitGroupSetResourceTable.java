package org.nmcpye.activitiesmanagement.extended.resourcetable.table;

import static org.nmcpye.activitiesmanagement.extended.common.util.TextUtils.removeLastComma;
import static org.nmcpye.activitiesmanagement.extended.system.util.SqlUtils.quote;

import com.google.common.collect.Lists;
import java.util.List;
import java.util.Optional;
import org.nmcpye.activitiesmanagement.domain.organisationunit.OrganisationUnitGroupSet;
import org.nmcpye.activitiesmanagement.extended.common.util.TextUtils;
import org.nmcpye.activitiesmanagement.extended.resourcetable.ResourceTable;
import org.nmcpye.activitiesmanagement.extended.resourcetable.ResourceTableType;

public class OrganisationUnitGroupSetResourceTable extends ResourceTable<OrganisationUnitGroupSet> {

    private boolean supportsPartialIndexes;

    private int organisationUnitLevels;

    public OrganisationUnitGroupSetResourceTable(
        List<OrganisationUnitGroupSet> objects,
        boolean supportsPartialIndexes,
        int organisationUnitLevels
    ) {
        super(objects);
        this.supportsPartialIndexes = supportsPartialIndexes;
        this.organisationUnitLevels = organisationUnitLevels;
    }

    @Override
    public ResourceTableType getTableType() {
        return ResourceTableType.ORG_UNIT_GROUP_SET_STRUCTURE;
    }

    @Override
    public String getCreateTempTableStatement() {
        UniqueNameVerifier uniqueNameVerifier = new UniqueNameVerifier();

        String statement =
            "create table " +
            getTempTableName() +
            " (" +
            "organisationunitid bigint not null, " +
            "organisationunitname varchar(230), " +
            "startdate date, ";

        for (OrganisationUnitGroupSet groupSet : objects) {
            statement += uniqueNameVerifier.ensureUniqueShortName(groupSet) + " varchar(230), ";
            statement += quote(groupSet.getUid()) + " character(11), ";
        }

        return removeLastComma(statement) + ")";
    }

    @Override
    public Optional<String> getPopulateTempTableStatement() {
        String sql =
            "insert into " +
            getTempTableName() +
            " " +
            "select ou.organisationunitid as organisationunitid, ou.name as organisationunitname, null as startdate, ";

        for (OrganisationUnitGroupSet groupSet : objects) {
            if (!groupSet.isIncludeSubhierarchyInAnalytics()) {
                sql +=
                    "(" +
                    "select oug.name from orgunitgroup oug " +
                    "inner join orgunitgroupmembers ougm on ougm.orgunitgroupid = oug.orgunitgroupid " +
                    "inner join orgunitgroupsetmembers ougsm on ougsm.orgunitgroupid = ougm.orgunitgroupid and ougsm.orgunitgroupsetid = " +
                    groupSet.getId() +
                    " " +
                    "where ougm.organisationunitid = ou.organisationunitid " +
                    "limit 1) as " +
                    quote(groupSet.getName()) +
                    ", ";

                sql +=
                    "(" +
                    "select oug.uid from orgunitgroup oug " +
                    "inner join orgunitgroupmembers ougm on ougm.orgunitgroupid = oug.orgunitgroupid " +
                    "inner join orgunitgroupsetmembers ougsm on ougsm.orgunitgroupid = ougm.orgunitgroupid and ougsm.orgunitgroupsetid = " +
                    groupSet.getId() +
                    " " +
                    "where ougm.organisationunitid = ou.organisationunitid " +
                    "limit 1) as " +
                    quote(groupSet.getUid()) +
                    ", ";
            } else {
                sql += "coalesce(";

                for (int i = organisationUnitLevels; i > 0; i--) {
                    sql +=
                        "(select oug.name from orgunitgroup oug " +
                        "inner join orgunitgroupmembers ougm on ougm.orgunitgroupid = oug.orgunitgroupid and ougm.organisationunitid = ous.idlevel" +
                        i +
                        " " +
                        "inner join orgunitgroupsetmembers ougsm on ougsm.orgunitgroupid = ougm.orgunitgroupid and ougsm.orgunitgroupsetid = " +
                        groupSet.getId() +
                        " " +
                        "limit 1),";
                }

                if (organisationUnitLevels == 0) {
                    sql += "null";
                }

                sql = removeLastComma(sql) + ") as " + quote(groupSet.getName()) + ", ";

                sql += "coalesce(";

                for (int i = organisationUnitLevels; i > 0; i--) {
                    sql +=
                        "(select oug.uid from orgunitgroup oug " +
                        "inner join orgunitgroupmembers ougm on ougm.orgunitgroupid = oug.orgunitgroupid and ougm.organisationunitid = ous.idlevel" +
                        i +
                        " " +
                        "inner join orgunitgroupsetmembers ougsm on ougsm.orgunitgroupid = ougm.orgunitgroupid and ougsm.orgunitgroupsetid = " +
                        groupSet.getId() +
                        " " +
                        "limit 1),";
                }

                if (organisationUnitLevels == 0) {
                    sql += "null";
                }

                sql = removeLastComma(sql) + ") as " + quote(groupSet.getUid()) + ", ";
            }
        }

        sql = removeLastComma(sql) + " ";
        sql += "from organisationunit ou " + "inner join _orgunitstructure ous on ous.organisationunitid = ou.organisationunitid";

        return Optional.of(sql);
    }

    @Override
    public Optional<List<Object[]>> getPopulateTempTableContent() {
        return Optional.empty();
    }

    @Override
    public List<String> getCreateIndexStatements() {
        String nameA = "in_orgunitgroupsetstructure_not_null_" + getRandomSuffix();
        String nameB = "in_orgunitgroupsetstructure_null_" + getRandomSuffix();

        // Two partial indexes as start date can be null

        String indexA =
            "create index " +
            nameA +
            " on " +
            getTempTableName() +
            "(organisationunitid, startdate) " +
            TextUtils.emptyIfFalse("where startdate is not null", supportsPartialIndexes);
        String indexB =
            "create index " +
            nameB +
            " on " +
            getTempTableName() +
            "(organisationunitid, startdate) " +
            TextUtils.emptyIfFalse("where startdate is null", supportsPartialIndexes);

        return Lists.newArrayList(indexA, indexB);
    }
}
