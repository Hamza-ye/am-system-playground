package org.nmcpye.activitiesmanagement.extended.mock;

import org.nmcpye.activitiesmanagement.domain.period.Period;
import org.nmcpye.activitiesmanagement.extended.i18n.I18nFormat;

import java.util.Date;

public class MockI18nFormat extends I18nFormat {

    public MockI18nFormat() {
        super(null);
    }

    @Override
    public String formatPeriod(Period period) {
        String name = period.getStartDate() + "-" + period.getEndDate();

        return name.toLowerCase().trim();
    }

    @Override
    public String formatDate(Date date) {
        return date.toString().toLowerCase().trim();
    }
}
