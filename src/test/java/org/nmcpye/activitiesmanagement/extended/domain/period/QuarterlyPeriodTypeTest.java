package org.nmcpye.activitiesmanagement.extended.domain.period;

import org.joda.time.DateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.nmcpye.activitiesmanagement.domain.period.Period;
import org.nmcpye.activitiesmanagement.domain.period.QuarterlyPeriodType;
import org.nmcpye.activitiesmanagement.domain.period.SixMonthlyPeriodType;
import org.nmcpye.activitiesmanagement.domain.period.YearlyPeriodType;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class QuarterlyPeriodTypeTest {

    private DateTime startDate;
    private DateTime endDate;
    private DateTime testDate;
    private QuarterlyPeriodType periodType;

    @BeforeEach
    public void before() {
        periodType = new QuarterlyPeriodType();
    }

    @Test
    public void testCreatePeriod() {
        testDate = new DateTime(2009, 8, 15, 0, 0);

        startDate = new DateTime(2009, 7, 1, 0, 0);
        endDate = new DateTime(2009, 9, 30, 0, 0);

        Period period = periodType.createPeriod(testDate.toDate());

        assertThat(startDate.toDate()).isEqualTo(period.getStartDate());
        assertThat(endDate.toDate()).isEqualTo(period.getEndDate());

        testDate = new DateTime(2009, 4, 15, 0, 0);

        startDate = new DateTime(2009, 4, 1, 0, 0);
        endDate = new DateTime(2009, 6, 30, 0, 0);

        period = periodType.createPeriod(testDate.toDate());

        assertThat(startDate.toDate()).isEqualTo(period.getStartDate());
        assertThat(endDate.toDate()).isEqualTo(period.getEndDate());

        testDate = new DateTime(2014, 11, 20, 0, 0);

        startDate = new DateTime(2014, 10, 1, 0, 0);
        endDate = new DateTime(2014, 12, 31, 0, 0);

        period = periodType.createPeriod(testDate.toDate());

        assertThat(startDate.toDate()).isEqualTo(period.getStartDate());
        assertThat(endDate.toDate()).isEqualTo(period.getEndDate());
    }

    @Test
    public void testCreatePeriodOverflow() {}

    @Test
    public void testGetNextPeriod() {
        testDate = new DateTime(2009, 8, 15, 0, 0);

        Period period = periodType.createPeriod(testDate.toDate());

        period = periodType.getNextPeriod(period);

        startDate = new DateTime(2009, 10, 1, 0, 0);
        endDate = new DateTime(2009, 12, 31, 0, 0);

        assertThat(startDate.toDate()).isEqualTo(period.getStartDate());
        assertThat(endDate.toDate()).isEqualTo(period.getEndDate());
    }

    @Test
    public void testGetNextPeriods() {
        testDate = new DateTime(2009, 8, 15, 0, 0);

        Period period = periodType.createPeriod(testDate.toDate());

        period = periodType.getNextPeriod(period, 3);

        startDate = new DateTime(2010, 4, 1, 0, 0);
        endDate = new DateTime(2010, 6, 30, 0, 0);

        assertThat(startDate.toDate()).isEqualTo(period.getStartDate());
        assertThat(endDate.toDate()).isEqualTo(period.getEndDate());
    }

    @Test
    public void testGetPreviousPeriod() {
        testDate = new DateTime(2009, 8, 15, 0, 0);

        Period period = periodType.createPeriod(testDate.toDate());

        period = periodType.getPreviousPeriod(period);

        startDate = new DateTime(2009, 4, 1, 0, 0);
        endDate = new DateTime(2009, 6, 30, 0, 0);

        assertThat(startDate.toDate()).isEqualTo(period.getStartDate());
        assertThat(endDate.toDate()).isEqualTo(period.getEndDate());
    }

    @Test
    public void testGeneratePeriods() {
        testDate = new DateTime(2009, 8, 15, 0, 0);

        List<Period> periods = periodType.generatePeriods(testDate.toDate());

        assertThat(4).isEqualTo(periods.size());
        assertThat(periodType.createPeriod(new DateTime(2009, 1, 1, 0, 0).toDate())).isEqualTo(periods.get(0));
        assertThat(periodType.createPeriod(new DateTime(2009, 4, 1, 0, 0).toDate())).isEqualTo(periods.get(1));
        assertThat(periodType.createPeriod(new DateTime(2009, 7, 1, 0, 0).toDate())).isEqualTo(periods.get(2));
        assertThat(periodType.createPeriod(new DateTime(2009, 10, 1, 0, 0).toDate())).isEqualTo(periods.get(3));
    }

    @Test
    public void testGenerateRollingPeriods() {
        testDate = new DateTime(2009, 8, 15, 0, 0);

        List<Period> periods = periodType.generateRollingPeriods(testDate.toDate());

        assertThat(4).isEqualTo(periods.size());
        assertThat(periodType.createPeriod(new DateTime(2008, 10, 1, 0, 0).toDate())).isEqualTo(periods.get(0));
        assertThat(periodType.createPeriod(new DateTime(2009, 1, 1, 0, 0).toDate())).isEqualTo(periods.get(1));
        assertThat(periodType.createPeriod(new DateTime(2009, 4, 1, 0, 0).toDate())).isEqualTo(periods.get(2));
        assertThat(periodType.createPeriod(new DateTime(2009, 7, 1, 0, 0).toDate())).isEqualTo(periods.get(3));
    }

    @Test
    public void testGenerateLast5Years() {
        testDate = new DateTime(2009, 8, 15, 0, 0);

        List<Period> periods = periodType.generateLast5Years(testDate.toDate());

        assertThat(20).isEqualTo(periods.size());
        assertThat(periodType.createPeriod(new DateTime(2005, 1, 1, 0, 0).toDate())).isEqualTo(periods.get(0));
        assertThat(periodType.createPeriod(new DateTime(2005, 4, 1, 0, 0).toDate())).isEqualTo(periods.get(1));
        assertThat(periodType.createPeriod(new DateTime(2005, 7, 1, 0, 0).toDate())).isEqualTo(periods.get(2));
        assertThat(periodType.createPeriod(new DateTime(2005, 10, 1, 0, 0).toDate())).isEqualTo(periods.get(3));
    }

    @Test
    public void testGeneratePeriodsBetweenDates() {
        startDate = new DateTime(2009, 8, 15, 0, 0);
        endDate = new DateTime(2010, 2, 20, 0, 0);

        List<Period> periods = periodType.generatePeriods(startDate.toDate(), endDate.toDate());

        assertThat(3).isEqualTo(periods.size());
        assertThat(periodType.createPeriod(new DateTime(2009, 7, 1, 0, 0).toDate())).isEqualTo(periods.get(0));
        assertThat(periodType.createPeriod(new DateTime(2009, 10, 1, 0, 0).toDate())).isEqualTo(periods.get(1));
        assertThat(periodType.createPeriod(new DateTime(2010, 1, 1, 0, 0).toDate())).isEqualTo(periods.get(2));
    }

    @Test
    public void testGetPeriodsBetween() {
        assertThat(1).isEqualTo(periodType.createPeriod().getPeriodSpan(periodType));
        assertThat(2).isEqualTo(new SixMonthlyPeriodType().createPeriod().getPeriodSpan(periodType));
        assertThat(4).isEqualTo(new YearlyPeriodType().createPeriod().getPeriodSpan(periodType));
    }

    @Test
    public void testGetRewindedDate() {
        assertThat(new DateTime(2020, 1, 15, 0, 0).toDate())
            .isEqualTo(periodType.getRewindedDate(new DateTime(2020, 10, 15, 0, 0).toDate(), 3));

        assertThat(new DateTime(2020, 7, 1, 0, 0).toDate())
            .isEqualTo(periodType.getRewindedDate(new DateTime(2020, 1, 1, 0, 0).toDate(), -2));
    }
}
