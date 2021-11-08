package org.nmcpye.activitiesmanagement.extended.administrationModule.resourcetable.table;

import com.google.common.collect.Lists;
import org.joda.time.DateTime;
import org.nmcpye.activitiesmanagement.domain.period.Period;
import org.nmcpye.activitiesmanagement.domain.period.PeriodType;
import org.nmcpye.activitiesmanagement.domain.period.WeeklyAbstractPeriodType;
import org.nmcpye.activitiesmanagement.extended.administrationModule.resourcetable.ResourceTable;
import org.nmcpye.activitiesmanagement.extended.administrationModule.resourcetable.ResourceTableType;
import org.nmcpye.activitiesmanagement.extended.common.IdentifiableObjectUtils;
import org.nmcpye.activitiesmanagement.extended.common.calendar.Calendar;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

import static org.nmcpye.activitiesmanagement.extended.systemmodule.system.util.SqlUtils.quote;

public class PeriodResourceTable extends ResourceTable<Period> {

    private final Logger log = LoggerFactory.getLogger(PeriodResourceTable.class);

    public PeriodResourceTable(List<Period> objects) {
        super(objects);
    }

    @Override
    public ResourceTableType getTableType() {
        return ResourceTableType.PERIOD_STRUCTURE;
    }

    @Override
    public String getCreateTempTableStatement() {
        String sql =
            "create table " +
            getTempTableName() +
            " (periodid bigint not null primary key, iso varchar(15) not null, daysno integer not null, startdate date not null, enddate date not null, year integer not null";

        for (PeriodType periodType : PeriodType.PERIOD_TYPES) {
            sql += ", " + quote(periodType.getName().toLowerCase()) + " varchar(15)";
        }

        sql += ")";

        return sql;
    }

    @Override
    public Optional<String> getPopulateTempTableStatement() {
        return Optional.empty();
    }

    @Override
    public Optional<List<Object[]>> getPopulateTempTableContent() {
        Calendar calendar = PeriodType.getCalendar();

        List<Object[]> batchArgs = new ArrayList<>();

        Set<String> uniqueIsoDates = new HashSet<>();

        for (Period period : objects) {
            if (period != null && period.isValid()) {
                final String isoDate = period.getIsoDate();

                final int year = resolveYearFromPeriod(period);

                if (!uniqueIsoDates.add(isoDate)) {
                    // Protect against duplicates produced by calendar implementations
                    log.warn("Duplicate ISO date for period, ignoring: " + period + ", ISO date: " + isoDate);
                    continue;
                }

                List<Object> values = new ArrayList<>();

                values.add(period.getId());
                values.add(isoDate);
                values.add(period.getDaysInPeriod());
                values.add(period.getStartDate());
                values.add(period.getEndDate());
                values.add(year);

                for (Period pe : PeriodType.getPeriodTypePeriods(period, calendar)) {
                    values.add(pe != null ? IdentifiableObjectUtils.getLocalPeriodIdentifier(pe, calendar) : null);
                }

                batchArgs.add(values.toArray());
            }
        }

        return Optional.of(batchArgs);
    }

    @Override
    public List<String> getCreateIndexStatements() {
        String name = "in_periodstructure_iso_" + getRandomSuffix();

        String sql = "create unique index " + name + " on " + getTempTableName() + "(iso)";

        return Lists.newArrayList(sql);
    }

    /**
     * Resolves the year from the given period.
     * <p>
     * Weekly period types are treated differently from other period types. A week is considered
     * to belong to the year for which 4 days or more fall inside. In this logic, 3 days are added
     * to the week start day and the year of the modified start date is used as reference year for
     * the period.
     *
     * @param period the {@link Period}.
     * @return the year.
     */
    private int resolveYearFromPeriod(Period period) {
        if (WeeklyAbstractPeriodType.class.isAssignableFrom(period.getPeriodType().getClass())) {
            return new DateTime(period.getStartDate().getTime()).plusDays(3).getYear();
        } else {
            return PeriodType.getCalendar().fromIso(period.getStartDate()).getYear();
        }
    }
}
