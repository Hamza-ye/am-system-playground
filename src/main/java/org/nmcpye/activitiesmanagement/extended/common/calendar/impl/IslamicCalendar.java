package org.nmcpye.activitiesmanagement.extended.common.calendar.impl;

import org.joda.time.DateTimeZone;
import org.joda.time.chrono.IslamicChronology;
import org.nmcpye.activitiesmanagement.extended.common.calendar.Calendar;
import org.nmcpye.activitiesmanagement.extended.common.calendar.ChronologyBasedCalendar;
import org.springframework.stereotype.Component;

@Component
public class IslamicCalendar extends ChronologyBasedCalendar {

    private static final Calendar SELF = new IslamicCalendar();

    public static Calendar getInstance() {
        return SELF;
    }

    protected IslamicCalendar() {
        super(IslamicChronology.getInstance(DateTimeZone.getDefault()));
    }

    @Override
    public String name() {
        return "islamic";
    }
}
