package org.nmcpye.activitiesmanagement.domain;

import org.junit.jupiter.api.Test;
import org.nmcpye.activitiesmanagement.web.rest.TestUtil;

import static org.assertj.core.api.Assertions.assertThat;

class LlinsFamilyReportTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(LlinsFamilyReport.class);
        LlinsFamilyReport llinsFamilyReport1 = new LlinsFamilyReport();
        llinsFamilyReport1.setId(1L);
        LlinsFamilyReport llinsFamilyReport2 = new LlinsFamilyReport();
        llinsFamilyReport2.setId(llinsFamilyReport1.getId());
        assertThat(llinsFamilyReport1).isEqualTo(llinsFamilyReport2);
        llinsFamilyReport2.setId(2L);
        assertThat(llinsFamilyReport1).isNotEqualTo(llinsFamilyReport2);
        llinsFamilyReport1.setId(null);
        assertThat(llinsFamilyReport1).isNotEqualTo(llinsFamilyReport2);
    }
}
