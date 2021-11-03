package org.nmcpye.activitiesmanagement.extended.service;

import java.util.List;
import org.nmcpye.activitiesmanagement.extended.common.calendar.Calendar;
import org.nmcpye.activitiesmanagement.extended.common.calendar.DateFormat;

/**
 * Simple service for returning all available calendars, and also giving the current system
 * calendar.
 *
 * @see Calendar
 */
public interface CalendarService {
    /**
     * Gets all available calendars as a sorted list.
     *
     * @return All available calendars
     */
    List<Calendar> getAllCalendars();

    /**
     * Gets all available date formats as list.
     *
     * @return All available date formats
     */
    List<DateFormat> getAllDateFormats();

    /**
     * Gets the currently selected system calendar.
     *
     * @return System calendar
     */
    Calendar getSystemCalendar();

    /**
     * Gets the currently selected date format.
     *
     * @return Date format
     * @see DateFormat
     */
    DateFormat getSystemDateFormat();
}
