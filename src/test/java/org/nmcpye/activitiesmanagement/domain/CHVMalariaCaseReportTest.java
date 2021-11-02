package org.nmcpye.activitiesmanagement.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.nmcpye.activitiesmanagement.web.rest.TestUtil;

class CHVMalariaCaseReportTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CHVMalariaCaseReport.class);
        CHVMalariaCaseReport cHVMalariaCaseReport1 = new CHVMalariaCaseReport();
        cHVMalariaCaseReport1.setId(1L);
        CHVMalariaCaseReport cHVMalariaCaseReport2 = new CHVMalariaCaseReport();
        cHVMalariaCaseReport2.setId(cHVMalariaCaseReport1.getId());
        assertThat(cHVMalariaCaseReport1).isEqualTo(cHVMalariaCaseReport2);
        cHVMalariaCaseReport2.setId(2L);
        assertThat(cHVMalariaCaseReport1).isNotEqualTo(cHVMalariaCaseReport2);
        cHVMalariaCaseReport1.setId(null);
        assertThat(cHVMalariaCaseReport1).isNotEqualTo(cHVMalariaCaseReport2);
    }
}
