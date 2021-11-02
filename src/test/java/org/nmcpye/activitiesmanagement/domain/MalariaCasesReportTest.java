package org.nmcpye.activitiesmanagement.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.nmcpye.activitiesmanagement.web.rest.TestUtil;

class MalariaCasesReportTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MalariaCasesReport.class);
        MalariaCasesReport malariaCasesReport1 = new MalariaCasesReport();
        malariaCasesReport1.setId(1L);
        MalariaCasesReport malariaCasesReport2 = new MalariaCasesReport();
        malariaCasesReport2.setId(malariaCasesReport1.getId());
        assertThat(malariaCasesReport1).isEqualTo(malariaCasesReport2);
        malariaCasesReport2.setId(2L);
        assertThat(malariaCasesReport1).isNotEqualTo(malariaCasesReport2);
        malariaCasesReport1.setId(null);
        assertThat(malariaCasesReport1).isNotEqualTo(malariaCasesReport2);
    }
}
