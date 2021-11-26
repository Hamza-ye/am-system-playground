package org.nmcpye.activitiesmanagement.domain;

import org.junit.jupiter.api.Test;
import org.nmcpye.activitiesmanagement.web.rest.TestUtil;

import static org.assertj.core.api.Assertions.assertThat;

class LlinsVillageReportTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(LlinsVillageReport.class);
        LlinsVillageReport llinsVillageReport1 = new LlinsVillageReport();
        llinsVillageReport1.setId(1L);
        LlinsVillageReport llinsVillageReport2 = new LlinsVillageReport();
        llinsVillageReport2.setId(llinsVillageReport1.getId());
        assertThat(llinsVillageReport1).isEqualTo(llinsVillageReport2);
        llinsVillageReport2.setId(2L);
        assertThat(llinsVillageReport1).isNotEqualTo(llinsVillageReport2);
        llinsVillageReport1.setId(null);
        assertThat(llinsVillageReport1).isNotEqualTo(llinsVillageReport2);
    }
}
