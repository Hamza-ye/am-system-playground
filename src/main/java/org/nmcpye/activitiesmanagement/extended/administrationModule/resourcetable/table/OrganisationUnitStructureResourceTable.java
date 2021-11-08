package org.nmcpye.activitiesmanagement.extended.administrationModule.resourcetable.table;

import com.google.common.collect.Lists;
import org.nmcpye.activitiesmanagement.domain.organisationunit.OrganisationUnit;
import org.nmcpye.activitiesmanagement.extended.organisationunit.OrganisationUnitService;
import org.nmcpye.activitiesmanagement.extended.administrationModule.resourcetable.ResourceTable;
import org.nmcpye.activitiesmanagement.extended.administrationModule.resourcetable.ResourceTableType;

import java.util.*;

import static org.nmcpye.activitiesmanagement.extended.systemmodule.system.util.SqlUtils.quote;

public class OrganisationUnitStructureResourceTable extends ResourceTable<OrganisationUnit> {

    private OrganisationUnitService organisationUnitService; // Nasty

    private int organisationUnitLevels;

    public OrganisationUnitStructureResourceTable(
        List<OrganisationUnit> objects,
        OrganisationUnitService organisationUnitService,
        int organisationUnitLevels
    ) {
        super(objects);
        this.organisationUnitService = organisationUnitService;
        this.organisationUnitLevels = organisationUnitLevels;
    }

    @Override
    public ResourceTableType getTableType() {
        return ResourceTableType.ORG_UNIT_STRUCTURE;
    }

    @Override
    public String getCreateTempTableStatement() {
        StringBuilder sql = new StringBuilder();

        sql
            .append("create table ")
            .append(getTempTableName())
            .append(" (organisationunitid bigint not null primary key, organisationunituid character(11), level integer");

        for (int k = 1; k <= organisationUnitLevels; k++) {
            sql
                .append(", ")
                .append(quote("idlevel" + k))
                .append(" bigint, ")
                .append(quote("uidlevel" + k))
                .append(" character(11), ")
                .append(quote("namelevel" + k))
                .append(" text");
        }

        return sql.append(");").toString();
    }

    @Override
    public Optional<String> getPopulateTempTableStatement() {
        return Optional.empty();
    }

    @Override
    public Optional<List<Object[]>> getPopulateTempTableContent() {
        List<Object[]> batchArgs = new ArrayList<>();

        for (int i = 0; i < organisationUnitLevels; i++) {
            int level = i + 1;

            Collection<OrganisationUnit> units = organisationUnitService.getOrganisationUnitsAtLevel(level);

            for (OrganisationUnit unit : units) {
                List<Object> values = new ArrayList<>();

                values.add(unit.getId());
                values.add(unit.getUid());
                values.add(level);

                Map<Integer, Long> identifiers = new HashMap<>();
                Map<Integer, String> uids = new HashMap<>();
                Map<Integer, String> names = new HashMap<>();

                for (int j = level; j > 0; j--) {
                    identifiers.put(j, unit.getId());
                    uids.put(j, unit.getUid());
                    names.put(j, unit.getName());

                    unit = unit.getParent();
                }

                for (int k = 1; k <= organisationUnitLevels; k++) {
                    values.add(identifiers.get(k) != null ? identifiers.get(k) : null);
                    values.add(uids.get(k));
                    values.add(names.get(k));
                }

                batchArgs.add(values.toArray());
            }
        }

        return Optional.of(batchArgs);
    }

    @Override
    public List<String> getCreateIndexStatements() {
        String name = "in_orgunitstructure_organisationunituid_" + getRandomSuffix();

        String sql = "create unique index " + name + " on " + getTempTableName() + "(organisationunituid)";

        return Lists.newArrayList(sql);
    }
}
