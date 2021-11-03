package org.nmcpye.activitiesmanagement.extended.domain.period;

import org.joda.time.DateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.nmcpye.activitiesmanagement.domain.period.BiMonthlyPeriodType;
import org.nmcpye.activitiesmanagement.domain.period.Period;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class BiMonthlyPeriodTypeTest {

    private DateTime startDate;
    private DateTime endDate;
    private DateTime testDate;
    private BiMonthlyPeriodType periodType;

    @BeforeEach
    void before() {
        periodType = new BiMonthlyPeriodType();
    }

    @Test
    void testCreatePeriod() {
        startDate = new DateTime(2009, 7, 1, 0, 0);
        endDate = new DateTime(2009, 8, 31, 0, 0);
        testDate = new DateTime(2009, 8, 15, 0, 0);

        Period period = periodType.createPeriod(testDate.toDate());

        assertThat(startDate.toDate()).isEqualTo(period.getStartDate());
        assertThat(endDate.toDate()).isEqualTo(period.getEndDate());

        startDate = new DateTime(2009, 3, 1, 0, 0);
        endDate = new DateTime(2009, 4, 30, 0, 0);
        testDate = new DateTime(2009, 3, 15, 0, 0);

        period = periodType.createPeriod(testDate.toDate());

        assertThat(startDate.toDate()).isEqualTo(period.getStartDate());
        assertThat(endDate.toDate()).isEqualTo(period.getEndDate());
    }

    @Test
    void testGetNextPeriod() {
        testDate = new DateTime(2009, 8, 15, 0, 0);

        Period period = periodType.createPeriod(testDate.toDate());

        period = periodType.getNextPeriod(period);

        startDate = new DateTime(2009, 9, 1, 0, 0);
        endDate = new DateTime(2009, 10, 31, 0, 0);

        assertThat(startDate.toDate()).isEqualTo(period.getStartDate());
        assertThat(endDate.toDate()).isEqualTo(period.getEndDate());
    }

    @Test
    void testGetPreviousPeriod() {
        testDate = new DateTime(2009, 8, 15, 0, 0);

        Period period = periodType.createPeriod(testDate.toDate());

        period = periodType.getPreviousPeriod(period);

        startDate = new DateTime(2009, 5, 1, 0, 0);
        endDate = new DateTime(2009, 6, 30, 0, 0);

        assertThat(startDate.toDate()).isEqualTo(period.getStartDate());
        assertThat(endDate.toDate()).isEqualTo(period.getEndDate());
    }

    @Test
    void testGeneratePeriods() {
        testDate = new DateTime(2009, 8, 15, 0, 0);

        List<Period> periods = periodType.generatePeriods(testDate.toDate());

        assertThat(6).isEqualTo(periods.size());
        assertThat(periodType.createPeriod(new DateTime(2009, 1, 1, 0, 0).toDate())).isEqualTo(periods.get(0));
        assertThat(periodType.createPeriod(new DateTime(2009, 3, 1, 0, 0).toDate())).isEqualTo(periods.get(1));
        assertThat(periodType.createPeriod(new DateTime(2009, 5, 1, 0, 0).toDate())).isEqualTo(periods.get(2));
        assertThat(periodType.createPeriod(new DateTime(2009, 7, 1, 0, 0).toDate())).isEqualTo(periods.get(3));
        assertThat(periodType.createPeriod(new DateTime(2009, 9, 1, 0, 0).toDate())).isEqualTo(periods.get(4));
        assertThat(periodType.createPeriod(new DateTime(2009, 11, 1, 0, 0).toDate())).isEqualTo(periods.get(5));
    }

    @Test
    void testGenerateRollingPeriods() {
        testDate = new DateTime(2009, 8, 15, 0, 0);

        List<Period> periods = periodType.generateRollingPeriods(testDate.toDate());

        assertThat(6).isEqualTo(periods.size());
        assertThat(periodType.createPeriod(new DateTime(2008, 9, 1, 0, 0).toDate())).isEqualTo(periods.get(0));
        assertThat(periodType.createPeriod(new DateTime(2008, 11, 1, 0, 0).toDate())).isEqualTo(periods.get(1));
        assertThat(periodType.createPeriod(new DateTime(2009, 1, 1, 0, 0).toDate())).isEqualTo(periods.get(2));
        assertThat(periodType.createPeriod(new DateTime(2009, 3, 1, 0, 0).toDate())).isEqualTo(periods.get(3));
        assertThat(periodType.createPeriod(new DateTime(2009, 5, 1, 0, 0).toDate())).isEqualTo(periods.get(4));
        assertThat(periodType.createPeriod(new DateTime(2009, 7, 1, 0, 0).toDate())).isEqualTo(periods.get(5));
    }

    @Test
    void testGenerateLast5Years() {
        testDate = new DateTime(2009, 8, 15, 0, 0);

        List<Period> periods = periodType.generateLast5Years(testDate.toDate());

        assertThat(30).isEqualTo(periods.size());
        assertThat(periodType.createPeriod(new DateTime(2005, 1, 1, 0, 0).toDate())).isEqualTo(periods.get(0));
        assertThat(periodType.createPeriod(new DateTime(2005, 3, 1, 0, 0).toDate())).isEqualTo(periods.get(1));
        assertThat(periodType.createPeriod(new DateTime(2005, 5, 1, 0, 0).toDate())).isEqualTo(periods.get(2));
        assertThat(periodType.createPeriod(new DateTime(2005, 7, 1, 0, 0).toDate())).isEqualTo(periods.get(3));
        assertThat(periodType.createPeriod(new DateTime(2005, 9, 1, 0, 0).toDate())).isEqualTo(periods.get(4));
        assertThat(periodType.createPeriod(new DateTime(2005, 11, 1, 0, 0).toDate())).isEqualTo(periods.get(5));
    }

    @Test
    void testGetRewindedDate() {
        assertThat(new DateTime(2020, 1, 5, 0, 0).toDate())
            .isEqualTo(periodType.getRewindedDate(new DateTime(2020, 7, 5, 0, 0).toDate(), 3));

        assertThat(new DateTime(2020, 10, 10, 0, 0).toDate())
            .isEqualTo(periodType.getRewindedDate(new DateTime(2020, 6, 10, 0, 0).toDate(), -2));
    }
}
