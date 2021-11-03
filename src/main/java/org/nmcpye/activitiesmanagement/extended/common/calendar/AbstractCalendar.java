package org.nmcpye.activitiesmanagement.extended.common.calendar;

import com.google.common.collect.Lists;
import java.util.List;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public abstract class AbstractCalendar implements Calendar {

    protected static final String[] DEFAULT_I18N_MONTH_NAMES = new String[] {
        "month.january",
        "month.february",
        "month.march",
        "month.april",
        "month.may",
        "month.june",
        "month.july",
        "month.august",
        "month.september",
        "month.october",
        "month.november",
        "month.december",
    };

    protected static final String[] DEFAULT_I18N_MONTH_SHORT_NAMES = new String[] {
        "month.short.january",
        "month.short.february",
        "month.short.march",
        "month.short.april",
        "month.short.may",
        "month.short.june",
        "month.short.july",
        "month.short.august",
        "month.short.september",
        "month.short.october",
        "month.short.november",
        "month.short.december",
    };

    protected static final String[] DEFAULT_I18N_DAY_NAMES = new String[] {
        "weekday.monday",
        "weekday.tuesday",
        "weekday.wednesday",
        "weekday.thursday",
        "weekday.friday",
        "weekday.saturday",
        "weekday.sunday",
    };

    protected static final String[] DEFAULT_I18N_DAY_SHORT_NAMES = new String[] {
        "weekday.short.monday",
        "weekday.short.tuesday",
        "weekday.short.wednesday",
        "weekday.short.thursday",
        "weekday.short.friday",
        "weekday.short.saturday",
        "weekday.short.sunday",
    };

    protected static final String DEFAULT_ISO8601_DATE_FORMAT = "yyyy-MM-dd";

    protected String dateFormat = DEFAULT_ISO8601_DATE_FORMAT;

    @Override
    public String getDateFormat() {
        return dateFormat;
    }

    @Override
    public void setDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
    }

    @Override
    public String formattedDate(DateTimeUnit dateTimeUnit) {
        return getDateFormat()
            .replace("yyyy", String.format("%04d", dateTimeUnit.getYear()))
            .replace("MM", String.format("%02d", dateTimeUnit.getMonth()))
            .replace("dd", String.format("%02d", dateTimeUnit.getDay()));
    }

    @Override
    public String formattedDate(String dateFormat, DateTimeUnit dateTimeUnit) {
        return dateFormat
            .replace("yyyy", String.format("%04d", dateTimeUnit.getYear()))
            .replace("MM", String.format("%02d", dateTimeUnit.getMonth()))
            .replace("dd", String.format("%02d", dateTimeUnit.getDay()));
    }

    @Override
    public String formattedIsoDate(DateTimeUnit dateTimeUnit) {
        dateTimeUnit = toIso(dateTimeUnit);
        DateTime dateTime = dateTimeUnit.toJodaDateTime();
        DateTimeFormatter format = DateTimeFormat.forPattern(getDateFormat());

        return format.print(dateTime);
    }

    @Override
    public DateTimeUnit toIso(int year, int month, int day) {
        return toIso(new DateTimeUnit(year, month, day));
    }

    @Override
    public DateTimeUnit toIso(String date) {
        DateTimeFormatter format = DateTimeFormat.forPattern(getDateFormat());
        DateTime dateTime = format.parseDateTime(date);

        return toIso(DateTimeUnit.fromJodaDateTime(dateTime));
    }

    @Override
    public DateTimeUnit fromIso(int year, int month, int day) {
        return fromIso(new DateTimeUnit(year, month, day, true));
    }

    @Override
    public DateInterval toInterval(DateIntervalType type) {
        return toInterval(today(), type);
    }

    @Override
    public DateInterval toInterval(DateTimeUnit dateTimeUnit, DateIntervalType type) {
        return toInterval(dateTimeUnit, type, 0, 1);
    }

    @Override
    public DateInterval toInterval(DateIntervalType type, int offset, int length) {
        return toInterval(today(), type, offset, length);
    }

    @Override
    public List<DateInterval> toIntervals(DateTimeUnit dateTimeUnit, DateIntervalType type, int offset, int length, int periods) {
        List<DateInterval> dateIntervals = Lists.newArrayList();

        for (int i = offset; i <= (offset + periods - 1); i++) {
            dateIntervals.add(toInterval(dateTimeUnit, type, i, length));
        }

        return dateIntervals;
    }

    @Override
    public DateTimeUnit today() {
        DateTime dateTime = DateTime.now(ISOChronology.getInstance(DateTimeZone.getDefault()));
        DateTimeUnit dateTimeUnit = new DateTimeUnit(dateTime.getYear(), dateTime.getMonthOfYear(), dateTime.getDayOfMonth(), true);
        return fromIso(dateTimeUnit);
    }

    @Override
    public int monthsInYear() {
        return 12;
    }

    @Override
    public int daysInWeek() {
        return 7;
    }

    @Override
    public String nameOfMonth(int month) {
        if (month > DEFAULT_I18N_MONTH_NAMES.length || month <= 0) {
            return null;
        }

        return DEFAULT_I18N_MONTH_NAMES[month - 1];
    }

    @Override
    public String shortNameOfMonth(int month) {
        if (month > DEFAULT_I18N_MONTH_SHORT_NAMES.length || month <= 0) {
            return null;
        }

        return DEFAULT_I18N_MONTH_SHORT_NAMES[month - 1];
    }

    @Override
    public String nameOfDay(int day) {
        if (day > DEFAULT_I18N_DAY_NAMES.length || day <= 0) {
            return null;
        }

        return DEFAULT_I18N_DAY_NAMES[day - 1];
    }

    @Override
    public String shortNameOfDay(int day) {
        if (day > DEFAULT_I18N_DAY_SHORT_NAMES.length || day <= 0) {
            return null;
        }

        return DEFAULT_I18N_DAY_SHORT_NAMES[day - 1];
    }

    @Override
    public boolean isIso8601() {
        return false;
    }

    @Override
    public DateTimeUnit isoStartOfYear(int year) {
        return new DateTimeUnit(year, 1, 1);
    }

    @Override
    public boolean isValid(DateTimeUnit dateTime) {
        if (dateTime.getMonth() < 1 || dateTime.getMonth() > monthsInYear()) {
            return false;
        }

        return dateTime.getDay() >= 1 && dateTime.getDay() <= daysInMonth(dateTime.getYear(), dateTime.getMonth());
    }
}
