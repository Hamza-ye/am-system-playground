package org.nmcpye.activitiesmanagement.extended.service;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.PostConstruct;
import org.nmcpye.activitiesmanagement.domain.period.PeriodType;
import org.nmcpye.activitiesmanagement.extended.common.calendar.Calendar;
import org.nmcpye.activitiesmanagement.extended.common.calendar.CalendarComparator;
import org.nmcpye.activitiesmanagement.extended.common.calendar.DateFormat;
import org.nmcpye.activitiesmanagement.extended.common.calendar.DateUnitPeriodTypeParser;
import org.nmcpye.activitiesmanagement.extended.common.calendar.impl.Iso8601Calendar;
import org.nmcpye.activitiesmanagement.extended.period.Cal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DefaultCalendarService implements CalendarService {

    private Set<Calendar> calendars;

    @Autowired
    public DefaultCalendarService(Set<Calendar> calendars) {
        checkNotNull(calendars);
        this.calendars = calendars;
    }

    private Map<String, Calendar> calendarMap = Maps.newHashMap();

    private static final List<DateFormat> DATE_FORMATS = Lists.newArrayList(
        new DateFormat("yyyy-MM-dd", "yyyy-MM-dd", "yyyy-MM-dd", "yyyy-mm-dd"),
        new DateFormat("dd-MM-yyyy", "dd-MM-yyyy", "dd-MM-yyyy", "dd-mm-yyyy")
    );

    // -------------------------------------------------------------------------
    // CalendarService implementation
    // -------------------------------------------------------------------------

    @PostConstruct
    public void init() {
        for (Calendar calendar : calendars) {
            calendarMap.put(calendar.name(), calendar);
        }

        PeriodType.setCalendarService(this);
        Cal.setCalendarService(this);
        DateUnitPeriodTypeParser.setCalendarService(this);
    }

    @Override
    public List<Calendar> getAllCalendars() {
        List<Calendar> sortedCalendars = Lists.newArrayList(calendarMap.values());
        Collections.sort(sortedCalendars, CalendarComparator.INSTANCE);
        return sortedCalendars;
    }

    @Override
    public List<DateFormat> getAllDateFormats() {
        return DATE_FORMATS;
    }

    @Override
    public Calendar getSystemCalendar() {
        String calendarKey = "iso8601"; //(String) settingManager.getSystemSetting( SettingKey.CALENDAR );
        String dateFormat = "yyyy-MM-dd"; //(String) settingManager.getSystemSetting( SettingKey.DATE_FORMAT );

        Calendar calendar = null;

        if (calendarMap.containsKey(calendarKey)) {
            calendar = calendarMap.get(calendarKey);
        } else {
            calendar = Iso8601Calendar.getInstance();
        }

        calendar.setDateFormat(dateFormat);

        return calendar;
    }

    @Override
    public DateFormat getSystemDateFormat() {
        String dateFormatKey = "yyyy-MM-dd"; //(String) settingManager.getSystemSetting( SettingKey.DATE_FORMAT );

        for (DateFormat dateFormat : DATE_FORMATS) {
            if (dateFormat.name().equals(dateFormatKey)) {
                return dateFormat;
            }
        }

        return DATE_FORMATS.get(0);
    }
}
