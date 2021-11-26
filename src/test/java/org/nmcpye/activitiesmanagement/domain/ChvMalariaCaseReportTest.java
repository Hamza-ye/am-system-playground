package org.nmcpye.activitiesmanagement.domain;

import org.junit.jupiter.api.Test;
import org.nmcpye.activitiesmanagement.web.rest.TestUtil;

import static org.assertj.core.api.Assertions.assertThat;

class ChvMalariaCaseReportTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ChvMalariaCaseReport.class);
        ChvMalariaCaseReport chvMalariaCaseReport1 = new ChvMalariaCaseReport();
        chvMalariaCaseReport1.setId(1L);
        ChvMalariaCaseReport chvMalariaCaseReport2 = new ChvMalariaCaseReport();
        chvMalariaCaseReport2.setId(chvMalariaCaseReport1.getId());
        assertThat(chvMalariaCaseReport1).isEqualTo(chvMalariaCaseReport2);
        chvMalariaCaseReport2.setId(2L);
        assertThat(chvMalariaCaseReport1).isNotEqualTo(chvMalariaCaseReport2);
        chvMalariaCaseReport1.setId(null);
        assertThat(chvMalariaCaseReport1).isNotEqualTo(chvMalariaCaseReport2);
    }
}
