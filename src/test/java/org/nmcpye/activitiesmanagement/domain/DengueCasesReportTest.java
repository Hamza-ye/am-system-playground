package org.nmcpye.activitiesmanagement.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.nmcpye.activitiesmanagement.domain.dataset.DengueCasesReport;
import org.nmcpye.activitiesmanagement.web.rest.TestUtil;

class DengueCasesReportTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DengueCasesReport.class);
        DengueCasesReport dengueCasesReport1 = new DengueCasesReport();
        dengueCasesReport1.setId(1L);
        DengueCasesReport dengueCasesReport2 = new DengueCasesReport();
        dengueCasesReport2.setId(dengueCasesReport1.getId());
        assertThat(dengueCasesReport1).isEqualTo(dengueCasesReport2);
        dengueCasesReport2.setId(2L);
        assertThat(dengueCasesReport1).isNotEqualTo(dengueCasesReport2);
        dengueCasesReport1.setId(null);
        assertThat(dengueCasesReport1).isNotEqualTo(dengueCasesReport2);
    }
}
