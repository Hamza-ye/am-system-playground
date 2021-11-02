package org.nmcpye.activitiesmanagement.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.nmcpye.activitiesmanagement.web.rest.TestUtil;

class LLINSFamilyReportHistoryTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(LLINSFamilyReportHistory.class);
        LLINSFamilyReportHistory lLINSFamilyReportHistory1 = new LLINSFamilyReportHistory();
        lLINSFamilyReportHistory1.setId(1L);
        LLINSFamilyReportHistory lLINSFamilyReportHistory2 = new LLINSFamilyReportHistory();
        lLINSFamilyReportHistory2.setId(lLINSFamilyReportHistory1.getId());
        assertThat(lLINSFamilyReportHistory1).isEqualTo(lLINSFamilyReportHistory2);
        lLINSFamilyReportHistory2.setId(2L);
        assertThat(lLINSFamilyReportHistory1).isNotEqualTo(lLINSFamilyReportHistory2);
        lLINSFamilyReportHistory1.setId(null);
        assertThat(lLINSFamilyReportHistory1).isNotEqualTo(lLINSFamilyReportHistory2);
    }
}
