package org.nmcpye.activitiesmanagement.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.nmcpye.activitiesmanagement.web.rest.TestUtil;

class RelativePeriodsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(RelativePeriods.class);
        RelativePeriods relativePeriods1 = new RelativePeriods();
        relativePeriods1.setId(1L);
        RelativePeriods relativePeriods2 = new RelativePeriods();
        relativePeriods2.setId(relativePeriods1.getId());
        assertThat(relativePeriods1).isEqualTo(relativePeriods2);
        relativePeriods2.setId(2L);
        assertThat(relativePeriods1).isNotEqualTo(relativePeriods2);
        relativePeriods1.setId(null);
        assertThat(relativePeriods1).isNotEqualTo(relativePeriods2);
    }
}
