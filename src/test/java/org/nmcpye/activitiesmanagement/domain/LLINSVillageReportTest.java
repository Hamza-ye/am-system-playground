package org.nmcpye.activitiesmanagement.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.nmcpye.activitiesmanagement.web.rest.TestUtil;

class LLINSVillageReportTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(LLINSVillageReport.class);
        LLINSVillageReport lLINSVillageReport1 = new LLINSVillageReport();
        lLINSVillageReport1.setId(1L);
        LLINSVillageReport lLINSVillageReport2 = new LLINSVillageReport();
        lLINSVillageReport2.setId(lLINSVillageReport1.getId());
        assertThat(lLINSVillageReport1).isEqualTo(lLINSVillageReport2);
        lLINSVillageReport2.setId(2L);
        assertThat(lLINSVillageReport1).isNotEqualTo(lLINSVillageReport2);
        lLINSVillageReport1.setId(null);
        assertThat(lLINSVillageReport1).isNotEqualTo(lLINSVillageReport2);
    }
}
