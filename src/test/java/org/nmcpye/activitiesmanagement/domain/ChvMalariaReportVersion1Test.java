package org.nmcpye.activitiesmanagement.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.nmcpye.activitiesmanagement.web.rest.TestUtil;

class ChvMalariaReportVersion1Test {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ChvMalariaReportVersion1.class);
        ChvMalariaReportVersion1 cHVMalariaReportVersion11 = new ChvMalariaReportVersion1();
        cHVMalariaReportVersion11.setId(1L);
        ChvMalariaReportVersion1 cHVMalariaReportVersion12 = new ChvMalariaReportVersion1();
        cHVMalariaReportVersion12.setId(cHVMalariaReportVersion11.getId());
        assertThat(cHVMalariaReportVersion11).isEqualTo(cHVMalariaReportVersion12);
        cHVMalariaReportVersion12.setId(2L);
        assertThat(cHVMalariaReportVersion11).isNotEqualTo(cHVMalariaReportVersion12);
        cHVMalariaReportVersion11.setId(null);
        assertThat(cHVMalariaReportVersion11).isNotEqualTo(cHVMalariaReportVersion12);
    }
}
