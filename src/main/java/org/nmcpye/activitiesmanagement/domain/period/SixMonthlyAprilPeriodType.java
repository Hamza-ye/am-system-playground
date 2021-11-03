package org.nmcpye.activitiesmanagement.domain.period;

import org.joda.time.DateTimeConstants;
import org.nmcpye.activitiesmanagement.extended.common.calendar.Calendar;
import org.nmcpye.activitiesmanagement.extended.common.calendar.DateTimeUnit;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * PeriodType for six-monthly Periods aligned to a financial year starting in April or October. A
 * valid April six-monthly Period has startDate set to either April 1st or October 1st, and endDate
 * set to the last day of the fifth month after the startDate.
 *
 */
@Entity //(name = "SixMonthlyAprilPeriodType")
@DiscriminatorValue("SixMonthlyApril")
public class SixMonthlyAprilPeriodType extends SixMonthlyAbstractPeriodType {

    /**
     * The name of the SixMonthlyPeriodType, which is "SixMonthly".
     */
    public static final String NAME = "SixMonthlyApril";
    private static final long serialVersionUID = -2770872821413382644L;
    private static final String ISO_FORMAT = "yyyyAprilSn";
    private static final String ISO8601_DURATION = "P6M";
    private static final int BASE_MONTH = DateTimeConstants.APRIL;

    // -------------------------------------------------------------------------
    // PeriodType functionality
    // -------------------------------------------------------------------------

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public int getBaseMonth() {
        return BASE_MONTH;
    }

    // -------------------------------------------------------------------------
    // CalendarPeriodType functionality
    // -------------------------------------------------------------------------

    @Override
    public String getIsoDate(DateTimeUnit dateTimeUnit, Calendar calendar) {
        int month = dateTimeUnit.getMonth();

        if (dateTimeUnit.isIso8601()) {
            month = calendar.fromIso(dateTimeUnit).getMonth();
        }

        switch (month) {
            case 4:
                return dateTimeUnit.getYear() + "AprilS1";
            case 10:
                return dateTimeUnit.getYear() + "AprilS2";
            default:
                throw new IllegalArgumentException("Month not valid [4,10]");
        }
    }

    /**
     * n refers to the semester, can be [1-2].
     */
    @Override
    public String getIsoFormat() {
        return ISO_FORMAT;
    }

    @Override
    public String getIso8601Duration() {
        return ISO8601_DURATION;
    }
}
