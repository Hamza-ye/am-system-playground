package org.nmcpye.activitiesmanagement.extended.domain.period;

import org.joda.time.DateTime;
import org.junit.jupiter.api.Test;
import org.nmcpye.activitiesmanagement.domain.period.*;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class MonthlyPeriodTypeTest {

    private DateTime startDate;
    private DateTime endDate;
    private DateTime testDate;
    private MonthlyPeriodType periodType = new MonthlyPeriodType();

    @Test
    void testCreatePeriod() {
        testDate = new DateTime(2009, 8, 15, 0, 0);

        startDate = new DateTime(2009, 8, 1, 0, 0);
        endDate = new DateTime(2009, 8, 31, 0, 0);

        Period period = periodType.createPeriod(testDate.toDate());

        assertThat(startDate.toDate()).isEqualTo(period.getStartDate());
        assertThat(endDate.toDate()).isEqualTo(period.getEndDate());

        testDate = new DateTime(2009, 6, 15, 0, 0);

        startDate = new DateTime(2009, 6, 1, 0, 0);
        endDate = new DateTime(2009, 6, 30, 0, 0);

        period = periodType.createPeriod(testDate.toDate());

        assertThat(startDate.toDate()).isEqualTo(period.getStartDate());
        assertThat(endDate.toDate()).isEqualTo(period.getEndDate());
    }

    @Test
    void testCreatePeriodFromISOString() {
        String isoPeriod = "201001";
        String alternativeIsoPeriod = "201001";

        Period period1 = periodType.createPeriod(isoPeriod);
        Period period2 = periodType.createPeriod(alternativeIsoPeriod);

        testDate = new DateTime(period1.getStartDate());
        assertThat(2010).isEqualTo(testDate.getYear());
        assertThat(1).isEqualTo(testDate.getMonthOfYear());

        testDate = new DateTime(period2.getStartDate());
        assertThat(2010).isEqualTo(testDate.getYear());
        assertThat(1).isEqualTo(testDate.getMonthOfYear());
    }

    @Test
    void testGetDaysInPeriod() {
        Period pA = periodType.createPeriod("20040315");
        Period pB = periodType.createPeriod("201403");
        Period pC = periodType.createPeriod("2014Q2");

        assertThat(1).isEqualTo(pA.getDaysInPeriod());
        assertThat(31).isEqualTo(pB.getDaysInPeriod());
        assertThat(91).isEqualTo(pC.getDaysInPeriod());
    }

    @Test
    void testGetNextPeriod() {
        testDate = new DateTime(2009, 8, 15, 0, 0);

        Period period = periodType.createPeriod(testDate.toDate());

        period = periodType.getNextPeriod(period);

        startDate = new DateTime(2009, 9, 1, 0, 0);
        endDate = new DateTime(2009, 9, 30, 0, 0);

        assertThat(startDate.toDate()).isEqualTo(period.getStartDate());
        assertThat(endDate.toDate()).isEqualTo(period.getEndDate());
    }

    @Test
    void testGetNextPeriods() {
        testDate = new DateTime(2009, 8, 15, 0, 0);

        Period period = periodType.createPeriod(testDate.toDate());

        period = periodType.getNextPeriod(period, 3);

        startDate = new DateTime(2009, 11, 1, 0, 0);
        endDate = new DateTime(2009, 11, 30, 0, 0);

        assertThat(startDate.toDate()).isEqualTo(period.getStartDate());
        assertThat(endDate.toDate()).isEqualTo(period.getEndDate());
    }

    @Test
    void testGetPreviousPeriod() {
        testDate = new DateTime(2009, 8, 15, 0, 0);

        Period period = periodType.createPeriod(testDate.toDate());

        period = periodType.getPreviousPeriod(period);

        startDate = new DateTime(2009, 7, 1, 0, 0);
        endDate = new DateTime(2009, 7, 31, 0, 0);

        assertThat(startDate.toDate()).isEqualTo(period.getStartDate());
        assertThat(endDate.toDate()).isEqualTo(period.getEndDate());
    }

    @Test
    void testIsAfter() {
        Period april = periodType.createPeriod(new DateTime(2009, 4, 1, 0, 0).toDate());
        Period may = periodType.createPeriod(new DateTime(2009, 5, 1, 0, 0).toDate());

        assertThat(may.isAfter(april)).isTrue();
        assertThat(april.isAfter(may)).isFalse();
        assertThat(may.isAfter(null)).isFalse();
    }

    @Test
    void testGeneratePeriods() {
        testDate = new DateTime(2009, 8, 15, 0, 0);

        List<Period> periods = periodType.generatePeriods(testDate.toDate());

        assertThat(12).isEqualTo(periods.size());
        assertThat(periodType.createPeriod(new DateTime(2009, 1, 1, 0, 0).toDate())).isEqualTo(periods.get(0));
        assertThat(periodType.createPeriod(new DateTime(2009, 2, 1, 0, 0).toDate())).isEqualTo(periods.get(1));
        assertThat(periodType.createPeriod(new DateTime(2009, 3, 1, 0, 0).toDate())).isEqualTo(periods.get(2));
        assertThat(periodType.createPeriod(new DateTime(2009, 4, 1, 0, 0).toDate())).isEqualTo(periods.get(3));
        assertThat(periodType.createPeriod(new DateTime(2009, 5, 1, 0, 0).toDate())).isEqualTo(periods.get(4));
        assertThat(periodType.createPeriod(new DateTime(2009, 6, 1, 0, 0).toDate())).isEqualTo(periods.get(5));
        assertThat(periodType.createPeriod(new DateTime(2009, 7, 1, 0, 0).toDate())).isEqualTo(periods.get(6));
        assertThat(periodType.createPeriod(new DateTime(2009, 8, 1, 0, 0).toDate())).isEqualTo(periods.get(7));
        assertThat(periodType.createPeriod(new DateTime(2009, 9, 1, 0, 0).toDate())).isEqualTo(periods.get(8));
        assertThat(periodType.createPeriod(new DateTime(2009, 10, 1, 0, 0).toDate())).isEqualTo(periods.get(9));
        assertThat(periodType.createPeriod(new DateTime(2009, 11, 1, 0, 0).toDate())).isEqualTo(periods.get(10));
        assertThat(periodType.createPeriod(new DateTime(2009, 12, 1, 0, 0).toDate())).isEqualTo(periods.get(11));
    }

    @Test
    void testGenerateRollingPeriods() {
        testDate = new DateTime(2009, 8, 15, 0, 0);

        List<Period> periods = periodType.generateRollingPeriods(testDate.toDate());

        assertThat(12).isEqualTo(periods.size());
        assertThat(periodType.createPeriod(new DateTime(2008, 9, 1, 0, 0).toDate())).isEqualTo(periods.get(0));
        assertThat(periodType.createPeriod(new DateTime(2008, 10, 1, 0, 0).toDate())).isEqualTo(periods.get(1));
        assertThat(periodType.createPeriod(new DateTime(2008, 11, 1, 0, 0).toDate())).isEqualTo(periods.get(2));
        assertThat(periodType.createPeriod(new DateTime(2008, 12, 1, 0, 0).toDate())).isEqualTo(periods.get(3));
        assertThat(periodType.createPeriod(new DateTime(2009, 1, 1, 0, 0).toDate())).isEqualTo(periods.get(4));
        assertThat(periodType.createPeriod(new DateTime(2009, 2, 1, 0, 0).toDate())).isEqualTo(periods.get(5));
        assertThat(periodType.createPeriod(new DateTime(2009, 3, 1, 0, 0).toDate())).isEqualTo(periods.get(6));
        assertThat(periodType.createPeriod(new DateTime(2009, 4, 1, 0, 0).toDate())).isEqualTo(periods.get(7));
        assertThat(periodType.createPeriod(new DateTime(2009, 5, 1, 0, 0).toDate())).isEqualTo(periods.get(8));
        assertThat(periodType.createPeriod(new DateTime(2009, 6, 1, 0, 0).toDate())).isEqualTo(periods.get(9));
        assertThat(periodType.createPeriod(new DateTime(2009, 7, 1, 0, 0).toDate())).isEqualTo(periods.get(10));
        assertThat(periodType.createPeriod(new DateTime(2009, 8, 1, 0, 0).toDate())).isEqualTo(periods.get(11));
    }

    @Test
    void testGenerateLast5Years() {
        testDate = new DateTime(2009, 8, 15, 0, 0);

        List<Period> periods = periodType.generateLast5Years(testDate.toDate());

        assertThat(60).isEqualTo(periods.size());
        assertThat(periodType.createPeriod(new DateTime(2005, 1, 1, 0, 0).toDate())).isEqualTo(periods.get(0));
        assertThat(periodType.createPeriod(new DateTime(2005, 2, 1, 0, 0).toDate())).isEqualTo(periods.get(1));
        assertThat(periodType.createPeriod(new DateTime(2005, 3, 1, 0, 0).toDate())).isEqualTo(periods.get(2));
        assertThat(periodType.createPeriod(new DateTime(2005, 4, 1, 0, 0).toDate())).isEqualTo(periods.get(3));
        assertThat(periodType.createPeriod(new DateTime(2005, 5, 1, 0, 0).toDate())).isEqualTo(periods.get(4));
        assertThat(periodType.createPeriod(new DateTime(2005, 6, 1, 0, 0).toDate())).isEqualTo(periods.get(5));
        assertThat(periodType.createPeriod(new DateTime(2005, 7, 1, 0, 0).toDate())).isEqualTo(periods.get(6));
        assertThat(periodType.createPeriod(new DateTime(2005, 8, 1, 0, 0).toDate())).isEqualTo(periods.get(7));
        assertThat(periodType.createPeriod(new DateTime(2005, 9, 1, 0, 0).toDate())).isEqualTo(periods.get(8));
        assertThat(periodType.createPeriod(new DateTime(2005, 10, 1, 0, 0).toDate())).isEqualTo(periods.get(9));
        assertThat(periodType.createPeriod(new DateTime(2005, 11, 1, 0, 0).toDate())).isEqualTo(periods.get(10));
        assertThat(periodType.createPeriod(new DateTime(2005, 12, 1, 0, 0).toDate())).isEqualTo(periods.get(11));
    }

    @Test
    void testGeneratePeriodsBetweenDates() {
        startDate = new DateTime(2009, 8, 15, 0, 0);
        endDate = new DateTime(2010, 2, 20, 0, 0);

        List<Period> periods = periodType.generatePeriods(startDate.toDate(), endDate.toDate());

        assertThat(7).isEqualTo(periods.size());
        assertThat(periodType.createPeriod(new DateTime(2009, 8, 1, 0, 0).toDate())).isEqualTo(periods.get(0));
        assertThat(periodType.createPeriod(new DateTime(2009, 9, 1, 0, 0).toDate())).isEqualTo(periods.get(1));
        assertThat(periodType.createPeriod(new DateTime(2009, 10, 1, 0, 0).toDate())).isEqualTo(periods.get(2));
        assertThat(periodType.createPeriod(new DateTime(2009, 11, 1, 0, 0).toDate())).isEqualTo(periods.get(3));
        assertThat(periodType.createPeriod(new DateTime(2009, 12, 1, 0, 0).toDate())).isEqualTo(periods.get(4));
        assertThat(periodType.createPeriod(new DateTime(2010, 1, 1, 0, 0).toDate())).isEqualTo(periods.get(5));
        assertThat(periodType.createPeriod(new DateTime(2010, 2, 1, 0, 0).toDate())).isEqualTo(periods.get(6));
    }

    @Test
    void testGetPeriodsBetween() {
        assertThat(1).isEqualTo(periodType.createPeriod().getPeriodSpan(periodType));
        assertThat(2).isEqualTo(new BiMonthlyPeriodType().createPeriod().getPeriodSpan(periodType));
        assertThat(3).isEqualTo(new QuarterlyPeriodType().createPeriod().getPeriodSpan(periodType));
        assertThat(6).isEqualTo(new SixMonthlyPeriodType().createPeriod().getPeriodSpan(periodType));
        assertThat(12).isEqualTo(new YearlyPeriodType().createPeriod().getPeriodSpan(periodType));
    }

    @Test
    void testGetRewindedDate() {
        assertThat(new DateTime(2020, 1, 15, 0, 0).toDate())
            .isEqualTo(periodType.getRewindedDate(new DateTime(2020, 4, 15, 0, 0).toDate(), 3));

        assertThat(new DateTime(2020, 3, 1, 0, 0).toDate())
            .isEqualTo(periodType.getRewindedDate(new DateTime(2020, 1, 1, 0, 0).toDate(), -2));
    }
}
