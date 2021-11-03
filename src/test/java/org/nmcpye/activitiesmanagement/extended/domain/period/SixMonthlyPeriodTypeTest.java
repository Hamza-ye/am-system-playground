package org.nmcpye.activitiesmanagement.extended.domain.period;

import org.joda.time.DateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.nmcpye.activitiesmanagement.domain.period.Period;
import org.nmcpye.activitiesmanagement.domain.period.SixMonthlyPeriodType;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SixMonthlyPeriodTypeTest {

    private DateTime startDate;
    private DateTime endDate;
    private DateTime testDate;
    private SixMonthlyPeriodType periodType;

    @BeforeEach
    void before() {
        periodType = new SixMonthlyPeriodType();
    }

    @Test
    void testCreatePeriod() {
        testDate = new DateTime(2009, 8, 15, 0, 0);

        startDate = new DateTime(2009, 7, 1, 0, 0);
        endDate = new DateTime(2009, 12, 31, 0, 0);

        Period period = periodType.createPeriod(testDate.toDate());

        assertEquals(startDate.toDate(), period.getStartDate());
        assertEquals(endDate.toDate(), period.getEndDate());

        testDate = new DateTime(2009, 4, 15, 0, 0);

        startDate = new DateTime(2009, 1, 1, 0, 0);
        endDate = new DateTime(2009, 6, 30, 0, 0);

        period = periodType.createPeriod(testDate.toDate());

        assertEquals(startDate.toDate(), period.getStartDate());
        assertEquals(endDate.toDate(), period.getEndDate());

        testDate = new DateTime(2014, 6, 1, 0, 0);

        startDate = new DateTime(2014, 1, 1, 0, 0);
        endDate = new DateTime(2014, 6, 30, 0, 0);

        period = periodType.createPeriod(testDate.toDate());

        assertEquals(startDate.toDate(), period.getStartDate());
        assertEquals(endDate.toDate(), period.getEndDate());

        testDate = new DateTime(2014, 7, 1, 0, 0);

        startDate = new DateTime(2014, 7, 1, 0, 0);
        endDate = new DateTime(2014, 12, 31, 0, 0);

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
        endDate = new DateTime(2010, 6, 30, 0, 0);

        assertEquals(startDate.toDate(), period.getStartDate());
        assertEquals(endDate.toDate(), period.getEndDate());

        testDate = new DateTime(2009, 4, 15, 0, 0);

        period = periodType.createPeriod(testDate.toDate());

        period = periodType.getNextPeriod(period);

        startDate = new DateTime(2009, 7, 1, 0, 0);
        endDate = new DateTime(2009, 12, 31, 0, 0);

        assertEquals(startDate.toDate(), period.getStartDate());
        assertEquals(endDate.toDate(), period.getEndDate());
    }

    @Test
    void testGetPreviousPeriod() {
        testDate = new DateTime(2009, 8, 15, 0, 0);

        Period period = periodType.createPeriod(testDate.toDate());

        period = periodType.getPreviousPeriod(period);

        startDate = new DateTime(2009, 1, 1, 0, 0);
        endDate = new DateTime(2009, 6, 30, 0, 0);

        assertEquals(startDate.toDate(), period.getStartDate());
        assertEquals(endDate.toDate(), period.getEndDate());

        testDate = new DateTime(2009, 4, 15, 0, 0);

        period = periodType.createPeriod(testDate.toDate());

        period = periodType.getPreviousPeriod(period);

        startDate = new DateTime(2008, 7, 1, 0, 0);
        endDate = new DateTime(2008, 12, 31, 0, 0);

        assertEquals(startDate.toDate(), period.getStartDate());
        assertEquals(endDate.toDate(), period.getEndDate());
    }

    @Test
    void testGeneratePeriods() {
        testDate = new DateTime(2009, 8, 15, 0, 0);

        List<Period> periods = periodType.generatePeriods(testDate.toDate());

        assertEquals(2, periods.size());
        assertEquals(periodType.createPeriod(new DateTime(2009, 1, 1, 0, 0).toDate()), periods.get(0));
        assertEquals(periodType.createPeriod(new DateTime(2009, 7, 1, 0, 0).toDate()), periods.get(1));

        testDate = new DateTime(2009, 4, 15, 0, 0);

        periods = periodType.generatePeriods(testDate.toDate());

        assertEquals(2, periods.size());
        assertEquals(periodType.createPeriod(new DateTime(2009, 1, 1, 0, 0).toDate()), periods.get(0));
        assertEquals(periodType.createPeriod(new DateTime(2009, 7, 1, 0, 0).toDate()), periods.get(1));
    }

    @Test
    void testGenerateRollingPeriods() {
        testDate = new DateTime(2009, 8, 15, 0, 0);

        List<Period> periods = periodType.generateRollingPeriods(testDate.toDate());

        assertEquals(2, periods.size());
        assertEquals(periodType.createPeriod(new DateTime(2009, 1, 1, 0, 0).toDate()), periods.get(0));
        assertEquals(periodType.createPeriod(new DateTime(2009, 7, 1, 0, 0).toDate()), periods.get(1));

        testDate = new DateTime(2009, 4, 15, 0, 0);

        periods = periodType.generateRollingPeriods(testDate.toDate());

        assertEquals(2, periods.size());
        assertEquals(periodType.createPeriod(new DateTime(2008, 7, 1, 0, 0).toDate()), periods.get(0));
        assertEquals(periodType.createPeriod(new DateTime(2009, 1, 1, 0, 0).toDate()), periods.get(1));
    }

    @Test
    void testGenerateLast5Years() {
        testDate = new DateTime(2009, 8, 15, 0, 0);

        List<Period> periods = periodType.generateLast5Years(testDate.toDate());

        assertEquals(10, periods.size());
        assertEquals(periodType.createPeriod(new DateTime(2005, 1, 1, 0, 0).toDate()), periods.get(0));
        assertEquals(periodType.createPeriod(new DateTime(2005, 7, 1, 0, 0).toDate()), periods.get(1));
    }

    @Test
    void testGetRewindedDate() {
        assertEquals(new DateTime(2020, 1, 15, 0, 0).toDate(), periodType.getRewindedDate(new DateTime(2021, 7, 15, 0, 0).toDate(), 3));

        assertEquals(new DateTime(2021, 1, 1, 0, 0).toDate(), periodType.getRewindedDate(new DateTime(2020, 1, 1, 0, 0).toDate(), -2));
    }
}
