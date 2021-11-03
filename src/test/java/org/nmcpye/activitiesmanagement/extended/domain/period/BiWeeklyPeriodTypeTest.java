package org.nmcpye.activitiesmanagement.extended.domain.period;

import org.joda.time.DateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.nmcpye.activitiesmanagement.domain.period.BiWeeklyPeriodType;
import org.nmcpye.activitiesmanagement.domain.period.Period;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class BiWeeklyPeriodTypeTest {

    private DateTime startDate;
    private DateTime endDate;
    private DateTime testDate;
    private BiWeeklyPeriodType periodType;

    @BeforeEach
    void before() {
        periodType = new BiWeeklyPeriodType();
    }

    @Test
    void testCreatePeriod() {
        startDate = new DateTime(2018, 1, 1, 0, 0);
        endDate = new DateTime(2018, 1, 14, 0, 0);
        testDate = new DateTime(2018, 1, 8, 0, 0);

        Period period = periodType.createPeriod(testDate.toDate());

        assertThat(startDate.toDate()).isEqualTo(period.getStartDate());
        assertThat(endDate.toDate()).isEqualTo(period.getEndDate());

        startDate = new DateTime(2017, 12, 18, 0, 0);
        endDate = new DateTime(2017, 12, 31, 0, 0);
        testDate = new DateTime(2017, 12, 29, 0, 0);

        period = periodType.createPeriod(testDate.toDate());

        assertThat(startDate.toDate()).isEqualTo(period.getStartDate());
        assertThat(endDate.toDate()).isEqualTo(period.getEndDate());
    }

    @Test
    void testGetNextPeriod() {
        testDate = new DateTime(2018, 1, 3, 0, 0);

        Period period = periodType.createPeriod(testDate.toDate());

        period = periodType.getNextPeriod(period);

        startDate = new DateTime(2018, 1, 15, 0, 0);
        endDate = new DateTime(2018, 1, 28, 0, 0);

        assertThat(startDate.toDate()).isEqualTo(period.getStartDate());
        assertThat(endDate.toDate()).isEqualTo(period.getEndDate());
    }

    @Test
    void testGetPreviousPeriod() {
        testDate = new DateTime(2018, 1, 4, 0, 0);

        Period period = periodType.createPeriod(testDate.toDate());

        period = periodType.getPreviousPeriod(period);

        startDate = new DateTime(2017, 12, 18, 0, 0);
        endDate = new DateTime(2017, 12, 31, 0, 0);

        assertThat(startDate.toDate()).isEqualTo(period.getStartDate());
        assertThat(endDate.toDate()).isEqualTo(period.getEndDate());
    }

    @Test
    void testGeneratePeriods() {
        testDate = new DateTime(2018, 10, 5, 0, 0);

        List<Period> periods = periodType.generatePeriods(testDate.toDate());

        assertThat(26).isEqualTo(periods.size());
        assertThat(periodType.createPeriod(new DateTime(2018, 1, 1, 0, 0).toDate())).isEqualTo(periods.get(0));
        assertThat(periodType.createPeriod(new DateTime(2018, 1, 15, 0, 0).toDate())).isEqualTo(periods.get(1));
        assertThat(periodType.createPeriod(new DateTime(2018, 1, 31, 0, 0).toDate())).isEqualTo(periods.get(2));
    }

    @Test
    void testGenerateRollingPeriods() {
        testDate = new DateTime(2018, 1, 1, 0, 0);

        List<Period> periods = periodType.generateRollingPeriods(testDate.toDate());

        assertThat(26).isEqualTo(periods.size());
        assertThat(periodType.createPeriod(new DateTime(2017, 1, 16, 0, 0).toDate())).isEqualTo(periods.get(0));
        assertThat(periodType.createPeriod(new DateTime(2017, 1, 30, 0, 0).toDate())).isEqualTo(periods.get(1));
        assertThat(periodType.createPeriod(new DateTime(2017, 2, 13, 0, 0).toDate())).isEqualTo(periods.get(2));
        assertThat(periodType.createPeriod(new DateTime(2017, 2, 28, 0, 0).toDate())).isEqualTo(periods.get(3));
        assertThat(periodType.createPeriod(new DateTime(2017, 3, 14, 0, 0).toDate())).isEqualTo(periods.get(4));
        assertThat(periodType.createPeriod(new DateTime(2017, 3, 29, 0, 0).toDate())).isEqualTo(periods.get(5));
        assertThat(periodType.createPeriod(testDate.toDate())).isEqualTo(periods.get(periods.size() - 1));
    }

    @Test
    void testToIsoDate() {
        testDate = new DateTime(2018, 1, 1, 0, 0);
        List<Period> periods = periodType.generateRollingPeriods(testDate.toDate());

        assertThat("2017BiW2").isEqualTo(periodType.getIsoDate(periods.get(0)));
        assertThat("2018BiW1").isEqualTo(periodType.getIsoDate(periods.get(25)));

        testDate = new DateTime(2019, 1, 1, 0, 0);
        periods = periodType.generateRollingPeriods(testDate.toDate());

        assertThat("2018BiW2").isEqualTo(periodType.getIsoDate(periods.get(0)));
        assertThat("2019BiW1").isEqualTo(periodType.getIsoDate(periods.get(25)));

        testDate = new DateTime(2010, 1, 1, 0, 0);
        periods = periodType.generateRollingPeriods(testDate.toDate());

        assertThat("2009BiW2").isEqualTo(periodType.getIsoDate(periods.get(0)));
        assertThat("2009BiW27").isEqualTo(periodType.getIsoDate(periods.get(25)));

        testDate = new DateTime(2020, 1, 1, 0, 0);
        periods = periodType.generateRollingPeriods(testDate.toDate());

        assertThat("2019BiW2").isEqualTo(periodType.getIsoDate(periods.get(0)));
        assertThat("2020BiW1").isEqualTo(periodType.getIsoDate(periods.get(25)));
    }

    @Test
    void testGetRewindedDate() {
        assertThat(new DateTime(2020, 1, 3, 0, 0).toDate())
            .isEqualTo(periodType.getRewindedDate(new DateTime(2020, 2, 14, 0, 0).toDate(), 3));

        assertThat(new DateTime(2020, 1, 31, 0, 0).toDate())
            .isEqualTo(periodType.getRewindedDate(new DateTime(2020, 1, 3, 0, 0).toDate(), -2));
    }
}
