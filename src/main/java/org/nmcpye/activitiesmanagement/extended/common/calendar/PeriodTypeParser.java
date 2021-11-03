package org.nmcpye.activitiesmanagement.extended.common.calendar;

public interface PeriodTypeParser {
    DateInterval parse(String period);

    DateInterval parse(Calendar calendar, String period);
}
