package org.nmcpye.activitiesmanagement.extended.domain.period;

import org.joda.time.DateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.nmcpye.activitiesmanagement.domain.period.Period;
import org.nmcpye.activitiesmanagement.domain.period.SixMonthlyAprilPeriodType;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SixMonthlyAprilPeriodTypeTest {

    private DateTime startDate;
    private DateTime endDate;
    private DateTime testDate;
    private SixMonthlyAprilPeriodType periodType;

    @BeforeEach
    void before() {
        periodType = new SixMonthlyAprilPeriodType();
    }

    @Test
    void testCreatePeriod() {
        testDate = new DateTime(2009, 8, 15, 0, 0);

        startDate = new DateTime(2009, 4, 1, 0, 0);
        endDate = new DateTime(2009, 9, 30, 0, 0);

        Period period = periodType.createPeriod(testDate.toDate());

        assertEquals(startDate.toDate(), period.getStartDate());
        assertEquals(endDate.toDate(), period.getEndDate());

        testDate = new DateTime(2009, 12, 15, 0, 0);

        startDate = new DateTime(2009, 10, 1, 0, 0);
        endDate = new DateTime(2010, 3, 31, 0, 0);

        period = periodType.createPeriod(testDate.toDate());

        assertEquals(startDate.toDate(), period.getStartDate());
        assertEquals(endDate.toDate(), period.getEndDate());

        testDate = new DateTime(2009, 7, 15, 0, 0);

        startDate = new DateTime(2009, 4, 1, 0, 0);
        endDate = new DateTime(2009, 9, 30, 0, 0);

        period = periodType.createPeriod(testDate.toDate());

        assertEquals(startDate.toDate(), period.getStartDate());
        assertEquals(endDate.toDate(), period.getEndDate());

        testDate = new DateTime(2015, 2, 1, 0, 0);

        startDate = new DateTime(2014, 10, 1, 0, 0);
        endDate = new DateTime(2015, 3, 31, 0, 0);

        period = periodType.createPeriod(testDate.toDate());

        assertEquals(startDate.toDate(), period.getStartDate());
        assertEquals(endDate.toDate(), period.getEndDate());

        testDate = new DateTime(2014, 3, 31, 0, 0);

        startDate = new DateTime(2013, 10, 1, 0, 0);
        endDate = new DateTime(2014, 3, 31, 0, 0);

        period = periodType.createPeriod(testDate.toDate());

        assertEquals(startDate.toDate(), period.getStartDate());
        assertEquals(endDate.toDate(), period.getEndDate());
    }

    @Test
    void testGetIsoDate() {
        testDate = new DateTime(2015, 2, 1, 0, 0);

        startDate = new DateTime(2014, 10, 1, 0, 0);
        endDate = new DateTime(2015, 3, 31, 0, 0);

        Period period = periodType.createPeriod(testDate.toDate());

        assertEquals(startDate.toDate(), period.getStartDate());
        assertEquals(endDate.toDate(), period.getEndDate());
    }

    @Test
    void testCreatePeriodFromIsoDate() {
        Period period = periodType.createPeriod("2015AprilS1");

        startDate = new DateTime(2015, 4, 1, 0, 0);
        endDate = new DateTime(2015, 9, 30, 0, 0);

        assertEquals(startDate.toDate(), period.getStartDate());
        assertEquals(endDate.toDate(), period.getEndDate());

        period = periodType.createPeriod("2015AprilS2");

        startDate = new DateTime(2015, 10, 1, 0, 0);
        endDate = new DateTime(2016, 3, 31, 0, 0);

        assertEquals(startDate.toDate(), period.getStartDate());
        assertEquals(endDate.toDate(), period.getEndDate());
    }

    @Test
    void testGetNextPeriod() {
        testDate = new DateTime(2009, 8, 15, 0, 0);

        Period period = periodType.createPeriod(testDate.toDate());

        period = periodType.getNextPeriod(period);

        startDate = new DateTime(2009, 10, 1, 0, 0);
        endDate = new DateTime(2010, 3, 31, 0, 0);

        assertEquals(startDate.toDate(), period.getStartDate());
        assertEquals(endDate.toDate(), period.getEndDate());

        testDate = new DateTime(2009, 12, 15, 0, 0);

        period = periodType.createPeriod(testDate.toDate());

        period = periodType.getNextPeriod(period);

        startDate = new DateTime(2010, 4, 1, 0, 0);
        endDate = new DateTime(2010, 9, 30, 0, 0);

        assertEquals(startDate.toDate(), period.getStartDate());
        assertEquals(endDate.toDate(), period.getEndDate());
    }

    @Test
    void testGetPreviousPeriod() {
        testDate = new DateTime(2009, 8, 15, 0, 0);

        Period period = periodType.createPeriod(testDate.toDate());

        period = periodType.getPreviousPeriod(period);

        startDate = new DateTime(2008, 10, 1, 0, 0);
        endDate = new DateTime(2009, 3, 31, 0, 0);

        assertEquals(startDate.toDate(), period.getStartDate());
        assertEquals(endDate.toDate(), period.getEndDate());

        testDate = new DateTime(2009, 12, 15, 0, 0);

        period = periodType.createPeriod(testDate.toDate());

        period = periodType.getPreviousPeriod(period);

        startDate = new DateTime(2009, 4, 1, 0, 0);
        endDate = new DateTime(2009, 9, 30, 0, 0);

        assertEquals(startDate.toDate(), period.getStartDate());
        assertEquals(endDate.toDate(), period.getEndDate());
    }

    @Test
    void testGeneratePeriods() {
        testDate = new DateTime(2009, 8, 15, 0, 0);

        List<Period> periods = periodType.generatePeriods(testDate.toDate());

        assertEquals(2, periods.size());
        assertEquals(periodType.createPeriod(new DateTime(2009, 4, 1, 0, 0).toDate()), periods.get(0));
        assertEquals(periodType.createPeriod(new DateTime(2009, 10, 1, 0, 0).toDate()), periods.get(1));

        testDate = new DateTime(2009, 12, 15, 0, 0);

        periods = periodType.generatePeriods(testDate.toDate());

        assertEquals(2, periods.size());
        assertEquals(periodType.createPeriod(new DateTime(2009, 4, 1, 0, 0).toDate()), periods.get(0));
        assertEquals(periodType.createPeriod(new DateTime(2009, 10, 1, 0, 0).toDate()), periods.get(1));
    }

    @Test
    void testGenerateRollingPeriods() {
        testDate = new DateTime(2009, 8, 15, 0, 0);

        List<Period> periods = periodType.generateRollingPeriods(testDate.toDate());

        assertEquals(2, periods.size());
        assertEquals(periodType.createPeriod(new DateTime(2008, 10, 1, 0, 0).toDate()), periods.get(0));
        assertEquals(periodType.createPeriod(new DateTime(2009, 4, 1, 0, 0).toDate()), periods.get(1));

        testDate = new DateTime(2009, 12, 15, 0, 0);

        periods = periodType.generateRollingPeriods(testDate.toDate());

        assertEquals(2, periods.size());
        assertEquals(periodType.createPeriod(new DateTime(2009, 4, 1, 0, 0).toDate()), periods.get(0));
        assertEquals(periodType.createPeriod(new DateTime(2009, 10, 1, 0, 0).toDate()), periods.get(1));
    }

    @Test
    void testGenerateLast5Years() {
        testDate = new DateTime(2009, 8, 15, 0, 0);

        List<Period> periods = periodType.generateLast5Years(testDate.toDate());

        assertEquals(10, periods.size());
        assertEquals(periodType.createPeriod(new DateTime(2005, 4, 1, 0, 0).toDate()), periods.get(0));
        assertEquals(periodType.createPeriod(new DateTime(2005, 10, 1, 0, 0).toDate()), periods.get(1));
    }

    @Test
    void testGetRewindedDate() {
        assertEquals(new DateTime(2020, 1, 15, 0, 0).toDate(), periodType.getRewindedDate(new DateTime(2021, 7, 15, 0, 0).toDate(), 3));

        assertEquals(new DateTime(2021, 1, 1, 0, 0).toDate(), periodType.getRewindedDate(new DateTime(2020, 1, 1, 0, 0).toDate(), -2));
    }
}
