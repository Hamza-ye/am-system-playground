package org.nmcpye.activitiesmanagement.extended.domain.period.comparator;

import com.google.common.collect.Lists;
import org.junit.jupiter.api.Test;
import org.nmcpye.activitiesmanagement.domain.period.MonthlyPeriodType;
import org.nmcpye.activitiesmanagement.domain.period.Period;
import org.nmcpye.activitiesmanagement.extended.period.comparator.DescendingPeriodComparator;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DescendingPeriodComparatorTest {

    @Test
    void testSort() {
        Period m03 = MonthlyPeriodType.getPeriodFromIsoString("201603");
        Period m04 = MonthlyPeriodType.getPeriodFromIsoString("201604");
        Period m05 = MonthlyPeriodType.getPeriodFromIsoString("201605");
        Period m06 = MonthlyPeriodType.getPeriodFromIsoString("201606");

        List<Period> periods = Lists.newArrayList(m04, m03, m06, m05);
        List<Period> expected = Lists.newArrayList(m06, m05, m04, m03);

        List<Period> sortedPeriods = periods.stream().sorted(new DescendingPeriodComparator()).collect(Collectors.toList());

        assertEquals(expected, sortedPeriods);
    }

    @Test
    void testMin() {
        Period m03 = MonthlyPeriodType.getPeriodFromIsoString("201603");
        Period m04 = MonthlyPeriodType.getPeriodFromIsoString("201604");
        Period m05 = MonthlyPeriodType.getPeriodFromIsoString("201605");
        Period m06 = MonthlyPeriodType.getPeriodFromIsoString("201606");

        List<Period> periods = Lists.newArrayList(m04, m03, m06, m05);

        Optional<Period> latest = periods.stream().min(DescendingPeriodComparator.INSTANCE);

        assertEquals(m06, latest.get());
    }
}
