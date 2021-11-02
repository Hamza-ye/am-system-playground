package org.nmcpye.activitiesmanagement.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.nmcpye.activitiesmanagement.web.rest.TestUtil;

class LLINSFamilyReportTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(LLINSFamilyReport.class);
        LLINSFamilyReport lLINSFamilyReport1 = new LLINSFamilyReport();
        lLINSFamilyReport1.setId(1L);
        LLINSFamilyReport lLINSFamilyReport2 = new LLINSFamilyReport();
        lLINSFamilyReport2.setId(lLINSFamilyReport1.getId());
        assertThat(lLINSFamilyReport1).isEqualTo(lLINSFamilyReport2);
        lLINSFamilyReport2.setId(2L);
        assertThat(lLINSFamilyReport1).isNotEqualTo(lLINSFamilyReport2);
        lLINSFamilyReport1.setId(null);
        assertThat(lLINSFamilyReport1).isNotEqualTo(lLINSFamilyReport2);
    }
}
