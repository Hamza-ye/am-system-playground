package org.nmcpye.activitiesmanagement.extended.domain.period;

import org.joda.time.DateTime;
import org.joda.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.nmcpye.activitiesmanagement.domain.period.CalendarPeriodType;
import org.nmcpye.activitiesmanagement.domain.period.FinancialAprilPeriodType;
import org.nmcpye.activitiesmanagement.domain.period.Period;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class FinancialAprilPeriodTypeTest {

    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private LocalDateTime testDate;
    private CalendarPeriodType periodType;

    @BeforeEach
    void before() {
        periodType = new FinancialAprilPeriodType();
    }

    @Test
    void testCreatePeriod() {
        testDate = new LocalDateTime(2009, 2, 15, 0, 0);

        startDate = new LocalDateTime(2008, 4, 1, 0, 0);
        endDate = new LocalDateTime(2009, 3, 31, 0, 0);

        Period period = periodType.createPeriod(testDate.toDate());

        assertThat(startDate.toDate()).isEqualTo(period.getStartDate());
        assertThat(endDate.toDate()).isEqualTo(period.getEndDate());

        testDate = new LocalDateTime(2009, 9, 12, 0, 0);

        period = periodType.createPeriod(testDate.toDate());

        startDate = new LocalDateTime(2009, 4, 1, 0, 0);
        endDate = new LocalDateTime(2010, 3, 31, 0, 0);

        assertThat(startDate.toDate()).isEqualTo(period.getStartDate());
        assertThat(endDate.toDate()).isEqualTo(period.getEndDate());
    }

    @Test
    void testGetNextPeriod() {
        testDate = new LocalDateTime(2009, 2, 15, 0, 0);

        Period period = periodType.createPeriod(testDate.toDate());

        period = periodType.getNextPeriod(period);

        startDate = new LocalDateTime(2009, 4, 1, 0, 0);
        endDate = new LocalDateTime(2010, 3, 31, 0, 0);

        assertThat(startDate.toDate()).isEqualTo(period.getStartDate());
        assertThat(endDate.toDate()).isEqualTo(period.getEndDate());
    }

    @Test
    void testGetPreviousPeriod() {
        testDate = new LocalDateTime(2009, 2, 15, 0, 0);

        Period period = periodType.createPeriod(testDate.toDate());

        period = periodType.getPreviousPeriod(period);

        startDate = new LocalDateTime(2007, 4, 1, 0, 0);
        endDate = new LocalDateTime(2008, 3, 31, 0, 0);

        assertThat(startDate.toDate()).isEqualTo(period.getStartDate());
        assertThat(endDate.toDate()).isEqualTo(period.getEndDate());
    }

    @Test
    void testGeneratePeriods() {
        testDate = new LocalDateTime(2009, 2, 15, 0, 0);

        List<Period> periods = periodType.generatePeriods(testDate.toDate());

        assertThat(11).isEqualTo(periods.size());
        assertThat(periodType.createPeriod(new LocalDateTime(2003, 4, 1, 0, 0).toDate())).isEqualTo(periods.get(0));
        assertThat(periodType.createPeriod(new LocalDateTime(2004, 4, 1, 0, 0).toDate())).isEqualTo(periods.get(1));
        assertThat(periodType.createPeriod(new LocalDateTime(2005, 4, 1, 0, 0).toDate())).isEqualTo(periods.get(2));
        assertThat(periodType.createPeriod(new LocalDateTime(2006, 4, 1, 0, 0).toDate())).isEqualTo(periods.get(3));
        assertThat(periodType.createPeriod(new LocalDateTime(2007, 4, 1, 0, 0).toDate())).isEqualTo(periods.get(4));
        assertThat(periodType.createPeriod(new LocalDateTime(2008, 4, 1, 0, 0).toDate())).isEqualTo(periods.get(5));
        assertThat(periodType.createPeriod(new LocalDateTime(2009, 4, 1, 0, 0).toDate())).isEqualTo(periods.get(6));
        assertThat(periodType.createPeriod(new LocalDateTime(2010, 4, 1, 0, 0).toDate())).isEqualTo(periods.get(7));
        assertThat(periodType.createPeriod(new LocalDateTime(2011, 4, 1, 0, 0).toDate())).isEqualTo(periods.get(8));
        assertThat(periodType.createPeriod(new LocalDateTime(2012, 4, 1, 0, 0).toDate())).isEqualTo(periods.get(9));
        assertThat(periodType.createPeriod(new LocalDateTime(2013, 4, 1, 0, 0).toDate())).isEqualTo(periods.get(10));

        testDate = new LocalDateTime(2009, 9, 12, 0, 0);

        periods = periodType.generatePeriods(testDate.toDate());

        assertThat(11).isEqualTo(periods.size());
        assertThat(periodType.createPeriod(new LocalDateTime(2004, 4, 1, 0, 0).toDate())).isEqualTo(periods.get(0));
    }

    @Test
    void testGetRewindedDate() {
        assertThat(new DateTime(2020, 1, 15, 0, 0).toDate())
            .isEqualTo(periodType.getRewindedDate(new DateTime(2023, 1, 15, 0, 0).toDate(), 3));

        assertThat(new DateTime(2022, 1, 1, 0, 0).toDate())
            .isEqualTo(periodType.getRewindedDate(new DateTime(2020, 1, 1, 0, 0).toDate(), -2));
    }
}
