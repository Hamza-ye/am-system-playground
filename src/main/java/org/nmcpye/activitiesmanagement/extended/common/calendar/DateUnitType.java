package org.nmcpye.activitiesmanagement.extended.common.calendar;

import org.nmcpye.activitiesmanagement.domain.period.*;

public enum DateUnitType {
    DAILY(DailyPeriodType.NAME, "\\b(\\d{4})(\\d{2})(\\d{2})\\b"),
    WEEKLY(WeeklyPeriodType.NAME, "\\b(\\d{4})W(\\d[\\d]?)\\b"),
    WEEKLY_WEDNESDAY(WeeklyWednesdayPeriodType.NAME, "\\b(\\d{4})WedW(\\d[\\d]?)\\b"),
    WEEKLY_THURSDAY(WeeklyThursdayPeriodType.NAME, "\\b(\\d{4})ThuW(\\d[\\d]?)\\b"),
    WEEKLY_SATURDAY(WeeklySaturdayPeriodType.NAME, "\\b(\\d{4})SatW(\\d[\\d]?)\\b"),
    WEEKLY_SUNDAY(WeeklySundayPeriodType.NAME, "\\b(\\d{4})SunW(\\d[\\d]?)\\b"),
    BI_WEEKLY(BiWeeklyPeriodType.NAME, "\\b(\\d{4})BiW(\\d[\\d]?)\\b"),
    MONTHLY(MonthlyPeriodType.NAME, "\\b(\\d{4})[-]?(\\d{2})\\b"),
    BI_MONTHLY(BiMonthlyPeriodType.NAME, "\\b(\\d{4})(\\d{2})B\\b"),
    QUARTERLY(QuarterlyPeriodType.NAME, "\\b(\\d{4})Q(\\d)\\b"),
    SIX_MONTHLY(SixMonthlyPeriodType.NAME, "\\b(\\d{4})S(\\d)\\b"),
    SIX_MONTHLY_APRIL(SixMonthlyAprilPeriodType.NAME, "\\b(\\d{4})AprilS(\\d)\\b"),
    YEARLY(YearlyPeriodType.NAME, "\\b(\\d{4})\\b"),
    FINANCIAL_APRIL(FinancialAprilPeriodType.NAME, "\\b(\\d{4})April\\b"),
    FINANCIAL_JULY(FinancialJulyPeriodType.NAME, "\\b(\\d{4})July\\b"),
    FINANCIAL_OCTOBER(FinancialOctoberPeriodType.NAME, "\\b(\\d{4})Oct\\b");

    private final String name;

    private final String pattern;

    DateUnitType(String name, String pattern) {
        this.name = name;
        this.pattern = pattern;
    }

    public static DateUnitType find(String pattern) {
        for (DateUnitType type : DateUnitType.values()) {
            if (pattern.matches(type.pattern)) {
                return type;
            }
        }

        return null;
    }

    public String getName() {
        return name;
    }

    public String getPattern() {
        return pattern;
    }
}
