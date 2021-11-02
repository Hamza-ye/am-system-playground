package org.nmcpye.activitiesmanagement.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.nmcpye.activitiesmanagement.web.rest.TestUtil;

class CasesReportClassTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CasesReportClass.class);
        CasesReportClass casesReportClass1 = new CasesReportClass();
        casesReportClass1.setId(1L);
        CasesReportClass casesReportClass2 = new CasesReportClass();
        casesReportClass2.setId(casesReportClass1.getId());
        assertThat(casesReportClass1).isEqualTo(casesReportClass2);
        casesReportClass2.setId(2L);
        assertThat(casesReportClass1).isNotEqualTo(casesReportClass2);
        casesReportClass1.setId(null);
        assertThat(casesReportClass1).isNotEqualTo(casesReportClass2);
    }
}
