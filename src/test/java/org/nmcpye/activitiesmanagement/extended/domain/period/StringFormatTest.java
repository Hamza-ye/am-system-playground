package org.nmcpye.activitiesmanagement.extended.domain.period;

import org.junit.jupiter.api.Test;
import org.nmcpye.activitiesmanagement.domain.period.*;

import java.util.Calendar;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

class StringFormatTest {

    private static Date getDate(int year, int month, int day) {
        final Calendar calendar = Calendar.getInstance();

        // override locale settings for weeks
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.setMinimalDaysInFirstWeek(4);

        calendar.clear();
        calendar.set(year, month - 1, day);

        return calendar.getTime();
    }

    @Test
    void testStringFormat() {
        Period day1 = new Period(new DailyPeriodType(), getDate(2010, 1, 1), getDate(2010, 1, 1));
        Period month1 = new Period(new MonthlyPeriodType(), getDate(2010, 1, 1), getDate(2010, 1, 31));
        Period year1 = new Period(new YearlyPeriodType(), getDate(2010, 1, 1), getDate(2010, 12, 31));
        Period quarter1 = new Period(new QuarterlyPeriodType(), getDate(2010, 1, 1), getDate(2010, 3, 31));
        Period semester1 = new Period(new SixMonthlyPeriodType(), getDate(2010, 1, 1), getDate(2010, 6, 30));
        Period semesterApril1 = new Period(new SixMonthlyAprilPeriodType(), getDate(2010, 4, 1), getDate(2010, 9, 30));
        Period biMonth1 = new Period(new BiMonthlyPeriodType(), getDate(2010, 3, 1), getDate(2010, 4, 30));
        Period financialApril = new Period(new FinancialAprilPeriodType(), getDate(2010, 4, 1), getDate(2011, 3, 31));
        Period financialJuly = new Period(new FinancialJulyPeriodType(), getDate(2010, 7, 1), getDate(2011, 6, 30));
        Period financialOct = new Period(new FinancialOctoberPeriodType(), getDate(2010, 10, 1), getDate(2011, 9, 30));

        assertEquals("20100101", day1.getIsoDate()); // "Day format",
        assertEquals("201001", month1.getIsoDate()); // "Month format",
        assertEquals("2010", year1.getIsoDate()); // "Year format",
        assertEquals("2010Q1", quarter1.getIsoDate()); // "Quarter format",
        assertEquals("2010S1", semester1.getIsoDate()); // "Semester format",
        assertEquals("2010AprilS1", semesterApril1.getIsoDate()); // "SemesterApril format",
        assertEquals("201002B", biMonth1.getIsoDate()); // "Bimonth format",
        assertEquals("2010April", financialApril.getIsoDate()); // "Financial April",
        assertEquals("2010July", financialJuly.getIsoDate()); // "Financial July",
        assertEquals("2010Oct", financialOct.getIsoDate()); // "Financial Oct",

        assertEquals(day1, PeriodType.getPeriodFromIsoString("20100101"));
        assertEquals(month1, PeriodType.getPeriodFromIsoString("201001"));
        assertEquals(year1, PeriodType.getPeriodFromIsoString("2010"));
        assertEquals(quarter1, PeriodType.getPeriodFromIsoString("2010Q1"));
        assertEquals(semester1, PeriodType.getPeriodFromIsoString("2010S1"));
        assertEquals(semesterApril1, PeriodType.getPeriodFromIsoString("2010AprilS1"));
        assertEquals(biMonth1, PeriodType.getPeriodFromIsoString("201002B"));
        assertEquals(financialApril, PeriodType.getPeriodFromIsoString("2010April"));
        assertEquals(financialJuly, PeriodType.getPeriodFromIsoString("2010July"));
        assertEquals(financialOct, PeriodType.getPeriodFromIsoString("2010Oct"));
    }
}
