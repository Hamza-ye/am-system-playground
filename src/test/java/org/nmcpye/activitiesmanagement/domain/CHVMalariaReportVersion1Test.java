package org.nmcpye.activitiesmanagement.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.nmcpye.activitiesmanagement.web.rest.TestUtil;

class CHVMalariaReportVersion1Test {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CHVMalariaReportVersion1.class);
        CHVMalariaReportVersion1 cHVMalariaReportVersion11 = new CHVMalariaReportVersion1();
        cHVMalariaReportVersion11.setId(1L);
        CHVMalariaReportVersion1 cHVMalariaReportVersion12 = new CHVMalariaReportVersion1();
        cHVMalariaReportVersion12.setId(cHVMalariaReportVersion11.getId());
        assertThat(cHVMalariaReportVersion11).isEqualTo(cHVMalariaReportVersion12);
        cHVMalariaReportVersion12.setId(2L);
        assertThat(cHVMalariaReportVersion11).isNotEqualTo(cHVMalariaReportVersion12);
        cHVMalariaReportVersion11.setId(null);
        assertThat(cHVMalariaReportVersion11).isNotEqualTo(cHVMalariaReportVersion12);
    }
}
