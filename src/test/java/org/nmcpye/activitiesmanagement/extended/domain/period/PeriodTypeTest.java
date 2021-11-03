package org.nmcpye.activitiesmanagement.extended.domain.period;

import org.junit.jupiter.api.Test;
import org.nmcpye.activitiesmanagement.domain.period.*;
import org.nmcpye.activitiesmanagement.extended.common.calendar.Calendar;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class PeriodTypeTest {

    @Test
    void testGetByIndex() {
        assertThat(PeriodType.getByIndex(-1)).isNull();

        PeriodType yearly = PeriodType.getByNameIgnoreCase("Yearly");
        assertThat(yearly).isNotNull();

        int yearlyIndex = PeriodType.getAvailablePeriodTypes().indexOf(yearly) + 1;
        assertThat(new YearlyPeriodType()).isEqualTo(PeriodType.getByIndex(yearlyIndex));
        assertThat(PeriodType.getByIndex(999)).isNull();
    }

    @Test
    void testGetPeriodTypeFromIsoString() {
        assertThat(PeriodType.getPeriodTypeFromIsoString("2011").getName()).isEqualTo("Yearly");
        assertThat(PeriodType.getPeriodTypeFromIsoString("201101").getName()).isEqualTo("Monthly");
        assertThat(PeriodType.getPeriodTypeFromIsoString("2011W1").getName()).isEqualTo("Weekly");
        assertThat(PeriodType.getPeriodTypeFromIsoString("2011W32").getName()).isEqualTo("Weekly");
        assertThat(PeriodType.getPeriodTypeFromIsoString("20110101").getName()).isEqualTo("Daily");
        assertThat(PeriodType.getPeriodTypeFromIsoString("2011Q3").getName()).isEqualTo("Quarterly");
        assertThat(PeriodType.getPeriodTypeFromIsoString("201101B").getName()).isEqualTo("BiMonthly");
        assertThat(PeriodType.getPeriodTypeFromIsoString("2011S1").getName()).isEqualTo("SixMonthly");
        assertThat(PeriodType.getPeriodTypeFromIsoString("2011AprilS1").getName()).isEqualTo("SixMonthlyApril");
        assertThat(PeriodType.getPeriodTypeFromIsoString("2011April").getName()).isEqualTo("FinancialApril");
        assertThat(PeriodType.getPeriodTypeFromIsoString("2011July").getName()).isEqualTo("FinancialJuly");
        assertThat(PeriodType.getPeriodTypeFromIsoString("2011Oct").getName()).isEqualTo("FinancialOct");

        assertThat(PeriodType.getPeriodTypeFromIsoString("201")).isNull();
        assertThat(PeriodType.getPeriodTypeFromIsoString("20111")).isNull();
        assertThat(PeriodType.getPeriodTypeFromIsoString("201W2")).isNull();
        assertThat(PeriodType.getPeriodTypeFromIsoString("2011Q12")).isNull();
        assertThat(PeriodType.getPeriodTypeFromIsoString("2011W234")).isNull();
        assertThat(PeriodType.getPeriodTypeFromIsoString("201er2345566")).isNull();
        assertThat(PeriodType.getPeriodTypeFromIsoString("2011Q10")).isNull();
    }

    @Test
    void testGetIsoDurationFromIsoString() {
        assertThat(PeriodType.getPeriodTypeFromIsoString("2011").getIso8601Duration()).isEqualTo("P1Y");
        assertThat(PeriodType.getPeriodTypeFromIsoString("201101").getIso8601Duration()).isEqualTo("P1M");
        assertThat(PeriodType.getPeriodTypeFromIsoString("2011W1").getIso8601Duration()).isEqualTo("P7D");
        assertThat(PeriodType.getPeriodTypeFromIsoString("20110101").getIso8601Duration()).isEqualTo("P1D");
        assertThat(PeriodType.getPeriodTypeFromIsoString("2011Q3").getIso8601Duration()).isEqualTo("P3M");
        assertThat(PeriodType.getPeriodTypeFromIsoString("201101B").getIso8601Duration()).isEqualTo("P2M");
        assertThat(PeriodType.getPeriodTypeFromIsoString("2011S1").getIso8601Duration()).isEqualTo("P6M");
        assertThat(PeriodType.getPeriodTypeFromIsoString("2011AprilS1").getIso8601Duration()).isEqualTo("P6M");
        assertThat(PeriodType.getPeriodTypeFromIsoString("2011April").getIso8601Duration()).isEqualTo("P1Y");
        assertThat(PeriodType.getPeriodTypeFromIsoString("2011July").getIso8601Duration()).isEqualTo("P1Y");
        assertThat(PeriodType.getPeriodTypeFromIsoString("2011Oct").getIso8601Duration()).isEqualTo("P1Y");
    }

    @Test
    void testGetPeriodTypePeriods() {
        Calendar calendar = PeriodType.getCalendar();

        Period jan2018 = PeriodType.getPeriodFromIsoString("201801");
        Period q12018 = PeriodType.getPeriodFromIsoString("2018Q1");
        Period y2018 = PeriodType.getPeriodFromIsoString("2018");
        Period fyApril2018 = PeriodType.getPeriodFromIsoString("2018April");

        int inxMonthly = PeriodType.PERIOD_TYPES.indexOf(new MonthlyPeriodType());
        int inxQuarterly = PeriodType.PERIOD_TYPES.indexOf(new QuarterlyPeriodType());
        int inxYearly = PeriodType.PERIOD_TYPES.indexOf(new YearlyPeriodType());
        int inxFinancialApril = PeriodType.PERIOD_TYPES.indexOf(new FinancialAprilPeriodType());

        List<Period> periods = PeriodType.getPeriodTypePeriods(jan2018, calendar);

        assertThat(jan2018).isEqualTo(periods.get(inxMonthly));
        assertThat(q12018).isEqualTo(periods.get(inxQuarterly));
        assertThat(y2018).isEqualTo(periods.get(inxYearly));

        periods = PeriodType.getPeriodTypePeriods(y2018, calendar);

        assertThat(periods.get(inxMonthly)).isNull();
        assertThat(periods.get(inxQuarterly)).isNull();
        assertThat(y2018).isEqualTo(periods.get(inxYearly));
        assertThat(periods.get(inxFinancialApril)).isNull();

        periods = PeriodType.getPeriodTypePeriods(fyApril2018, calendar);

        assertThat(periods.get(inxMonthly)).isNull();
        assertThat(periods.get(inxQuarterly)).isNull();
        assertThat(periods.get(inxYearly)).isNull();
        assertThat(fyApril2018).isEqualTo(periods.get(inxFinancialApril));
    }
}
