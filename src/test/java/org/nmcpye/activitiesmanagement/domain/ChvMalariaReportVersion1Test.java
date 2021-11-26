package org.nmcpye.activitiesmanagement.domain;

import org.junit.jupiter.api.Test;
import org.nmcpye.activitiesmanagement.web.rest.TestUtil;

import static org.assertj.core.api.Assertions.assertThat;

class ChvMalariaReportVersion1Test {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ChvMalariaReportVersion1.class);
        ChvMalariaReportVersion1 chvMalariaReportVersion11 = new ChvMalariaReportVersion1();
        chvMalariaReportVersion11.setId(1L);
        ChvMalariaReportVersion1 chvMalariaReportVersion12 = new ChvMalariaReportVersion1();
        chvMalariaReportVersion12.setId(chvMalariaReportVersion11.getId());
        assertThat(chvMalariaReportVersion11).isEqualTo(chvMalariaReportVersion12);
        chvMalariaReportVersion12.setId(2L);
        assertThat(chvMalariaReportVersion11).isNotEqualTo(chvMalariaReportVersion12);
        chvMalariaReportVersion11.setId(null);
        assertThat(chvMalariaReportVersion11).isNotEqualTo(chvMalariaReportVersion12);
    }
}
