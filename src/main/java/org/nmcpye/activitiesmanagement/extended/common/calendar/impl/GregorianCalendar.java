package org.nmcpye.activitiesmanagement.extended.common.calendar.impl;

import org.joda.time.DateTimeZone;
import org.joda.time.chrono.GregorianChronology;
import org.nmcpye.activitiesmanagement.extended.common.calendar.Calendar;
import org.nmcpye.activitiesmanagement.extended.common.calendar.ChronologyBasedCalendar;
import org.springframework.stereotype.Component;

@Component
public class GregorianCalendar extends ChronologyBasedCalendar {

    private static final Calendar SELF = new GregorianCalendar();

    protected GregorianCalendar() {
        super(GregorianChronology.getInstance(DateTimeZone.getDefault()));
    }

    public static Calendar getInstance() {
        return SELF;
    }

    @Override
    public String name() {
        return "gregorian";
    }

    @Override
    public boolean isIso8601() {
        return true;
    }
}
