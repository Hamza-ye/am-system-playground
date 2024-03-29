package org.nmcpye.activitiesmanagement.extended.period;

import org.apache.commons.collections4.Predicate;
import org.nmcpye.activitiesmanagement.domain.period.Period;
import org.nmcpye.activitiesmanagement.domain.period.PeriodType;

import java.util.Calendar;
import java.util.Date;

public class PastPeriodPredicate implements Predicate {

    private Date date;

    protected PastPeriodPredicate() {}

    public PastPeriodPredicate(Date d) {
        Calendar cal = PeriodType.createCalendarInstance(d);
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        this.date = cal.getTime();
    }

    @Override
    public boolean evaluate(Object o) {
        return ((Period) o).getEndDate().compareTo(date) <= 0;
    }
}
