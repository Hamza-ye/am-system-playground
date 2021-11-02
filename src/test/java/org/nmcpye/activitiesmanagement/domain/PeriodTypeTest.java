package org.nmcpye.activitiesmanagement.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.nmcpye.activitiesmanagement.web.rest.TestUtil;

class PeriodTypeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PeriodType.class);
        PeriodType periodType1 = new PeriodType();
        periodType1.setId(1L);
        PeriodType periodType2 = new PeriodType();
        periodType2.setId(periodType1.getId());
        assertThat(periodType1).isEqualTo(periodType2);
        periodType2.setId(2L);
        assertThat(periodType1).isNotEqualTo(periodType2);
        periodType1.setId(null);
        assertThat(periodType1).isNotEqualTo(periodType2);
    }
}
