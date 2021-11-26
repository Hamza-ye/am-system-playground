package org.nmcpye.activitiesmanagement.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.nmcpye.activitiesmanagement.web.rest.TestUtil;

class LlinsVillageReportHistoryTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(LlinsVillageReportHistory.class);
        LlinsVillageReportHistory lLINSVillageReportHistory1 = new LlinsVillageReportHistory();
        lLINSVillageReportHistory1.setId(1L);
        LlinsVillageReportHistory lLINSVillageReportHistory2 = new LlinsVillageReportHistory();
        lLINSVillageReportHistory2.setId(lLINSVillageReportHistory1.getId());
        assertThat(lLINSVillageReportHistory1).isEqualTo(lLINSVillageReportHistory2);
        lLINSVillageReportHistory2.setId(2L);
        assertThat(lLINSVillageReportHistory1).isNotEqualTo(lLINSVillageReportHistory2);
        lLINSVillageReportHistory1.setId(null);
        assertThat(lLINSVillageReportHistory1).isNotEqualTo(lLINSVillageReportHistory2);
    }
}
