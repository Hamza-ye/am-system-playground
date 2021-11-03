package org.nmcpye.activitiesmanagement.extended.domain.period;

import org.joda.time.DateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.nmcpye.activitiesmanagement.domain.period.CalendarPeriodType;
import org.nmcpye.activitiesmanagement.domain.period.FinancialJulyPeriodType;
import org.nmcpye.activitiesmanagement.domain.period.Period;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class FinancialJulyPeriodTypeTest {

    private DateTime startDate;
    private DateTime endDate;
    private DateTime testDate;
    private CalendarPeriodType periodType;

    @BeforeEach
    void before() {
        periodType = new FinancialJulyPeriodType();
    }

    @Test
    void testCreatePeriod() {
        testDate = new DateTime(2009, 2, 15, 0, 0);

        startDate = new DateTime(2008, 7, 1, 0, 0);
        endDate = new DateTime(2009, 6, 30, 0, 0);

        Period period = periodType.createPeriod(testDate.toDate());

        assertThat(startDate.toDate()).isEqualTo(period.getStartDate());
        assertThat(endDate.toDate()).isEqualTo(period.getEndDate());

        testDate = new DateTime(2009, 9, 12, 0, 0);

        period = periodType.createPeriod(testDate.toDate());

        startDate = new DateTime(2009, 7, 1, 0, 0);
        endDate = new DateTime(2010, 6, 30, 0, 0);

        assertThat(startDate.toDate()).isEqualTo(period.getStartDate());
        assertThat(endDate.toDate()).isEqualTo(period.getEndDate());
    }

    @Test
    void testGetNextPeriod() {
        testDate = new DateTime(2009, 2, 15, 0, 0);

        Period period = periodType.createPeriod(testDate.toDate());

        period = periodType.getNextPeriod(period);

        startDate = new DateTime(2009, 7, 1, 0, 0);
        endDate = new DateTime(2010, 6, 30, 0, 0);

        assertThat(startDate.toDate()).isEqualTo(period.getStartDate());
        assertThat(endDate.toDate()).isEqualTo(period.getEndDate());
    }

    @Test
    void testGetPreviousPeriod() {
        testDate = new DateTime(2009, 2, 15, 0, 0);

        Period period = periodType.createPeriod(testDate.toDate());

        period = periodType.getPreviousPeriod(period);

        startDate = new DateTime(2007, 7, 1, 0, 0);
        endDate = new DateTime(2008, 6, 30, 0, 0);

        assertThat(startDate.toDate()).isEqualTo(period.getStartDate());
        assertThat(endDate.toDate()).isEqualTo(period.getEndDate());
    }

    @Test
    void testGeneratePeriods() {
        testDate = new DateTime(2009, 2, 15, 0, 0);

        List<Period> periods = periodType.generatePeriods(testDate.toDate());

        assertThat(11).isEqualTo(periods.size());
        assertThat(periodType.createPeriod(new DateTime(2003, 7, 1, 0, 0).toDate())).isEqualTo(periods.get(0));
        assertThat(periodType.createPeriod(new DateTime(2004, 7, 1, 0, 0).toDate())).isEqualTo(periods.get(1));
        assertThat(periodType.createPeriod(new DateTime(2005, 7, 1, 0, 0).toDate())).isEqualTo(periods.get(2));
        assertThat(periodType.createPeriod(new DateTime(2006, 7, 1, 0, 0).toDate())).isEqualTo(periods.get(3));
        assertThat(periodType.createPeriod(new DateTime(2007, 7, 1, 0, 0).toDate())).isEqualTo(periods.get(4));
        assertThat(periodType.createPeriod(new DateTime(2008, 7, 1, 0, 0).toDate())).isEqualTo(periods.get(5));
        assertThat(periodType.createPeriod(new DateTime(2009, 7, 1, 0, 0).toDate())).isEqualTo(periods.get(6));
        assertThat(periodType.createPeriod(new DateTime(2010, 7, 1, 0, 0).toDate())).isEqualTo(periods.get(7));
        assertThat(periodType.createPeriod(new DateTime(2011, 7, 1, 0, 0).toDate())).isEqualTo(periods.get(8));
        assertThat(periodType.createPeriod(new DateTime(2012, 7, 1, 0, 0).toDate())).isEqualTo(periods.get(9));
        assertThat(periodType.createPeriod(new DateTime(2013, 7, 1, 0, 0).toDate())).isEqualTo(periods.get(10));

        testDate = new DateTime(2009, 9, 12, 0, 0);

        periods = periodType.generatePeriods(testDate.toDate());

        assertThat(11).isEqualTo(periods.size());
        assertThat(periodType.createPeriod(new DateTime(2004, 7, 1, 0, 0).toDate())).isEqualTo(periods.get(0));
    }

    @Test
    void testGetRewindedDate() {
        assertThat(new DateTime(2020, 1, 15, 0, 0).toDate())
            .isEqualTo(periodType.getRewindedDate(new DateTime(2023, 1, 15, 0, 0).toDate(), 3));

        assertThat(new DateTime(2022, 1, 1, 0, 0).toDate())
            .isEqualTo(periodType.getRewindedDate(new DateTime(2020, 1, 1, 0, 0).toDate(), -2));
    }
}
