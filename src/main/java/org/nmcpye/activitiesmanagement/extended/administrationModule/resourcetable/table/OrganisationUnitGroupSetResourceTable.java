package org.nmcpye.activitiesmanagement.extended.administrationModule.resourcetable.table;

import com.google.common.collect.Lists;
import org.nmcpye.activitiesmanagement.domain.organisationunit.OrganisationUnitGroupSet;
import org.nmcpye.activitiesmanagement.extended.administrationModule.resourcetable.ResourceTable;
import org.nmcpye.activitiesmanagement.extended.administrationModule.resourcetable.ResourceTableType;
import org.nmcpye.activitiesmanagement.extended.common.util.TextUtils;

import java.util.List;
import java.util.Optional;

import static org.nmcpye.activitiesmanagement.extended.common.util.TextUtils.removeLastComma;
import static org.nmcpye.activitiesmanagement.extended.systemmodule.system.util.SqlUtils.quote;

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
            "select ou.id as organisationunitid, ou.name as organisationunitname, null as startdate, ";

        for (OrganisationUnitGroupSet groupSet : objects) {
            if (!groupSet.isIncludeSubhierarchyInAnalytics()) {
                sql +=
                    "(" +
                    "select oug.name from orgunit_group oug " +
                    "inner join orgunit_group_members ougm on ougm.orgunit_group_id = oug.id " +
                    "inner join orgunit_groupset_members ougsm on ougsm.orgunit_group_id = ougm.orgunit_group_id and ougsm.orgunit_groupset_id = " +
                    groupSet.getId() +
                    " " +
                    "where ougm.organisation_unit_id = ou.id " +
                    "limit 1) as " +
                    quote(groupSet.getName()) +
                    ", ";

                sql +=
                    "(" +
                    "select oug.uid from orgunit_group oug " +
                    "inner join orgunit_group_members ougm on ougm.orgunit_group_id = oug.id " +
                    "inner join orgunit_groupset_members ougsm on ougsm.orgunit_group_id = ougm.orgunit_group_id and ougsm.orgunit_groupset_id = " +
                    groupSet.getId() +
                    " " +
                    "where ougm.organisation_unit_id = ou.id " +
                    "limit 1) as " +
                    quote(groupSet.getUid()) +
                    ", ";
            } else {
                sql += "coalesce(";

                for (int i = organisationUnitLevels; i > 0; i--) {
                    sql +=
                        "(select oug.name from orgunit_group oug " +
                        "inner join orgunit_group_members ougm on ougm.orgunit_group_id = oug.id and ougm.organisation_unit_id = ous.idlevel" +
                        i +
                        " " +
                        "inner join orgunit_groupset_members ougsm on ougsm.orgunit_group_id = ougm.orgunit_group_id and ougsm.orgunit_groupset_id = " +
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
                        "(select oug.uid from orgunit_group oug " +
                        "inner join orgunit_group_members ougm on ougm.orgunit_group_id = oug.id and ougm.organisation_unit_id = ous.idlevel" +
                        i +
                        " " +
                        "inner join orgunit_groupset_members ougsm on ougsm.orgunit_group_id = ougm.orgunit_group_id and ougsm.orgunit_groupset_id = " +
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
        sql += "from organisation_unit ou " + "inner join _orgunitstructure ous on ous.organisationunitid = ou.id";

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
