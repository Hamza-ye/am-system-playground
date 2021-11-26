package org.nmcpye.activitiesmanagement.domain;

import org.junit.jupiter.api.Test;
import org.nmcpye.activitiesmanagement.web.rest.TestUtil;

import static org.assertj.core.api.Assertions.assertThat;

class LlinsVillageReportHistoryTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(LlinsVillageReportHistory.class);
        LlinsVillageReportHistory llinsVillageReportHistory1 = new LlinsVillageReportHistory();
        llinsVillageReportHistory1.setId(1L);
        LlinsVillageReportHistory llinsVillageReportHistory2 = new LlinsVillageReportHistory();
        llinsVillageReportHistory2.setId(llinsVillageReportHistory1.getId());
        assertThat(llinsVillageReportHistory1).isEqualTo(llinsVillageReportHistory2);
        llinsVillageReportHistory2.setId(2L);
        assertThat(llinsVillageReportHistory1).isNotEqualTo(llinsVillageReportHistory2);
        llinsVillageReportHistory1.setId(null);
        assertThat(llinsVillageReportHistory1).isNotEqualTo(llinsVillageReportHistory2);
    }
}
