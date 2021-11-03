package org.nmcpye.activitiesmanagement.extended.common.calendar.impl;

import org.joda.time.DateTimeZone;
import org.joda.time.chrono.BuddhistChronology;
import org.nmcpye.activitiesmanagement.extended.common.calendar.Calendar;
import org.nmcpye.activitiesmanagement.extended.common.calendar.ChronologyBasedCalendar;
import org.springframework.stereotype.Component;

@Component
public class ThaiCalendar extends ChronologyBasedCalendar {

    private static final Calendar SELF = new ThaiCalendar();

    protected ThaiCalendar() {
        super(BuddhistChronology.getInstance(DateTimeZone.getDefault()));
    }

    public static Calendar getInstance() {
        return SELF;
    }

    @Override
    public String name() {
        return "thai";
    }
}
