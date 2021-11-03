package org.nmcpye.activitiesmanagement.extended.domain.period;

import org.joda.time.DateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.nmcpye.activitiesmanagement.domain.period.CalendarPeriodType;
import org.nmcpye.activitiesmanagement.domain.period.Period;
import org.nmcpye.activitiesmanagement.domain.period.YearlyPeriodType;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class YearlyPeriodTypeTest {

    private DateTime startDate;
    private DateTime endDate;
    private DateTime testDate;
    private CalendarPeriodType periodType;

    @BeforeEach
    void before() {
        periodType = new YearlyPeriodType();
    }

    @Test
    void testCreatePeriod() {
        testDate = new DateTime(2009, 8, 15, 0, 0);

        startDate = new DateTime(2009, 1, 1, 0, 0);
        endDate = new DateTime(2009, 12, 31, 0, 0);

        Period period = periodType.createPeriod(testDate.toDate());

        assertEquals(startDate.toDate(), period.getStartDate());
        assertEquals(endDate.toDate(), period.getEndDate());

        testDate = new DateTime(2009, 4, 15, 0, 0);

        period = periodType.createPeriod(testDate.toDate());

        assertEquals(startDate.toDate(), period.getStartDate());
        assertEquals(endDate.toDate(), period.getEndDate());
    }

    @Test
    void testGetNextPeriod() {
        testDate = new DateTime(2009, 8, 15, 0, 0);

        Period period = periodType.createPeriod(testDate.toDate());

        period = periodType.getNextPeriod(period);

        startDate = new DateTime(2010, 1, 1, 0, 0);
        endDate = new DateTime(2010, 12, 31, 0, 0);

        assertEquals(startDate.toDate(), period.getStartDate());
        assertEquals(endDate.toDate(), period.getEndDate());
    }

    @Test
    void testGetPreviousPeriod() {
        testDate = new DateTime(2009, 8, 15, 0, 0);

        Period period = periodType.createPeriod(testDate.toDate());

        period = periodType.getPreviousPeriod(period);

        startDate = new DateTime(2008, 1, 1, 0, 0);
        endDate = new DateTime(2008, 12, 31, 0, 0);

        assertEquals(startDate.toDate(), period.getStartDate());
        assertEquals(endDate.toDate(), period.getEndDate());
    }

    @Test
    void testGeneratePeriods() {
        testDate = new DateTime(2009, 8, 15, 0, 0);

        List<Period> periods = periodType.generatePeriods(testDate.toDate());

        assertEquals(11, periods.size());
        assertEquals(periodType.createPeriod(new DateTime(2004, 1, 1, 0, 0).toDate()), periods.get(0));
        assertEquals(periodType.createPeriod(new DateTime(2005, 1, 1, 0, 0).toDate()), periods.get(1));
        assertEquals(periodType.createPeriod(new DateTime(2006, 1, 1, 0, 0).toDate()), periods.get(2));
        assertEquals(periodType.createPeriod(new DateTime(2007, 1, 1, 0, 0).toDate()), periods.get(3));
        assertEquals(periodType.createPeriod(new DateTime(2008, 1, 1, 0, 0).toDate()), periods.get(4));
        assertEquals(periodType.createPeriod(new DateTime(2009, 1, 1, 0, 0).toDate()), periods.get(5));
        assertEquals(periodType.createPeriod(new DateTime(2010, 1, 1, 0, 0).toDate()), periods.get(6));
        assertEquals(periodType.createPeriod(new DateTime(2011, 1, 1, 0, 0).toDate()), periods.get(7));
        assertEquals(periodType.createPeriod(new DateTime(2012, 1, 1, 0, 0).toDate()), periods.get(8));
        assertEquals(periodType.createPeriod(new DateTime(2013, 1, 1, 0, 0).toDate()), periods.get(9));
        assertEquals(periodType.createPeriod(new DateTime(2014, 1, 1, 0, 0).toDate()), periods.get(10));
    }

    @Test
    void testGenerateLast5Years() {
        testDate = new DateTime(2009, 8, 15, 0, 0);

        List<Period> periods = new YearlyPeriodType().generateLast5Years(testDate.toDate());

        assertEquals(5, periods.size());
        assertEquals(periodType.createPeriod(new DateTime(2005, 1, 1, 0, 0).toDate()), periods.get(0));
        assertEquals(periodType.createPeriod(new DateTime(2006, 1, 1, 0, 0).toDate()), periods.get(1));
        assertEquals(periodType.createPeriod(new DateTime(2007, 1, 1, 0, 0).toDate()), periods.get(2));
        assertEquals(periodType.createPeriod(new DateTime(2008, 1, 1, 0, 0).toDate()), periods.get(3));
        assertEquals(periodType.createPeriod(new DateTime(2009, 1, 1, 0, 0).toDate()), periods.get(4));
    }

    @Test
    void testGetRewindedDate() {
        assertEquals(new DateTime(2020, 1, 15, 0, 0).toDate(), periodType.getRewindedDate(new DateTime(2023, 1, 15, 0, 0).toDate(), 3));

        assertEquals(new DateTime(2022, 1, 1, 0, 0).toDate(), periodType.getRewindedDate(new DateTime(2020, 1, 1, 0, 0).toDate(), -2));
    }
}
