package org.nmcpye.activitiesmanagement.domain;

import org.junit.jupiter.api.Test;
import org.nmcpye.activitiesmanagement.web.rest.TestUtil;

import static org.assertj.core.api.Assertions.assertThat;

class LlinsFamilyReportHistoryTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(LlinsFamilyReportHistory.class);
        LlinsFamilyReportHistory llinsFamilyReportHistory1 = new LlinsFamilyReportHistory();
        llinsFamilyReportHistory1.setId(1L);
        LlinsFamilyReportHistory llinsFamilyReportHistory2 = new LlinsFamilyReportHistory();
        llinsFamilyReportHistory2.setId(llinsFamilyReportHistory1.getId());
        assertThat(llinsFamilyReportHistory1).isEqualTo(llinsFamilyReportHistory2);
        llinsFamilyReportHistory2.setId(2L);
        assertThat(llinsFamilyReportHistory1).isNotEqualTo(llinsFamilyReportHistory2);
        llinsFamilyReportHistory1.setId(null);
        assertThat(llinsFamilyReportHistory1).isNotEqualTo(llinsFamilyReportHistory2);
    }
}
