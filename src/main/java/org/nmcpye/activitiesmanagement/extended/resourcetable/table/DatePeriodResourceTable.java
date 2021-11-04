package org.nmcpye.activitiesmanagement.extended.resourcetable.table;

import org.nmcpye.activitiesmanagement.domain.period.DailyPeriodType;
import org.nmcpye.activitiesmanagement.domain.period.Period;
import org.nmcpye.activitiesmanagement.domain.period.PeriodType;
import org.nmcpye.activitiesmanagement.extended.common.calendar.Calendar;
import org.nmcpye.activitiesmanagement.extended.common.collection.UniqueArrayList;
import org.nmcpye.activitiesmanagement.extended.period.Cal;
import org.nmcpye.activitiesmanagement.extended.resourcetable.ResourceTable;
import org.nmcpye.activitiesmanagement.extended.resourcetable.ResourceTableType;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.nmcpye.activitiesmanagement.extended.system.util.SqlUtils.quote;

public class DatePeriodResourceTable extends ResourceTable<Period> {

    public DatePeriodResourceTable(List<Period> objects) {
        super(objects);
    }

    @Override
    public ResourceTableType getTableType() {
        return ResourceTableType.DATE_PERIOD_STRUCTURE;
    }

    @Override
    public String getCreateTempTableStatement() {
        String sql = "create table " + getTempTableName() + " (dateperiod date not null primary key, year integer not null";

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
        List<PeriodType> periodTypes = PeriodType.getAvailablePeriodTypes();

        List<Object[]> batchArgs = new ArrayList<>();

        Date startDate = new Cal(1975, 1, 1, true).time(); //TODO Create a dynamic solution instead of fixing the date
        Date endDate = new Cal(2025, 1, 1, true).time();

        List<Period> dailyPeriods = new DailyPeriodType().generatePeriods(startDate, endDate);

        List<Date> days = new UniqueArrayList<>(dailyPeriods.stream().map(Period::getStartDate).collect(Collectors.toList()));

        Calendar calendar = PeriodType.getCalendar();

        for (Date day : days) {
            List<Object> values = new ArrayList<>();

            final int year = PeriodType.getCalendar().fromIso(day).getYear();

            values.add(day);
            values.add(year);

            for (PeriodType periodType : periodTypes) {
                values.add(periodType.createPeriod(day, calendar).getIsoDate());
            }

            batchArgs.add(values.toArray());
        }

        return Optional.of(batchArgs);
    }

    @Override
    public List<String> getCreateIndexStatements() {
        List<String> indexes = new ArrayList<>();

        for (PeriodType periodType : PeriodType.PERIOD_TYPES) {
            String colName = periodType.getName().toLowerCase();
            String indexName = "in" + getTableName() + "_" + colName + "_" + getRandomSuffix();
            String sql = "create index " + indexName + " on " + getTempTableName() + "(" + quote(colName) + ")";
            indexes.add(sql);
        }

        return indexes;
    }
}
