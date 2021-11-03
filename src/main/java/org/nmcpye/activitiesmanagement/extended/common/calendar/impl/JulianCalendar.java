package org.nmcpye.activitiesmanagement.extended.common.calendar.impl;

import org.joda.time.DateTimeZone;
import org.joda.time.chrono.JulianChronology;
import org.nmcpye.activitiesmanagement.extended.common.calendar.Calendar;
import org.nmcpye.activitiesmanagement.extended.common.calendar.ChronologyBasedCalendar;
import org.springframework.stereotype.Component;

@Component
public class JulianCalendar extends ChronologyBasedCalendar {

    private static final Calendar self = new JulianCalendar();

    protected JulianCalendar() {
        super(JulianChronology.getInstance(DateTimeZone.getDefault()));
    }

    public static Calendar getInstance() {
        return self;
    }

    @Override
    public String name() {
        return "julian";
    }
}
