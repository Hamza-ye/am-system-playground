package org.nmcpye.activitiesmanagement.extended.common.calendar.impl;

import org.joda.time.DateTimeZone;
import org.joda.time.chrono.ISOChronology;
import org.nmcpye.activitiesmanagement.extended.common.calendar.Calendar;
import org.nmcpye.activitiesmanagement.extended.common.calendar.ChronologyBasedCalendar;
import org.springframework.stereotype.Component;

@Component
public class Iso8601Calendar extends ChronologyBasedCalendar {

    private static final Calendar SELF = new Iso8601Calendar();

    protected Iso8601Calendar() {
        super(ISOChronology.getInstance(DateTimeZone.getDefault()));
    }

    public static Calendar getInstance() {
        return SELF;
    }

    @Override
    public String name() {
        return "iso8601";
    }

    @Override
    public boolean isIso8601() {
        return true;
    }
}
